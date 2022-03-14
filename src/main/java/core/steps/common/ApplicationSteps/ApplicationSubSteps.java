package core.steps.common.ApplicationSteps;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.annotations.StepExt;
import core.annotations.UseFullDesktopScreenshots;
import core.applications.Application;
import core.applications.ApplicationManager;
import core.steps.BaseSteps;
import core.steps.common.UtilSteps.UtilSubSteps;
import core.utils.debugmode.DebugModeUtils;
import core.utils.report.ReportUtils;
import core.utils.timeout.TimeoutUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import static core.utils.debugmode.DebugModeUtils.debugModeIsActivated;
import static java.lang.Runtime.getRuntime;

/**
 * Created by Akarpenko on 09.01.2018.
 */
public class ApplicationSubSteps extends BaseSteps {

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("application \"{application}\" is opened")
    public void stepOpenApplication(String application) {
        ApplicationManager.getInstance().get().switchToApplication(application);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("close application \"{application}\"")
    public void stepCloseApplication(String application){
        ApplicationManager.getInstance().get().closeApplication(application);
    }

    @UseFullDesktopScreenshots(ReportUtils.FullDesktopScreenshotMode.DO_BOTH_WEBDRIVER_AND_FULL_DESKTOP_SCREENSHOTS)
    @StepExt("switch to window \"{window}\"")
    public void stepSwitchToWindow(String window) {
        Application app = getActiveApplication();
        HashMap activeWindows = app.windows;

        if (activeWindows.containsKey(window)) {
            getActiveDriver().switchTo().window(activeWindows.get(window).toString());
            app.activeWindow = window;
        } else {
            String newWindowHandle=null;
            //wait for some more time to stabilize IE
            if (getActiveDriver() instanceof InternetExplorerDriver){
                new UtilSubSteps().stepWaitFor(2);
            }
            for (int i=0;i<3;i++) {
                for (String handle : getActiveDriver().getWindowHandles()) {
                    if (!activeWindows.containsValue(handle))
                        newWindowHandle = handle;
                }
                if (newWindowHandle!=null)
                    break;
                else {
                    TimeoutUtils.sleep(5);
                    Logger.getRootLogger().info("Window not found, retrying..");
                }
            }
            if (newWindowHandle ==null)
                throw new RuntimeException("window not found");
            else
            {
                activeWindows.put(window, newWindowHandle);
                getActiveDriver().switchTo().window(newWindowHandle);
                app.activeWindow = window;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                getActiveDriver().manage().window().maximize();
            }
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("close window \"{window}\"")
    public void stepCloseWindow(String window) {

        HashMap activeWindows = getActiveApplication().windows;

        if (activeWindows.containsKey(window)) {
            getActiveDriver().switchTo().window(activeWindows.get(window).toString());
            getActiveDriver().close();
            activeWindows.remove(window);
            if (debugModeIsActivated()) {
                DebugModeUtils.setDebugModeFileProperty("windows", activeWindows.toString());
            }
        } else {
            throw new RuntimeException("PRINT SOME INFO HERE");
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("current window is closed")
    public void stepCheckCurrentWindowIsClosed() {

        try {
            getActiveDriver().getTitle();
        }
        catch (NoSuchWindowException exception){
            return;
        }
        Assert.fail("Window is not closed when it is supposed to");

    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("maximize current window")
    public void stepMaximizeCurrentWindow() {
        getActiveDriver().manage().window().maximize();
        ((JavascriptExecutor)getActiveDriver()).executeScript("window.resizeTo(1920, 1080);");


    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("maximize current window")
    public void stepActivateCurrentWindow() {
        ActivateAppUsingVbs(getActiveDriver().getTitle());
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("current URL is added to Internet Explorer compatibility view setting")
    public void stepCurrentURLAddedToIECompatibilityViewSetting() {
        WebDriver driver = getActiveDriver();
        Robot rob = null;
        try {
            rob = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        // Activate App 1st time and press ALT, then Escape (if 1st app activation is not present for some reason 2nd app activation fails)
        ActivateAppUsingVbs(driver.getTitle());
        ((JavascriptExecutor) driver).executeScript("window.focus();");
        rob.keyPress(KeyEvent.VK_TAB);
        rob.keyRelease(KeyEvent.VK_TAB);
        TimeoutUtils.sleep(1);
        rob.keyPress(KeyEvent.VK_ENTER);
        rob.keyRelease(KeyEvent.VK_ENTER);
        TimeoutUtils.sleep(1);

        rob.keyPress(KeyEvent.VK_ALT);
        rob.keyRelease(KeyEvent.VK_ALT);
        TimeoutUtils.sleep(1);
        rob.keyPress(KeyEvent.VK_ESCAPE);
        rob.keyRelease(KeyEvent.VK_ESCAPE);
        TimeoutUtils.sleep(1);
        ReportUtils.attachFullDesktopScreenshot("before compatibility view setting modification");


        // Activate App 2nd time and press ALT + X to open Internet Explorer "Tools" widget
        ActivateAppUsingVbs(driver.getTitle());
        ((JavascriptExecutor) driver).executeScript("window.focus();");
        rob.keyPress(KeyEvent.VK_ALT);
        rob.keyPress(KeyEvent.VK_X);
        rob.keyRelease(KeyEvent.VK_X);
        rob.keyRelease(KeyEvent.VK_ALT);
        TimeoutUtils.sleep(1);

        //Choose "Compatibility View Settings" in the "Tools" widget
        for (int i = 1; i <= 9; i++) {
            rob.keyPress(KeyEvent.VK_DOWN);
            rob.keyRelease(KeyEvent.VK_DOWN);
        }
        rob.keyPress(KeyEvent.VK_ENTER);
        rob.keyRelease(KeyEvent.VK_ENTER);
        TimeoutUtils.sleep(1);

        //Press "Add" button in the "Compatibility View Settings" window (in order to add current application URL and activate compatibility mode for it)
        rob.keyPress(KeyEvent.VK_TAB);
        rob.keyRelease(KeyEvent.VK_TAB);
        TimeoutUtils.sleep(1);
        rob.keyPress(KeyEvent.VK_ENTER);
        rob.keyRelease(KeyEvent.VK_ENTER);
        TimeoutUtils.sleep(1);
        ReportUtils.attachFullDesktopScreenshot("compatibility view setting modification");
        rob.keyPress(KeyEvent.VK_ESCAPE);
        rob.keyRelease(KeyEvent.VK_ESCAPE);
    }

    public static void ActivateAppUsingVbs(String appName){
        try {
            File file = File.createTempFile("AppActivate",".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);

            String vbs = "" +
                    "Set objShell = CreateObject(\"wscript.shell\")\n" +
                    "objShell.AppActivate(\"" + appName + "\")";
            fw.write(vbs);
            fw.close();
            Process p = getRuntime().exec("wscript " + file.getPath());
            p.waitFor();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("set registry key FEATURE_BROWSER_EMULATION value iexplore.exe = 9999")
    public void stepSetRegistryKeyForIexplore() {
        try {
            getRuntime().exec("reg ADD \"HKCU\\Software\\Microsoft\\Internet Explorer\\Main\\FeatureControl\\FEATURE_BROWSER_EMULATION\" /v iexplore.exe /t REG_DWORD /d 9999 /f");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NeverTakeAllureScreenshotsForThisStep
    @StepExt("process \"{processName}\" is killed")
    public void stepProcessIsKilled(String processName) {
        try {
            getRuntime().exec("taskkill /F /IM " + processName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
