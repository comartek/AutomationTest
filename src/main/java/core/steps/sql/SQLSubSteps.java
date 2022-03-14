package core.steps.sql;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.annotations.StepExt;
import core.sql.SQLManager;
import core.steps.BaseSteps;
import core.steps.common.UtilSteps.UtilSubSteps;
import core.utils.lang.LangUtils;
import core.utils.report.MetaParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jbehave.core.model.ExamplesTable;
import org.junit.Assert;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Akarpenko on 26.02.2018.
 */
public class SQLSubSteps extends BaseSteps {

    static UtilSubSteps utilSubSteps = new UtilSubSteps();


    @StepExt("CIF number is received by USERNAME \"{value}\" and saved to variable \"{variable}\"")
    public static String stepCIFisReceivedByParameterAndSavedToVariable(String value, String variable) {
        String query = "";


        query = String.format("select cust_id AS CIF from OCBADMIN.CUSTOMER_DIRECTORY " +
                "where DIRECTORY_ID = (select directory_id from OCBADMIN.CUSTOMER where USER_NAME='%s')", value);


        List<Map<String, String>> result = SQLManager.getInstance().get().OCB_UAT_DB.executeQueryWithResults(query);

        if (result.size() > 0)
            utilSubSteps.stepSaveVariable(result.get(0).get("CIF"), variable);
        else
            Assert.fail(String.format("Customer profile not found by USERNAME:%s", value));

        return result.get(0).get("CIF");
    }

    @StepExt("E-mail address is received by username \"{value}\" and saved to variable \"{variable}\"")
    public void stepEmailAddressIsReceivedByParameterAndSavedToVariable(String value, String variable) {
        String query = "";


        query = String.format("select EMAIL_ADDRESS from OCBADMIN.CUSTOMER where USER_NAME='%s'", value);


        List<Map<String, String>> result = SQLManager.getInstance().get().OCB_UAT_DB.executeQueryWithResults(query);

        if (result.size() > 0)
            utilSubSteps.stepSaveVariable(result.get(0).get("EMAIL_ADDRESS"), variable);
        else
            Assert.fail(String.format("Customer profile not found by USERNAME:%s", value));

    }

    @StepExt("phone number is received by username \"{value}\" and saved to variable \"{variable}\"")
    public void stepPhoneNumberIsReceivedByParameterAndSavedToVariable(String value, String variable) {
        String query = "";


        query = String.format("select DATA_PHONE from OCBADMIN.CUSTOMER where USER_NAME='%s'", value);


        List<Map<String, String>> result = SQLManager.getInstance().get().OCB_UAT_DB.executeQueryWithResults(query);

        if (result.size() > 0)
            utilSubSteps.stepSaveVariable(result.get(0).get("DATA_PHONE"), variable);
        else
            Assert.fail(String.format("Customer profile not found by USERNAME:%s", value));

    }

    @StepExt("OTP method is received by username \"{value}\" and saved to variable \"{variable}\"")
    public void stepOtpMethodIsReceivedByParameterAndSavedToVariable(String value, String variable) {
        String query = "";


        query = String.format("select OTP_CHANNEL from OCBADMIN.CUSTOMER where USER_NAME='%s'", value);


        List<Map<String, String>> result = SQLManager.getInstance().get().OCB_UAT_DB.executeQueryWithResults(query);

        if (result.size() > 0) {
            String otp_method_code = result.get(0).get("OTP_CHANNEL");
            switch (otp_method_code) {
                case "1":
                    utilSubSteps.stepSaveVariable("SMS", variable);
                    break;
                case "2":
                    utilSubSteps.stepSaveVariable("Email", variable);
                    break;
            }
        } else
            Assert.fail(String.format("Customer profile not found by USERNAME:%s", value));

    }

