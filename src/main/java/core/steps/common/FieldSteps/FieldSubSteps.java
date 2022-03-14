package core.steps.common.FieldSteps;

import core.SwipeAppium;
import core.Zoom;
import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.annotations.StepExt;
import core.elements.T24TreeView;
import core.elements.VirtualButton;
import core.elements.dropdownlist.DropdownInterface;
import core.elements.dropdownlist.DropdownList;
import core.elements.dropdownlist.subtypes.AutocompleteInputComboBox;
import core.enums.KeyEnum;
import core.htmlElements.element.ExtendedWebElement;
import core.htmlElements.element.Select;
import core.steps.BaseSteps;
import core.steps.common.ApplicationSteps.ApplicationSubSteps;
import core.steps.common.PageSteps.PageSubSteps;
import core.steps.common.UtilSteps.UtilSubSteps;
import core.utils.datapool.DataPool;
import core.utils.evaluationManager.ScriptUtils;
import core.utils.report.ReportUtils;
import core.utils.timeout.TimeoutUtils;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.jbehave.core.model.ExamplesTable;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static core.pages.BasePage.activateElementBorderIfNeeded;
import static core.utils.actions.CustomActions.moveCursorToTheTopLeftCornerInTheOCBApp;
import static core.utils.evaluationManager.EvaluationManager.evalVariable;
import static core.utils.timeout.TimeoutUtils.*;

import java.io.FileWriter;
import java.io.IOException;


/**
 * Created by Akarpenko on 10.01.2018.
 */
public class FieldSubSteps extends BaseSteps {

    UtilSubSteps utilSubSteps = new UtilSubSteps();
    static ApplicationSubSteps applicationSubSteps = new ApplicationSubSteps();
    private Logger logger = Logger.getLogger("DEBUG_INFO");
    PageSubSteps pageSubSteps = new PageSubSteps();

    @StepExt("field is add \"{fieldName}\" to cart")
    public void stepFieldIsAddProduct(String fieldName) {
        String text = "//p[contains(text(),'"+fieldName+"')]/parent::div/parent::div/following-sibling::div/child::div[@class='btn-add-to-cart text-base  text-center rounded cursor-pointer']";
        getActiveDriver().findElement(By.xpath(text)).click();
    }

    @StepExt("field \"{fieldName}\" is filled iframe \"{iframe}\" with vaules \"{vaule}\"")
    public void stepFieldIsFilledWithiFrame(String fieldName,String iframes, String vaule) {

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        ExtendedWebElement iframe = getCurrentPage().getFieldSafe(iframes);
        activateElementBorderIfNeeded(iframe);

//        WebElement iframe = getActiveDriver().findElement(By.xpath("//div[@id='payex-card-container']/iframe"));
        //Switch to the frame
        getActiveDriver().switchTo().frame(iframe);

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        try {
            webElement.sendKeys(vaule);
        } catch (NoSuchElementException e) {
            Assert.fail("Set of elements does not contain the element with text = '" + vaule + "'");
        }

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getActiveDriver().switchTo().defaultContent();
    }

    @StepExt("field \"{fieldName}\" is clicked with iframe \"{iframe}\"")
    public void stepFieldIsClickiFrame(String fieldName,String iframes) {

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        ExtendedWebElement iframe = getCurrentPage().getFieldSafe(iframes);
        activateElementBorderIfNeeded(iframe);

        //Switch to the frame
        getActiveDriver().switchTo().frame(iframe);

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        webElement.click();
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getActiveDriver().switchTo().defaultContent();
    }

    @StepExt("field \"{fieldName}\" check count \"{option}\"  and save in \"{variable}\"")
    public void checkcountDay(String fieldName,String option, String variable) {
        ExtendedWebElement fieldNameList = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(fieldNameList);
        String xpath = getCurrentPage().getFieldAnnotations(option);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        try {
//            WebElement element = getActiveDriver().findElement(By.xpath(fieldName));
            try {
                List<WebElement> elementList = fieldNameList.findElements(By.xpath(xpath));
                int value = elementList.size();
                String s = String.valueOf(value);
//                System.out.println("item: " + s);
                utilSubSteps.stepSaveVariable(s, variable);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }



    @StepExt("field \"{fieldName}\" is not visible we reload the page ")
    public void stepFieldIsReloadPage(String fieldName) {
        String xpath = getCurrentPage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        while(getActiveDriver().findElements(By.xpath(xpath)).size() == 0 )
        {
            getActiveDriver().navigate().refresh();
        }
    }


    @StepExt("field \"{fieldName}\" is clicked")
    public void stepFieldIsClicked(String fieldName) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        webElement.click();
        try {
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);

            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        } catch (NoSuchWindowException e) {
        }

        //Todo убрать увод курсора с загрузки страницы
        if (webElement instanceof VirtualButton)
            moveCursorToTheTopLeftCornerInTheOCBApp();
    }

    @StepExt("field is clicked Back button in webdriver")
    public void stepFieldIsClickedBackButtonWebdriver() {
        getActiveDriver().navigate().back();

    }

    @StepExt("field scroll to \"{fieldName}\" category item")
    public void stepFieldscrollFindCategoryItem(String fieldName) {
        String text = "//div[contains(@id,'category-item')]/child::p[contains(text(),\""+fieldName+"\")]";
        getActiveDriver().manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
        // identify element
        WebElement l=getActiveDriver().findElement(By.xpath(text));
        // Actions class with moveToElement()
        Actions a = new Actions(getActiveDriver());
        a.moveToElement(l);
        a.perform();

    }
    @StepExt("field scroll to \"{fieldName}\" web screen")
    public void stepFieldscrollDownWeb(String fieldName) {
        String text = "//p[contains(text(),\""+fieldName+"\")]/parent::div/parent::div/following-sibling::div/child::div[@class=\"btn-add-to-cart text-base  text-center rounded cursor-pointer\"]";
        getActiveDriver().manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
        // identify element
        WebElement l=getActiveDriver().findElement(By.xpath(text));
        // Actions class with moveToElement()
        Actions a = new Actions(getActiveDriver());
        a.moveToElement(l);
        a.perform();
//        a.click();
    }


    @StepExt("field \"{fieldName}\" is double clicked")
    public void stepFieldIsDoubleClicked(String fieldName) {

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        webElement.doubleClick();

        try {
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);

            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        } catch (NoSuchWindowException e) {
        }

