package core.utils.report;

import core.annotations.UseFullDesktopScreenshots;
import core.htmlElements.element.ExtendedWebElement;
import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.services.soap.AbstractRequest;
import core.utils.evaluationManager.ScriptUtils;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.*;
import org.monte.media.io.ByteArrayImageInputStream;
import org.monte.media.io.ByteArrayImageOutputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import core.JbehaveRunner;
import org.openqa.selenium.WebDriver;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static core.steps.BaseSteps.getActiveMobileDriver;
import static core.steps.BaseSteps.getCurrentPage;
import static core.utils.evaluationManager.ScriptUtils.env;
import static core.utils.report.MetaParser.getCurrentDataset;
import static core.utils.report.MetaParser.getCurrentScenarioShortname;
import static core.utils.timeout.TimeoutUtils.returnDefaultTimeOut;
import static core.utils.timeout.TimeoutUtils.setTimeOut;
import static core.steps.BaseSteps.getActiveDriver;

/**
 * Created by Akarpenko on 17.11.2017.
 */
public class ReportUtils {

    private static List<ExtendedWebElement> borderedWebElementsList = new LinkedList<>();


    static String table_head_variables = "<html>\n" +
            "  <head>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
            "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css\" integrity=\"sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb\" crossorigin=\"anonymous\">\n" +
            "    </head>\n" +
            "    <body>\n" +
            "      <table class='table table-striped table-bordered table-sm'>\n" +
            "        <thead >\n" +
            "          <tr>\n" +
            "            <th>Value</th>\n" +
            "            <th>Evaluated</th>\n" +
            "          </tr>\n" +
            "        </thead>" +
            "        <tbody>";

    static String table_foot_variables = "</tbody>\n" +
            "      </table>\n" +
            "    </body>\n" +
            "  </html>";

    public static void attachVariables(String message, String[] values, String[] evalValues) {

        if (values.length != evalValues.length)
            return;

        String html = table_head_variables;

        for (int i = 0; i < values.length; i++) {

            html = html + "<tr><td>" + values[i] + "</td><td>" + evalValues[i] + "</td></tr>";
        }

        html = html + table_foot_variables;

        ByteArrayInputStream inStream;
        inStream = new ByteArrayInputStream(html.getBytes());
        Allure.addAttachment(message, inStream);
    }

    public static void attachVariables(String[] values, String[] evalValues) {
        attachVariables("Variables", values, evalValues);
    }

    public static void attachVariables(String message, Map<String, String> map) {

        String html = table_head_variables;

        for (Map.Entry<String, String> entry : map.entrySet()) {
            html = html + "<tr><td>" + entry.getKey() + "</td><td>" + entry.getValue() + "</td></tr>";
        }

        html = html + table_foot_variables;

        ByteArrayInputStream inStream;
        inStream = new ByteArrayInputStream(html.getBytes());
        Allure.addAttachment(message, inStream);
    }


    static String table_head_table = "<html>\n" +
            "  <head>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
            "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css\" integrity=\"sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb\" crossorigin=\"anonymous\">\n" +
            "    </head>\n" +
            "    <body>\n" +
            "      <table class='table table-striped table-bordered table-sm'>\n" +
            "        <thead >\n" +
            "          <tr>\n";
//            "          </tr>\n" +
//            "        </thead>" +
//            "        <tbody>";

    static String table_foot_table = "</tbody>\n" +
            "      </table>\n" +
            "    </body>\n" +
            "  </html>";


    public static void attachExampletable(ExamplesTable table) {

        String html = table_head_table;

        for (String header : table.getHeaders()) {
            html = html + "<th>" + header + "</th>\n";
        }

        html = html + "</tr></thead><tbody>";

        for (Map<String, String> row : table.getRows()) {

            html = html + "<tr>";

            for (Map.Entry<String, String> field : row.entrySet()) {

                html = html + "<td>" + field.getValue() + "</td>";

            }

            html = html + "</tr>";
        }


        html = html + table_foot_table;


        ByteArrayInputStream inStream;
        inStream = new ByteArrayInputStream(html.getBytes());
        Allure.addAttachment("Table", inStream);

    }

