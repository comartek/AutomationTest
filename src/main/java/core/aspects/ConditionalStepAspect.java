package core.aspects;

import core.conditional.Conditional;
import core.conditional.Conditional.ConditionalBlockState;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;


@Aspect
public class ConditionalStepAspect {
    
    @SuppressWarnings("PMD.UnnecessaryLocalBeforeReturn")
    @Around("(@annotation(org.jbehave.core.annotations.When)||@annotation(org.jbehave.core.annotations.Then)||@annotation(org.jbehave.core.annotations.Given)) && execution(* *(..))")
    public Object step(final ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Conditional conditional = Conditional.getInstance().get();

        if (methodSignature.getMethod().getName().startsWith("conditional")) {
            conditional.stepInsideThenOrElseBlockIsSkipped = false;
            conditional.stepInsideIfOrElseIfBlockIsFailed = false;
            conditional.throwableForStepInsideIfOrElseIfBlock = null;

            if (methodSignature.getMethod().getName().equals("conditionalIfBlockStart")) {
                if (!conditional.conditionalBlockState.equals(ConditionalBlockState.NONE))
                    throw new RuntimeException("IF block should be started only after finishing previous conditional block");
                conditional.conditionalBlockState = ConditionalBlockState.IF_BLOCK;
                conditional.conditionResult = true;
                conditional.conditionalBlockFinished = false;
            }
            if (methodSignature.getMethod().getName().equals("conditionalThenBlockStart")) {
                if (!(conditional.conditionalBlockState.equals(ConditionalBlockState.IF_BLOCK)|| conditional.conditionalBlockState.equals(ConditionalBlockState.ELSEIF_BLOCK)))
                    throw new RuntimeException("THEN block should be started only after IF or ELSEIF block");
                conditional.conditionalBlockState = ConditionalBlockState.THEN_BLOCK;
            }
            if (methodSignature.getMethod().getName().equals("conditionalElseBlockStart")) {
                if (!(conditional.conditionalBlockState.equals(ConditionalBlockState.IF_BLOCK)||
                        conditional.conditionalBlockState.equals(ConditionalBlockState.THEN_BLOCK)||
                        conditional.conditionalBlockState.equals(ConditionalBlockState.ELSEIF_BLOCK)))
                    throw new RuntimeException("ELSE block should be started only after IF,THEN or ELSEIF block");
                if (conditional.conditionResult)
                    conditional.conditionalBlockFinished = true;
                conditional.conditionalBlockState = ConditionalBlockState.ELSE_BLOCK;
            }
            if (methodSignature.getMethod().getName().equals("conditionalElseIfBlockStart")) {
                if (!(conditional.conditionalBlockState.equals(ConditionalBlockState.IF_BLOCK)||
                        conditional.conditionalBlockState.equals(ConditionalBlockState.THEN_BLOCK)||
                        conditional.conditionalBlockState.equals(ConditionalBlockState.ELSEIF_BLOCK)))
                    throw new RuntimeException("ELSEIF block should be started only after IF,THEN or ELSEIF block");
                if (conditional.conditionResult)
                    conditional.conditionalBlockFinished = true;
                conditional.conditionalBlockState = ConditionalBlockState.ELSEIF_BLOCK;
//                elseIfConditionResult = !Conditional.getInstance().get().conditionResult;
                conditional.conditionResult = true;
            }
            if (methodSignature.getMethod().getName().equals("conditionalBlockEnd")) {
                if (conditional.conditionalBlockState.equals(ConditionalBlockState.NONE))
                    throw new RuntimeException("There is no conditional block to end here");
                conditional.conditionalBlockState = ConditionalBlockState.NONE;
                conditional.conditionResult = true;
            }

//            //Set skipped status for main conditional step in the Allure report if THEN_BLOCK or ELSE_BLOCK or ELSEIF_BLOCK should not be executed
//            if (!methodSignature.getMethod().getName().equals("conditionalIfBlockStart") && !methodSignature.getMethod().getName().equals("conditionalBlockEnd")) {
//                conditional.skipStepExecution(); //check if step should be skipped in the allure report
//            }
        }
        else {
            if(!conditional.skipStepExecution()) {
                if (conditional.conditionalBlockState == ConditionalBlockState.IF_BLOCK || conditional.conditionalBlockState == ConditionalBlockState.ELSEIF_BLOCK) {
                    try {
                        final Object proceed = joinPoint.proceed();
                        return proceed;
                    } catch (Throwable e) {
                        conditional.conditionResult = false;
                        conditional.stepInsideIfOrElseIfBlockIsFailed = true;
                        conditional.throwableForStepInsideIfOrElseIfBlock = e;
                        return null;
                    }
                } else {
                    return joinPoint.proceed();
                }
            }


//            if (conditional.conditionalBlockState == ConditionalBlockState.NONE)
//                return joinPoint.proceed();
//
//            if (conditional.conditionalBlockState == ConditionalBlockState.IF_BLOCK) {
//                if (conditional.conditionalBlockFinished)
//                    return null;
//                if (conditional.conditionResult) {
//                    try {
//                        final Object proceed = joinPoint.proceed();
//                        return proceed;
//                    } catch (Throwable e) {
//                        conditional.conditionResult = false;
//                        return null;
//                    }
//                }
//            }
//
//            if (conditional.conditionalBlockState == ConditionalBlockState.ELSEIF_BLOCK) {
//                if (conditional.conditionalBlockFinished)
//                    return null;
//                if (conditional.conditionResult) {
//                    try {
//                        final Object proceed = joinPoint.proceed();
//                        return proceed;
//                    } catch (Throwable e) {
//                        conditional.conditionResult = false;
//                        return null;
//                    }
//                }
//            }
//
//            if (conditional.conditionalBlockState == ConditionalBlockState.THEN_BLOCK){
//                if (conditional.conditionalBlockFinished)
//                    return null;
//                if (conditional.conditionResult)
//                    return joinPoint.proceed();
//            }
//            if (conditional.conditionalBlockState == ConditionalBlockState.ELSE_BLOCK){
//                if (conditional.conditionalBlockFinished)
//                    return null;
//                if (!conditional.conditionResult)
//                    return joinPoint.proceed();
//            }
        }

        return null;
    }
}
