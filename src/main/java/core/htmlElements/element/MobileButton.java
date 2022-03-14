package core.htmlElements.element;

import io.appium.java_client.MobileElement;
import org.openqa.selenium.WebElement;

/**
 * Represents web page button control.
 *
 * @author Alexander Tolmachev starlight@yandex-team.ru
 *         Date: 14.08.12
 */
public class MobileButton extends TypifiedMobileElement {
    /**
     * Specifies wrapped {@link io.appium.java_client.MobileElement}.
     *
     * @param wrappedElement {@code WebElement} to wrap.
     */
    public MobileButton(MobileElement wrappedElement) {
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
