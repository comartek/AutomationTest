package core.applications;

import com.google.common.collect.ImmutableMap;
import core.utils.evaluationManager.ScriptUtils;
import core.utils.ie.InternetExplorerStateListener;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Alert;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static core.utils.debugmode.DebugModeUtils.*;
import static core.utils.lang.LangUtils.getDefaultLanguage;


/**
 * Created by Akarpenko on 19.10.2017.
 */

/**
 * This is core of framework.
 * Class ApplicationManager loads drivers, initialize instances of webdriver with necessary settings.
 */
public class ApplicationManager {

    private static ThreadLocal<ApplicationManager> instance = new ThreadLocal<>();

    /**
     * Private constructor.  Called while first launch.
     * @see ApplicationManager#loadDrivers()
     * @see ApplicationManager#loadConfig()
     */
    private ApplicationManager(){
        loadDrivers();
        loadConfig();
    }

    /**
     * Basic method of singleton. (ThreadLocal for multithreading support)
     */
    public static ThreadLocal<ApplicationManager> getInstance() {
        if (instance.get() == null)
        {
            instance.set(new ApplicationManager());
        }
        return instance;
    }


    /**
     * List of Applications.
     * @see Application
     */
    public Map<String,Application> Applications;

    /**
     * Active Applications.
     * @see Application
     */
    public Application ActiveApplication;


    /**
     * Get created in ApplicationConfigurator Applications.
     * @see ApplicationConfigurator
     */
    public void loadConfig() {
        Applications = ApplicationConfigurator.getInstance().get().Applications;
    }

    /**
     * Uploading webdrivers for different browsers to system variables.
     */
    public void loadDrivers(){
//        System.setProperty("webdriver.chrome.driver", ".//drv/chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", ".//drv/chromedriver");
        System.setProperty("webdriver.gecko.driver", ".//drv/geckodriver.exe");
        System.setProperty("webdriver.ie.driver", ".//drv/IEDriverServer.exe");
    }

