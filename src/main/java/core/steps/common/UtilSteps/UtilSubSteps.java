package core.steps.common.UtilSteps;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.annotations.StepExt;
import core.steps.BaseSteps;
import core.utils.datapool.DataPool;
import core.utils.evaluationManager.ScriptUtils;
import core.utils.lang.LangUtils;
import core.utils.mail.MailUtils;
import core.utils.report.ReportUtils;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static core.utils.evaluationManager.EvaluationManager.getVariable;
import static core.utils.evaluationManager.EvaluationManager.setVariable;
import static core.utils.timeout.TimeoutUtils.loadingTimeout;
import static core.utils.timeout.TimeoutUtils.sleep;
import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by Akarpenko on 10.01.2018.
 */
public class UtilSubSteps extends BaseSteps {


    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("switch to datapool root")
    public void stepSwitchToDatapoolRoot() {
        DataPool.getInstance().switchToRoot();
    }


    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("switch to datapool section \"{section}\"")
    public void stepSwitchToDatapoolSection(String section) {
        if (section.equals("DEFAULT") || section.equals("ROOT"))
            DataPool.getInstance().switchToRoot();
        else
            DataPool.getInstance().switchToSection(section);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("switch to datapool section without dataset \"{section}\"")
    public void stepSwitchToDatapoolSectionWithoutDataset(String section) {
        if (section.equals("DEFAULT") || section.equals("ROOT"))
            DataPool.getInstance().switchToRoot();
        else
            DataPool.getInstance().switchToSectionWithoutDataset(section);
    }


    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("variable \"{variable}\" with value \"{value}\" is saved to datapool")
    public void stepSaveVariableToPool(String variable, String value) {

        Logger.getRootLogger().info(String.format("'%s' saved in datapool variable '%s'", value, variable));
        DataPool.getInstance().getPool().put(variable, value);
        DataPool.getInstance().savePool();
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("initialize datapool variable \"{variable}\" with value \"{value}\"")
    public void stepInitVariableInDatapool(String variable, String value) {

        if (DataPool.getInstance().getPool().get(variable) == null)
            stepSaveVariableToPool(variable, value);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("initialize variable \"{variable}\" with value \"{value}\"")
    public void stepInitVariableWithValue(String variable, String value) {

        if (StringUtils.isEmpty(getVariable(variable).toString())) {
            Logger.getRootLogger().info(String.format("'%s' saved in variable '%s'", value, variable));
            setVariable(variable, value);
        }

    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value \"{value}\" is saved to variable \"{variable}\"")
    public void stepSaveVariable(String value, String variable) {

        Logger.getRootLogger().info(String.format("'%s' saved in variable '%s'", value, variable));
        setVariable(variable, value);
    }


    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("wait for \"{count}\" seconds")
    public void stepWaitFor(int count) {
        sleep(count);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value \"$value1\" \"$operation\" \"$value2\"")
    public void stepValueOperation(String value1, String operation, String value2) {

        if(!checkCondition(value1, operation, value2)){
            Assert.fail(String.format("\nVerification is failed:\n1st value: %s\noperation: %s\n2nd value: %s",
                    value1, operation, value2));
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value \"{value1}\" equals \"{value2}\"")
    public void stepValueEquals(String value1, String value2) {
        Assert.assertEquals(value2, value1);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value \"{value1}\" smaller than \"{value2}\"")
    public void stepValueSmaller(String value1, String value2) {
        boolean result = false;
        if(Integer.parseInt(value1) < Integer.parseInt(value2)){
            result = true;
        }
        Assert.assertTrue(result);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value \"{value1}\" greater than \"{value2}\"")
    public void stepValueGreater(String value1, String value2) {
        boolean result = false;
        if(Integer.parseInt(value1) > Integer.parseInt(value2)){
            result = true;
        }
        Assert.assertTrue(result);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value \"{value1}\" contains \"{value2}\"")
    public void stepValueContains(String value1, String value2) {
        Assert.assertTrue(String.format("value1 (%s) not contains value2 (%s)", value1, value2), value1.contains(value2));
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value \"{value1}\" not contains \"{value2}\"")
    public void stepValueNotContains(String value1, String value2) {
        Assert.assertTrue(String.format("value1 (%s) contains value2 (%s)", value1, value2), !value1.contains(value2));

    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value \"{value1}\" matches \"{value2}\"")
    public void stepValueMatches(String value1, String value2) {
        Assert.assertTrue(String.format("value1 (%s) not matches value2 (%s)", value1, value2), value1.matches(value2));
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value \"{value1}\" is lowercase")
    public void stepValueLowercase(String value1) {
        if(!isStringLowerCase(value1)){
            Assert.fail(String.format("\nVerification is failed:\n1st value: %s", value1));
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value last of string \"{value1}\" is less than value last of string\"{value2}\"")
    public void stepValueLastOfString(String value1, String value2) {
        if (Integer.parseInt(value1.substring(value1.lastIndexOf(" ")+1)) > Integer.parseInt( value2.substring(value2.lastIndexOf(" ")+1))){
            Assert.fail(String.format("value1(%s) is greater than value2(%s)", value1, value2));
    }

    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value \"{value1}\" is not equals \"{value2}\"")
    public void stepValueIsNotEquals(String value1, String value2) {
        Assert.assertNotEquals(value2, value1);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("number value \"$value1\" is less than or equal to \"$value2\"")
    public void stepNumberValueIsLessThanOrEqualTo(String value1, String value2) {
        if (Integer.parseInt(value1) > Integer.parseInt(value2))
            Assert.fail(String.format("value1(%s) is greater than value2(%s)", value1, value2));
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("number value \"{value1}\" is less than \"{value2}\"")
    public void stepNumberValueIsLess(String value1, String value2) {
        if (Integer.parseInt(value1) >= Integer.parseInt(value2))
            Assert.fail(String.format("value1(%s) is greater than or equal to value2(%s)", value1, value2));
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("number value \"{value1}\" is greater than \"{value2}\"")
    public void stepNumberValueIsGreaterThan(String value1, String value2) {
        if (Double.parseDouble(value1) <= Double.parseDouble(value2))
            Assert.fail(String.format("(%s) is less than or equal to (%s)", value1, value2));
    }


    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("received one time password for user \"{login}\" (wait for \"{maxWaitTimeInSeconds}\" seconds) with any OTP method and saved its value to variable \"{variable}\"")
    public void stepGetOneTimePasswordForUserWithMethod(String username, int maxWaitTimeInSeconds, String variable) {
        String oneTimePassword = null;
        stepWaitFor(3);
        Date dateFrom = new Date(System.currentTimeMillis() - 15 * 60 * 1000); //min date Time = now - 10 minutes
        Date dateTo = new Date(System.currentTimeMillis() + 15 * 60 * 1000);
        Logger.getRootLogger().info(String.format("Current time: %s", new Date(System.currentTimeMillis())));
        Logger.getRootLogger().info(String.format("OTP search start time: %s", dateFrom));
        Logger.getRootLogger().info(String.format("OTP search end time: %s", dateTo));
        try {
            String otpMethod = System.getProperty("otp.preferredMethod");
            switch (otpMethod) {
                case "CUSTOM_MAIL_SERVER":
                    oneTimePassword = MailUtils.getOneTimePasswordFromService(username, dateFrom, dateTo, maxWaitTimeInSeconds);
                    break;
                case "OTP_DB":
                default:
                    oneTimePassword = MailUtils.getOneTimePasswordFromDatabaseWithMethod(username, dateFrom, dateTo, maxWaitTimeInSeconds);
                    break;
            }

        } catch (Exception e) {
//            //Secondly we try to ask user to manually enter OTP into Java text field
//            String message = String.format("Please input one time password value\n\nIn order to get OTP value you can use Email:\n*login = %s\n*password = %s", MailUtils.user, MailUtils.password);
//            oneTimePassword = JavaDialogInput.askUserToInputValue(message, maxWaitTimeInSeconds); //waiting up to 5 mins until user manually enter OTP password into the Java Dialog window
//            if (oneTimePassword == null)
            throw new RuntimeException("Could not receive one time password", e);
        }

        stepSaveVariable(oneTimePassword, variable);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("multivariable \"{multivariable}\" with separator \"{separator}\" is parsed to set of variable with prefix-name \"{name}\"")
    public void stepMultiVariableWithSeparatorIsParsedToSetOfVariablesWithName(String multivariable, String separator, String name) {

        String[] inputVariableArray = multivariable.split("-");

        for (int i = 0; i < inputVariableArray.length; i++) {
            stepSaveVariable(inputVariableArray[i], name + "_" + (i + 1));
        }

    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("max loading wait time is changed to \"{maxLoadingWaitTime}\" seconds")
    public void stepMaxLoadingWaitTimeIsChanged(String maxLoadingWaitTime) {
        int maxLoadingWaitTime_ev = Integer.parseInt(maxLoadingWaitTime);
        loadingTimeout = maxLoadingWaitTime_ev;
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("change language property to \"{language}\"")
    public void stepChangeLanguageProperty(String language) {
        String property = null;
        getActiveApplication().language = LangUtils.Language.valueOf(language);
        System.setProperty("lang", language);

        switch (language) {
            case "EN_US":
                property = System.getProperty("ocb.url.en_us");
                System.setProperty("ocb.url", property);
                break;
            case "VI_VN":
                property = System.getProperty("ocb.url.vi_vn");
                System.setProperty("ocb.url", property);
                break;
        }

        //
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("get days between \"{dateBefore}\" and \"{dateAfter}\"")
    public String stepDaysBetweenDates(String dateBefore, String dateAfter) {
        String result = "";
        try {
            long daysBetween = DAYS.between(LocalDate.parse(dateBefore), LocalDate.parse(dateAfter));
            result = String.valueOf(daysBetween);
        } catch (Exception e) {
            Assert.fail("Con not calculate date diff");
            e.printStackTrace();
        }
        return result;
    }

    private boolean isStringLowerCase(String str){

        //convert String to char array
        char[] charArray = str.toCharArray();

        for(int i=0; i < charArray.length; i++){

            //if the character is a letter
            if( Character.isLetter(charArray[i]) ){

                //if any character is not in lower case, return false
                if( !Character.isLowerCase( charArray[i] ))
                    return false;
            }
        }

        return true;

    }

    public boolean checkCondition(String value1, String operation, String value2){
        switch (operation.toLowerCase()) {
            case "equals":
                return value1.equals(value2);
            case "not equals":
                return !value1.equals(value2);
            case "contains":
                return value1.contains(value2);
            case "not contains":
                return !value1.contains(value2);
            case "matches":
                return value1.matches(value2);
            default:
                throw new AssertionError("Operation value = '" + operation + "' is not allowed. List of allowed operations: 'equals', 'not equals', 'contains', 'not contains', 'matches'");
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value \"{value1}\"  greater or equal than \"{value2}\"")
    public void stepValueGreaterOrEqual(String value1, String value2) {
        boolean result = false;
        if(Integer.parseInt(value1) >= Integer.parseInt(value2)){
            result = true;
        }
        Assert.assertTrue(result);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value \"{value1}\" is saved as nearest business day date in variable \"{value2}\"")
    public void stepSaveDateFromFieldAsBusinessDayInVariable(String value1, String value2) {
        try {
            Object currentDate = value1;
            String initialValue = String.valueOf(currentDate);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = dateFormat.parse(initialValue);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int day = calendar.get(Calendar.DAY_OF_WEEK);
            String value = null;

            if (day >= Calendar.MONDAY && day <= Calendar.FRIDAY) {
                value = initialValue;
            } else if (day == Calendar.SATURDAY) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                value = dateFormat.format(calendar.getTime());
            } else { //Calendar.SUNDAY
                calendar.add(Calendar.DAY_OF_YEAR, +1);
                value = dateFormat.format(calendar.getTime());
            }
            stepSaveVariable(value, value2);
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("value \"{value1}\" equals value \"{value2}\" or \"{value3}\"")
    public void stepValueEqualsValues(String value1, String value2, String value3) {
        if (value1.equals(value2)) {
            Assert.assertEquals(value1, value2);
        } if (value1.equals(value3)) {
            Assert.assertEquals(value1, value3);
        } else {Assert.assertEquals(value1, value2);}

    }

    public void takeScreenShot(){
        try {
            File scrFile = ((TakesScreenshot)getActiveMobileDriver()).getScreenshotAs(OutputType.FILE);
            String screenShotID = UUID.randomUUID().toString();
            File originalScreenshot = ((TakesScreenshot) getActiveMobileDriver()).getScreenshotAs(OutputType.FILE);
            Path path = Paths.get("target/output/screenshots/"+screenShotID+".png");
            Files.createDirectories(path.getParent());
            Files.copy(originalScreenshot.toPath(),  path, StandardCopyOption.REPLACE_EXISTING);
            byte[] originalScreenshot2 = ((TakesScreenshot) getActiveMobileDriver()).getScreenshotAs(OutputType.BYTES);
            float quality = Float.valueOf(ScriptUtils.env("allure.screenshotsQuality"));
            Allure.getLifecycle().addAttachment(screenShotID, "image/jpeg", "jpg", ReportUtils.compressImage(originalScreenshot2, "jpg", quality));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("read outlook email save to variable \"{variable}\"")
    public void stepGetContentOutlookMail(String variable) {
        String contentMail = MailUtils.getContentOutlookMail();
        stepSaveVariable(contentMail, variable);
    }
}
