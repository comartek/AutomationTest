package core.utils.report;


import core.annotations.UseFullDesktopScreenshots;
import core.applications.Application;
import core.applications.ApplicationManager;
import core.conditional.Conditional;
import core.exceptions.ChainScriptFailedException;
import core.utils.assertExt.AssertExt;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.internal.AllureStorage;
import io.qameta.allure.model.*;
import io.qameta.allure.util.ResultsUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.model.StoryDuration;
import org.jbehave.core.reporters.NullStoryReporter;
import org.jbehave.core.steps.StepCandidate;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Stream;

import static core.steps.BaseSteps.getActiveApplication;
import static core.steps.common.AssertionSteps.AssertionSubSteps.msgForForcedFailScenarioAfterSoftAssertError;
import static core.utils.evaluationManager.ScriptUtils.metaIgnoreCase;
import static core.utils.lang.LangUtils.getLanguageAsString;
import static core.utils.report.ReportUtils.*;
import static core.utils.report.VideoCaptureUtils.*;

public class AllureJBehaveReporter extends NullStoryReporter implements FrameworkErrorsAllureLogger {
    private static final String MD_5 = "md5";
    private static final String unknownScenarioError = "Unknown Scenario Error/Exception";
    private final AllureLifecycle lifecycle;
    private Story stories;
    private Story lastStory;
    private String scenarioUuid;
    private final Map<String, Status> scenarioStatusStorage;
    private List<String> scenarioFailedMessages;
    private String previousNonAndStep;
    private final LinkedList<String> steps;
    private boolean scenarioExampleReportIsStarted;

    //    exampletable
    Scenario _scenario=null;
    int exampleIndex=0;
//    -------

    public AllureJBehaveReporter() {
        this(Allure.getLifecycle());
    }

    private AllureJBehaveReporter(AllureLifecycle lifecycle) {
        this.scenarioStatusStorage = new HashMap<>();
        this.lifecycle = lifecycle;
        this.scenarioFailedMessages = new LinkedList<>();
        this.previousNonAndStep = null;
        this.steps = new LinkedList<>();
        this.scenarioExampleReportIsStarted = false;
    }

