import org.jbehave.core.io.StoryFinder;
import core.JbehaveRunner;

import java.util.List;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;

/**
 * Created by Akarpenko on 29.11.2017.
 */
public class TestRunner2 extends JbehaveRunner {
    @Override
    public List<String> storyPaths() {
        String suite = System.getProperty("suite", "" );
        List<String> storyPaths = new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), suite, "");
//        List<String> storyPaths = new StoryFinder().findPaths(codeLocationFromClass(this.getClass()).getFile(), asList(suite.split("\\s*,\\s*" )), null);

        if(storyPaths.size() == 0){
            throw new RuntimeException("No stories has been found based on the suite parameter = '" + suite + "'");
        } else {
            System.out.println("[INFO] ------------------------------------------------------------------------");
            System.out.println("[INFO] Stories found based on the suite parameter '" + suite + "' = " + storyPaths.size() + ":");
            storyPaths.forEach((storyPath) -> System.out.println("[INFO]   - " + storyPath));
            System.out.println("[INFO] ------------------------------------------------------------------------");
        }
        return storyPaths;
    }
}
