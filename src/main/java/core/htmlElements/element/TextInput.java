package core.htmlElements.element;

import core.utils.timeout.TimeoutUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Optional;

import static core.steps.BaseSteps.getActiveDriver;
import static core.steps.BaseSteps.getCurrentPage;

/**
 * Represents text input control (such as &lt;input type="text"/&gt; or &lt;textarea/&gt;).
 *
 * @author Alexander Tolmachev starlight@yandex-team.ru
 *         Date: 13.08.12
 */
public class TextInput extends TypifiedElement {
    /**
     * Specifies wrapped {@link WebElement}.
     *
     * @param wrappedElement {@code WebElement} to wrap.
     */
    public TextInput(WebElement wrappedElement) {
        super(wrappedElement);
    }

    /**
     * @return Text entered into the text input.
     * @deprecated Use getText() instead.
     * <p/>
     * Retrieves the text entered into this text input.
     */
    @Deprecated
    public String getEnteredText() {
        return getText();
    }

    /**
     * Retrieves the text entered into this text input.
     *
     * @return Text entered into the text input.
     */
    @Override
    public String getText() {
        if ("textarea".equals(getWrappedElement().getTagName()) || getWrappedElement().getAttribute("class").contains("textarea")) {
            return getWrappedElement().getText();
        }

        String value=getWrappedElement().getAttribute("value");
        if (value!=null)
            return value;
        value=getWrappedElement().getText();
        if (StringUtils.isNotEmpty(value))
            return value;
        return  "";
    }

    /**
     * Returns sequence of backspaces and deletes that will clear element.
     * clear() can't be used because generates separate onchange event
     * See https://github.com/yandex-qatools/htmlelements/issues/65
     */
    public String getClearCharSequence() {
        return StringUtils.repeat(Keys.DELETE.toString() + Keys.BACK_SPACE, getText().length());
    }


    @Override
    public void sendKeys(CharSequence... keysToSend) {
// IEDriver has a bug when it can randomly input characters in wrong case, so we have to do a workaround here
// https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/3699
        if (getActiveDriver() instanceof InternetExplorerDriver){
            InvalidElementStateException inputException=null;
            for (int i=0;i<3;i++){
                try {
                    getWrappedElement().sendKeys(getClearCharSequence());
                    getWrappedElement().sendKeys(keysToSend);
                    String realText=getText();
//                if (realText.equalsIgnoreCase(keysToSend[0].toString())&&
//                        !realText.equals(keysToSend[0].toString())) {
                    if (!realText.equals(keysToSend[0].toString())) {
                        Logger.getRootLogger().warn(String.format("Resulting text in field \"%s\" does not match input \"%s\"",realText,keysToSend[0].toString()));
                        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
                        getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
                        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
                        continue;
                    }
                    else {
                        inputException = null;
                        break;
                    }
                }
                catch (InvalidElementStateException e){
                    inputException = e;
                    Logger.getRootLogger().info("Catched InvalidElementStateException, retrying input..");
                    TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
                    getCurrentPage().waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
                    TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
                }

            }

            if (inputException!=null)
                throw inputException;
        } else {
            getWrappedElement().sendKeys(getClearCharSequence());
            getWrappedElement().sendKeys(keysToSend);
        }
        // blurActiveElement(); //deactivate focus because otherwise border for some elements can be green instead of chosen color in method activateElementBorder()
    }

    @Override
    public boolean isEnabled()
    {
        return getWrappedElement().isEnabled()&&
                (getAttribute("readonly")==null||getAttribute("readonly").equals("false"));
    }
}
