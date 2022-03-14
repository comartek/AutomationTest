package core.steps.common.UtilSteps;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.steps.BaseSteps;
import org.jbehave.core.annotations.When;

public class ConditionalSteps extends BaseSteps {
    @NeverTakeAllureScreenshotsForThisStep
    @When("CONDITIONAL: if next steps execute successfully")
    public void conditionalIfBlockStart() { }

    @NeverTakeAllureScreenshotsForThisStep
    @When("CONDITIONAL: then execute next steps")
    public void conditionalThenBlockStart() { }

    @NeverTakeAllureScreenshotsForThisStep
    @When("CONDITIONAL: else execute next steps")
    public void conditionalElseBlockStart() { }

    @NeverTakeAllureScreenshotsForThisStep
    @When("CONDITIONAL: else if next steps execute successfully")
    public void conditionalElseIfBlockStart() { }

    @NeverTakeAllureScreenshotsForThisStep
    @When("CONDITIONAL: end of conditional block")
    public void conditionalBlockEnd() { }
}
