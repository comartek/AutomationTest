package core.annotations;

import core.utils.report.ReportUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//allows to take a full desktop screenshots instead of selenium webdriver screenshots

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UseFullDesktopScreenshots {
    ReportUtils.FullDesktopScreenshotMode value() default ReportUtils.FullDesktopScreenshotMode.INSTEAD_OF_WEBDRIVER_SCREENSHOT_ALWAYS;
}
