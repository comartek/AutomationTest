package core.steps.common.AssertionSteps;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.steps.BaseSteps;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;


/**
 * Created by Akarpenko on 10.01.2018.
 */
public class AssertionSteps extends BaseSteps {


    AssertionSubSteps assertionSubSteps = new AssertionSubSteps();

    @NeverTakeAllureScreenshotsForThisStep
    @When("SYSTEM: SOFT ASSERT MODE IS ACTIVATED")
    public void whenActivateSoftAssertMode(){
        assertionSubSteps.stepActivateSoftAssertMode();
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("SYSTEM: SOFT ASSERT MODE IS DEACTIVATED")
    public void whenDeactivateSoftAssertMode(){
        assertionSubSteps.stepDeactivateSoftAssertMode();
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("SYSTEM: SOFT ASSERT MODE IS DEACTIVATED, AND THEN FAIL SCENARIO IF SOFT ASSERT ERRORS HAS BEEN CAUGHT EARLIER")
    public void whenDeactivateSoftAssertModeAndThenFailIfSoftAssertErrorsWasCaught()
    {
        assertionSubSteps.stepDeactivateSoftAssertModeAndThenFailIfSoftAssertErrorsWasCaught();
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("SYSTEM: FAIL SCENARIO IF SOFT ASSERT ERRORS HAS BEEN CAUGHT EARLIER")
    public void thenFailIfSoftAssertErrorsHasBeenCaught()
    {
        assertionSubSteps.stepFailIfSoftAssertErrorsHasBeenCaught();
    }



}
