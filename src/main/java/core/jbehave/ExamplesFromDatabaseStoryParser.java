package core.jbehave;

import core.JbehaveRunner;
import core.sql.SQLManager;
import core.utils.report.ScriptDataIOProvider;
import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.model.*;
import org.jbehave.core.parsers.RegexStoryParser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Denis on 07.04.2018.
 */
public class ExamplesFromDatabaseStoryParser extends ExtendableStoryParser {

    @Override
    protected Scenario parseScenario(String scenarioAsText) {
        String scenarioNameForErrorLog = "";
        try{
            String title = scenarioNameForErrorLog = this.findScenarioTitle(scenarioAsText);
            String scenarioWithoutKeyword = StringUtils.removeStart(scenarioAsText, this.keywords.scenario()).trim();
            String scenarioWithoutTitle = StringUtils.removeStart(scenarioWithoutKeyword, title);
            scenarioWithoutTitle = this.startingWithNL(scenarioWithoutTitle);
            Meta meta = this.findScenarioMeta(scenarioWithoutTitle);
            ExamplesTable examplesTable = this.findExamplesTable(scenarioWithoutTitle);

            if (meta.hasProperty("input_file"))
                examplesTable = ScriptDataIOProvider.instance.generateDatasets(meta);
            else
            if (!meta.hasProperty("offline") && StringUtils.isEmpty(examplesTable.asString()))
                examplesTable = generateDatasets(title,meta,System.getProperty("datasetFilter"));

            GivenStories givenStories = this.findScenarioGivenStories(scenarioWithoutTitle);
            if(givenStories.requireParameters()) {
                givenStories.useExamplesTable(examplesTable);
            }

            List steps = this.findSteps(scenarioWithoutTitle);
            return new Scenario(title, meta, givenStories, examplesTable, steps);
        } catch (Throwable throwable){
            JbehaveRunner.getJbehaveRunner().allureJBehaveReporter.logFrameworkErrorToTheAllureReport(throwable, scenarioNameForErrorLog, false);
            throw throwable;
        }
    }

    protected Pattern findingImportSteps(){
        return Pattern.compile("Given IMPORT STEPS \"(.*)\" from \"(.*)\"");
    }

    protected List<String> processImportSteps(String stepAsText){
        Matcher matcher = this.findingImportSteps().matcher(stepAsText);

        if (matcher.find()){
            String storyAsText = new LoadFromClasspath().loadStoryAsText(matcher.group(2));
            Story story = new RegexStoryParser().parseStory(storyAsText);
            return story.getScenarios().stream().filter(x->x.getTitle().equals(matcher.group(1))).findFirst().orElse(null).getSteps();
        }
        else
            return  Arrays.asList(stepAsText);
    }


    protected List<String> findSteps(String stepsAsText) {
        Matcher matcher = this.findingSteps().matcher(stepsAsText);
        ArrayList steps = new ArrayList();

        for(int startAt = 0; matcher.find(startAt); startAt = matcher.start(4)) {
//            steps.add(StringUtils.substringAfter(matcher.group(1), "\n"));
            steps.addAll(processImportSteps(StringUtils.substringAfter(matcher.group(1), "\n")));
        }

        return steps;
    }


    protected ExamplesTable generateDatasets(String title, Meta meta, String datasetFilter) {
        ExamplesTable table = new ExamplesTable("");

        String scenario_shortname=meta.getProperty("scenario_shortname");

        //1. datasetFilter from envprops or command line args has highest priority
        //2. value of default_dataset from DB is used if 1st returns empty filter
        //3. datasetFilter.default from envprops is used as a fallback

        if (StringUtils.isEmpty(datasetFilter) && StringUtils.isNotEmpty(scenario_shortname)) {
            String sqlQuery = String.format("SELECT default_dataset from scenarios where short_name='%s'", scenario_shortname);
            List<Map<String, String>> results = SQLManager.getInstance().get().LocalDB.executeQueryWithResults(sqlQuery);
            if(results.size() == 0){
                throw new RuntimeException(String.format("short_name = '%s' has not been found in the table 'scenarios' of the DB schema '%s' (%s)", scenario_shortname, System.getProperty("testDB.schema"), sqlQuery));
            }
            datasetFilter = results.get(0).get("default_dataset");
            if(datasetFilter != null && datasetFilter.contains(".")){
                throw new RuntimeException(String.format("default_dataset '%s' contains '.' (delimiter between datasets is ',' and not '.'). DB schema = '%s', SQL query = '%s'", datasetFilter, System.getProperty("testDB.schema"), sqlQuery));
            }
        }

        if (StringUtils.isEmpty(datasetFilter))
            datasetFilter = System.getProperty("datasetFilter.default");

        if (datasetFilter.equalsIgnoreCase("none"))
            return table;



        if (StringUtils.isNotEmpty(scenario_shortname)){
            List<Map<String,String>> datasets_all= SQLManager.getInstance().get().LocalDB.executeQueryWithResults(
                    String.format("SELECT distinct dataset FROM data \n" +
                            "where scenario= (select id from scenarios where short_name='%s')" +
                            "and not ISNULL(dataset)",scenario_shortname));
            if (datasets_all.size()==0)
                return table;

            List<String> datasetNames = new ArrayList<>();
            String table_as_string="|dataset|\n";
            for (Map<String,String> dataset:datasets_all){
                if (datasetFilter.equalsIgnoreCase("first_found")){
                    table_as_string+=String.format("|%s|\n",dataset.get("dataset"));
                    break;
                }
                if (datasetMatchesFilter(dataset.get("dataset"),datasetFilter))
                    table_as_string+=String.format("|%s|\n",dataset.get("dataset"));

                datasetNames.add(dataset.get("dataset"));
            }
            if (!table_as_string.equals("|dataset|\n")){
                table=new ExamplesTable(table_as_string);
            } else {

                throw new RuntimeException(String.format("No dataset matches datasetFilter '%s' (all avalable datasets: '%s')", datasetFilter, Arrays.toString(datasetNames.toArray())));
            }
        }

        return table;
    }

    protected boolean datasetMatchesFilter(String dataset,String datasetFilter)
    {
        if (datasetFilter.equalsIgnoreCase("all") || datasetFilter.equals(""))
            return true;
        if (datasetFilter.equalsIgnoreCase("none"))
            return false;

        String[] datasets = datasetFilter.substring(1).split(",");
        switch (datasetFilter.charAt(0)){
            case '+':
                return Arrays.stream(datasets).anyMatch(x->x.equals(dataset));
            case '-':
                return Arrays.stream(datasets).noneMatch(x->x.equals(dataset));
            default:
                throw new RuntimeException(String.format("Incorrect dataset filter format: %s",datasetFilter));
        }
    }

}
