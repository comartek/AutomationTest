package core.elements.dropdownlist.subtypes;

import core.htmlElements.element.TypifiedElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

import static core.utils.actions.CustomActions.moveCursorToTheTopLeftCornerInTheOCBApp;
import static core.steps.BaseSteps.getActiveDriver;

/**
 * Created by Akarpenko on 20.11.2017.
 */
public class CustomComboBox extends TypifiedElement {

    /**
     * Specifies wrapped {@link WebElement}.
     *
     * @param wrappedElement {@code WebElement} to wrap.
     */
    public CustomComboBox(WebElement wrappedElement) {
        super(wrappedElement);
    }



    @Override
    public void sendKeys(CharSequence... charSequences) {

        String value = charSequences[0].toString();
        this.click();
        String optionsXpath1 = "./ul/li/span"; //root for SubMenu_Dropdown existing on all frameworkPages
        String optionsXpath2 = "./div/span"; //root for Selector like ReceivedMessages_Dropdown on ReceivedMessagesPage
        List<WebElement> options = this.getWrappedElement().findElements(By.xpath(optionsXpath1 + "|" + optionsXpath2));
        options.stream().filter(x ->
                x.getText().trim().equals(value)).findFirst().get().click();

    }


    @Override
    public void activateElementBorder() {
        JavascriptExecutor js = (JavascriptExecutor) getActiveDriver();
        js.executeScript("arguments[0].style.border = '1px solid red';",this);
        moveCursorToTheTopLeftCornerInTheOCBApp();
    }
    @Override
    public void deactivateElementBorder() {
        JavascriptExecutor js = (JavascriptExecutor) getActiveDriver();
        js.executeScript("arguments[0].style.border = 'none';",this);
        moveCursorToTheTopLeftCornerInTheOCBApp();
    }
}
