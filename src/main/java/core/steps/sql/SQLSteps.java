package core.steps.sql;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.steps.BaseSteps;
import core.utils.evaluationManager.EvaluationManager;
import core.utils.lang.LangUtils;
import core.utils.report.ReportUtils;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;

import java.util.List;
import java.util.Map;

/**
 * Created by Akarpenko on 26.02.2018.
 */
public class SQLSteps extends BaseSteps {

    SQLSubSteps sqlSubSteps = new SQLSubSteps();

    @NeverTakeAllureScreenshotsForThisStep
    @When("CIF number is received by USERNAME \"$value\" and saved to variable \"$variable\"")
    public void whenCIFisReceivedByparameterAndSavedToVariable(String value, String variable) {

        String value_ev = EvaluationManager.evalVariable(value);

        sqlSubSteps.stepCIFisReceivedByParameterAndSavedToVariable(value_ev, variable);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("passport ID is received by USERNAME \"$value\" and saved to variable \"$variable\"")
    public void whenPassportIDReceivedByParameterAndSavedToVariable(String value, String variable) {

        String value_ev = EvaluationManager.evalVariable(value);

        sqlSubSteps.stepPassportIDReceivedByParameterAndSavedToVariable(value_ev, variable);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("E-mail address is received by USERNAME \"$value\" and saved to variable \"$variable\"")
    public void whenEmailAddressReceivedByUsernameAndSavedToVariable(String value, String variable) {

        String value_ev = EvaluationManager.evalVariable(value);
        sqlSubSteps.stepEmailAddressIsReceivedByParameterAndSavedToVariable(value_ev, variable);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("phone number is received by USERNAME \"$value\" and saved to variable \"$variable\"")
    public void whenPhoneReceivedByUsernameAndSavedToVariable(String value, String variable) {

        String value_ev = EvaluationManager.evalVariable(value);
        sqlSubSteps.stepPhoneNumberIsReceivedByParameterAndSavedToVariable(value_ev, variable);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("OTP method is received by USERNAME \"$value\" and saved to variable \"$variable\"")
    public void whenOtpMethodReceivedByUsernameAndSavedToVariable(String value, String variable) {

        String value_ev = EvaluationManager.evalVariable(value);
        sqlSubSteps.stepOtpMethodIsReceivedByParameterAndSavedToVariable(value_ev, variable);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("branch label is received by BranchNo index \"$branchNo\" and saved to the variable \"$variable\"")
    public void whenBranchLabelIsReceivedByBranchNoIndexAndSavedToTheVariable(String branchNo, String variable) {
        String branchNo_ev = EvaluationManager.evalVariable(branchNo);
        String variable_ev = EvaluationManager.evalVariable(variable);

        sqlSubSteps.stepBranchLabelIsReceivedByBranchNoIndexAndSavedToTheVariable(branchNo_ev, variable_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("check that username \"$username\" is non-existing")
    public void whenCheckThatUsernameIsNonExisting(String username) {
        String username_ev = EvaluationManager.evalVariable(username);

        sqlSubSteps.stepCheckThatUsernameIsNonExisting(username_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("interest rate is received by currency \"$currency\", term \"$term\", amount \"$amount\", name_en \"$name_en\" and saved to variable \"$variable\"")
    public void whenInterestRateIsReceivedByCurrencytermAmountNameEnAndSavedToVariable
            (String currency, String term, String amount, String name_en, String variable) {

        String currency_ev = EvaluationManager.evalVariable(currency);
        String term_ev = EvaluationManager.evalVariable(term);
        String amount_ev = EvaluationManager.evalVariable(amount);
        String name_en_ev = EvaluationManager.evalVariable(name_en);

        sqlSubSteps.stepInterestRateIsReceivedByCurrencytermAmountNameEnAndSavedToVariable
                (currency_ev, term_ev, amount_ev, name_en_ev, variable);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("account type is received by Product ID \"$productID\" and saved to variable \"$variable\"")
    public void whenAccountTypeIsReceivedByProductIdAndSavedToVariable(String productID, String variable) {

        String productID_ev = EvaluationManager.evalVariable(productID);

        sqlSubSteps.stepAccountTypeIsReceivedByProductIdAndSavedToVariable(productID_ev, variable, LangUtils.getDefaultLanguage());

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("city name is received by Branch code \"$branchCode\" and saved to the variable \"$variable\"")
    public void whenCityNameIsReceivedByBranchCodeAndSavedToTheVariable(String branchCode, String variable) {

        String branchCode_ev = EvaluationManager.evalVariable(branchCode);

        sqlSubSteps.stepCityNameIsReceivedByBranchCodeAndSavedToTheVariable(branchCode_ev, variable);
    }


    @NeverTakeAllureScreenshotsForThisStep
    @When("perform SQL query to database \"$database\":$query")
    public void whenPerformSQLQueryToDatabase(String database, ExamplesTable query) {

        ReportUtils.attachExampletable(query);
        query = EvaluationManager.evaluateExampleTable(query);
        StringBuilder query_str = new StringBuilder();

        for (int i = 0; i < query.getRowCount(); i++) {
            query_str.append(query.getRow(i).get("query"));
            query_str.append("\r\n");
        }
        List<Map<String, String>> list = sqlSubSteps.stepPerformSQLQueryToDatabase(database, query_str.toString());

    }


    @NeverTakeAllureScreenshotsForThisStep
    @When("execute script in database \"$database\":$query")
    public void whenExecuteScriptInDatabase(String database, ExamplesTable query) {

        ReportUtils.attachExampletable(query);
        query = EvaluationManager.evaluateExampleTable(query);
        StringBuilder query_str = new StringBuilder();

        for (int i = 0; i < query.getRowCount(); i++) {
            query_str.append(query.getRow(i).get("query"));
            query_str.append("\r\n");
        }
        sqlSubSteps.stepExecuteScriptInDatabase(database, query_str.toString());

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("count records returned by latest SQL query to database \"$database\" and save result to a variable \"$variable\"")
    public void whenCountRecordsReturnedByLatestSQLQuery(String database, String variable) {
        sqlSubSteps.stepCountRecordsReturnedByLatestSQLQuery(database, variable);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("latest SQL query to database \"$database\" returned \"$count\" records")
    public void whenLatestSQLQueryToDatabaseReturnedRecords(String database, String count) {
        int count_ev = Integer.parseInt(EvaluationManager.evalVariable(count));
        sqlSubSteps.stepLatestSQLQueryToDatabaseReturnedRecords(database, count_ev);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("latest SQL query to database \"$database\" results are recorded to variables:$variables")
    public void whenSQLQueryResultsAreRecordedToVariables(String database, ExamplesTable variables) {

        ReportUtils.attachExampletable(variables);
        variables = EvaluationManager.evaluateExampleTable(variables);

        sqlSubSteps.stepSQLQueryResultsAreRecordedToVariables(database, variables);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("parameter \"$parameter\" of dataset \"$dataset\" for scenario \"$scenario\" is modified with value \"$value\"")
    public void whenParameterOfDatasetForScenarioIsMpdifiedWithValue(String parameter, String dataset,
                                                                     String scenario, String value) {

        String parameter_ev = EvaluationManager.evalVariable(parameter);
        String dataset_ev = EvaluationManager.evalVariable(dataset);
        String scenario_ev = EvaluationManager.evalVariable(scenario);
        String value_ev = EvaluationManager.evalVariable(value);

        sqlSubSteps.stepParameterOfDatasetForScenarioIsMpdifiedWithValue(parameter_ev, dataset_ev, scenario_ev, value_ev);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("set parameter \"$parameter\" in test database for current scenario and dataset to \"$value\"")
    public void whenSetParameterForCurrentScenarioAndDataset(String parameter, String value) {

        String parameter_ev = EvaluationManager.evalVariable(parameter);
        String value_ev = EvaluationManager.evalVariable(value);

        sqlSubSteps.stepSetParameterForCurrentScenarioAndDataset(parameter_ev, value_ev);

    }
    @NeverTakeAllureScreenshotsForThisStep
    @When("get unique value from table \"$table\" where \"$column\" equals \"$value\"")
    public void whenGetUniqueValuesFromTableByCondition(String table, String column, String value) {
        String table_ev = EvaluationManager.evalVariable(table);
        String column_ev = EvaluationManager.evalVariable(column);
        String value_ev = EvaluationManager.evalVariable(value);
        sqlSubSteps.stepGetUniqueValuesFromTableByCondition(table_ev,column_ev,value_ev);
    }
    @NeverTakeAllureScreenshotsForThisStep
    @When("get unique value from table \"$table\" where \"$column\" less than \"$value\"")
    public void whenGetUniqueValuesFromTableBefore(String table, String column, String value) {
        String table_ev = EvaluationManager.evalVariable(table);
        String column_ev = EvaluationManager.evalVariable(column);
        String value_ev = EvaluationManager.evalVariable(value);
        sqlSubSteps.stepGetUniqueValuesFromTableBefore(table_ev,column_ev,value_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("get unique values from table \"$table\"")
    public void whenGetUniqueValuesFromTable(String table) {
        String table_ev = EvaluationManager.evalVariable(table);

        sqlSubSteps.stepGetUniqueValuesFromTable(table_ev);

    }
    @NeverTakeAllureScreenshotsForThisStep
    @When("get unique values from table \"$table\" and do not delete")
    public void whenGetUniqueValuesFromTableNotDelete(String table) {
        String table_ev = EvaluationManager.evalVariable(table);

        sqlSubSteps.stepGetUniqueValuesFromTableNotDelete(table_ev);

    }
    @NeverTakeAllureScreenshotsForThisStep
    @When("get record \"$id\" from table \"$table\"")
    public void whenGetRecordFromTable(String id, String table) {
        String table_ev = EvaluationManager.evalVariable(table);
        String id_ev = EvaluationManager.evalVariable(id);
        sqlSubSteps.stepGetRecordFromTable(id_ev,table_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("save next value of counter \"$counter\" to variable \"$variable\"")
    public void whenGetNextValueOfCounter(String counter, String variable) {
        counter = EvaluationManager.evalVariable(counter);

        sqlSubSteps.stepGetNextValueOfCounter(counter, variable);

    }

    @When("first time password is received by phone \"$phone\" and saved to variable \"$variable\"")
    public void whenFTPisReceivedByPhoneAndSavedToVariable(String phone, String variable) {

        String phone_ev = EvaluationManager.evalVariable(phone);
        sqlSubSteps.stepFTPisReceivedByPhoneAndSavedToVariable(phone_ev, variable);
    }

    @When("update record where \"$column\" equals \"$value\" from table \"$table\"")
    public void whenUpdateRecord(String column, String value, String table){
        String column_ev = EvaluationManager.evalVariable(column);
        String value_ev = EvaluationManager.evalVariable(value);
        String table_ev = EvaluationManager.evalVariable(table);
        sqlSubSteps.stepUpdateRecord(column_ev, value_ev, table_ev);
    }
    @When("get current dataset and save to variable  \"$variable\"")
    public void g(String variable) {
        sqlSubSteps.getCurrentDataset(variable);
    }
}
