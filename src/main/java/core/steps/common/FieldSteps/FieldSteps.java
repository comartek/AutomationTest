package core.steps.common.FieldSteps;


import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.enums.KeyEnum;
import core.steps.BaseSteps;
import core.utils.evaluationManager.EvaluationManager;
import core.utils.evaluationManager.ScriptUtils;
import core.utils.report.ReportUtils;
import io.appium.java_client.android.AndroidDriver;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.util.List;

import static core.utils.evaluationManager.EvaluationManager.evalVariable;
import static core.utils.evaluationManager.EvaluationManager.evaluateExampleTable;


/**
 * Created by Akarpenko on 19.10.2017.
 */
public class FieldSteps extends BaseSteps {

    FieldSubSteps fieldSubSteps = new FieldSubSteps();


    //Thực hiện hành động Click
    @When("field \"$fieldName\" is clicked")
    public void whenFieldIsClicked(String fieldName) {
        fieldSubSteps.stepFieldIsClicked(fieldName);
    }

    @When("field \"$fieldName\" is not visible we reload the page")
    public void whenFieldIsReloadPage(String fieldName) {
        fieldSubSteps.stepFieldIsReloadPage(fieldName);
    }


    //Thực hiện hành động Click
    @When("field is clicked Back button in webdriver")
    public void whenFieldIsClickedBackButton() {
        fieldSubSteps.stepFieldIsClickedBackButtonWebdriver();
    }

    @When("field \"$fieldName\" is clicked the first \"$location\" on Google Map")
    public void whenFieClickFirstLocationGoogleMap(String fieldName, String location) {
        fieldSubSteps.stepFieClickFirstLocationGoogleMap(fieldName,location);
    }

    // Input giá trị vào textbox chứa elements
    @When("field \"$fieldName\" is filled iframe \"$iframe\" with vaule \"$vaule\"")
    public void whenFieldIsClickediFrame(String fieldName,String iframe, String vaule) {
        fieldSubSteps.stepFieldIsFilledWithiFrame(fieldName,iframe,vaule);

    }

    // Input giá trị vào textbox chứa elements
    @When("field \"$fieldName\" is clicked with iframe \"$iframe\"")
    public void whenFieldIsClickediFrame(String fieldName,String iframe) {
        fieldSubSteps.stepFieldIsClickiFrame(fieldName,iframe);

    }

    //Thực hiện hành động Click
    @When("field is add \"$fieldName\" to cart")
    public void whenFieldIsAddProduct(String fieldName) {
        fieldSubSteps.stepFieldIsAddProduct(fieldName);
    }

    @When("field scroll \"$text\" web screen")
    public void thenFieldscrollwithelement(String fieldName) {
        fieldSubSteps.stepFieldscrollDownWeb(fieldName);
    }

    @When("field scroll \"$fieldName\" category item")
    public void thenFieldscrollFindCategoryItem(String fieldName) {
        fieldSubSteps.stepFieldscrollFindCategoryItem(fieldName);
    }

    @When("field is swipe from \"$fieldName\" to left on category item")
    public void thenFieldscrollFromRightToLeftItem(String fieldName) {
        fieldSubSteps.stepFieldSwipeFromRightToLeftByWeb(fieldName);
    }

    @When("field is swipe from \"$fieldName\" to right on category item")
    public void thenFieldscrollFromLeftToRightItem(String fieldName) {
        fieldSubSteps.stepFieldSwipeFromLeftToRightByWeb(fieldName);
    }

    @When("field \"$fieldName\" is clicked if appeared")
    public void whenFieldIsClickedIfAppeared(String fieldName) {
        List<WebElement> tempList = fieldSubSteps.isDisplayed(fieldName, 10);
        if(tempList.size() != 0){
            fieldSubSteps.stepFieldIsClicked(fieldName);
        }

    }
    //Thực hiện hành động Click 2 lần
    @When("field \"$fieldName\" is double clicked")
    public void whenFieldIsDoubleClicked(String fieldName) {
        fieldSubSteps.stepFieldIsDoubleClicked(fieldName);
    }

    //Thực hiện hành động Click khi chờ
    @When("field \"$fieldName\" is clicked without wait")
    public void whenFieldIsClickedWithoutWait(String fieldName) {
        fieldSubSteps.stepFieldIsClickedWithoutWait(fieldName);
    }

    @When("field \"$fieldName\" is clicked (by JavaScript)")
    public void whenFieldIsClickedByJS(String fieldName) {
        fieldSubSteps.stepFieldIsClickedByJS(fieldName);
    }

    @When("click field \"$fieldName\" and accept an alert (if exists)")
    public void whenClickAndAcceptAlert(String fieldName) {
        fieldSubSteps.stepClickAndAcceptAlert(fieldName);
    }

    @When("click field \"$fieldName\" and switch to print window")
    public void whenClickAndSwitchToPrintWindow(String fieldName) {
        fieldSubSteps.stepClickAndSwitchToPrintWindow(fieldName);
    }

    //ToDo DELETE this step and replace its usage with CONDITIONAL steps
    ////Thực hiện hành động Click khi element hiển thị
    @When("field \"$fieldName\" (if exists) is clicked")
    public void whenIfExistsFieldIsClicked(String fieldName) {

        fieldSubSteps.stepIfExistsFieldIsClicked(fieldName);

    }

    //Thực hiện hành động scroll màn hình và click element
    @When("field \"$fieldName\" is clicked without scrolling into view")
    public void whenFieldIsClickedWithoutSrollingIntoView(String fieldName) {
        fieldSubSteps.stepFieldIsClickedWithoutSrollingIntoView(fieldName);
    }

