package core.utils.report;

import com.google.gson.internal.LinkedTreeMap;
import core.exceptions.ChainScriptFailedException;
import core.sql.SQLManager;
import core.utils.datapool.DataPool;
import core.utils.evaluationManager.EvaluationManager;
import core.utils.lang.LangUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.reporters.DelegatingStoryReporter;
import org.jbehave.core.reporters.NullStoryReporter;
import org.jbehave.core.reporters.StoryReporter;

import java.sql.ResultSet;
import java.util.*;

/**
 * Created by Akarpenko on 17.11.2017.
 */
public class MetaParser extends DelegatingStoryReporter implements ExtendedStoryReporter {

    private static Story _story = null;
    private static Scenario _scenario=null;
    private static String _scenarioShortname;
    private static String _dataset;
    private static String _chainId;
    private static List<String> _chain_depends_on;
    private static ChainScriptDependencyMode _chainScriptDependencyMode;

    public static boolean isScenarioPassed;
    public static ChainScriptFailedException chainScriptFailedException;

    public static Story getCurrentStory() {
        return _story;
    }
    public static Scenario getCurrentScenario() {
        return _scenario;
    }
    public static String getCurrentScenarioShortname() {
        return _scenarioShortname;
    }
    public static String getCurrentDataset() {
        return _dataset;
    }
    public static int getCurrentDatasetIndex(){return exampleIndex;}
    public static String getChainId() {return _chainId;}
    public static List<String> getChainDependsOn() {return _chain_depends_on;}
    public static boolean isChainScript() {return StringUtils.isNotEmpty(_chainId);}
    public static boolean isChainStart() {return isChainScript()&&_chain_depends_on==null;}
    public static boolean isScriptShouldBeSkipped() {return isChainScript()&&chainScriptFailedException!=null;}
    public static ChainScriptDependencyMode getChainScriptDependencyMode() {return _chainScriptDependencyMode; }


    private static int exampleIndex;
    private static int passedExamples;
    private static int failedExamples;

    public MetaParser(StoryReporter... reporters){
        super(reporters);
    }




    @Override
    public void beforeStory(Story story, boolean givenStory) {
        EvaluationManager.variablesMeta.get().clear();
        Meta meta = story.getMeta();
        for (String metaProp : meta.getPropertyNames()) {
            String value = meta.getProperty(metaProp);
            EvaluationManager.variablesMeta.get().put(metaProp,value);
        }
        _story = story;
        super.beforeStory(story, givenStory);
    }

    private void parseVariableDeclarations(Meta meta){
        String variables =meta.getProperty("variables");
        if (StringUtils.isEmpty(variables))
            return;
        String[] variables_array = variables.replace("\r","").replace("\n","").split(";");
        for (String var:variables_array){
            var = var.trim();
            int firstSpaceIndex = var.indexOf(" ");
            if (firstSpaceIndex == -1 || firstSpaceIndex == var.length() - 1) //check that space exists; and that there are symbols exist after a first space
                continue;
            String type = var.substring(0, firstSpaceIndex);
            String[] sameTypeVariables = var.substring(firstSpaceIndex).split(",");
            for(String sameTypeVariable : sameTypeVariables) {
                EvaluationManager.getVariableTypes().put(sameTypeVariable.trim(), type);
            }
        }
    }

    private void parseVariables(Meta meta){
        for (String metaProp : meta.getPropertyNames()) {
            String value = meta.getProperty(metaProp);
            EvaluationManager.variablesMeta.get().put(metaProp, value);
        }
    }
    private void parseMessageDictionary(Meta meta){
        String meta_dictionary=meta.getProperty("message_dictionary");
        System.setProperty("message_dictionary",meta_dictionary);
    }

    private boolean parseOfflineTag(Meta meta){
        String meta_offline=meta.getProperty("offline");
        return StringUtils.isNotEmpty(meta_offline);
    }

    private void parseChainInfo(Meta meta) {
        String chain_id = meta.getProperty("chain_id");
        chainScriptFailedException=null;
        if (StringUtils.isNotEmpty(chain_id)) {
            _chainId = chain_id;
            if (meta.hasProperty("chain_depends_on_all") && meta.hasProperty("chain_depends_on_any_ignoring_datasets")) {
                chainScriptFailedException = new ChainScriptFailedException("Meta parameters @chain_depends_on_all and @chain_depends_on_any_ignoring_datasets are not allowed to be used simultaneously (you can only use one of them)");
            }

            if (meta.hasProperty("chain_depends_on_all")) {
                _chain_depends_on = Arrays.asList(meta.getProperty("chain_depends_on_all").replace(" ", "").split(","));
                _chainScriptDependencyMode=ChainScriptDependencyMode.EACH;
            }
            else
            if (meta.hasProperty("chain_depends_on_any_ignoring_datasets")) {
                _chain_depends_on = Arrays.asList(meta.getProperty("chain_depends_on_any_ignoring_datasets").replace(" ", "").split(","));
                _chainScriptDependencyMode=ChainScriptDependencyMode.ANY_IGNORING_DATASETS;
            }
            else
                _chain_depends_on = null;
        } else {
            _chainId = null;
            _chain_depends_on = null;
        }
    }

