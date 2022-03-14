package core.steps.common.CollectionSteps;

import core.annotations.StepExt;
import core.collections.BaseCollectionItem;
import core.htmlElements.element.ExtendedWebElement;
import core.sql.SQLManager;
import core.steps.BaseSteps;
import core.steps.common.UtilSteps.UtilSubSteps;
import core.utils.report.ReportUtils;
import core.utils.timeout.TimeoutUtils;
import org.jbehave.core.model.ExamplesTable;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static core.pages.BasePage.activateElementBorderIfNeeded;

/**
 * Created by Akarpenko on 10.01.2018.
 */
public class CollectionSubSteps extends BaseSteps {
    static UtilSubSteps utilSubSteps = new UtilSubSteps();

    @StepExt("in collection \"{collection}\" select item by index \"{index}\"")
    public void stepInCollectionSelectItemByIndex(String collection, int index) {
        getCurrentPage().SelectedCollectionItem = null;
        getCurrentPage().SelectedCollectionItem = getCurrentPage().getCollectionItem(collection, index);
        Assert.assertNotNull("Collection item with index does not exist", getCurrentPage().SelectedCollectionItem);
    }

    @StepExt("save collection \"{collection}\" items count to variable \"{variable}\"")
    public void stepSaveCollectionItemsCountToVariable(String collection, String variable) {
        int count = ((List<BaseCollectionItem>) getCurrentPage().getField(collection)).size();
        utilSubSteps.stepSaveVariable(String.valueOf(count), variable);
    }

    @StepExt("save collection \"{collection}\" items count to variable \"{variable}\" search items by conditions")
    public void stepSaveCollectionItemsCountToVariableSearchItemsByConditions(String collection, String variable, ExamplesTable conditions) {
        int count = getCurrentPage().countCollectionItems(collection, conditions);
        utilSubSteps.stepSaveVariable(String.valueOf(count), variable);
    }

    @StepExt("in collection \"{collection}\" select item by conditions")
    public void stepInCollectionSelectItemByConditions(String collection, ExamplesTable conditions) {

        ReportUtils.attachExampletable(conditions);
        getCurrentPage().SelectedCollectionItem = null;
        getCurrentPage().SelectedCollectionItem = getCurrentPage().getCollectionItem(collection, conditions);
        getCurrentPage().scrollElementIntoView(getCurrentPage().SelectedCollectionItem);
        getCurrentPage().activateElementBorderIfNeeded(getCurrentPage().SelectedCollectionItem);
        Assert.assertNotNull("Collection item with conditions does not exist", getCurrentPage().SelectedCollectionItem);

    }

    @StepExt("in collection \"{collection}\" find and select item by conditions")
    public void stepInCollectionFindAndSelectItemByConditions(String collection, ExamplesTable conditions) {
        int page = 1;
        int lastPage = 1;
        WebElement element;
        WebElement elementLastPage = null;
        element = getActiveDriver().findElement(By.xpath("//td[@id='completedTransfersGrid_pager_center']//td[@id='pager']/*[text()='" + page + "']"));
        if (element.isDisplayed() && element.isEnabled()) element.click();
        elementLastPage = getActiveDriver().findElement(By.xpath("//td[@id='completedTransfersGrid_pager_center']//td[@id='pager']/*[last()]"));
        //td[@id='pager']/*[last()]
        lastPage = Integer.parseInt(elementLastPage.getText());

        ReportUtils.attachExampletable(conditions);
        getCurrentPage().SelectedCollectionItem = null;
        while (true) {
            //need some wait
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getCurrentPage().SelectedCollectionItem = getCurrentPage().getCollectionItem(collection, conditions);

            if (getCurrentPage().SelectedCollectionItem == null && page < lastPage) {
                page++;
                element = getActiveDriver().findElement(By.xpath("//td[contains(@id,'pager_center')]//td[@id='pager']/*[text()='" + page + "']"));
                if (element.isDisplayed() && element.isEnabled()) element.click();
            } else break;
        }
        getCurrentPage().scrollElementIntoView(getCurrentPage().SelectedCollectionItem);
        getCurrentPage().activateElementBorderIfNeeded(getCurrentPage().SelectedCollectionItem);
        //Assert.assertNotNull("Collection item with conditions does not exist", getCurrentPage().SelectedCollectionItem);

    }

    @StepExt("in collection \"{collection}\" item with conditions exists")
    public void stepInCollectionItemWithConditionsExists(String collection, ExamplesTable conditions){

        ReportUtils.attachExampletable(conditions);
        getCurrentPage().SelectedCollectionItem = null;
        BaseCollectionItem collectionItem = getCurrentPage().getCollectionItem(collection, conditions);
        if(collectionItem != null){
            getCurrentPage().activateElementBorderIfNeeded(collectionItem);
        }
        Assert.assertNotNull("Collection item with conditions does not exist", collectionItem);
    }

