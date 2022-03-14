package core;

import com.google.common.base.Strings;
import core.jbehave.ExamplesFromDatabaseStoryParser;
import core.utils.report.AllureJBehaveReporter;
import core.utils.report.MetaParser;

import core.utils.report.TestRailReporter;
import core.win32.OsNativeWindowsImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.failures.BatchFailures;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.Story;
import org.jbehave.core.model.StoryDuration;
import org.jbehave.core.reporters.DelegatingStoryReporter;
import org.jbehave.core.reporters.ReportsCount;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.*;
import core.steps.BaseSteps;
import org.junit.After;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CancellationException;


import static core.utils.report.VideoCaptureUtils.stopScreenRecording;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;

/**
 * Created by Akarpenko on 08.11.2017.
 */

public class JbehaveRunner extends JUnitStories {

    private static JbehaveRunner jbehaveRunner = null;
    public static JbehaveRunner getJbehaveRunner(){
        return jbehaveRunner;
    }

    public Embedder embedder;
    public AllureJBehaveReporter allureJBehaveReporter;
    public MetaParser metaParser;
    public TestRailReporter testRailReporter;

    public JbehaveRunner() {
        try{
            jbehaveRunner = this;

            loadEnvironment();
            setLoggingLevel();

            allureJBehaveReporter = new AllureJBehaveReporter();
            testRailReporter = new TestRailReporter();

            //MetaParser is now our root reporter controlling others (to fix issues with events order)
            metaParser = new MetaParser(allureJBehaveReporter, testRailReporter);

            this.embedder = configuredEmbedder();



            /*default values from class EmbedderControls
            (these values will be used by default even if we will not use Embedder object)
                batch = false;
                skip = false;
                generateViewAfterStories = true;
                ignoreFailureInStories = false;
                ignoreFailureInView = false;
                verboseFailures = false;
                verboseFiltering = false;
                storyTimeouts = "300";
                threads = 1;
                failOnStoryTimeout = false;
             */
            String storyTimeout = "360000";
            if (StringUtils.isNotEmpty(System.getProperty("storyTimeout")))
                storyTimeout = System.getProperty("storyTimeout");

            embedder.embedderControls().useStoryTimeouts(storyTimeout).doIgnoreFailureInStories(false).
                    doIgnoreFailureInView(true).doVerboseFailures(true);
            embedder.useMetaFilters(loadMetaFilters());
            embedder.useEmbedderFailureStrategy(new CanceledStoryStrategy(embedder));
            this.useEmbedder(embedder);

        } catch (Throwable throwable) {
            allureJBehaveReporter.logFrameworkErrorToTheAllureReport(throwable);
        }
    }

    @Override
    @Test
    public void run() throws Throwable {
        try {
            embedder.runStoriesAsPaths(storyPaths());
        } catch (Embedder.RunningStoriesFailed runningStoriesFailed){
            throw runningStoriesFailed;
        } catch (Throwable t){
            System.err.println("[!!!] JBehaveRunner.run caught " + t.getClass().getSimpleName() + " with message: " + t.getMessage());
            allureJBehaveReporter.logFrameworkErrorToTheAllureReport(t);
            throw t;
        } finally {
            embedder.generateCrossReference();
        }
    }

    // Here we specify the configuration, starting from default MostUsefulConfiguration,
    // and changing only what is needed
    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryParser(new ExamplesFromDatabaseStoryParser())
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withDefaultFormats()
                        .withFormats(CONSOLE)
//                        .withReporters(allureJBehaveReporter, metaParser, testRailReporter)
                        .withReporters(metaParser)
                );

    }

    // Here we specify the steps classes
    @Override
    public InjectableStepsFactory stepsFactory() {
        // varargs, can have more that one steps classes
        return new ScanningStepsFactory(configuration(), BaseSteps.class);// InstanceStepsFactory(configuration(), new PageSteps(), new UtilSteps());
//        return SerenityStepFactory.withStepsFromPackage("ru.iep.autotests", configuration()).andClassLoader(getClassLoader());
    }

    @Override
    public List<String> storyPaths()  {
        //return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), "**/Sample.story", "");
        String suite = System.getProperty("suite", "" );
        List<String> storyPaths = new StoryFinder().findPaths(codeLocationFromClass(this.getClass()).getFile(), asList(suite.split("\\s*,\\s*" )), null);
        if(storyPaths.size() == 0){
            throw new RuntimeException("No stories has been found based on the suite parameter = '" + suite + "'");
        } else {
            System.out.println("[INFO] ------------------------------------------------------------------------");
            System.out.println("[INFO] Stories found based on the suite parameter = '" + suite + "'");
            storyPaths.forEach(System.out::println);
            System.out.println("[INFO] ------------------------------------------------------------------------");
        }
        return storyPaths;
    }

    private static boolean isExternalParameter(String param) {
        return System.getProperty(param) != null;
    }

    private static void loadParamsOverrideUTF(){
//        String[] args = (new OsNativeWindowsImpl()).getCommandLineArguments(null);
//        for(String arg:args){
//            Logger.getRootLogger().info(arg);
//            if (arg.startsWith("-Dparams_override=")) {
//                System.setProperty("params_override", arg.replace("-Dparams_override=", ""));
//                Logger.getRootLogger().info("Params override changed");
//                Logger.getRootLogger().info(System.getProperty("params_override"));
//            }
//        }
    }

    public static void loadEnvironment(){
        loadParamsOverrideUTF();
        Properties props = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("environment.properties");
            InputStreamReader isr = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader buffReader = new BufferedReader(isr);
            // load a properties file
            props.load(buffReader);

            for (Map.Entry<Object,Object> prop:props.entrySet()){
                if (!isExternalParameter(prop.getKey().toString())){
                    System.setProperty(prop.getKey().toString(),prop.getValue().toString());
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<String> loadMetaFilters(){
        String filter = System.getProperty("filter");

        if (Strings.isNullOrEmpty(filter)){
            filter = "-skip";
        }

        return asList(filter);
    }

    public void setLoggingLevel(){
        String level=System.getProperty("logger.level");
        if (!StringUtils.isEmpty(level))
            if (level.equals("DEBUG_INFO")){
                Logger.getRootLogger().setLevel(Level.INFO);
                Logger.getLogger("DEBUG_INFO").setLevel(Level.DEBUG);
            }
            else
                Logger.getRootLogger().setLevel(Level.toLevel(level, Level.INFO));

    }


    @After
    public void afterTest() throws IOException {
        //Trying to stop recording video in case if afterScenario has not been executed successfully
        try{
            stopScreenRecording();
        }catch (RuntimeException ignored){
            //ignore RuntimeException
        }
    }


    class CanceledStoryStrategy implements Embedder.EmbedderFailureStrategy{
        public Embedder embedder;

        public CanceledStoryStrategy(Embedder embedder){
            this.embedder = embedder;
        }

        @Override
        public void handleFailures(BatchFailures failures) {
            for(String key : failures.keySet()){
                Throwable throwable = failures.get(key);
                if (throwable instanceof CancellationException){
                    Story story = new Story(key);
                    StoryDuration duration = embedder.storyManager().runningStory(story).getDuration();
                    allureJBehaveReporter.storyCancelled(story, duration);
                }
            }
            throw new Embedder.RunningStoriesFailed(failures);
        }

        @Override
        public void handleFailures(ReportsCount count) {
            throw new Embedder.RunningStoriesFailed(count);
        }
    }
}
