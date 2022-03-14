package core.pages;

import core.htmlElements.element.Button;
import core.htmlElements.element.Named;
import core.htmlElements.element.TextBlock;
import org.openqa.selenium.support.FindBy;

public class AndroidTabletCommonObjectsPage extends RosyBasePage {

    @FindBy(xpath = "//*[@content-desc='logout']/android.view.ViewGroup/android.widget.TextView")
    public TextBlock NotAvatar;

    @FindBy(xpath = "//*[@text='Confirm']")
    public Button Confirm_Button;

    @FindBy(xpath = "//android.widget.TextView[@text='Search']")
    public Button OpenSearch_Button;

    @FindBy(xpath = "//*[contains(@text,'Choose')]")
    public Button Filter_Open;

    @FindBy(xpath = "//android.widget.TextView[@text='Filter']")
    public Button OpenFilter_Button;

    @FindBy(xpath = "//android.widget.TextView[@text='Hide filters']")
    public Button HideFilter_Button;

    @FindBy(xpath = "//android.widget.TextView[@text='Reset filters']")
    public Button ResetFilters_Button;

    @FindBy(xpath = "//android.widget.TextView[@text='Apply filters']")
    public Button ApplyFilters_Button;

    @FindBy(xpath = "(//android.widget.TextView[@text='All'])[1]")
    public Button All_Button;

    @FindBy(xpath = "(//android.widget.TextView[@text='Photos'])[1]")
    public Button Photos_Button;

    @FindBy(xpath = "//*[@text='Documents']")
    public Button Documents_Button;

    @FindBy(xpath = "//*[@text='Receipts']")
    public Button Receipts_Button;

    @FindBy(xpath = "//*[@text='Scanned']")
    public Button Scanned_Button;

    @FindBy(xpath = "//*[@text='Apply filters']")
    public Button Apply_Filters;

    @FindBy(xpath = "//*[@resource-id='com.android.permissioncontroller:id/permission_allow_button']")
    public  Button Apply_all_Button;

    @FindBy(xpath = "//*[@resource-id='com.android.permissioncontroller:id/permission_allow_foreground_only_button']")
    public  Button While_Using_The_App_Button;

    @FindBy(xpath = "//*[@text='Continue']")
    public Button Continue;
    @FindBy(xpath = "//*[@content-desc='add_vault']")
    public Button Add_Vault_Button;
    @FindBy(xpath = "//*[@text='Enable Access']")
    public Button EnableAccess_Button;
    @FindBy(xpath = "//*[@text='Allow all the time']")
    public Button Permission_Allow_Always_Button;
    @FindBy(xpath = "//*[@text='Allow']")
    public Button Permission_Allow_Button;
    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'home_icon')]")
    public Button HomeMenu_Button;
    @FindBy(xpath = "(//android.widget.TextView[contains(@text,'total items')])")
    public Button Total_Items;

    @FindBy(xpath = "//*[contains(@text,'No results found')]")
    public Button ResultSearch;

    @FindBy(xpath = "//android.widget.TextView[@text='DD']")
    public Button With_Out_Avatar;

    @FindBy(xpath = "//android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.widget.ImageView")
    public Button With_Avatar;

//    @FindBy(xpath = "//*[@content-desc='vault_icon']")
    @FindBy(xpath = "//*[contains(@content-desc,'vault_icon')]")
    public Button VaultMenu_Button;

    @FindBy(xpath = "//*[contains(@content-desc,'addressBook_icon')]")
    public Button AddressBookMenu_Button;

    @FindBy(xpath = "//*[contains(@content-desc,'inbox_icon')]")
    public Button NotificationMenu_Button;

    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'show_icon')]")
    public Button OpenLeftMenu_Button;

    @FindBy(xpath = "//*[contains(@content-desc,'settings_icon')]")
    public Button Settings_Button;

    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'logout')]")
    public Button LogOut_Button;
    @FindBy(xpath = "(//*[@text='Back'])[1]")
    public Button Back_Button;

    @FindBy(xpath = "//*[@text='Hide']")
    public Button Hide_Button_Menu;

    @FindBy(xpath = "//*[contains(@text,'Take a Look')]")
    public Button Take_A_Look_Button;

    @FindBy(xpath = "//android.widget.HorizontalScrollView/android.view.ViewGroup/android.view.ViewGroup")
    public Button Number_Card;

    @FindBy(xpath = "//*[contains(@text,'Add a memory')]")
    public Button Add_Memory;

    @FindBy(xpath = "(//*[contains(@text,'Add Your Memory')])[2]")
    public Button Add_Your_Memory_Button;

    @Override
    public boolean isLoaded()
    {
        return true;
    }
}
