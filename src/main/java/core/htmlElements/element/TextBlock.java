package core.htmlElements.element;

import org.openqa.selenium.WebElement;

/**
 * Represents text block on a web page.
 *
 * @author Behruz Afzali xy6er@yandex-team.ru
 */
public class TextBlock extends TypifiedElement {
    public TextBlock(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public String getText() {
    if (isDisplayed())
        return super.getText();
    else
        return getAttribute("textContent");
    }
}
