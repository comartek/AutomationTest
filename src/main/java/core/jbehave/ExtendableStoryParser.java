package core.jbehave;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.annotations.AfterScenario.Outcome;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.ResourceLoader;
import org.jbehave.core.model.Description;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.model.GivenStories;
import org.jbehave.core.model.Lifecycle;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Narrative;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.model.TableTransformers;
import org.jbehave.core.model.Lifecycle.Steps;
import org.jbehave.core.parsers.StoryParser;


/**
 * Created by Denis on 07.04.2018.
 */
public class ExtendableStoryParser  implements StoryParser {
    protected static final String NONE = "";
    protected final Keywords keywords;
    protected final ExamplesTableFactory tableFactory;

    public ExtendableStoryParser() {
        this((ResourceLoader)(new LoadFromClasspath()), (TableTransformers)(new TableTransformers()));
    }

    public ExtendableStoryParser(ResourceLoader resourceLoader, TableTransformers tableTransformers) {
        this(new LocalizedKeywords(), resourceLoader, tableTransformers);
    }

    public ExtendableStoryParser(Keywords keywords, ResourceLoader resourceLoader, TableTransformers tableTransformers) {
        this(keywords, new ExamplesTableFactory(keywords, resourceLoader, tableTransformers));
    }

    public ExtendableStoryParser(ExamplesTableFactory tableFactory) {
        this(tableFactory.keywords(), tableFactory);
    }

    public ExtendableStoryParser(Keywords keywords, ExamplesTableFactory tableFactory) {
        this.keywords = keywords;
        this.tableFactory = tableFactory;
        this.tableFactory.useKeywords(keywords);
    }

    public ExtendableStoryParser(Configuration configuration) {
        this.keywords = configuration.keywords();
        this.tableFactory = new ExamplesTableFactory(configuration);
    }

    public Story parseStory(String storyAsText) {
        return this.parseStory(storyAsText, (String)null);
    }

    public Story parseStory(String storyAsText, String storyPath) {
        Description description = this.parseDescriptionFrom(storyAsText);
        Meta meta = this.parseStoryMetaFrom(storyAsText);
        Narrative narrative = this.parseNarrativeFrom(storyAsText);
        GivenStories givenStories = this.parseGivenStories(storyAsText);
        Lifecycle lifecycle = this.parseLifecycle(storyAsText);
        List scenarios = this.parseScenariosFrom(storyAsText);
        Story story = new Story(storyPath, description, meta, narrative, givenStories, lifecycle, scenarios);
        if(storyPath != null) {
            story.namedAs((new File(storyPath)).getName());
        }

        return story;
    }

    protected Description parseDescriptionFrom(String storyAsText) {
        Matcher findingDescription = this.findingDescription().matcher(storyAsText);
        return findingDescription.matches()?new Description(findingDescription.group(1).trim()):Description.EMPTY;
    }

    protected Meta parseStoryMetaFrom(String storyAsText) {
        Matcher findingMeta = this.findingStoryMeta().matcher(this.preScenarioText(storyAsText));
        if(findingMeta.matches()) {
            String meta = findingMeta.group(1).trim();
            return Meta.createMeta(meta, this.keywords);
        } else {
            return Meta.EMPTY;
        }
    }

    protected String preScenarioText(String storyAsText) {
        String[] split = storyAsText.split(this.keywords.scenario());
        return split.length > 0?split[0]:storyAsText;
    }

    protected Narrative parseNarrativeFrom(String storyAsText) {
        Matcher findingNarrative = this.findingNarrative().matcher(storyAsText);
        if(findingNarrative.matches()) {
            String narrative = findingNarrative.group(1).trim();
            return this.createNarrative(narrative);
        } else {
            return Narrative.EMPTY;
        }
    }

    protected Narrative createNarrative(String narrative) {
        Matcher findingElements = this.findingNarrativeElements().matcher(narrative);
        String asA;
        String iWantTo;
        if(findingElements.matches()) {
            String findingAlternativeElements1 = findingElements.group(1).trim();
            asA = findingElements.group(2).trim();
            iWantTo = findingElements.group(3).trim();
            return new Narrative(findingAlternativeElements1, asA, iWantTo);
        } else {
            Matcher findingAlternativeElements = this.findingAlternativeNarrativeElements().matcher(narrative);
            if(findingAlternativeElements.matches()) {
                asA = findingAlternativeElements.group(1).trim();
                iWantTo = findingAlternativeElements.group(2).trim();
                String soThat = findingAlternativeElements.group(3).trim();
                return new Narrative("", asA, iWantTo, soThat);
            } else {
                return Narrative.EMPTY;
            }
        }
    }