    private void parseDatasets(Meta meta){
        String meta_shortname = meta.getProperty("scenario_shortname");

        if (StringUtils.isNotEmpty(meta_shortname)) {
            _scenarioShortname = meta_shortname;
            if (_scenario.getExamplesTable().getRows().size() == 0) {
                startDataset(_scenarioShortname, null);
            } else {
                //иначе пока не делаем ничего, грузить датасет будем в example()
            }
        }
    }

    private void parseDatasetsFromInputFile(Meta meta){}



    private void prepareOutputFile(Meta meta){
       ScriptDataIOProvider.instance.prepareOutputFile(meta);
    }

    private void appendOutputFile(Meta meta){
        ScriptDataIOProvider.instance.appendOutputFile(meta,isScenarioPassed?"PASSED":"FAILED");
    }

    @Override
    public void beforeScenario(String title) {
        exampleIndex = 0;
        passedExamples = 0;
        failedExamples = 0;
        isScenarioPassed = true;
        chainScriptFailedException = null;

        _scenario=_story.getScenarios().stream().filter(x -> x.getTitle().equals(title)).findFirst().get();
        Meta meta = _scenario.getMeta();

        parseVariables(meta);
        parseMessageDictionary(meta);
        parseVariableDeclarations(meta);
        parseChainInfo(meta);

        if (meta.hasProperty("input_file"))
            parseDatasetsFromInputFile(meta);
        else
        if (!parseOfflineTag(meta)) {
           parseDatasets(meta);
        }

        if (meta.hasProperty("output_file"))
            prepareOutputFile(meta);

        super.beforeScenario(title);

    }

    public static String getDatasetPassedStatus(){
        if (failedExamples ==0)
            return String.format("All (%s of %s) datasets passed",passedExamples,passedExamples+failedExamples);
        else
        if (passedExamples ==0)
            return String.format("All (%s of %s) datasets failed",failedExamples,passedExamples+failedExamples);
        else
           return String.format("%s of %s datasets failed",failedExamples,passedExamples+failedExamples);
    }

    @Override
    public void afterScenario() {
        afterExample();
        if (failedExamples ==0)
            Logger.getRootLogger().info(String.format("All (%s of %s) datasets passed",passedExamples,passedExamples+failedExamples));
        else
            if (passedExamples ==0)
                Logger.getRootLogger().error(String.format("All (%s of %s) datasets failed",failedExamples,passedExamples+failedExamples));
        else
                Logger.getRootLogger().error(String.format("%s of %s datasets failed",failedExamples,passedExamples+failedExamples));

        super.afterScenario();
    }

    @Override
    public void example(Map<String, String> tableRow) {
        if (exampleIndex>0) {
            afterExample();
        }

        String datasetID = tableRow.get("dataset");
        _dataset = datasetID;
        isScenarioPassed = true;
        chainScriptFailedException = null;
        if (_scenario.getMeta().hasProperty("input_file"))
            loadDatasetFromInputFile(_scenario.getMeta().getProperty("input_file"),datasetID);
        else
            startDataset(_scenarioShortname, datasetID);
        exampleIndex++;

        super.example(tableRow);
    }

    public void afterExample(){
        if (isChainScript()){
            DataPool.getInstance().switchToSection(_chainId);
            DataPool.getInstance().getPool().put("CHAIN_STATUS_"+_scenarioShortname,isScenarioPassed?"passed":"failed");
            DataPool.getInstance().savePool();
        }

        if (isScenarioPassed)
            passedExamples++;
        else
            failedExamples++;

        appendOutputFile(_scenario.getMeta());

        for (StoryReporter reporter : getDelegates()) {
            if (reporter instanceof ExtendedStoryReporter)
                ((ExtendedStoryReporter) reporter).afterExample();
        }
    }

    private void loadDatasetFromInputFile(String filename,String dataset) {


        EvaluationManager.clearVariables();
        EvaluationManager.scenarioVariablesDB.set(ScriptDataIOProvider.instance.loadDataset(filename,dataset));

        //3. overwrite with values provided by user
        String params_override = System.getProperty("params_override");
        if (StringUtils.isNotEmpty(params_override)) {
            String[] params_split = params_override.split(";");
            for (String param : params_split) {
                String[] split = param.split("=");
                if (split.length>1) {
                    String param_name = split[0];
                    String param_value = split[1];
                    EvaluationManager.scenarioVariablesDB.get().put(param_name, param_value);
                }
            }
        }
    }

