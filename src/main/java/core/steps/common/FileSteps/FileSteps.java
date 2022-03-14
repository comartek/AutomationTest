package core.steps.common.FileSteps;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.steps.BaseSteps;
import core.steps.common.UtilSteps.UtilSubSteps;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static core.utils.evaluationManager.EvaluationManager.evalVariable;

public class FileSteps extends BaseSteps {

    UtilSubSteps utilSubSteps = new UtilSubSteps();
    FileSubSteps fileSubSteps= new FileSubSteps();

    @NeverTakeAllureScreenshotsForThisStep
    @When("download file from URL \"$url\" and save to path \"$path\"")
    public void whenDownloadFileAndSaveToPath(String url, String path){

        String url_ev = evalVariable(url);
        String path_ev = evalVariable(path);

        fileSubSteps.stepDownloadFileAndSaveToPath(url_ev, path_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("PDF file \"$file\" contains \"$pageCount\" pages")
    public void thenPdfFileContainsPages(String file, String pageCount){

        String file_ev = evalVariable(file);
        int pageCount_ev = Integer.parseInt(evalVariable(pageCount));

        fileSubSteps.stepPdfFileContainsPages(file_ev, pageCount_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("PDF file \"$file\" contains text \"$text\" on page number \"$pageNum\"")
    public void thenPdfFileContainsTextOnPage(String file,String text, String pageNum){

        String file_ev = evalVariable(file);
        String text_ev = evalVariable(text);
        int pageNum_ev = Integer.parseInt(evalVariable(pageNum));

        fileSubSteps.stepPdfFileContainsTextOnPage(file_ev,text_ev,pageNum_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("remove file \"$file\" if it exists")
    public void whenRemoveFileIfExists(String file){
        String file_ev = evalVariable(file);
        fileSubSteps.stepRemoveFileIfExists(file_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @Then("file \"$file\" exists")
    public void thenFileExists(String file){
        String file_ev = evalVariable(file);
        fileSubSteps.stepFileExists(file_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("absolute path of a framework file \"$frameworkFile\" is saved to variable \"$variable\"")
    public void whenAbsolutePathOfAFrameworkFileIsSavedToVariable(String frameworkFile, String variable){
        String frameworkFile_ev = evalVariable(frameworkFile);
        fileSubSteps.stepAbsolutePathOfAFrameworkFileIsSavedToVariable(frameworkFile_ev, variable);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("create CSV file for overdraft UPL and saved to path \"$path\"")
    public void whenCreateCSVFileForUPL(String path){
        String path_eval = evalVariable(path);
        fileSubSteps.stepCreateCSVFileForUPL(path_eval);
    }

}
