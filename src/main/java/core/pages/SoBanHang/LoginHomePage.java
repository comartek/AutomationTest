package core.pages.SoBanHang;

import core.htmlElements.element.Button;
import core.htmlElements.element.TextBlock;
import core.htmlElements.element.TextInput;
import core.pages.RosyBasePage;
import org.openqa.selenium.support.FindBy;

public class LoginHomePage extends RosyBasePage {
  @FindBy(xpath = "//*[@text='Nhập số điện thoại']")
  public TextInput Input_Phone;

  @FindBy(xpath = "//*[@text='Nhập mã xác thực']")
  public TextInput Verify_Code_Screen;

  @FindBy(xpath = "//*[contains(@text,'Xin chào')]")
  public TextBlock Hello_Label;


  @FindBy(xpath = "//*[@text='Tiếp tục']")
  public Button Continue_Button;

  @FindBy(xpath = "//*[@resource-id='OTPInputView']")
  public TextInput Input_OTP;

  @FindBy(xpath = "//*[@text='SoBanHang']/parent::android.widget.LinearLayout/following-sibling::android.widget.TextView")
  public TextBlock OTP_SoBanHang;

  @FindBy(xpath = "//*[@resource-id='com.android.systemui:id/dismiss_view']")
  public Button Dismiss_OTP_Notifications;

  @FindBy(xpath = "//*[@resource-id='com.android.mms:id/message_body']")
  public TextBlock OTP_Detail_SoBanHanh;

  @FindBy(xpath = "//*[@resource-id='miui:id/up']")
  public Button Back_Button;

  @Override
  public boolean isLoaded() {
    return true;
  }
}
