package core.collections;

import core.htmlElements.element.ExtendedWebElement;
import core.htmlElements.element.HtmlElement;
import org.jbehave.core.model.ExamplesTable;
import org.openqa.selenium.WebElement;

import java.util.Map;

import static core.utils.evaluationManager.EvaluationManager.evalVariable;

/**
 * Created by Akarpenko on 10.11.2017.
 */
public class BaseCollectionItem extends HtmlElement {

    String ConditionRegex = "";

    public Object getField(String fieldName){
        try {
            return ((WebElement) getClass().getField(fieldName).get(this));
        } catch (Exception e) {
            return null;
        }
    }

    public boolean matchesCondition(ExamplesTable conditions){
        for (Map<String, String> row : conditions.getRows()) {
            String field = row.get("field");
            String operation = row.get("operation");
            String value = row.get("value");

            if (!checkCondition(field,operation,value))
                return false;
        }
        return true;
    }

    public boolean checkCondition(String fieldName, String operation, String value){

        ExtendedWebElement field = (ExtendedWebElement) getField(fieldName);

        return field.checkCondition(operation, value);
    }

}
