package core.elements;

import core.htmlElements.element.Table;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Stream;

import static core.steps.BaseSteps.getActiveDriver;
import static java.util.stream.Collectors.toList;

public class T24Table extends Table {

    public T24Table(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<WebElement> getHeadings() {
        return getWrappedElement().findElements(By.xpath(".//th[@class='columnHeader' and not(.//th)]"));
    }

    @Override
    public List<String> getHeadingsAsString() {
        return getHeadings().stream()
                .map(headingElement -> {
                    //For some reason you cant getText() of T24 table heading if it is not displayed (.getText() returns empty string)
                    //even though you can getText() of table cell which is not displayed
                    //Therefore we need to execute scrollIntoView() for a table heading if it is not displayed
                    if(!headingElement.isDisplayed()){
                        ((JavascriptExecutor)getActiveDriver()).executeScript("arguments[0].scrollIntoView(true);", headingElement);
                    }
                    return headingElement.getText();
                })
                .collect(toList());
    }

    @Override
    public List<List<WebElement>> getRows() {
        return getWrappedElement()
                .findElements(By.xpath(".//table[@id='datadisplay']//tr"))
                .stream()
                .map(rowElement -> rowElement.findElements(By.xpath(".//td")))
                .filter(row -> row.size() > 0) // ignore rows with no <td> tags
                .collect(toList());
    }

    @Override
    public List<WebElement> getColumnByIndex(int index) {
        return getWrappedElement().findElements(
                By.cssSelector(String.format("table[id='datadisplay'] tr > td:nth-of-type(%d)", index)));
    }
}
