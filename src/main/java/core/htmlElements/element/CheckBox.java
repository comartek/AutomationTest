package core.htmlElements.element;

import core.utils.timeout.TimeoutUtils;
import org.junit.Assert;
import org.openqa.selenium.*;

import java.util.concurrent.TimeUnit;

/**
 * Represents web page checkbox control.
 *
 * @author Artem Koshelev artkoshelev@yandex-team.ru
 * @author Alexander Tolmachev starlight@yandex-team.ru
 */
public class CheckBox extends TypifiedElement {
    /**
     * Specifies wrapped {@link WebElement}.
     *
     * @param wrappedElement {@code WebElement} to wrap.
     */
    public CheckBox(WebElement wrappedElement) {
        super(wrappedElement);
    }

    /**
     * Finds label corresponding to this checkbox using "following-sibling::label" xpath.
     *
     * @return {@code WebElement} representing label or {@code null} if no label has been found.
     */
    public WebElement getLabel() {
        try {
            return getWrappedElement().findElement(By.xpath("following-sibling::label"));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Finds a text of the checkbox label.
     *
     * @return Label text or {@code null} if no label has been found.
     */
    public String getLabelText() {
        WebElement label = getLabel();
        return label == null ? null : label.getText();
    }


    public String getText() {
        return String.valueOf(isSelected()).toUpperCase();
    }

    /**
     * Selects checkbox if it is not already selected.
     */
    public void select() {
        for (int i = 0; i < 3; i++) {
            if (!isSelected()) {
                getWrappedElement().sendKeys(Keys.UP); //scroll UP in order to be able to click checkboxes below main menu items (these items "a" elements covers more space then visible size of item)
                TimeoutUtils.sleep(500, TimeUnit.MILLISECONDS);
                getWrappedElement().click();
                TimeoutUtils.sleep(500, TimeUnit.MILLISECONDS);
            } else
                break;
        }
    }

    /**
     * Deselects checkbox if it is not already deselected.
     */
    public void deselect() {
        for (int i = 0; i < 3; i++) {
            if (isSelected()) {
                getWrappedElement().sendKeys(Keys.UP); //scroll UP in order to be able to click checkboxes below main menu items (these items "a" elements covers more space then visible size of item)
                TimeoutUtils.sleep(500, TimeUnit.MILLISECONDS);
                getWrappedElement().click();
                TimeoutUtils.sleep(500, TimeUnit.MILLISECONDS);
            } else
                break;
        }
    }

    /**
     * Selects checkbox if passed value is {@code true} and deselects otherwise.
     */
    public void set(boolean value) {
        if (value) {
            select();
        } else {
            deselect();
        }
    }


    public void sendKeys(CharSequence... charSequences) {
        String value = charSequences[0].toString().toLowerCase();
        if (value.equals("false")){
            if (this.getText().equals("TRUE")) {
                this.deselect();
            }
        } else if (value.equals("true")) {
            if (this.getText().equals("FALSE")){
                this.select();
            }
        } else
            Assert.fail(String.format("Input value for this field should be TRUE or FALSE"));

    }
}