        //Todo убрать увод курсора с загрузки страницы
        if (webElement instanceof VirtualButton)
            moveCursorToTheTopLeftCornerInTheOCBApp();
    }

    @StepExt("field \"{fieldName}\" is clicked without wait")
    public void stepFieldIsClickedWithoutWait(String fieldName) {

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        webElement.click();

        try {
//              TimeoutUtils.sleep(1);
        } catch (NoSuchWindowException e) {
        }

        //Todo убрать увод курсора с загрузки страницы
        if (webElement instanceof VirtualButton)
            moveCursorToTheTopLeftCornerInTheOCBApp();
    }

    @StepExt("field \"{fieldName}\" is clicked (by JavaScript)")
    public void stepFieldIsClickedByJS(String fieldName) {

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        ((JavascriptExecutor) getActiveDriver())
            .executeScript("arguments[0].click();", webElement.getWrappedElement());

        try {
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);

            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        } catch (NoSuchWindowException e) {
        }

        //Todo убрать увод курсора с загрузки страницы
        if (webElement instanceof VirtualButton)
            moveCursorToTheTopLeftCornerInTheOCBApp();
    }

    @StepExt("click \"{fieldName}\" and accept an alert (if exists)")
    public void stepClickAndAcceptAlert(String fieldName) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        webElement.click();
        TimeoutUtils.sleep(2);
        Alert alert = ExpectedConditions.alertIsPresent().apply(getActiveDriver());
        if (alert != null) {
            System.err.println("alert text = " + alert.getText());
            alert.accept();
        }

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);

    }

    @StepExt("click \"{fieldName}\" and switch to print window")
    public void stepClickAndSwitchToPrintWindow(String fieldName) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        webElement.click();
        TimeoutUtils.sleep(5);
        String handle = getActiveDriver().getWindowHandle();
        try {
            TimeoutUtils.setTimeOut(5);
            if (getActiveDriver().getWindowHandles().toArray().length
                > getActiveApplication().windows.size()) {
                int newWindowsCount = getActiveDriver().getWindowHandles().toArray().length
                    - getActiveApplication().windows.size();
                for (int i = 0; i < newWindowsCount; i++) {
                    applicationSubSteps.stepSwitchToWindow("PrintDialog" + i);
                    if (getActiveDriver().findElements(By.xpath("//div[@id='sidebar']")).size()
                        > 0) {
                        return;
                    }
                }

            }
        } finally {
            TimeoutUtils.returnDefaultTimeOut();
        }
        Assert.fail("Print window is not found");
    }


    @StepExt("field \"{fieldName}\" (if exists) is clicked")
    public void stepIfExistsFieldIsClicked(String fieldName) {
        long previousTimeout = TimeoutUtils.getTimeOut();
        TimeoutUtils.setTimeOut(2);

        ExtendedWebElement webElement = (ExtendedWebElement) getCurrentPage().getField(fieldName);
        if (webElement.existsAndDisplayed()) {
            activateElementBorderIfNeeded(webElement);
            webElement.click();

            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);

            //Todo убрать увод курсора с загрузки страницы
            moveCursorToTheTopLeftCornerInTheOCBApp();
        } else {
            System.out.println("Annotation: Optional field does not exist");
        }

        TimeoutUtils.setTimeOut(previousTimeout);
    }

    @StepExt("field \"{fieldName}\" is clicked without scrolling into view")
    public void stepFieldIsClickedWithoutSrollingIntoView(String fieldName) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        String borderElementScrollIntoView = System.getProperty("borderElementScrollIntoView");
        System.setProperty("borderElementScrollIntoView", "false");
        activateElementBorderIfNeeded(webElement);
        System.setProperty("borderElementScrollIntoView", borderElementScrollIntoView);
        webElement.click();

        try {
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);

            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        } catch (NoSuchWindowException e) {
        }

        //Todo убрать увод курсора с загрузки страницы
        if (webElement instanceof VirtualButton)
            moveCursorToTheTopLeftCornerInTheOCBApp();
    }

    @StepExt("field is swipe from \"{fieldName}\" to left on category Item")
    public void stepFieldSwipeFromRightToLeftByWeb(String fileName) {
        String text = "//div[contains(@id,'category-item')]/child::p[contains(text(),\""+fileName+"\")]";
        WebElement element = getActiveDriver().findElement(By.xpath(text));
//        Dimension size = getActiveDriver().manage().window().getSize();
//        int anchor = (int) (size.height * 0.5);
        int startPoint = (int) (element.getSize().getWidth() * 0.9);
        int endPoint = (int) (element.getSize().getWidth() * 0.01);
        JavascriptExecutor jse = (JavascriptExecutor)getActiveDriver();
        jse.executeScript("window.scrollBy("+startPoint+","+endPoint+")", "");

    }

    @StepExt("field is swipe from \"{fieldName}\" to right on category Item")
    public void stepFieldSwipeFromLeftToRightByWeb(String fileName) {
        String text = "//div[contains(@id,'category-item')]/child::p[contains(text(),\""+fileName+"\")]";
        WebElement element = getActiveDriver().findElement(By.xpath(text));
//        Dimension size = getActiveDriver().manage().window().getSize();
//        int anchor = (int) (size.height * 0.5);
        int startPoint = (int) (element.getSize().getWidth()* 0.9);
        int endPoint = (int) (element.getSize().getWidth() * 0.01);
        JavascriptExecutor jse = (JavascriptExecutor)getActiveDriver();
        jse.executeScript("window.scrollBy("+endPoint+","+startPoint+")", "");

    }

    @StepExt("field \"{fieldName}\" is without scrolling into view")
    public void stepFieldIsWithoutSrollingIntoView(String fieldName) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        String borderElementScrollIntoView = System.getProperty("borderElementScrollIntoView");
        System.setProperty("borderElementScrollIntoView", "false");
        activateElementBorderIfNeeded(webElement);
        System.setProperty("borderElementScrollIntoView", borderElementScrollIntoView);
        webElement.scrollIntoView();
        try {
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);

            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        } catch (NoSuchWindowException e) {
        }

        //Todo убрать увод курсора с загрузки страницы
        if (webElement instanceof VirtualButton)
            moveCursorToTheTopLeftCornerInTheOCBApp();
    }


    @StepExt("field \"{fieldName}\" is filled with value \"{value}\"")
    public void stepFieldIsFilledWithValue(String fieldName, String value) {

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        try {
            webElement.sendKeys(value);
        } catch (NoSuchElementException e) {
            Assert.fail("Set of elements does not contain the element with text = '" + value + "'");
        }

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);

    }

    @StepExt("field \"{fieldName}\" is filled with value \"{value}\" then enter")
    public void stepFieldIsFilledWithValueThenEnter(String fieldName, String value) {

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        try {
            webElement.sendKeys(value + "\n");
        } catch (NoSuchElementException e) {
            Assert.fail("Set of elements does not contain the element with text = '" + value + "'");
        }

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);

    }

    @StepExt("field \"{fieldName}\" is not contains value \"{value}\"")
    public void stepFieldIsNotContainsValue(String fieldName, String value) {

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        if (webElement instanceof DropdownList)
            ((DropdownList) webElement).sendKeysAndCheckNotExist(value);

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);

    }

    @StepExt("field \"{fieldName}\" (if enabled) is filled with value \"{value}\"")
    public void stepFieldIsFilledWithValueIfEnabled(String fieldName, String value) {

        ExtendedWebElement webElement = (ExtendedWebElement) getCurrentPage().getField(fieldName);
        activateElementBorderIfNeeded(webElement);

        TimeoutUtils.setTimeOut(1);
        if (webElement.existsAndDisplayed() && webElement.isEnabled())
            webElement.sendKeys(value);
        TimeoutUtils.returnDefaultTimeOut();

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);

    }

    @StepExt("field \"{fieldName}\" value \"{operation}\" \"{expectedValue}\"")
    public void stepFieldValueOperation(String fieldName, String operation, String expectedValue) {
        verifyOperationValue(operation);

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);

        String actualValue = webElement.getText().trim();
        if (!webElement.checkCondition(operation, expectedValue)) {
            Assert.fail(String.format(
                "\nActual value (of the field '%s'): \n'%s' \nis not '%s' to the expected value \n'%s'",
                fieldName, actualValue, operation, expectedValue));
        }
    }

    private void verifyOperationValue(String operation) {
        operation = operation.trim().toLowerCase();
        String[] allowedOperations = new String[]{"equals", "not equals", "contains",
            "not contains", "matches", "text content equals"};
        if (!Arrays.asList(allowedOperations).contains(operation)) {
            Assert.fail(
                "Operation value = '" + operation + "' is not allowed. List of allowed operations: "
                    + Arrays.toString(allowedOperations));
        }
    }


    @StepExt("field \"{fieldName}\" value equals  \"{expectedValue}\"")
    public void stepFieldValueEquals(String fieldName, String expectedValue) {

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);

        String actualValue = webElement.getText().trim();
        Assert.assertTrue(
            String.format(
                "\nActual value (of the field '%s'): \n'%s' \nis not equal to the expected value \n'%s'",
                fieldName, actualValue, expectedValue),
            webElement.checkCondition("equals", expectedValue));
    }

    @StepExt("field \"{fieldName}\" value equals  \"{expectedValue}\" without scroll")
    public void stepFieldValueEqualsWithoutScroll(String fieldName, String expectedValue) {

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        //activateElementBorderIfNeeded(webElement);

        String actualValue = webElement.getText().trim();
        Assert.assertTrue(
            String.format(
                "\nActual value (of the field '%s'): \n'%s' \nis not equal to the expected value \n'%s'",
                fieldName, actualValue, expectedValue),
            webElement.checkCondition("equals", expectedValue));
    }

    @StepExt("field \"{fieldName}\" value is not equal to \"{expectedValue}\"")
    public void stepFieldValueIsNotEqual(String fieldName, String expectedValue) {

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);

        String actualValue = webElement.getText().trim();
        Assert.assertTrue(
            String.format(
                "\nActual value (of the field '%s'): \n'%s' \nis equal to the expected value \n'%s'",
                fieldName, actualValue, expectedValue),
            webElement.checkCondition("not equals", expectedValue));
    }

    @StepExt("field \"{fieldName}\" contains value  \"{expectedValue}\"")
    public void stepFieldContainsValue(String fieldName, String expectedValue) {

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);

        String actualValue = webElement.getText().trim();
        Assert.assertTrue(
            String.format(
                "\nActual value (of the field '%s'): \n'%s' \ndoesn not contain expected value \n'%s'",
                fieldName, actualValue, expectedValue),
            webElement.checkCondition("contains", expectedValue));
    }

    @StepExt("field \"{fieldName}\" contains value  \"{expectedValue}\" (max wait time = \\\"{maxWaitTime}\\\" seconds)")
    public void thenFieldContainsValueWithWait(String fieldName, String expectedValue,
        Long maxWaitTime) {

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        WebDriverWait webDriverWait = new WebDriverWait(getActiveDriver(), maxWaitTime);
        try {
            TimeoutUtils.setTimeOut(0);
            webDriverWait
                .until(ExpectedConditions.attributeContains(webElement, "value", expectedValue));
            activateElementBorderIfNeeded(webElement);
        } catch (Throwable t) {
            String actualValue = webElement.getText().trim();
            Assert.assertTrue(
                String.format(
                    "\nActual value (of the field '%s'): \n'%s' \ndoesn not contain expected value \n'%s' after wait for %s seconds",
                    fieldName, actualValue, expectedValue, maxWaitTime),
                webElement.checkCondition("contains", expectedValue));
        } finally {
            TimeoutUtils.returnDefaultTimeOut();
        }

    }

    @StepExt("field \"{fieldName}\" value is saved in variable \"{variable}\"")
    public void stepFieldValueIsSavedInVariable(String fieldName, String variable) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        String value = webElement.getText().trim();
        utilSubSteps.stepSaveVariable(value, variable);
    }

    @StepExt("field \"{fieldName}\" value is saved Textarea in variable \"{variable}\"")
    public void stepFieldValueIsSavedTextAreaInVariable(String fieldName, String variable) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        String value = webElement.getAttribute("value").trim();
        utilSubSteps.stepSaveVariable(value, variable);

    }

    @StepExt("price of \"{fieldName}\" is saved in variable \"{variable}\"")
    public void stepFieldIsPriceProduct(String fieldName,String variable) {
        String text = "(//p[contains(text(),\""+fieldName+"\")]/parent::div/parent::div/following-sibling::div/child::div)[1]";
        WebElement TxtBoxContent = getActiveDriver().findElement(By.xpath(text));
        String value = TxtBoxContent.getAttribute("innerHTML");
        utilSubSteps.stepSaveVariable(value, variable);

    }


    @StepExt("field \"{fieldName}\" value is saved in variable \"{variable}\" blocked")
    public void stepFieldValueIsSavedInVariableBlocked(String fieldName, String variable) {
        ExtendedWebElement webElement = getCurrentPage().getFieldBlocked(fieldName);
        activateElementBorderIfNeeded(webElement);
        String value = webElement.getText().trim();
        utilSubSteps.stepSaveVariable(value, variable);
//        setVariable(variable,value);
    }

    @StepExt("field \"{fieldName}\" value (if exists) is saved in variable \"{variable}\"")
    public void stepFieldValueIfExistsIsSavedInVariable(String fieldName, String variable) {
        ExtendedWebElement webElement = (ExtendedWebElement) (getCurrentPage().getField(fieldName));
        if (webElement.existsAndDisplayed()) {
            activateElementBorderIfNeeded(webElement);
            String value = webElement.getText().trim();
            utilSubSteps.stepSaveVariable(value, variable);
        } else {
            System.out
                .println("Annotation: field does not exist, set the default value in another step");
            String value = "0";
            utilSubSteps.stepSaveVariable(value, variable);
        }
    }

    @StepExt("attribute \"{attributeName}\" of field \"{fieldName}\" value is saved in variable \"{variable}\"")
    public void stepAttributeOfFieldValueIsSavedInVariable(String attributeName, String fieldName,
        String variable) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        String value = webElement.getAttribute(attributeName).trim();
        utilSubSteps.stepSaveVariable(value, variable);
