package core.steps.common.CollectionSteps;

import core.steps.BaseSteps;
import core.utils.evaluationManager.EvaluationManager;
import core.utils.report.ReportUtils;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;

import static core.utils.evaluationManager.EvaluationManager.evalVariable;
import static core.utils.evaluationManager.EvaluationManager.evaluateExampleTable;

/**
 * Created by Akarpenko on 10.11.2017.
 */
public class CollectionSteps extends BaseSteps {

    CollectionSubSteps collectionSubSteps = new CollectionSubSteps();


    @When("in collection \"$collection\" select item by index \"$index\"")
    public void whenInCollectionSelectItemByIndex(String collection, String index) {
        collectionSubSteps.stepInCollectionSelectItemByIndex(collection, Integer.parseInt(evalVariable(index)));
    }

    @When("save collection \"$collection\" items count to variable \"$variable\"")
    public void whenSaveCollectionItemsCountToVariable(String collection, String variable) {
        collectionSubSteps.stepSaveCollectionItemsCountToVariable(collection, variable);
    }

    @When("save collection \"$collection\" items count to variable \"$variable\", search items by conditions: $conditions")
    public void whenSaveCollectionItemsCountToVariableSearchItemsByConditions(String collection, String variable, ExamplesTable conditions) {
        ReportUtils.attachExampletable(conditions);
        conditions = evaluateExampleTable(conditions);
        collectionSubSteps.stepSaveCollectionItemsCountToVariableSearchItemsByConditions(collection, variable, conditions);
    }

    @When("in collection \"$collection\" select item by conditions: $conditions")
    public void whenInCollectionSelectItemByConditions(String collection, ExamplesTable conditions) {

        ReportUtils.attachExampletable(conditions);
        conditions = evaluateExampleTable(conditions);
        collectionSubSteps.stepInCollectionSelectItemByConditions(collection, conditions);

    }

    @When("in collection \"$collection\" find and select item by conditions: $conditions")
    public void whenInCollectionFindAndSelectItemByConditions(String collection, ExamplesTable conditions) {

        ReportUtils.attachExampletable(conditions);
        conditions = evaluateExampleTable(conditions);
        collectionSubSteps.stepInCollectionSelectItemByConditions(collection,conditions);
    }

    @Then("in collection \"$collection\" item with conditions exists: $conditions")
    public void thenInCollectionItemWithConditionsExists(String collection, ExamplesTable conditions) {

        ReportUtils.attachExampletable(conditions);
        conditions = evaluateExampleTable(conditions);
        collectionSubSteps.stepInCollectionItemWithConditionsExists(collection, conditions);
    }

    @Then("in collection \"$collection\" item with conditions does not exist: $conditions")
    public void thenInCollectionItemWithConditionsDoesNotExist(String collection, ExamplesTable conditions) {

        ReportUtils.attachExampletable(conditions);
        conditions = evaluateExampleTable(conditions);
        collectionSubSteps.stepInCollectionItemWithConditionsDoesNotExist(collection, conditions);

    }

    @Then("in collection \"$collection\" all items match conditions: $conditions")
    public void thenInCollectionAllItemsMatchConditions(String collection, ExamplesTable conditions) {

        ReportUtils.attachExampletable(conditions);
        conditions = evaluateExampleTable(conditions);
        collectionSubSteps.stepInCollectionAllItemsMatchConditions(collection, conditions);
    }

    @Then("collection \"$collection\" is empty")
    public void thenCollectionIsEmpty(String collection) {
        collectionSubSteps.stepCollectionIsEmpty(collection);
    }

    @Then("collection \"$collection\" is not empty")
    public void thenCollectionIsNotEmpty(String collection) {
        collectionSubSteps.stepCollectionIsNotEmpty(collection);
    }

    @Then("collection \"$collection\" contains all records from latest SQL query to database \"$database\": $mapping")
    public void thenCollectionContainsAllRecordsFromSqlQuery(String collection,String database,ExamplesTable mapping) {
        ReportUtils.attachExampletable(mapping);
        mapping = evaluateExampleTable(mapping);
        collectionSubSteps.stepCollectionContainsAllRecordsFromSqlQuery(collection,database, mapping);
    }

    @Then("date of transaction in collection \"$collection\" is satisfy date range \"$dateRange\"")
    public void thenDateOfTransactionInCollectionIsSatisfyDateOfRange(String collection, String dateRange) {
        String dateRange_ev = EvaluationManager.evalVariable(dateRange);
        collectionSubSteps.stepDateOfTransactionInCollectionIsSatisfyDateOfRange(collection, dateRange_ev);
    }
}
