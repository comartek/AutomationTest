package core.elements;

import core.htmlElements.element.Button;
import core.htmlElements.element.TypifiedElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;

/**
 * Created by Akarpenko on 02.11.2017.
 */
public class ClickByEnterButton extends Button {

    public ClickByEnterButton(WebElement wrappedElement){
        super(wrappedElement);
    }

    @Override
    public void click() {
       getWrappedElement().sendKeys(Keys.ENTER);
    }
}
