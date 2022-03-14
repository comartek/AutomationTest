package core.steps.hooks;

import core.applications.Application;
import core.applications.ApplicationManager;
import org.apache.commons.io.FileUtils;
import org.jbehave.core.annotations.AfterStory;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.BeforeStory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static core.utils.debugmode.DebugModeUtils.*;

/**
 * Created by Akarpenko on 08.11.2017.
 */
public class Hooks {

    @BeforeStories
    public void prepareData() {
    }

    @BeforeStories
    public void clearOutput() {
        try {
            FileUtils.deleteDirectory(new File("target/output/"));
        } catch (IOException e) {

        }
    }


    @BeforeStory
    public void setLanguage() {
//        String property = LangUtils.getDefaultLanguage() == LangUtils.Language.EN_US ?
//                System.getProperty("ocb.url.en_us") : System.getProperty("ocb.url.vi_vn");
//        System.setProperty("ocb.url", property);
    }

    @BeforeStory
    public void getVariablesAndApplicationIfDebugModeIsActivated() {
        if(debugModeIsActivated()){
            loadVariablesFromADebugModeFile();
            loadDatapoolSectionFromADebugModeFile();
            loadApplicationStateFromADebugModeFile();
        }
    }


    @BeforeScenario
    public void setScenarioRunStatusToDefault(){
        for(Application app : ApplicationManager.getInstance().get().Applications.values()){
            if (app.isInitialized) {
                app.isScenarioPassed = true;
            }
        }
    }

    @AfterStory
    public void saveVariablesAndApplicationIfDebugModeIsActivated(){
        if(debugModeIsActivated()) {
            clearDebugModeFile();
            writeVariablesIntoADebugModeFile();
            writeDatapoolSectionIntoADebugModeFile();
            for(Application app : ApplicationManager.getInstance().get().Applications.values()){
                if (app!=null && app.isInitialized) {
                    writeApplicationStateIntoADebugModeFile(app);
                }
            }
        }
        try {
            String environmentFile = String.format("t24.url=%s\n" +
                    "ocb.url=%s",System.getProperty("t24.url"),System.getProperty("ocb.url"));
            FileOutputStream fo = new FileOutputStream(new File("target/allure-results/environment.properties"));
            byte[] data = environmentFile.getBytes();
            fo.write(data);
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    @AfterStory
    public void clearData() {
        ApplicationManager.getInstance().get().closeAllApplications();
    }
}
