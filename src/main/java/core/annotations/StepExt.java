package core.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface  StepExt {

    /**
     * The step text.
     *
     * @return the step text.
     */
    String value() default "";

    /**
     * Option for hide step parameters.
     *
     * @return boolean flag to disable step parameters showing.
     */
    boolean hideParams() default false;

    boolean screenshotsForSubsteps() default false;

    ForcedStepStatus forcedStepStatus() default ForcedStepStatus.NO_FORCED_STATUS;

    enum ForcedStepStatus {
        FAILED,
        BROKEN,
        PASSED,
        SKIPPED,
        NO_FORCED_STATUS
    }
}
