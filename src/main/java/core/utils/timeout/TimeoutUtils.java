package core.utils.timeout;

import core.applications.ApplicationManager;
import core.utils.evaluationManager.ScriptUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Duration;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import core.steps.BaseSteps;

import java.util.concurrent.TimeUnit;

/**
 * Created by Akarpenko on 10.11.2017.
 */
public class TimeoutUtils extends BaseSteps {

    static private Logger logger = Logger.getLogger("DEBUG_INFO");

    private static long implicitlyWaitTimeoutInSeconds = -1;

    public static void setTimeOut(Long timeOutSeconds){
        implicitlyWaitTimeoutInSeconds = timeOutSeconds;
        try{
            if(getActiveMobileDriver() != null){
                ApplicationManager.getInstance().get().ActiveApplication.mobileDriver.manage().timeouts().implicitlyWait(timeOutSeconds, TimeUnit.SECONDS);
            }else{
                ApplicationManager.getInstance().get().ActiveApplication.driver.manage().timeouts().implicitlyWait(timeOutSeconds, TimeUnit.SECONDS);
            }
        }catch (UnhandledAlertException ignored){
            /*do nothing*/
        }
    }
    public static void setTimeOut(Integer timeOutSeconds){
        setTimeOut(Long.valueOf(timeOutSeconds));
    }
    public static void setTimeOut(Duration timeOut){
        setTimeOut(timeOut.in(TimeUnit.SECONDS));
    }
    public static void setTimeOut(Integer timeOut, TimeUnit timeUnit){
        Duration duration = new Duration(timeOut, timeUnit);
        setTimeOut(duration.in(TimeUnit.SECONDS));
    }

    public static void returnDefaultTimeOut(){
        long value = Long.parseLong(ApplicationManager.getInstance().get().ActiveApplication.capabilities.getCapability("browser.implicitTimeout").toString());
        setTimeOut(value);
    }

    public static long getTimeOut(){
        return implicitlyWaitTimeoutInSeconds >= 0 ? implicitlyWaitTimeoutInSeconds : Long.parseLong(ApplicationManager.getInstance().get().ActiveApplication.capabilities.getCapability("browser.implicitTimeout").toString());
    }

    public static int loadingTimeoutDefault = 300;
    public static int loadingTimeout = loadingTimeoutDefault;

    public static boolean waitForJStoLoad(int maxWaitTimeInSeconds) {

        WebDriverWait wait = new WebDriverWait(ApplicationManager.getInstance().get().ActiveApplication.driver, maxWaitTimeInSeconds);

        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    logger.debug("jquery="+((JavascriptExecutor)driver).executeScript("return jQuery.active"));
                    return ((Long)((JavascriptExecutor)driver).executeScript("return jQuery.active") == 0);
                }
                catch (Exception e) {
                    //do not log this kind of exceptions because if a site does not use jQuery at all, it would throw on every call
//                    Logger.getRootLogger().error(e);
                    return true;
                }
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                logger.debug("document="+((JavascriptExecutor)driver).executeScript("return document.readyState"));
                return ((JavascriptExecutor)driver).executeScript("return document.readyState")
                        .toString().equals("complete");
                }
                catch (Exception e) {
                    Logger.getRootLogger().error(e);
                    return true;
                }
            }
        };

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }


    public static void sleep(long time, TimeUnit unit){
        long timeInMillis = unit.toMillis(time);
        try {
            Thread.sleep(timeInMillis);
        }
        catch (Exception ignored){
            //Exception is ignored
        }
    }

    public static void sleep(long timeInSeconds){
        sleep(timeInSeconds, TimeUnit.SECONDS);
    }


}
