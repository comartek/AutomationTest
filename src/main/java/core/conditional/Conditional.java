package core.conditional;

public class Conditional {
    public enum ConditionalBlockState {NONE,IF_BLOCK,THEN_BLOCK,ELSE_BLOCK,ELSEIF_BLOCK}

    private static ThreadLocal<Conditional> instance = new ThreadLocal<>();

    public ConditionalBlockState conditionalBlockState = ConditionalBlockState.NONE;
    public boolean conditionResult = true;
    public boolean conditionalBlockFinished = true;
    public boolean stepInsideThenOrElseBlockIsSkipped = false;
    public boolean stepInsideIfOrElseIfBlockIsFailed = false;
    public Throwable throwableForStepInsideIfOrElseIfBlock = null;

    public static ThreadLocal<Conditional> getInstance() {
        if (instance.get() == null)
        {
            instance.set(new Conditional());
        }
        return instance;
    }

    public boolean skipStepExecution(){
        stepInsideThenOrElseBlockIsSkipped = skipStepExecutionImpl();
        return stepInsideThenOrElseBlockIsSkipped;
    }

    private boolean skipStepExecutionImpl(){
        if (this.conditionalBlockState == ConditionalBlockState.NONE)
            return false;

        if (this.conditionalBlockState == ConditionalBlockState.IF_BLOCK) {
            return this.conditionalBlockFinished || !this.conditionResult;
        }

        if (this.conditionalBlockState == ConditionalBlockState.ELSEIF_BLOCK) {
            return this.conditionalBlockFinished || !this.conditionResult;
        }

        if (this.conditionalBlockState == ConditionalBlockState.THEN_BLOCK) {
            return this.conditionalBlockFinished || !this.conditionResult;
        }

        if (this.conditionalBlockState == ConditionalBlockState.ELSE_BLOCK) {
            return this.conditionalBlockFinished || this.conditionResult;
        }

        return true;
    }
}
