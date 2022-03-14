package core.pages.SoBanHang;

import core.htmlElements.element.Button;
import core.pages.IphoneCommonObjectsPage;
import org.openqa.selenium.support.FindBy;

public class HomePageiPhone extends IphoneCommonObjectsPage {

  @FindBy(xpath = "(//XCUIElementTypeOther[@name=\"Quản lý\"])[2]")
  public Button Quan_Ly_Button;
}
