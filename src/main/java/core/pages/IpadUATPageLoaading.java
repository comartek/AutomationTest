package core.pages;

import static core.steps.BaseSteps.getActiveMobileDriver;
import static core.utils.timeout.TimeoutUtils.setTimeOut;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class IpadUATPageLoaading extends IpadCommonObjectsPage {
    @Override
    public void waitUntilLoadingIndicatorIsInvisible(int maxWaitTimeInSeconds) {
        long previousTimeoutValue = 180;
        setTimeOut(0); // timeout to wait until Loading Indicator APPEARS before we start waiting fot it to become invisible

        WebDriverWait webDriverWait = new WebDriverWait(getActiveMobileDriver(), maxWaitTimeInSeconds);
        webDriverWait.ignoring(StaleElementReferenceException.class).until(webDriver -> {
            try {
                //It is important to keep loadingIndicator XPath inside this method. Otherwise, switchTo annotation will fail to execute
                List<WebElement> checkLoadingIndicator = getActiveMobileDriver().findElements(By.xpath("//XCUIElementTypeStaticText[contains(@name,'Loading...')]"));
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
