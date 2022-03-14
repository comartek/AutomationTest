package core.elements.dropdownlist.subtypes;

import core.elements.dropdownlist.DropdownInterface;
import core.htmlElements.element.Select;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Quotes;

import java.util.Iterator;
import java.util.List;

import static core.steps.BaseSteps.getActiveDriver;

public class CustomSelect extends Select implements DropdownInterface {
    /**
     * Specifies wrapped {@link WebElement}.
     * Performs no checks unlike {@link org.openqa.selenium.support.ui.Select}. All checks are made later
     * in {@link #getSelect()} method.
     *
     * @param wrappedElement {@code WebElement} to wrap.
     */
    public CustomSelect(WebElement wrappedElement) {
        super(wrappedElement);
    }


    @Override
    public void sendKeys(CharSequence... charSequences) {
        this.selectByVisibleText(charSequences[0].toString());
        blurActiveElement();
    }

    @Override
    public String getText() {
        String text = this.getFirstSelectedOption().getText();
        blurActiveElement();
        return text;
    }

    @Override
    public void activateElementBorder() {
        JavascriptExecutor js = (JavascriptExecutor) getActiveDriver();
        js.executeScript("arguments[0].style.border = '1px solid red';",this);
        blurActiveElement();
    }

    @Override
    public boolean isValueExist(String expectedValue) {
        return getOptions().stream().anyMatch(x->x.getText().contains(expectedValue));
    }

    @Override
    public boolean isExactValueExist(String expectedValue) {
       return getOptions().stream().anyMatch(x->x.getText().equals(expectedValue));
    }

}
