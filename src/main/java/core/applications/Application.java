package core.applications;

import core.pages.BasePage;
import core.pages.BaseMobilePage;
import core.utils.lang.LangUtils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;

/**
 * Class Application with properties
 * isInitialized - boolean property,
 */
public class Application {

    /**
     * name of application from file application.json
     */
    public String name;
    /**
     * full name of webdriver: Chrome, Internet Explorer, Firefox, Appium
     */
    public String drivername;
    /**
     * to check or set if application is initialized
     */
    public boolean isInitialized;
    /**
     * webdriver of application
     */
    public WebDriver driver;
    /**
     * webdriver of application
     */
    public AppiumDriver mobileDriver;
    /**
     * class of page, where we are in web application now
     */
    public BasePage currentPage;

    public BaseMobilePage currentMobilePage;
    /**
     * login name of the currently logged in user
     */
    public String currentLogin;
    /**
     * capabilities, which are set in file applications.json
     */
    public DesiredCapabilities capabilities;
    /**
     * language of web application interface
     */
    public LangUtils.Language language;

    /**
     * Map of user-defined window/tab names to actual window handles
     */
    public HashMap<String,String> windows=new HashMap<>();

    /**
     * String name of active window
     */
    public String activeWindow;

    /**
     * to check if application has been loaded from DebugMode file
     */
    public boolean isLoadedFromDebugModeFile;

    /**
     * to check if scenario is passed (no Errors or Exceptions)
     */
    public boolean isScenarioPassed;
}