    protected GivenStories parseGivenStories(String storyAsText) {
        String scenarioKeyword = this.keywords.scenario();
        String beforeScenario = "";
        if(StringUtils.contains(storyAsText, scenarioKeyword)) {
            beforeScenario = StringUtils.substringBefore(storyAsText, scenarioKeyword);
        }

        Matcher findingGivenStories = this.findingStoryGivenStories().matcher(beforeScenario);
        String givenStories = findingGivenStories.find()?findingGivenStories.group(1).trim():"";
        return new GivenStories(givenStories);
    }

    protected Lifecycle parseLifecycle(String storyAsText) {
        String scenarioKeyword = this.keywords.scenario();
        String beforeScenario = "";
        if(StringUtils.contains(storyAsText, scenarioKeyword)) {
            beforeScenario = StringUtils.substringBefore(storyAsText, scenarioKeyword);
        }

        Matcher findingLifecycle = this.findingLifecycle().matcher(beforeScenario);
        String lifecycle = findingLifecycle.find()?findingLifecycle.group(1).trim():"";
        Matcher findingBeforeAndAfter = Pattern.compile(".*" + this.keywords.before() + "(.*)\\s*" + this.keywords.after() + "(.*)\\s*", 32).matcher(lifecycle);
        if(findingBeforeAndAfter.matches()) {
            String findingBefore1 = findingBeforeAndAfter.group(1).trim();
            Steps findingAfter2 = this.parseBeforeLifecycle(findingBefore1);
            String beforeSteps1 = findingBeforeAndAfter.group(2).trim();
            Steps[] afterLifecycle1 = this.parseAfterLifecycle(beforeSteps1);
            return new Lifecycle(findingAfter2, afterLifecycle1);
        } else {
            Matcher findingBefore = Pattern.compile(".*" + this.keywords.before() + "(.*)\\s*", 32).matcher(lifecycle);
            Steps beforeSteps;
            if(findingBefore.matches()) {
                String findingAfter1 = findingBefore.group(1).trim();
                beforeSteps = this.parseBeforeLifecycle(findingAfter1);
                return new Lifecycle(beforeSteps, new Steps[]{new Steps(new ArrayList())});
            } else {
                Matcher findingAfter = Pattern.compile(".*" + this.keywords.after() + "(.*)\\s*", 32).matcher(lifecycle);
                if(findingAfter.matches()) {
                    beforeSteps = Steps.EMPTY;
                    String afterLifecycle = findingAfter.group(1).trim();
                    Steps[] afterSteps = this.parseAfterLifecycle(afterLifecycle);
                    return new Lifecycle(beforeSteps, afterSteps);
                } else {
                    return Lifecycle.EMPTY;
                }
            }
        }
    }

    protected Steps parseBeforeLifecycle(String lifecycleAsText) {
        return new Steps(this.findSteps(this.startingWithNL(lifecycleAsText)));
    }

    protected Steps[] parseAfterLifecycle(String lifecycleAsText) {
        ArrayList list = new ArrayList();
        String[] var3 = lifecycleAsText.split(this.keywords.outcome());
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String byOutcome = var3[var5];
            byOutcome = byOutcome.trim();
            if(!byOutcome.isEmpty()) {
                String outcomeAsText = this.findOutcome(byOutcome);
                String filtersAsText = this.findFilters(StringUtils.removeStart(byOutcome, outcomeAsText));
                List steps = this.findSteps(this.startingWithNL(StringUtils.removeStart(byOutcome, filtersAsText)));
                list.add(new Steps(this.parseOutcome(outcomeAsText), this.parseFilters(filtersAsText), steps));
            }
        }

