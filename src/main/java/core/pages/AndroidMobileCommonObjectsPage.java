package core.pages;

import core.htmlElements.element.*;
import org.openqa.selenium.support.FindBy;

public class AndroidMobileCommonObjectsPage extends RosyBasePage {

    @FindBy(xpath = "//*[@resource-id='com.android.permissioncontroller:id/permission_allow_foreground_only_button']")
    public Button LocationNew;
    @FindBy(xpath = "//android.widget.TextView[contains(@text,'Tap')]")
    public Button TapForNewItems_Button;

    @FindBy(xpath = "//*[contains(@text,'Set up syncing')]")
    public Button SetupSyncing;
    @FindBy(xpath = "//*[contains(@content-desc,'add_vault')]")
    public Button Add_Vault_Button;

    @FindBy(xpath = "//*[contains(@text,'Log In')]")
    public Button Login_Button;

    @FindBy(xpath = "//*[@text='Confirm']")
    public Button Confirm_Button;

    @FindBy(xpath = "//*[@text='Back']")
    public Button Back_Button;

    @FindBy(xpath = "//*[@text='Vault']")
    public Button Vault_Button;

    @FindBy(xpath = "//*[@text='Continue']")
    public Button Continue;

    @FindBy(xpath = "//*[contains(@content-desc,'openDrawerMenuButton')]")
    public Button LeftMenu_Button;

    //    @FindBy(xpath = "//android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup[1]")
//    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'logout')]")
//    public Button LogOut_Button;

    @FindBy(xpath = "//*[@text='Log out']")
    public Button Log_Out_Button;

    @FindBy(xpath = "//*[contains(@text,'Feedback')]")
    public Button Feedback_Button;

    @FindBy(xpath = "//*[contains(@text,'Sync')]")
    public Button Syncing;

    @FindBy(xpath = "//*[@text='My Settings']")
    public Button My_Settings;

    @FindBy(xpath = "//android.view.ViewGroup//android.view.ViewGroup[3]")
    public Button AddStoryMenu_Button;

    @FindBy(xpath = "//android.view.ViewGroup//android.view.ViewGroup[4]")
    public Button SyncMenu_Button;

    @FindBy(xpath = "//*[contains(@content-desc,'close')]")
    public Button Close_LeftMenu;


    @FindBy(xpath = "//android.view.ViewGroup//android.view.ViewGroup[5]")
    public Button NotificationMenu_Button;

    @FindBy(xpath = "//*[contains(@content-desc,'homeBottomTab')]")
    public Button HomeMenu_Button;

    @FindBy(xpath = "//*[contains(@content-desc,'vaultBottomTab')]")
    public Button VaultMenu_Button;

    @FindBy(xpath = "//*[contains(@content-desc,'addressBookBottomTab')]")
    public Button AddressbookAndroid;

    @FindBy(xpath = "//*[contains(@content-desc,'galleryBottomTab')]")
    public Button GalleryMenu_Button;

    @FindBy(xpath = "//*[@text='Import items']")
    public Button Upload_Items_Button;

    @FindBy(xpath = "//*[@text='Sync items']")
    public Button Sync_Items_Button;

    @FindBy(xpath = "//*[@text='Take a photo']")
    public Button Take_Photo_Button;

    @FindBy(xpath = "//android.widget.TextView[@text='Cancel']")
    public Button Cancel_Button;

    @FindBy(xpath = "//*[contains(@text,'m Rosy')]")
    public Button HI;

    @FindBy(xpath = "//*[contains(@text,'About Cards')]")
    public Button About_Cards;

    @FindBy(xpath = "//*[contains(@text,'About')]")
    public Button About_LeftMenu_Screen_Button;

    @FindBy(xpath = "//*[contains(@text,'Try it out')]")
    public Button Try_It_Out;

    @FindBy(xpath = "//*[@text='Don’t show me again']/preceding-sibling::android.view.ViewGroup/child::android.view.ViewGroup")
    public CheckBox Dont_Show_Me_Again_CheckBox;

    @FindBy(xpath = "//*[contains(@text,'Got')]")
    public Button Got_It_Button;

    @FindBy(xpath = "//*[contains(@text,'Got it')]")
    public Button Got_it_Tooltip;
    
    @FindBy(xpath = "//*[@text='Close']")
    public Button Close;

    @FindBy(xpath = "//*[@text='Camera']")
    public Button Camera_Button;

    @FindBy(xpath = "//*[@text='Enable access']")
    public Button EnableAccess_Button;

