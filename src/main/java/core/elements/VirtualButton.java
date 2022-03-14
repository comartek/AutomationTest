package core.elements;

import core.htmlElements.element.TypifiedElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;

import static core.steps.BaseSteps.getActiveDriver;

/**
 * Created by Akarpenko on 02.11.2017.
 */
public class VirtualButton extends TypifiedElement {

    public VirtualButton(WebElement wrappedElement){
        super(wrappedElement);
    }

    @Override
    public void click() {
        ((JavascriptExecutor)((WrapsDriver) getWrappedElement()).getWrappedDriver()).
                executeScript("arguments[0].click();", getWrappedElement());
    }

    @Override
    public boolean existsAndDisplayed() {
        return exists();
    }
}
