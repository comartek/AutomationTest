package core.winium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Runtime.getRuntime;

public class WiniumUtils {
    public static final String WINIUM_DRIVER_PATH = ".\\drv\\Winium.Desktop.Driver.exe";

    public static Process startWiniumProcess(){
        try {
//            getRuntime().exec("taskkill /F /IM Winium.Desktop.Driver.exe");
            ProcessBuilder pro = new ProcessBuilder(WINIUM_DRIVER_PATH);
            return pro.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static WebDriver getWiniumDriver(){
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("debugConnectToRunningApp","true");
        return getWiniumDriver(cap);
    }

    public static WebDriver getWiniumDriver(String applicationPath){
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("app",applicationPath);
        return getWiniumDriver(cap);
    }

    public static WebDriver getWiniumDriver(DesiredCapabilities caps){
        try {
            return new RemoteWebDriver(new URL("http://localhost:9999"),caps);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


}