    private void startDataset(String shortname,String dataset){
        _dataset = dataset;
        if (getChainId()!=null){
            DataPool.getInstance().switchToSection(getChainId());
            if (isChainStart() || System.getProperty("ignoreChainScriptFailures").equalsIgnoreCase("true")){
                DataPool.getInstance().getPool().put("CHAIN_STATUS_"+_scenarioShortname,"passed");
                DataPool.getInstance().savePool();
            }
            else {
                if (getChainScriptDependencyMode()==ChainScriptDependencyMode.EACH){
                    for(String script:getChainDependsOn()){
                        Object chain_status = DataPool.getInstance().getPool().get("CHAIN_STATUS_"+script);
                        if (chain_status==null){
                            //probably we have to fail here as status of previous script in chain is unknown, so it may have not been executed
                            chainScriptFailedException = new ChainScriptFailedException(String.format("Execution of current script is aborted for dataset '%s', because result of the previously executed script '%s' in chain '%s' with dataset '%s' has not been found in the datapool.", dataset, script, getChainId(), dataset));
                            break;
                        }
                        else
                        if (chain_status.toString().equalsIgnoreCase("failed")){
                            chainScriptFailedException = new ChainScriptFailedException(String.format("Execution of current script is aborted for dataset '%s', because previously executed script '%s' in chain '%s' with dataset '%s' has failed.", dataset, script, getChainId(), dataset));
                            break;
                        }
                        else
                        if (chain_status.toString().equalsIgnoreCase("passed")){

                        }
                    }
                }
                else
                if (getChainScriptDependencyMode()==ChainScriptDependencyMode.ANY_IGNORING_DATASETS){
                    boolean matchingResultFound=false;
                    scriptLoop:
                    for(String script:getChainDependsOn()) {
                        String statusVarName ="CHAIN_STATUS_"+script;
                        LinkedTreeMap<String, Object> sectionWithoutDataset = DataPool.getInstance().switchToSectionWithoutDataset(getChainId());
                        if (sectionWithoutDataset.get(statusVarName)!=null && sectionWithoutDataset.get(statusVarName).toString().equalsIgnoreCase("passed")){
                            matchingResultFound = true;
                            break;
                        }
                        datasetLoop:
                        for (Map.Entry<String,Object> entry: sectionWithoutDataset.entrySet()){
                            if (entry.getValue()instanceof LinkedTreeMap ){
                            LinkedTreeMap<String, Object> datasetSubsection = (LinkedTreeMap<String, Object>)(entry.getValue());

                                if (datasetSubsection.get(statusVarName)!=null && datasetSubsection.get(statusVarName).toString().equalsIgnoreCase("passed")){
                                    matchingResultFound = true;
                                    break scriptLoop;
                                }
                            }
                        }
                    }
                    DataPool.getInstance().switchToSection(getChainId());
                    if (!matchingResultFound){
                        chainScriptFailedException = new ChainScriptFailedException(String.format("Execution of current script is aborted for dataset '%s', because previously executed scripts '%s' in chain '%s' failed on all datasets.",dataset, getCurrentScenario().getMeta().getProperty("chain_depends_on_any_ignoring_datasets"),getChainId()));
                    }
                }

            }

        }
        loadDataset(shortname, dataset);
    }

    private void loadDataset(String shortname,String dataset) {
        String lang = LangUtils.getDefaultLanguage().toString();
        ResultSet resultSet;
        ThreadLocal<Map<Object, Object>> myMap = new ThreadLocal<Map<Object, Object>>() {
            protected HashMap initialValue() {
                return new HashMap<Object, Object>();
            }
        };
        try {
            //1. download default dataset (dataset=null)
            resultSet = SQLManager.getInstance().get().LocalDB.executeQuery(String.format
                    ("SELECT name,value from data JOIN scenarios ON  data.scenario=scenarios.id" +
                                    " where (short_name='%s' or full_name='%s') and isNull(dataset) " +
                                    "and (language='%s' or language='' or isNull(language))",
                            shortname, shortname, lang));
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String value = resultSet.getString("value");
                myMap.get().put(name, value);
            }
            //2. overwrite with values from selected dataset (if exists)
            if (dataset != null) {
                resultSet = SQLManager.getInstance().get().LocalDB.executeQuery(String.format
                        ("SELECT name,value from data JOIN scenarios ON  data.scenario=scenarios.id" +
                                        " where (short_name='%s' or full_name='%s') and dataset='%s' " +
                                        "and (language='%s' or language='' or isNull(language))",
                                shortname, shortname, dataset, lang));
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String value = resultSet.getString("value");
                    myMap.get().put(name, value);
                }
            }
            EvaluationManager.scenarioVariablesDB = myMap;
        } catch (Exception e) {
            Logger.getRootLogger().error(e.toString());
        }
        //3. overwrite with values provided by user
        String params_override = StringEscapeUtils.unescapeJava(System.getProperty("params_override"));
        if (StringUtils.isNotEmpty(params_override)) {
            String[] params_split = params_override.split(";");
            for (String param : params_split) {
                String[] split = param.split("=");
                if (split.length>1) {
                    String param_name = split[0];
                    String param_value = split[1];
                    EvaluationManager.scenarioVariablesDB.get().put(param_name, param_value);
                }
            }
        }
    }
}

enum ChainScriptDependencyMode{EACH,ANY_IGNORING_DATASETS}
