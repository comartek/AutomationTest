package core.elements.dropdownlist.subtypes;

import core.elements.dropdownlist.DropdownInterface;
import core.htmlElements.element.Button;
import core.htmlElements.element.TypifiedElement;
import core.utils.timeout.TimeoutUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.BreakIterator;
import java.util.List;
import java.util.Optional;

import static core.pages.BasePage.activateElementBorderIfNeeded;
import static core.steps.BaseSteps.getActiveDriver;
import static core.steps.BaseSteps.getCurrentPage;
import static core.utils.timeout.TimeoutUtils.getTimeOut;
import static core.utils.timeout.TimeoutUtils.setTimeOut;

/**
 * Created by Alex Zagriychuk on 12.01.2018.
 *
 * Example of SapUiComboBox:
 * OCB -> SAVING -> Open Online Saving -> "Saving account type" field
 */
public class SapUiComboBox extends TypifiedElement implements DropdownInterface {

    /**
     * Specifies wrapped {@link WebElement}.
     *
     * @param wrappedElement {@code WebElement} to wrap.
     */
    public SapUiComboBox(WebElement wrappedElement) {
        super(wrappedElement);
    }



    @Override
    public void sendKeys(CharSequence... charSequences) {

        WebDriverWait wait = new WebDriverWait(getActiveDriver(),10);

        String value = charSequences[0].toString();


        Button comboBoxBtn = new Button(this.findElement(
                By.xpath("./preceding-sibling::div[@class='sapUiTfComboIcon']")));
        comboBoxBtn.click();

        Integer waitCount = 0;
        while (waitCount<10) {

            try {
                List<WebElement> options = getActiveDriver().findElements
                        (By.xpath("//div[contains(@class,'sapUiLbx') and contains(@style, 'display: block')]/ul/li/span"));

                Optional<WebElement> opt = options.stream().filter(x -> {
                    String text = x.getText().trim();
                    System.out.println(text);
                    return text.equals(value);
                }).findFirst();
                if (opt.isPresent()) {
                    opt.get().click();
                    break;
                }
                System.err.println("not found");
            }
            catch (StaleElementReferenceException e)
            {
                System.err.println("stale ref");
            }

            try {
                Thread.sleep(1000);
                waitCount++;
            }
            catch (Exception e){

            }

        }

        if (waitCount == 10){
            throw new RuntimeException();
        }


        getCurrentPage().waitUntilSapBusyIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        //ToDo check that sap bysy indicator is invisible (XPath when
        // visible = "//div[@class='sapUiLocalBusyIndicator'" and when not visible this element does not exist]

        //reactivate element border for a screenshot
        // (because sometimes initial border disappears after clicking element in the dropdown menu)
        activateElementBorderIfNeeded(this);
        blurActiveElement();
    }

    @Override
    public String getText() {
        return getWrappedElement().getAttribute("value");
    }

    public void selectByPartialValue(String value){

        WebDriverWait wait = new WebDriverWait(getActiveDriver(),10);

        Button comboBoxBtn = new Button(this.findElement(
                By.xpath("./preceding-sibling::div[@class='sapUiTfComboIcon']")));
        comboBoxBtn.click();

        Integer waitCount = 0;
        while (waitCount<10) {

            try {
                List<WebElement> options = getActiveDriver().findElements
                        (By.xpath("//div[contains(@class,'sapUiLbx') and contains(@style, 'display: block')]/ul/li/span"));

                Optional<WebElement> opt = options.stream().filter(x -> {
                    String text = x.getText().trim();
                    if (text.isEmpty())
                        text = x.getAttribute("title").trim();
                    System.out.println(text);
                    return text.contains(value);
                }).findFirst();
                if (opt.isPresent()) {
                    opt.get().click();
                    break;
                }
                System.err.println("not found");
            }
            catch (StaleElementReferenceException e)
            {
                System.err.println("stale ref");
            }

            try {
                Thread.sleep(1000);
                waitCount++;
            }
            catch (Exception e){

            }

        }

        if (waitCount == 10){
            throw new RuntimeException();
        }


        getCurrentPage().waitUntilSapBusyIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        //ToDo check that sap bysy indicator is invisible (XPath when
        // visible = "//div[@class='sapUiLocalBusyIndicator'" and when not visible this element does not exist]

        //reactivate element border for a screenshot
        // (because sometimes initial border disappears after clicking element in the dropdown menu)
        activateElementBorderIfNeeded(this);
        blurActiveElement();
    }

}