    /**
     * Basic method of ApplicationManager.
     * It initializes webdrivers with specific parameters for browsers and mobile platforms.
     * It checks if an application is already initialized.
     * @param application name of application from file application.json
     */
    public void initializeApplication(Application application) {
        Application app = application;
        if (!app.isInitialized) {
            app.isScenarioPassed = true;
            app.isLoadedFromDebugModeFile = false;
            app.currentPage = null;
            app.currentLogin = "";
            app.language = getDefaultLanguage();
            app.windows = new HashMap<>();
            app.activeWindow = "";

            if (app.drivername.toLowerCase().equals("chrome")) {
                ChromeOptions opt = new ChromeOptions();
                opt.addArguments("--no-sandbox");
                opt.addArguments("use-fake-ui-for-media-stream");
                opt.addArguments("use-fake-device-for-media-stream");
                opt.setExperimentalOption("w3c", false);
                opt.setExperimentalOption("useAutomationExtension", false);
                opt.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                app.driver = new ChromeDriver(opt);
                app.driver.manage().timeouts().implicitlyWait(Long.parseLong(
                        app.capabilities.getCapability("browser.implicitTimeout").toString()), TimeUnit.SECONDS);
                app.driver.manage().timeouts().pageLoadTimeout(Long.parseLong(
                        app.capabilities.getCapability("browser.waitPageLoadedTimeout").toString()), TimeUnit.SECONDS);
                app.driver.manage().window().maximize();
                app.windows.put("MainWindow",app.driver.getWindowHandle());
                app.activeWindow = "MainWindow";
            } else if (app.drivername.toLowerCase().equals("firefox")) {
                FirefoxOptions options=new FirefoxOptions();
                options.addPreference("security.sandbox.content.level", 5);
                app.driver = new FirefoxDriver(options);
                app.driver.manage().timeouts().implicitlyWait(Long.parseLong(
                        app.capabilities.getCapability( "browser.implicitTimeout").toString()), TimeUnit.SECONDS);
                app.driver.manage().timeouts().pageLoadTimeout(Long.parseLong(
                        app.capabilities.getCapability("browser.waitPageLoadedTimeout").toString()), TimeUnit.SECONDS);
                app.driver.manage().window().maximize();

                app.windows.put("MainWindow",app.driver.getWindowHandle());
                app.activeWindow = "MainWindow";
            } else if (app.drivername.toLowerCase().equals("internet explorer")) {
                InternetExplorerOptions options=new InternetExplorerOptions();
                options.ignoreZoomSettings();
//                options.requireWindowFocus();
                options.destructivelyEnsureCleanSession();
                app.driver = new InternetExplorerDriver(options);
                app.driver.manage().timeouts().implicitlyWait(Long.parseLong(
                        app.capabilities.getCapability("browser.implicitTimeout").toString()), TimeUnit.SECONDS);
                app.driver.manage().timeouts().pageLoadTimeout(Long.parseLong(
                        app.capabilities.getCapability("browser.waitPageLoadedTimeout").toString()), TimeUnit.SECONDS);
                app.driver.manage().window().maximize();

                app.windows.put("MainWindow",app.driver.getWindowHandle());
                app.activeWindow = "MainWindow";

                InternetExplorerStateListener.startListening();
            } else if (app.drivername.toLowerCase().equals("android")) {
                try {
                    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                    desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, ScriptUtils.env("platformVersion"));
                    desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, ScriptUtils.env("deviceName"));
//                    desiredCapabilities.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir")+ "/"  + ScriptUtils.env("applicationLink"));
                    desiredCapabilities.setCapability(MobileCapabilityType.NO_RESET, true);
                    desiredCapabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
                    desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
                    desiredCapabilities.setCapability("appWaitDuration", "30000");
                    desiredCapabilities.setCapability("appPackage", ScriptUtils.env("appPackage"));
                    desiredCapabilities.setCapability("appWaitPackage", ScriptUtils.env("appPackage"));
                    desiredCapabilities.setCapability("appActivity", ScriptUtils.env("appActivity"));
                    desiredCapabilities.setCapability("noReset", true);
                    desiredCapabilities.setCapability("newCommandTimeout", 0);
                    URL url = new URL(ScriptUtils.env("appiumHub"));
                    app.mobileDriver = new AndroidDriver(url, desiredCapabilities);
                    app.mobileDriver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else if (app.drivername.toLowerCase().equals("ios")){
                try {
                    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                    desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
                    desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, ScriptUtils.env("platformVersion"));
                    desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, ScriptUtils.env("XCUITest"));
                    desiredCapabilities.setCapability(MobileCapabilityType.UDID, ScriptUtils.env("udid"));
                    desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, ScriptUtils.env("deviceName"));
                    desiredCapabilities.setCapability("bundleId", ScriptUtils.env("bundleId"));
                    desiredCapabilities.setCapability(MobileCapabilityType.NO_RESET, false);
                    desiredCapabilities.setCapability("newCommandTimeout", 0);
                    URL url = new URL(ScriptUtils.env("appiumHub"));
                    app.mobileDriver = new IOSDriver(url, desiredCapabilities);
                    app.mobileDriver.manage().timeouts().implicitlyWait(180, TimeUnit.MINUTES);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (app.driver!=null)
                app.isInitialized = true;
        }
    }

    /**
     * Set active application by name.
     * Try to initialize application (if it is not).
     * @param name name of application from file applications.json
     */
    public void switchToApplication(String name){
        Application application = getApplication(name);
        ActiveApplication = application;
        if (application.isLoadedFromDebugModeFile){
            application.isInitialized = false;
        }
        initializeApplication(application);
    }

    /**
     * Get application from list of Applications.
     * @param name name of application from file applications.json
     */
    public Application getApplication(String name){
        Application application = Applications.get(name);

        if(application != null) {
            return application;
        }
        else {
            throw new RuntimeException(String.format(
                    "Application '%s' has not been found in the list of available applications (applications.json): %s",
                    name, Applications.keySet().toString()));
        }
    }

    /**
     * Close application from list of Applications.
     * @param name name of application from file applications.json
     */
    public void closeApplication(String name)
    {
        Application app = getApplication(name);
        System.out.println("h1111");
        closeApplication(app);
    }
    /**
     * Close application from list of Applications.
     * @param app application from list of Applications
     */
    public void closeApplication(Application app)
    {
        if (app!=null && app.isInitialized) {
            if(!debugModeIsActivated() || shouldCloseBrowserInDebugMode(app)) {
                app.isInitialized = false;
                if (app.driver instanceof FirefoxDriver) {
                    app.driver.get("about:about");
                    Alert alert = ExpectedConditions.alertIsPresent().apply(app.driver);
                    if (alert != null)
                        alert.accept();
                }
//                app.driver.close();
                app.driver.quit();
                try {
                    System.out.println("test quit app");
                    app.driver.quit();
                    app.mobileDriver.closeApp();
                } catch (Exception ignored) {
                    //do nothing
                    System.out.println("e2e23e3");
                }
                app.driver = null;
            }
        }
    }

    /**
     * Close active application.
     */
    public void closeActiveApplication()
    {
        if (ActiveApplication!=null)
            closeApplication(ActiveApplication.name);
    }
    /**
     * Close all applications.
     */
    public void closeAllApplications()
    {
        for(Map.Entry<String, Application> app : Applications.entrySet()){

            if (app.getValue()!=null)
                closeApplication(app.getValue());
        }
    }
}

