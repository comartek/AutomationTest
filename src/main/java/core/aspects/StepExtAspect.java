package core.aspects;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.annotations.StepExt;
import core.annotations.UseFullDesktopScreenshots;
import core.utils.assertExt.AssertExt;
import core.utils.report.ReportUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.*;

import static io.qameta.allure.util.AspectUtils.getParameters;
import static io.qameta.allure.util.AspectUtils.getParametersMap;
import static io.qameta.allure.util.NamingUtils.processNameTemplate;
import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;

@Aspect
public class StepExtAspect {

    private static AllureLifecycle lifecycle;
    private static boolean isSubstepScreenshotsEnabled=false;
    private static Stack<Boolean> subStepScreenshotsEnabledStack = new Stack<>();

    @SuppressWarnings("PMD.UnnecessaryLocalBeforeReturn")
    @Around("@annotation(core.annotations.StepExt) && execution(* *(..))")
    public Object step(final ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final StepExt step = methodSignature.getMethod().getAnnotation(StepExt.class);
        final String name = step.value().isEmpty()
                ? methodSignature.getName()
                : processNameTemplate(step.value(), getParametersMap(methodSignature, joinPoint.getArgs()));
        final String uuid = UUID.randomUUID().toString();
        final StepResult result = new StepResult()
                .withName(name);
        if (!step.hideParams())
            result.withParameters(getParameters(methodSignature, joinPoint.getArgs()));

        if(step.screenshotsForSubsteps()){
            isSubstepScreenshotsEnabled = step.screenshotsForSubsteps();
            subStepScreenshotsEnabledStack.push(isSubstepScreenshotsEnabled);
        }
        if (isSubstepScreenshotsEnabled)
            Logger.getRootLogger().info(name);

        final NeverTakeAllureScreenshotsForThisStep noScreenshots = methodSignature.getMethod().getAnnotation(NeverTakeAllureScreenshotsForThisStep.class);
        boolean allureScreenshotsAllowed = noScreenshots == null;
        final UseFullDesktopScreenshots useFullDesktopScreenshots = methodSignature.getMethod().getAnnotation(UseFullDesktopScreenshots.class);
        ReportUtils.FullDesktopScreenshotMode fullDesktopScreenshotMode = useFullDesktopScreenshots == null ? null : useFullDesktopScreenshots.value();

        getLifecycle().startStep(uuid, result);
        AssertExt.getInterceptedErrorsList().add(new ArrayList<>());
        try {
            final Object proceed = joinPoint.proceed();
            List<Throwable> stepErrors=AssertExt.getInterceptedErrorsList().getLast();
            if (stepErrors.size()>0) {
                if (stepErrors.stream().anyMatch(x -> x instanceof Exception)) {
                    getLifecycle().updateStep(uuid, s -> s.withStatus(Status.BROKEN));
                    throw new RuntimeException("One or more substeps of this step are failed with exceptions");
                }
                if (stepErrors.stream().anyMatch(x -> x instanceof AssertionError)) {
                    getLifecycle().updateStep(uuid, s -> s.withStatus(Status.BROKEN));
                    throw new AssertionError("One or more substeps of this step are failed");
                }
            }
            else {
                getLifecycle().updateStep(uuid, s -> s.withStatus(Status.PASSED));
            }
            return proceed;
        } catch (Throwable e) {
            AssertExt.getInterceptedErrorsList().getLast().add(e);
            AssertExt.setCountInterceptedErrors(AssertExt.getCountInterceptedErrors() + 1);
            getLifecycle().updateStep(uuid, s -> s
                    .withStatus(getStatus(e).orElse(Status.BROKEN))
                    .withStatusDetails(getStatusDetails(e).orElse(null)));
            throw e;
        } finally {
            if (isSubstepScreenshotsEnabled && allureScreenshotsAllowed)
                ReportUtils.attachScreenshotBasedOnScreenshotLogMode(ReportUtils.StepState.AFTER_STEP, fullDesktopScreenshotMode);

            if(step.screenshotsForSubsteps()) {
                subStepScreenshotsEnabledStack.pop();
                isSubstepScreenshotsEnabled = subStepScreenshotsEnabledStack.size() > 0 ? subStepScreenshotsEnabledStack.peek() : false;
            }

            StepExt.ForcedStepStatus forcedStepStatus = step.forcedStepStatus();
            if(forcedStepStatus != StepExt.ForcedStepStatus.NO_FORCED_STATUS){
                Status status = Status.valueOf(forcedStepStatus.name());
                getLifecycle().updateStep(uuid, s -> s.withStatus(status));
            }

            getLifecycle().stopStep(uuid);
            AssertExt.getInterceptedErrorsList().removeLast();
        }
    }

    /**
     * For tests only.
     *
     * @param allure allure lifecycle to set.
     */
    public static void setLifecycle(final AllureLifecycle allure) {
        lifecycle = allure;
    }

    public static AllureLifecycle getLifecycle() {
        if (Objects.isNull(lifecycle)) {
            lifecycle = Allure.getLifecycle();
        }
        return lifecycle;
    }
}
