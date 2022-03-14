package core.elements;

import core.htmlElements.element.*;
import core.utils.assertExt.AssertExt;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

/**
 * Created by DDmitryS on 14.12.2017.
 */
public class CustomRadioButton extends TypifiedElement {

    public CustomRadioButton(WebElement Radio) {
        super(Radio);
    }


    public void sendKeys(CharSequence... charSequences) {
        String value = charSequences[0].toString().toLowerCase();
            if (value.equals("true"))
            {
                this.click();
            }
        else
            Assert.fail(String.format("Input value for this field may be equal only TRUE"));
    }

    @Override
    public String getText() {
        String output = new String();
        if (this.isSelected()) {
            output = "TRUE";
        } else {
            output = "FALSE";
        }
        return output;
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