//        setVariable(variable,value);
    }

    @StepExt("attribute \"{attributeName}\" value of field \"{fieldName}\" equals \"{expectedValue}\"")
    public void stepAttributeValueOfFieldEquals(String attributeName, String fieldName,
        String expectedValue) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        String actualValue = webElement.getAttribute(attributeName).trim();
        Assert.assertTrue(
            String.format(
                "\nActual value of attribute '%s' (of the field '%s'): \n'%s' \nis not equal to the expected value of this attribute \n'%s'",
                attributeName, fieldName, actualValue, expectedValue),
            expectedValue.equals(actualValue));
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("field \"{fieldName}\" value is saved to datapool in variable \"{variable}\"")
    public void stepSaveFieldValueToPool(String fieldName, String variable) {

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        String value = webElement.getText().trim();

        Logger.getRootLogger()
            .info(String.format("'%s' saved in datapool variable '%s'", value, variable));
        DataPool.getInstance().getPool().put(variable, value);
        DataPool.getInstance().savePool();
    }

    @StepExt("field \"{fieldName}\" is clicked the first \"{location}\" on Google Map")
    public void stepFieClickFirstLocationGoogleMap(String fieldName, String location) {
      String xpath = getCurrentPage().getFieldAnnotations(fieldName);
      xpath = xpath.replaceAll(".+xpath\\=", "");
      xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        new WebDriverWait(getActiveDriver(), 20).until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).sendKeys(location);
        new WebDriverWait(getActiveDriver(), 20).until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='pac-item-query']/span[@class='pac-matched']"))).click();
    }

    @StepExt("field \"{fieldName}\" exists")
    public void stepFieldExists(String fieldName) {
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        ExtendedWebElement webElement = ((ExtendedWebElement) getCurrentPage().getField(fieldName));
        if (webElement.existsAndDisplayed()) {
            activateElementBorderIfNeeded(webElement);
        } else {
            Assert.fail(String.format("Field '%s' does not exist. %s", fieldName,
                getCurrentPage().getFieldLog(fieldName)));
        }
    }

    public boolean stepFieldExistsOnPage(String fieldName) {
        boolean isExist;
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        ExtendedWebElement webElement = ((ExtendedWebElement) getCurrentPage().getField(fieldName));
        if (webElement.existsAndDisplayed()) {
            isExist = true;
            activateElementBorderIfNeeded(webElement);
        } else {
            isExist = false;
        }
        return isExist;
    }

    @StepExt("field \"{fieldName}\" exists (max wait time = \"{maxWaitTime}\" seconds)")
    public void stepFieldExistsWithWait(String fieldName, Long maxWaitTime) {
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        ExtendedWebElement webElement = ((ExtendedWebElement) getCurrentPage().getField(fieldName));
        WebDriverWait webDriverWait = new WebDriverWait(getActiveDriver(), maxWaitTime);
        try {
            TimeoutUtils.setTimeOut(0);
            webDriverWait
                .until(ExpectedConditions.visibilityOfAllElements(Arrays.asList(webElement)));
            activateElementBorderIfNeeded(webElement);
        } catch (Throwable t) {
            Assert.fail(String
                .format("Field '%s' does not exist (max wait time %s). %s\nCaused by:\n%s",
                    fieldName, maxWaitTime, getCurrentPage().getFieldLog(fieldName),
                    t.getMessage()));
        } finally {
            TimeoutUtils.returnDefaultTimeOut();
        }
    }

    @StepExt("field \"{fieldName}\" does not exist")
    public void stepFieldDoesNotExist(String fieldName) {
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        ExtendedWebElement webElement = (ExtendedWebElement) getCurrentPage().getField(fieldName);
        try {
            TimeoutUtils.setTimeOut(1);

            if (webElement.existsAndDisplayed()) {
                activateElementBorderIfNeeded(
                    webElement); //add Border to highlight an element which wasn`t supposed to be displayed
                Assert.fail(String.format("Field '%s' exists but it wasn`t supposed to. %s",
                    fieldName, getCurrentPage().getFieldLog(fieldName)));
            }
        } finally {
            TimeoutUtils.returnDefaultTimeOut();
        }
    }

    @StepExt("select item from dropdown \"{fieldName}\" by partial value \"{value}\"")
    public void stepSelectFromDropdownByPartialValue(String fieldName, String value) {
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);

        //TODO refactor all dropdowns
        if (webElement instanceof DropdownInterface)
            ((DropdownInterface) webElement).selectByPartialValue(value);
        else if (webElement instanceof DropdownList)
            ((DropdownList) webElement).selectByPartialValue(value);

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
    }

    @StepExt("select item from dropdown \"{fieldName}\" by exact value \"{value}\"")
    public void stepSelectFromDropdownByExactValue(String fieldName, String value) {
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);

        //TODO refactor all dropdowns
        if (webElement instanceof AutocompleteInputComboBox)
            ((AutocompleteInputComboBox) webElement).selectByExactValue(value);
        else if (webElement instanceof DropdownList)
            ((DropdownList) webElement).selectByExactValue(value);
        else
            webElement.sendKeys(value);

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
    }

    @StepExt("first unselected item from dropdown \"{fieldName}\" is saved to variable \"{variable}\"")
    public void stepFirstUnselectedItemFromDropdownIsSaved(String fieldName, String variable) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);

        Select select = (Select) webElement;

        String value = select.getFirstUnselectedOption();

        utilSubSteps.stepSaveVariable(value, variable);
    }

    @StepExt("item \"$item\" in dropdown \"$fieldName\" exist")
    public void stepItemFromDropdownExist(String item, String fieldName) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);

        if (!webElement.isExactValueExist(item)) {
            Assert.fail(String.format("Item '%s' wasn't found in the field '%s'", item, fieldName));
        }
    }

    @StepExt("item with partial value \"$value\" in dropdown \"$fieldName\" exist")
    public void stepItemWithPartialValueFromDropdownExist(String item, String fieldName) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);

        if (!webElement.isValueExist(item)) {
            Assert.fail(String.format("Item '%s' wasn't found in the field '%s'", item, fieldName));
        }
    }

    @StepExt("item \"$item\" in dropdown \"$fieldName\" does not exist")
    public void stepItemFromDropdownDoesNotExist(String item, String fieldName) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        if (webElement.isExactValueExist(item)) {
            Assert.fail(String.format("Item '%s' was found in the field '%s'", item, fieldName));
        }
    }

    @StepExt("item in dropdown \"$fieldName\" with regex value exist")
    public void stepItemFromDropdownWithRegexValueExist(String fieldName,
        ExamplesTable parameters) {

        ReportUtils.attachExampletable(parameters);
        Map<String, String> params_map = convertExampleTableToMap(parameters, "name", "value");
        String partialValue = params_map.get("PartialValue");
        String regex = params_map.get("Regex");

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);

        if (!webElement.isValueExistUsingRegex(partialValue, regex)) {
            Assert.fail(
                String.format("Item '%s' wasn't found in the field '%s'", partialValue, fieldName));
        }
    }

    @StepExt("item in dropdown \"$fieldName\" with regex value does not exist")
    public void stepItemFromDropdownWithRegexValueDoesNotExist(String fieldName,
        ExamplesTable parameters) {

        ReportUtils.attachExampletable(parameters);
        Map<String, String> params_map = convertExampleTableToMap(parameters, "name", "value");
        String partialValue = params_map.get("PartialValue");
        String regex = params_map.get("Regex");

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);

        if (webElement.isValueExistUsingRegex(partialValue, regex)) {
            Assert.fail(
                String.format("Item '%s' was found in the field '%s'", partialValue, fieldName));
        }
    }

    @StepExt("move cursor to field \"{fieldName}\"")
    public void stepMoveCursorToField(String fieldName) {
        Actions action = new Actions(getActiveDriver());
        ExtendedWebElement extendedWebElement = (ExtendedWebElement) getCurrentPage()
            .getField(fieldName);
        activateElementBorderIfNeeded(extendedWebElement);
        action.moveToElement(extendedWebElement.getWrappedElement()).build().perform();
        sleep(2);
    }

    @StepExt("click item \"{itemPath}\" in treeview field \"{fieldName}\"")
    public void stepClickItemInTreeviewField(String itemPath, String fieldName) {
        T24TreeView webElement = (T24TreeView) (getCurrentPage().getFieldSafe(fieldName));
        activateElementBorderIfNeeded(webElement);
        webElement.clickItem(itemPath);
    }


    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("field \"{fieldName}\" is saved as nearest business day date in variable \"{variable}\"")
    public void stepSaveDateFromFieldAsBusinessDayInVariable(String fieldName, String variable) {
        try {
            ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
            activateElementBorderIfNeeded(webElement);
            String initialValue = webElement.getText().trim();

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = dateFormat.parse(initialValue);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int day = calendar.get(Calendar.DAY_OF_WEEK);
            String value = null;

            if (day >= Calendar.MONDAY && day <= Calendar.FRIDAY) {
                value = initialValue;
            } else if (day == Calendar.SATURDAY) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                value = dateFormat.format(calendar.getTime());
            } else { //Calendar.SUNDAY
                calendar.add(Calendar.DAY_OF_YEAR, -2);
                value = dateFormat.format(calendar.getTime());
            }

            utilSubSteps.stepSaveVariable(value, variable);

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    @StepExt("the key \"$keyName\" is pressed")
    public void stepPressKey(KeyEnum keyName) {
        try {

            Robot robot = new Robot();
            robot.setAutoDelay(250);
            robot.keyPress(keyName.getValue());
            robot.keyRelease(keyName.getValue());
        } catch (Exception e) {
        }
    }

    @StepExt("press ENTER on the field \"fieldName\"")
    public void stepPressKeyVar2(String fieldName) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        webElement.sendKeys(Keys.RETURN);
    }

    @StepExt("press key \"key\" on the field \"fieldName\"")
    public void stepPressKeyOnTheField(String key, String fieldName) {
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);

        Keys keys = Keys.valueOf(key);
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        webElement.getWrappedElement().sendKeys(keys);

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
    }

    @StepExt("field \"{fieldName}\" value is saved in variable \"{variable}\" if empty \"{defaultValue}\"")
    public void stepFieldValueIsSavedInVariableIfEmptyDefault(String fieldName, String variable,
        String defaultValue) {
        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        String value = webElement.getText().trim();
        if (value.equals("")) {
            value = defaultValue;
        }
        utilSubSteps.stepSaveVariable(value, variable);
    }

    @StepExt("delete all files with name matching regex \"$fileNameRegex\" in default directory")
    public void stepDeleteAllFilesWithNameMatchingRegexInDefaultDownloadsDirectory(
        String fileNameRegex) {
        String location = System.getProperty("user.home") + "\\Downloads";

        File directory = new File(location);
        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(fileNameRegex);
            }
        });
        for (File file : files) {
            if (!file.delete()) {
                System.err.println("Can't remove file: " + file.getAbsolutePath());
            } else
                System.out.println("The file has been deleted: " + file.getAbsolutePath());
        }
    }

    public String findExcelFileUsingRegexInDefaultDownloadDirectoryAndParseIt(
        String fileNameRegex) {
        String location = System.getProperty("user.home") + "\\Downloads";

        File directory = new File(location);
        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(fileNameRegex);
            }
        });
        if (files.length > 1)
            System.err.println("More than 1 file found");
        //  String excelFileText = ExcelParser.parser(files[0].getPath());
        return "Вы не готовы!";
    }

    @StepExt("field \"fieldName\" is submitted")
    public void stepFieldIsSubmitted(String fieldName) {
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);

        ((JavascriptExecutor) getActiveDriver())
            .executeScript("arguments[0].submit();", webElement.getWrappedElement());

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
    }

    @StepExt("remove readonly attribute for field \"$fieldName\"")
    public void stepRemoveReadOnlyAttributeForField(String fieldName) {
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);

        ExtendedWebElement webElement = getCurrentPage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(webElement);
        ((JavascriptExecutor) getActiveDriver())
            .executeScript("arguments[0].removeAttribute('readonly','readonly')",
                webElement.getWrappedElement());

        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
    }

    @StepExt("wait max \"{value}\" seconds for invisibility of \"{fieldName}\"")
    public void stepWaitForFieldIsInvisibility(int maxWaitTimeInSeconds, String fieldName) {
        try {
            WebDriverWait webDriverWait = new WebDriverWait(getActiveDriver(),
                maxWaitTimeInSeconds);
            webDriverWait
                .until(ExpectedConditions.invisibilityOf(getCurrentPage().getFieldSafe(fieldName)));
        } catch (AssertionError assertEx) {

        } catch (Exception ex) {

        }
    }

