package core.elements;

import core.htmlElements.element.TypifiedElement;
import core.utils.assertExt.AssertExt;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class OCBRadioButtonGroup extends TypifiedElement {
    public OCBRadioButtonGroup(WebElement Radio) {
        super(Radio);
    }

    public List<WebElement> getButtons() {
        String radioName = getWrappedElement().getAttribute("name");

        String xpath= String.format(".//label[input[@type='radio']]");

        return getWrappedElement().findElements(By.xpath(xpath));
    }

    @Override
    public String getText() {
        String output = new String();
        for(WebElement button:getButtons())
        {
            boolean isSelected=button.findElement(By.xpath("input[@type='radio']")).isSelected();
            if (isSelected)
                output=button.getText();
        }
        return output;
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        String value = charSequences[0].toString();
        for(WebElement button:getButtons())
        {
            if (button.getText().trim().equals(value)) {
                button.click();//.findElement(By.xpath("div[@class='control__indicator']")).click();
                return;
            }
        }
        Assert.fail(String.format("Radiobutton with text '%s' is not found on page",value));
    }

    @Override
    public boolean isEnabled()
    {
        return getButtons().get(0).findElement(By.xpath("input[@type='radio']")).isEnabled();
    }
}