    @StepExt("passport ID is received by USERNAME \"{value}\" and saved to variable \"{variable}\"")
    public void stepPassportIDReceivedByParameterAndSavedToVariable(String value, String variable) {
        String query = "";


        query = String.format("select ID_PASSPORT from OCBADMIN.CUSTOMER where USER_NAME='%s'", value);


        List<Map<String, String>> result = SQLManager.getInstance().get().OCB_UAT_DB.executeQueryWithResults(query);

        if (result.size() > 0)
            utilSubSteps.stepSaveVariable(result.get(0).get("ID_PASSPORT"), variable);
        else
            Assert.fail(String.format("Customer profile not found by USERNAME:%s", value));
    }

    @StepExt("branch label is received by BranchNo index \"$branchNo\" and saved to the variable \"$variable\"")
    public void stepBranchLabelIsReceivedByBranchNoIndexAndSavedToTheVariable(String branchNo, String variable) {
        String query = "";
        ResultSet resultSet;


        query = String.format("select BRANCH_NAME from OCBADMIN.custom_branch where CODE = '%s'", branchNo);

        resultSet = SQLManager.getInstance().get().OCB_UAT_DB.executeQuery(query);

        try {
            while (resultSet.next()) {
                utilSubSteps.stepSaveVariable(resultSet.getString("BRANCH_NAME"), variable);
            }

            SQLManager.getInstance().get().OCB_UAT_DB.disconnect();

        } catch (Exception e) {
            Logger.getRootLogger().error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @StepExt("check that username \"$username\" is non-existing")
    public void stepCheckThatUsernameIsNonExisting(String username) {
        String query = "";
        ResultSet resultSet;

        query = String.format("select * from OCBADMIN.CUSTOMER where USER_NAME='%s'", username);

        resultSet = SQLManager.getInstance().get().OCB_UAT_DB.executeQuery(query);

        try {
            Boolean exist = resultSet.next();
            Assert.assertFalse(String.format("username '%s' is not non-existing", username), exist);
            SQLManager.getInstance().get().OCB_UAT_DB.disconnect();

        } catch (Exception e) {
            Logger.getRootLogger().error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @StepExt("account type in language \"{language}\" is received by Product ID \"{productID}\" and saved to variable \"{variable}\"")
    public void stepAccountTypeIsReceivedByProductIdAndSavedToVariable(String productID, String variable, LangUtils.Language language) {
        String query = "";
        if (language.equals(LangUtils.Language.EN_US))
            query = String.format("select ocb_name_en as name from product_mapping where productID='%s'", productID);
        else
            query = String.format("select ocb_name_vn as name from product_mapping where productID='%s'", productID);

        List<Map<String, String>> result = SQLManager.getInstance().get().LocalDB.executeQueryWithResults(query);

        if (result.size() > 0)
            utilSubSteps.stepSaveVariable(result.get(0).get("name"), variable);
        else
            Assert.fail(String.format("Product not found by ProductID:%s", productID));

    }


    @StepExt("interest rate is received by currency \"$currency\", term \"$term\", amount \"$amount\", name_en \"$name_en\" and saved to variable \"$variable\"")
    public void stepInterestRateIsReceivedByCurrencytermAmountNameEnAndSavedToVariable
            (String currency, String term, String amount, String name_en, String variable) {

        String query = "";

        query = String.format("select min(A.ONL_SAV_RATE) AS ONL_SAV_RATE from OCBADMIN.CUSTOM_ONLINE_SAV_RATES A " +
                "where  CURRENCY= '%s' and TERM = '%s' and (%s  between AMOUNT_MIN and AMOUNT_MAX) " +
                "and exists(select 1 from OCBADMIN.CUSTOM_ONL_SAV_PRODUCTS B where A.PRODUCT_CODE=B.PRODUCT_CODE and " +
                "exists(select 1 from OCBADMIN.CUSTOM_ONL_SAV_ACCT_TYPES C " +
                "where B.ACCOUNT_TYPE_ID= C.ACCOUNT_TYPE_ID and C.NAME_EN = '%s'))", currency, term + "M", amount, name_en);

        List<Map<String, String>> results = SQLManager.getInstance().get().OCB_UAT_DB.executeQueryWithResults(query);

        Double value = Double.parseDouble(results.get(0).get("ONL_SAV_RATE").toString());

        utilSubSteps.stepSaveVariable(value.toString(), variable);

    }


    @StepExt("city name is received by Branch code \"$branchCode\" and saved to the variable \"$variable\"")
    public void stepCityNameIsReceivedByBranchCodeAndSavedToTheVariable(String branchCode, String variable) {
        String query = "";
        ResultSet resultSet;

        query = String.format("select CITY_NAME from OCBADMIN.custom_city where CITY_ID = (select CITY_ID from OCBADMIN.custom_branch where code = '%s')", branchCode);
        resultSet = SQLManager.getInstance().get().OCB_UAT_DB.executeQuery(query);

        try {
            while (resultSet.next()) {
                utilSubSteps.stepSaveVariable(resultSet.getString("CITY_NAME"), variable);
            }

            SQLManager.getInstance().get().OCB_UAT_DB.disconnect();

        } catch (Exception e) {
            Logger.getRootLogger().error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @StepExt("perform SQL query to database \"{database}\":")
    public List<Map<String, String>> stepPerformSQLQueryToDatabase(String database, String query) {
        List<Map<String, String>> list = SQLManager.getInstance().get().getSqlDriver(database).executeQueryWithResults(query);
        return list;

    }

    @StepExt("count records returned by latest SQL query to database \"{database}\" and save result to a variable \"{variable}\"")
    public void stepCountRecordsReturnedByLatestSQLQuery(String database, String variable) {
        int count = SQLManager.getInstance().get().getSqlDriver(database).getLastQueryResults().size();
        utilSubSteps.stepSaveVariable(String.valueOf(count), variable);
    }

    @StepExt("latest SQL query to database \"{database}\" returned \"{count}\" records")
    public void stepLatestSQLQueryToDatabaseReturnedRecords(String database, int count) {
        Assert.assertEquals("Query results count does not match expected", count, SQLManager.getInstance().get().getSqlDriver(database).getLastQueryResults().size());
    }

    @StepExt("latest SQL query to database \"{database}\" results are recorded to variables")
    public void stepSQLQueryResultsAreRecordedToVariables(String database, ExamplesTable variables) {
        List<Map<String, String>> results = SQLManager.getInstance().get().getSqlDriver(database).getLastQueryResults();
        for (int i = 0; i < variables.getRowCount(); i++) {
            utilSubSteps.stepSaveVariable(results.get(0).get(variables.getRow(i).get("column")), variables.getRow(i).get("variable"));
        }

    }

    @StepExt("execute script in database \"{database}\":")
    public void stepExecuteScriptInDatabase(String database, String query) {
        SQLManager.getInstance().get().getSqlDriver(database).executeCallable(query);

    }

    @StepExt("parameter \"$parameter\" of dataset \"$dataset\" for scenario \"$scenario\" is modified with value \"$value\"")
    public void stepParameterOfDatasetForScenarioIsMpdifiedWithValue(String parameter, String dataset,
                                                                     String scenario, String value) {
        String query = "";
        query = String.format("UPDATE customers SET %s='%s' WHERE %s='%s'");

        if (dataset.equals("")) {

            query = String.format("UPDATE data SET value='%s' where " +
                            "scenario=(select id from scenarios where short_name='%s') and name='%s'",
                    value, scenario, parameter);
        } else {
            query = String.format("");

            query = String.format("UPDATE data SET value='%s' where " +
                            "scenario=(select id from scenarios where short_name='%s') and name='%s' and dataset='%s'",
                    value, scenario, parameter, dataset);
        }


        SQLManager.getInstance().get().LocalDB.executeUpdate(query);
        SQLManager.getInstance().get().LocalDB.disconnect();


    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("set parameter \"{parameter}\" in test database for current scenario and dataset to \"{value}\"")
    public void stepSetParameterForCurrentScenarioAndDataset(String parameter, String value) {

        String scenario = MetaParser.getCurrentScenarioShortname();
        String dataset = MetaParser.getCurrentDataset();
        String query = "";
        if (StringUtils.isNotEmpty(dataset))
            query = String.format("UPDATE data SET value='%s' where " +
                            "scenario=(select id from scenarios where short_name='%s') and dataset='%s' and name='%s'",
                    value, scenario, dataset, parameter);
        else
            query = String.format("UPDATE data SET value='%s' where " +
                            "scenario=(select id from scenarios where short_name='%s') and isnull(dataset) and name='%s'",
                    value, scenario, parameter);
        SQLManager.getInstance().get().LocalDB.executeUpdate(query);

    }

    @StepExt("get unique values from table \"{table}\"")
    public void stepGetUniqueValuesFromTable(String table) {
        String query = String.format("select * from %s\n" +
                "where id=(select min(id) from %s)", table, table);
        List<Map<String, String>> results = SQLManager.getInstance().get().LocalDB.executeQueryWithResults(query);
        if (results.size() == 0)
            throw new RuntimeException(String.format("No data left in '%s' table.", table));
        Map<String, String> result = results.get(0);
        String id = result.get("id");
        for (String param : result.keySet()) {
            if (!param.equalsIgnoreCase("id"))
                utilSubSteps.stepSaveVariable(result.get(param), param);
        }

        SQLManager.getInstance().get().LocalDB.executeUpdate(String.format("delete from %s\n" +
                "where id=%s", table, id));


    }
    @StepExt("get unique values from table \"{table}\" and do not delete")
    public void stepGetUniqueValuesFromTableNotDelete(String table) {
        String query = String.format("select * from %s\n" +
                "where id=(select min(id) from %s)", table, table);
        List<Map<String, String>> results = SQLManager.getInstance().get().LocalDB.executeQueryWithResults(query);
        if (results.size() == 0)
            throw new RuntimeException(String.format("No data left in '%s' table.", table));
        Map<String, String> result = results.get(0);
        String id = result.get("id");
        for (String param : result.keySet()) {
            if (!param.equalsIgnoreCase("id"))
                utilSubSteps.stepSaveVariable(result.get(param), param);
        }
    }
    @StepExt("get record \"{id}\" from table \"{table}\"")
    public void stepGetRecordFromTable(String id, String table) {
        String query = String.format("select * from %s\n" +
                "where id="+id+"", table, table);
        List<Map<String, String>> results = SQLManager.getInstance().get().LocalDB.executeQueryWithResults(query);
        if (results.size() == 0)
            throw new RuntimeException(String.format("No data left in '%s' table.", table));
        Map<String, String> result = results.get(0);
        id = result.get("id");
        for (String param : result.keySet()) {
            if (!param.equalsIgnoreCase("id"))
                utilSubSteps.stepSaveVariable(result.get(param), param);
        }

    }
    @StepExt("get unique values from table \"{table}\" where \"{column}\" equals \"{value}\"")
    public void stepGetUniqueValuesFromTableByCondition(String table, String column, String value){
        String query = String.format("select * from %s\n" +
                "where %s = %s LIMIT 1", table, column, value);
        System.out.println(query);
        List<Map<String, String>> results = SQLManager.getInstance().get().LocalDB.executeQueryWithResults(query);
        if (results.size() == 0)
            throw new RuntimeException(String.format("No data left in '%s' table.", table));
        Map<String, String> result = results.get(0);
        String id = result.get("id");
        for (String param : result.keySet()) {
            if (!param.equalsIgnoreCase("id"))
                utilSubSteps.stepSaveVariable(result.get(param), param);
        }

        SQLManager.getInstance().get().LocalDB.executeUpdate(String.format("delete from %s\n" +
                "where id=%s", table, id));
    }

    @StepExt("get unique values from table \"{table}\" where \"{column}\" less than \"{value}\"")
    public void stepGetUniqueValuesFromTableBefore(String table, String column, String value){
        String query = String.format("select * from %s\n" +
                "where %s < '%s' LIMIT 1", table, "date_format(str_to_date("+column+",'%d/%m/%Y'),'%Y-%m-%d')", value);
        System.out.println(query);
        List<Map<String, String>> results = SQLManager.getInstance().get().LocalDB.executeQueryWithResults(query);
        if (results.size() == 0)
            throw new RuntimeException(String.format("No data left in '%s' table.", table));
        Map<String, String> result = results.get(0);
        String id = result.get("id");
        for (String param : result.keySet()) {
            if (!param.equalsIgnoreCase("id"))
                utilSubSteps.stepSaveVariable(result.get(param), param);
        }

        SQLManager.getInstance().get().LocalDB.executeUpdate(String.format("delete from %s\n" +
                "where id=%s", table, id));
    }

    @StepExt("save next value of counter \"{counter}\" to variable \"{variable}\"")
    public void stepGetNextValueOfCounter(String counter, String variable) {
        String table = "counters";
        String query = String.format("select * from %s\n" +
                "where name='%s'", table, counter);
        List<Map<String, String>> results = SQLManager.getInstance().get().LocalDB.executeQueryWithResults(query);
        if (results.size() == 0)
            throw new RuntimeException(String.format("Counter with name '%s' was not found in '%s' table.", counter, table));
        Map<String, String> result = results.get(0);
        String id = result.get("id");
        long value = Long.parseLong(result.get("current_value"));
        long increment = Long.parseLong(result.get("increment"));
        long max_value = Long.parseLong(result.get("max_value"));

        value = value + increment;
        if (value > max_value)
            throw new RuntimeException(String.format("Counter with name '%s' has exceeded its max value. Make the required adjustments to data and reset the counter.", counter, table));

        utilSubSteps.stepSaveVariable(String.valueOf(value), variable);


        SQLManager.getInstance().get().LocalDB.executeUpdate(String.format("update %s set current_value=%s where name='%s'", table, value, counter));


    }

    @StepExt("first time password is received by phone \"$phone\" and saved to variable \"$variable\"")
    public void stepFTPisReceivedByPhoneAndSavedToVariable(String phone, String variable) {

        ResultSet resultSet = SQLManager.getInstance().get().ESB_UAT_DB.executeQuery(String.format(
                "select MESSAGETIMESTAMP," +
                        "to_char(regexp_substr(transactiondetail, " +
                        "'(la )(.+?)(<\\/Body>)', 1,1,null,2)) " +
                        "as first_time_password from soalogs " +
                        "where (1=1) " +
                        "and operationname = 'executeCorrespondenceOperation' " +
                        "and transactiondetail like '%%%s' || '</ns%%:PhoneNo>%%' " +
                        "order by MESSAGETIMESTAMP desc", phone));
        try {
            while (resultSet.next()) {
                String pwd = resultSet.getString("first_time_password");
                if (StringUtils.isNotEmpty(pwd) && !pwd.equalsIgnoreCase("null")) {
                    utilSubSteps.stepSaveVariable(pwd, variable);
                    return;
                }
            }
            Assert.fail("Could not find first-time password in database");
//            SQLManager.getInstance().get().ESB_UAT_DB.disconnect();
        } catch (Exception e) {
            Logger.getRootLogger().error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @StepExt("update record where \"{column}\" equals \"{value}\" from table \"{table}\"")
    public void stepUpdateRecord(String column, String value, String table){
        String query = String.format("select * from %s\n" +
                "where %s = '%s' LIMIT 1", table, column, value);
        System.out.println(query);
        List<Map<String, String>> results = SQLManager.getInstance().get().LocalDB.executeQueryWithResults(query);
        if (results.size() == 0)
            throw new RuntimeException(String.format("No data left in '%s' table.", table));
        Map<String, String> result = results.get(0);
        String id = result.get("id");
        String num_of_sub_card = result.get("times");
        if (num_of_sub_card.equals("2")){
            String queryUpdate = String.format("update %s set times='1' where id=%s", table, id);
            SQLManager.getInstance().get().LocalDB.executeUpdate(queryUpdate);
        }else{
            SQLManager.getInstance().get().LocalDB.executeUpdate(String.format("delete from %s\n" +
                    "where id=%s", table, id));
        }
    }
    @StepExt("get current dataset and save to variable \"{variable}\"")
    public void getCurrentDataset(String variable) {
        String dataset = MetaParser.getCurrentDataset();
        utilSubSteps.stepSaveVariable(dataset, variable);
    }
}