    public synchronized AllureStorage getAllureStorage(){
        try {
            Field f = getLifecycle().getClass().getDeclaredField("storage");
            f.setAccessible(true);
            return (AllureStorage) f.get(getLifecycle()); //IllegalAccessException
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void startStep(String uuid, StepResult result){
        this.getLifecycle().startStep(uuid, result);
        steps.add(uuid);
    }
    public synchronized void stopStep(){
//        String uuid = getAllureStorage().getCurrentStep().get();
        this.getLifecycle().stopStep();
//        steps.remove(uuid);
    }


    public synchronized void stopStep(String uuid){
        this.getLifecycle().stopStep(uuid);
        steps.remove(uuid);
    }

    public synchronized void beforeStory(Story story, boolean givenStory) {
        this.stories = story;
        if(story.getPath().contains("/")){
            this.lastStory = story;
        }
    }

    public synchronized void afterStory(boolean givenStory) {
        this.stories = null;
    }

    public synchronized void beforeScenario(String title) {
        this.scenarioUuid = UUID.randomUUID().toString();
        Story story = (Story) this.stories;
        _scenario=story.getScenarios().stream().filter(x -> x.getTitle().equals(title)).findFirst().get();
        //если в examplesTable пусто то создаем как обычно
        if (_scenario.getExamplesTable().getRows().size()==0)
            startScenarioNoExamples(title);
        else
        {
            //иначе пока не делаем ничего, создавать testresult будем в example()
            exampleIndex = 0;
        }
    }

    @Override
    public synchronized void example(Map<String, String> tableRow) {
        if (exampleIndex>0)
        {
            //закрываем отчет от предыдущего датасета
            finishScenario();
        }
        this.scenarioUuid = UUID.randomUUID().toString();
        String datasetID=tableRow.get("dataset");
        if (datasetID!=null)
            startScenarioWithExamples(_scenario.getTitle(),datasetID);
        else
            startScenarioWithExamples(_scenario.getTitle(),"Example #"+exampleIndex);
        exampleIndex++;
    }

    private synchronized void startScenarioNoExamples(String title)
    {
        try {
            String lang = getLanguageAsString();

            String browser = ApplicationManager.getInstance().get().getApplication(System.getProperty("browser")).drivername;
            title = String.format("%s. ### Browser: %s, language: %s ###", title, browser, lang);
            startScenarioCommonPart(title, null);
        } catch (Throwable throwable) {
            logFrameworkErrorToTheAllureReport(throwable, title, false);
        }
    }

    private synchronized void startScenarioWithExamples(String title,String datasetID)
    {
        try {
            String lang = getLanguageAsString();

            String browser = ApplicationManager.getInstance().get().getApplication(System.getProperty("browser")).drivername;
            title = String.format("%s. ### Dataset: %s, Browser: %s, language: %s ###", title,datasetID, browser, lang);
            startScenarioCommonPart(title, datasetID);
        } catch (Throwable throwable) {
            logFrameworkErrorToTheAllureReport(throwable, title, false);
        }
    }

    private synchronized void startScenarioCommonPart(String title, String datasetID){
        AssertExt.setCountInterceptedErrors(0);
        AssertExt.setSoftAssertModeToDefaultValue();
        scenarioFailedMessages.clear();

        Story story = (Story) this.stories;
        if(this.scenarioUuid == null){
            this.scenarioUuid = UUID.randomUUID().toString();
        }
        String uuid = (String) this.scenarioUuid;
        String fullName = String.format("%s: %s", story.getName(), title);

        Label[] labels = new Label[]{ResultsUtils.createStoryLabel(story.getName()), ResultsUtils.createHostLabel(), ResultsUtils.createThreadLabel()};
        String epic = getScenarioEpic(story);
        String feature = getScenarioFeature(story);
        if (Boolean.valueOf(System.getProperty("allure.separateResultsByBrowser"))) {
            epic = (epic == null ? "" : epic + " - ") + System.getProperty("browser");
        }

        if (epic != null)
            labels = ArrayUtils.add(labels, ResultsUtils.createEpicLabel(epic));
        if (feature != null)
            labels = ArrayUtils.add(labels, ResultsUtils.createFeatureLabel(feature));
        labels = ArrayUtils.add(labels, ResultsUtils.createSeverityLabel(getScenarioSeverityLevel()));

        TestResult result = (new TestResult()).withUuid(uuid).withName(title).withFullName(fullName).withStage(Stage.SCHEDULED).withLabels(labels).withDescriptionHtml(getDescription(story)).withHistoryId(this.md5(fullName));
        this.getLifecycle().scheduleTestCase(result);
        this.getLifecycle().startTestCase(result.getUuid());
        scenarioExampleReportIsStarted = true;

        startScenarioVideoRecordingIfActivated(datasetID);
    }

    private synchronized void startScenarioVideoRecordingIfActivated(String datasetID) {
        if(isScreenCaptureActivated()){
            String storyName = ((Story) this.stories).getName();
            String scenarioName = _scenario.getTitle();
            String replacementText = storyName + (datasetID == null ? "" : " (ds = " + datasetID + ")") + " ---";
            startScreenRecording(replacementText);
            String videoFilePath = getScreenRecordingFilePathAfterRename(getLastScreenRecordFilePath(), replacementText);

            System.out.println("VIDEO RECORDING HAS STARTED: " + videoFilePath);

            //add step to the allure report
            String stepUuid = UUID.randomUUID().toString();
            String stepName = "Video recording has been activated (file path is inside)";
            startStep(stepUuid, (new StepResult()).withName(stepName).withStatus(Status.PASSED));
            ReportUtils.attachMessage("Video file path", videoFilePath);
            if(isCopyMovieIntoTheTmpDirActivated()){
                String tmpFilePath = new File(getTmpVideoFolderPath()).getAbsolutePath() + File.separator + new File(videoFilePath).getName();
                System.out.println("VIDEO WILL BE COPIED INTO THE TMP FOLDER: " + tmpFilePath);
                ReportUtils.attachMessage("Video copy file path (tmp folder)", tmpFilePath);
            }
            stopStep();
        }
    }

    private synchronized SeverityLevel getScenarioSeverityLevel(){
        String severity = "";
        try{
            severity = metaIgnoreCase("severity");
        } catch (RuntimeException ignored){
            //ignored
        }

        switch (severity.trim().toUpperCase()){
            case "":
                return SeverityLevel.NORMAL;
            case "CRITICAL":
                return SeverityLevel.CRITICAL;
            case "BLOCKER":
                return SeverityLevel.BLOCKER;
            case "MINOR":
                return SeverityLevel.MINOR;
            case "TRIVIAL":
                return SeverityLevel.TRIVIAL;
            case "NORMAL":
                return SeverityLevel.NORMAL;
            default:
                throw new RuntimeException("Value '" + severity + "' is not allowed in the scenario meta parameter 'severity'. List of allowed values (case does not matter): BLOCKER, CRITICAL, NORMAL, MINOR, TRIVIAL");
        }
    }

    private synchronized String getScenarioEpic(Story story){
        try{
            return metaIgnoreCase("epic");
        } catch (RuntimeException ignored){
            String[] subpath = story.getPath().split("/");
            return subpath.length > 3 ? subpath[subpath.length - 3] : null;
        }
    }

    private synchronized String getScenarioFeature(Story story){
        try{
            return metaIgnoreCase("feature");
        } catch (RuntimeException ignored){
            String[] subpath = story.getPath().split("/");
            return subpath.length > 2 ? subpath[subpath.length - 2] : null;
        }
    }

    public synchronized void afterScenario() {
        clearBorderedWebElementsList();
        finishScenario();
    }
    private synchronized void finishScenario()
    {
        try{
            if(isScreenCaptureActivated()) {
                stopScreenRecording();
            }

            if(scenarioExampleReportIsStarted){
                String uuid = (String)this.scenarioUuid;
                Status status = (Status)this.scenarioStatusStorage.getOrDefault(uuid, Status.PASSED);
                this.getLifecycle().updateTestCase(uuid, (testResult) -> {
                    testResult.withStatus(status);
                });
                this.scenarioUuid = null;
                this.getLifecycle().stopTestCase(uuid);
                this.getLifecycle().writeTestCase(uuid);
                scenarioExampleReportIsStarted = false;
            }
        } catch (Throwable throwable) {
            logFrameworkErrorToTheAllureReport(throwable, true);
        }
    }

    public synchronized void beforeStep(String step) {
        try{
            AssertExt.clearInterceptedErrorsList();
            AssertExt.getInterceptedErrorsList().add(new ArrayList<>());
            String stepUuid = UUID.randomUUID().toString();

            //TODO it's not a bug it's a feature
            //This works correct for only such mask
            // "When/Then there is the body of method:
            // |field|operation|value|
            // |f1|o1|v1|
            // |f2|o2|v2|"

            step = step.replaceFirst("\\|[\\s\\S]+\\|", "/see table in attachment/");
            if(isScreenCaptureActivated() && isAddVideoTimestampIntoAllureStepsActivated()){
                startStep(stepUuid, (new StepResult()).withName(step + "  [* video timestamp = " + getCurrentRecordTimeStamp() + "]"));
            }else{
                startStep(stepUuid, (new StepResult()).withName(step));
            }

            StepCandidate stepCandidate = findStepCandidate(step, previousNonAndStep);
            UseFullDesktopScreenshots useFullDesktopScreenshotsAnnotation = getUseFullDesktopScreenshotsAnnotation(stepCandidate);
            setFullDesktopScreenshotMode(useFullDesktopScreenshotsAnnotation == null ? null : useFullDesktopScreenshotsAnnotation.value());
            boolean neverTakeScreenshots = step.equals(unknownScenarioError) || stepHasAnnotationNeverTakeAllureScreenshotsForThisStep(stepCandidate);
            setNeverTakeScreenshotsForCurrentStep(neverTakeScreenshots);
            updatePreviousNonAndStep(step);
            attachScreenshotBasedOnScreenshotLogMode(ReportUtils.StepState.BEFORE_STEP);
        } catch (Throwable throwable) {
            //fail fast (kill current story because after an error in beforeStep() method there`s not much sense to execute the whole story)
            logFrameworkErrorToTheAllureReport(throwable, true);
            throw new RuntimeException(throwable);
        }
    }

    private synchronized void updatePreviousNonAndStep(String step){
        if(!step.startsWith("And "))
            previousNonAndStep = step;
    }

    public synchronized void successful(String step) {
        try {
            Conditional conditional = Conditional.getInstance().get();
            if(conditional.stepInsideThenOrElseBlockIsSkipped){
                this.getLifecycle().updateStep((result) -> {
                    result.withStatus(Status.SKIPPED).withName("(CONDITIONAL SKIP) " + step);
                });
                stopStep();
            } else if(conditional.stepInsideIfOrElseIfBlockIsFailed){
                attachScreenshotBasedOnScreenshotLogMode(ReportUtils.StepState.FAILED);
                Throwable cause = conditional.throwableForStepInsideIfOrElseIfBlock.getCause() == null ? conditional.throwableForStepInsideIfOrElseIfBlock : conditional.throwableForStepInsideIfOrElseIfBlock.getCause();
                attachMessage("Stack trace", ExceptionUtils.getStackTrace(cause));
                deactivateBordersOfTheBorderedElements();
                attachPageSource("Active page source");
                this.getLifecycle().updateStep((result) -> {
                    result.withStatus(Status.FAILED).withName("(" + conditional.conditionalBlockState + " FAIL) " + step);
                });
                stopStep();
            } else {
                List<Throwable> interceptedErrorsList = AssertExt.getInterceptedErrorsList().getFirst();
                if (interceptedErrorsList.size() == 0) {
                    this.getLifecycle().updateStep((result) -> {
                        result.withStatus(Status.PASSED);
                    });
                    attachScreenshotBasedOnScreenshotLogMode(ReportUtils.StepState.AFTER_STEP);
                    deactivateBordersOfTheBorderedElements();
                    stopStep();

                    this.updateScenarioStatus(Status.PASSED);
                } else if (interceptedErrorsList.size() == 1) {
                    failed(step, interceptedErrorsList.get(0));
                } else {
                    failedWithSubSteps(step, interceptedErrorsList);
                }
            }
        } catch (Throwable throwable) {
            logFrameworkErrorToTheAllureReport(throwable);
        }
    }

    public synchronized void ignorable(String step) {
        //without custom code (only standard "ignorable" method  code) try catch is redundant...
        //...but it is here in case if someone add custom code here (which can cause an Exception) but forget to add try-catch and logFrameworkErrorToTheAllureReport
        try{
//          process input step name for special reporting steps like Step number and Comment
            if (step.toLowerCase().startsWith("!-- then step "))
            {
                step=step.replace("!-- ","");
                step=step.replaceAll("Then (S|s)tep ","Step ");
            }
            else
            if (step.toLowerCase().startsWith("!-- then comment: "))
            {
                step=step.replace("!-- ","");
                step=step.replaceAll("Then (C|c)omment: ","");
            }
            String stepUuid = UUID.randomUUID().toString();
            startStep(stepUuid, (new StepResult()).withName(step));

            this.getLifecycle().updateStep((result) -> {
                result.withStatus(Status.SKIPPED);
            });
            stopStep();

            this.updateScenarioStatus(Status.SKIPPED);
        } catch (Throwable throwable) {
            logFrameworkErrorToTheAllureReport(throwable);
        }
    }

    public synchronized void pending(String step) {
        //without custom code (only standard "pending" method code) try catch is redundant...
        //...but it is here in case if someone add custom code here (which can cause an Exception) but forget to add try-catch and logFrameworkErrorToTheAllureReport
        try{
            String stepUuid = UUID.randomUUID().toString();
            startStep(stepUuid, (new StepResult()).withName(step));

            this.getLifecycle().updateStep((result) -> {
                result.withStatus(Status.SKIPPED);
            });
            stopStep();

            this.updateScenarioStatus(Status.SKIPPED);
        } catch (Throwable throwable) {
            logFrameworkErrorToTheAllureReport(throwable);
        }
    }

    @Override
    public synchronized void notPerformed(String step) {
        try{
            String stepUuid = UUID.randomUUID().toString();
            startStep(stepUuid, (new StepResult()).withName("(NOT PERFORMED) "+step));

            this.getLifecycle().updateStep((result) -> {
                result.withStatus(Status.SKIPPED);
            });
            stopStep();

            this.updateScenarioStatus(Status.SKIPPED);
        } catch (Throwable throwable) {
            logFrameworkErrorToTheAllureReport(throwable);
        }
    }


    public synchronized void setIsScenarioPassedForAnActiveApplication(boolean isScenarioPassed){
        Application application = ApplicationManager.getInstance().get().ActiveApplication;
        if(application != null){
            application.isScenarioPassed = isScenarioPassed;
        }
        MetaParser.isScenarioPassed = isScenarioPassed;
    }

    public synchronized void failed(String step, Throwable cause) {
        try {
            setIsScenarioPassedForAnActiveApplication(false);

            if (cause.getCause()!=null)
                cause=cause.getCause();
            String causeMessage=cause.toString();
            String causeTrace=ExceptionUtils.getStackTrace(cause);

            Status status = cause instanceof ChainScriptFailedException ? Status.SKIPPED : ResultsUtils.getStatus(cause).orElse(null);
            this.getLifecycle().updateStep((result) -> {
                result.withStatus(status).withStatusDetails(new StatusDetails().withMessage(causeMessage).withTrace(causeTrace));
            });

            //getting Error message from cause if cause.getCause() == null
            String errMessage = cause.getCause() == null ? (cause.getMessage() != null ? cause.getMessage() : cause.toString()) : (cause.getCause().getMessage() != null ? cause.getCause().getMessage() : cause.getCause().toString());
            String errToString = cause.getCause() == null ? cause.toString() : cause.getCause().toString();
            if (!msgForForcedFailScenarioAfterSoftAssertError.equals(errMessage)) {
                String message = String.format("%s \nin step \"%s\"", errToString, step);
                scenarioFailedMessages.add(message);
            }

            this.getLifecycle().updateTestCase((x) -> {
                String testCaseFailedMessage = (scenarioFailedMessages.size() > 1 ? "Multiple failed steps has been detected (" + scenarioFailedMessages.size() + "):\n" + listToStringForReport(scenarioFailedMessages) : scenarioFailedMessages.toString());
                x.withStatusDetails(new StatusDetails().withMessage(testCaseFailedMessage));
            });
            attachScreenshotBasedOnScreenshotLogMode(ReportUtils.StepState.FAILED);
            attachMessage("Stack trace", causeTrace);
            deactivateBordersOfTheBorderedElements();
            attachPageSource("Active page source");

            stopStep();

            this.updateScenarioStatus(status);
        } catch (Throwable throwable) {
            logFrameworkErrorToTheAllureReport(throwable);
        }
        finally {
            Logger.getRootLogger().error("TEST FAILED");
        }
    }

    /*------ custom failed for List<Throwable> --------*/
    public synchronized void failedWithSubSteps (String step, List<Throwable> throwableList) {
//        Exception canceled = null;
        try {
            setIsScenarioPassedForAnActiveApplication(false);

            String allStackTraces = "";
            Status stepStatus = Status.BROKEN;
            List<StepResult> subSteps = new ArrayList<>();
            for (Throwable throwable : throwableList) {
                allStackTraces = allStackTraces + (allStackTraces.equals("") ? "" : "\n\n-----------------------------------------------------\n\n") + ExceptionUtils.getStackTrace(throwable.getCause());
                Status subStepStatus = throwable.getCause() instanceof Error ? Status.FAILED : Status.BROKEN;
                if (subStepStatus == Status.FAILED)
                    stepStatus = subStepStatus;
                StepResult stepResult = new StepResult().withName(throwable.getCause().toString()).withStatus(subStepStatus).withStatusDetails(new StatusDetails().withMessage(throwable.getCause().toString()).withTrace(ExceptionUtils.getStackTrace(throwable.getCause())));
                subSteps.add(stepResult);
            }

            final Status finalStepStatus = stepStatus;
            this.getLifecycle().updateStep((result) -> {
                result.withStatus(finalStepStatus)
//                        .withSteps(subSteps)
                        .withStatusDetails(new StatusDetails().withMessage("Multiple problems"));
            });

            String message = String.format("%s in step \"%s\"", "Multiple problems", step);
            scenarioFailedMessages.add(message);

            this.getLifecycle().updateTestCase((x) -> {
                String testCaseFailedMessage = (scenarioFailedMessages.size() > 1 ? "Multiple failed steps has been detected (" + scenarioFailedMessages.size() + "):\n" + listToStringForReport(scenarioFailedMessages) : scenarioFailedMessages.toString());
                x.withStatusDetails(new StatusDetails().withMessage(testCaseFailedMessage));
            });

            attachScreenshotBasedOnScreenshotLogMode(ReportUtils.StepState.FAILED);
            attachMessage("Stack trace", allStackTraces);
            deactivateBordersOfTheBorderedElements();
            stopStep();

            this.updateScenarioStatus(stepStatus);
        } catch (Throwable throwable) {
            logFrameworkErrorToTheAllureReport(throwable);
        }

    }

    public synchronized AllureLifecycle getLifecycle() {
        return this.lifecycle;
    }

    protected synchronized void updateScenarioStatus(Status passed) {
        String scenarioUuid = (String)this.scenarioUuid;
        this.max((Status)this.scenarioStatusStorage.get(scenarioUuid), passed).ifPresent((status) -> {
            Status var10000 = (Status)this.scenarioStatusStorage.put(scenarioUuid, status);
        });
    }

    private synchronized String md5(String string) {
        return DatatypeConverter.printHexBinary(this.getMessageDigest().digest(string.getBytes(StandardCharsets.UTF_8)));
    }

    private synchronized MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException var2) {
            throw new IllegalStateException("Could not find md5 hashing algorithm", var2);
        }
    }

