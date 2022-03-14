package core.utils.debugmode;

import com.codepine.api.testrail.model.Run;
import core.applications.Application;
import core.applications.ApplicationManager;
import core.pages.PageFactory;
import core.utils.datapool.DataPool;
import core.utils.evaluationManager.EvaluationManager;
import core.utils.lang.LangUtils;
import core.utils.timeout.TimeoutUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.remote.http.JsonHttpCommandCodec;
import org.openqa.selenium.remote.http.JsonHttpResponseCodec;
import org.openqa.selenium.remote.http.W3CHttpCommandCodec;
import org.openqa.selenium.remote.http.W3CHttpResponseCodec;
import org.openqa.selenium.remote.service.DriverCommandExecutor;
import org.openqa.selenium.remote.service.DriverService;

import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class DebugModeUtils {

    private static final String DELIMITER = ";;; "; //do not use RegExp special symbols here
    private static String debugModeFilePath = "DebugMode.properties";

    public static boolean debugModeIsActivated(){
        return Boolean.valueOf(System.getProperty("debugModeIsActivated"));
    }

    public static boolean shouldCloseBrowserInDebugMode() {
        return shouldCloseBrowserInDebugMode(ApplicationManager.getInstance().get().ActiveApplication);
    }

    public static boolean shouldCloseBrowserInDebugMode(Application app){
        if(debugModeIsActivated()){
            if(Boolean.valueOf(System.getProperty("closeBrowserIfTestIsPassedInDebugMode"))){
                return app!=null && app.isScenarioPassed;
            }
        }
        return false;
    }

    public static void writeVariablesIntoADebugModeFile(){
        String message = "";
        try {
            Properties properties = new Properties();
            properties.setProperty("variables", mapToString(EvaluationManager.getVariables()));
            setDebugModeFileProperties(properties);
            message = "Variables have been successfully saved into the debug mode file.";
        } catch (Exception ignored){
            message = "Failed to save variables into a debug mode file. " + ignored.getClass().getSimpleName() + ": " + ignored.getMessage();
        }
        printDebugModeMessage(message);
    }

    public static void writeDatapoolSectionIntoADebugModeFile(){
        String message = "";
        try {
            Properties properties = new Properties();
            properties.setProperty("DatapoolSectionType", DataPool.currentDatapoolSectionType.toString());
            properties.setProperty("DatapoolSectionName", DataPool.currentSectionName);
            setDebugModeFileProperties(properties);
            message = "Datapool section have been successfully saved into the debug mode file.";
        } catch (Exception ignored){
            message = "Failed to save datapool section into a debug mode file. " + ignored.getClass().getSimpleName() + ": " + ignored.getMessage();
        }
        printDebugModeMessage(message);
    }

    public static void writeApplicationStateIntoADebugModeFile(Application app){
        String message = "";
        try {
            Properties properties = new Properties();

            //app.*
            properties.setProperty("app.name", app.name);
            properties.setProperty("app.drivername", app.drivername);
            properties.setProperty("app.language", app.language.toString());
            properties.setProperty("app.currentLogin", app.currentLogin);
            properties.setProperty("app.isInitialized", String.valueOf(app.isInitialized));
            properties.setProperty("app.currentPage", app.currentPage == null ? nullStringText() : app.currentPage.getClass().getSimpleName());
            properties.setProperty("app.windows", mapToString(app.windows));
            properties.setProperty("app.activeWindow", app.activeWindow);
            properties.setProperty("app.capabilities", mapToString(app.capabilities.asMap()));

            //app.driver
            RemoteWebDriver remoteWebDriver = ((RemoteWebDriver) app.driver);
            HttpCommandExecutor executor = (HttpCommandExecutor) remoteWebDriver.getCommandExecutor();
            URL url = executor.getAddressOfRemoteServer();
            SessionId session_id = remoteWebDriver.getSessionId();
            properties.setProperty("app.driver.url", url.toString());
            properties.setProperty("app.driver.session_id", session_id.toString());
            properties.setProperty("app.driver.capabilities", mapToString(remoteWebDriver.getCapabilities().asMap()));

            setDebugModeFileProperties(properties);

            message = "Application state has been successfully saved into the debug mode file.";
        } catch (Exception ignored){
            message = "Failed to save application state into a debug mode file. " + ignored.getClass().getSimpleName() + ": " + ignored.getMessage();
        }
        printDebugModeMessage(message);
    }

    public static void loadDatapoolSectionFromADebugModeFile(){
        String message = "";
        try{
            Properties properties = getDebugModeFileProperties();
            DataPool.SectionType sectionType = DataPool.SectionType.valueOf(properties.getProperty("DatapoolSectionType"));
            String sectionName = properties.getProperty("DatapoolSectionName");
            if(sectionType == DataPool.SectionType.ROOT){
                DataPool.getInstance().switchToRoot();
            } else if (sectionType == DataPool.SectionType.SECTION_WITH_DATASET){
                DataPool.getInstance().switchToSection(sectionName);
            } else if (sectionType == DataPool.SectionType.SECTION_WITHOUT_DATASET){
                DataPool.getInstance().switchToSectionWithoutDataset(sectionName);
            } else {
                throw new RuntimeException("DebugModeUtils - Unknown DataPool.SectionType = " + sectionType);
            }
            message = "Datapool section have been successfully loaded from a debug mode file.";
        } catch (Exception ignored){
            message = "Failed to load datapool section from a debug mode file. " + ignored.getClass().getSimpleName() + ": " + ignored.getMessage();
        }
        printDebugModeMessage(message);
    }

    public static void loadVariablesFromADebugModeFile(){
        String message = "";
        try{
            Properties properties = getDebugModeFileProperties();
            EvaluationManager.setVariables(stringToMap(properties.getProperty("variables")));
            message = "Variables have been successfully loaded from a debug mode file.";
        } catch (Exception ignored){
            message = "Failed to load variables from a debug mode file. " + ignored.getClass().getSimpleName() + ": " + ignored.getMessage();
        }
        printDebugModeMessage(message);
    }

    public static void loadApplicationStateFromADebugModeFile(){
         String message = "";
         try{
             Properties properties = getDebugModeFileProperties();

             //app.*
             Application app = ApplicationManager.getInstance().get().getApplication(properties.getProperty("app.name"));
             ApplicationManager.getInstance().get().ActiveApplication = app;
             app.isScenarioPassed = true;
             app.isLoadedFromDebugModeFile = true;
             app.currentLogin = properties.getProperty("app.currentLogin");
             app.language = LangUtils.Language.valueOf(properties.getProperty("app.language"));
             app.capabilities = new DesiredCapabilities(stringToMap(properties.getProperty("app.capabilities")));

             //app.driver
             HashMap<String, String> capsMap = stringToMap(properties.getProperty("app.driver.capabilities"));
             Capabilities driverCaps = new DesiredCapabilities(capsMap);
             app.driver = createDriverFromSessionAndUrl(getWebDriverType(app.drivername), properties.getProperty("app.driver.session_id"), properties.getProperty("app.driver.url"), new DesiredCapabilities());

             //app.windows
             app.windows = stringToMap(properties.getProperty("app.windows"));
             app.activeWindow = properties.getProperty("app.activeWindow");
             String windowHandle = app.windows.get(app.activeWindow);
             if(!app.driver.getWindowHandle().equals(windowHandle))
                 app.driver.switchTo().window(windowHandle);

             //currentPage
             String currPage = properties.getProperty("app.currentPage");
             app.currentPage = currPage.equals(nullStringText()) ? null : PageFactory.getPage(currPage, app.driver);

             //Always supposed to be the last line!!!
             app.isInitialized = Boolean.valueOf(properties.getProperty("app.isInitialized"));
             message = "Application state has been successfully loaded from a debug mode file. Application name: " + app.name;
         } catch (Exception ignored){
             message = "Failed to load application state from a debug mode file. " + ignored.getClass().getSimpleName() + ": " + ignored.getMessage();
         }
         printDebugModeMessage(message);
    }

    public static void clearDebugModeFile(){
        try {
            File file = new File(debugModeFilePath);
            if(file.exists()){
                PrintWriter pw = new PrintWriter(debugModeFilePath);
                pw.close();
            }
        } catch (FileNotFoundException ignored){
            printDebugModeMessage("Failed to clear the debug mode file. " + ignored.getClass().getSimpleName() + ": " + ignored.getMessage());
        }
    }

    private static void printDebugModeMessage(String message){
        System.out.println("[DebugMode]--------------------------------------------------");
        System.out.println("[DebugMode] " + message);
        System.out.println("[DebugMode]--------------------------------------------------");
    }

    private static String nullStringText(){
        return "{{{NULL}}}";
    }

    private static String mapToString(Map map){
        Object[] a = map.entrySet().toArray();

        //Arrays.toString with another delimiter
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(a[i]);
            if (i == iMax) {
                b.append(']');
                break;
            }
            b.append(DELIMITER);
        }
        return b.toString();
    }

    private static HashMap<String,String> stringToMap(String map){
        String[] strings = map.replace("[", "").replace("]", "").split(DELIMITER);
        HashMap<String, String> result = new HashMap<>();
        for (String entry : strings) {
            int delimiterIndex = entry.indexOf("=");
            String key = entry.substring(0,delimiterIndex);
            String value = entry.substring(delimiterIndex + 1);
            result.put(key, value);
        }
        return result;
    }


    public static Properties getDebugModeFileProperties() {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(debugModeFilePath));
        } catch (FileNotFoundException ignored){
            //ignored
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    public static void setDebugModeFileProperty(String name, String value){
        Properties props = getDebugModeFileProperties();
        props.setProperty(name, value);
        try {
            File file = new File(debugModeFilePath);
            if(!file.exists()){
                createDebugModeFile();
            }
            OutputStream out = new FileOutputStream(file);
            props.store(out, "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createDebugModeFile(){
        Path path = Paths.get(debugModeFilePath);
        if(!Files.exists(path)){
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setDebugModeFileProperties(Properties properties){
        Properties currentProps = getDebugModeFileProperties();
        currentProps.putAll(properties);
        try {
            File file = new File(debugModeFilePath);
            if(!file.exists()){
                createDebugModeFile();
            }
            OutputStream out = new FileOutputStream(file);
            currentProps.store(out, "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkWebDriverIsAlive(WebDriver webDriver) {
        final String[] handle = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    handle[0] = webDriver.getWindowHandle();
                } catch (NoSuchWindowException ignored){
                    /*ignored*/
                }
            }
        });
        thread.start();
        TimeoutUtils.sleep(1);
        if(thread.isAlive()) {
            thread.interrupt();
            thread.stop();
            throw new RuntimeException("Current web driver is not alive (driver methods call will lead to thread lock)");
        }
    }

    public static WebDriverType getWebDriverType(String applicationDriverName){
        if(applicationDriverName.equals("Chrome")){
            return WebDriverType.ChromeDriver;
        } else if(applicationDriverName.equals("Internet Explorer")){
            return WebDriverType.InternetExplorerDriver;
        } else if(applicationDriverName.equals("Firefox")){
            return WebDriverType.FirefoxDriver;
        } else {
            throw new RuntimeException("Can not getWebDriverType based on String: " + applicationDriverName);
        }
    }

    public enum WebDriverType {
        InternetExplorerDriver,
        ChromeDriver,
        FirefoxDriver
    }


    public static RemoteWebDriver createDriverFromSessionAndUrl(WebDriverType driverType, String sessionId, String url, Capabilities desiredCapabilities){

//        CommandExecutor httpCommandExecutor = new HttpCommandExecutor(getURL(url)) {
//
//            @Override
//            public Response execute(Command command) throws IOException {
//                Response response = null;
//                if (command.getName() == "newSession") {
//                    response = new Response();
//                    response.setSessionId(sessionId);
//                    response.setStatus(0);
//                    response.setValue(Collections.<String, String>emptyMap());
//
//                    try {
//                        Field commandCodec = null;
//                        commandCodec = this.getClass().getSuperclass().getDeclaredField("commandCodec");
//                        commandCodec.setAccessible(true);
//                        commandCodec.set(this, new W3CHttpCommandCodec());
////                        commandCodec.set(this, new JsonHttpCommandCodec());
//
//                        Field responseCodec = null;
//                        responseCodec = this.getClass().getSuperclass().getDeclaredField("responseCodec");
//                        responseCodec.setAccessible(true);
//                        responseCodec.set(this, new W3CHttpResponseCodec());
////                        responseCodec.set(this, new JsonHttpResponseCodec());
//                    } catch (NoSuchFieldException | IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    response = super.execute(command);
//                }
//                return response;
//            }
//        };

        DriverService service = getDriverService(getURL(url).getPort(), driverType);

        CommandExecutor driverCommandExecutor = new DriverCommandExecutor(service) {

            @Override
            public Response execute(Command command) throws IOException {
                Response response = null;
                if (command.getName() == "newSession") {
                    response = new Response();
                    response.setSessionId(sessionId);
                    response.setStatus(0);
                    response.setValue(Collections.<String, String>emptyMap());

                    try {
                        Field service = this.getClass().getSuperclass().getDeclaredField("service");
                        service.setAccessible(true);
                        DriverService driverService = (DriverService)service.get(this);

                        Field commandCodec = null;
                        commandCodec = this.getClass().getSuperclass().getSuperclass().getDeclaredField("commandCodec");
                        commandCodec.setAccessible(true);
                        if(driverService instanceof ChromeDriverService) {
                            commandCodec.set(this, new JsonHttpCommandCodec());
                        }else {
                            commandCodec.set(this, new W3CHttpCommandCodec());
                        }

                        Field responseCodec = null;
                        responseCodec = this.getClass().getSuperclass().getSuperclass().getDeclaredField("responseCodec");
                        responseCodec.setAccessible(true);
                        if(driverService instanceof ChromeDriverService) {
                            responseCodec.set(this, new JsonHttpResponseCodec());
                        }else {
                            responseCodec.set(this, new W3CHttpResponseCodec());
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                } else {
                    response = super.execute(command);
                }
                return response;
            }
        };

        RemoteWebDriver remoteWebDriver = null;

//        // Initialization of specific drivers instead of RemoteWebDriver does not work because we can`t pass our custom CommandExecutor (with overridden execute() method).
//        // In case if specific drivers are necessary, then maybe we need to add Driver`s source code and in this code create extra constructor which can receive our custom CommandExecutor
//        if (driverType == WebDriverType.InternetExplorerDriver) {
//            remoteWebDriver = new InternetExplorerDriver((InternetExplorerDriverService)service, desiredCapabilities);
//        } else if (driverType == WebDriverType.ChromeDriver) {
//            remoteWebDriver = new ChromeDriver((ChromeDriverService)service, desiredCapabilities);
//        } else if (driverType == WebDriverType.FirefoxDriver ){
//            remoteWebDriver = new FirefoxDriver((GeckoDriverService)service, desiredCapabilities);
//        } else {
//            throw new RuntimeException("No logic to create DriverService based on WebDriverType = " + driverType);
//        }

//        remoteWebDriver = new RemoteWebDriver(httpCommandExecutor, desiredCapabilities);
        remoteWebDriver = new RemoteWebDriver(driverCommandExecutor, desiredCapabilities);
        checkWebDriverIsAlive(remoteWebDriver);

        return remoteWebDriver;
    }

    private static DriverService getDriverService(int port, WebDriverType driverType){
        DriverService service = null;
        if (driverType == WebDriverType.InternetExplorerDriver) {
            service = (InternetExplorerDriverService) ((InternetExplorerDriverService.Builder) (new InternetExplorerDriverService.Builder()).usingPort(port)).build();
        } else if (driverType == WebDriverType.ChromeDriver) {
            service = (ChromeDriverService) ((ChromeDriverService.Builder) (new ChromeDriverService.Builder()).usingPort(port)).build();
        } else if (driverType == WebDriverType.FirefoxDriver ){
            service = (GeckoDriverService) ((GeckoDriverService.Builder) (new GeckoDriverService.Builder()).usingPort(port)).build();
        } else {
            throw new RuntimeException("No logic to create DriverService based on WebDriverType = " + driverType);
        }
        return service;
    }

    private static URL getURL(String url){
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


//    //-------------------------------------------
//    //TEST RECONNECTION TO THE DRIVERS
//    //-------------------------------------------
//    public static void main(String [] args) {
//        createDebugModeFile();
//        loadDrivers();
//
//        RemoteWebDriver driver = new FirefoxDriver();
//        WebDriverType webDriverType = null;
//        if(driver instanceof FirefoxDriver){
//            webDriverType = WebDriverType.FirefoxDriver;
//        }else if(driver instanceof ChromeDriver){
//            webDriverType = WebDriverType.ChromeDriver;
//        }else if(driver instanceof InternetExplorerDriver){
//            webDriverType = WebDriverType.InternetExplorerDriver;
//        }
//        HttpCommandExecutor executor = (HttpCommandExecutor) driver.getCommandExecutor();
//        URL url = executor.getAddressOfRemoteServer();
//        SessionId session_id = driver.getSessionId();
//        setDebugModeFileProperty("url", url.toString());
//        setDebugModeFileProperty("session_id", session_id.toString());
//
//
//        driver.get("http://www.yandex.ru");
//        WebElement elem = driver.findElement(By.xpath("//input[@id='text']"));
//        boolean b1 = elem.isDisplayed();
//        elem.sendKeys("ABC");
//
//        RemoteWebDriver driver2 = createDriverFromSessionAndUrl(webDriverType, session_id.toString(), url.toString(), new DesiredCapabilities());
//        WebElement elem2 = driver2.findElement(By.xpath("//input[@id='text']"));
//        elem2.sendKeys("XYZ");
//        boolean b2 = elem2.isDisplayed();
//        driver2.get("http://www.google.com");
//    }
//
//    private static void loadDrivers(){
//        System.setProperty("webdriver.chrome.driver", ".//drv/chromedriver.exe");
//        System.setProperty("webdriver.gecko.driver", ".//drv/geckodriver.exe");
//        System.setProperty("webdriver.ie.driver", ".//drv/IEDriverServer.exe");
//    }
}
