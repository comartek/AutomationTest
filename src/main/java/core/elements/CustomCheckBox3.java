package core.elements;

import core.htmlElements.element.*;
import core.utils.assertExt.AssertExt;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by DDmitryS
 *
 * Example of CustomCheckBox3:
 * T24 -> ?FIN.IN.BR1.2 -> Teller transaction>Man hinh tich hop GDV>Partial Withdrawal And Preclosure -> "Pre_closure_Ind_Checkbox" field
 *
 */

public class CustomCheckBox3 extends TypifiedElement {
    public CustomCheckBox3(WebElement CheckBox) {
        super(CheckBox);
    }

    @Override
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
        String checked = this.getAttribute("checked");
        if ("true".equals(checked))
            return "TRUE";
        else
            return "FALSE";
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
