package core.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static core.steps.BaseSteps.getActiveMobileDriver;
import static core.utils.timeout.TimeoutUtils.setTimeOut;

public class PageNotVaultCompleted extends AndroidMobileCommonObjectsPage {



    @Override
    public void waitUntilLoadingIndicatorIsInvisible(int maxWaitTimeInSeconds) {
        long previousTimeoutValue = 60;
        setTimeOut(0); // timeout to wait until Loading Indicator APPEARS before we start waiting fot it to become invisible

        WebDriverWait webDriverWait = new WebDriverWait(getActiveMobileDriver(), maxWaitTimeInSeconds);
        webDriverWait.ignoring(StaleElementReferenceException.class).until(webDriver -> {
            try {
                //It is important to keep loadingIndicator XPath inside this method. Otherwise, switchTo annotation will fail to execute
                List<WebElement> checkLoadingIndicator = getActiveMobileDriver().findElements(By.xpath("//*[@content-desc='VaultScreen']/android.view.ViewGroup/android.widget.ImageView"));
                return checkLoadingIndicator.size() == 0;
            } catch (NoSuchElementException | NoSuchWindowException ignored) {
                return true;
            }
        });
        setTimeOut(previousTimeoutValue);
    }


    @Override
    public boolean isLoaded()
    {
        return true;
    }
}
