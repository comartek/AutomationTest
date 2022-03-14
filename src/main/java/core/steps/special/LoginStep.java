package core.steps.special;

import core.annotations.StepExt;
import core.htmlElements.element.ExtendedWebElement;
import core.steps.common.ApplicationSteps.ApplicationSubSteps;
import core.steps.common.FieldSteps.FieldSubSteps;
import core.steps.common.PageSteps.PageSubSteps;
import core.steps.common.UtilSteps.UtilSubSteps;
import core.steps.sql.SQLSubSteps;
import core.utils.evaluationManager.EvaluationManager;
import core.utils.evaluationManager.ScriptUtils;
import core.utils.report.ReportUtils;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static core.steps.BaseSteps.getActiveDriver;
import static core.steps.BaseSteps.getCurrentPage;
import static core.utils.evaluationManager.EvaluationManager.evalVariable;

/**
 * Created by Akarpenko on 16.11.2017.
 */
public class LoginStep {

    private boolean forcedReLogin = false;

    FieldSubSteps fieldSubSteps = new FieldSubSteps();
    PageSubSteps pageSubSteps = new PageSubSteps();
    UtilSubSteps utilSubSteps = new UtilSubSteps();
    SQLSubSteps sqlSubSteps = new SQLSubSteps();
    ApplicationSubSteps applicationSubSteps = new ApplicationSubSteps();


    private static boolean logScreenshotForEverySubStep = true;

    private static void logScreenshotForCurrentSubStep(String message) {
        if (logScreenshotForEverySubStep) {
            System.out.println(message);
            ReportUtils.attachScreenshot(message);
        }
    }
    @When("NhaHang  is open website")
    @StepExt(screenshotsForSubsteps = true)
    public void logInNhaHangApplication() {
        String url = System.getProperty("win.url");
        pageSubSteps.stepOpenUrlInBrowser(url);
    }

    @When("open Win Nha Hang in browser mobile")
    @StepExt(screenshotsForSubsteps = true)
    public void whenOpenUrlInBrowserMobile() {
        String url = System.getProperty("win.url");
        pageSubSteps.stepOpenUrlInBrowserMobile(url);
    }

    @When("open Win Nha Hang in Safari browser mobile")
    @StepExt(screenshotsForSubsteps = true)
    public void whenOpenUrlInSafariBrowserMobile() {
        pageSubSteps.stepOpenUrlInSafariBrowserMobile();
    }