//    ---------------------------------- Scroll ---------------------------------------

    @StepExt("mobile field is scroll up when loading screen iOS")
    public void mobileScrollUpScreenIOS() {
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("direction", "up");
        getActiveMobileDriver().executeScript("mobile:scroll", scrollObject);

    }

    @StepExt("mobile field \"{fieldName}\" is scroll up iOS")
    public void mobileScrollUpElementIOS(String fieldName) {
        RemoteWebElement element = (RemoteWebElement) getActiveMobileDriver()
            .findElement(By.className("XCUIElementTypeOther"));
        String scrollElement = element.getId();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("element", scrollElement);
        scrollObject.put("direction", "down");
        scrollObject.put("xpath", fieldName);
        getActiveMobileDriver().executeScript("mobile:scroll", scrollObject);

    }


    @StepExt("mobile field \"{direction}\" is scroll down to \"{fieldName}\"")
    public void scrollToMobileElement(String fieldName, String direction) {

        final int maximumScrolls = 5;

        for (int i = 0; i < maximumScrolls; i++) {
            try {
                if (findElementsByPredicateString("label CONTAINS \"" + fieldName + "\"").size()
                    > 0)
                    // PredicateString & label is the locator strategy that I used. It can be changed to others as needed for your app.
                    break;
            } catch (Exception e) {
                e.printStackTrace();
            }
            scroll(direction);
        }
    }

    private void scroll(String direction) {
        final HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("direction", direction);
        getActiveMobileDriver().executeScript("mobile:scroll", scrollObject);
    }

    public List<MobileElement> findElementsByPredicateString(String predicateString) {
        return getActiveMobileDriver().findElements(MobileBy.iOSNsPredicateString(predicateString));
    }

    @StepExt("\"$env\" is opened Android")
    public void chooseEnvAndroid(String application) {

        String application_ev = evalVariable(application);
        String envi = ScriptUtils.env("env");
        String checkenvi = "//*[@text='" + envi + "']";

        try {
            getActiveMobileDriver().getPageSource();
            List<WebElement> elementListEnvi = new ArrayList<>();
            try {

                setMobileDriverTimeoutToSecond(10);
                elementListEnvi = getActiveMobileDriver().findElements(By.xpath(checkenvi));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementListEnvi.size() == 0)) {
            } else {
                elementListEnvi.get(0).click();
            }
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }


    @StepExt("\"$env\" is opened iOS")
    public void chooseEnviOS(String application) {

        String application_ev = evalVariable(application);
        String envi = ScriptUtils.env("env");
        String checkenvi = "//*[@name='" + envi + "']";

        try {
            getActiveMobileDriver().getPageSource();
            List<WebElement> elementListEnvi = new ArrayList<>();
            try {

                setMobileDriverTimeoutToSecond(10);
                elementListEnvi = getActiveMobileDriver().findElements(By.xpath(checkenvi));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementListEnvi.size() == 0)) {
            } else {
                elementListEnvi.get(0).click();
            }
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }


    @StepExt("mobile field \"$fieldName\" is clear an element's value")
    public void mobileScrollClearValue(String fieldName) {
        RemoteWebElement element = (RemoteWebElement) getActiveMobileDriver()
            .findElement(By.xpath(fieldName));
        element.clear();

    }

    @StepExt("mobile field \"{fieldName}\" is swipe left iOS")
    public void mobileScrollLeftElementIOS(String fieldName) {
        RemoteWebElement element = (RemoteWebElement) getActiveMobileDriver()
            .findElement(By.className("XCUIElementTypeOther"));
        String scrollElement = element.getId();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("element", scrollElement);
        scrollObject.put("direction", "left");
        scrollObject.put("xpath", fieldName);
        getActiveMobileDriver().executeScript("mobile:swipe", scrollObject);

    }

    @StepExt("mobile field is clicked Back button of devices Android")
    public void stepMobileFieldIsClickedBackOfDevice() {
        ((AndroidDriver<?>) getActiveMobileDriver()).pressKey(new KeyEvent(AndroidKey.BACK));
    }

    public void pressDeleteKey() {
        HashMap swipeObject = new HashMap();
        swipeObject.put("keycode", 67);
        ((JavascriptExecutor) getActiveMobileDriver())
            .executeScript("mobile: keyevent", swipeObject);
    }

    @StepExt("mobile field \"{fieldName}\" is CLEAR field")
    public void stepMobileFieldIsClickedClearOfDevice(String fieldName) {
//        ((AndroidDriver<?>) getActiveMobileDriver()).pressKey(new KeyEvent(AndroidKey.DEL));
        String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        setMobileDriverTimeoutToSecond(60);
        WebElement myEl = getActiveMobileDriver().findElement(By.xpath(xpath));
        myEl.clear();
    }


     @StepExt("mobile field \"{fieldName}\" is clicked")
    public void stepMobileFieldIsClicked(String fieldName) {
        try {
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            setMobileDriverTimeoutToSecond(10);
            getActiveMobileDriver().findElement(By.xpath(xpath)).click();



        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field open Notifications")
    public void stepMobileFieldIsOpenNotifications() {
        try {
            Map<String, Object> response = (Map<String, Object>)getActiveMobileDriver().executeScript("mobile:getNotifications");
            List<Map<String, Object>> notifications = (List<Map<String, Object>>)response .get("statusBarNotifications");
            for (Map<String, Object> notification : notifications) {
                Map<String, String> innerNotification = (Map<String, String>)notification.get("notification");
                if (innerNotification.get("title") != null) {
                    System.out.println(innerNotification.get("title"));
                }
            }

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }


    @StepExt("mobile field is clicked \"{item}\" of \"{product}\" product")
    public void stepMobileFieldIsClickedToElement(String fieldName, String product) {
        try {
            setMobileDriverTimeoutToSecond(30);
            getActiveMobileDriver().findElement(
//                By.xpath("//XCUIElementTypeStaticText[@name='"+ product +"']/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther/following-sibling::XCUIElementTypeOther/child::XCUIElementTypeOther/child::XCUIElementTypeOther/child::XCUIElementTypeOther/child::XCUIElementTypeOther/child::XCUIElementTypeOther[contains(@name,'"+ fieldName +"')]")
                By.xpath("//XCUIElementTypeStaticText[@name='"+ product +"']/parent::XCUIElementTypeOther/following-sibling::XCUIElementTypeOther/child::XCUIElementTypeOther/child::XCUIElementTypeScrollView/child::XCUIElementTypeOther/child::XCUIElementTypeOther/child::XCUIElementTypeOther/child::XCUIElementTypeOther/child::XCUIElementTypeOther/child::XCUIElementTypeOther/child::XCUIElementTypeOther[contains(@name,'"+ fieldName +"')]")
            ).click();

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }


    @StepExt("mobile field \"{fieldName}\" is double clicked")
    public void stepMobileFieldIsDoubleClicked(String fieldName) {
        try {
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            setMobileDriverTimeoutToSecond(30);
//           System.out.println("Xpath: " + xpath);
//            exampleOfFluentWithPredicate(xpath);
//            getActiveMobileDriver().findElement(By.xpath(xpath)).();
            Actions action = new Actions(getActiveMobileDriver());
            action.moveToElement(getActiveMobileDriver().findElement(By.xpath(xpath)));
            action.doubleClick();
            action.perform();




        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field \"{fieldName}\" is clicked if displayed")
    public void stepMobileFieldIsClickedIfDisplayed(String fieldName) {
        String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        if (getActiveMobileDriver().findElements(By.xpath(xpath)).size() != 0) {
            getActiveMobileDriver().findElement(By.xpath(xpath)).click();
        }
    }


    @StepExt("mobile field \"{fieldName}\" is displayed without scroll")
    public void stepMobileFieldIsDisplayedWithoutScroll(String fieldName) {
        setMobileDriverTimeoutToSecond(1);
        WebElement webElement = (WebElement) getCurrentMobilePage().getField(fieldName);
        webElement.click();
        setMobileDriverTimeoutToSecond(60);
    }


    @StepExt("mobile field \"{fieldName}\" is filled with value \"{value}\"")
    public void stepMobileFieldIsFilledWithValue(String fieldName, String value) {
        String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        getActiveMobileDriver().findElement(By.xpath(xpath)).sendKeys(value);
    }

    @StepExt("mobile field \"{fieldName}\" have length \"{variable}\"")
    public void stepMobileFieldHaveLength(String fieldName, String variable) {
        getActiveMobileDriver().getPageSource();
        String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        WebElement txtValue = getActiveMobileDriver().findElement(By.xpath(xpath));
        int getLength = txtValue.getText().length();
        String value = String.valueOf(getLength);
        System.out.println("Length of txt" + getLength);
        utilSubSteps.stepSaveVariable(value, variable);
}

    @StepExt("mobile field \"{fieldName}\" is filled with value \"{value}\" then enter")
    public void stepMobileFieldIsFilledWithValueEnter(String fieldName, String value) {
        String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        getActiveMobileDriver().findElement(By.xpath(xpath)).sendKeys(value + "\n");
    }

    @StepExt("mobile password field \"{fieldName}\" is filled with value \"{value}\"")
    public void stepMobilePasswordFieldIsFilledWithValue(String fieldName, String password) {
        String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        getActiveMobileDriver().findElement(By.xpath(xpath)).click();
        getActiveMobileDriver().findElement(By.xpath(xpath)).click();
        for (int i = 0; i < password.length(); i++) {
            if (i < password.length() - 1)
                getActiveMobileDriver().getKeyboard().sendKeys(password.charAt(i) + "");
            else
                getActiveMobileDriver().getKeyboard().sendKeys(password.charAt(i) + "\n");
        }
    }


    @StepExt("mobile password field is filled with value \"{value}\"")
    public void stepMobilePasswordFieldFilenameIsFilledWithValue(String password) {
        stepMobileFieldIsClicked("Password_Input");
        String[] passwordSplitter = password.split("|");
        try {
            for (int i = 0; i < passwordSplitter.length; i++) {
                Thread.sleep(500);
                switch (passwordSplitter[i]) {
                    case "0":
                        Runtime.getRuntime().exec("adb shell input keyevent " + "7");
                        break;
                    case "1":
                        Runtime.getRuntime().exec("adb shell input keyevent " + "8");
                        break;
                    case "2":
                        Runtime.getRuntime().exec("adb shell input keyevent " + "9");
                        break;
                    case "3":
                        Runtime.getRuntime().exec("adb shell input keyevent " + "10");
                        break;
                    case "4":
                        Runtime.getRuntime().exec("adb shell input keyevent " + "11");
                        break;
                    case "5":
                        Runtime.getRuntime().exec("adb shell input keyevent " + "12");
                        break;
                    case "6":
                        Runtime.getRuntime().exec("adb shell input keyevent " + "13");
                        break;
                    case "7":
                        Runtime.getRuntime().exec("adb shell input keyevent " + "14");
                        break;
                    case "8":
                        Runtime.getRuntime().exec("adb shell input keyevent " + "15");
                        break;
                    case "9":
                        Runtime.getRuntime().exec("adb shell input keyevent " + "16");
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @StepExt("mobile field \"{fieldName}\" value equals  \"{expectedValue}\"")
    public void stepMobileFieldValueEquals(String fieldName, String expectedValue) {
        WebElement webElement = (WebElement) getCurrentMobilePage().getField(fieldName);
        String actualValue = webElement.getText().trim();
        Assert.assertTrue(
            String.format(
                "\nActual value (of the field '%s'): \n'%s' \nis not equal to the expected value \n'%s'",
                fieldName, actualValue, expectedValue), actualValue.equals(expectedValue));
    }

    @StepExt("mobile field \"{fieldName}\" value contains  \"{expectedValue}\"")
    public void stepMobileFieldValueContains(String fieldName, String expectedValue) {
        WebElement webElement = (WebElement) getCurrentMobilePage().getField(fieldName);
        String actualValue = webElement.getText().trim();
        Assert.assertTrue(
            String.format(
                "\nActual value (of the field '%s'): \n'%s' \nis not equal to the expected value \n'%s'",
                fieldName, actualValue, expectedValue), actualValue.contains(expectedValue));
    }

    @StepExt("mobile field \"{fieldName}\" value is saved in variable \"{variable}\"")
    public void stepMobileFieldValueIsSavedInVariable(String fieldName, String variable) {
        try {
//            getActiveMobileDriver().getPageSource();
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = new ArrayList<>();
            try {
//                exampleOfFluentWithPredicate(xpath);
                setMobileDriverTimeoutToSecond(10);
                elementList = getActiveMobileDriver().findElements(By.xpath(xpath));

            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementList.size() == 0)) {
                String value = "0";
                utilSubSteps.stepSaveVariable(value, variable);
            } else {
                WebElement myEl = getActiveMobileDriver().findElement(By.xpath(xpath));
                String value = myEl.getText();
                if ((value == null) || (value.isEmpty())) {
                    value = myEl.getAttribute("value");

                }
                if ((value == null) || (value.isEmpty())) {
                    value = "0";
                }
                utilSubSteps.stepSaveVariable(value, variable);
            }
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field \"{fieldName}\" value is saved OTP in variable \"{variable}\"")
    public void stepMobileFieldValueIsSavedOTPInVariable(String fieldName, String variable) {
        try {
//            getActiveMobileDriver().getPageSource();
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = new ArrayList<>();
            try {
//                exampleOfFluentWithPredicate(xpath);
                setMobileDriverTimeoutToSecond(10);
                elementList = getActiveMobileDriver().findElements(By.xpath(xpath));

            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementList.size() == 0)) {
                String value = "0";
                utilSubSteps.stepSaveVariable(value, variable);
            } else {
                WebElement myEl = getActiveMobileDriver().findElement(By.xpath(xpath));
                String value = myEl.getText();
//                String value = elementList.get(elementList.size() - 1).getText();
                String OTPNumber = value.replaceAll("[^-?0-9]+", "");
                String firstFourChars = "";
                System.out.println("List OTPNumber: " + OTPNumber);
                if (OTPNumber.length() > 7)
                {
                    firstFourChars = OTPNumber.substring(1, 5);
                }
                else
                {
                    firstFourChars = OTPNumber.substring(0, 4);
                }
                if ((OTPNumber == null) || (OTPNumber.isEmpty())) {
                    OTPNumber = myEl.getAttribute("value");

                }
                if ((OTPNumber == null) || (OTPNumber.isEmpty())) {
                    OTPNumber = "0";
                }
                utilSubSteps.stepSaveVariable(firstFourChars, variable);
            }
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field \"{fieldName}\" value is saved OTP in variable \"{variable}\" for iPhone")
    public void stepMobileFieldValueIsSavedOTPInVariableiPhone(String fieldName, String variable) {
        try {
//            getActiveMobileDriver().getPageSource();
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = new ArrayList<>();
            try {
//                exampleOfFluentWithPredicate(xpath);
                setMobileDriverTimeoutToSecond(10);
                elementList = getActiveMobileDriver().findElements(By.xpath(xpath));

            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementList.size() == 0)) {
                String value = "0";
                utilSubSteps.stepSaveVariable(value, variable);
            } else {
                WebElement myEl = getActiveMobileDriver().findElement(By.xpath(xpath));
                String value = myEl.getText();
//                String value = elementList.get(elementList.size() - 1).getText();
                String OTPNumber = value.replaceAll("[^-?0-9]+", "");
                String firstFourChars = "";
                System.out.println("List OTPNumber: " + OTPNumber);
                if (OTPNumber.length() > 8)
                {
                    firstFourChars = OTPNumber.substring(1, 5);
                }
                else
                {
                    firstFourChars = OTPNumber.substring(0, 4);
                }
                if ((OTPNumber == null) || (OTPNumber.isEmpty())) {
                    OTPNumber = myEl.getAttribute("value");

                }
                if ((OTPNumber == null) || (OTPNumber.isEmpty())) {
                    OTPNumber = "0";
                }
                utilSubSteps.stepSaveVariable(firstFourChars, variable);
            }
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field \"{fieldName}\" is System out println")
    public void stepMobileFieldsysout(String fieldName) {
//        getActiveMobileDriver().getPageSource();
        String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        List<WebElement> elementList = new ArrayList<>();

        List<WebElement> listOfElements = getActiveMobileDriver().findElements(By.xpath(xpath));
        for (WebElement element : listOfElements) {
            System.out.println(element.getText());
        }


    }

    @StepExt("mobile field \"{fieldName}\" value is saved all item on list items in the variable \"{variable}\"")
    public void stepMobileFieldValueIsSavedInVariableAllItems(String fieldName, String variable) {
        try {
//            getActiveMobileDriver().getPageSource();
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
            try {

                setMobileDriverTimeoutToSecond(10);
                elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementList.size() == 0)) {
                String value = "0";
                utilSubSteps.stepSaveVariable(value, variable);
            } else {
                System.out.println("Number of elements:" + elementList.size());

                for (int i = 0; i < elementList.size(); i++) {
                    String value = elementList.get(i).getAttribute("value");
                    System.out
                        .println("Radio button text:" + value);
                    utilSubSteps.stepSaveVariable(value, variable);
                }
//                WebElement myEl = getActiveMobileDriver().findElement(By.xpath(xpath));
//                String value = elementList.get(0).getText();
//                if ((value == null) || (value.isEmpty())) {
//                    value = myEl.getAttribute("value");
//                }
//                if ((value == null) || (value.isEmpty())) {
//                    value = "0";
//                }
//                utilSubSteps.stepSaveVariable(value, variable);
            }

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }


    @StepExt("mobile field \"{fieldName}\" value is saved first item on list items in the variable \"{variable}\"")
    public void stepMobileFieldValueIsSavedInVariableFirtItems(String fieldName, String variable) {
        try {
//            getActiveMobileDriver().getPageSource();
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
            try {

//                setMobileDriverTimeoutToSecond(10);
              exampleOfFluentWithPredicate(xpath);
              elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementList.size() == 0)) {
                String value = "0";
                utilSubSteps.stepSaveVariable(value, variable);
            } else {
                WebElement myEl = getActiveMobileDriver().findElement(By.xpath(xpath));
                String value = elementList.get(0).getText();
                if ((value == null) || (value.isEmpty())) {
                    value = myEl.getAttribute("value");
                }
                if ((value == null) || (value.isEmpty())) {
                    value = "0";
                }
                utilSubSteps.stepSaveVariable(value, variable);
            }

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field \"{fieldName}\" value is saved last item on list items in the variable \"{variable}\"")
    public void stepMobileFieldValueIsSavedInVariableLastitems(String fieldName, String variable) {
        try {
//            getActiveMobileDriver().getPageSource();
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = getActiveMobileDriver().findElements(By.xpath(xpath));

            try {

//                setMobileDriverTimeoutToSecond(10);
              exampleOfFluentWithPredicate(xpath);
              elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementList.size() == 0)) {
                String value = "0";
                utilSubSteps.stepSaveVariable(value, variable);
            } else {
                WebElement myEl = getActiveMobileDriver().findElement(By.xpath(xpath));
//                for (WebElement radio : elementList) {
                String value = elementList.get(elementList.size() - 1).getText();
                if ((value == null) || (value.isEmpty())) {
                    value = myEl.getAttribute("value");

                }
                if ((value == null) || (value.isEmpty())) {
                    value = "0";
                }
                utilSubSteps.stepSaveVariable(value, variable);
            }
//          }
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field \"{fieldName}\" value contains text \"{text}\"  in list items is saved the variable \"{variable}\"")
    public void stepMobileFieldClickItemsSaveVariable(String fieldName, String text,
        String variable) {
        try {
//            getActiveMobileDriver().getPageSource();
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = getActiveMobileDriver().findElements(By.xpath(xpath));

            try {

//                setMobileDriverTimeoutToSecond(10);
              exampleOfFluentWithPredicate(xpath);
              elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementList.size() == 0)) {
                String value = "0";
                utilSubSteps.stepSaveVariable(value, variable);
            } else {
                WebElement myEl = getActiveMobileDriver().findElement(By.xpath(xpath));
//                String value = elementList.get(elementList.size()-1).getText();
                for (WebElement radio : elementList) {
                    String value = radio.getText();

                    if (value.equals(text)) {
                        if ((value == null) || (value.isEmpty())) {
                            value = myEl.getAttribute("value");

                        }
                        if ((value == null) || (value.isEmpty())) {
                            value = "0";
                        }
                        utilSubSteps.stepSaveVariable(value, variable);
                        break;
                    }

                }
            }

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field \"{fieldName}\" is click last item on list items")
    public void stepMobileFieldClickLastitems(String fieldName) {
        try {
//            getActiveMobileDriver().getPageSource();
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = getActiveMobileDriver().findElements(By.xpath(xpath));

            try {

//                setMobileDriverTimeoutToSecond(10);
              exampleOfFluentWithPredicate(xpath);
              elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementList.size() == 0)) {
                String value = "0";
                stepMobileFieldIsClicked(value);
            } else {

                WebElement myEl = getActiveMobileDriver().findElement(By.xpath(xpath));
                String value = elementList.get(elementList.size() - 1).getText();
                if ((value == null) || (value.isEmpty())) {
                    value = myEl.getAttribute("value");
                }
                if ((value == null) || (value.isEmpty())) {
                    value = "0";
                }
                myEl.click();

            }
//            }

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field \"{fieldName}\" exists")
    public void stepMobileFieldExists(String fieldName) {
        Assert.assertTrue(isDisplayed(fieldName, 60).size() > 0);
    }

    @StepExt("mobile field \"{fieldName}\" exists when waiting for \"{time}\" seconds")
    public void stepMobileFieldExistsWhenWaitingTime(String fieldName,int time) {
        Assert.assertTrue(isDisplayed(fieldName,time).size() > 0);
    }

    @StepExt("mobile field \"{fieldName}\" exists very fast")
    public void stepMobileFieldExistsVeryFast(String fieldName) {
        Assert.assertTrue(isDisplayed(fieldName, 2).size() > 0);
    }

    @StepExt("mobile field \"{fieldName}\" not exists")
    public void stepMobileFieldNotExists(String fieldName) {
        Assert.assertTrue(isDisplayed(fieldName, 10).size() == 0);
    }


    @StepExt("mobile field check keyboard active with android")
    public void stepMobileFieldIsKeyboardShown () {
        boolean isKeyboardShown = ((AndroidDriver) getActiveMobileDriver()).isKeyboardShown();

        try {
            if (isKeyboardShown) {
                System.out.println("KeyBoardAndroid(): Keyboard is show\n");
//               getActiveMobileDriver().hideKeyboard();
            }
        }
        catch (final NoSuchSessionException e) {
            System.err.println("KeyBoardAndroid(): Keyboard is hidden\n" + e.getMessage());
            return;
        }

    }

    @StepExt("mobile field check hideKeyboard")
    public void stepMobileFieldIshideKeyboard () {
        getActiveMobileDriver().hideKeyboard();
    }

    @StepExt("mobile field Delete All Cookies Web only")
    public void stepMobileFielddeleteAllCookies () {
        getActiveMobileDriver().manage().deleteAllCookies();

    }

    @StepExt("mobile field \"{fieldName}\" is displayed with value \"{value}\"")
    public void stepMobileFieldIsDisplayedWithValue(String fieldName, String value) {
        String xpathBuilt = ScriptUtils.xpathFrom("xpath_rosy_mobile", fieldName, value);
        setMobileDriverTimeoutToSecond(15);
        List<WebElement> eleList = getActiveMobileDriver().findElements(By.xpath(xpathBuilt));
        while (eleList.size() == 0) {
            scrollDown();
            eleList = getActiveMobileDriver().findElements(By.xpath(xpathBuilt));
        }
        Assert.assertTrue("Element is displayed", eleList.size() > 0);
        setMobileDriverTimeoutToSecond(60);
    }

    @StepExt("mobile field \"{fieldName}\" is clicked with value \"{value}\"")
    public void stepMobileFieldIsClickedWithValue(String fieldName, String value) {
        List<WebElement> eleList = null;
        String xpathBuilt = ScriptUtils.xpathFrom("xpath_rosy_mobile", fieldName, value);
        setMobileDriverTimeoutToSecond(1);
        eleList = getActiveMobileDriver().findElements(By.xpath(xpathBuilt));
        int count = 0;
        while (eleList.size() == 0 && count < 5) {
            count++;
            scrollDown();
            eleList = getActiveMobileDriver().findElements(By.xpath(xpathBuilt));
        }
        eleList.get(0).click();
        setMobileDriverTimeoutToSecond(60);
    }

    @StepExt("mobile field \"{fieldName}\" is scroll down")
    public void stepMobileFieldIsClickedElementWhenScrollDown(String fieldName) {
        String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        setMobileDriverTimeoutToSecond(10);
        List<MobileElement> elements = getActiveMobileDriver().findElements(By.xpath(xpath));
        int count = 0;
        while (elements.size() == 0 && count < 5) {
            elements = getActiveMobileDriver().findElements(By.xpath(xpath));
            if(elements.size() != 0){
                break;
            }
            count++;
            scrollDown();

        }
        elements.get(0).click();
//        setMobileDriverTimeoutToSecond(60);
    }

    @StepExt("mobile field is scroll down to text \"{text}\" of iOS")
    public void stepMobileFieldIsClickedTextElementWhenScrollDown(String text) {
        String fieldName = "//*[contains(@name,'"+ text +"')]";
        setMobileDriverTimeoutToSecond(5);
        List<MobileElement> elements = getActiveMobileDriver().findElements(By.xpath(fieldName));
        int count = 0;
        while (elements.size() == 0 && count < 5) {
            elements = getActiveMobileDriver().findElements(By.xpath(fieldName));
            if(elements.size() != 0){
                break;
            }
            count++;
            scrollDown();

        }
        elements.get(0).click();
//        setMobileDriverTimeoutToSecond(60);
    }

    @StepExt("application is logged in with user \"{user}\" and \"{password}\"")
    public void stepApplicationIsLoggedInWithUserAndPassword(String user, String password) {
        pageSubSteps.stepPageIsLoaded("Login_FirstPageAndroidMobile");
        if (isDisplayed("NotUser_Button", 10).size() > 0) {
            stepMobileFieldIsClicked("NotUser_Button");
        }
        utilSubSteps.stepWaitFor(5);
        stepMobileFieldIsClicked("Login_Button");
        pageSubSteps.stepPageIsLoaded("Login_InputUserPageAndroidMobile");
        stepMobileFieldIsFilledWithValue("UserID_Input", user);
        stepMobilePasswordFieldIsFilledWithValue("Password_Rosy_Input", password);
    }

    @StepExt("switch to coordinates \"{x}\" and \"{y}\"")
    public void swithchTo(int x, int y) {
        try {
            setMobileDriverTimeoutToSecond(60);

            try {
                setMobileDriverTimeoutToSecond(60);
                TouchAction action = new TouchAction(getActiveMobileDriver());
                action.press(PointOption.point(x, y))
                    .release().perform();

                //   getActiveMobileDriver().switchTo().window("handle");
            } catch (Exception ex) {
                throw new WebDriverException();
            }
        } catch (Exception e) {
            System.err.println("swipeElementAndroid(): TouchAction FAILED\n" + e.getMessage());
            return;
        }

    }

    @StepExt("log out application")
    public void stepLogOutApplication() {
        stepMobileFieldIsClicked("LeftMenu_Button");
        stepMobileFieldIsClicked("LogOut_Button");
        stepMobileFieldIsClicked("NotUser_Button");
    }


    @StepExt("create account with first name \"{firstname}\" and last name \"{lastname}\"")
    public void firstandlastname(String firstname, String lastname) {
        pageSubSteps.stepPageIsLoaded("NamePage");
        stepMobileFieldIsFilledWithValue("Frist_Name", firstname);
        stepMobileFieldIsFilledWithValue("LastName", lastname);
        stepMobileFieldIsClicked("enter");


    }

    @StepExt("create account with email \"{email}\"")
    public void stepCreateAccountwithEmail(String email) {
        pageSubSteps.stepPageIsLoaded("Login_FirstPageAndroidMobile");
        if (isDisplayed("NotUser_Button", 10).size() > 0) {
            stepMobileFieldIsClicked("NotUser_Button");
        }
        stepMobileFieldIsClicked("CreateAccount_Button");
        pageSubSteps.stepPageIsLoaded("EmailRosy");
        stepMobileFieldIsFilledWithValue("Email_address", email);
        getActiveMobileDriver().getKeyboard().pressKey(Keys.ENTER);
    }


    @StepExt("mobile field count with \"{fieldName}\" and compare  \"{variable}\" ")
    public void stepMobileFieldCompareAuto(String fieldName, int variable) {
        try {

            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            setMobileDriverTimeoutToSecond(60);
            List<MobileElement> elements = getActiveMobileDriver().findElements(By.xpath(xpath));

            try {
                Assert.assertEquals(elements.size(), variable);
            } catch (Exception ex) {
            }
            setMobileDriverTimeoutToSecond(60);

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field \"{fieldName}\" is counted value and saved in variable \"{variable}\"")
    public void stepCountVault(String fieldName, String variable) {
        try {
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = new ArrayList<>();
            try {
                setMobileDriverTimeoutToSecond(10);
                elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementList.size() == 0)) {
                String value = "0";
                utilSubSteps.stepSaveVariable(value, variable);
            } else {
                String value = String.valueOf(elementList.size());
                utilSubSteps.stepSaveVariable(value, variable);
            }
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("get element size in \"{fieldName}\" save in \"{variable}\"")
    public void stepGetElementSize(String fieldName, String variable) {
        try {
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            int count = 0;
            List<WebElement> elementList = new ArrayList<>();
            try {
                setMobileDriverTimeoutToSecond(10);
                elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementList.size() == 0)) {
                String value = "0";
                utilSubSteps.stepSaveVariable(value, variable);
            } else {
                String value = String.valueOf(elementList.size());

                for (int i = 0; i < value.length(); i++) {
                    if (value.charAt(i) != ' ')
                        count++;
                }
                String countSize = String.valueOf(count);
                utilSubSteps.stepSaveVariable(countSize, variable);
            }
        } catch (Exception ex) {
            throw new WebDriverException();
        }

    }

    @StepExt("count number search contact in sharing with \"{fieldName}\" save in \"{variable}\"")
    public void stepCountSearchContactSharing(String fieldName, String variable) {
        try {
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = new ArrayList<>();
            try {
                setMobileDriverTimeoutToSecond(10);
                elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementList.size() == 0)) {
                String value = "0";
                utilSubSteps.stepSaveVariable(value, variable);
            } else {
                String value = String.valueOf(elementList.size());
                utilSubSteps.stepSaveVariable(value, variable);
            }
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }


    @StepExt("count number search contact in \"{fieldName}\" save in \"{variable}\"")
    public void stepCountSearchContact(String fieldName, String variable) {
        try {
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = new ArrayList<>();
            try {
                setMobileDriverTimeoutToSecond(10);
                elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementList.size() == 0)) {
                String value = "0";
                utilSubSteps.stepSaveVariable(value, variable);
            } else {
                String value = String.valueOf(elementList.size() - 1);
                utilSubSteps.stepSaveVariable(value, variable);
            }
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }


    @StepExt("count number stores in \"{fieldName}\" save in \"{variable}\"")
    public void stepCountStores(String fieldName, String variable) {
        try {
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = new ArrayList<>();

            try {
                setMobileDriverTimeoutToSecond(10);
                elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((elementList.size() == 0)) {
                String value = "0";
                utilSubSteps.stepSaveVariable(value, variable);
            } else {
                String value = String.valueOf(elementList.size() - 1);
                utilSubSteps.stepSaveVariable(value, variable);
            }
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }


    @StepExt("mobile field count contacts with \"{fieldName}\" and compare  \"{variable}\" ")
    public void stepMobileFieldContactsCompareAuto(String fieldName, int variable) {
        try {

            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            setMobileDriverTimeoutToSecond(60);
            List<MobileElement> elements = getActiveMobileDriver().findElements(By.xpath(xpath));
            int value = (elements.size() - 2) / 2;

            try {
                Assert.assertEquals(value, variable);
            } catch (Exception ex) {
            }
            setMobileDriverTimeoutToSecond(60);

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field swipe left with screen")
    public void stepMobileFieldscrollSwipe() {
        TouchAction swipe = new TouchAction(getActiveMobileDriver())
            .press(PointOption.point(810, 666))
            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
            .moveTo(PointOption.point(188, 666))
            .release()
            .perform();
    }



    public void scroll(int startx, int starty, int endx, int endy) {

        TouchAction touchAction = new TouchAction(getActiveMobileDriver());

        touchAction.longPress(PointOption.point(startx, starty))
            .moveTo(PointOption.point(endx, endy))
            .release()
            .perform();

    }

    public void scrollDown() {

        //The viewing size of the device
        Dimension size = getActiveMobileDriver().manage().window().getSize();

        //Starting y location set to 80% of the height (near bottom)
        int starty = (int) (size.height * 0.74);
        //Ending y location set to 20% of the height (near top)
        int endy = (int) (size.height * 0.4);
        //x position set to mid-screen horizontally
        int startx = (size.width / 2);

        scroll(startx, starty, startx, endy);

    }

    public void scrollUp() {

        int startX = 0;
        int endX = 0;
        int startY = 0;
        int endY = 0;
        //The viewing size of the device
        Dimension size = getActiveMobileDriver().manage().window().getSize();

        //Starting y location set to 80% of the height (near bottom)
        int endy = (int) (size.height * 0.80);
        //Ending y location set to 20% of the height (near top)
        int starty = (int) (size.height * 0.20);
        //x position set to mid-screen horizontally
        int startx = (int) size.width / 2;

        scroll(startx, starty, startx, endy);


    }

    @StepExt("mobile field scroll down with OTP screen")
    public void stepMobileFieldscrollDownAutoOTP() {
        Dimension size = getActiveMobileDriver().manage().window().getSize();

        //Starting y location set to 80% of the height (near bottom)
        int endy = (int) (size.height * 0.80);
        //Ending y location set to 20% of the height (near top)
        int starty = (int) (size.height * 0.01);
        //x position set to mid-screen horizontally
        int startx = (int) size.width / 2;

        System.out.println("start: " + starty);
        scroll(startx, starty, startx, endy);
    }


    @StepExt("mobile field scroll down with screen")
    public void stepMobileFieldscrollDownAuto() {
        scrollDown();
    }

    @StepExt("mobile field scroll down with Notification screen")
    public void stepMobileFieldscrollDownNotificationScreen() {
        //The viewing size of the device
        Dimension size = getActiveMobileDriver().manage().window().getSize();

        //Starting y location set to 80% of the height (near bottom)
        int starty = (int) (size.height * 0.99);
        //Ending y location set to 20% of the height (near top)
        int endy = (int) (size.height * 0.1);
        //x position set to mid-screen horizontally
        int startx = (size.width / 2);
        System.out.println("start: " + starty);
        scroll(startx, starty, startx, endy);
    }

    @StepExt("mobile field scroll down Full Year of Date screen")
    public void stepMobileFieldscrollDownYearDate() {
        //The viewing size of the device
        Dimension size = getActiveMobileDriver().manage().window().getSize();

        //Starting y location set to 80% of the height (near bottom)
        int starty = (int) (size.height * 0.60);
        //Ending y location set to 20% of the height (near top)
        int endy = (int) (size.height * 0.50);
        //x position set to mid-screen horizontally
        int startx = (size.width / 2);
        scroll(startx, starty, startx, endy);
    }

    @StepExt("mobile field scroll down to bottom page")
    public void stepMobileFieldscrollToBottom() {
        Dimension size = getActiveMobileDriver().manage().window().getSize();
        scroll(0, 0, 0, 500);
    }


    @StepExt("mobile field is scroll to text \"{text}\" for Android screen")
    public void stepMobileFieldscrolltoElement(String text) {
        MobileElement listitem = (MobileElement) getActiveMobileDriver().findElement(
            MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0))" +
                    ".scrollIntoView(new UiSelector().textContains(\"" + text + "\").instance(0))"));
        listitem.click();

    }

    @StepExt("mobile field is scroll to id \"{id}\" for Android screen")
    public void stepMobileFieldscrolltoID(String id) {
        MobileElement listitem = (MobileElement) getActiveMobileDriver().findElement(
            MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0))" +
                    ".scrollIntoView(new UiSelector().resourceIdMatches(\"" + id + "\"))"));
        System.out.println(listitem.getLocation());
        listitem.click();
    }



    @StepExt("mobile field is swipe from left to right")
    public void stepMobileFieldSwipeFromleftToRight() {
        Dimension size = getActiveMobileDriver().manage().window().getSize();
        int anchor = (int) (size.height * 0.5);
        int startPoint = (int) (size.width * 0.1);
        int endPoint = (int) (size.width * 0.8);

        new TouchAction(getActiveMobileDriver())
            .press(PointOption.point(startPoint, anchor))
            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
            .moveTo(PointOption.point(endPoint, anchor))
            .release().perform();
    }


    @StepExt("mobile field is swipe from right to left")
    public void stepMobileFieldSwipeByPercentage() {
        Dimension size = getActiveMobileDriver().manage().window().getSize();
        int anchor = (int) (size.height * 0.5);
        int startPoint = (int) (size.width * 0.9);
        int endPoint = (int) (size.width * 0.01);

        new TouchAction(getActiveMobileDriver())
            .press(PointOption.point(startPoint, anchor))
            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
            .moveTo(PointOption.point(endPoint, anchor))
            .release().perform();
    }



    @StepExt("mobile field scroll up with screen")
    public void stepMobileFieldscrollUpAuto() {
        scrollUp();
    }


    @StepExt("mobile field scroll up with \"{fieldName}\"")
    public void stepMobileFieldscrollUpFindElement(String fieldName) {
        try {
//            getActiveMobileDriver().getPageSource();
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            setMobileDriverTimeoutToSecond(60);
            List<MobileElement> elements = getActiveMobileDriver().findElements(By.xpath(xpath));
            System.out.println(elements.get(0));
            try {
                setMobileDriverTimeoutToSecond(60);
                SwipeAppium swipeAppium = new SwipeAppium();
                swipeAppium.swipeElementAndroid(elements.get(0), SwipeAppium.Direction.UP);

                getActiveMobileDriver().getPageSource();
            } catch (Exception ex) {
            }
            setMobileDriverTimeoutToSecond(60);
            getActiveMobileDriver().getPageSource();

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }


    @StepExt("mobile field zoom \"{fieldName}\" ")
    public void stepMobileFieldZoomAuto(String fieldName) {
        try {

            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            setMobileDriverTimeoutToSecond(60);
            List<MobileElement> elements = getActiveMobileDriver().findElements(By.xpath(xpath));
//            System.out.println(elements.get(0));
            try {
                setMobileDriverTimeoutToSecond(60);
                Zoom zoom = new Zoom();
                zoom.ZoomInAndOut(xpath);
            } catch (Exception ex) {
            }
            setMobileDriverTimeoutToSecond(60);

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field size \"{fieldName}\" and save \"{variable}\" ")
    public void stepMobileFieldSize(String fieldName, String variable) {
        try {

            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            setMobileDriverTimeoutToSecond(60);
            List<MobileElement> elements = getActiveMobileDriver().findElements(By.xpath(xpath));
//            System.out.println(elements.get(0));
            try {
                setMobileDriverTimeoutToSecond(60);
                Dimension windowSize = elements.get(0).getSize();
                String value = String.valueOf(windowSize);
//                System.out.println(value);
                String size = value.replace("(", "");
                String size1 = size.replace(")", "");
                String string = size1;
                String[] parts = string.split(",");
                String part1 = parts[0]; // 004-
                System.out.println(part1);
                String part2 = parts[1];
                utilSubSteps.stepSaveVariable(part1, variable);
            } catch (Exception ex) {
            }
            setMobileDriverTimeoutToSecond(60);
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field tablet delete item has memmories")
    public void stepMobileFieldDeleteHasStoryAuto() {
        try {
            setMobileDriverTimeoutToSecond(60);
            List<MobileElement> elements = getActiveMobileDriver().findElements(By.xpath(
                "(//*[@content-desc='vault-items']/android.view.ViewGroup/android.view.ViewGroup)"));
            int listvault = elements.size();
            try {
                setMobileDriverTimeoutToSecond(60);
                for (int i = 1; i <= listvault; i++) {
                    List<MobileElement> vault = getActiveMobileDriver().findElements(By.xpath(
                        "(//*[@content-desc='vault-items']/android.view.ViewGroup/android.view.ViewGroup)["
                            + i + "]/android.view.ViewGroup/android.view.ViewGroup"));
                    if (vault.size() > 0) {
                        vault.get(0).click();
                        pageSubSteps.stepPageIsLoaded("ItemViewerPage");
                        stepMobileFieldIsClicked("Delete_Vault");
                        stepMobileFieldIsClicked("Delete_Button");
                        stepMobileFieldIsClicked("ConfirmDelete_Button");
                        break;
                    } else {
                        System.out.println("Item " + i + " without memory");
                    }
                }
            } catch (Exception ex) {
            }
            setMobileDriverTimeoutToSecond(60);
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field tablet delete item without memmories")
    public void stepMobileFieldDeleteWithoutStoryAuto() {
        try {
            setMobileDriverTimeoutToSecond(60);
            List<MobileElement> elements = getActiveMobileDriver().findElements(By.xpath(
                "(//*[@content-desc='vault-items']/android.view.ViewGroup/android.view.ViewGroup)"));
            int listvault = elements.size();
            try {
                setMobileDriverTimeoutToSecond(60);
                for (int i = 1; i <= listvault; i++) {
                    List<MobileElement> vault = getActiveMobileDriver().findElements(By.xpath(
                        "(//*[@content-desc='vault-items']/android.view.ViewGroup/android.view.ViewGroup)["
                            + i + "]/android.view.ViewGroup/android.view.ViewGroup"));

                    List<MobileElement> vaultnotStory = getActiveMobileDriver().findElements(
                        By.xpath(
                            "(//*[@content-desc='vault-items']/android.view.ViewGroup/android.view.ViewGroup)["
                                + i + "]"));
                    if (vault.size() == 0) {
                        vaultnotStory.get(0).click();
                        pageSubSteps.stepPageIsLoaded("ItemViewerPage");
                        stepMobileFieldIsClicked("Delete_Vault");
                        stepMobileFieldIsClicked("ConfirmDelete_Button");
                        break;
                    } else {
                        System.out.println("Item " + i + " has memories");
                    }
                }
            } catch (Exception ex) {
            }
            setMobileDriverTimeoutToSecond(60);
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }


    @StepExt("mobile field tablet input PIN \"{variable}\"")
    public void stepMobileFieldForgotPin(String variable) {
        try {
            setMobileDriverTimeoutToSecond(60);
            char[] ch = variable.toCharArray();
            try {
                setMobileDriverTimeoutToSecond(60);
                for (int i = 0; i < ch.length; i++) {
                    System.out.println(ch[i]);
                    List<MobileElement> vault = getActiveMobileDriver()
                        .findElements(By.xpath("//*[@text='" + ch[i] + "']"));
                    System.out.println(vault.get(0));
                    vault.get(0).click();
                }
            } catch (Exception ex) {
            }
            setMobileDriverTimeoutToSecond(60);
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("uninstall App")
    public void stepMobileFieldUninstall() {
        try {
            setMobileDriverTimeoutToSecond(60);
            getActiveMobileDriver().removeApp("com.demorosyv2.dev");
            setMobileDriverTimeoutToSecond(60);
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("Clear data App")
    public void stepMobileFieldClearData() {
        try {
//            setMobileDriverTimeoutToSecond(60);
            getActiveMobileDriver().resetApp();
//            setMobileDriverTimeoutToSecond(60);
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("Stop appium server")
    public void stepMobileFieldStopAppium() {
//       String[] command = { "/usr/bin/killall", "-KILL", "node" };
//		try {
//			Runtime.getRuntime().exec(command);
//			System.out.println("Appium server stopped.");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Exited!");
            }
        });
        for (; ; )
            ;
    }


    @StepExt("mobile field play memories with list \"{fieldName}\"")
    public void stepMobileFieldplayMemories(String fieldName) {
        try {
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            setMobileDriverTimeoutToSecond(180);
            List<MobileElement> elements = getActiveMobileDriver().findElements(By.xpath(xpath));
            int i = elements.size();
            int value = i - 1;
            getActiveMobileDriver()
                .findElement(By.xpath("(//android.widget.TextView[@text='Play'])[" + value + "]"))
                .click();

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("Width in \"{fieldName}\" save in \"{variable}\"")
    public void stepWidthVault(String fieldName, String variable) {
        try {
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = new ArrayList<>();
            try {
                setMobileDriverTimeoutToSecond(10);
                elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
                float value = elementList.get(0).getSize().width;
                String s = String.valueOf(value);
//                System.out.println(s);
                utilSubSteps.stepSaveVariable(s, variable);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile tap coordinates \"{x}\" and \"{y}\"")
    public void tapMobile(int x, int y) {
        try {
            setMobileDriverTimeoutToSecond(60);
            TouchAction action = new TouchAction(getActiveMobileDriver());
            action.press(PointOption.point(x, y))
                .release().perform();
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("select webview")
    public void selectwebview() {
        try {
            Set<String> contextNames = getActiveMobileDriver().getContextHandles();
            System.out.println(contextNames.size());
            for (String contextName : contextNames) {
                System.out.println(contextName);
                if (contextName.contains("WEBVIEW")) {
                    getActiveMobileDriver().context(contextName);
                }
            }
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field \"{fieldName}\" is tap")
    public void tapElement(String fieldName) {
        try {
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            setMobileDriverTimeoutToSecond(180);
            List<MobileElement> elements = getActiveMobileDriver().findElements(By.xpath(xpath));

            try {
                setMobileDriverTimeoutToSecond(60);
                AndroidTouchAction touch = new AndroidTouchAction(getActiveMobileDriver());
                touch.tap(TapOptions.tapOptions()
                    .withElement(ElementOption.element(elements.get(0))))
                    .perform();
            } catch (Exception ex) {
                throw new WebDriverException();
            }
        } catch (Exception e) {
            System.err.println("tapElementAndroid(): TouchAction FAILED\n" + e.getMessage());
            return;
        }
    }


    @StepExt("mobile field is filled with value \"{value}\" ADB")
    public void stepMobileFieldIsFilledWithValueADB(String value) {
        getActiveMobileDriver().getKeyboard().sendKeys(value );
//        Actions action = new Actions(getActiveMobileDriver());
//        action.sendKeys(value).perform();

    }


    @StepExt("mobile field is senkeys with value \"{value}\" ADB")
    public void stepMobileFieldIsSenkeysWithValueADB(String value) {
        Random r = new Random();
        for(int i = 0; i < value.length(); i++) {
            try {
                Thread.sleep((int)(r.nextGaussian() * 15 + 100));
            } catch(InterruptedException e) {}
            String s = new StringBuilder().append(value.charAt(i)).toString();
            getActiveMobileDriver().getKeyboard().sendKeys(s );
        }

    }


    @StepExt("mobile field is filled with value SPACE ADB")
    public void stepMobileFieldIsFilledWithSpaceValueADB() {
        getActiveMobileDriver().getKeyboard().sendKeys(Keys.SPACE);
    }

    @StepExt("mobile pass field \"$fieldName\" is clicked and fill with backspace value")
    public void stepMobilePassFieldFilledIsClickedAndFillWithBackspaceValue(String fieldName) {
        try {
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            setMobileDriverTimeoutToSecond(30);
            getActiveMobileDriver().findElement(By.xpath(xpath)).click();
            getActiveMobileDriver().getKeyboard().sendKeys(Keys.BACK_SPACE);
            getActiveMobileDriver().getKeyboard().sendKeys(Keys.BACK_SPACE);
            getActiveMobileDriver().getKeyboard().sendKeys(Keys.BACK_SPACE);
            getActiveMobileDriver().getKeyboard().sendKeys(Keys.BACK_SPACE);
            getActiveMobileDriver().getKeyboard().sendKeys(Keys.BACK_SPACE);
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("field choose memories  \"{fieldName}\" is clicked")
    public void stepFieldIsClickedd(String fieldName) {
        String xpath = getCurrentPage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");

        List<WebElement> allProduct = getActiveDriver().findElements(By.xpath(xpath));
        int value = allProduct.size() - 1;
        List<WebElement> allProduct1 = getActiveDriver()
            .findElements(By.xpath("(//div[text()='Play']/parent::div)[" + value + "]"));
        allProduct1.get(0).click();

        try {
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);

            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        } catch (NoSuchWindowException e) {
        }
    }

    @StepExt("field count \"{fieldName}\"  save in \"{variable}\"")
    public void stepFieldIsCount(String fieldName, String variable) {
        String xpath = getCurrentPage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");

        List<WebElement> elementList = getActiveDriver().findElements(By.xpath(xpath));
        if ((elementList.size() == 0)) {
            String value = "0";
            utilSubSteps.stepSaveVariable(value, variable);
        } else {
            String value = String.valueOf(elementList.size() - 1);
            utilSubSteps.stepSaveVariable(value, variable);
        }
        try {
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);

            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        } catch (NoSuchWindowException e) {
        }
    }


    @StepExt("field upload \"{fieldName}\" with url \"{variable}\"")
    public void stepFieldIsUpload(String fieldName, String variable) throws AWTException {
        String xpath = getCurrentPage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        String a = variable;
        WebElement uploadFile = getActiveDriver().findElement(By.xpath(xpath));
        uploadFile.sendKeys(variable);
        try {
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);

            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        } catch (NoSuchWindowException e) {
        }
    }

    @StepExt("field delete item without memmories")
    public void stepFieldDeleteWithoutStoryAuto() {
        List<WebElement> elementList = getActiveDriver()
            .findElements(By.xpath("//div[@aria-label='vault-items']//div[@tabindex='0']"));
        int listvault = elementList.size();
        try {
            for (int i = 1; i <= listvault; i++) {
                List<WebElement> vault = getActiveDriver().findElements(By.xpath(
                    "(//div[@aria-label='vault-items']//div[@tabindex='0'])[" + i
                        + "]//div[@dir='auto']"));

                List<WebElement> vaultnotStory = getActiveDriver().findElements(
                    By.xpath("(//div[@aria-label='vault-items']//div[@tabindex='0'])[" + i + "]"));
                if (vault.size() == 0) {
                    vaultnotStory.get(0).click();
                    pageSubSteps.stepPageIsLoaded("PreviewPage");
                    stepFieldIsClicked("DeleteFirstStories_Button");
                    stepFieldExists("Yes_Button");
                    stepFieldIsClicked("Yes_Button");

                    break;
                } else {
                    System.out.println("Item " + i + " has memories");
                }
            }
        } catch (Exception ex) {
        }
        try {
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);

            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        } catch (NoSuchWindowException e) {
        }
    }

    @StepExt("field delete item has memmories")
    public void stepFieldDeletHasStoryAuto() {
        List<WebElement> elementList = getActiveDriver()
            .findElements(By.xpath("//div[@aria-label='vault-items']//div[@tabindex='0']"));
        int listvault = elementList.size();
        try {
            for (int i = 1; i <= listvault; i++) {
                List<WebElement> vault = getActiveDriver().findElements(By.xpath(
                    "(//div[@aria-label='vault-items']//div[@tabindex='0'])[" + i
                        + "]//div[@dir='auto']"));

                List<WebElement> vaultnotStory = getActiveDriver().findElements(
                    By.xpath("(//div[@aria-label='vault-items']//div[@tabindex='0'])[" + i + "]"));
                if (vault.size() > 0) {
                    vault.get(0).click();
                    pageSubSteps.stepPageIsLoaded("PreviewPage");
                    stepFieldIsClicked("DeleteFirstStories_Button");
                    stepFieldExists("DeleteFirstStories_Button");
                    stepFieldIsClicked("DeleteFirstStories_Button");
                    stepFieldIsClicked("Yes_Button");
                    break;
                } else {
                    System.out.println("Item " + i + " has memories");
                }
            }
        } catch (Exception ex) {
        }
        try {
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);

            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        } catch (NoSuchWindowException e) {
        }
    }


    @StepExt("create account with \"{fieldName}\"")
    public void stepCreateAccountwithfieldName(String fieldName) {
        String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        getActiveMobileDriver().manage().timeouts().implicitlyWait(0, TimeUnit.MINUTES);

        pageSubSteps.stepPageIsLoaded("Login_FirstPageAndroidMobile");
        if (isDisplayed("NotUser_Button", 10).size() > 0) {
            stepMobileFieldIsClicked("NotUser_Button");
        }
        stepMobileFieldIsClicked("Register_Button");
        pageSubSteps.stepPageIsLoaded("EmailRosy");
        Date now = new Date(); // java.util.Date, NOT java.sql.Date or java.sql.Timestamp!
        String format3 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(now);
        System.out.println(format3);
        getActiveMobileDriver().findElement(By.xpath(xpath)).sendKeys(format3 + "aphone@gmail.com");

        getActiveMobileDriver().getKeyboard().pressKey(Keys.ENTER);
    }

    @StepExt("create account with \"{fieldName}\" Tablet")
    public void stepCreateAccountwithfieldNameTablet(String fieldName) {
        String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        getActiveMobileDriver().manage().timeouts().implicitlyWait(0, TimeUnit.MINUTES);

        pageSubSteps.stepPageIsLoaded("Login_FirstPageAndroidMobile");
        if (isDisplayed("NotUser_Button", 10).size() > 0) {
            stepMobileFieldIsClicked("NotUser_Button");
        }
        stepMobileFieldIsClicked("CreateAccount_Button");
        pageSubSteps.stepPageIsLoaded("EmailRosy");
        Date now = new Date(); // java.util.Date, NOT java.sql.Date or java.sql.Timestamp!
        String format3 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(now);
        System.out.println(format3);
        getActiveMobileDriver().findElement(By.xpath(xpath)).sendKeys(format3 + "tablet@gmail.com");
        getActiveMobileDriver().getKeyboard().pressKey(Keys.ENTER);
    }

    @StepExt("application is logged in with user with \"{user}\" and \"{password}\"")
    public void stepApplicationIsLoggedInWithUserAndPasswordAndroid(String user, String password) {
//        sleep(5);
        pageSubSteps.stepPageIsLoaded("Login_FirstPageAndroidMobile");
        if (isDisplayed("NotUser_Button", 10).size() > 0) {
            stepMobileFieldIsClicked("NotUser_Button");
        }
        sleep(2);
        stepMobileFieldIsClicked("Login_Button");
        pageSubSteps.stepPageIsLoaded("Login_InputUserPageAndroidMobile");
        stepMobileFieldIsFilledWithValue("UserID_Input", user);
        sleep(2);
        stepMobilePasswordFieldIsFilledWithValue("Password_Rosy_Input", password);

    }

    @StepExt("application is logged in with user free trial account with \"{user}\" and \"{password}\"")
    public void stepApplicationIsLoggedInWithUserFreeTrialAndPasswordAndroid(String user, String password) {
        pageSubSteps.stepPageIsLoaded("Login_FirstPageAndroidMobile");
        sleep(2);
        stepMobileFieldIsClicked("Login_Free_Trial_Button");
        pageSubSteps.stepPageIsLoaded("Login_InputUserPageAndroidMobile");
        stepMobileFieldIsFilledWithValue("UserID_Free_Trial_Input", user);
        sleep(2);
        stepMobilePasswordFieldIsFilledWithValue("Password_Free_Trial_Rosy_Input", password);

    }


    public List<WebElement> isDisplayed(String fieldName,int maxWait) {
        List<WebElement> eleList = new ArrayList<>();
        try {
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            setMobileDriverTimeoutToSecond(maxWait);
            eleList = getActiveMobileDriver().findElements(By.xpath(xpath));
//            System.out.println("eleList:" +eleList);
            setMobileDriverTimeoutToSecond(10);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
        return eleList;
    }

    public void setMobileDriverTimeoutToSecond(int second) {
        getActiveMobileDriver().manage().timeouts().implicitlyWait(second, TimeUnit.SECONDS);
    }

    public void setMobileDriverTimeoutToExplicitWaitSecond(String xpath) {
        WebDriverWait wait = new WebDriverWait(getActiveMobileDriver(),30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }
    public void exampleOfFluentWithPredicate(String xpath) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(getActiveMobileDriver())
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(NoSuchElementException.class);
            wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath(xpath)));
        } catch (Exception ignore) { }
    }


    public void FluentWithPredicate(String xpath) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(getActiveMobileDriver())
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(NoSuchElementException.class);
            wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath(xpath)));
        } catch (Exception ignore) { }

    }

    public void setFieldDriverTimeoutToSecond(int second) {
        getActiveDriver().manage().timeouts().implicitlyWait(second, TimeUnit.SECONDS);
    }




    // Auto import email

    @StepExt("mobile field is create new email contacts")
    public void stepMobileFieldIsFilledWithValueAuto() {
//        String xpath = getCurrentMobilePage().getFieldAnnotations(email);
//        xpath = xpath.replaceAll(".+xpath\\=", "");
//        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        final String randomEmail = randomEmail();
        getActiveMobileDriver().getKeyboard().sendKeys(randomEmail + "\n");
    }

    @StepExt("field \"{fieldName}\" is filled with value auto")
    public void stepFieldIsFilledWithValueAuto(String fieldName) {
        String xpath = getCurrentPage().getFieldAnnotations(fieldName);
        final String randomEmail = randomEmail();
//        System.out.println(randomEmail);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        getActiveDriver().findElement(By.xpath(xpath)).sendKeys(randomEmail);
//        getActiveMobileDriver().hideKeyboard();
    }

    // Web auto create email
    @StepExt("field \"{fieldName}\" is clicked with value \"{value}\"")
    public void stepFieldIsClickedWithValue(String fieldName, String value) {
        List<WebElement> eleList = null;
        String xpathBuilt = ScriptUtils.xpathFrom("xpath_rosy_Web", fieldName, value);
        setFieldDriverTimeoutToSecond(1);
        eleList = getActiveDriver().findElements(By.xpath(xpathBuilt));
        int count = 0;
        while (eleList.size() == 0 && count < 5) {
            count++;
            scrollDown();
            eleList = getActiveDriver().findElements(By.xpath(xpathBuilt));
        }
        eleList.get(0).click();
        setFieldDriverTimeoutToSecond(60);
    }

    // Web scroll

    @StepExt("field srcoll up web")
    public void stepFieldScrollUPWeb() {
        JavascriptExecutor js = (JavascriptExecutor) getActiveDriver();
        js.executeScript("window.scrollBy(0,-1000)");
    }

    // Web scroll

    @StepExt("field srcoll down scroll web")
    public void stepFieldScrollDownWeb() {
        JavascriptExecutor js = (JavascriptExecutor) getActiveDriver();
        js.executeScript("window.scrollBy(0,300)");
    }

    @StepExt("Check number smart cart in \"{fieldName}\" and save in \"{variable}\"")
    public void checkMumberSmart(String fieldName, String variable) {
        try {
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = new ArrayList<>();
            try {
                setMobileDriverTimeoutToSecond(10);
                elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
                int value = elementList.size();
                String s = String.valueOf(value);
//                System.out.println(s);
                utilSubSteps.stepSaveVariable(s, variable);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field \"{fieldName}\" is scroll up Win app IOS screen")
    public void scrollUpMenu(String fieldName) {
        String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        setMobileDriverTimeoutToSecond(2);
        List<MobileElement> elements = new ArrayList<>();
        int count = 0;
        while (elements.size() == 0 && count < 5) {
            setMobileDriverTimeoutToSecond(5);
            elements = getActiveMobileDriver().findElements(By.xpath(xpath));
            if(elements.size() != 0){
                break;
            }
            count++;
            Dimension size = getActiveMobileDriver().manage().window().getSize();
            //Starting y location set to 80% of the height (near bottom)
            int endy = (int) (size.height * 0.80);
            //Ending y location set to 20% of the height (near top)
            int starty = (int) (size.height * 0.20);
            //x position set to mid-screen horizontally
            int startx = (int) (size.width * 0.1);

            scroll(startx, starty, startx, endy);
        }
        elements.get(0).click();
    }



    @StepExt("mobile field \"{fieldName}\" is scroll down Win app IOS screen")
    public void scrollMenu(String fieldName) {
        String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        List<MobileElement> elements = new ArrayList<>();
        int count = 0;
        while (elements.size() == 0 && count < 5) {
            setMobileDriverTimeoutToSecond(5);
            elements = getActiveMobileDriver().findElements(By.xpath(xpath));
            if(elements.size() != 0){
                break;
            }
            count++;
            Dimension size = getActiveMobileDriver().manage().window().getSize();
            //Starting y location set to 80% of the height (near bottom)
            int starty = (int) (size.height * 0.74);
            //Ending y location set to 20% of the height (near top)
            int endy = (int) (size.height * 0.4);
            //x position set to mid-screen horizontally
            int startx = (int) (size.width * 0.1);
            scroll(startx, starty, startx, endy);
        }
        elements.get(0).click();
    }

    @StepExt("field \"{fieldName}\" check count \"{option}\"  and save in \"{variable}\"")
    public void mobileFieldcheckcountDay(String fieldName,String option, String variable) {
        ExtendedWebElement fieldNameList = getCurrentMobilePage().getFieldSafe(fieldName);
        activateElementBorderIfNeeded(fieldNameList);
        String xpath = getCurrentMobilePage().getFieldAnnotations(option);
        xpath = xpath.replaceAll(".+xpath\\=", "");
        xpath = xpath.replaceAll("(\\,\\s+.+)", "");
        try {
//            WebElement element = getActiveDriver().findElement(By.xpath(fieldName));
            try {
                List<WebElement> elementList = fieldNameList.findElements(By.xpath(xpath));
                int value = elementList.size();
                String s = String.valueOf(value);
//                System.out.println("item: " + s);
                utilSubSteps.stepSaveVariable(s, variable);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field \"{fieldName}\" number story is saved in variable \"{variable}\"")
    public void stepMobileFieldValueStoryIsSavedInVariable(String fieldName, String variable) {
        try {
//            getActiveMobileDriver().getPageSource();
            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
            xpath = xpath.replaceAll(".+xpath\\=", "");
            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
            List<WebElement> elementList = new ArrayList<>();
            try {

                setMobileDriverTimeoutToSecond(10);
                elementList = getActiveMobileDriver().findElements(By.xpath(xpath));
                int number = elementList.size();
                WebElement myEl = getActiveMobileDriver().findElement(By.xpath("(" + xpath + "[" + number + "])//android.widget.TextView"));
                String value = myEl.getText();
                if ((value == null) || (value.isEmpty())) {
                    value = myEl.getAttribute("value");
                }
                if ((value == null) || (value.isEmpty())) {
                    value = "0";
                }
                utilSubSteps.stepSaveVariable(value, variable);
            } catch (Exception ex) {
                throw new WebDriverException();
            }
        } catch (Exception ex) {
            throw new WebDriverException();
        }
    }

    @StepExt("mobile field is Sliding the drawer down in Date Screen on Android")
    public void stepMobileFieldClickdrawerdownScreeen () {
        Dimension size = getActiveMobileDriver().manage().window().getSize();
        int startAnchor = (size.width / 2);
        int endAnchor = (size.height / 2);
        int startPoint = (int) (size.height * 0.2);
        int endPoint = (int) (size.height * 0.4);
        TouchAction touchAction = new TouchAction(getActiveMobileDriver());
        touchAction.press(PointOption.point(startPoint,startAnchor))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                .moveTo(PointOption.point(endPoint, endAnchor))
                .release().perform();
    }

    @StepExt("mobile field is auto create email")
    public void stepMobileFieldAutoCreateEmail () {
        String email = randomEmail1();
        getActiveMobileDriver().getKeyboard().sendKeys(email + "\n");
        try {
            FileWriter myWriter = new FileWriter("CreateAutoEmail.txt",true);
            BufferedWriter bw = new BufferedWriter(myWriter);
            bw.write(email + "\n");
            bw.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @StepExt("mobile field is auto create email syncing")
    public void stepMobileFieldAutoCreateEmailSync () {
        String email = randomSyncEmail();
        getActiveMobileDriver().getKeyboard().sendKeys(email + "\n");
        try {
            FileWriter myWriter = new FileWriter("CreateAutoEmailSync.txt",true);
            BufferedWriter bw = new BufferedWriter(myWriter);
            bw.write(email + "\n");
            bw.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

//    public static String RemoveFirstFIle() throws IOException {
//        String newString = "The New String";
//        File myFile = new File("PathToFile");
//        // An array to store each line in the file
//        ArrayList<String> fileContent = new ArrayList<String>();
//        Scanner myReader = new Scanner(myFile);
//        while (myReader.hasNextLine()) {
//            // Reads the file content into an array
//            fileContent.add(myReader.nextLine());
//        }
//        myReader.close();
//        // Removes Original Line
//        fileContent.remove(0);
//        // Enters New Line
//        fileContent.add(0, newString);
//        // Writes the new content to file
//        FileWriter myWriter = new FileWriter("PathToFile");
//        for (String eachLine : fileContent) {
//            myWriter.write(eachLine + "\n");
//        }
//        myWriter.close();
//        return text;
//    }


    public static String ChooseFirstFIle() throws IOException{
//        BufferedReader br = new BufferedReader(new FileReader("C:\\testing.txt"));
        BufferedReader brTest = new BufferedReader(new FileReader("CreateAutoEmail.txt"));
        String text = brTest .readLine();
        System.out.println("Firstline is : " + text);
        brTest.close();
        return text;
    }



    //Random email

    public static String randomEmail() {
        return "random-" + RandomStringUtils.randomAlphanumeric(5).toLowerCase()  + "@nx.com";
    }

    public static String randomEmail1() {

        return "auto-" + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "@nx.com";
    }

    public static String randomSyncEmail() {

        return "sync-" + RandomStringUtils.randomAlphanumeric(5).toLowerCase() + "@nx.com";
    }



}
