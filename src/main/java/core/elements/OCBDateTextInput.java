package core.elements;

import core.htmlElements.element.TextBlock;
import core.htmlElements.element.TextInput;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class OCBDateTextInput extends TextInput {
    public OCBDateTextInput(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public String getText(){
        String startDate = this.findElement(By.xpath("./following-sibling::input[@id='exportHistoryStartDate']")).getAttribute("value");
        String endDate = this.findElement(By.xpath("./following-sibling::input[@id='exportHistoryEndDate']")).getAttribute("value");
        return startDate + "-" + endDate;
    }
}
