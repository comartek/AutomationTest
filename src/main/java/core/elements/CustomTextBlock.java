package core.elements;

import core.htmlElements.element.TextBlock;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CustomTextBlock extends TextBlock {
    public CustomTextBlock(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public String getText(){
        return this.findElement(By.xpath("./following-sibling::input")).getAttribute("value");
    }
}
