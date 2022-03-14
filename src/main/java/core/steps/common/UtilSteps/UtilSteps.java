package core.steps.common.UtilSteps;


import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.annotations.StepExt;
import core.steps.BaseSteps;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import static core.utils.evaluationManager.EvaluationManager.evalVariable;
import static core.utils.timeout.TimeoutUtils.loadingTimeoutDefault;


/**
 * Created by Akarpenko on 02.11.2017.
 */



public class UtilSteps extends BaseSteps {

    UtilSubSteps utilSubSteps = new UtilSubSteps();

    @NeverTakeAllureScreenshotsForThisStep
    @When("switch to datapool root")
    public void whenSwitchToDatapoolRoot() {
        utilSubSteps.stepSwitchToDatapoolRoot();
    }


    @NeverTakeAllureScreenshotsForThisStep
    @When("switch to datapool section \"$section\"")
    public void whenSwitchToDatapoolSection(String section) {
        utilSubSteps.stepSwitchToDatapoolSection(section);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("switch to datapool section without dataset \"$section\"")
    public void whenSwitchToDatapoolSectionWithoutDataset(String section) {
        utilSubSteps.stepSwitchToDatapoolSectionWithoutDataset(section);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("variable \"$variable\" with value \"$value\" is saved to datapool")
    public void whenSaveVariableToPool(String variable, String value){

        String value_ev = evalVariable(value);

        utilSubSteps.stepSaveVariableToPool(variable, value_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("initialize datapool variable \"$variable\" with value \"$value\"")
    public void whenInitVariableInDatapool(String variable, String value){

        String value_ev = evalVariable(value);

        utilSubSteps.stepInitVariableInDatapool(variable, value_ev);
    }


    @NeverTakeAllureScreenshotsForThisStep
    @When("value \"$value\" is saved to variable \"$variable\"")
    public void whenSaveVariable(String value, String variable){
        String value_ev = evalVariable(value);

        utilSubSteps.stepSaveVariable(value_ev, variable);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("if value of variable \"$variable\" equals null set value \"$value\"")
    public void whenRefactorNullToZeroVariable(String variable,String value){
        String value_ev = evalVariable("%{"+variable+"}%");
        if(value_ev.equals("")){
            utilSubSteps.stepSaveVariable(value, variable);
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("initialize variable \"$variable\" with value \"$value\"")
    public void whenInitVariableWithValue(String variable, String value){

        String value_ev = evalVariable(value);

        utilSubSteps.stepInitVariableWithValue(variable, value_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("fail")
    public void fail()
    {
        Assert.fail("WASTED");
    }


    @NeverTakeAllureScreenshotsForThisStep
    @When("wait for \"$count\" seconds")
    public void whenWaitFor(int count){
                utilSubSteps.stepWaitFor(count);
    }

    @Then("value \"$value1\" \"$operation\" \"$value2\"")
    public void thenValueOperation(String value1, String operation, String value2) {

        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);
        String operation_ev = ((String) evalVariable(operation)).trim();
        utilSubSteps.stepValueOperation(value1_ev, operation_ev, value2_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("value \"$value1\" equals \"$value2\"")
    public void thenValueEquals(String value1, String value2){
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);

        utilSubSteps.stepValueEquals(value1_ev, value2_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("value \"$value1\" smaller than \"$value2\"")
    public void thenValueSmaller(String value1, String value2){
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);

        utilSubSteps.stepValueSmaller(value1_ev, value2_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("value \"$value1\" greater than \"$value2\"")
    public void thenValueGreater(String value1, String value2){
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);

        utilSubSteps.stepValueGreater(value1_ev, value2_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("value \"$value1\" contains \"$value2\"")
    public void thenValuecontains(String value1, String value2){
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);
        utilSubSteps.stepValueContains(value1_ev, value2_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("value \"$value1\" not contains \"$value2\"")
    public void thenValueNotcontains(String value1, String value2){
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);
        utilSubSteps.stepValueNotContains(value1_ev, value2_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("value last of string \"$value1\" is less than value last of string\"$value2\"")
    public void thenValueLastOfString(String value1, String value2){
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);
        utilSubSteps.stepValueLastOfString(value1_ev, value2_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("value \"$value1\" matches \"$value2\"")
    public void thenValueMatches(String value1, String value2){
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);
        utilSubSteps.stepValueMatches(value1_ev, value2_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("value \"$value1\" is lowercase")
    public void thenValuelowercase(String value1){
        String value1_ev = evalVariable(value1);
        utilSubSteps.stepValueLowercase(value1_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("value \"$value1\" is not equals \"$value2\"")
    public void thenValueIsNotEquals(String value1, String value2){
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);

        utilSubSteps.stepValueIsNotEquals(value1_ev, value2_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("number value \"$value1\" is less than or equal to \"$value2\"")
    public void thenNumberValueIsLessThanOrEqualTo(String value1, String value2){
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);

        utilSubSteps.stepNumberValueIsLessThanOrEqualTo(value1_ev, value2_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("number value \"$value1\" is less than \"$value2\"")
    public void thenNumberValueIsLess(String value1, String value2){
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);

        utilSubSteps.stepNumberValueIsLess(value1_ev, value2_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("number value \"$value1\" is greater than \"$value2\"")
    public void thenNumberValueisGreaterThan(String value1, String value2){
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);

        utilSubSteps.stepNumberValueIsGreaterThan(value1_ev, value2_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("received one time password for user \"$login\" (wait for \"$maxWaitTimeInSeconds\" seconds) with any OTP method and saved its value to variable \"$variable\"")
    public void whenGetOneTimePasswordForUserWithMethod(String username, String maxWaitTimeInSeconds, String variable){
        username=evalVariable(username);
        int maxWaitTimeInSeconds_ev = Integer.valueOf(evalVariable(maxWaitTimeInSeconds));
        utilSubSteps.stepGetOneTimePasswordForUserWithMethod(username,maxWaitTimeInSeconds_ev, variable);
    }


    @NeverTakeAllureScreenshotsForThisStep
    @Then("{S|s}tep $step")
    @StepExt(value = "Step {step}", hideParams = true, forcedStepStatus = StepExt.ForcedStepStatus.SKIPPED)
    public void thenStep(String step){
//        this is a fake step to send step numbers to Allure report
        String s = "1";
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("{C|c}omment: $comment")
    @StepExt(value = "Comment: {comment}", hideParams = true, forcedStepStatus = StepExt.ForcedStepStatus.SKIPPED)
    public void thenComment(String comment){
//        this is a fake step to send comments to Allure report
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("multivariable \"$multivariable\" with separator \"$separator\" is parsed to set of variable with prefix-name \"$name\"")
    public void whenMultiVariableWithSeparatorIsParsedToSetOfVariablesWithName(String multivariable, String separator, String name){
        multivariable = evalVariable(multivariable);
        utilSubSteps.stepMultiVariableWithSeparatorIsParsedToSetOfVariablesWithName(multivariable, separator, name);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("use message dictionary \"$file\"")
    public void whenUseMessageDictionary(String file) {
        System.setProperty("message_dictionary",file);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("max loading wait time is changed to \"$maxLoadingWaitTime\" seconds")
    public void whenMaxLoadingWaitTimeIsChanged(String maxLoadingWaitTime) {
        utilSubSteps.stepMaxLoadingWaitTimeIsChanged(maxLoadingWaitTime);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("max loading wait time is returned to the default value")
    public void whenMaxLoadingWaitTimeIsReturnedToDefault() {
        utilSubSteps.stepMaxLoadingWaitTimeIsChanged(String.valueOf(loadingTimeoutDefault));
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("change language property to \"$language\"")
    public void whenChangeLanguageProperty(String language) {
        utilSubSteps.stepChangeLanguageProperty(language);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Given("IMPORT STEPS \"$scenarioName\" from \"$fileName\"")
    public void givenImportStepsFrom(String scenarioName,String fileName) {

    }

    @NeverTakeAllureScreenshotsForThisStep
    @Given("loop case to datapool section without dataset \"$section\"")
    public void givevSwitchToDatapoolSectionWithoutDataset(String section) {
        utilSubSteps.stepSwitchToDatapoolSectionWithoutDataset(section);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("get days between \"$date1\" and \"$date\" to variable \"$var\"")
    public void whenDaysBetweenDates (String date1, String date2,String var){
        String value1_ev = evalVariable(date1);
        String value2_ev = evalVariable(date2);
        String s=utilSubSteps.stepDaysBetweenDates(value1_ev, value2_ev);
        utilSubSteps.stepSaveVariable(s, var);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("value \"$value1\" greater or equal than \"$value2\"")
    public void thenValueGreaterOrEqual(String value1, String value2){
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);

        utilSubSteps.stepValueGreaterOrEqual(value1_ev, value2_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("value \"$value1\" is saved as nearest business day date in variable \"$value2\"")
    public void whenSaveDateFromFieldAsBusinessDayInVariable(String value1, String value2) {
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);
        utilSubSteps.stepSaveDateFromFieldAsBusinessDayInVariable(value1_ev, value2_ev);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("value \"$value1\" equals value \"$value2\" or \"$value3\"")
    public void thenValueEqualsValues(String value1, String value2, String value3){
        String value1_ev = evalVariable(value1);
        String value2_ev = evalVariable(value2);
        String value3_ev = evalVariable(value3);
        utilSubSteps.stepValueEqualsValues(value1_ev, value2_ev,value3_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("read outlook email save to variable \"$variable\"")
    public void whenReadContentOutlookMail(String variable){
        String variable_ev = evalVariable(variable);
        utilSubSteps.stepGetContentOutlookMail(variable_ev);
    }

}


