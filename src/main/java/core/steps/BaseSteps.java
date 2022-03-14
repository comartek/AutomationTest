package core.steps;

import core.annotations.StepExt;
import core.applications.Application;
import core.applications.ApplicationManager;
import core.pages.BaseMobilePage;
import core.pages.BasePage;
import core.utils.assertExt.AssertExt;
import io.appium.java_client.AppiumDriver;
import org.jbehave.core.model.ExamplesTable;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Akarpenko on 08.11.2017.
 */
public class BaseSteps {
    public static Application getActiveApplication() {
        return ApplicationManager.getInstance().get().ActiveApplication;
    }

    public static WebDriver getActiveDriver(){
        return  ApplicationManager.getInstance().get().ActiveApplication.driver;
    }
    public static AppiumDriver getActiveMobileDriver(){
        return ApplicationManager.getInstance().get().ActiveApplication.mobileDriver;
    }

    public static BasePage getCurrentPage(){
        return ApplicationManager.getInstance().get().ActiveApplication.currentPage;
    }

    public static BaseMobilePage getCurrentMobilePage(){
        return ApplicationManager.getInstance().get().ActiveApplication.currentMobilePage;
    }

    public static void setCurrentPage(BasePage page){
        ApplicationManager.getInstance().get().ActiveApplication.currentPage = page;
    }

    public static void setCurrentMobilePage(BaseMobilePage page){
        ApplicationManager.getInstance().get().ActiveApplication.currentMobilePage = page;
    }



       public static Map<String, String> convertExampleTableToMap(ExamplesTable table,String nameParam,String valueParam){

        Map<String,String> map = new HashMap<>();


        for (Map<String, String> row : table.getRows()) {
            map.put(row.get(nameParam),row.get(valueParam));
        }

        return map;
    }

    // Use this to call substeps from a step if you don't want execution to fail after first error
    public void softAssertSubsteps(Action... actions){
           for (Action action:actions) {
               try {
                   action.execute();
               } catch (Throwable t) {
                   if (!AssertExt.getSoftAssertMode())
                       throw t;
                   else {
                       AssertExt.getInterceptedErrorsList().getLast().add(t);
                       AssertExt.setCountInterceptedErrors(AssertExt.getCountInterceptedErrors() + 1);
                   }
               }
           }
    }

    //Use this to wrap an arbitrary code block into a substep
    @StepExt(value = "{stepText}",hideParams = true)
    public void substep(String stepText, Action action) {
//        try {
//            action.execute();
//        } catch (Throwable t) {
//            if (!AssertExt.getSoftAssertMode())
//                throw t;
//        }
        action.execute();
    }

    public interface Action{
           void execute();
    }




}