    @Given("Rosy ipad application is logged in via user \"$login\" / \"$password\"")
    @StepExt(screenshotsForSubsteps = true)
    public void logInIpadRosyApplication(String login, String password) {
        String login_ev = EvaluationManager.evalVariable(login);
        String password_ev = EvaluationManager.evalVariable(password);
        pageSubSteps.stepPageIsLoaded("EnvironmentPage");
        List<WebElement> tempListEmvironment = fieldSubSteps.isDisplayed("DEV_Button", 1);

        if(tempListEmvironment.size() != 0){
            fieldSubSteps.stepMobileFieldIsClicked("DEV_Button");
        }

        pageSubSteps.stepPageIsLoaded("FaceIDIpadPage");
        List<WebElement> tempListFaceID = fieldSubSteps.isDisplayed("DONT_ALLOW_Face_ID_BUTTON", 1);

        if(tempListFaceID.size() != 0){
            fieldSubSteps.stepMobileFieldIsClicked("DONT_ALLOW_Face_ID_BUTTON");
        }

        pageSubSteps.stepPageIsLoaded("FaceIDIpadPage");
        List<WebElement> tempListUsingApp = fieldSubSteps.isDisplayed("Allow_Using_App_Button", 1);

        if(tempListUsingApp.size() != 0){
            fieldSubSteps.stepMobileFieldIsClicked("Allow_Using_App_Button");
        }

        pageSubSteps.stepPageIsLoaded("FaceIDIpadPage");
        List<WebElement> tempListTouchID = fieldSubSteps.isDisplayed("Cancel_TouchID_Button", 1);
        if(tempListTouchID.size() != 0){
            fieldSubSteps.stepMobileFieldIsClicked("Cancel_TouchID_Button");
        }

        pageSubSteps.stepPageIsLoaded("IpadLogin_FirstPage");

        List<WebElement> tempList = fieldSubSteps.isDisplayed("NotUser_Button", 1);

        if(tempList.size() != 0){
            fieldSubSteps.stepMobileFieldIsClicked("NotUser_Button");
        }
        fieldSubSteps.stepMobileFieldIsClicked("Login_Button");
        pageSubSteps.stepPageIsLoaded("IpadLogin_CreateUserPage");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("UserID_Input", login_ev);
        fieldSubSteps.stepMobileFieldIsClicked("Password_Input");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Password_Input", password_ev);
    }
    @When("Rosy application is logged in via user \"$login\" / \"$password\"")
    @StepExt(screenshotsForSubsteps = true)
    public void logInRosyApplication(String login, String password) {
        String url = System.getProperty("rosy.url");
        String login_ev = EvaluationManager.evalVariable(login);
        String password_ev = EvaluationManager.evalVariable(password);
        ReportUtils.attachVariables(new String[]{"url", "login", "password"}, new String[]{url, login_ev, password_ev});

        pageSubSteps.stepOpenUrlInBrowser(url);
//        pageSubSteps.stepPageIsLoaded("DevPage");
//        ExtendedWebElement webElementDev = ((ExtendedWebElement) getCurrentPage().getField("Dev_Button"));
//        if (webElementDev.existsAndDisplayed()) {
//            fieldSubSteps.stepFieldIsClicked("Dev_Button");
//        }
        pageSubSteps.stepPageIsLoaded("LoginFirstPage");
        ExtendedWebElement webElement = ((ExtendedWebElement) getCurrentPage().getField("NotUser_Button"));
        if (webElement.existsAndDisplayed()) {
            fieldSubSteps.stepFieldIsClicked("NotUser_Button");
        }
        fieldSubSteps.stepFieldIsClicked("Login_Button");
        pageSubSteps.stepPageIsLoaded("LoginInputPage");
        fieldSubSteps.stepFieldIsFilledWithValue("Email_Input", login_ev);
        getCurrentPage().getFieldSafe("Email_Input").deactivateElementBorder();
        getActiveDriver().findElement(new By.ByXPath("//input[@data-testid='roundedPinCodeInput']")).sendKeys(password_ev);
        utilSubSteps.stepWaitFor(25);
        pageSubSteps.stepPageIsLoaded("BeginTrialHomePage");
        ExtendedWebElement webElementDev = ((ExtendedWebElement) getCurrentPage().getField("Begin_Trial_Button"));
        if (webElementDev.existsAndDisplayed()) {
            fieldSubSteps.stepFieldIsClicked("Begin_Trial_Button");
        }
    }