    @StepExt("in collection \"{collection}\" all items match conditions")
    public void stepInCollectionAllItemsMatchConditions(String collection, ExamplesTable conditions){
        ReportUtils.attachExampletable(conditions);
        List<BaseCollectionItem> collectionItems = (List<BaseCollectionItem>) (getCurrentPage().getField(collection));
        for (int i=0;i<collectionItems.size();i++){
            boolean matches = collectionItems.get(i).matchesCondition(conditions);
            if(!matches){
                getCurrentPage().activateElementBorderIfNeeded(collectionItems.get(i));
            }
            Assert.assertTrue(String.format("Item index %s doesn not match conditions",i),matches);
        }

    }

    @StepExt("in collection \"{collection}\" item with conditions does not exist")
    public void stepInCollectionItemWithConditionsDoesNotExist(String collection, ExamplesTable conditions) {

        ReportUtils.attachExampletable(conditions);
        getCurrentPage().SelectedCollectionItem = null;
        try {
            BaseCollectionItem foundCollectionItem = getCurrentPage().getCollectionItem(collection, conditions);
            if (foundCollectionItem != null) {
                activateElementBorderIfNeeded(foundCollectionItem);
                throw new AssertionError("Collection item with conditions exists (it wasn`t supposed to)");
            }
        } catch (AssertionError e) {

        }

    }

    @StepExt("collection \"{collection}\" is empty")
    public void stepCollectionIsEmpty(String collection) {
        long currTimeOut = TimeoutUtils.getTimeOut();
        TimeoutUtils.setTimeOut(1);
        List<BaseCollectionItem> collectionItems = (List<BaseCollectionItem>) (getCurrentPage().getField(collection));
        Assert.assertEquals(0, collectionItems.size());
        TimeoutUtils.setTimeOut(currTimeOut);
    }

    @StepExt("collection \"{collection}\" is not empty")
    public void stepCollectionIsNotEmpty(String collection) {
        long currTimeOut = TimeoutUtils.getTimeOut();
        TimeoutUtils.setTimeOut(1);
        List<BaseCollectionItem> collectionItems = (List<BaseCollectionItem>) (getCurrentPage().getField(collection));
        if (collectionItems.size() == 0) {
            Assert.fail("Collection is empty");
        }
        TimeoutUtils.setTimeOut(currTimeOut);
    }


    @StepExt("collection \"{collection}\" contains all records from latest SQL query to database \"{database}\"")
    public void stepCollectionContainsAllRecordsFromSqlQuery(String collection,String database,ExamplesTable mapping) {
        ReportUtils.attachExampletable(mapping);
        List<BaseCollectionItem> collectionItems = (List<BaseCollectionItem>) (getCurrentPage().getField(collection));
        List<Map<String,String>> queryResult= SQLManager.getInstance().get().getSqlDriver(database).getLastQueryResults();



        for (int i=0;i<queryResult.size();i++){
            for (Map<String, String> row : mapping.getRows()){
                Assert.assertEquals("",
                        queryResult.get(i).get(row.get("DB_FIELD")),
                        ((ExtendedWebElement) collectionItems.get(i).getField(row.get("COLLECTION_FIELD"))).getText());
            }
//            boolean matches = collectionItems.get(i).matchesCondition(conditions);
//            if(!matches){
//                getCurrentPage().activateElementBorderIfNeeded(collectionItems.get(i));
//            }
//            Assert.assertTrue(String.format("Item index %s doesn not match conditions",i),matches);
        }

    }


    /**
     * Date should be 1st column in the table
     */
    @StepExt("date of transaction in \"{collection}\" is satisfy date range \"{dateRange}\"")
    public void stepDateOfTransactionInCollectionIsSatisfyDateOfRange(String collection, String dateRange) {

        WebElement element;
        WebElement elementLastPage = getActiveDriver().findElement(By.xpath("//td[@id='pager']/*[last()]"));
        String stringStartDate = dateRange.replaceAll("(\\D+\\d{2}\\/\\d{2}\\/\\d{4})$", "");
        String stringEndDate = dateRange.replaceAll("^(\\d{2}\\/\\d{2}\\/\\d{4}\\D+)", "");
        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        List<BaseCollectionItem> collectionItems = (List<BaseCollectionItem>) (getCurrentPage().getField(collection));

        try {
            Date startDate = originalFormat.parse(stringStartDate);
            Date endDate = originalFormat.parse(stringEndDate);


        int page = 1;
        int lastPage = Integer.parseInt(elementLastPage.getText());

        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(BaseCollectionItem item : collectionItems){
                if(!item.getText().equals("")) {
                    Date date = originalFormat.parse(item.getText().replaceAll(" .*", ""));
                    if(!(date.after(startDate) && date.before(endDate))) Assert.fail(String.format("Date '%s' does not satisfy date range '%s'", date, dateRange));
                }
            }

            if (page < lastPage) {
                page++;
                element = getActiveDriver().findElement(By.xpath("//td[@id='pager']/a[text()='" + page + "']"));
                if (element.isDisplayed() && element.isEnabled()) element.click();
            } else break;
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
