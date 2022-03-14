package core.annotations;

import core.utils.assertExt.AssertExt;
import core.utils.lang.FindByUtils;
import core.utils.lang.LangUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.AbstractFindByBuilder;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactoryFinder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;

import static core.utils.lang.LangUtils.getCurrAppLanguage;

/**
 * Created by AZagriychuk on 14.11.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface SwitchToPlural {
    SwitchTo[] value();
    String finalFrameNameRegExpPattern() default "";
}