    private synchronized Optional<Status> max(Status first, Status second) {
        return Stream.of(first, second).filter(Objects::nonNull).min(Enum::compareTo);
    }


    /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * SOMETIMES if you run in debug mode while storyCanceled method is invoked, Allure report might throw an AllureResultsWriteException !!!!
     * Although if you run in the normal Run mode the same code, which causes storyCancelled method call, then no AllureResultsWriteException will be thrown
     * So be aware of that if you want to debug the code which causes storyCancelled (when story timeout is exceeded)
     *
     * Example:
     * 1. set small story timeout in the class JbehaveRunner (embedder ... useStoryTimeouts("1"))
     * 2. create a story with multiple scenarioUuid which would last more then 1 seccond (or use stories from the folder \src\test\resources\stories\Samples\StoryCancelledExamples and set environment.properties => 'suite=**\/StoryCancelled*.story' (!!! delete \))
     * 3. set a breakpoint in this class in afterScenario() method on the line "this.getLifecycle().stopTestCase(uuid);""
     * 4. run a script in debug mode, and each time you stop on the breakpoint just press F9 (resume)
     * 5. you will see an Exception AllureResultsWriteException: Could not write Allure test result (Caused by: java.nio.channels.ClosedByInterruptException) in the console, but Allure reports will not be complete (not all Canceled stories will be there)
     * 6. run the program again in normal mode (Shift + F10)
     * 7. you will see no AllureResultsWriteException in the Console, and in the Allure report all Canceled stories will be present
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * !!!! Unfortunately sometimes (rarely) if story was Cancelled on timeout, AllureResultsWriteException can be thrown even in the normal running mode (but it seem to be very difficult to debug because of the problem stated above) !!!
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
    public synchronized void storyCancelled(Story story, StoryDuration storyDuration) {
        String errMessage = String.format("Story '%s' has been canceled. Duration has exceeded timeout of %s seconds, ", story.getPath(), storyDuration.getTimeoutInSecs());
        logStoryCancelled(errMessage);
    }