    @When("field \"$fieldName\" is without scrolling into view")
    public void whenFieldIsWithoutSrollingIntoView(String fieldName) {
        fieldSubSteps.stepFieldIsWithoutSrollingIntoView(fieldName);
    }

    // Input giá trị vào textbox chứa elements
    @When("field \"$fieldName\" is filled with value \"$value\"")
    public void whenFieldIsFilledWithValue(String fieldName, String value) {

        String value_ev = evalVariable(value);
        fieldSubSteps.stepFieldIsFilledWithValue(fieldName, value_ev);

    }
    // Input giá trị vào textbox chứa elements vaf enter
    @When("field \"$fieldName\" is filled with value \"$value\" then enter")
    public void whenFieldIsFilledWithValueThenEnter(String fieldName, String value) {

        String value_ev = evalVariable(value);
        fieldSubSteps.stepFieldIsFilledWithValueThenEnter(fieldName, value_ev);

    }

    @Then("field \"$fieldName\" is not contains value \"$value\"")
    public void whenFieldIsNotContainsValue(String fieldName, String value) {

        String value_ev = evalVariable(value);
        fieldSubSteps.stepFieldIsNotContainsValue(fieldName, value_ev);

    }

    @When("field \"$fieldName\" (if enabled) is filled with value \"$value\"")
    public void whenFieldIsFilledWithValueIfEnabled(String fieldName, String value) {

        String value_ev = evalVariable(value);
        fieldSubSteps.stepFieldIsFilledWithValueIfEnabled(fieldName, value_ev);

    }

    @Then("field \"$fieldName\" value \"$operation\" \"$expectedValue\"")
    public void thenFieldValueOperation(String fieldName, String operation, String expectedValue) {

        String expectedValue_ev = ((String) evalVariable(expectedValue)).trim();
        String operation_ev = ((String) evalVariable(operation)).trim();
        fieldSubSteps.stepFieldValueOperation(fieldName, operation_ev, expectedValue_ev);
    }


    //Kiểm tra elements giống nhau hay ko
    @Then("field \"$fieldName\" value equals  \"$expectedValue\"")
    public void thenFieldValueEquals(String fieldName, String expectedValue) {

        String expectedValue_ev = ((String) evalVariable(expectedValue)).trim();
        fieldSubSteps.stepFieldValueEquals(fieldName, expectedValue_ev);

    }

    // Kiểm tra elements giống nhau thì thực hiện hành động scroll
    @Then("field \"$fieldName\" value equals  \"$expectedValue\" without scroll")
    public void thenFieldValueEqualsWithoutScroll(String fieldName, String expectedValue) {

        String expectedValue_ev = ((String) evalVariable(expectedValue)).trim();
        fieldSubSteps.stepFieldValueEqualsWithoutScroll(fieldName, expectedValue_ev);

    }
    //Kiểm tra giá trị trả về khác với elements

    @Then("field \"$fieldName\" value is not equal to \"$expectedValue\"")
    public void thenFieldValueIsNotEqual(String fieldName, String expectedValue) {

        String expectedValue_ev = ((String) evalVariable(expectedValue)).trim();
        fieldSubSteps.stepFieldValueIsNotEqual(fieldName, expectedValue_ev);

    }

    @Then("field \"$fieldName\" contains value  \"$expectedValue\"")
    public void thenFieldContainsValue(String fieldName, String expectedValue) {

        String expectedValue_ev = ((String) evalVariable(expectedValue)).trim();
        fieldSubSteps.stepFieldContainsValue(fieldName, expectedValue_ev);

    }

    @Then("field \"$fieldName\" contains value  \"$expectedValue\" (max wait time = \"$maxWaitTime\" seconds)")
    public void thenFieldContainsValueWithWait (String fieldName, String expectedValue, String maxWaitTime) {

        String expectedValue_ev = ((String) evalVariable(expectedValue)).trim();
        String maxWaitTime_ev = evalVariable(maxWaitTime);
        Long maxWaitTime_lng = Long.valueOf(maxWaitTime_ev);
        fieldSubSteps.thenFieldContainsValueWithWait (fieldName, expectedValue_ev, maxWaitTime_lng);

    }

    // Lưu giá trị của element thành 1 biến
    @When("field \"$fieldName\" value is saved in variable \"$variable\"")
    public void whenFieldValueIsSavedInVariable(String fieldName, String variable) {
        fieldSubSteps.stepFieldValueIsSavedInVariable(fieldName, variable);
    }

    // Lưu giá trị của element thành 1 biến
    @When("field \"$fieldName\" value is saved TextArea in variable \"$variable\"")
    public void whenFieldValueIsSavedTextAreaInVariable(String fieldName, String variable) {
        fieldSubSteps.stepFieldValueIsSavedTextAreaInVariable(fieldName, variable);
    }

    // Lưu giá trị của element thành 1 biến
    @When("price of \"$fieldName\" is saved in variable \"$variable\"")
    public void whenFieldPriceIsSavedInVariable(String fieldName, String variable) {
        fieldSubSteps.stepFieldIsPriceProduct(fieldName,variable);
    }

    @When("field \"$fieldName\" value is saved in variable \"$variable\" blocked")
    public void whenFieldValueIsSavedInVariableBlocked(String fieldName, String variable) {
        fieldSubSteps.stepFieldValueIsSavedInVariableBlocked(fieldName, variable);
    }

    @When("field \"$fieldName\" value (if exists) is saved in variable \"$variable\"")
    public void whenFieldValueIfExistsIsSavedInVariable(String fieldName, String variable) {
        fieldSubSteps.stepFieldValueIfExistsIsSavedInVariable(fieldName, variable);
    }

