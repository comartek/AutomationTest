package core.pages;

import core.annotations.SwitchTo;
import core.annotations.SwitchToFrameChain;
import core.annotations.SwitchToPlural;
import core.applications.ApplicationManager;
import core.collections.BaseCollectionItem;
import core.htmlElements.element.ExtendedWebElement;
import core.htmlElements.element.HtmlElement;
import core.htmlElements.element.TypifiedElement;
import core.htmlElements.loader.decorator.HtmlElementDecorator;
import core.htmlElements.loader.decorator.HtmlElementLocatorFactory;
import core.htmlElements.utils.HtmlElementUtils;
import core.utils.report.ReportUtils;
import core.utils.timeout.TimeoutUtils;
import org.apache.log4j.Logger;
import org.jbehave.core.model.ExamplesTable;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static core.steps.BaseSteps.getActiveDriver;
import static core.utils.javaScript.JavaScriptUtils.isElementVisible;
import static core.utils.timeout.TimeoutUtils.*;


/**
 * BasePage is ancestor for all page and base-page classes.
 */
public abstract class BasePage {

    private Logger logger = Logger.getLogger("DEBUG_INFO");

    private WebDriver driver;

    public static long timeInMillisWastedOnVerifyPageIsLoaded = 0;
    public static long verifyPageIsLoadedCallCount = 0;


    /**
     * Getter for WebDriver.
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Setter for WebDriver.
     */
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * SelectedCollectionItem is a selected row of the table on the page.
     */
    public BaseCollectionItem SelectedCollectionItem;


    /**
     * Method of page and elements initialization .
     * @param driver webdriver to initialization page
     */
    public void initialize(WebDriver driver) {
        this.driver = driver;
        initPageClass();
        checkPageFieldsCanBeInitialized();
        PageFactory.initElements(new HtmlElementDecorator(new HtmlElementLocatorFactory(driver)), this);
        SelectedCollectionItem = null;
    }

    /**
     * Initialization of page.
     * In current version it initializes just SwitchTo - attribute to switch into frame on page in case of there is all page content within frame.
     */
    public void initPageClass(){
        SwitchTosSequence switchTosSequence = getSwitchTosSequence();
        ArrayList<SwitchTo> switchTos = switchTosSequence.switchTos;
        String finalFrameNameRegExpPattern = switchTosSequence.finalFrameNameRegExpPattern;
        if(switchTos.size() > 0){
            //Max 3 attempts to reach the expected frame (based on SwitchToPlural.finalFrameNameRegExpPattern())
            for(int i = 0; i < 3; i++){
                for (SwitchTo switchTo : switchTos) {
                    TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
                    this.waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
                    logger.debug(String.format("Before @SwitchTo active frame/window name = '%s' (page = '%s')", getActiveFrameName(), this.getClass().toString()));

                    SwitchTo.SwitchToUtils.validateInputValues(switchTo, this.getClass());
                    SwitchTo.SwitchToUtils.performSwitchTo(switchTo, this.getClass(), getActiveDriver());

                    TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
                    this.waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
                    logger.debug(String.format("After @SwitchTo active frame/window name = '%s' (page = '%s')", getActiveFrameName(), this.getClass().toString()));
                }

                //Check if multiple SwitchTo (SwitchToPlural) has led to the expected frame (frame name pattern)
                if(finalFrameNameRegExpPattern.equals("")){
                    break;
                }else{
                    String frameName = getActiveFrameName();
                    Pattern pattern = Pattern.compile(finalFrameNameRegExpPattern);
                    Matcher matcher = pattern.matcher(frameName);
                    if(matcher.find()){
                        break;
                    } else {
                        //Logging is temporary and may be deleted once SwitchToPlural is proven to work right every time
                        String msg = String.format("Active frame name '%s' does not match RegExp pattern '%s' (attempt # %d)", frameName, finalFrameNameRegExpPattern, i+1);
                        logger.error(msg);
                        ReportUtils.attachMessage("SwitchTo err", msg);
                        sleep(1);
                    }
                }
            }
        }
        else
        {
//            perform switch to default to fix transitions from pages with frames to pages without frames
            ApplicationManager.getInstance().get().ActiveApplication.driver.switchTo().defaultContent();
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            this.waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        }
    }

    private class SwitchTosSequence{
        ArrayList<SwitchTo> switchTos;
        String finalFrameNameRegExpPattern;
        SwitchTosSequence(ArrayList<SwitchTo> switchTos, String finalFrameNameRegExpPattern){
            this.switchTos = switchTos;
            this.finalFrameNameRegExpPattern = finalFrameNameRegExpPattern;
        }
    }

