package core.htmlElements.element;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static core.steps.BaseSteps.getActiveDriver;

public abstract class MobileExtendedWebElement implements WrapsElement, Named, WebElement {

    public abstract boolean exists();

    public boolean existsAndDisplayed() {
        int retryCount = 3; // retry in case of StaleElementException
        for (int i = 0; i < retryCount; i++) {
            try {
                if (getWrappedElement().isDisplayed()) {
                    return true;
                } else {
                /*
                    Workaround to solve a problem (so far only OCB):
                    .isDisplayed() returns FALSE if text span is actually DISPLAYED but has an empty value ("")

                    If we add a '1px solid red' to a 'style.border' attribute then if this field is actually displayed...
                    ... next call of .isDisplayed() method will return TRUE (if the filed is not displayed then as expected .isDisplayed() method will return FALSE again)
                    In the end we set an initial style value back to a style attribute
                 */
//                    String styleBefore = getWrappedElement().getAttribute("style");
//                    JavascriptExecutor js = (JavascriptExecutor) getActiveDriver();
//                    js.executeScript("arguments[0].style.border = '1px solid red';", this);
                    boolean result = getWrappedElement().isDisplayed();
//                    js.executeScript("arguments[0].style = '" + styleBefore + "';", this);
                    return result;
                }
            } catch (NoSuchElementException ignored) {
                return false;
            } catch (StaleElementReferenceException ignored) {
                continue;
            }
        }
        return false;
    }


    public boolean checkCondition(String operation, String value) {

        MobileExtendedWebElement field = this;

        if (!(operation.toLowerCase().equals("not exists") || field.exists()))
            return false;
        switch (operation.toLowerCase()) {
            case "equals":
                return field.getText().trim().equals(value);
            case "not equals":
                return !field.getText().trim().equals(value);
            case "contains":
                return field.getText().trim().contains(value);
            case "not contains":
                return !field.getText().trim().contains(value);
            case "exists":
                return field.exists();
            case "not exists":
                return !field.exists();
            case "matches":
                return field.getText().trim().matches(value);
            case "text content equals":
                return field.getAttribute("textContent").equals(value);
            default:
                return false;
        }
    }


    public boolean isValueExist(String expectedValue) {
        return false;
    }

    public boolean isExactValueExist(String expectedValue) {
        return false;
    }

    public boolean isValueExistUsingRegex(String expectedPartialValue, String regex) {
        return false;
    }

    public boolean checkAttributeValue(String attribute, String expectedValue) {
        String fieldName = this.getName().replace(" ", "");
        String actualValue = this.getAttribute(attribute);
        if (actualValue == null) {
            Assert.fail(String.format("Attribute '%s' has not been found for a field with name '%s'", attribute, fieldName));
        } else if (actualValue.equals(expectedValue)) {
            return true;
        } else {
            Assert.fail(String.format("Attribute`s '%s' actual value '%s' and expected value '%s' are not equal (field name = '%s')", attribute, actualValue, expectedValue, fieldName));
        }

        //unreachable code
        return false;
    }
}