    public static String listToStringForReport(List list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < list.size() + 1; i++) {
            sb.append("{").append(i).append("}").append('[').append(list.get(i - 1).toString()).append(']').append(';').append("\n");
        }
        return sb.toString();
    }


    public static void attachMessage(String message) {
        attachMessage("Message", message);
    }

    public static void attachMessage(String attachName, String message) {
        Allure.addAttachment(attachName, message);
        //Allure.getLifecycle().addAttachment(attachName, "text/plain", "txt", message.getBytes(StandardCharsets.UTF_8));
    }

    public static void attachHtml(String attachName, String html) {
        ByteArrayInputStream inStream;
        inStream = new ByteArrayInputStream(html.getBytes());
        Allure.addAttachment(attachName, inStream);
    }

    public static void attachFile(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            Allure.addAttachment(filePath, (String)null, fileInputStream, FilenameUtils.getExtension(filePath));
        } catch (FileNotFoundException e) {
            throw new  RuntimeException(e);
        }
    }

    public static void addBorderedWebElementToTheList(ExtendedWebElement webElement) {
        borderedWebElementsList.add(webElement);
    }

    public static void deactivateBordersOfTheBorderedElements() {
        if (borderedWebElementsList.size() > 0) {
//            setTimeOut(1, TimeUnit.MILLISECONDS);

            for (ExtendedWebElement webElement : borderedWebElementsList) {
                try {
                    webElement.deactivateElementBorder();
                } catch (Exception ignored) {
                    //ignore all exception during deactivation of the elements borders
                }
            }

            clearBorderedWebElementsList();
            returnDefaultTimeOut();
        }
    }

    public static void clearBorderedWebElementsList(){
        borderedWebElementsList.clear();
    }


    public static boolean checkIfCurrentPageIsLoaded_AndAttachAllureMessageIfItsNotLoaded(){
        if(getCurrentPage().isLoaded_NoWaitTimeout()) {
            return true;
        }else{
            String msg = "Page '" + getCurrentPage().getClass().getSimpleName() + "' is not loaded";
            ReportUtils.attachMessage(msg, msg);
            return false;
        }
    }

    /**************************************************************
     *******************   Allure screenshots   *******************
     **************************************************************/
    /*Variables*/
    private static boolean neverTakeScreenshotsForCurrentStep = false;
    private static FullDesktopScreenshotMode fullDesktopScreenshotMode;
    private static List<CandidateSteps> allCandidateSteps;
    private static StepFinder stepFinder;

    /*Enums*/
    public enum ScreenshotLogMode {
        AFTER_EACH_STEP,
        BEFORE_AND_AFTER_EACH_STEP,
        BEFORE_EACH_STEP_AND_ON_ERRORS,
        ONLY_ON_ERRORS,
        ONLY_FORCED,
        BLOCK_ALL_EVEN_FORCED
    }

    public enum StepState {
        BEFORE_STEP,
        AFTER_STEP,
        FAILED
    }

    public enum FullDesktopScreenshotMode{
        ONLY_IF_WEBDRIVER_SCREENSHOT_HAS_FAILED_TO_BE_TAKEN,
        INSTEAD_OF_WEBDRIVER_SCREENSHOT_ALWAYS,
        INSTEAD_OF_WEBDRIVER_SCREENSHOT_ON_FAILED_OR_BROKEN_STEP,
        DO_BOTH_WEBDRIVER_AND_FULL_DESKTOP_SCREENSHOTS
    }


    /* Setters */
    public static void setNeverTakeScreenshotsForCurrentStep(boolean value) {
        neverTakeScreenshotsForCurrentStep = value;
    }
    public static void setFullDesktopScreenshotMode(FullDesktopScreenshotMode mode) {
        fullDesktopScreenshotMode = mode;
    }

    public static void setStepFinder(StepFinder finder) {
        stepFinder = finder;
    }

    public static StepFinder getStepsFinder(){
        if(stepFinder == null){
            stepFinder = JbehaveRunner.getJbehaveRunner().configuration().stepFinder();
        }
        return stepFinder;
    }

    public static List<CandidateSteps> getAllCandidateSteps(){
        if(allCandidateSteps == null){
            allCandidateSteps = JbehaveRunner.getJbehaveRunner().stepsFactory().createCandidateSteps();
        }
        return allCandidateSteps;
    }

    public static boolean stepHasAnnotationNeverTakeAllureScreenshotsForThisStep(StepCandidate step) {
        if (step == null){
            return false;
        }
        return step.getMethod().getAnnotation(NeverTakeAllureScreenshotsForThisStep.class) != null;
    }

    public static UseFullDesktopScreenshots getUseFullDesktopScreenshotsAnnotation(StepCandidate stepCandidate){
        if (stepCandidate == null){
            return null;
        }
        return stepCandidate.getMethod().getAnnotation(UseFullDesktopScreenshots.class);
    }

    public static StepCandidate findStepCandidate(String step, String previousNonAndStep){
        //List<Stepdoc> foundSteps = stepFinder.findMatching(step, allCandidateSteps); DOES NOT WORK WITH "AND" STEPS
        List<StepCandidate> foundSteps;
        try {
            foundSteps = findMatchingExt(step, previousNonAndStep);
        } catch (NullPointerException e) {
            return null;
        }

        if (foundSteps.size() == 0) {
            throw new RuntimeException(String.format("Framework does not have BDD action that matches to step '%s'.", step));
        } else if (foundSteps.size() > 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= foundSteps.size(); i++) {
                sb.append("\n").append("{").append(i).append("}[").append("Pattern = ").append(foundSteps.get(i - 1).getPatternAsString()).append("; Method = ").append(foundSteps.get(i - 1).getMethod()).append("]");
            }
            throw new RuntimeException(String.format("Framework contains more then one BDD action that matches to step '%s'. \nSteps from framework: %s", step, sb));
        } else {
            return foundSteps.get(0);
        }
    }

    //Changed version of method findMatching from class org.jbehave.core.steps.StepFinder
    //... here we use candidate.matches(String step, String previousNonAndStep) insted of candidate.matches(String stepAsString) in the original findMatching method
    private static List<StepCandidate> findMatchingExt(String step, String previousNonAndStep) {
        List<StepCandidate> foundSteps = new LinkedList<>();
        List<StepCandidate> stepCandidateList = getStepsFinder().collectCandidates(getAllCandidateSteps());
        for (StepCandidate stepCandidate : stepCandidateList) {
            if (stepCandidate.matches(step, previousNonAndStep))
                foundSteps.add(stepCandidate);
        }
        return foundSteps;
    }

    /* attachScreenshot */
    public static void attachScreenshotBasedOnScreenshotLogMode(StepState stepState) {
        attachScreenshotBasedOnScreenshotLogMode(stepState, fullDesktopScreenshotMode);
    }

    public static void attachScreenshotBasedOnScreenshotLogMode(StepState stepState, FullDesktopScreenshotMode fullDesktopScreenshotMode) {
        ScreenshotLogMode logMode = getActiveScreenshotsLogMode();
        if (stepState == StepState.BEFORE_STEP) {
            if (logMode == ScreenshotLogMode.BEFORE_AND_AFTER_EACH_STEP || logMode == ScreenshotLogMode.BEFORE_EACH_STEP_AND_ON_ERRORS)
                attachScreenshot(stepState.toString(), stepState, fullDesktopScreenshotMode);
        } else if (stepState == StepState.AFTER_STEP) {
            if (logMode == ScreenshotLogMode.BEFORE_AND_AFTER_EACH_STEP || logMode == ScreenshotLogMode.AFTER_EACH_STEP)
                attachScreenshot(stepState.toString(), stepState, fullDesktopScreenshotMode);
        } else if (stepState == StepState.FAILED) {
            if (logMode == ScreenshotLogMode.ONLY_ON_ERRORS || logMode == ScreenshotLogMode.BEFORE_AND_AFTER_EACH_STEP || logMode == ScreenshotLogMode.AFTER_EACH_STEP || logMode == ScreenshotLogMode.BEFORE_EACH_STEP_AND_ON_ERRORS)
                attachScreenshot(stepState.toString(), stepState, fullDesktopScreenshotMode);
        }
    }

    public static ScreenshotLogMode getActiveScreenshotsLogMode() {
        String propertyName = "allure.screenshotsLogMode";
        String systemLogMode = env(propertyName);
        if (systemLogMode == null)
            throw new RuntimeException("Can`t get system property '" + propertyName + "' (env(propertyName) == null)");

        return ScreenshotLogMode.valueOf(systemLogMode);
    }

    public static void attachScreenshot() {
        attachScreenshot("forced screenshot");
    }

    public static void attachScreenshot(String name) {
        attachScreenshot(name, null, FullDesktopScreenshotMode.ONLY_IF_WEBDRIVER_SCREENSHOT_HAS_FAILED_TO_BE_TAKEN);
    }

    public static void attachScreenshot(String name, StepState stepState, FullDesktopScreenshotMode fullDesktopScreenshotMode) {
        if (getActiveScreenshotsLogMode() != ScreenshotLogMode.BLOCK_ALL_EVEN_FORCED && !neverTakeScreenshotsForCurrentStep) {
            if (fullDesktopScreenshotMode == null) {
                fullDesktopScreenshotMode = FullDesktopScreenshotMode.ONLY_IF_WEBDRIVER_SCREENSHOT_HAS_FAILED_TO_BE_TAKEN;
            }

            boolean takeWebDriverScreenshot = true;
            boolean takeFullDesktopScreenshot = false;
            if (fullDesktopScreenshotMode == FullDesktopScreenshotMode.DO_BOTH_WEBDRIVER_AND_FULL_DESKTOP_SCREENSHOTS) {
                takeWebDriverScreenshot = true;
                takeFullDesktopScreenshot = true;
            } else if (fullDesktopScreenshotMode == FullDesktopScreenshotMode.ONLY_IF_WEBDRIVER_SCREENSHOT_HAS_FAILED_TO_BE_TAKEN) {
                takeWebDriverScreenshot = true;
                takeFullDesktopScreenshot = false;
            } else if (fullDesktopScreenshotMode == FullDesktopScreenshotMode.INSTEAD_OF_WEBDRIVER_SCREENSHOT_ALWAYS) {
                takeWebDriverScreenshot = false;
                takeFullDesktopScreenshot = true;
            } else if ((fullDesktopScreenshotMode == FullDesktopScreenshotMode.INSTEAD_OF_WEBDRIVER_SCREENSHOT_ON_FAILED_OR_BROKEN_STEP) && (stepState == StepState.FAILED)) {
                takeWebDriverScreenshot = false;
                takeFullDesktopScreenshot = true;
            }

            if (stepState == StepState.FAILED && "true".equals(System.getProperty("exportFailedScreenshots"))) {
                ReportUtils.exportWebDriverScreenshot(String.format("FAILED_%s_%s", getCurrentScenarioShortname(), getCurrentDataset()));
            }

            boolean failedToAttachWebDriverScreenshot = false;
            if (takeWebDriverScreenshot) {
                failedToAttachWebDriverScreenshot = !attachWebDriverScreenshot(name);
            }

            takeFullDesktopScreenshot = takeFullDesktopScreenshot || (failedToAttachWebDriverScreenshot && (fullDesktopScreenshotMode == FullDesktopScreenshotMode.ONLY_IF_WEBDRIVER_SCREENSHOT_HAS_FAILED_TO_BE_TAKEN));
            if (takeFullDesktopScreenshot && !ScriptUtils.env("browser").toLowerCase().equals("android")) {
                attachFullDesktopScreenshot(name);
            }
        }
    }

    public static boolean exportWebDriverScreenshot(String name){
        Object activeDriver;
        try {
            if(getActiveMobileDriver() != null){
                activeDriver = getActiveMobileDriver();
            }else{
                activeDriver = getActiveDriver();
            }
        } catch (NullPointerException ex) {
            activeDriver = null;
        }
        if(activeDriver == null){
            //WebDriver Screenshot can only be taken with an existing WebDriver, therefore no driver = no WebDriver screenshot
            return false;
        }


        try {
            File originalScreenshot = ((TakesScreenshot) activeDriver).getScreenshotAs(OutputType.FILE);
            Path path = Paths.get("target/output/screenshots/"+name+".png");
            Files.createDirectories(path.getParent());
            Files.copy(originalScreenshot.toPath(),  path, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            Allure.getLifecycle().updateStep((result) -> {
                System.out.println(result.toString());
                if (result.getStatus() != Status.FAILED) {
                    result.withStatusDetails(new StatusDetails().withMessage(e.getMessage()).withTrace(ExceptionUtils.getStackTrace(e)));
                }
            });

            attachMessage("Failed to take a selenium screenshot", "Failed to take a selenium screenshot:\n\nStack trace:\n%s" + ExceptionUtils.getStackTrace(e));
            return false;
        }
    }


    public static boolean attachWebDriverScreenshot(String name){
        Object activeDriver;
        try {
            if(getActiveMobileDriver() != null){
                activeDriver = getActiveMobileDriver();
            }else{
                activeDriver = getActiveDriver();
            }
        } catch (NullPointerException ex) {
            activeDriver = null;
        }
        if(activeDriver == null){
            //WebDriver Screenshot can only be taken with an existing WebDriver, therefore no driver = no WebDriver screenshot
            return false;
        }


        try {
            byte[] originalScreenshot = ((TakesScreenshot) activeDriver).getScreenshotAs(OutputType.BYTES);
            float quality = Float.valueOf(ScriptUtils.env("allure.screenshotsQuality"));
            Allure.getLifecycle().addAttachment(name, "image/jpeg", "jpg", compressImage(originalScreenshot, "jpg", quality));
            return true;
        } catch (Exception e) {
            Allure.getLifecycle().updateStep((result) -> {
                System.out.println(result.toString());
                if (result.getStatus() != Status.FAILED) {
                    result.withStatusDetails(new StatusDetails().withMessage(e.getMessage()).withTrace(ExceptionUtils.getStackTrace(e)));
                }
            });

            attachMessage("Failed to take a selenium screenshot", "Failed to take a selenium screenshot:\n\nStack trace:\n%s" + ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    public static boolean attachFullDesktopScreenshot(String name){
        try {
            byte[] originalScreenshot = FullDesktopScreenshot.getScreenshotAsBytes();
            float quality = Float.valueOf(env("allure.screenshotsQuality"));
            Allure.getLifecycle().addAttachment(name + " (Full desktop screenshot)", "image/jpeg", "jpg", compressImage(originalScreenshot, "jpg", quality));
            return true;
        } catch (Exception e) {
            Allure.getLifecycle().updateStep((result) -> {
                if (result.getStatus() != Status.FAILED) {
                    result.withStatusDetails(new StatusDetails().withMessage(e.getMessage()).withTrace(ExceptionUtils.getStackTrace(e)));
                }
            });

            attachMessage("Failed to take a full desktop screenshot", "Failed to take a full desktop screenshot:\n\nStack trace:\n%s" + ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    public static byte[] compressImage(byte[] originalImage, String imageFormat, float compressionQuality){
        if(compressionQuality >= 1.0f){
//            System.out.println("[SCREENSHOT COMPRESSION] compression will not be performed because compression quality is 1 or larger (" + compressionQuality + ").");
            return originalImage;
        }

        try {
            long start = System.currentTimeMillis();

            ByteArrayImageInputStream inputStream = new ByteArrayImageInputStream(originalImage);
            BufferedImage image = ImageIO.read(inputStream);
            if (image.getColorModel().hasAlpha()) {
                image = dropAlphaChannel(image);
            }

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(imageFormat);
            ImageWriter writer = (ImageWriter) writers.next();

            ByteArrayImageOutputStream outputStream = new ByteArrayImageOutputStream();
            writer.setOutput(outputStream);

            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(compressionQuality);  // Change the quality value you prefer
            writer.write(null, new IIOImage(image, null, null), param);

            byte[] compressedImage = outputStream.toByteArray();
            outputStream.close();
            writer.dispose();

            long end = System.currentTimeMillis() - start;
            double compressionSizeRatio = compressedImage.length * 1.0 / originalImage.length;
            String msg = "[SCREENSHOT COMPRESSION] compression took " + end + " millis; compression quality = " + compressionQuality + "; compression size ratio = " + compressionSizeRatio;
            if(compressionSizeRatio >= 1.0){
                msg = msg + " (original image will be used, because \"compressed\" image is bigger then original)";
                compressedImage = originalImage;
            }
//            System.out.println(msg);

            return compressedImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage dropAlphaChannel(BufferedImage src) {
        BufferedImage convertedImg = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        convertedImg.getGraphics().drawImage(src, 0, 0, null);
        return convertedImg;
    }

    public static void attachPageSource(String name) {
        try {
            String xml = getActiveDriver().getPageSource();
            attachMessage(name, xml);
        } catch (Throwable ignored) {
            /*do nothing*/
        }
    }

    public static void attachXml(String attachName, String message) {
        Allure.addAttachment(attachName, "text/xml", message, ".xml");
        //Allure.getLifecycle().addAttachment(attachName, "text/plain", "txt", message.getBytes(StandardCharsets.UTF_8));
    }


    public static void attachSOAP(AbstractRequest request) {
        attachXml(request.getClass().getTypeName() + " - Request", request.getRequest());
        attachXml(request.getClass().getTypeName() + " - Response", request.getResponse());
    }


}