    private SwitchTosSequence getSwitchTosSequence(){
        ArrayList<SwitchTo> switchTos = new ArrayList<>();
        String finalFrameNameRegExpPattern = "";

        SwitchToPlural switchToPlural = this.getClass().getAnnotation(SwitchToPlural.class);
        SwitchToFrameChain switchToFrameChain = this.getClass().getAnnotation(SwitchToFrameChain.class);

        if (switchToFrameChain != null){
            switchTos.addAll(Arrays.asList(SwitchTo.SwitchToUtils.generateSwitchToFrames(switchToFrameChain)));
            finalFrameNameRegExpPattern = switchToFrameChain.finalFrameNameRegExpPattern();
        } else
        if (switchToPlural != null){
            switchTos.addAll(Arrays.asList(switchToPlural.value()));
            finalFrameNameRegExpPattern = switchToPlural.finalFrameNameRegExpPattern();
        } else {
            SwitchTo switchTo = getFirstSwitchToAnnotationOfPageClassOrItsSuperClasses(this);
//            SwitchTo switchTo = this.getClass().getAnnotation(SwitchTo.class);
            if(switchTo != null){
                switchTos.add(switchTo);
            }
        }
        return new SwitchTosSequence(switchTos, finalFrameNameRegExpPattern);
    }


    /**
     * Searching for the @SwitchTo annotation in the chosen page class or its super classes (first one found closest to the chosen page class in the hierarchy)
     * @param basePage page for which we try to find @SwitchTo annotation
     */
    private SwitchTo getFirstSwitchToAnnotationOfPageClassOrItsSuperClasses(BasePage basePage){
        Class<?> pageClass = basePage.getClass();
        while(pageClass != null){
            SwitchTo switchTo = pageClass.getAnnotation(SwitchTo.class);
            if(switchTo != null) {
                return switchTo;
            }

            pageClass = pageClass.getSuperclass();
            if (pageClass.equals(BasePage.class)){
                break;
            }
        }
        return null;
    }

