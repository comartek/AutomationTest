package core.aspects;

import core.exceptions.SoftAssertException;
import core.utils.assertExt.AssertExt;
import core.utils.report.MetaParser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class SoftAssertAspect {

    @SuppressWarnings("PMD.UnnecessaryLocalBeforeReturn")
    @Around("(@annotation(org.jbehave.core.annotations.When)||@annotation(org.jbehave.core.annotations.Then)||@annotation(org.jbehave.core.annotations.Given)) && execution(* *(..))")
    public Object step(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (MetaParser.isScriptShouldBeSkipped())
            throw MetaParser.chainScriptFailedException;
        try {
            final Object proceed = joinPoint.proceed();
            return proceed;
        } catch (Throwable e) {
            if(e instanceof SoftAssertException || !AssertExt.getSoftAssertMode()) {
                throw e;
            } else {
                AssertExt.getInterceptedErrorsList().getLast().add(e);
                AssertExt.setCountInterceptedErrors(AssertExt.getCountInterceptedErrors() + 1);
                if (e.getCause() != null)
                    e.getCause().printStackTrace();
                else
                    e.printStackTrace();
            }
        } finally {

        }
        return null;
    }

}
