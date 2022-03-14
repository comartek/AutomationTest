package core.pages.SoBanHang;

import core.htmlElements.element.Button;
import core.pages.IphoneCommonObjectsPage;
import org.openqa.selenium.support.FindBy;

public class LoginiPhonePage extends IphoneCommonObjectsPage {

  @FindBy(xpath = "(//*[@name=\"NotificationCell\" and contains(@label,'SoBanHang')])[1]")
  public Button OTP_NotificationCell;

}