    /**
     * Check if all page fields can be initialized.
     */
    public void checkPageFieldsCanBeInitialized(){
        for(Class proxyIn = this.getClass(); proxyIn != Object.class; proxyIn = proxyIn.getSuperclass()) {
            Field[] fields = proxyIn.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                Field field = fields[i];
                //boolean webElementCanNotBeInitialized = HtmlElementUtils.isWebElement(field) && (Modifier.isAbstract (field.getType().getModifiers()) || Modifier.isInterface(field.getType().getModifiers())) && field.getType() != WebElement.class ;
                boolean webElementCanNotBeInitialized = HtmlElementUtils.isWebElement(field) && (field.getType() == TypifiedElement.class || field.getType() == ExtendedWebElement.class);
                if(webElementCanNotBeInitialized)
                    throw new RuntimeException(String.format("Field '%s' (class '%s') from Page '%s' can not be initialized by PageFactory.initElements (because an exception 'java.lang.IllegalArgumentException: Can not set ... field ... to com.sun.proxy.$Proxy35' will be thrown during the initialization for the following classes: TypifiedElement, ExtendedWebElement). You can use HtmlElement class instead of currently used unallowed class", field.getName(), field.getType().toString(), this.getClass().toString()));

                //Also this framework does not allow to use FindBy for WebElement
                if(field.getType() == WebElement.class)
                    throw new RuntimeException(String.format("Field '%s' (class '%s') from Page '%s' can not be initialized by PageFactory.initElements (because initialization of element of chosen class is not allowed by THIS FRAMEWORK). You can use HtmlElement class instead of currently used unallowed class", field.getName(), field.getType().toString(), this.getClass().toString()));
            }
        }
    }

    /**
     * Check if the page is loaded. True by default. In descendants this method checks one or more key fields to be displayed and exist.
     */
    public abstract boolean isLoaded();

    /**
     * Checks if all specified elements are loaded (existsAndDisplayed). Returns true if all elements are loaded. Or throws AssertionError with a message which specifies all non-existing elements
     */
    public boolean checkElementsExistOnThePage(ExtendedWebElement... elements){
        //Timeout will only be changed after the first element is checked using the original timeout (and for 2nd+ elements timeout will be 1 second)
        long timeOutCache = TimeoutUtils.getTimeOut();
        boolean changeTimeout = false;
        ArrayList<String> nonExistingElements = new ArrayList<>();
        for(ExtendedWebElement element : elements) {
            if (changeTimeout) {
                TimeoutUtils.setTimeOut(1);
            }
            if (!element.existsAndDisplayed()) {
                nonExistingElements.add(element.getName().replace(" ", ""));
                changeTimeout = true;
            }
        }
        TimeoutUtils.setTimeOut(timeOutCache);

        if(nonExistingElements.size() == 0){
            return true;
        } else {
            Assert.fail("Page '" + this.getClass().getSimpleName() + "' has not been loaded. These elements have not been found on the page: " + Arrays.toString(nonExistingElements.toArray()));
            return false; //Unreachable code because of AssertionError!!!
        }
    }

    /**
     * Checks if all specified elements do not exist on the current page (existsAndDisplayed). Returns true if all elements do not exist. Or throws AssertionError with a message which specifies all existing elements
     */
    public boolean checkElementsDoNotExistOnThePage(ExtendedWebElement... elements){
        long timeOutCache = TimeoutUtils.getTimeOut();
        TimeoutUtils.setTimeOut(0);

        ArrayList<String> existingElements = new ArrayList<>();
        for(ExtendedWebElement element : elements){
            if(element.existsAndDisplayed()){
                existingElements.add(element.getName().replace(" ", ""));
            }
        }

        TimeoutUtils.setTimeOut(timeOutCache);

        if(existingElements.size() == 0){
            return true;
        } else {
            Assert.fail("Page '" + this.getClass().getSimpleName() + "' has not been loaded. These elements were not supposed to exist on the page (but they exist): " + Arrays.toString(existingElements.toArray()));
            return false; //Unreachable code because of AssertionError!!!
        }
    }

    /**
     * Verifies that current page is loaded (by calling its implementation of the 'isLoaded()' method).
     */
    public void verifyPageIsLoaded(){
//        verifyPageIsLoadedCallCount++;
//        long startTime = System.currentTimeMillis();
//        if(!this.isLoaded_NoWaitTimeout()){
//            timeInMillisWastedOnVerifyPageIsLoaded = timeInMillisWastedOnVerifyPageIsLoaded + System.currentTimeMillis() - startTime;
//            throw new RuntimeException("Page '" + this.getClass().getSimpleName() + "' is not loaded");
//        }
//        timeInMillisWastedOnVerifyPageIsLoaded = timeInMillisWastedOnVerifyPageIsLoaded + System.currentTimeMillis() - startTime;
    }

    public boolean isLoaded_NoWaitTimeout(){
        long previousTimeout = TimeoutUtils.getTimeOut();
        TimeoutUtils.setTimeOut(0);
        if (this.isLoaded()) {
            TimeoutUtils.setTimeOut(previousTimeout);
            return true;
        }
        TimeoutUtils.setTimeOut(previousTimeout);
        return false;
    }

    /**
     * Util method allows to light current webElement on the page with red border.
     * @param extendedWebElement current webElement
     */
    public static void activateElementBorderIfNeeded(ExtendedWebElement extendedWebElement){
        //borderElement() is called only if borderElementIsActivated = true, Object extendedWebElement is instance of ExtendedWebElement, and extendedWebElement.existsAndDisplayed() = true
        //Every exception is getting caught and therefore the method does not perform anything
        try {
            if (Boolean.valueOf(System.getProperty("borderElementIsActivated"))){
                setTimeOut(1, TimeUnit.MILLISECONDS);
                if (extendedWebElement.existsAndDisplayed()) {
                    //scroll to element (true - element will be at the top of the view, false - element will be at the bottom of the view)
                    if (Boolean.valueOf(System.getProperty("borderElementScrollIntoView"))) {
                        ((JavascriptExecutor) getActiveDriver()).executeScript("arguments[0].scrollIntoView(true);", extendedWebElement);
                    }

//                    //moveToElement
//                    Actions action = new Actions(getActiveDriver());
//                    action.moveToElement(extendedWebElement.getWrappedElement()).build().perform();

                    //if element tag name is "a" then border this element AND we try to find 'span' child tag and border this span element as well (otherwise it is not always visible if we border 'a' tag element)
                    if (extendedWebElement.getTagName().equals("a")){
                        List<WebElement> aTagElementChildren = extendedWebElement.findElements(By.xpath("./span"));
                        if(aTagElementChildren.size() > 0) {
                            HtmlElement tmpElem = new HtmlElement();
                            tmpElem.setWrappedElement(aTagElementChildren.get(0));

                            tmpElem.activateElementBorder();
                            ReportUtils.addBorderedWebElementToTheList(tmpElem);
                        }
                    }

                    extendedWebElement.activateElementBorder();
                    ReportUtils.addBorderedWebElementToTheList(extendedWebElement);
                }
            }

        } catch (Exception e){
            //e.printStackTrace();
        } finally {
            returnDefaultTimeOut();
        }
    }


    /**
     * Basic method of class. Getting field by string name.
     * At first it tries to find element in collection, if cant, then tries to find it on the page.
     * @param fieldName string name of element
     */
    public Object getField(String fieldName) {
        verifyPageIsLoaded();

        try {
            if (SelectedCollectionItem!=null)
            {
                try {
                    return (SelectedCollectionItem.getClass().getField(fieldName).get(SelectedCollectionItem));
                }
                catch (Exception e)
                {
                    //do nothing
                    //this case means the element is not found in collection and will be searched on the page
                }
            }

            try{
                return (getClass().getField(fieldName).get(this));
            } catch (NoSuchFieldException e){
                throw new NoSuchFieldException(getFieldLog(fieldName));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Basic method of class. Getting field by string name.
     * It should be used only for fields of page (not for collection items). Exception handling is implemented here.
     */
    public ExtendedWebElement getFieldSafe(String fieldName){
        verifyPageIsLoaded();

        Object object = getField(fieldName);
        if (object instanceof ExtendedWebElement)
        {
            if (((ExtendedWebElement) object).existsAndDisplayed()) {
                return (ExtendedWebElement) object;
            } else {

                Assert.fail("The field does not exist or it is not displayed" + "\n\nFiled info:" + getFieldLog(fieldName));
                return null;// на самом деле досюда не дойдет
            }
        }
        else
        {
            //вывод ошибки о неверном типе элемента
            throw new RuntimeException(String.format("Object '%s' of Class '%s' is not an instance of TypifiedElement",fieldName,object.getClass()));
        }
    }

    /**
     * Basic method of class. Getting field by string name.
     * It should be used only for fields of page (not for collection items). Exception handling is implemented here.
     *
     * This method different from getFieldSafe(). It allows to get field in case if field is blocked by loadTimeIndicator
     */
    public ExtendedWebElement getFieldBlocked(String fieldName){
        verifyPageIsLoaded();

        Object object = getField(fieldName);
        if (object instanceof ExtendedWebElement)
        {
            WebDriverWait wait = new WebDriverWait(getDriver(), 60);
            wait.until(ExpectedConditions.visibilityOf(((ExtendedWebElement)object).getWrappedElement()));

            if (((ExtendedWebElement) object).exists()) {
                return (ExtendedWebElement) object;
            } else {

                Assert.fail("The field does not exist " + "\n\nFiled info:" + getFieldLog(fieldName));
                return null;// на самом деле досюда не дойдет
            }
        }
        else
        {
            //вывод ошибки о неверном типе элемента
            throw new RuntimeException(String.format("Object '%s' of Class '%s' is not an instance of TypifiedElement",fieldName,object.getClass()));
        }
    }

    public ExtendedWebElement getFieldSafeIfExists(String fieldName){
        verifyPageIsLoaded();

        Object object = getField(fieldName);
        if (object instanceof ExtendedWebElement)
        {
            if (((ExtendedWebElement) object).existsAndDisplayed()) {
                return (ExtendedWebElement) object;
            }
            else {
//                AssertExt.assertFalse(true);
                return null; // unreachable
            }
        }
        else
        {
            throw new RuntimeException(String.format("Object '%s' of Class '%s' is not an instance of TypifiedElement",fieldName,object.getClass()));
        }
    }

    /**
     * Getting table row by index
     * @param collectionName table name
     * @param index index
     */
    public BaseCollectionItem getCollectionItem(String collectionName, int index){
        verifyPageIsLoaded();

        try {
            List<BaseCollectionItem> collection = (List<BaseCollectionItem>)  getClass().getField(collectionName).get(this);
            BaseCollectionItem item = collection.get(index);
            scrollCollectionItemIntoViewIfItIsNotVisible(item); //currently is only being used for video purposes (mainly for T24)
            return  item;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Getting table row by conditions
     * @param collectionName table name
     * @param conditions conditions as ExamplesTable
     */
    public BaseCollectionItem getCollectionItem(String collectionName, ExamplesTable conditions){
        verifyPageIsLoaded();

        try {
            List<BaseCollectionItem> list = (List<BaseCollectionItem>) getField(collectionName);
            Assert.assertTrue("Collection '" + collectionName + "' was not found in application",list.size() > 0);

            TimeoutUtils.setTimeOut(1);
            for (BaseCollectionItem item : list) {
//                activateElementBorderIfNeeded(item);

                scrollCollectionItemIntoViewIfItIsNotVisible(item); //currently is only being used for video purposes (mainly for T24)

                if (item.matchesCondition(conditions)) {
//                    item.deactivateElementBorder();
                    return item;
                }
//                item.deactivateElementBorder();
            }

            return null;

        } catch (Exception e) {
            return null;
        }
        finally
        {
            TimeoutUtils.returnDefaultTimeOut();
        }
    }

    /**
     * Count table rows that matches conditions
     * @param collectionName table name
     * @param conditions conditions as ExamplesTable
     */
    public int countCollectionItems(String collectionName, ExamplesTable conditions){
        verifyPageIsLoaded();
        int count = 0;

        try {
            List<BaseCollectionItem> list = (List<BaseCollectionItem>) getField(collectionName);
            Assert.assertTrue("Collection '" + collectionName + "' was not found in application",list.size() > 0);

            TimeoutUtils.setTimeOut(1);
            for (BaseCollectionItem item : list) {
                scrollCollectionItemIntoViewIfItIsNotVisible(item); //currently is only being used for video purposes (mainly for T24)
                if (item.matchesCondition(conditions)) {
                    count++;
                }
            }
        } catch (Exception e) {
            /*DO NOTHING*/
        } finally {
            TimeoutUtils.returnDefaultTimeOut();
        }
        return count;
    }

    public void scrollCollectionItemIntoViewIfItIsNotVisible(BaseCollectionItem item){
        //currently it is only being used for video purposes (mainly for T24), therefore we ignore every Exception
        try{
            if(!isElementVisible(item)) {
                ((JavascriptExecutor) getActiveDriver()).executeScript("arguments[0].scrollIntoView(true);", item);
            }
        } catch(Exception e){
            //ignored
        }
    }

    public void scrollElementIntoView(ExtendedWebElement item){
        //currently it is only being used for video purposes (mainly for T24), therefore we ignore every Exception
        try{
            ((JavascriptExecutor) getActiveDriver()).executeScript("arguments[0].scrollIntoView(true);", item);
        } catch(Exception e) {
            //ignored
        }
    }

    /**
     * Logging method for exception handling.
     */
    public String getFieldLog(String fieldName){
        String fieldAnnotations = getFieldAnnotations(fieldName);
        String logRelatedToAnnotations = fieldAnnotations.contains("FindBy") ? fieldAnnotations + "." : "Field does not have a 'FindBy...' annotation.";
        return "\nField name = '" + fieldName + "'; \nActive page = '" + ApplicationManager.getInstance().get().ActiveApplication.currentPage.getClass() + "'; \nActive language = '" + ApplicationManager.getInstance().get().ActiveApplication.language + "'; \n'FindBy...' annotation = " + logRelatedToAnnotations + "\n\n";
    }

    /**
     * Getting field annotation.
     */
    public String getFieldAnnotations(String fieldName){
        try{
            return Arrays.toString(getClass().getField(fieldName).getAnnotations());
        } catch (NoSuchFieldException e) {
            return "";
        }
    }

    /**
     * Getting active frame/window name.
     */
    public String getActiveFrameName(){
        JavascriptExecutor jsExecutor = (JavascriptExecutor)getActiveDriver();
        try {
            return (String) jsExecutor.executeScript("return self.name");
        }catch(JavascriptException e){
            return "";
        }
    }

    /**
     * Declaration of method for loading indicator. OCB only.
     */
    public void waitUntilLoadingIndicatorIsInvisible(int maxWaitTimeInSeconds){}

    /**
     * Declaration of method for SAP elements loading indicator. OCB only.
     */
    public void waitUntilSapBusyIndicatorIsInvisible(int maxWaitTimeInSeconds){}

    public void afterPageLoad(){

    }

    public void reloadPage(){
        SwitchTosSequence switchTosSequence = getSwitchTosSequence();
        ArrayList<SwitchTo> switchTos = switchTosSequence.switchTos;
        if(switchTos.size() > 0){
            for (SwitchTo switchTo : switchTos) {
                SwitchTo.SwitchToUtils.validateInputValues(switchTo, this.getClass());
                SwitchTo.SwitchToUtils.performSwitchTo(switchTo, this.getClass(), getActiveDriver());
            }
        } else {
            //perform switch to default to fix transitions from pages with frames to pages without frames
            ApplicationManager.getInstance().get().ActiveApplication.driver.switchTo().defaultContent();
        }

        ((JavascriptExecutor)driver).executeScript("document.location.reload(true);");
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        this.waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
    }

}
