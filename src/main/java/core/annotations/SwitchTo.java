package core.annotations;

import core.utils.lang.FindByUtils;
import core.utils.timeout.TimeoutUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SwitchTo {

    //Mandatory SwitchTo type
    SwitchToType switchToType();

    //Optional SwitchTo values. Supposed to match chosen type of SwitchToType enum ...
    // ... therefore for some SwitchToType types no SwitchTo value is needed (for example SWITCH_TO_PARENT_FRAME)
    boolean preliminarilySwitchToDefaultContent() default true;
    int switchToFrameUsingInt() default defaultInt;
    String switchToFrameUsingString() default defaultString;
    String switchToWindowUsingString() default defaultString;
    // Also Optional SwitchTo values (can be multiple) that can only be used for SwitchToType SWITCH_TO_FRAME_using_WEB_ELEMENT
    How webElementFindByHow() default How.UNSET;
    String webElementFindByUsing() default defaultString;
    String webElementFindById() default defaultString;
    String webElementFindByName() default defaultString;
    String webElementFindByClassName() default defaultString;
    String webElementFindByCss() default defaultString;
    String webElementFindByTagName() default defaultString;
    String webElementFindByLinkText() default defaultString;
    String webElementFindByPartialLinkText() default defaultString;
    String webElementFindByXpath() default defaultString;


    enum SwitchToType{
        SWITCH_TO_FRAME_using_INT,
        SWITCH_TO_FRAME_using_STRING,
        SWITCH_TO_FRAME_using_WEB_ELEMENT,
        SWITCH_TO_PARENT_FRAME,
        SWITCH_TO_WINDOW_using_STRING,
        SWITCH_TO_DEFAULT_CONTENT,
        SWITCH_TO_ACTIVE_ELEMENT,
        SWITCH_TO_ALERT
        //THROW_EXCEPTION //can be used as default SwitchToType which is basically UNALLOWED to be used and will cause an exception
    }

    // DEFAULT VALUES
    String defaultString = "_SwitchTo_default String";
    int defaultInt = -1234567890;

    class SwitchToFrameByXpathImpl implements SwitchTo{

        String xpath;
        boolean switchToDefault;

        SwitchToFrameByXpathImpl(boolean switchToDefault, String xpath){
            this.xpath = xpath;
            this.switchToDefault = switchToDefault;
        }

        @Override
        public SwitchToType switchToType() {
            return SwitchToType.SWITCH_TO_FRAME_using_WEB_ELEMENT;
        }

        @Override
        public boolean preliminarilySwitchToDefaultContent() {
            return switchToDefault;
        }

        @Override
        public int switchToFrameUsingInt() {
            return 0;
        }

        @Override
        public String switchToFrameUsingString() {
            return null;
        }

        @Override
        public String switchToWindowUsingString() {
            return null;
        }

        @Override
        public How webElementFindByHow() {
            return null;
        }

        @Override
        public String webElementFindByUsing() {
            return null;
        }

        @Override
        public String webElementFindById() {
            return null;
        }

        @Override
        public String webElementFindByName() {
            return null;
        }

        @Override
        public String webElementFindByClassName() {
            return null;
        }

        @Override
        public String webElementFindByCss() {
            return null;
        }

        @Override
        public String webElementFindByTagName() {
            return null;
        }

        @Override
        public String webElementFindByLinkText() {
            return null;
        }

        @Override
        public String webElementFindByPartialLinkText() {
            return null;
        }

        @Override
        public String webElementFindByXpath() {
            return xpath;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return SwitchToFrameByXpathImpl.class;
        }
    }

    // UTILS
    class SwitchToUtils {

        public static SwitchTo[] generateSwitchToFrames(SwitchToFrameChain switchToFrameChain)
        {
            SwitchTo[] array = new SwitchTo[switchToFrameChain.value().length];
            for (int i=0;i<array.length;i++){
                SwitchTo switchTo=new SwitchToFrameByXpathImpl(i==0,switchToFrameChain.value()[i]);
                array[i]=switchTo;
            }
            return array;
        }

        public static void validateInputValues(SwitchTo switchTo, Class pageClass) {
            if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_FRAME_using_INT)) {
                if (switchTo.switchToFrameUsingInt() == defaultInt || !switchTo.switchToFrameUsingString().equals(defaultString) || !switchTo.switchToWindowUsingString().equals(defaultString) || !switchTo.webElementFindByHow().equals(How.UNSET) || !switchTo.webElementFindByUsing().equals(defaultString) || !switchTo.webElementFindById().equals(defaultString) || !switchTo.webElementFindByName().equals(defaultString) || !switchTo.webElementFindByClassName().equals(defaultString) || !switchTo.webElementFindByCss().equals(defaultString) || !switchTo.webElementFindByTagName().equals(defaultString) || !switchTo.webElementFindByLinkText().equals(defaultString) || !switchTo.webElementFindByPartialLinkText().equals(defaultString) || !switchTo.webElementFindByXpath().equals(defaultString))
                    throw new IllegalArgumentException(String.format("Illegal use of annotation 'SwitchTo' for the class '%s'. For a chosen type 'SwitchTo.SwitchToType.SWITCH_TO_FRAME_using_INT' it is mandatory to specify value for a parameter 'switchToFrameUsingInt' (and no other parameters is allowed)", pageClass.toString()));
            } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_FRAME_using_STRING)) {
                if (switchTo.switchToFrameUsingString().equals(defaultString) || switchTo.switchToFrameUsingInt() != defaultInt || !switchTo.switchToWindowUsingString().equals(defaultString) || !switchTo.webElementFindByHow().equals(How.UNSET) || !switchTo.webElementFindByUsing().equals(defaultString) || !switchTo.webElementFindById().equals(defaultString) || !switchTo.webElementFindByName().equals(defaultString) || !switchTo.webElementFindByClassName().equals(defaultString) || !switchTo.webElementFindByCss().equals(defaultString) || !switchTo.webElementFindByTagName().equals(defaultString) || !switchTo.webElementFindByLinkText().equals(defaultString) || !switchTo.webElementFindByPartialLinkText().equals(defaultString) || !switchTo.webElementFindByXpath().equals(defaultString))
                    throw new IllegalArgumentException(String.format("Illegal use of annotation 'SwitchTo' for the class '%s'. For a chosen type 'SwitchTo.SwitchToType.SWITCH_TO_FRAME_using_STRING' it is mandatory to specify value for a parameter 'switchToFrameUsingString' (and no other parameters is allowed)", pageClass.toString()));
            } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_FRAME_using_WEB_ELEMENT)) {
                if (switchTo.switchToFrameUsingInt() != defaultInt || !switchTo.switchToFrameUsingString().equals(defaultString) || !switchTo.switchToWindowUsingString().equals(defaultString))
                    throw new IllegalArgumentException(String.format("Illegal use of annotation 'SwitchTo' for the class '%s'. For a chosen type 'SwitchTo.SwitchToType.SWITCH_TO_FRAME_using_WEB_ELEMENT' it is allowed to only use parameters which begins with 'webElement...' (the same way as for @FindBy) (and no other parameters is allowed)", pageClass.toString()));
            } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_PARENT_FRAME)) {
                if (switchTo.switchToFrameUsingInt() != defaultInt || !switchTo.switchToFrameUsingString().equals(defaultString) || !switchTo.switchToWindowUsingString().equals(defaultString) || !switchTo.webElementFindByHow().equals(How.UNSET) || !switchTo.webElementFindByUsing().equals(defaultString) || !switchTo.webElementFindById().equals(defaultString) || !switchTo.webElementFindByName().equals(defaultString) || !switchTo.webElementFindByClassName().equals(defaultString) || !switchTo.webElementFindByCss().equals(defaultString) || !switchTo.webElementFindByTagName().equals(defaultString) || !switchTo.webElementFindByLinkText().equals(defaultString) || !switchTo.webElementFindByPartialLinkText().equals(defaultString) || !switchTo.webElementFindByXpath().equals(defaultString))
                    throw new IllegalArgumentException(String.format("Illegal use of annotation 'SwitchTo' for the class '%s'. For a chosen type 'SwitchTo.SwitchToType.SWITCH_TO_PARENT_FRAME' it is NOT allowed to specify a value for any parameter but 'switchToType'", pageClass.toString()));
            } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_WINDOW_using_STRING)) {
                if (switchTo.switchToWindowUsingString().equals(defaultString) || switchTo.switchToFrameUsingInt() != defaultInt || !switchTo.switchToFrameUsingString().equals(defaultString) || !switchTo.webElementFindByHow().equals(How.UNSET) || !switchTo.webElementFindByUsing().equals(defaultString) || !switchTo.webElementFindById().equals(defaultString) || !switchTo.webElementFindByName().equals(defaultString) || !switchTo.webElementFindByClassName().equals(defaultString) || !switchTo.webElementFindByCss().equals(defaultString) || !switchTo.webElementFindByTagName().equals(defaultString) || !switchTo.webElementFindByLinkText().equals(defaultString) || !switchTo.webElementFindByPartialLinkText().equals(defaultString) || !switchTo.webElementFindByXpath().equals(defaultString))
                    throw new IllegalArgumentException(String.format("Illegal use of annotation 'SwitchTo' for the class '%s'. For a chosen type 'SwitchTo.SwitchToType.SWITCH_TO_WINDOW_using_STRING' it is mandatory to specify value for a parameter 'switchToWindowUsingString' (and no other parameters is allowed)", pageClass.toString()));
            } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_DEFAULT_CONTENT)) {
                if (switchTo.switchToFrameUsingInt() != defaultInt || !switchTo.switchToFrameUsingString().equals(defaultString) || !switchTo.switchToWindowUsingString().equals(defaultString) || !switchTo.webElementFindByHow().equals(How.UNSET) || !switchTo.webElementFindByUsing().equals(defaultString) || !switchTo.webElementFindById().equals(defaultString) || !switchTo.webElementFindByName().equals(defaultString) || !switchTo.webElementFindByClassName().equals(defaultString) || !switchTo.webElementFindByCss().equals(defaultString) || !switchTo.webElementFindByTagName().equals(defaultString) || !switchTo.webElementFindByLinkText().equals(defaultString) || !switchTo.webElementFindByPartialLinkText().equals(defaultString) || !switchTo.webElementFindByXpath().equals(defaultString))
                    throw new IllegalArgumentException(String.format("Illegal use of annotation 'SwitchTo' for the class '%s'. For a chosen type 'SwitchTo.SwitchToType.SWITCH_TO_DEFAULT_CONTENT' it is NOT allowed to specify a value for any parameter but 'switchToType'", pageClass.toString()));
            } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_ACTIVE_ELEMENT)) {
                if (switchTo.switchToFrameUsingInt() != defaultInt || !switchTo.switchToFrameUsingString().equals(defaultString) || !switchTo.switchToWindowUsingString().equals(defaultString) || !switchTo.webElementFindByHow().equals(How.UNSET) || !switchTo.webElementFindByUsing().equals(defaultString) || !switchTo.webElementFindById().equals(defaultString) || !switchTo.webElementFindByName().equals(defaultString) || !switchTo.webElementFindByClassName().equals(defaultString) || !switchTo.webElementFindByCss().equals(defaultString) || !switchTo.webElementFindByTagName().equals(defaultString) || !switchTo.webElementFindByLinkText().equals(defaultString) || !switchTo.webElementFindByPartialLinkText().equals(defaultString) || !switchTo.webElementFindByXpath().equals(defaultString))
                    throw new IllegalArgumentException(String.format("Illegal use of annotation 'SwitchTo' for the class '%s'. For a chosen type 'SwitchTo.SwitchToType.SWITCH_TO_ACTIVE_ELEMENT' it is NOT allowed to specify a value for any parameter but 'switchToType'", pageClass.toString()));
            } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_ALERT)) {
                if (switchTo.switchToFrameUsingInt() != defaultInt || !switchTo.switchToFrameUsingString().equals(defaultString) || !switchTo.switchToWindowUsingString().equals(defaultString) || !switchTo.webElementFindByHow().equals(How.UNSET) || !switchTo.webElementFindByUsing().equals(defaultString) || !switchTo.webElementFindById().equals(defaultString) || !switchTo.webElementFindByName().equals(defaultString) || !switchTo.webElementFindByClassName().equals(defaultString) || !switchTo.webElementFindByCss().equals(defaultString) || !switchTo.webElementFindByTagName().equals(defaultString) || !switchTo.webElementFindByLinkText().equals(defaultString) || !switchTo.webElementFindByPartialLinkText().equals(defaultString) || !switchTo.webElementFindByXpath().equals(defaultString))
                    throw new IllegalArgumentException(String.format("Illegal use of annotation 'SwitchTo' for the class '%s'. For a chosen type 'SwitchTo.SwitchToType.SWITCH_TO_ALERT' it is NOT allowed to specify a value for any parameter but 'switchToType'", pageClass.toString()));
            }
        }

        public static void performSwitchTo(SwitchTo switchTo, Class pageClass, WebDriver driver){
            try {
                if(switchTo.preliminarilySwitchToDefaultContent()) {
                    driver.switchTo().defaultContent();
                }

                if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_FRAME_using_INT)) {
                    driver.switchTo().frame(switchTo.switchToFrameUsingInt());
                } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_FRAME_using_STRING)) {
                    driver.switchTo().frame(switchTo.switchToFrameUsingString());
                } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_FRAME_using_WEB_ELEMENT)) {
                    FindBy findBy = FindByUtils.getFindByFromSwitchTo(switchTo);
                    By by = new FindBy.FindByBuilder().buildIt(findBy, null);
                    WebElement element = driver.findElement(by);
                    driver.switchTo().frame(element);
                } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_PARENT_FRAME)) {
                    driver.switchTo().parentFrame();
                } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_WINDOW_using_STRING)) {
                    driver.switchTo().window(switchTo.switchToWindowUsingString());
                } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_DEFAULT_CONTENT)) {
                    driver.switchTo().defaultContent();
                } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_ACTIVE_ELEMENT)) {
                    driver.switchTo().activeElement();
                } else if (switchTo.switchToType().equals(SwitchToType.SWITCH_TO_ALERT)) {
                    driver.switchTo().alert();
                } else {
                    //Exception unknown SwitchToType value (this method need to be updated)
                    throw new RuntimeException(String.format("Framework Error: Method performSwitchTo of class SwitchTo.SwitchToUtils has received an unknown value of enum SwitchTo.SwitchToType (value = '%s'). Ask framework developers to fix it by assigning specific behavior for the current SwitchTo.SwitchToType value in the method SwitchTo.SwitchToUtils.performSwitchTo", switchTo.switchToType()));
                }
            } catch (Exception causeException) {
                String switchToLog = String.format("An exception was thrown while trying to perform driver.switchTo()... operation on a Page '%s' using @SwitchTo annotation (current SwitchTo info: %s). The cause exception is in the following log", pageClass.toString(), switchTo.toString());
                throw new RuntimeException(switchToLog, causeException);
            }
        }

    }

}