    public synchronized void logStoryCancelled(String errMessage){
        try {
            setIsScenarioPassedForAnActiveApplication(false);

            Story story = lastStory;
            String scenarioUuid = (String)this.scenarioUuid;
            if (scenarioUuid == null){
                scenarioUuid = UUID.randomUUID().toString();
                this.scenarioUuid = scenarioUuid;

                //if story is available then we create simple new test result (witch has no extra information unlike beforeScenario() method)
                String scenarioTitle = "";
                String storyName = story.getName();
                String fullName = String.format("%s: %s", storyName, scenarioTitle);
                String[] subpath = story.getPath().split("/");
                String epic = subpath.length > 3 ? subpath[subpath.length - 3] : null;
                String feature = subpath.length > 2 ? subpath[subpath.length - 2] : null;
                if (Boolean.valueOf(System.getProperty("allure.separateResultsByBrowser"))) {
                    epic = (epic == null ? "" : epic + " - ") + System.getProperty("browser");
                }

                Label[] labels = new Label[]{ResultsUtils.createStoryLabel(story.getName()), ResultsUtils.createHostLabel(), ResultsUtils.createThreadLabel()};
                if (epic != null)
                    labels = ArrayUtils.add(labels, ResultsUtils.createEpicLabel(epic));
                if (feature != null)
                    labels = ArrayUtils.add(labels, ResultsUtils.createFeatureLabel(feature));
                TestResult testResult = (new TestResult()).withUuid(scenarioUuid).withName(scenarioTitle).withFullName(fullName).withStage(Stage.SCHEDULED).withLabels(labels).withDescriptionHtml(getDescription(story)).withHistoryId(this.md5(fullName));
                this.getLifecycle().scheduleTestCase(testResult);
                this.getLifecycle().startTestCase(scenarioUuid);
                scenarioExampleReportIsStarted = true;
            }

            Status status = null; //null => Unknown status
//            System.out.println("[CURRENT CONTEXT] before = " + getAllureStorage().getCurrentStep().orElse(""));
            setCurrentStepContext(scenarioUuid);
//            System.out.println("[CURRENT CONTEXT] after = " + getAllureStorage().getCurrentStep().orElse(""));

            String stepUid = UUID.randomUUID().toString();
            StepResult stepResult = (new StepResult()).withStatus(status).withStatusDetails(new StatusDetails().withMessage(errMessage)).withName("Story has been cancelled on the previous step");
            startStep(stepUid, stepResult);
            attachFullDesktopScreenshot("Story is canceled");
            stopStep(stepUid);

            this.getLifecycle().updateTestCase(scenarioUuid, (x) -> {
                x.withStatus(status).withStatusDetails(new StatusDetails().withMessage(errMessage));
            });

            this.updateScenarioStatus(status);
            this.scenarioUuid = null;
            this.getLifecycle().stopTestCase(scenarioUuid);
            this.getLifecycle().writeTestCase(scenarioUuid);
            scenarioExampleReportIsStarted = false;
            if(getActiveApplication() != null){
                getActiveApplication().driver = null;
            }
        } catch(Throwable throwable) {
            logFrameworkErrorToTheAllureReport(throwable, true);
        }
    }

