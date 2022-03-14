package core.elements;

import core.htmlElements.element.*;
import core.utils.assertExt.AssertExt;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

/**
 * Created by DDmitryS on 14.12.2017.
 */
public class CustomCheckBox extends TypifiedElement {
    public CustomCheckBox(WebElement CheckBox) {
        super(CheckBox);
    }


    public void sendKeys(CharSequence... charSequences) {
        String value = charSequences[0].toString().toLowerCase();
        if (value.equals("false")){
            if (this.getText().equals("TRUE")) {
                this.click();
            }
        } else if (value.equals("true")) {
            if (this.getText().equals("FALSE")){
                this.click();
            }
        } else
            Assert.fail(String.format("Input value for this field should be TRUE or FALSE"));

    }

    @Override
    public String getText() {
        String backgroundColor = this.getCssValue("background-color");
        if (backgroundColor.equals("rgba(1, 160, 80, 1)") || backgroundColor.equals("rgba(0, 132, 70, 1)")) {
            return "TRUE";
        } else if (backgroundColor.equals("rgba(230, 230, 230, 1)") || backgroundColor.equals("rgba(204, 204, 204, 1)")) {
            return "FALSE";
        } else {
            Assert.fail("The state of the checkbox is undefined. Check rgba conditions of the webelement");
            return ""; //Unreachable
        }
    }

    @Override
    public boolean checkCondition(String operation, String value) {

        ExtendedWebElement field = this;

        if (!(field.exists() || operation.toLowerCase().equals("not exists")))
            return false;
        switch (operation.toLowerCase()) {
            case "equals":
                return field.getText().toLowerCase().equals(value.toLowerCase());
            case "not equals":
                return !field.getText().equals(value);
            case "contains":
                return field.getText().contains(value);
            case "not contains":
                return !field.getText().contains(value);
            case "exists":
                return field.exists();
            case "not exists":
                return !field.exists();
            case "matches":
                return field.getText().matches(value);
            default:
                return false;
        }
    }
}
