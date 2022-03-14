package core.htmlElements.element;

import io.appium.java_client.MobileElement;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static core.steps.BaseSteps.getActiveDriver;

/**
 * The base class to be used for making classes representing typified elements (i.e mobile page controls such as
 * text inputs, buttons or more complex elements).
 * <p/>
 * There are several already written classes representing standard mobile page controls:
 * <ul>
 * <li>{@link TextInput}</li>
 * <li>{@link CheckBox}</li>
 * <li>{@link Select}</li>
 * <li>{@link Link}</li>
 * <li>{@link MobileButton}</li>
 * <li>{@link Radio}</li>
 * <li>{@link TextBlock}</li>
 * <li>{@link Image}</li>
 * <li>{@link Form}</li>
 * <li>{@link FileInput}</li>
 * </ul>
 * <p/>
 * But you can also write your own typified elements if needed. For example:
 * <p/>
 * <pre>
 * <code>public class Link extends TypifiedElement {
 * public Link(WebElement wrappedElement) {
 * super(wrappedElement);
 * }
 * <p/>
 * public String getReference() {
 * return getWrappedElement().getAttribute("href");
 * }
 * }
 * </code>
 * </pre>
 *
 * @author Alexander Tolmachev starlight@yandex-team.ru
 *         Date: 13.08.12
 */
public abstract class TypifiedMobileElement extends ExtendedWebElement {
    private final MobileElement wrappedElement;
    private String name;

    /**
     * Specifies wrapped {@link WebElement}.
     *
     * @param wrappedElement {@code WebElement} to wrap.
     */
    protected TypifiedMobileElement(MobileElement wrappedElement) {
        this.wrappedElement = wrappedElement;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets a name of an element. This method is used by initialization mechanism and is not intended
     * to be used directly.
     *
     * @param name Name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns specified name, actually the same as {@link #getName()} method.
     *
     * @return {@code String} representing name.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Determines whether or not this element exists on page.
     *
     * @return True if the element exists on page, false otherwise.
     */
    @SuppressWarnings("squid:S1166")
    // Sonar "Exception handlers should preserve the original exception" rule
    public boolean exists() {
        try {
            getWrappedElement().isDisplayed();
        } catch (NoSuchElementException ignored) {
            return false;
        }
        return true;
    }

    /**
     *
             * @author densm27
     *         Date: 02.11.17
     */
    @Override
    public MobileElement getWrappedElement() {
        //return wrappedElement;

        //Двухуровневое проксирование:
        //Первый уровень - HTMLElement/TypifiedElement
        //Второй - WebElementNamedProxyHandler, имитирующий IWebElement
        //На третьем уровне сам IWebElement нужного типа
        return wrappedElement instanceof WrapsElement?((MobileElement)wrappedElement):wrappedElement;
    }

    @Override
    public void click() {
        getWrappedElement().click();
    }

    @Override
    public void submit() {
        getWrappedElement().submit();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        getWrappedElement().sendKeys(keysToSend);
        blurActiveElement(); //deactivate focus because otherwise border for some elements can be green instead of chosen color in method activateElementBorder()
    }

    @Override
    public void clear() {
        getWrappedElement().clear();
    }

    @Override
    public String getTagName() {
        return getWrappedElement().getTagName();
    }

    @Override
    public String getAttribute(String name) {
        return getWrappedElement().getAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return getWrappedElement().isSelected();
    }

    @Override
    public boolean isEnabled() {
        return getWrappedElement().isEnabled();
    }

    @Override
    public String getText() {
        return getWrappedElement().getText();
    }


    @Override
    public List<WebElement> findElements(By by) {
        return findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return getWrappedElement().findElement(by);
    }

    @Override
    public boolean isDisplayed() {
        return getWrappedElement().isDisplayed();
    }

    @Override
    public Point getLocation() {
        return getWrappedElement().getLocation();
    }

    @Override
    public Dimension getSize() {
        return getWrappedElement().getSize();
    }

    @Override
    public Rectangle getRect() {
        return getWrappedElement().getRect();
    }

    @Override
    public String getCssValue(String propertyName) {
        return getWrappedElement().getCssValue(propertyName);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) {
        return getWrappedElement().getScreenshotAs(target);
    }

}