    @When("attribute \"$attributeName\" of field \"$fieldName\" value is saved in variable \"$variable\"")
    public void whenFieldAttributeValueIsSavedInVariable(String attributeName, String fieldName, String variable) {
        fieldSubSteps.stepAttributeOfFieldValueIsSavedInVariable(attributeName, fieldName, variable);
    }

    @Then("attribute \"$attributeName\" value of field \"$fieldName\" equals \"$expectedValue\"")
    public void thenAttributeValueOfFieldEquals(String attributeName, String fieldName, String expectedValue) {
        String expectedValue_ev = ((String) evalVariable(expectedValue)).trim();
        fieldSubSteps.stepAttributeValueOfFieldEquals(attributeName, fieldName, expectedValue_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("field \"$fieldName\" value is saved to datapool in variable \"$variable\"")
    public void whenSaveFieldValueToPool(String fieldName, String variable){
        fieldSubSteps.stepSaveFieldValueToPool(fieldName, variable);
    }

    //Kiểm tra elements  hiển thị
    @Then("field \"$fieldName\" exists")
    public void thenFieldExists(String fieldName) {
        List<WebElement> tempList = fieldSubSteps.isDisplayed(fieldName, 60);
        fieldSubSteps.stepFieldExists(fieldName);
    }


    //Kiểm tra elements  hiển thị
    @Then("field \"$fieldName\" exists if appeared")
    public void thenFieldExistsIfDisplay(String fieldName) {
        List<WebElement> tempList = fieldSubSteps.isDisplayed(fieldName, 120);
        if(tempList.size() != 0) {
            fieldSubSteps.stepFieldExists(fieldName);
        }
    }

    @Then("field \"$fieldName\" exists (max wait time = \"$maxWaitTime\" seconds)")
    public void thenFieldExistsWithWait(String fieldName, String maxWaitTime) {
        String maxWaitTime_ev = evalVariable(maxWaitTime);
        Long maxWaitTime_lng = Long.valueOf(maxWaitTime_ev);
        fieldSubSteps.stepFieldExistsWithWait(fieldName, maxWaitTime_lng);
    }
    //Kiểm tra element ko hiển thị

    @Then("field \"$fieldName\" does not exist")
    public void thenFieldDoesNotExist(String fieldName) {
        fieldSubSteps.stepFieldDoesNotExist(fieldName);
    }

    //attention!!! during moveToElement popup menu can cover some elements
    @When("move cursor to field \"$fieldName\"")
    public void whenMoveCursorToField(String fieldName) {
        fieldSubSteps.stepMoveCursorToField(fieldName);
    }


    // chọn giá trị trong dropdown
    @When("select item from dropdown \"$fieldName\" by value \"$value\"")
    public void whenSelectFromDropdownByValue(String fieldName, String value) {

        String value_ev = evalVariable(value);

        fieldSubSteps.stepFieldIsFilledWithValue(fieldName, value_ev);
    }

    @When("select item from dropdown \"$fieldName\" by partial value \"$value\"")
    public void whenSelectFromDropdownByPartialValue(String fieldName, String value) {

        String value_ev = evalVariable(value);

        fieldSubSteps.stepSelectFromDropdownByPartialValue(fieldName, value_ev);
    }

    @When("select item from dropdown \"$fieldName\" by exact value \"$value\"")
    public void whenSelectFromDropdownByExactValue(String fieldName, String value) {

        String value_ev = evalVariable(value);

        fieldSubSteps.stepSelectFromDropdownByExactValue(fieldName, value_ev);
    }

    @Then("item \"$item\" in dropdown \"$fieldName\" exist")
    public void thenItemFromDropdownExist(String item, String fieldName) {
        String item_ev = EvaluationManager.evalVariable(item);
        fieldSubSteps.stepItemFromDropdownExist(item_ev, fieldName);
    }

    @Then("item with partial value \"$value\" in dropdown \"$fieldName\" exist")
    public void thenItemWithPartialValueFromDropdownExist(String item, String fieldName) {
        String item_ev = EvaluationManager.evalVariable(item);
        fieldSubSteps.stepItemWithPartialValueFromDropdownExist(item_ev, fieldName);
    }

    @Then("item in dropdown \"$fieldName\" with regex value exist: $parameters")
    public void thenItemFromDropdownWithRegexValueExist(String fieldName, ExamplesTable parameters) {
        ReportUtils.attachExampletable(parameters);
        ExamplesTable params_ev = evaluateExampleTable(parameters);

        fieldSubSteps.stepItemFromDropdownWithRegexValueExist(fieldName, params_ev);
    }

    @Then("item \"$item\" in dropdown \"$fieldName\" does not exist")
    public void whenItemFromDropdownDoesNotExist(String item, String fieldName) {
        String item_ev = EvaluationManager.evalVariable(item);
        fieldSubSteps.stepItemFromDropdownDoesNotExist(item_ev, fieldName);
    }

    @Then("item in dropdown \"$fieldName\" with regex value does not exist: $parameters")
    public void thenItemFromDropdownWithRegexValueDoesNotExist(String fieldName, ExamplesTable parameters) {
        ReportUtils.attachExampletable(parameters);
        ExamplesTable params_ev = evaluateExampleTable(parameters);

        fieldSubSteps.stepItemFromDropdownWithRegexValueDoesNotExist(fieldName, params_ev);
    }

    @When("first unselected item from dropdown field \"$fieldName\" is saved to variable \"$variable\"")
    public void whenFirstUnselectedItemFromDropdownIsSaved(String fieldName, String variable) {

        fieldSubSteps.stepFirstUnselectedItemFromDropdownIsSaved(fieldName, variable);

    }

    @When("click item \"$itemPath\" in treeview field \"$fieldName\"")
    public void whenClickItemInTreeviewField(String itemPath, String fieldName) {

        fieldSubSteps.stepClickItemInTreeviewField(itemPath, fieldName);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("field \"$fieldName\" is saved as nearest business day date in variable \"$variable\"")
    public void whenSaveDateFromFieldAsBusinessDayInVariable(String fieldName, String variable) {

        String value_ev = evalVariable(fieldName);
        fieldSubSteps.stepSaveDateFromFieldAsBusinessDayInVariable(value_ev, variable);

    }

    @When("the key \"$keyName\" is pressed")
    public void whenTheKeyIsPressed(String keyName) {
        KeyEnum keyEnum = KeyEnum.valueOf(keyName.toUpperCase());
        fieldSubSteps.stepPressKey(keyEnum);
    }

    @When("press ENTER on the field \"$fieldName\"")
    public void whenTheKeyIsPressedVar2(String fieldName) {
        //  KeyEnum keyEnum = KeyEnum.valueOf(keyName.toUpperCase());
        fieldSubSteps.stepPressKeyVar2(fieldName);
    }


    @When("press key \"$key\" on the field \"$fieldName\"")
    public void whenTheKeyIsPressedOnTheField(String key, String fieldName) {
        fieldSubSteps.stepPressKeyOnTheField(key, fieldName);
    }

    @When("field \"$fieldName\" value (or \"$defaultValue\" if empty) is saved in variable \"$variable\"")
    public void whenFieldValueIsSavedInVariableIfEmptyDefault(String fieldName, String defaultValue, String variable) {
        fieldSubSteps.stepFieldValueIsSavedInVariableIfEmptyDefault(fieldName, variable, defaultValue);
    }

    @When("delete all files with name matching regex \"$fileNameRegex\" in default downloads directory")
    public void whenDeleteAllFilesWithNameMatchingRegexInDefaultDirectory(String fileNameRegex) {
        fieldSubSteps.stepDeleteAllFilesWithNameMatchingRegexInDefaultDownloadsDirectory(fileNameRegex);
    }



    @When("field \"$fieldName\" is submitted")
    public void whenFieldIsSubmitted(String fieldName) {
        fieldSubSteps.stepFieldIsSubmitted(fieldName);
    }

    @When("remove readonly attribute for field \"$fieldName\"")
    public void whenRemoveReadOnlyAttributeForField(String fieldName) {
//        fieldSubSteps.stepRemoveReadOnlyAttributeForField(fieldName);
    }

    @When("wait max \"$maxWaitTimeInSeconds\" seconds for invisibility field \"$fieldName\"")
    public void whenWaitForFieldIsInvisibility(int maxWaitTimeInSeconds, String fieldName) {
        fieldSubSteps.stepWaitForFieldIsInvisibility(maxWaitTimeInSeconds, fieldName);
    }


    @When("mobile field \"$fieldName\" is clicked if appeared")
    public void whenMobileFieldIsClickedIfAppeared(String fieldName) {
        List<WebElement> tempList = fieldSubSteps.isDisplayed(fieldName, 10);
        if(tempList.size() != 0){
//            String xpath = getCurrentMobilePage().getFieldAnnotations(fieldName);
//            xpath = xpath.replaceAll(".+xpath\\=", "");
//            xpath = xpath.replaceAll("(\\,\\s+.+)", "");
//            getActiveMobileDriver().findElement(By.xpath(xpath)).click();
            fieldSubSteps.stepMobileFieldIsClicked(fieldName);
        }
    }

//    Choose env
    @When("\"$env\" is opened android")
    public void chooseEnvAndroid(String application) {
        fieldSubSteps.chooseEnvAndroid(application);
    }


    @When("\"$env\" is opened iOS")
    public void chooseEnviOS(String application) {
        fieldSubSteps.chooseEnviOS(application);
    }

    // --------------- Scroll --------------------------

    @When("mobile field is scroll up when loading screen iOS")
    public void mobileScrollUpScreenIOS() {

        fieldSubSteps.mobileScrollUpScreenIOS();
    }

    @When("mobile field \"$fieldName\" is scroll up iOS")
    public void whenMobileFieldScrollUp(String fieldName) {

        fieldSubSteps.mobileScrollUpElementIOS(fieldName);
    }

    @When("mobile field \"$fieldName\" is swipe left iOS")
    public void whenMobileFieldScrollLeft(String fieldName) {

        fieldSubSteps.mobileScrollLeftElementIOS(fieldName);
    }

//  Clear key

    @When("mobile field \"$fieldName\" is clear an element's value")
    public void whenMobileFieldClearValue(String fieldName) {

        fieldSubSteps.mobileScrollClearValue(fieldName);
    }


    @When("tap in coordinates  \"$x\" save in \"$y\"")
    public void whenswithchToTap(int x, int y){
        fieldSubSteps.swithchTo(x,y);
    }

    @When("mobile field \"$fieldName\" is clicked")
    public void whenMobileFieldIsClicked(String fieldName) {
        fieldSubSteps.stepMobileFieldIsClicked(fieldName);
    }

    @When("mobile field open Notifications")
    public void whenMobileFieldIsOpenNotifications() {
        fieldSubSteps.stepMobileFieldIsOpenNotifications();
    }

    @When("mobile field \"$fieldName\" is scroll down Win app IOS screen")
    public void whenMobileFieldIsClickedMenu(String fieldName) {
        fieldSubSteps.scrollMenu(fieldName);
    }

    @When("mobile field \"$fieldName\" is scroll up Win app IOS screen")
    public void whenMobileFieldIsScrollUpMenu(String fieldName) {
        fieldSubSteps.scrollUpMenu(fieldName);
    }


    @When("mobile field is clicked \"$item\" of \"$product\" product")
    public void whenMobileFieldIsClickedToElement(String fieldName,String product) {
        fieldSubSteps.stepMobileFieldIsClickedToElement(fieldName,product);
    }


    @When("mobile field \"$fieldName\" is double clicked")
    public void whenMobileFieldIsDoubleClicked(String fieldName) {
        fieldSubSteps.stepMobileFieldIsDoubleClicked(fieldName);
    }

    @When("mobile field is clicked Back button of devices Android")
    public void whenMobileFieldIsClickedBackButton() {
        fieldSubSteps.stepMobileFieldIsClickedBackOfDevice();
    }

    @When("mobile field \"$fieldName\" is CLEAR field")
    public void whenMobileFieldIsClickedClearButton(String fieldName) {
        fieldSubSteps.stepMobileFieldIsClickedClearOfDevice(fieldName);
    }

    @When("mobile pass field \"$fieldName\" is clicked and fill with backspace value")
    public void whenMobilePassFieldFilledIsClickedAndFillWithBackspaceValue(String fieldName) {
        fieldSubSteps.stepMobilePassFieldFilledIsClickedAndFillWithBackspaceValue(fieldName);
    }

    @When("mobile field \"$fieldName\" is displayed without scroll")
    public void whenMobileFieldIsDisplayedWithoutScroll(String fieldName) {
        fieldSubSteps.stepMobileFieldIsDisplayedWithoutScroll(fieldName);
    }

    @When("mobile field \"$fieldName\" is filled with value \"$value\"")
    public void whenMobileFieldIsFilledWithValue(String fieldName, String value) {

        String value_ev = evalVariable(value);
        fieldSubSteps.stepMobileFieldIsFilledWithValue(fieldName, value_ev);

    }
    @When("mobile field check hideKeyboard")
    public void whenMobileFieldIsFilledWithValue() {

        fieldSubSteps.stepMobileFieldIshideKeyboard();

    }

    @When("mobile field Delete All Cookies Web only")
    public void whenMobileFieldIsFilledDeleteAllCookies() {

        fieldSubSteps.stepMobileFielddeleteAllCookies();

    }

    @Then("mobile field \"$fieldName\" have length \"$variable\"")
    public void thenMobileFieldHaveLength(String fieldName, String variable) {
        fieldSubSteps.stepMobileFieldHaveLength(fieldName, variable);
    }

    @When("mobile field \"$fieldName\" is filled with value \"$value\" then enter")
    public void whenMobileFieldIsFilledWithValueEnter(String fieldName, String value) {

        String value_ev = evalVariable(value);
        fieldSubSteps.stepMobileFieldIsFilledWithValueEnter(fieldName, value_ev);

    }

    @Then("mobile field \"$fieldName\" value equals \"$expectedValue\"")
    public void thenMobileFieldValueEquals(String fieldName, String expectedValue) {

        String expectedValue_ev = ((String) evalVariable(expectedValue)).trim();
        fieldSubSteps.stepMobileFieldValueEquals(fieldName, expectedValue_ev);

    }

    @Then("mobile field \"$fieldName\" value contains \"$expectedValue\"")
    public void thenMobileFieldValueContains(String fieldName, String expectedValue) {

        String expectedValue_ev = ((String) evalVariable(expectedValue)).trim();
        fieldSubSteps.stepMobileFieldValueContains(fieldName, expectedValue_ev);

    }

    @When("mobile field \"$fieldName\" value is saved in variable \"$variable\"")
    public void whenMobileFieldValueIsSavedInVariable(String fieldName, String variable) {
        fieldSubSteps.stepMobileFieldValueIsSavedInVariable(fieldName, variable);
    }

    @When("mobile field \"$fieldName\" value is saved OTP in variable \"$variable\"")
    public void whenMobileFieldValueIsSavedOTPInVariable(String fieldName, String variable) {
        fieldSubSteps.stepMobileFieldValueIsSavedOTPInVariable(fieldName, variable);
    }

    @When("mobile field \"$fieldName\" value is saved OTP in variable \"$variable\" for iPhone")
    public void whenMobileFieldValueIsSavedOTPInVariableiPhone(String fieldName, String variable) {
        fieldSubSteps.stepMobileFieldValueIsSavedOTPInVariableiPhone(fieldName, variable);
    }

    @When("mobile field \"$fieldName\" value is saved first items on list items in the variable \"$variable\"")
    public void whenMobileFieldValueIsSavedInVariableFirstItem(String fieldName, String variable) {
        fieldSubSteps.stepMobileFieldValueIsSavedInVariableFirtItems(fieldName, variable);
    }

    @When("mobile field \"$fieldName\" value is saved all items on list items in the variable \"$variable\"")
    public void whenMobileFieldValueIsSavedInVariableAllItem(String fieldName, String variable) {
        fieldSubSteps.stepMobileFieldValueIsSavedInVariableAllItems(fieldName, variable);
    }

    @When("mobile field \"$fieldName\" value is saved last items on list items in the variable \"$variable\"")
    public void whenMobileFieldValueIsSavedInVariableLastItem(String fieldName, String variable) {
        fieldSubSteps.stepMobileFieldValueIsSavedInVariableLastitems(fieldName, variable);
    }

    @When("mobile field \"$fieldName\" is click last items on list items")
    public void whenMobileFieldClickLastItem(String fieldName) {
        fieldSubSteps.stepMobileFieldClickLastitems(fieldName);
    }

    @When("mobile field \"$fieldName\" value contains \"$text\" in list items is saved the variable \"$variable\"")
    public void whenMobileFieldCli(String fieldName,String text, String variable) {
        fieldSubSteps.stepMobileFieldClickItemsSaveVariable(fieldName,text, variable);
    }

    @When("mobile field \"$fieldName\" is System out println")
    public void whensysoutprl(String fieldName) {
        fieldSubSteps.stepMobileFieldsysout(fieldName);
    }

    @Then("mobile field \"$fieldName\" exists")
    public void thenMobileFieldExists(String fieldName) {
        fieldSubSteps.stepMobileFieldExists(fieldName);
    }

    @Then("mobile field \"$fieldName\" exists when waiting for \"$time\" seconds")
    public void thenMobileFieldExistsWait(String fieldName,int time) {
        fieldSubSteps.stepMobileFieldExistsWhenWaitingTime(fieldName,time);
    }

    @Then("mobile field \"$fieldName\" exists if appeared")
    public void thenMobileFieldExistsifAppeared(String fieldName) {
        List<WebElement> tempList = fieldSubSteps.isDisplayed(fieldName, 30);
        if(tempList.size() != 0){
            fieldSubSteps.stepMobileFieldExists(fieldName);
        }
    }

    @Then("mobile field \"$fieldName\" exists very fast")
    public void stepMobileFieldExistsVeryFast(String fieldName) {
        List<WebElement> tempList = fieldSubSteps.isDisplayed(fieldName, 10);
        if(tempList.size() != 0){
            fieldSubSteps.stepMobileFieldExistsVeryFast(fieldName);
        }
    }

    @Then("mobile field \"$fieldName\" not exists")
    public void thenMobileFieldNotExists(String fieldName) {
        fieldSubSteps.stepMobileFieldNotExists(fieldName);
    }

    @Then("mobile field check keyboard active with android")
    public void thenMobileFieldKeyboardActive() {
        fieldSubSteps.stepMobileFieldIsKeyboardShown();
    }

    @Then("mobile field \"$fieldName\" is displayed with value \"$value\"")
    public void whenMobileFieldIsDisplayedWithValue (String fieldName, String value){
        String evalValue = EvaluationManager.evalVariable(value);
        fieldSubSteps.stepMobileFieldIsDisplayedWithValue(fieldName, evalValue);
    }

    @When("mobile field \"$fieldName\" is clicked with value \"$value\"")
    public void whenMobileFieldIsClickedWithValue (String fieldName, String value){
        String evalValue = EvaluationManager.evalVariable(value);
        fieldSubSteps.stepMobileFieldIsClickedWithValue(fieldName, evalValue);
    }

    @When("mobile field \"$fieldName\" is scroll down")
    public void whenMobileFieldIsClickedElementWhenScrollDown (String fieldName){
        fieldSubSteps.stepMobileFieldIsClickedElementWhenScrollDown(fieldName);
    }

    @Then("hide mobile keyboard")
    public void whenHideMobileKeyboard(){
        getActiveMobileDriver().hideKeyboard();
//        getActiveMobileDriver().getKeyboard();
    }

    @When("application is logged in with user \"$user\" and \"$password\"")
    public void whenApplicationIsLoggedInWithUserAndPassword(String user, String password){
        String userValue = EvaluationManager.evalVariable(user);
        String passwordValue = EvaluationManager.evalVariable(password);
        fieldSubSteps.stepApplicationIsLoggedInWithUserAndPassword(userValue, passwordValue);
    }
    @When("log out application")
    public void whenLogOutApplication(){
        fieldSubSteps.stepLogOutApplication();
    }

    @When("enter value \"$user\" and click search")
    public void enterValueAndClickSearch(String value){
        getActiveMobileDriver().getKeyboard().sendKeys(value);
        getActiveMobileDriver().getKeyboard().pressKey(Keys.ENTER);
    }


    @When("press \"$button\" on keyboard")
    public void pressKeyboard(String button){
        String xpath = "//*[@name='" + button + "']";
        getActiveMobileDriver().findElement(By.xpath(xpath)).click();
    }



    @When("create account with first name \"$firstname\" and last name \"$lastname\"")
    public void FirstandLastName(String firstname, String lastname) {
        String firstNameValue = EvaluationManager.evalVariable(firstname);
        String lastNameValue = EvaluationManager.evalVariable(lastname);
        fieldSubSteps.firstandlastname(firstNameValue, lastNameValue);

    }

    @When("create account with email \"$email\"")
    public void CreateAccount(String email) {
        String emailValue = EvaluationManager.evalVariable(email);
        fieldSubSteps.stepCreateAccountwithEmail(emailValue);
    }

    @When("mobile password field \"$fieldName\" is filled with value \"$value\"")
    public void wheninputpassword(String fieldName, String password) {
        String passwordValue = EvaluationManager.evalVariable(password);
        fieldSubSteps.stepMobilePasswordFieldIsFilledWithValue(fieldName, passwordValue);
    }


    @When("mobile field count with \"$fieldName\" and compare  \"$variable\"")
    public void whenMobileFieldCompareAuto(String fieldName, int variable) {
        fieldSubSteps.stepMobileFieldCompareAuto(fieldName, variable);
    }


    @When("mobile field count contacts with \"$fieldName\" and compare  \"$variable\"")
    public void whenMobileFieldContactsCompareAuto(String fieldName, int variable) {
        fieldSubSteps.stepMobileFieldContactsCompareAuto(fieldName, variable);
    }



    @When("mobile field \"$fieldName\" is counted value and saved in variable \"$variable\"")
    public void CountVault(String fieldName, String variable) {
        fieldSubSteps.stepCountVault(fieldName, variable);

    }

    @When("get element size in \"$fieldName\" save in \"$variable\"")
    public void whenGetElementSize(String fieldName, String variable) {
        fieldSubSteps.stepGetElementSize(fieldName, variable);

    }

    @When("count number search contact in \"$fieldName\" save in \"$variable\"")
    public void CountSearchContact(String fieldName, String variable) {
        fieldSubSteps.stepCountSearchContact(fieldName, variable);

    }

    @When("count number search contact in sharing with \"$fieldName\" save in \"$variable\"")
    public void CountSearchContactSharing(String fieldName, String variable) {
        fieldSubSteps.stepCountSearchContactSharing(fieldName, variable);

    }

    @When("count number stores in \"$fieldName\" save in \"$variable\"")
    public void CountStores(String fieldName, String variable) {
        fieldSubSteps.stepCountStores(fieldName, variable);

    }


    @When("mobile field scroll down width screen")
    public void thenMobileFieldscrollwithelement() {
        fieldSubSteps.stepMobileFieldscrollDownAuto();
    }

    @When("mobile field scroll down width Notification screen")
    public void thenMobileFieldscrollwithNotification() {
        fieldSubSteps.stepMobileFieldscrollDownNotificationScreen();
    }

    @When("mobile field scroll up width OTP screen")
    public void thenMobileFieldscrollOTPwithelement() {
        fieldSubSteps.stepMobileFieldscrollDownAutoOTP();
    }

    @When("mobile field scroll down with Full Year of Date screen")
    public void thenMobileFieldscrollDownYearDate() {
        fieldSubSteps.stepMobileFieldscrollDownYearDate();
    }

    @When("mobile field scroll down to bottom page")
    public void thenMobileFieldscrollToBottom() {
        fieldSubSteps.stepMobileFieldscrollToBottom();
    }

//    @When("mobile field scroll down width Read Memory screen")
//    public void thenMobileFieldscrolldownReadMemoryScreen() {
//        fieldSubSteps.stepMobileFieldscrollDownReadMemoryScreen();
//    }
////
//    @When("mobile field scroll up width Read Memory screen")
//    public void thenMobileFieldscrollUpReadMemoryScreen() {
//        fieldSubSteps.stepMobileFieldscrollUpReadMemoryScreen();
//    }

    @When("mobile field is scroll to text \"$text\" for android screen")
//    @When("mobile field \"$fieldName\" when scroll up for android")
    public void thenMobileFieldscrolltoelement(String text) {
        fieldSubSteps.stepMobileFieldscrolltoElement(text);
    }

    @When("mobile field is scroll down to text \"$text\" of iOS screen")
    public void thenMobileFieldIsClickedTextElementWhenScrollDown(String text) {
        fieldSubSteps.stepMobileFieldIsClickedTextElementWhenScrollDown(text);
    }


    @When("mobile field is swipe from right to left")
    public void thenMobileFieldSwipeByPercentage() {
        fieldSubSteps.stepMobileFieldSwipeByPercentage();
    }

    @When("mobile field is swipe from left to right")
    public void thenMobileFieldSwipeFromLeftToRight() {
        fieldSubSteps.stepMobileFieldSwipeFromleftToRight();
    }

    @When("mobile field is scroll to id \"$id\" for android screen")
    public void thenMobileFieldscrolltoID(String id) {
        fieldSubSteps.stepMobileFieldscrolltoID(id);
    }

    @When("mobile field scroll swipe left width screen")
    public void thenMobileFieldswipewithelement() {
        fieldSubSteps.stepMobileFieldscrollSwipe();
    }


    @When("mobile field scroll up with screen")
    public void thenMobileFieldscrollUpwithelement() {
        fieldSubSteps.stepMobileFieldscrollUpAuto();
    }


    @When("mobile field size \"$fieldName\" and save  \"$variable\"")
    public void whenMobileFieldCompareAuto(String fieldName, String variable) {
        fieldSubSteps.stepMobileFieldSize(fieldName, variable);
    }

    @When("mobile field tablet delete item has memmories")
    public void whenMobileFieldDeleteNotStoryAuto(){
        fieldSubSteps.stepMobileFieldDeleteHasStoryAuto();
    }

    @When("mobile field tablet delete item without memmory")
    public void whenMobileFieldDeleteWithoutStoryAuto(){
        fieldSubSteps.stepMobileFieldDeleteWithoutStoryAuto();
    }


    @When("mobile field tablet input PIN \"$variable\"")
    public void whenMobileFieldForgotPin(String variable){
        fieldSubSteps.stepMobileFieldForgotPin(variable);
    }

    @When("mobile field is filled with value \"$value\" ADB")
    public void whenMobileFieldIsFilledWithValueADB(String value) {
        String value_ev = evalVariable(value);
        fieldSubSteps.stepMobileFieldIsFilledWithValueADB(value_ev);
    }

    @When("mobile field is senkeys with value \"$value\" ADB")
    public void whenMobileFieldIsSenkeysWithValueADB(String value) {
        String value_ev = evalVariable(value);
        fieldSubSteps.stepMobileFieldIsSenkeysWithValueADB(value_ev);
    }

    @When("mobile field is auto create email")
    public void whenMobileFieldIsFilledWithValueADB() {
        fieldSubSteps.stepMobileFieldAutoCreateEmail();
    }

    @When("mobile field is auto create email syncing")
    public void whenMobileFieldIsFilledWithEmailSync() {
        fieldSubSteps.stepMobileFieldAutoCreateEmailSync();
    }

    @When("mobile field is filled with space value ADB")
    public void whenMobileFieldIsFilledWithSpaceValueADB() {
        fieldSubSteps.stepMobileFieldIsFilledWithSpaceValueADB();
    }

    @When("uninstall App")
    public void  whenMobileFieldUninstall(){
        fieldSubSteps.stepMobileFieldUninstall();
    }

    @When("clear data App")
    public void  whenMobileFieldClearData(){
        fieldSubSteps.stepMobileFieldClearData();
    }

    // stop appium server

    @When("stop appium server")
    public void  whenMobileFieldStopAppium(){
        fieldSubSteps.stepMobileFieldStopAppium();
    }

    @When("mobile field play memories with list \"$fieldName\"")
    public void whenMobileFieldplayMemories(String fieldName){
        fieldSubSteps.stepMobileFieldplayMemories(fieldName);
    }

    @When("width in \"$fieldName\" save in \"$variable\"")
    public void whenWidthVault(String fieldName, String variable){
        fieldSubSteps.stepWidthVault(fieldName,variable);
    }

    @When("select webview")
    public void whenselectwebview(){
        fieldSubSteps.selectwebview();
    }

    @When("mobile tap coordinates \"$x\" and \"$y\"")
    public void whenswithchTo(int x, int y){
        fieldSubSteps.tapMobile(x,y);
    }

    @When("mobile field \"$fieldName\" is tap")
    public void whenMobileFieldIsTap(String fieldName) {
        fieldSubSteps.tapElement(fieldName);
    }

    @When("mobile field zoom \"$fieldName\"")
    public void thenMobileFieldZoomAuto(String fieldName) {
        fieldSubSteps.stepMobileFieldZoomAuto(fieldName);
    }

    @When("field choose memories \"$fieldName\" is click")
    public void whenMobileFieldIsclick(String fieldName) {
        fieldSubSteps.stepFieldIsClickedd(fieldName);
    }

    @When("field count \"$fieldName\" and save in \"$variable\"")
    public void whenFieldIsCount(String fieldName, String variable) {
        fieldSubSteps.stepFieldIsCount(fieldName, variable);

    }

    @When("field upload \"$fieldName\" with url \"$variable\"")
    public void whenFieldIsUpload(String fieldName, String variable) throws AWTException {
        fieldSubSteps.stepFieldIsUpload(fieldName, variable);

    }
    @When("field delete item without memmory")
    public void whenFieldDeleteWithoutStoryAuto(){
        fieldSubSteps.stepFieldDeleteWithoutStoryAuto();
    }


    @When("field delete item has memmory")
    public void whenFieldDeletHasStoryAuto(){
        fieldSubSteps.stepFieldDeletHasStoryAuto();
    }


    @When("create account with \"$fieldName\"")
    public void CreateAccountAndroid(String fieldName) {
//        String emailValue = EvaluationManager.evalVariable(email);
        fieldSubSteps.stepCreateAccountwithfieldName(fieldName);
    }

    @When("create account with \"$fieldName\" Tablet")
    public void CreateAccountTablet(String fieldName) {
//        String emailValue = EvaluationManager.evalVariable(email);
        fieldSubSteps.stepCreateAccountwithfieldNameTablet(fieldName);
    }

    @When("application is logged in with user with \"$user\" and \"$password\"")
    public void whenApplicationIsLoggedInWithUserAndPasswordAndroid(String user, String password){
        String userValue = EvaluationManager.evalVariable(user);
        String passwordValue = EvaluationManager.evalVariable(password);
        fieldSubSteps.stepApplicationIsLoggedInWithUserAndPasswordAndroid(userValue, passwordValue);
    }

    @When("application is logged in with user free trial account with \"$user\" and \"$password\"")
    public void whenApplicationIsLoggedInWithUserFreeTrialAndPasswordAndroid(String user, String password){
        String userValue = EvaluationManager.evalVariable(user);
        String passwordValue = EvaluationManager.evalVariable(password);
        fieldSubSteps.stepApplicationIsLoggedInWithUserFreeTrialAndPasswordAndroid(userValue, passwordValue);
    }



    @When("field \"$fieldName\" is clicked with value \"$value\"")
    public void whenFieldIsClickedWithValue (String fieldName, String value){
        String evalValue = EvaluationManager.evalVariable(value);
        fieldSubSteps.stepFieldIsClickedWithValue(fieldName, evalValue);
    }


    @When("mobile field \"$fieldName\" check and save in \"$variable\"")
    public void checkSmartCard(String fieldName, String variable) {
        fieldSubSteps.checkMumberSmart(fieldName, variable);
    }

    @When("field \"$fieldName\" verify count \"$option\" and save in \"$variable\"")
    public void checkcountDay(String fieldName,String option, String variable) {
        fieldSubSteps.checkcountDay(fieldName,option, variable);
    }

    @When("mobile field \"$fieldName\" verify count \"$option\" and save in \"$variable\"")
    public void mobileFieldcheckcountDay(String fieldName,String option, String variable) {
        fieldSubSteps.mobileFieldcheckcountDay(fieldName,option, variable);
    }

    @When("mobile field \"$fieldName\" number story is saved in variable \"$variable\"")
    public void whenMobileFieldValueNumberIsSavedInVariable(String fieldName, String variable) {
        fieldSubSteps.stepMobileFieldValueStoryIsSavedInVariable(fieldName, variable);
    }

    @When("mobile field scroll up with \"$fieldName\"")
    public void thenMobileFieldscrollwithelement(String fieldName) {
        fieldSubSteps.stepMobileFieldscrollUpFindElement(fieldName);
    }

    @When("mobile field is Sliding the drawer down in Date Screen on Android")
    public void thenMobileFieldClickdrawerdownScreeen() {
        fieldSubSteps.stepMobileFieldClickdrawerdownScreeen();
    }

//    @When("mobile field \"{$fieldName}\" is filled with value auto")
//    public void whenMobileFieldIsFilledWithValueAuto(String fieldName) {
//        fieldSubSteps.stepMobileFieldIsFilledWithValueAuto(fieldName);
//    }

}