        return (Steps[])list.toArray(new Steps[list.size()]);
    }

    protected String findOutcome(String stepsByOutcome) {
        Matcher findingOutcome = this.findingLifecycleOutcome().matcher(stepsByOutcome);
        return findingOutcome.matches()?findingOutcome.group(1).trim():this.keywords.outcomeAny();
    }

    protected Outcome parseOutcome(String outcomeAsText) {
        return outcomeAsText.equals(this.keywords.outcomeSuccess())?Outcome.SUCCESS:(outcomeAsText.equals(this.keywords.outcomeFailure())?Outcome.FAILURE:Outcome.ANY);
    }

    protected String findFilters(String stepsByFilters) {
        Matcher findingFilters = this.findingLifecycleFilters().matcher(stepsByFilters.trim());
        return findingFilters.matches()?findingFilters.group(1).trim():"";
    }

    protected String parseFilters(String filtersAsText) {
        return StringUtils.removeStart(filtersAsText, this.keywords.metaFilter()).trim();
    }

    protected List<Scenario> parseScenariosFrom(String storyAsText) {
        ArrayList parsed = new ArrayList();
        Iterator var3 = this.splitScenarios(storyAsText).iterator();

        while(var3.hasNext()) {
            String scenarioAsText = (String)var3.next();
            parsed.add(this.parseScenario(scenarioAsText));
        }

        return parsed;
    }

    protected List<String> splitScenarios(String storyAsText) {
        ArrayList scenarios = new ArrayList();
        String scenarioKeyword = this.keywords.scenario();
        if(StringUtils.contains(storyAsText, scenarioKeyword)) {
            storyAsText = StringUtils.substringAfter(storyAsText, scenarioKeyword);
        }

        String[] var4 = storyAsText.split(scenarioKeyword);
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String scenarioAsText = var4[var6];
            if(scenarioAsText.trim().length() > 0) {
                scenarios.add(scenarioKeyword + "\n" + scenarioAsText);
            }
        }

        return scenarios;
    }

    protected Scenario parseScenario(String scenarioAsText) {
        String title = this.findScenarioTitle(scenarioAsText);
        String scenarioWithoutKeyword = StringUtils.removeStart(scenarioAsText, this.keywords.scenario()).trim();
        String scenarioWithoutTitle = StringUtils.removeStart(scenarioWithoutKeyword, title);
        scenarioWithoutTitle = this.startingWithNL(scenarioWithoutTitle);
        Meta meta = this.findScenarioMeta(scenarioWithoutTitle);
        ExamplesTable examplesTable = this.findExamplesTable(scenarioWithoutTitle);
        GivenStories givenStories = this.findScenarioGivenStories(scenarioWithoutTitle);
        if(givenStories.requireParameters()) {
            givenStories.useExamplesTable(examplesTable);
        }

        List steps = this.findSteps(scenarioWithoutTitle);
        return new Scenario(title, meta, givenStories, examplesTable, steps);
    }

    protected String startingWithNL(String text) {
        return !text.startsWith("\n")?"\n" + text:text;
    }

    protected String findScenarioTitle(String scenarioAsText) {
        Matcher findingTitle = this.findingScenarioTitle().matcher(scenarioAsText);
        return findingTitle.find()?findingTitle.group(1).trim():"";
    }

    protected Meta findScenarioMeta(String scenarioAsText) {
        Matcher findingMeta = this.findingScenarioMeta().matcher(scenarioAsText);
        if(findingMeta.matches()) {
            String meta = findingMeta.group(1).trim();
            return Meta.createMeta(meta, this.keywords);
        } else {
            return Meta.EMPTY;
        }
    }

    protected ExamplesTable findExamplesTable(String scenarioAsText) {
        Matcher findingTable = this.findingExamplesTable().matcher(scenarioAsText);
        String tableInput = findingTable.find()?findingTable.group(1).trim():"";
        return this.tableFactory.createExamplesTable(tableInput);
    }

    protected GivenStories findScenarioGivenStories(String scenarioAsText) {
        Matcher findingGivenStories = this.findingScenarioGivenStories().matcher(scenarioAsText);
        String givenStories = findingGivenStories.find()?findingGivenStories.group(1).trim():"";
        return new GivenStories(givenStories);
    }

    protected List<String> findSteps(String stepsAsText) {
        Matcher matcher = this.findingSteps().matcher(stepsAsText);
        ArrayList steps = new ArrayList();

        for(int startAt = 0; matcher.find(startAt); startAt = matcher.start(4)) {
            steps.add(StringUtils.substringAfter(matcher.group(1), "\n"));
        }

        return steps;
    }

    protected Pattern findingDescription() {
        String metaOrNarrativeOrLifecycleOrScenario = this.concatenateWithOr(new String[]{this.keywords.meta(), this.keywords.narrative(), this.keywords.lifecycle(), this.keywords.scenario()});
        return Pattern.compile("(.*?)(" + metaOrNarrativeOrLifecycleOrScenario + ").*", 32);
    }

    protected Pattern findingStoryMeta() {
        String narrativeOrLifecycleOrGivenStories = this.concatenateWithOr(new String[]{this.keywords.narrative(), this.keywords.lifecycle(), this.keywords.givenStories()});
        return Pattern.compile(".*" + this.keywords.meta() + "(.*?)\\s*(\\Z|" + narrativeOrLifecycleOrGivenStories + ").*", 32);
    }

    protected Pattern findingNarrative() {
        String givenStoriesOrLifecycleOrScenario = this.concatenateWithOr(new String[]{this.keywords.givenStories(), this.keywords.lifecycle(), this.keywords.scenario()});
        return Pattern.compile(".*" + this.keywords.narrative() + "(.*?)\\s*(" + givenStoriesOrLifecycleOrScenario + ").*", 32);
    }

    protected Pattern findingNarrativeElements() {
        return Pattern.compile(".*" + this.keywords.inOrderTo() + "(.*)\\s*" + this.keywords.asA() + "(.*)\\s*" + this.keywords.iWantTo() + "(.*)", 32);
    }

    protected Pattern findingAlternativeNarrativeElements() {
        return Pattern.compile(".*" + this.keywords.asA() + "(.*)\\s*" + this.keywords.iWantTo() + "(.*)\\s*" + this.keywords.soThat() + "(.*)", 32);
    }

    protected Pattern findingStoryGivenStories() {
        String lifecycleOrScenario = this.concatenateWithOr(new String[]{this.keywords.lifecycle(), this.keywords.scenario()});
        return Pattern.compile(".*" + this.keywords.givenStories() + "(.*?)\\s*(\\Z|" + lifecycleOrScenario + ").*", 32);
    }

    protected Pattern findingLifecycle() {
        return Pattern.compile(".*" + this.keywords.lifecycle() + "\\s*(.*)", 32);
    }

    protected Pattern findingLifecycleOutcome() {
        String startingWords = this.concatenateWithOr("\\n", "", this.keywords.startingWords());
        String outcomes = this.concatenateWithOr(new String[]{this.keywords.outcomeAny(), this.keywords.outcomeSuccess(), this.keywords.outcomeFailure()});
        return Pattern.compile("\\s*(" + outcomes + ")\\s*(" + this.keywords.metaFilter() + "|" + startingWords + ").*", 32);
    }

    protected Pattern findingLifecycleFilters() {
        String startingWords = this.concatenateWithOr("\\n", "", this.keywords.startingWords());
        String filters = this.concatenateWithOr(new String[]{this.keywords.metaFilter()});
        return Pattern.compile("\\s*(" + filters + "[\\w\\+\\-\\_\\s]*)(" + startingWords + ").*", 32);
    }

    protected Pattern findingScenarioTitle() {
        String startingWords = this.concatenateWithOr("\\n", "", this.keywords.startingWords());
        return Pattern.compile(this.keywords.scenario() + "((.)*?)\\s*(" + this.keywords.meta() + "|" + startingWords + ").*", 32);
    }

    protected Pattern findingScenarioMeta() {
        String startingWords = this.concatenateWithOr("\\n", "", this.keywords.startingWords());
        return Pattern.compile(".*" + this.keywords.meta() + "(.*?)\\s*(" + this.keywords.givenStories() + "|" + startingWords + ").*", 32);
    }

    protected Pattern findingScenarioGivenStories() {
        String startingWords = this.concatenateWithOr("\\n", "", this.keywords.startingWords());
        return Pattern.compile("\\n" + this.keywords.givenStories() + "((.|\\n)*?)\\s*(" + startingWords + ").*", 32);
    }

    protected Pattern findingSteps() {
        String initialStartingWords = this.concatenateWithOr("\\n", "", this.keywords.startingWords());
        String followingStartingWords = this.concatenateWithOr("\\n", "\\s", this.keywords.startingWords());
        return Pattern.compile("((" + initialStartingWords + ")\\s(.)*?)\\s*(\\Z|" + followingStartingWords + "|\\n" + this.keywords.examplesTable() + ")", 32);
    }

    protected Pattern findingExamplesTable() {
        return Pattern.compile("\\n" + this.keywords.examplesTable() + "\\s*(.*)", 32);
    }

    protected String concatenateWithOr(String... keywords) {
        return this.concatenateWithOr((String)null, (String)null, keywords);
    }

    protected String concatenateWithOr(String beforeKeyword, String afterKeyword, String[] keywords) {
        StringBuilder builder = new StringBuilder();
        String before = beforeKeyword != null?beforeKeyword:"";
        String after = afterKeyword != null?afterKeyword:"";
        String[] var7 = keywords;
        int var8 = keywords.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            String keyword = var7[var9];
            builder.append(before).append(keyword).append(after).append("|");
        }

        return StringUtils.removeEnd(builder.toString(), "|");
    }
}

