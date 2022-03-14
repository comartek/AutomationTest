package core.utils.report;

public interface FrameworkErrorsAllureLogger {
    void logFrameworkErrorToTheAllureReport(Throwable throwable);
    void logFrameworkErrorToTheAllureReport(Throwable throwable, boolean performAfterScenarioInTheEnd);
    void logFrameworkErrorToTheAllureReport(Throwable throwable, String scenarioTitle, boolean performAfterScenarioInTheEnd);
}
