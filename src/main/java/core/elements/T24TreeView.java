package core.elements;

import core.htmlElements.element.Button;
import core.htmlElements.element.TypifiedElement;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static core.utils.report.ReportUtils.addBorderedWebElementToTheList;
import static core.utils.report.ReportUtils.deactivateBordersOfTheBorderedElements;

public class T24TreeView extends TypifiedElement {


    public T24TreeView(WebElement wrappedElement){
        super(wrappedElement);
    }

    public void clickItem(String path)
    {
        int subitemOpenTimeout=5;
        int subItemStateAfterClickMaxWaitTime = 2;
        int subItemMaxReopenAttempts = 5;

        String[] subpath=path.split(">");

        WebElement currentNode=this;

        for (int i=0;i<subpath.length;i++) {
            if (i==subpath.length-1) {
                //Last element in the path supposed to be a link, pressing this link
                WebElement nextNode= currentNode.findElement(By.xpath(String.format(".//a[normalize-space(.)='%s']",subpath[i].trim())));
                borderTreeSubItem(nextNode);
                new WebDriverWait(((WrapsDriver) getWrappedElement()).getWrappedDriver(), subitemOpenTimeout).until(ExpectedConditions.elementToBeClickable(nextNode)).click();
            } else {
                //Not a last element (not a final link). Searching for a subelement of the path
                WebElement nextNode = currentNode.findElement(By.xpath(String.format("./ul/li[span[normalize-space(.)='%s']]",subpath[i].trim())));
                borderTreeSubItem(nextNode);

                //Trying to open tree sub item (multiple attempts if first attempts is not successful)
                for (int attempt = 1; attempt <= subItemMaxReopenAttempts; attempt++) {
                    //Checking if tree sublement is already opened (based on the picture)
                    WebElement arrow = nextNode.findElement(By.xpath(String.format("./span/img")));
                    if (arrow.getAttribute("src").contains("menu_down")) {
                        //Sub group is not opened, click Sub group item in order to open it
//                    arrow.click();
                        new WebDriverWait(((WrapsDriver) getWrappedElement()).getWrappedDriver(), subitemOpenTimeout).until(ExpectedConditions.elementToBeClickable(arrow)).click();
                        try {
                            new WebDriverWait(((WrapsDriver) getWrappedElement()).getWrappedDriver(), subItemStateAfterClickMaxWaitTime).until(ExpectedConditions.not(ExpectedConditions.attributeContains(arrow, "src", "menu_down")));
                            break;
                        }catch (TimeoutException e){
                            if(attempt == subItemMaxReopenAttempts){
                                throw e;
                            }
                        }
                    } else {
                        //Sub group is already opened
                        break;
                    }
                }
                currentNode = nextNode;
            }
        }
    }

    private void borderTreeSubItem(WebElement subItem){
        deactivateBordersOfTheBorderedElements();
        Button tmp = new Button(subItem);
        tmp.activateElementBorder();
        addBorderedWebElementToTheList(tmp);
    }
}
