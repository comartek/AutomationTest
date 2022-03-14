package core.elements.dropdownlist.subtypes;

import core.elements.VirtualButton;
import core.htmlElements.element.Button;
import core.htmlElements.element.ExtendedWebElement;
import core.htmlElements.element.TypifiedElement;
import core.utils.timeout.TimeoutUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotSelectableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static core.pages.BasePage.activateElementBorderIfNeeded;
import static core.steps.BaseSteps.getActiveDriver;
import static core.steps.BaseSteps.getCurrentPage;

/**
 * Created by Alex Zagriychuk on 11.01.2018.
 * <p>
 * Example of AutocompleteInputComboBox:
 * OCB -> BILL PAYMENT -> Add a Payee -> "Choose Module" field
 */
public class AutocompleteInputComboBox extends TypifiedElement {

    /**
     * Specifies wrapped {@link WebElement}.
     *
     * @param wrappedElement {@code WebElement} to wrap.
     */
    public AutocompleteInputComboBox(WebElement wrappedElement) {
        super(wrappedElement);
    }

//    @FindAll({
//            @FindBy(xpath = "//ul[contains(@class, 'ui-autocomplete') and contains(@style, 'display: block')]/li/a")
//    })
//    public List<Button> options;


    @Override
    public void sendKeys(CharSequence... charSequences) {
        String value = charSequences[0].toString();
        getWrappedElement().clear();
        getWrappedElement().sendKeys(value);

        WebDriverWait wait = new WebDriverWait(getActiveDriver(), 10);

        Integer waitCount = 0;
        while (waitCount < 10) {

            try {
                List<WebElement> options = getActiveDriver().findElements
                        (By.xpath("//ul[contains(@class, 'ui-autocomplete') and contains(@style, 'display: block')]/li/a"));

                Optional<WebElement> opt = options.stream().filter(x -> {
                    String text = x.getText().trim();
                    return text.contains(value);
                }).findFirst();
                if (opt.isPresent()) {
                    //wait.until(ExpectedConditions.elementToBeClickable(opt.get())).click();
                    new VirtualButton(opt.get()).click();
                    //opt.get().click();
                    break;
                }
                System.err.println("not found");
            } catch (StaleElementReferenceException e) {
                System.err.println("stale ref");
            }

            try {
                Thread.sleep(1000);
                waitCount++;
            } catch (Exception e) {

            }

        }

        if (waitCount == 10) {
            throw new ElementNotSelectableException("");
        }


//        List<WebElement> options = getActiveDriver().findElements(By.xpath("//ul[contains(@class, 'ui-autocomplete') and contains(@style, 'display: block')]/li/a"));
//        wait.until(ExpectedConditions.elementToBeClickable(options.stream().filter(x -> x.getText().trim().contains(value)).findFirst().get())).click();

//        options.stream().filter(x -> x.getText().trim().contains(value)).findFirst().get().click();

        //reactivate element border for a screenshot (because sometimes initial border disappears after clicking element in the dropdown menu)
        activateElementBorderIfNeeded(this);
        blurActiveElement();
    }

    public void selectByExactValue(CharSequence... charSequences) {
        String value = charSequences[0].toString();
        getWrappedElement().clear();
        getWrappedElement().sendKeys(value);

        WebDriverWait wait = new WebDriverWait(getActiveDriver(), 10);

        Integer waitCount = 0;
        while (waitCount < 10) {

            try {
                List<WebElement> options = getActiveDriver().findElements
                        (By.xpath("//ul[contains(@class, 'ui-autocomplete') and contains(@style, 'display: block')]/li/a"));

                Optional<WebElement> opt = options.stream().filter(x -> {
                    String text = x.getText().trim();
                    return text.equals(value);
                }).findFirst();
                if (opt.isPresent()) {
                    //wait.until(ExpectedConditions.elementToBeClickable(opt.get())).click();
                    new VirtualButton(opt.get()).click();
                    //opt.get().click();
                    break;
                }
                System.err.println("not found");
            } catch (StaleElementReferenceException e) {
                System.err.println("stale ref");
            }
            try {
                Thread.sleep(1000);
                waitCount++;
            } catch (Exception e) {
            }
        }

        if (waitCount == 10) {
            throw new ElementNotSelectableException("");
        }


//        List<WebElement> options = getActiveDriver().findElements(By.xpath("//ul[contains(@class, 'ui-autocomplete') and contains(@style, 'display: block')]/li/a"));
//        wait.until(ExpectedConditions.elementToBeClickable(options.stream().filter(x -> x.getText().trim().contains(value)).findFirst().get())).click();

//        options.stream().filter(x -> x.getText().trim().contains(value)).findFirst().get().click();

        //reactivate element border for a screenshot (because sometimes initial border disappears after clicking element in the dropdown menu)
        activateElementBorderIfNeeded(this);
        blurActiveElement();
    }


    @Override
    public String getText() {
        return getWrappedElement().getAttribute("value");
    }


    @Override
    public boolean isValueExist(String expectedValue) {
        boolean isExist = false;
//        Button button = new Button(this.getWrappedElement().findElement(By.xpath("./following-sibling::button")));
//        button.click();
//
//        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
//        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
//        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);

        getWrappedElement().clear();
        getWrappedElement().sendKeys(expectedValue);

        List<WebElement> options = getActiveDriver().findElements
                (By.xpath("//ul[contains(@class, 'ui-autocomplete') and contains(@style, 'display: block')]/li/a"));
        for (WebElement element : options) {
            if (element.getText().contains(expectedValue)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    @Override
    public boolean isValueExistUsingRegex(String expectedPartialValue, String regex) {
        boolean isExist = false;

        Pattern pattern = Pattern.compile(regex);
        Matcher m;

        getWrappedElement().clear();
        getWrappedElement().sendKeys(expectedPartialValue);

        List<WebElement> options = getActiveDriver().findElements
                (By.xpath("//ul[contains(@class, 'ui-autocomplete') and contains(@style, 'display: block')]/li/a"));
        for (WebElement element : options) {
            m = pattern.matcher(element.getText());
            if (m.matches()) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    @Override
    public boolean isExactValueExist(String expectedValue){
        boolean isExist = false;
        getWrappedElement().clear();
        getWrappedElement().sendKeys(expectedValue);

        List<WebElement> options = getActiveDriver().findElements
                (By.xpath("//ul[contains(@class, 'ui-autocomplete') and contains(@style, 'display: block')]/li/a"));
        for (WebElement element : options) {
            if (element.getText().equals(expectedValue)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    public void sendKeysAndCheckNotExist(CharSequence... charSequences) {
        String value = charSequences[0].toString();
        getWrappedElement().clear();
        getWrappedElement().sendKeys(value);

        WebDriverWait wait = new WebDriverWait(getActiveDriver(), 10);

        Integer waitCount = 0;
        while (waitCount < 3) {

            try {
                List<WebElement> options = getActiveDriver().findElements
                        (By.xpath("//ul[contains(@class, 'ui-autocomplete') and contains(@style, 'display: block')]/li/a"));

                Optional<WebElement> opt = options.stream().filter(x -> {
                    String text = x.getText().trim();
                    return text.contains(value);
                }).findFirst();
                if (!opt.isPresent()) {
                    break;
                }
                System.err.println("found this element");
            } catch (StaleElementReferenceException e) {
                System.err.println("stale ref");
            }

            try {
                Thread.sleep(1000);
                waitCount++;
            } catch (Exception e) {

            }

        }

        if (waitCount == 3) {
            throw new ElementNotSelectableException("");
        }
        blurActiveElement();
    }
}
