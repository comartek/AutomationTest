package core.annotations;

import core.utils.lang.FindByUtils;
import core.utils.lang.LangUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.AbstractFindByBuilder;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactoryFinder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Created by AZagriychuk on 28.09.2018.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@PageFactoryFinder(FindByEng.FindByBuilder.class)
public @interface FindByEng {
    How how() default How.UNSET;

    String using() default "";

    String id() default "";

    String name() default "";

    String className() default "";

    String css() default "";

    String tagName() default "";

    String linkText() default "";

    String partialLinkText() default "";

    String xpath() default "";


    class FindByBuilder extends AbstractFindByBuilder {

        public FindByBuilder() {
        }

        public By buildIt(Object annotation, Field field) {
            FindByEng findByEng = (FindByEng)annotation;
            FindBy findBy = FindByUtils.castFindByEngToFindBy(findByEng);
            this.assertValidFindBy(findBy);
            By ans = this.buildByFromShortFindBy(findBy);
            if (ans == null) {
                ans = this.buildByFromLongFindBy(findBy);
            }

            return ans;
        }
    }
}
