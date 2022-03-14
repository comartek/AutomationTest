package core.pages;

import core.htmlElements.element.Button;
import core.htmlElements.element.TextBlock;
import org.openqa.selenium.support.FindBy;

/**
 * Created by Akarpenko on 16.11.2017.
 */

public class IpadCommonObjectsPage extends RosyBasePage {


    @FindBy(xpath = "//XCUIElementTypeOther[@name=\"CANCEL\"]")
    public Button Cancel_Button;

    @FindBy(xpath = "//XCUIElementTypeOther[@name=\"REMOVE\"]")
    public Button Remove_Button;

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Total: \"]/following-sibling::XCUIElementTypeStaticText")
    public TextBlock Total_Price_Label;

    @FindBy(xpath = "//XCUIElementTypeButton[@name=\"Dismiss\"]")
    public Button Dismiss_Button;

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Transaction cancelled\"]")
    public TextBlock Transaction_Cancelled_Label;

    @FindBy(xpath = "(//XCUIElementTypeOther[@name=\"TERMINAL\"])[2]")
    public Button TERMINAL_Button;

    @FindBy(xpath = "(//XCUIElementTypeStaticText[contains(@name,'kr')]/parent::XCUIElementTypeOther/preceding-sibling::XCUIElementTypeOther/child::XCUIElementTypeStaticText)[1]")
    public TextBlock Name_Item_First_Label;

    @FindBy(xpath = "(//XCUIElementTypeOther[@name=\"PAYMENT\"])[2]")
    public Button Payment_Button;

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name=\"title\"]")
    public TextBlock Add_To_Cart_Label;




    @Override
    public boolean isLoaded()
    {
        return true;
    }
}
