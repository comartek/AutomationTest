package core.elements.dropdownlist.subtypes;

import core.elements.VirtualButton;
import core.elements.dropdownlist.DropdownInterface;
import core.htmlElements.element.TypifiedElement;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotSelectableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Optional;

import static core.pages.BasePage.activateElementBorderIfNeeded;
import static core.steps.BaseSteps.getActiveDriver;

/**
 * Created by Alex Nazarov on 11.01.2018.
 * <p>
 * Example of AutocompleteInputComboBox:
 * OCB -> ACCOUNTS -> Transaction search -> "Load Saved Search" field
 */
public class AutocompleteInput extends TypifiedElement implements DropdownInterface {

    /**
     * Specifies wrapped {@link WebElement}.
     *
     * @param wrappedElement {@code WebElement} to wrap.
     */
    public AutocompleteInput(WebElement wrappedElement) {
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
        getWrappedElement().click();
        getWrappedElement().sendKeys(value);
        WebDriverWait wait = new WebDriverWait(getActiveDriver(), 10);

        Integer waitCount = 0;
        while (waitCount < 10) {

            try {
                List<WebElement> options = getWrappedElement().findElements
                        (By.xpath("//ul[contains(@class, 'ui-autocomplete')]/li/a"));

                Optional<WebElement> opt = options.stream().filter(x -> {
                    String text = x.getText().trim();
                    System.out.println(text);
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
    public void selectByPartialValue(String value) {
        getWrappedElement().clear();
        getWrappedElement().click();
        getWrappedElement().sendKeys(value);
        WebDriverWait wait = new WebDriverWait(getActiveDriver(), 10);

        Integer waitCount = 0;
        while (waitCount < 10) {

            try {
                List<WebElement> options = getWrappedElement().findElements
                        (By.xpath("./following-sibling::ul[contains(@class, 'ui-autocomplete')]/li/a"));

                Optional<WebElement> opt = options.stream().filter(x -> {
                    String text = x.getText().trim();
                    System.out.println(text);
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

        //reactivate element border for a screenshot (because sometimes initial border disappears after clicking element in the dropdown menu)
        activateElementBorderIfNeeded(this);
        blurActiveElement();
    }
    public void sendKeysAndCheckNotExist(CharSequence... charSequences) {
        String value = charSequences[0].toString();
        getWrappedElement().clear();
        getWrappedElement().click();
        getWrappedElement().sendKeys(value);
        WebDriverWait wait = new WebDriverWait(getActiveDriver(), 10);

        Integer waitCount = 0;
        while (waitCount < 3) {

            try {
                List<WebElement> options = getWrappedElement().findElements
                        (By.xpath("//ul[contains(@class, 'ui-autocomplete')]/li/a"));

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
