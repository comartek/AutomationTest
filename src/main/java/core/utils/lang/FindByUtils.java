package core.utils.lang;

import core.annotations.FindByEng;
import core.annotations.FindByVie;
import core.annotations.SwitchTo;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.lang.annotation.Annotation;

public class FindByUtils {

    public static FindBy castFindByEngToFindBy(FindByEng findByEng){
        return new FindByImpl(findByEng);
    }

    public static FindBy castFindByVieToFindBy(FindByVie findByVie){
        return new FindByImpl(findByVie);
    }

    public static FindBy getFindByFromSwitchTo(SwitchTo switchTo){
        return new FindByImpl(switchTo);
    }

    static class FindByImpl implements FindBy {
        How how;
        String using;
        String id;
        String name;
        String className;
        String css;
        String tagName;
        String linkText;
        String partialLinkText;
        String xpath;

        FindByImpl(SwitchTo switchTo){
            how = switchTo.webElementFindByHow();
            using = switchTo.webElementFindByUsing().equals(SwitchTo.defaultString) ? "" : switchTo.webElementFindByUsing();
            id = switchTo.webElementFindById().equals(SwitchTo.defaultString) ? "" : switchTo.webElementFindById();
            name = switchTo.webElementFindByName().equals(SwitchTo.defaultString) ? "" : switchTo.webElementFindByName();
            className = switchTo.webElementFindByClassName().equals(SwitchTo.defaultString) ? "" : switchTo.webElementFindByClassName();
            css = switchTo.webElementFindByCss().equals(SwitchTo.defaultString) ? "" : switchTo.webElementFindByCss();
            tagName = switchTo.webElementFindByTagName().equals(SwitchTo.defaultString) ? "" : switchTo.webElementFindByTagName();
            linkText = switchTo.webElementFindByLinkText().equals(SwitchTo.defaultString) ? "" : switchTo.webElementFindByLinkText();
            partialLinkText = switchTo.webElementFindByPartialLinkText().equals(SwitchTo.defaultString) ? "" : switchTo.webElementFindByPartialLinkText();
            xpath = switchTo.webElementFindByXpath().equals(SwitchTo.defaultString) ? "" : switchTo.webElementFindByXpath();
        }

        FindByImpl(FindByEng findByEng){
            how = findByEng.how();
            using = findByEng.using();
            id = findByEng.id();
            name = findByEng.name();
            className = findByEng.className();
            css = findByEng.css();
            tagName = findByEng.tagName();
            linkText = findByEng.linkText();
            partialLinkText = findByEng.partialLinkText();
            xpath = findByEng.xpath();
        }

        FindByImpl(FindByVie findByVie){
            how = findByVie.how();
            using = findByVie.using();
            id = findByVie.id();
            name = findByVie.name();
            className = findByVie.className();
            css = findByVie.css();
            tagName = findByVie.tagName();
            linkText = findByVie.linkText();
            partialLinkText = findByVie.partialLinkText();
            xpath = findByVie.xpath();
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }

        @Override
        public How how() {
            return how;
        }

        @Override
        public String using() {
            return using;
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String className() {
            return className;
        }

        @Override
        public String css() {
            return css;
        }

        @Override
        public String tagName() {
            return tagName;
        }

        @Override
        public String linkText() {
            return linkText;
        }

        @Override
        public String partialLinkText() {
            return partialLinkText;
        }

        @Override
        public String xpath() {
            return xpath;
        }
    }
}