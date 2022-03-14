package core.steps.common.ApplicationSteps;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.annotations.UseFullDesktopScreenshots;
import core.steps.BaseSteps;
import core.utils.report.ReportUtils;
import core.utils.timeout.TimeoutUtils;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;

import java.util.Set;

import static core.utils.evaluationManager.EvaluationManager.evalVariable;

/**
 * Created by Akarpenko on 15.11.2017.
 */
public class ApplicationSteps extends BaseSteps {

    ApplicationSubSteps applicationSubSteps = new ApplicationSubSteps();


    @NeverTakeAllureScreenshotsForThisStep
    @Given("application \"$application\" is opened")
    public void givenOpenApplication(String application) {
        String application_ev = evalVariable(application);
        applicationSubSteps.stepOpenApplication(application_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("application \"$application\" is opened env")
    public void givenChooseEnv(String application) {
        String application_ev = evalVariable(application);
        applicationSubSteps.stepOpenApplication(application_ev);
    }


    @NeverTakeAllureScreenshotsForThisStep
    @When("close application \"$application\"")
    public void whenCloseApplication(String application){
        String application_ev = evalVariable(application);
        applicationSubSteps.stepCloseApplication(application_ev);
    }

    /**
     * Performs a switch to the browser tab/window by a user-defined identifier
     * (NOT the handle provided by driver.getWindowHandle())
     * @param window Name of a browser window to switch to.
     *               If this name already exists in application's windows map, a switch to that window is performed.
     *               If there is no such name, it is added to the map,
     *               linked to a first found window that is not yet in the map.
     *               "MainWindow" is a predefined name for the browser window opened on startup.
     */
    @UseFullDesktopScreenshots(ReportUtils.FullDesktopScreenshotMode.DO_BOTH_WEBDRIVER_AND_FULL_DESKTOP_SCREENSHOTS)
    @When("switch to window \"$window\"")
    public void whenSwitchToWindow(String window) {
        String window_ev = evalVariable(window);
        applicationSubSteps.stepSwitchToWindow(window_ev);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("close window \"$window\"")
    public void whenCloseWindow(String window) {
        String window_ev = evalVariable(window);
        applicationSubSteps.stepCloseWindow(window_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("current window is closed")
    public void whenCloseWindow() {
        applicationSubSteps.stepCheckCurrentWindowIsClosed();
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("maximize current window")
    public void whenMaximizeCurrentWindow() {
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        applicationSubSteps.stepMaximizeCurrentWindow();
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("activate current window")
    public void whenActivateCurrentWindow() {
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        applicationSubSteps.stepActivateCurrentWindow();
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("current URL is added to Internet Explorer compatibility view setting")
    public void whenCurrentURLAddedToIECompatibilityViewSetting() {
        applicationSubSteps.stepCurrentURLAddedToIECompatibilityViewSetting();
    }

    /*
    * This step is necessary to emulate "Internet Explorer compatibility view setting" modification (therefore avoid the necessity of using whenCurrentURLAddedToIECompatibilityViewSetting() method)
    */
    @NeverTakeAllureScreenshotsForThisStep
    @When("set registry key FEATURE_BROWSER_EMULATION value iexplore.exe = 9999")
    public void whenSetRegistryKeyForIexplore() {
        applicationSubSteps.stepSetRegistryKeyForIexplore();
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("process \"$processName\" is killed")
    public void whenProcessIsKilled(String processName) {
        applicationSubSteps.stepProcessIsKilled(processName);
    }

    @When("set browser resolution with resolution \"$width\" x \"$height\"")
    public void whenSetBrowserResolutionWithWidthAndHeight(String width, String height){
        int width_int = Integer.parseInt(evalVariable(width));
        int height_int = Integer.parseInt(evalVariable(height));
        getActiveDriver().manage().window().setSize(new Dimension(width_int, height_int));

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("new tab is opened")
    public void givenOpenTab() {
        ((JavascriptExecutor) getActiveDriver()).executeScript("window.open('','_blank');");

        Set<String> handles = getActiveDriver().getWindowHandles();
        String currentWindowHandle = getActiveDriver().getWindowHandle();
        for (String handle : handles) {
            if (!currentWindowHandle.equals(handle)) {
                getActiveDriver().switchTo().window(handle);
            }
        }
    }
}