    private synchronized void setCurrentStepContext(String uuid){
        AllureStorage allureStorage = getAllureStorage();
        try {
            Field f = allureStorage.getClass().getDeclaredField("currentStepContext");
            f.setAccessible(true);
            ThreadLocal<LinkedList<String>> currentStepContext = (ThreadLocal<LinkedList<String>>)f.get(allureStorage);
            currentStepContext.set(new LinkedList<>(Arrays.asList(uuid)));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /********************************************************
     * method to log to the allure report Error/Exception which occurred outside of story step (Allure reporting error, or any other framework error that is not a part of a Story step)
     ********************************************************/
    public synchronized void logFrameworkErrorToTheAllureReport(Throwable throwable){
        logFrameworkErrorToTheAllureReport(throwable, "Unknown scenario", false);
    }

    public synchronized void logFrameworkErrorToTheAllureReport(Throwable throwable, boolean performAfterScenarioInTheEnd){
        logFrameworkErrorToTheAllureReport(throwable, "Unknown scenario", performAfterScenarioInTheEnd);
    }

    public synchronized void logFrameworkErrorToTheAllureReport(Throwable throwable, String scenarioTitle, boolean performAfterScenarioInTheEnd){
        System.out.println("[INFO] --- logFrameworkErrorToTheAllureReport ---");
        throwable.printStackTrace(System.out);

        String unknownProblem = "Unknown Allure report problem";
        try{
            setIsScenarioPassedForAnActiveApplication(false);

            AllureStorage allureStorage = getAllureStorage();

            Throwable cause = throwable.getCause() == null ? throwable : throwable.getCause();
            Status status = throwable instanceof AssertionError ? Status.FAILED : Status.BROKEN;
            Story story = lastStory;
            String scenarioUuid = (String) this.scenarioUuid;
            if(scenarioUuid == null){
                scenarioUuid = UUID.randomUUID().toString();
            }

            //getting current Allure report TestResult or creating new TestResult if there is no current test result available
            TestResult testResult = allureStorage.getTestResult(scenarioUuid).orElse(null);
            if(testResult == null){
                if(scenarioTitle == null || scenarioTitle.equals("")){
                    scenarioTitle = "Unknown scenario";
                }

                if(story != null) {
                    //if story is available then we create simple new test result (witch has no extra information unlike beforeScenario() method)
                    String storyName = story.getName();
                    String fullName = String.format("%s: %s", storyName, scenarioTitle);
                    String[] subpath = story.getPath().split("/");
                    String epic = subpath.length > 3 ? subpath[subpath.length - 3] : null;
                    String feature = subpath.length > 2 ? subpath[subpath.length - 2] : null;
                    if (Boolean.valueOf(System.getProperty("allure.separateResultsByBrowser"))) {
                        epic = (epic == null ? "" : epic + " - ") + System.getProperty("browser");
                    }

                    Label[] labels = new Label[]{ResultsUtils.createStoryLabel(story.getName()), ResultsUtils.createHostLabel(), ResultsUtils.createThreadLabel()};
                    if (epic != null)
                        labels = ArrayUtils.add(labels, ResultsUtils.createEpicLabel(epic));
                    if (feature != null)
                        labels = ArrayUtils.add(labels, ResultsUtils.createFeatureLabel(feature));
                    testResult = (new TestResult()).withUuid(scenarioUuid).withName(scenarioTitle).withFullName(fullName).withStage(Stage.SCHEDULED).withLabels(labels).withDescriptionHtml(getDescription(story)).withHistoryId(this.md5(fullName));
                    this.getLifecycle().scheduleTestCase(testResult);
                    this.getLifecycle().startTestCase(scenarioUuid);

                } else {
                    //if story == null it means that an Error/Exception occurred before scenario has started (log the problem and rethrow the error)
                    testResult = (new TestResult()).withUuid(scenarioUuid).withName(scenarioTitle).withStatus(status).withStatusDetails(new StatusDetails().withMessage(cause.toString()).withTrace(ExceptionUtils.getStackTrace(cause))).withDescriptionHtml(getDescription(story));
                    this.getLifecycle().scheduleTestCase(testResult);
                    this.getLifecycle().startTestCase(scenarioUuid);
                    this.getLifecycle().stopTestCase(scenarioUuid);
                    this.getLifecycle().writeTestCase(scenarioUuid);

                    throw new RuntimeException(throwable);
                }
            }

            //getting current Allure StepResult or creating a new one if needed
            StepResult stepResult = getCurrStepResult();
            if(stepResult == null){
                String stepUuid = UUID.randomUUID().toString();
                stepResult = new StepResult();
                startStep(stepUuid, stepResult.withName(unknownScenarioError));
            }

            //setting up step result and test result
            stepResult.withStatus(status).withStatusDetails(new StatusDetails().withMessage(cause.toString()).withTrace(ExceptionUtils.getStackTrace(cause)));
            testResult.withStatusDetails(new StatusDetails().withMessage(String.format("%s in step \"%s\"", cause.toString(), stepResult.getName())).withTrace(ExceptionUtils.getStackTrace(cause))).withDescriptionHtml(getDescription(story));
            attachMessage("Stack trace", ExceptionUtils.getStackTrace(cause));
            stopStep();

            this.updateScenarioStatus(status);

            //simple version of afterScenario() method
            if (performAfterScenarioInTheEnd){
                this.scenarioUuid = null;
                testResult.withStatus(status);
                this.getLifecycle().stopTestCase(scenarioUuid);
                this.getLifecycle().writeTestCase(scenarioUuid);
                scenarioExampleReportIsStarted = false;
            }
        } catch (Throwable throwable2) {
            if(unknownProblem.equals(scenarioTitle)){
                //if unknownProblem text and scenarioTitle text is equal it means that logFrameworkErrorToTheAllureReport method called itself more then once (in order to avoid infinite loop we don`t try to log Allure error this time)
                throw new RuntimeException(throwable2);
            } else {
                logFrameworkErrorToTheAllureReport(throwable2, unknownProblem, performAfterScenarioInTheEnd);
            }
        }
    }

    private synchronized StepResult getCurrStepResult(){
        final StepResult[] res = {null};
        try{
            this.getLifecycle().updateStep((result) -> {
                if(result.getClass().equals(StepResult.class))
                    res[0] = result;
            });
        } catch (IllegalStateException ignored) {
            //do nothing
        }
        return res[0];
    }

    public synchronized TestResult getCurrTestResult(){
        return getAllureStorage().getTestResult(scenarioUuid).get();
    }

    private String getDescription(Story story){
        String node=System.getProperty("jenkins_executing_node");
        String jenkinsBuildUrl=System.getProperty("jenkins_build_url");
        String jenkinsBuildNumber=System.getProperty("jenkins_build_number");
        String description="";
        if (story!=null)
            description = story.getDescription().asString();
        if (StringUtils.isNotEmpty(jenkinsBuildUrl))
            description = String.format("<strong>Build URL: </strong> <a href='%s'>#%s</a><br><p>%s</p>",jenkinsBuildUrl, jenkinsBuildNumber, description);
        if (StringUtils.isNotEmpty(node))
            description = String.format("<strong>Node: </strong> <a href='/computer/%s/'>%s</a><br><p>%s</p>",node, node, description);
        return description;
    }

}
