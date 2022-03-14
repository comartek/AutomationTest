package core.elements.dropdownlist;

import core.elements.dropdownlist.subtypes.*;
import core.htmlElements.element.TypifiedElement;
import core.utils.timeout.TimeoutUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;

import java.util.List;

public class DropdownList extends TypifiedElement {

    /**
     * Specifies wrapped {@link WebElement}.
     * @param wrappedElement {@code WebElement} to wrap.
     */

    public DropdownList(WebElement wrappedElement) {
        super(wrappedElement);
    }

    private TypifiedElement getDropdownListSubType(){
        TypifiedElement res = null;
        String tagName = this.getTagName().toLowerCase();
        String atrClass = this.getAttribute("class");
        //String atrStyle = this.getAttribute("style");
        //String atrAriaAutocomplete = this.getAttribute("aria-autocomplete");

        long previousTimeout = TimeoutUtils.getTimeOut();
        TimeoutUtils.setTimeOut(0);
        List<WebElement> innerDropdownHolders = this.getWrappedElement().findElements(By.xpath(".//*[contains(@class, 'DropdownHolder')]"));
        boolean innerDropdownHolderExists = innerDropdownHolders.size() > 0;
        TimeoutUtils.setTimeOut(previousTimeout);

        if (tagName.equals("select")) {
            res = new CustomSelect(this.getWrappedElement());
        } else if (innerDropdownHolderExists) {
            res = new CustomComboBox(this.getWrappedElement());
        } else if (atrClass.contains("sapUiTf")) {
            res = new SapUiComboBox(this.getWrappedElement());
        } else if (atrClass.contains("ui-autocomplete-input combobox")) {
            res = new AutocompleteInputComboBox(this.getWrappedElement());
        } else if (atrClass.contains("ui-autocomplete-input")) {
            res = new AutocompleteInput(this.getWrappedElement());
        } else {
            throw new RuntimeException("Cannot identify subtype of a DropdownList object:\n" + this.getWrappedElement().toString());
        }

        return res;
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        TypifiedElement dropdownList = getDropdownListSubType();
        dropdownList.sendKeys(charSequences);
    }

    public void sendKeysAndCheckNotExist(CharSequence... charSequences){
        TypifiedElement dropdownList = getDropdownListSubType();
        if (dropdownList instanceof AutocompleteInputComboBox) {
            ((AutocompleteInputComboBox) dropdownList).sendKeysAndCheckNotExist(charSequences);
        }else if (dropdownList instanceof AutocompleteInput){
            ((AutocompleteInput) dropdownList).sendKeysAndCheckNotExist(charSequences);
        }
        else throw new NotImplementedException("");
    }

    public void selectByPartialValue(String value){
        TypifiedElement dropdownList = getDropdownListSubType();
        ((DropdownInterface)dropdownList).selectByPartialValue(value);
    }
    public void selectByExactValue(String value){
        TypifiedElement dropdownList = getDropdownListSubType();
        if (dropdownList instanceof AutocompleteInputComboBox)
            ((AutocompleteInputComboBox)dropdownList).selectByExactValue(value);
        else throw new NotImplementedException("");
    }


    @Override
    public String getText() {
        TypifiedElement dropdownList = getDropdownListSubType();
        return dropdownList.getText();
    }


    @Override
    public String toString() {
        TypifiedElement dropdownList = getDropdownListSubType();
        return dropdownList.toString();
    }

    @Override
    public void click() {
        TypifiedElement dropdownList = getDropdownListSubType();
        dropdownList.click();
    }

    @Override
    public boolean isValueExist(String expectedValue) {
        TypifiedElement dropdownList = getDropdownListSubType();
        return dropdownList.isValueExist(expectedValue);
    }

    @Override
    public boolean isExactValueExist(String expectedValue) {
        TypifiedElement dropdownList = getDropdownListSubType();
        return dropdownList.isExactValueExist(expectedValue);
    }

    @Override
    public boolean isValueExistUsingRegex(String expectedPartialValue, String regex) {
        TypifiedElement dropdownList = getDropdownListSubType();
        return dropdownList.isValueExistUsingRegex(expectedPartialValue, regex);
    }

}
