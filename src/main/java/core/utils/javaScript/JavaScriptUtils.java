package core.utils.javaScript;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import static core.steps.BaseSteps.getActiveDriver;

public class JavaScriptUtils {
    public static boolean isElementVisible(WebElement elem){
        /*
        * Check if element is currently visible:
        * 1. Get element coordinates
        * 2. Get parent element with scrollbar
        * 3. Determine if current element coordinates is within these parent element visible range
        * */

        String jsCode = "" +
                "var elemRect = arguments[0].getBoundingClientRect();\n" +
                "var elemScrollParent = getScrollParent(arguments[0],true);\n" +
                "var elemScrollParentRect = elemScrollParent.getBoundingClientRect();\n" +
                "var isVisible = (elemRect.top >= elemScrollParentRect.top) && (elemRect.bottom + getWindowScrollBarHeight() <= elemScrollParentRect.bottom) && (elemRect.bottom < window.innerHeight);\n" +
                "return isVisible;\n" +
                "\n" +
                "function getScrollParent(element, includeHidden) {\n" +
                "    var style = getComputedStyle(element);\n" +
                "    var excludeStaticParent = style.position === \"absolute\";\n" +
                "    var overflowRegex = includeHidden ? /(auto|scroll|hidden)/ : /(auto|scroll)/;\n" +
                "\n" +
                "    if (style.position === \"fixed\") return document.body;\n" +
                "    for (var parent = element; (parent = parent.parentElement);) {\n" +
                "        style = getComputedStyle(parent);\n" +
                "        if (excludeStaticParent && style.position === \"static\") {\n" +
                "            continue;\n" +
                "        }\n" +
                "        if (overflowRegex.test(style.overflow + style.overflowY + style.overflowX)) return parent;\n" +
                "    }\n" +
                "\n" +
                "    return document.body;\n" +
                "}\n" +
                "function getWindowScrollBarHeight() {\n" +
                "    let bodyStyle = window.getComputedStyle(document.body);\n" +
                "    let fullHeight = document.body.scrollHeight;\n" +
                "    let contentHeight = document.body.getBoundingClientRect().height;\n" +
                "    let marginTop = parseInt(bodyStyle.getPropertyValue('margin-top'), 10);\n" +
                "    let marginBottom = parseInt(bodyStyle.getPropertyValue('margin-bottom'), 10);\n" +
                "    return fullHeight - contentHeight - marginTop - marginBottom;\n" +
                "}";
        Object jsVar = ((JavascriptExecutor) getActiveDriver()).executeScript(jsCode, elem);

        return Boolean.valueOf(String.valueOf(jsVar));
    }
}
