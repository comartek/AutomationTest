package core.elements;

import core.htmlElements.element.TypifiedElement;
import core.utils.assertExt.AssertExt;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by Akarpenko on 30.03.2018.
 */
public class T24RadioButtonGroup extends TypifiedElement {

    public T24RadioButtonGroup(WebElement Radio) {
        super(Radio);
    }

    public List<WebElement> getButtons() {

        String xpath= String.format(".//td[input[@type='radio']]");

        return getWrappedElement().findElements(By.xpath(xpath));
    }

    @Override
    public String getText() {
        String output = new String();
        for(WebElement button:getButtons())
        {
            boolean isSelected=button.findElement(By.xpath("input[@type='radio']")).isSelected();
            if (isSelected)
                output=button.findElement(By.xpath("span")).getText();
        }
        return output;
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        String value = charSequences[0].toString();
        for(WebElement button:getButtons())
        {
            if (button.findElement(By.xpath("span")).getText().trim().equals(value)) {
                button.findElement(By.xpath("input[@type='radio']")).click();
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
