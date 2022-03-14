package core.steps.common.AssertionSteps;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.annotations.StepExt;
import core.exceptions.SoftAssertException;
import core.steps.BaseSteps;
import core.utils.assertExt.AssertExt;

/**
 * Created by Akarpenko on 10.01.2018.
 */
public class AssertionSubSteps extends BaseSteps {

    public static String msgForForcedFailScenarioAfterSoftAssertError =
            "Forced fail scenario because soft assert error has been caught earlier";

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("SYSTEM: SOFT ASSERT MODE IS ACTIVATED")
    public void stepActivateSoftAssertMode(){
        AssertExt.setSoftAssertMode(true);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("SYSTEM: SOFT ASSERT MODE IS DEACTIVATED")
    public void stepDeactivateSoftAssertMode(){
        AssertExt.setSoftAssertMode(false);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("SYSTEM: SOFT ASSERT MODE IS DEACTIVATED, AND THEN FAIL SCENARIO IF SOFT ASSERT ERRORS HAS BEEN CAUGHT EARLIER")
    public void stepDeactivateSoftAssertModeAndThenFailIfSoftAssertErrorsWasCaught()
    {
        AssertExt.setSoftAssertMode(false);
        if(AssertExt.getCountInterceptedErrors() > 0)
            throw new SoftAssertException(msgForForcedFailScenarioAfterSoftAssertError);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("SYSTEM: FAIL SCENARIO IF SOFT ASSERT ERRORS HAS BEEN CAUGHT EARLIER")
    public void stepFailIfSoftAssertErrorsHasBeenCaught()
    {
        if(AssertExt.getCountInterceptedErrors() > 0)
            throw new SoftAssertException(msgForForcedFailScenarioAfterSoftAssertError);
    }
}
