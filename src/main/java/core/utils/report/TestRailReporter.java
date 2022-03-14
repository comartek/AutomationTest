package core.utils.report;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.*;
import core.utils.datapool.DataPool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.jbehave.core.reporters.NullStoryReporter;

import java.util.*;

import static core.utils.report.MetaParser.isScenarioPassed;

public class TestRailReporter extends NullStoryReporter implements ExtendedStoryReporter{

    private TestRail testRail;
    private int projectID=-1;
    private int runID=-1;


    private String failureMessage;
    private String failedStep;
    private List<Map<String,String>> failedDatasets;

    @Override
    public void beforeScenario(String title) {
        failureMessage = "";
        failedDatasets = new ArrayList<>();
    }

    @Override
    public void afterExample() {
        if (!isScenarioPassed) {
            Map<String, String> failedDataset = new HashMap<>();
            failedDataset.put("failedDataset", MetaParser.getCurrentDataset());
            failedDataset.put("failedStep", failedStep);
            failedDataset.put("failureMessage", failureMessage);
            failedDatasets.add(failedDataset);
        }
    }

    public boolean isReportingEnabled(){
        return "true".equalsIgnoreCase(System.getProperty("testrail.reportingEnabled"))&&
                testRail!=null&&
                projectID!=-1&&
                runID!=-1;
    }

    public TestRailReporter(){
        try {
            testRail = TestRail.builder(
                    System.getProperty("testrail.url"),
                    System.getProperty("testrail.login"),
                    System.getProperty("testrail.password")
            ).build();
            projectID = Integer.valueOf(System.getProperty("testrail.projectID"));

            String runIdExpression = System.getProperty("testrail.runID");
            if (StringUtils.isNumeric(runIdExpression))
                runID = Integer.valueOf(runIdExpression);
            else
                if (runIdExpression.startsWith("name:")){
                    Run run = testRail.runs().list(projectID).execute().stream().filter(x -> x.getName().equals(runIdExpression.replace("name:", ""))).findFirst().orElse(null);
                    if (run!=null)
                        runID=run.getId();
                }
                else
                if (runIdExpression.equals("latest")){
                    Run run = testRail.runs().list(projectID).execute().stream().max(Comparator.comparingInt(Run::getId)).orElse(null);
                    if (run!=null)
                        runID=run.getId();
                }


        }
        catch (Throwable e){
            Logger.getRootLogger().error("Error in TestRail reporter",e);
        }

    }

    @Override
    public void failed(String step, Throwable cause) {
        if (cause.getCause()!=null)
            cause=cause.getCause();
        String causeMessage=cause.toString();
        String causeTrace= ExceptionUtils.getStackTrace(cause);

        failedStep = step;
        failureMessage = causeMessage;
    }

    @Override
    public void afterScenario() {
        try {
            if (isReportingEnabled()) {
                List<CaseField> caseFields = testRail.caseFields().list().execute();
                List<ResultField> resultFields = testRail.resultFields().list().execute();
                Optional<Case> caseOptional = testRail.cases().list(projectID, caseFields).execute().stream().filter(
                        x -> MetaParser.getCurrentScenarioShortname().equals(x.getCustomFields().get("script_id"))).findFirst();

                if (caseOptional.isPresent()) {
                    Case aCase = caseOptional.get();
                    Result result = new Result();

                    //result status
                    if (failedDatasets.size()==0)
                        result.setStatusId(1);
                    else
                        result.setStatusId(5);

                    //result comment


                    result.setComment(getResultComment());

                    testRail.results().addForCase(runID, aCase.getId(), result, resultFields).execute();
                }
            }
        }
        catch (Throwable e){
            Logger.getRootLogger().error("Error in TestRail reporter",e);
        }
    }

    private String getResultComment(){
        String comment="";
        comment=MetaParser.getDatasetPassedStatus()+"\n\n";

        for (Map<String,String> failedDataset:failedDatasets){
            comment = comment+String.format("__Failed dataset: %s__",failedDataset.get("failedDataset")) +"\n\n";
            comment = comment+String.format("Exception in step __%s__:",failedDataset.get("failedStep"))+"\n";
            comment = comment + "`" + failedDataset.get("failureMessage") + "`\n\n";
        }
        String browser=System.getProperty("browser");

        comment = comment+ String.format("__Browser:__\n%s\n", browser);

        String node=System.getProperty("jenkins_executing_node");
        String jenkinsBuildUrl=System.getProperty("jenkins_build_url");
        String jenkinsBuildNumber=System.getProperty("jenkins_build_number");
        if (StringUtils.isNotEmpty(jenkinsBuildUrl))
            comment = comment+String.format("__Build URL:__\n%s\n",jenkinsBuildUrl);
        if (StringUtils.isNotEmpty(node))
            comment = comment+ String.format("__Node:__\n%s\n", node);
        return comment;
    }


}
