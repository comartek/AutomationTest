package core.steps.common.PageSteps;

import core.annotations.StepExt;
import core.pages.BasePage;
import core.pages.MobilePageFactory;
import core.pages.PageFactory;
import core.pages.RosyBasePage;
import core.steps.BaseSteps;
import core.steps.common.UtilSteps.UtilSubSteps;
import core.utils.timeout.TimeoutUtils;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * Created by Akarpenko on 10.01.2018.
 */
public class PageSubSteps extends BaseSteps {

    UtilSubSteps utilSubSteps = new UtilSubSteps();

    @StepExt("open page \"{url}\" in browser")
    public void stepOpenUrlInBrowser(String url) {
        getActiveDriver().navigate().to(url);
        stepAlertAcceptedIfExisted();
    }

    @StepExt("open page \"{url}\" in browser mobile")
    public void stepOpenUrlInBrowserMobile(String url) {
        getActiveMobileDriver().navigate().to(url);
//        stepAlertAcceptedIfExisted();
    }

    @StepExt("open page \"{url}\" in safari browser mobile")
    public void stepOpenUrlInSafariBrowserMobile() {
        getActiveMobileDriver().activateApp("com.apple.mobilesafari");

    }


    @StepExt("page \"{page}\" is loaded")
    public void stepPageIsLoaded(String page) {
        if(getActiveMobileDriver() != null){
            RosyBasePage currentPage = MobilePageFactory.getPage(page, getActiveMobileDriver());
            setCurrentMobilePage(currentPage);
            getCurrentMobilePage().waitUntilLoadingIndicatorIsInvisible(300);
            currentPage.isLoaded();
        }else{
            TimeoutUtils.setTimeOut(300);
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            BasePage currentPage = PageFactory.getPage(page, getActiveDriver());
            setCurrentPage(currentPage);

            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            currentPage.waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
            Assert.assertTrue("Page '" + page + "' has not been loaded", currentPage.isLoaded());
            TimeoutUtils.returnDefaultTimeOut();
            currentPage.afterPageLoad();
        }
    }

    @StepExt("page \"{page}\" is syncing loaded when waiting for \"{time}\" seconds")
    public void stepPageIsLoadedSync(String page, int time) {
        if(getActiveMobileDriver() != null){
            RosyBasePage currentPage = MobilePageFactory.getPage(page, getActiveMobileDriver());
            setCurrentMobilePage(currentPage);
            getCurrentMobilePage().waitUntilLoadingIndicatorIsInvisible(time);
            currentPage.isLoaded();
        }else{
            TimeoutUtils.setTimeOut(time);
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            BasePage currentPage = PageFactory.getPage(page, getActiveDriver());
            setCurrentPage(currentPage);

            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            currentPage.waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
            Assert.assertTrue("Page '" + page + "' has not been loaded", currentPage.isLoaded());
            TimeoutUtils.returnDefaultTimeOut();
            currentPage.afterPageLoad();
        }
    }


    @StepExt("alert accepted if existed")
    public void stepAlertAcceptedIfExisted() {
        Alert alert = getAlert(0);
        if (alert != null) {
            System.err.println("alert text = " + alert.getText());
            alert.accept();
        }
    }

    @StepExt("alert accepted if existed (max wait time = \"{maxWaitTime}\" seconds)")
    public void stepAlertAcceptedIfExistedWithMaxWaitTime(int maxWaitTime) {
        Alert alert = getAlert(maxWaitTime);
        if (alert != null) {
            System.err.println("alert text = " + alert.getText());
            alert.accept();
        }
    }

    @StepExt("alert dismissed if existed")
    public void stepAlertDismissedIfExisted() {
        Alert alert = getAlert(0);
        if (alert != null) {
            System.err.println("alert text = " + alert.getText());
            alert.dismiss();
        }
    }

    @StepExt("alert dismissed if existed (max wait time = \"{maxWaitTime}\" seconds)")
    public void stepAlertDismissedIfExistedWithMaxWaitTime(int maxWaitTime) {
        Alert alert = getAlert(maxWaitTime);
        if (alert != null) {
            System.err.println("alert text = " + alert.getText());
            alert.dismiss();
        }
    }

    private Alert getAlert(int maxWaitTime){
        if(maxWaitTime > 0){
            new WebDriverWait(getActiveDriver(), maxWaitTime)
                    .ignoring(NoAlertPresentException.class)
                    .until(ExpectedConditions.alertIsPresent());
        }
        return ExpectedConditions.alertIsPresent().apply(getActiveDriver());
    }

    @StepExt("current page is still loaded")
    public void stepCurrentPageIsStillLoaded() {
        BasePage currentPage = getCurrentPage();
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        currentPage.waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
        Assert.assertTrue("Current Page is not still loaded", currentPage.isLoaded());
    }

    @StepExt("synchronized with app")
    public void stepSynchronizedWithApp() {
        BasePage currentPage = getCurrentPage();
        TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
        currentPage.waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
    }

    @StepExt("current page frame is reloaded (refreshed)")
    public void stepCurrentPageIsReloadedRefreshed() {
        TimeoutUtils.setTimeOut(30);
        BasePage currentPage = getCurrentPage();
        currentPage.reloadPage();
        TimeoutUtils.returnDefaultTimeOut();
    }



    @StepExt("page invisible \"{page}\" is loaded")
    public void stepInvisiblePageIsLoaded(String page) {
        if(getActiveMobileDriver() != null){
            RosyBasePage currentPage = MobilePageFactory.getPage(page, getActiveMobileDriver());
            setCurrentMobilePage(currentPage);
            getCurrentMobilePage().waitUntilLoadingIndicatorIsInvisible(60);
            currentPage.isLoaded();
        }else{
            TimeoutUtils.setTimeOut(10);
            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            BasePage currentPage = PageFactory.getPage(page, getActiveDriver());
            setCurrentPage(currentPage);

            TimeoutUtils.waitForJStoLoad(TimeoutUtils.loadingTimeout);
            currentPage.waitUntilLoadingIndicatorIsInvisible(TimeoutUtils.loadingTimeout);
            Assert.assertTrue("Page '" + page + "' has not been loaded", currentPage.isLoaded());
            TimeoutUtils.returnDefaultTimeOut();
            currentPage.afterPageLoad();
        }
    }

}
