package core.htmlElements.element;

import core.elements.dropdownlist.DropdownInterface;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Quotes;

import java.util.Iterator;
import java.util.List;

import static core.steps.BaseSteps.getActiveDriver;

/**
 * Represents web page select control.
 * <p/>
 * Actually this class wraps {@code WebDriver} {@link org.openqa.selenium.support.ui.Select} and delegates
 * all method calls to it. But unlike {@code WebDriver} {@code Select} class there are no checks performed
 * in the constructor of this class, so it can be used correctly with lazy initialization mechanism.
 *
 * @author Artem Koshelev artkoshelev@yandex-team.ru
 * @author Alexander Tolmachev starlight@yandex-team.ru
 */
public class Select extends TypifiedElement implements DropdownInterface {
    /**
     * Specifies wrapped {@link WebElement}.
     * Performs no checks unlike {@link org.openqa.selenium.support.ui.Select}. All checks are made later
     * in {@link #getSelect()} method.
     *
     * @param wrappedElement {@code WebElement} to wrap.
     */
    public Select(WebElement wrappedElement) {
        super(wrappedElement);
    }

    /**
     * Constructs instance of {@link org.openqa.selenium.support.ui.Select} class.
     *
     * @return {@link org.openqa.selenium.support.ui.Select} class instance.
     */
    private org.openqa.selenium.support.ui.Select getSelect() {
        return new org.openqa.selenium.support.ui.Select(getWrappedElement());
    }

    /**
     * Indicates whether this select element support selecting multiple options at the same time.
     * This is done by checking the value of the "multiple" attribute.
     *
     * @return {@code true} if select element support selecting multiple options and {@code false} otherwise.
     */
    public boolean isMultiple() {
        return getSelect().isMultiple();
    }

    /**
     * Returns all options belonging to this select tag.
     *
     * @return A list of {@code WebElements} representing options.
     */
    public List<WebElement> getOptions() {
        return getSelect().getOptions();
    }

    /**
     * Returns all selected options belonging to this select tag.
     *
     * @return A list of {@code WebElements} representing selected options.
     */
    public List<WebElement> getAllSelectedOptions() {
        return getSelect().getAllSelectedOptions();
    }

    /**
     * The first selected option in this select tag (or the currently selected option in a normal select).
     *
     * @return A {@code WebElement} representing selected option.
     */
    public WebElement getFirstSelectedOption() {
        return getSelect().getFirstSelectedOption();
    }

    /**
     * Indicates if select has at least one selected option.
     *
     * @return {@code true} if select has at least one selected option and {@code false} otherwise.
     */
    public boolean hasSelectedOption() {
        return getOptions().stream().anyMatch(WebElement::isSelected);
    }

    /**
     * Select all options that display text matching the argument. That is, when given "Bar" this
     * would select an option like:
     * <p/>
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param text The visible text to match against
     */
    public void selectByVisibleText(String text) {
        getSelect().selectByVisibleText(text);
    }

    /**
     * Select the option at the given index. This is done by examining the "index" attribute of an
     * element, and not merely by counting.
     *
     * @param index The option at this index will be selected
     */
    public void selectByIndex(int index) {
        getSelect().selectByIndex(index);
    }

    /**
     * Select all options that have a value matching the argument. That is, when given "foo" this
     * would select an option like:
     * <p/>
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param value The value to match against
     */
    public void selectByValue(String value) {
        getSelect().selectByValue(value);
    }

    /**
     * Clear all selected entries. This is only valid when the SELECT supports multiple selections.
     *
     * @throws UnsupportedOperationException If the SELECT does not support multiple selections
     */
    public void deselectAll() {
        getSelect().deselectAll();
    }

    /**
     * Deselect all options that have a value matching the argument. That is, when given "foo" this
     * would deselect an option like:
     * <p/>
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param value The value to match against
     */
    public void deselectByValue(String value) {
        getSelect().deselectByValue(value);
    }

    /**
     * Deselect the option at the given index. This is done by examining the "index" attribute of an
     * element, and not merely by counting.
     *
     * @param index The option at this index will be deselected
     */
    public void deselectByIndex(int index) {
        getSelect().deselectByIndex(index);
    }

    /**
     * Deselect all options that display text matching the argument. That is, when given "Bar" this
     * would deselect an option like:
     * <p/>
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param text The visible text to match against
     */
    public void deselectByVisibleText(String text) {
        getSelect().deselectByVisibleText(text);
    }


    @Override
    public String getText() {
        String text = getSelect().getFirstSelectedOption().getText();
        return text;
    }

    public String getFirstUnselectedOption() {
        return getOptions().stream().filter(w->!w.isSelected()).findFirst().get().getText();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {

        selectByVisibleText(keysToSend[0].toString());

        //Workaround for T24 selects failing in IE when triggering page reload on lost focus
        if (getActiveDriver() instanceof InternetExplorerDriver)
            getWrappedElement().sendKeys("\t");
    }



    @Override
    public void selectByPartialValue(String value) {
        List<WebElement> candidates = this.getWrappedElement().findElements(By.xpath(".//option[contains(., " + Quotes.escape(value) + ")]"));
        boolean isMultiple = this.isMultiple();

        Iterator var6 = candidates.iterator();
        boolean matched = false;

        while(var6.hasNext()) {
            WebElement option = (WebElement)var6.next();
            if (option.getText().contains(value)) {
                this.setSelected(option, true);
                if (!isMultiple) {
                    return;
                }
                matched = true;
            }
        }
        if (isMultiple)
            for(int i=0;i<getOptions().size();i++){
            WebElement option = getOptions().get(i);
            if (!option.getText().contains(value)) {
                this.setSelected(option, false);
            }
        }

        if (!matched) {
            throw new NoSuchElementException("Cannot locate element which contains text: " + value);
        }

    }

    private void setSelected(WebElement option, boolean select) {
        boolean isSelected = option.isSelected();
        if (!isSelected && select || isSelected && !select) {
            option.click();
        }

    }

    @Override
    public boolean isValueExist(String value) {
        List<WebElement> candidates = this.getWrappedElement().findElements(By.xpath(".//option[contains(., " + Quotes.escape(value) + ")]"));
        Iterator var6 = candidates.iterator();
        boolean matched = false;

        while(var6.hasNext()) {
            WebElement option = (WebElement)var6.next();
            if (option.getText().contains(value)) {
                matched = true;
                break;
            }
        }
        return matched;
    }

    @Override
    public boolean isExactValueExist(String value) {
        List<WebElement> candidates = this.getWrappedElement().findElements(By.xpath(".//option[contains(., " + Quotes.escape(value) + ")]"));
        Iterator var6 = candidates.iterator();
        boolean matched = false;

        while(var6.hasNext()) {
            WebElement option = (WebElement)var6.next();
            if (option.getText().equals(value)) {
                matched = true;
                break;
            }
        }
        return matched;
    }
}