    @FindBy(xpath = "//*[contains(@content-desc,'Select photos')]/child::android.widget.TextView")
    public Button Select_Photos_Button;

    @FindBy(xpath = "//*[@text='Select photos']")
    public Button Select_Photo_Button;

    @FindBy(xpath = "//*[@resource-id='com.android.permissioncontroller:id/permission_allow_button']")
    public Button Allow_Button_Type1;

    @FindBy(xpath = "//*[@resource-id='com.android.permissioncontroller:id/permission_allow_button']")
    public Button Allow_Button;

    @FindBy(xpath = "//*[@text='Allow']")
    public Button Allow_Access_Button;

    @FindBy(xpath = "//*[@text='Deny']")
    public Button Deny_Access_Button;

    @FindBy(xpath = "//*[starts-with(@text,'Don')]")
    public Button Dont_Allow_Button;

    @FindBy(xpath = "//*[@text='ALLOW']")
    public Button Allow_Button_Type3;

//    @FindBy(xpath = "//*[@resource-id='com.android.packageinstaller:id/permission_allow_button']")
    @FindBy(xpath = "//*[@resource-id='com.android.permissioncontroller:id/permission_allow_button']")
    public  Button Allow_Button_Type2;

    @FindBy(xpath = "//*[contains(@text,'While using the app')]")
    public  Button Allow_Using_The_App_Button;

    @FindBy(xpath = "//*[contains(@text,'Take a look')]")
    public Button Take_A_Look_Button;

    @FindBy(xpath = "//*[@text='Connect']")
    public Button Connect_Button;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup//android.widget.ImageView")
    public Button listSearch;

    @FindBy(xpath = "(//*[@resource-id='com.demorosyv2.dev:id/texture_view']/parent::android.widget.FrameLayout/following-sibling::android.view.ViewGroup/child::android.view.ViewGroup/child::android.view.ViewGroup)[1]")
    public Button Take_Camera_Button;


    @FindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup")
    public Button Camera_Button_1;

    @FindBy(xpath = "//*[@text='Send to Vault']")
    public Button Send_Vault_Button;

    @FindBy(xpath = "//*[@text='Send an invite']")
    public Button Send_An_Invite_Button;

    @FindBy(xpath = "//*[@text='First name']/following-sibling::android.widget.EditText[1]")
    public TextInput FName_Send_Invite_Input;

    @FindBy(xpath = "//*[@text='Last name']/following-sibling::android.widget.EditText[1]")
    public TextInput LName_Send_Invite_Input;

    @FindBy(xpath = "//*[@text='Email']/following-sibling::android.widget.EditText[1]")
    public TextInput Email_Send_Invite_Input;

    @FindBy(xpath = "//*[@text='Invalid email address']")
    public TextInput Invalid_Email_Address_Label;

    @FindBy(xpath = "//*[@text='Send an invite']")
    public TextBlock Send_An_Invite_Screen_Label;

    @FindBy(xpath = "//*[@text='Send']")
    public Button Send_Button;

    @FindBy(xpath = "//*[@text='Invite sent']")
    public TextBlock Invite_Sent_Mess_Label;

    @FindBy(xpath = "//*[@text='Save photo']")
    public Button Save_Photo_Button;

    @FindBy(xpath = "//*[contains(@text,'Email')]/preceding-sibling::android.widget.TextView")
    public TextBlock Error_wrong_here;

    @FindBy(xpath = "//*[@text='Close']")
    public Button Close_Button;

    @FindBy(xpath = "//*[contains(@text,'Enter a name')]")
    public TextInput Search_Name_Input;

    @FindBy(xpath = "//*[@text='Cancel']/parent::android.view.ViewGroup/preceding-sibling::android.widget.EditText")
    public TextInput Re_Search_Name_Share_Button;

    @FindBy(xpath = "(//*[@text='Invite'])[1]")
    public Button Invite_Button;

    @FindBy(xpath = "(//*[@text='Share'])[2]")
    public Button First_Share_Contact_Button;

    @FindBy(xpath = "(//*[@text='Unshare'])[1]")
    public Button First_UnShare_Contact_Button;

//    @FindBy(xpath = "(//*[@text='Sent'])[1]")
    @FindBy(xpath = "(//*[@text='Invite pending'])[1]")
    public TextInput Sent_Status_Label;

    @FindBy(xpath = "//*[@text='\uE670']/following-sibling::android.widget.TextView[contains(@text,'Search')]")
    public Button Search_Item_Photo_Button;

    @FindBy(xpath = "//*[contains(@text,'Show Me Around')]")
    public Button Show_Me_Around_Button;

