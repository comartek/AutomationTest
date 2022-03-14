package core.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static core.steps.BaseSteps.getActiveMobileDriver;
import static core.utils.timeout.TimeoutUtils.setTimeOut;

/**
 * Created by BaoPN on 12.02.2020.
 */

public abstract class RosyBasePage extends BaseMobilePage {

    private Logger logger = Logger.getLogger("DEBUG_INFO");

    @Override
    public void waitUntilLoadingIndicatorIsInvisible(int maxWaitTimeInSeconds) {
        long previousTimeoutValue = 60;
    setTimeOut(0); // timeout to wait until Loading Indicator APPEARS before we start waiting fot it to become invisible

    WebDriverWait webDriverWait = new WebDriverWait(getActiveMobileDriver(), maxWaitTimeInSeconds);
        webDriverWait.ignoring(StaleElementReferenceException.class).until(webDriver -> {
        try {
            List<WebElement> checkLoadingIndicator = new ArrayList<>();
            //It is important to keep loadingIndicator XPath inside this method. Otherwise, switchTo annotation will fail to execute
            if(System.getProperty("browser").toLowerCase().equals("android")){
                checkLoadingIndicator = getActiveMobileDriver().findElements(By.xpath("//android.widget.ProgressBar"));
            }else if(System.getProperty("browser").toLowerCase().equals("ios")){
                checkLoadingIndicator = getActiveMobileDriver().findElements(By.xpath("//*[contains(@name,'Processing')]"));
//                checkLoadingIndicator = getActiveMobileDriver().findElements(By.xpath("//XCUIElementTypeActivityIndicator[@name='In progress']"));
            }
            logger.debug("Wait for invisibility of loading indicator");
            return checkLoadingIndicator.size() == 0;
        } catch (NoSuchElementException | NoSuchWindowException ignored) {
            return true;
        }
    });
    setTimeOut(previousTimeoutValue);
}

    @Override
    public void waitUntilSapBusyIndicatorIsInvisible(int maxWaitTimeInSeconds) {
//        WebDriverWait webDriverWait = new WebDriverWait(getActiveDriver(), maxWaitTimeInSeconds);
//        ExtendedWebElement elem = (ExtendedWebElement) this.getField("sapBusyIndicator");
//
//        long previousTimeoutValue = getTimeOut();
//        setTimeOut(1); // timeout to wait until SAP busy Indicator APPEARS before we start waiting fot it to become invisible
//        webDriverWait.ignoring(StaleElementReferenceException.class).until(driver -> {
//            logger.debug("Wait for invisibility of SAP busy indicator");
//            return !elem.exists() || !elem.isDisplayed();
//        });
//        setTimeOut(previousTimeoutValue);
    }

    @Override
    public void afterPageLoad(){
        super.afterPageLoad();
        //remove chat window
        try {
//            ((JavascriptExecutor)getActiveDriver()).executeScript("$('#livechat-full').remove()");
        }catch (Exception ignored){}
    }

}
