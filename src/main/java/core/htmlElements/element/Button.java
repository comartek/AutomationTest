package core.htmlElements.element;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static core.steps.BaseSteps.getActiveDriver;

/**
 * Represents web page button control.
 *
 * @author Alexander Tolmachev starlight@yandex-team.ru
 *         Date: 14.08.12
 */
public class Button extends TypifiedElement {
    /**
     * Specifies wrapped {@link WebElement}.
     *
     * @param wrappedElement {@code WebElement} to wrap.
     */
    public Button(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public void click() {
//        if (getActiveDriver() instanceof InternetExplorerDriver) {
//            new WebDriverWait(((WrapsDriver) getWrappedElement()).getWrappedDriver(), 5000)
////                .ignoring(StaleElementReferenceException.class,ClassCastException.class)
//                    .until(ExpectedConditions.elementToBeClickable(getWrappedElement()));//.sendKeys(Keys.ENTER);
//            Actions act = new Actions(getActiveDriver());
//            act.click(getWrappedElement()).build().perform();
//        } else
            super.click();
    }

}
