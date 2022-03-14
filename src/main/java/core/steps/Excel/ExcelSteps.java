package core.steps.Excel;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.steps.BaseSteps;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;

import java.util.Map;

import static core.utils.evaluationManager.EvaluationManager.evalVariable;

public class ExcelSteps extends BaseSteps {

    ExcelSubSteps excelSubSteps = new ExcelSubSteps();

    @NeverTakeAllureScreenshotsForThisStep
    @When("change tmp xls with path \"$path\" sheet \"$nameSheet\" and row \"$rowNum\":$change_param")
    public void whenSetValuesToExelForOverdraft(String path,String nameSheet,int rowNum ,ExamplesTable change_param){
        path=evalVariable(path);
        excelSubSteps.openBook(path);
        for (Map<String, String> row : change_param.getRows()) {
            excelSubSteps.setExelVal(row.get("field"), path, nameSheet, rowNum, evalVariable(row.get("value")));
        }
        excelSubSteps.close();
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("compare excel file \"$file1\" with excel file \"$file2\"")
    public void whenCompareExcelFileWithExcelFile(String file1, String file2){
        String evalFile1 = evalVariable(file1);
        String evalFile2 = evalVariable(file2);
        excelSubSteps.stepCompareExcelFileWithExcelFile(file1, file2);
    }


    @NeverTakeAllureScreenshotsForThisStep
    @When("update T24 DB from file OFS \"$file\" from sheet \"$sheet\"")
    public void whenUpdateT24DBFromFile(String file, String sheet){
        String evalFile = evalVariable(file);
        String evalSheet = evalVariable(sheet);
        excelSubSteps.stepWhenUpdateT24DBFromFile(evalFile, evalSheet);
    }
}