    @FindBy(xpath = "//*[contains(@text,'I’ll explore on my own')]")
    public Button I_Explore_My_Own_Button;

    @FindBy(xpath = "//*[contains(@text,'Next')]")
    public Button Next_Item_Button;

    @FindBy(xpath = "//*[contains(@text, 'explore your Vault')]")
    public TextBlock Explore_Vault_Text;

    @FindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.widget.TextView[2]")
    public TextBlock Your_Vault_Safely;

    @FindBy(xpath = "//*[@text='Got it']")
    public Button GotIt_Button;

    @FindBy(xpath = "//*[@text='Got It']")
    public Button Ingestion_Got_It_Button;

    @FindBy(xpath = "//*[@text='Give access to your photos']")
    public TextBlock Give_Access_Text;

    @FindBy(xpath = "//*[@text='Please tap on the photos you want to copy into Rosy’s Vault.']")
    public TextBlock Upload_Items_Modal_Text;

    @FindBy(xpath = "//*[@text='CANCEL']")
    public Button POPUP_CANCEL_Button;

    @FindBy(xpath = "//*[@text='Use this photo']")
    public Button Use_This_Photo_Button;

    @FindBy(xpath = "//*[@text='What a lovely profile image!']")
    public TextBlock Text_Update_Avatar;

    @FindBy(xpath = "//*[contains(@text,'getting your content from Google')]")
    public TextBlock Getting_Content_GG_Text;

    @FindBy(xpath = "//*[contains(@text,'getting your content from Dropbox')]")
    public TextBlock Getting_Content_Dropbox_Text;

    @FindBy(xpath = "//*[contains(@text, 'getting your content from Google')]")
    public TextBlock Getting_Content_Text;

    @FindBy(xpath = "//*[contains(@text,'getting your content from Box')]")
    public TextBlock Getting_Content_Box_Text;

    @FindBy(xpath = "//*[contains(@text,'getting your content from One Drive')]")
    public TextBlock Getting_Content_OneDrive_Text;

    @FindBy(xpath = "//*[contains(@text, 'loading your content')]")
    public TextBlock Loading_Content_Text;

    @FindBy(xpath = "//*[contains(@text, 'Changes have been saved')]")
    public TextBlock Saved_Changes_Text;

    @FindBy(xpath = "//*[@text='Google']")
    public Button Google_Sync_Button;

    @FindBy(xpath = "//*[@text='One Drive']")
    public Button OneDrive_Sync_Button;

    @FindBy(xpath = "//*[@text='Box']")
    public Button Box_Sync_Button;

    @FindBy(xpath = "//*[@text='Dropbox']")
    public Button Dropbox_Sync_Button;

    @FindBy(xpath = "//*[contains(@text, 'wants to connect with you')]")
    public TextBlock Want_To_Connect_Text;

    // Onboarding

    @FindBy(xpath = "//*[contains(@text,'Click the selections below')]")
    public TextBlock Click_The_Selections_Below_To_Create_Memeories_ToolTip;

    @FindBy(xpath = "(//*[contains(@text,'Add a Memory to this item')])[1]")
    public TextBlock Add_A_Memory_To_This_Item_Next_Tooltip;

    @FindBy(xpath = "(//*[contains(@text,'You can search this item based')])[1]")
    public TextBlock You_Can_Search_This_Item_Based_Got_It_ToolTip;

    @FindBy(xpath = "(//*[contains(@text,'Tap “Add details” or “Edit Item')])[1]")
    public TextBlock Tap_Add_Details_Or_Edit_Item_Details_Tooltip;

    @FindBy(xpath = "(//*[contains(@text,'Add details so you can remember')])[1]")
    public TextBlock Add_Details_So_You_Can_Remember_Edit_Details_Tooltip;

    @FindBy(xpath = "(//*[contains(@text,'As I learn people you identify')])[1]")
    public TextBlock As_I_Learn_People_You_Indentify_Tooltip;

    @FindBy(xpath = "(//*[contains(@text,'Invite your friends and family')])[1]")
    public TextBlock Invite_Your_Friends_And_Family_Share_Tooltip_1;

    @FindBy(xpath = "(//*[contains(@text,'Invite more friends by tapping “Add.”')])[1]")
    public TextBlock Invite_More_Friends_By_Tapping_Add_Share_Tooltip_2;

    @FindBy(xpath = "//*[@text=\"Export complete\"]")
    public TextBlock Export_Complete_Label;


    @Override
    public boolean isLoaded()
    {
        return true;
    }
}
