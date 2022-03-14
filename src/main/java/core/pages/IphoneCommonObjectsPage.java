package core.pages;

import core.htmlElements.element.*;
import org.openqa.selenium.support.FindBy;

/**
 * Created by Akarpenko on 16.11.2017.
 */

public class IphoneCommonObjectsPage extends RosyBasePage {


    @FindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Totalt\"]/parent::XCUIElementTypeOther/following-sibling::XCUIElementTypeOther/child::XCUIElementTypeStaticText")
    public TextBlock Total_Price;

    @FindBy(xpath = "//XCUIElementTypeButton[@name=\"Bekrefte\"]")
    public Button Bekrefte_Button;

    @FindBy(xpath = "//XCUIElementTypeButton[@name=\"Avbryt\"]")
    public Button Avbryt_Button;

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Handlevognen\"]")
    public Button Handlevognen_Button;

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Ordre\"]")
    public Button Ordre_Button;

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Info\"]")
    public Button Info_Button;

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Hjem\"]")
    public Button Hjem_Button;

    @FindBy(xpath = "//XCUIElementTypeOther[@name=\"banner\"]/XCUIElementTypeImage[1]")
    public Button Menu_Button;



    @Override
    public boolean isLoaded()
    {
        return true;
    }
}