    @When("Rosy application is logged in  failed via user \"$login\" / \"$password\"")
    @StepExt(screenshotsForSubsteps = true)
    public void logInFailedRosyApplication(String login, String password) {
        String url = System.getProperty("rosy.url");
        String login_ev = EvaluationManager.evalVariable(login);
        String password_ev = EvaluationManager.evalVariable(password);
        ReportUtils.attachVariables(new String[]{"url", "login", "password"}, new String[]{url, login_ev, password_ev});

        pageSubSteps.stepOpenUrlInBrowser(url);
        pageSubSteps.stepPageIsLoaded("LoginFirstPage");
        ExtendedWebElement webElement = ((ExtendedWebElement) getCurrentPage().getField("NotUser_Button"));
        if (webElement.existsAndDisplayed()) {
            fieldSubSteps.stepFieldIsClicked("NotUser_Button");
        }
        fieldSubSteps.stepFieldIsClicked("Login_Button");
        pageSubSteps.stepPageIsLoaded("LoginInputPage");
        fieldSubSteps.stepFieldIsFilledWithValue("Email_Input", login_ev);
        getCurrentPage().getFieldSafe("Email_Input").deactivateElementBorder();
        getActiveDriver().findElement(new By.ByXPath("//input[@data-testid='roundedPinCodeInput']")).sendKeys(password_ev);
        WebDriverWait webDriverWait = new WebDriverWait(getActiveDriver(), 30);
//        WebElement webelement  = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@aria-label='home_icon'])[2]")));
    }

//    }
@When("Rosy create email auto")
@StepExt(screenshotsForSubsteps = true)
public void CreateaccountRosy() {
//    pageSubSteps.stepPageIsLoaded("DevHomePage");
//    ExtendedWebElement webElementDev = ((ExtendedWebElement) getCurrentPage().getField("Dev_Button"));
//    if (webElementDev.existsAndDisplayed()) {
//        fieldSubSteps.stepFieldIsClicked("Dev_Button");
//    }
    pageSubSteps.stepPageIsLoaded("LoginFirstPage");
    ExtendedWebElement webElement = ((ExtendedWebElement) getCurrentPage().getField("NotUser_Button"));
    if (webElement.existsAndDisplayed()) {
        fieldSubSteps.stepFieldIsClicked("NotUser_Button");
    }
    fieldSubSteps.stepFieldIsClicked("CreateAccount_Button");
    pageSubSteps.stepPageIsLoaded("InputEmail");
    fieldSubSteps.stepFieldIsClicked("Email_Input");
    fieldSubSteps.stepFieldIsFilledWithValueAuto("Email_Input");
    getCurrentPage().getFieldSafe("Email_Input").deactivateElementBorder();
    WebDriverWait webDriverWait = new WebDriverWait(getActiveDriver(), 30);
    fieldSubSteps.stepFieldIsClicked("Contiune_Button");
//        WebElement webelement  = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@aria-label='home_icon'])[2]")));

}
    @When("Rosy wait Home_icon exists")
    @StepExt(screenshotsForSubsteps = true)
    public void WaitHome_Icon() {
        WebDriverWait webDriverWait = new WebDriverWait(getActiveDriver(), 30);
        WebElement webelement  = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@aria-label='home_icon'])[2]")));

    }


    @When("Rosy account with FirstName \"$firstName\" and LastName \"$lastName\"")
    @StepExt(screenshotsForSubsteps = true)
    public void Nameaccount(String firstName, String lastName) {

//        String url = System.getProperty("rosy.url");
        String firstName_ev = EvaluationManager.evalVariable(firstName);
        String lastName_ev = EvaluationManager.evalVariable(lastName);
        ReportUtils.attachVariables(new String[]{"firstName", "lastName"}, new String[]{firstName, lastName});

//        pageSubSteps.stepOpenUrlInBrowser(url);
        pageSubSteps.stepPageIsLoaded("Name_Account");
        fieldSubSteps.stepFieldIsFilledWithValue("firstName_text", firstName_ev);
        getCurrentPage().getFieldSafe("firstName_text").deactivateElementBorder();
        fieldSubSteps.stepFieldIsFilledWithValue("LastName_text", lastName_ev);
        getCurrentPage().getFieldSafe("firstName_text").deactivateElementBorder();
        fieldSubSteps.stepFieldIsClicked("Continue");

    }

    @When("Rosy create account with password \"$password\"")
    @StepExt(screenshotsForSubsteps = true)
    public void PasswordCreate(String password) {
//        String url = System.getProperty("rosy.url");
        String password_ev = EvaluationManager.evalVariable(password);
        ReportUtils.attachVariables(new String[]{"password"}, new String[]{password_ev});

//        pageSubSteps.stepOpenUrlInBrowser(url);
        pageSubSteps.stepPageIsLoaded("SetPINW");

        getActiveDriver().findElement(new By.ByXPath("//*[@aria-label='roundedPinCodeInput']")).sendKeys(password_ev);

        WebDriverWait webDriverWait = new WebDriverWait(getActiveDriver(), 30);
    }


    @Given("Rosy mobile home page is opened for user \"$login\" / \"$password\"")
    @StepExt(screenshotsForSubsteps = true)
    public void openRosyMobileHomePage(String login, String password)
    {
        String login_ev = EvaluationManager.evalVariable(login);
        String password_ev = EvaluationManager.evalVariable(password);
//        pageSubSteps.stepPageIsLoaded("EnvironmentPage");
//        List<WebElement> tempListEmvironment = fieldSubSteps.isDisplayed("DEV_Button", 1);
//
//        if(tempListEmvironment.size() != 0){
//            fieldSubSteps.stepMobileFieldIsClicked("DEV_Button");
//        }
//
//        pageSubSteps.stepPageIsLoaded("FaceIDPageIphone");
//        List<WebElement> tempListFaceID = fieldSubSteps.isDisplayed("Cancel_FaceID_Button", 1);
//
//        if(tempListFaceID.size() != 0){
//            fieldSubSteps.stepMobileFieldIsClicked("Cancel_FaceID_Button");
//        }
//
//        pageSubSteps.stepPageIsLoaded("FaceIDPageIphone");
//        List<WebElement> tempAllowFaceID = fieldSubSteps.isDisplayed("DONT_ALLOW_Face_ID_BUTTON", 1);
//
//        if(tempAllowFaceID.size() != 0){
//            fieldSubSteps.stepMobileFieldIsClicked("DONT_ALLOW_Face_ID_BUTTON");
//        }


        pageSubSteps.stepPageIsLoaded("IphoneLogin_FirstPage");

        List<WebElement> tempList = fieldSubSteps.isDisplayed("NotUser_Button", 1);

        if(tempList.size() != 0){
            fieldSubSteps.stepMobileFieldIsClicked("NotUser_Button");
        }
        fieldSubSteps.stepMobileFieldIsClicked("Login_Button");

//        utilSubSteps.stepWaitFor(10);

        pageSubSteps.stepPageIsLoaded("IphoneLogin_CreateUserPage");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("UserID_Input", login_ev);
        fieldSubSteps.stepMobileFieldIsClicked("Password_Input");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Password_Input", password_ev);

//        pageSubSteps.stepPageIsLoaded("FaceIDPageIphone");
//        List<WebElement> tempListAllowFaceID = fieldSubSteps.isDisplayed("DONT_ALLOW_Face_ID_BUTTON_2", 1);
//
//        if(tempListAllowFaceID.size() != 0){
//            fieldSubSteps.stepMobileFieldIsClicked("DONT_ALLOW_Face_ID_BUTTON_2");
//        }
//        pageSubSteps.stepPageIsLoaded("FaceIDPageIphone");
//        List<WebElement> tempTrialFree = fieldSubSteps.isDisplayed("Begin_Free_Trial_Button", 1);
//
//        if(tempTrialFree.size() != 0){
//            fieldSubSteps.stepMobileFieldIsClicked("Begin_Free_Trial_Button");
//        }

    }

    @When("Rosy ios home page is opened for user \"$login\" / \"$password\"")
    @StepExt(screenshotsForSubsteps = true)
    public void openRosyiOSHomePage(String login, String password)
    {
        String login_ev = EvaluationManager.evalVariable(login);
        String password_ev = EvaluationManager.evalVariable(password);
        pageSubSteps.stepPageIsLoaded("EnvironmentPage");
        List<WebElement> tempListEmvironment = fieldSubSteps.isDisplayed("DEV_Button", 1);

        if(tempListEmvironment.size() != 0){
            fieldSubSteps.stepMobileFieldIsClicked("DEV_Button");
        }
        pageSubSteps.stepPageIsLoaded("IphoneLogin_FirstPage");

        List<WebElement> tempList = fieldSubSteps.isDisplayed("NotUser_Button", 1);

        if(tempList.size() != 0){
            fieldSubSteps.stepMobileFieldIsClicked("NotUser_Button");
        }
        fieldSubSteps.stepMobileFieldIsClicked("Login_Button");
        pageSubSteps.stepPageIsLoaded("IphoneLogin_CreateUserPage");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("UserID_Input", login_ev);
        fieldSubSteps.stepMobileFieldIsClicked("Password_Input");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Password_Input", password_ev);
    }

    @Given("Rosy mobile home page is create for user \"$login\" / \"$password\" with firstname \"$firstname\" and lastname \"$lastname\"")
    @StepExt(screenshotsForSubsteps = true)
    public void createRosyMobileHomePage(String login, String password,String lastname, String firstname)
    {
        String login_ev = EvaluationManager.evalVariable(login);
        String password_ev = EvaluationManager.evalVariable(password);
        String lname_ev = EvaluationManager.evalVariable(lastname);
        String fname_ev = EvaluationManager.evalVariable(firstname);
        pageSubSteps.stepPageIsLoaded("EnvironmentPage");
        List<WebElement> tempListEmvironment = fieldSubSteps.isDisplayed("DEV_Button", 1);

        if(tempListEmvironment.size() != 0){
            fieldSubSteps.stepMobileFieldIsClicked("DEV_Button");
        }

        pageSubSteps.stepPageIsLoaded("IphoneLogin_FirstPage");

        List<WebElement> tempList = fieldSubSteps.isDisplayed("NotUser_Button", 1);

        if(tempList.size() != 0){
            fieldSubSteps.stepMobileFieldIsClicked("NotUser_Button");
        }
        fieldSubSteps.stepMobileFieldIsClicked("CreateAccount_Button");
        pageSubSteps.stepPageIsLoaded("IphoneLogin_CreateUserPage");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("UserID_Input", login_ev);
        fieldSubSteps.stepMobileFieldIsClicked("Click_Logo_Button");
        fieldSubSteps.stepMobileFieldIsClicked("Continue_Button");
        fieldSubSteps.stepMobileFieldIsClicked("Confirm_Button");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("First_Name_Input", fname_ev);
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Last_Name_Input", lname_ev);
        fieldSubSteps.stepMobileFieldIsClicked("Click_Lname_Label");
        fieldSubSteps.stepMobileFieldIsClicked("Continue_Button");
        fieldSubSteps.stepMobileFieldIsClicked("Password_Input");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Password_Input", password_ev);
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Password_Input", password_ev);
        fieldSubSteps.stepMobileFieldIsClicked("Agree_And_Continue_Button");
//        fieldSubSteps.stepMobileFieldIsClicked("Continue_Button");
    }
    @Given("Rosy ipad application is create via user \"$login\" / \"$password\" with firstname \"$firstname\" and lastname \"$lastname\"")
    @StepExt(screenshotsForSubsteps = true)
    public void createRosyApplication(String login, String password,String lastname, String firstname) {
        String login_ev = EvaluationManager.evalVariable(login);
        String password_ev = EvaluationManager.evalVariable(password);
        String lname_ev = EvaluationManager.evalVariable(lastname);
        String fname_ev = EvaluationManager.evalVariable(firstname);
        pageSubSteps.stepPageIsLoaded("EnvironmentPage");
        List<WebElement> tempListEmvironment = fieldSubSteps.isDisplayed("DEV_Button", 1);

        if(tempListEmvironment.size() != 0){
            fieldSubSteps.stepMobileFieldIsClicked("DEV_Button");
        }
        pageSubSteps.stepPageIsLoaded("IpadLogin_FirstPage");

        List<WebElement> tempList = fieldSubSteps.isDisplayed("NotUser_Button", 1);

        if(tempList.size() != 0){
            fieldSubSteps.stepMobileFieldIsClicked("NotUser_Button");
        }
        fieldSubSteps.stepMobileFieldIsClicked("CreateAccount_Button");
        pageSubSteps.stepPageIsLoaded("IpadLogin_CreateUserPage");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("UserID_Input", login_ev);
//
        fieldSubSteps.stepMobileFieldIsClicked("Continue_Button");
        fieldSubSteps.stepMobileFieldIsClicked("Confirm_Button");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("First_Name_Input", fname_ev);
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Last_Name_Input", lname_ev);
//        fieldSubSteps.stepMobileFieldIsClicked("Click_Lname_Label");
        fieldSubSteps.stepMobileFieldIsClicked("Continue_Button");
        fieldSubSteps.stepMobileFieldIsClicked("Password_Input");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Password_Input", password_ev);
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Password_Input", password_ev);
        fieldSubSteps.stepMobileFieldIsClicked("Agree_And_Continue_Button");

    }

    @When("Rosy mobile change \"$password\" with new PIN value \"$pin\"")
    @StepExt(screenshotsForSubsteps = true)
    public void changePINRosyApplication(String password) {

        String password_ev = EvaluationManager.evalVariable(password);

        fieldSubSteps.stepMobileFieldIsClicked("Password_Input");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Password_Input", password_ev);
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Password_Input", password_ev);
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Password_Input", password_ev);


    }

    // create auto email contacs
    @When("mobile field is create new contacts")
    @StepExt(screenshotsForSubsteps = true)
    public void createEmailNewContact(){
        fieldSubSteps.stepMobileFieldIsFilledWithValueAuto();
    }

    // Create auto email



    @When("Rosy loading url")
    @StepExt(screenshotsForSubsteps = true)
    public void logInRosyApplication() {
        String url = System.getProperty("rosy.url");
        ReportUtils.attachVariables(new String[]{"url"}, new String[]{url });
        pageSubSteps.stepOpenUrlInBrowser(url);
    }

    @When("field srcoll up web")
    @StepExt(screenshotsForSubsteps = true)
    public void fieldScrollUpWeb() {
        fieldSubSteps.stepFieldScrollUPWeb();
    }

    @When("field srcoll down screen web")
    @StepExt(screenshotsForSubsteps = true)
    public void fieldScrollDownWeb() {
        fieldSubSteps.stepFieldScrollDownWeb();
    }

    // mở popup giữa các màn hình
    @When("Popup signin google with email \"$emailev\" and lastname \"$passwordev\"")
    @StepExt(screenshotsForSubsteps = true)
    public void logInGoogleApplication(String emailev, String passwordev) {

        // Store the current window handle
        String winHandleBefore = getActiveDriver().getWindowHandle();
        // Perform the click operation that opens new window
        // Switch to new window opened
        for(String winHandle : getActiveDriver().getWindowHandles()){
            getActiveDriver().switchTo().window(winHandle);
        }
        // Perform the actions on new window
        pageSubSteps.stepPageIsLoaded("GG_email_Screen_W");
        fieldSubSteps.stepFieldIsClicked("email");
        fieldSubSteps.stepFieldIsFilledWithValue("email", emailev);
        fieldSubSteps.stepFieldIsClicked("Next");
//        fieldSubSteps.stepFieldIsClicked("Next");
        utilSubSteps.stepWaitFor(5);
        pageSubSteps.stepPageIsLoaded("Gg_Pass_Screen_W");
        fieldSubSteps.stepFieldIsClicked("password");
        fieldSubSteps.stepFieldIsFilledWithValue("password", passwordev);
        fieldSubSteps.stepFieldIsClicked("Next");
        utilSubSteps.stepWaitFor(5);
        pageSubSteps.stepInvisiblePageIsLoaded("Allow_Screen_W");
//        pageSubSteps.stepPageIsLoaded("Allow_Screen_W");
        fieldSubSteps.stepFieldScrollUPWeb();
        utilSubSteps.stepWaitFor(5);
        fieldSubSteps.stepFieldIsClicked("Allow_Button");
        utilSubSteps.stepWaitFor(25);
        // Close the new window, if that window no more required
        getActiveDriver().close();
        // Switch back to original browser (first window)
        getActiveDriver().switchTo().window(winHandleBefore);
        // Continue with original browser (first window)

    }


    @When("Popup signin box with email \"$emailev\" and lastname \"$passwordev\"")
    @StepExt(screenshotsForSubsteps = true)
    public void logInBoxApplication(String emailev, String passwordev) {

        // Store the current window handle
        String winHandleBefore = getActiveDriver().getWindowHandle();
        // Perform the click operation that opens new window
//        pageSubSteps.stepPageIsLoaded("Choose_provider_Vault_W");
//        fieldSubSteps.stepFieldIsClicked("Box_Button");
        // Switch to new window opened
        for(String winHandle : getActiveDriver().getWindowHandles()){
            getActiveDriver().switchTo().window(winHandle);
        }
        // Perform the actions on new window
        pageSubSteps.stepPageIsLoaded("Box_Screen_W");
        fieldSubSteps.stepFieldIsClicked("email");
        fieldSubSteps.stepFieldIsFilledWithValue("email", emailev);
        fieldSubSteps.stepFieldIsClicked("password");
        fieldSubSteps.stepFieldIsFilledWithValue("password", passwordev);
        fieldSubSteps.stepFieldIsClicked("Authorize_Button");
        utilSubSteps.stepWaitFor(5);
        pageSubSteps.stepPageIsLoaded("Box_Access_Screen_W");
        fieldSubSteps.stepFieldIsClicked("Grant_Access_to_Box_Button");
        utilSubSteps.stepWaitFor(25);
        // Close the new window, if that window no more required
        getActiveDriver().close();
        // Switch back to original browser (first window)
        getActiveDriver().switchTo().window(winHandleBefore);
        // Continue with original browser (first window)

    }

    @When("Popup signin onedriver with email \"$emailev\" and lastname \"$passwordev\"")
    @StepExt(screenshotsForSubsteps = true)
    public void logInOneDriverApplication(String emailev, String passwordev) {

        // Store the current window handle
        String winHandleBefore = getActiveDriver().getWindowHandle();
        // Perform the click operation that opens new window
        // Switch to new window opened
        for(String winHandle : getActiveDriver().getWindowHandles()){
            getActiveDriver().switchTo().window(winHandle);
        }
        // Perform the actions on new window
        pageSubSteps.stepPageIsLoaded("OneDrive_Screen_W");
        fieldSubSteps.stepFieldIsClicked("email");
        fieldSubSteps.stepFieldIsFilledWithValue("email", emailev);
        fieldSubSteps.stepFieldIsClicked("Next");
//        fieldSubSteps.stepFieldIsClicked("Next");
        utilSubSteps.stepWaitFor(5);
        fieldSubSteps.stepFieldIsClicked("password");
        fieldSubSteps.stepFieldIsFilledWithValue("password", passwordev);
        fieldSubSteps.stepFieldIsClicked("Next");
        utilSubSteps.stepWaitFor(5);
        pageSubSteps.stepInvisiblePageIsLoaded("OneDrive_Access_Screen_W");
        fieldSubSteps.stepFieldScrollUPWeb();
        utilSubSteps.stepWaitFor(5);
        fieldSubSteps.stepFieldIsClicked("Yes_Button");
        utilSubSteps.stepWaitFor(25);
        // Close the new window, if that window no more required
        getActiveDriver().close();
        // Switch back to original browser (first window)
        getActiveDriver().switchTo().window(winHandleBefore);
        // Continue with original browser (first window)

    }

    @When("Popup signin outlook with email \"$emailev\" and lastname \"$passwordev\"")
    @StepExt(screenshotsForSubsteps = true)
    public void logInOutLookApplication(String emailev, String passwordev) {

        // Store the current window handle
        String winHandleBefore = getActiveDriver().getWindowHandle();
        // Perform the click operation that opens new window
        // Switch to new window opened
        for(String winHandle : getActiveDriver().getWindowHandles()){
            getActiveDriver().switchTo().window(winHandle);
        }
        // Perform the actions on new window
        pageSubSteps.stepPageIsLoaded("OneDrive_Screen_W");
        fieldSubSteps.stepFieldIsClicked("email");
        fieldSubSteps.stepFieldIsFilledWithValue("email", emailev);
        fieldSubSteps.stepFieldIsClicked("Next");
//        fieldSubSteps.stepFieldIsClicked("Next");
        utilSubSteps.stepWaitFor(5);
        fieldSubSteps.stepFieldIsClicked("password");
        fieldSubSteps.stepFieldIsFilledWithValue("password", passwordev);
        fieldSubSteps.stepFieldIsClicked("Next");
        utilSubSteps.stepWaitFor(5);
        pageSubSteps.stepInvisiblePageIsLoaded("OneDrive_Access_Screen_W");
        fieldSubSteps.stepFieldScrollUPWeb();
        utilSubSteps.stepWaitFor(5);
        fieldSubSteps.stepFieldIsClicked("Yes_Button");
        utilSubSteps.stepWaitFor(25);
        // Close the new window, if that window no more required
        getActiveDriver().close();
        // Switch back to original browser (first window)
        getActiveDriver().switchTo().window(winHandleBefore);
        // Continue with original browser (first window)

    }

    // create account with email contact
    // Create auto email

    @When("Rosy iphone is create contacts email \"$email\" / password \"$password\"")
    @StepExt(screenshotsForSubsteps = true)
    public void createEmailContactHomePageAuto( String email,String password)
    {

        String email_ev = EvaluationManager.evalVariable(email);
        String password_ev = EvaluationManager.evalVariable(password);
//        pageSubSteps.stepPageIsLoaded("EnvironmentPage");
//        List<WebElement> tempListEmvironment = fieldSubSteps.isDisplayed("DEV_Button", 1);
//
//        if(tempListEmvironment.size() != 0){
//            fieldSubSteps.stepMobileFieldIsClicked("DEV_Button");
//        }

        pageSubSteps.stepPageIsLoaded("IphoneLogin_FirstPage");

        List<WebElement> tempList = fieldSubSteps.isDisplayed("NotUser_Button", 10);

        if(tempList.size() != 0){
            fieldSubSteps.stepMobileFieldIsClicked("NotUser_Button");
        }
        fieldSubSteps.stepMobileFieldIsClicked("Login_Button");
        pageSubSteps.stepPageIsLoaded("IphoneLogin_CreateUserPage");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("UserID_Input", email_ev);
        fieldSubSteps.stepMobileFieldIsClicked("Password_Input");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Password_Input", password_ev);
//        fieldSubSteps.stepMobileFieldIsClicked("Continue_Button");
    }

    @When("Rosy iPhone is login free trial account with email \"$email\" / password \"$password\"")
    @StepExt(screenshotsForSubsteps = true)
    public void LoginEmailFreeTrialHomePageAutoiOS( String email,String password)
    {
        String email_ev = EvaluationManager.evalVariable(email);
        String password_ev = EvaluationManager.evalVariable(password);
        pageSubSteps.stepPageIsLoaded("IphoneLogin_FirstPage");
        fieldSubSteps.stepMobileFieldIsClicked("Login_Button");
        pageSubSteps.stepPageIsLoaded("IphoneLogin_CreateUserPage");
        fieldSubSteps.stepMobileFieldIsClicked("UserID_Free_Trial_Input");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("UserID_Free_Trial_Input", email_ev);
        utilSubSteps.stepWaitFor(2);
        fieldSubSteps.stepMobileFieldIsClicked("Password_Free_Trial_Input");
        fieldSubSteps.stepMobileFieldIsFilledWithValue("Password_Free_Trial_Input", password_ev);
    }

}