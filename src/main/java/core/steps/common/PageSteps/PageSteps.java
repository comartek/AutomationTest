package core.steps.common.PageSteps;


import core.steps.BaseSteps;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static core.utils.evaluationManager.EvaluationManager.evalVariable;

/**
 * Created by Akarpenko on 19.10.2017.
 */
public class PageSteps extends BaseSteps {

    PageSubSteps pageSubSteps = new PageSubSteps();

    @When("open url \"$url\" in browser")
    public void whenOpenUrlInBrowser(String url) {
        String url_ev = evalVariable(url);
        pageSubSteps.stepOpenUrlInBrowser(url_ev);
    }


    @Then("page \"$page\" is loaded")
    public void thenPageIsLoaded(String page)
    {
        pageSubSteps.stepPageIsLoaded(page);
    }

    @Then("page \"$page\" is syncing loaded when waiting for \"$time\" seconds")
    public void thenPageIsSyncingLoaded(String page,int time)
    {
        pageSubSteps.stepPageIsLoadedSync(page,time);
    }

    @Then("page invisible \"$page\" is loaded")
    public void theninvisiblePageIsLoaded(String page)
    {
        pageSubSteps.stepInvisiblePageIsLoaded(page);
    }

    @Then("current page is still loaded")
    public void thenCurrentPageIsStillLoaded()
    {
        pageSubSteps.stepCurrentPageIsStillLoaded();
    }

    @When("synchronized with app")
    public void whenSynchronizedWithApp()
    {
        pageSubSteps.stepSynchronizedWithApp();
    }

    @When("alert accepted if existed")
    public void whenAlertAcceptedIfExisted()
    {
        pageSubSteps.stepAlertAcceptedIfExisted();
    }

    @When("alert accepted if existed (max wait time = \"$maxWaitTime\" seconds)")
    public void whenAlertAcceptedIfExistedWithMaxWaitTime(String maxWaitTime)
    {
        int maxWaitTimeInt = Integer.parseInt(maxWaitTime);
        pageSubSteps.stepAlertAcceptedIfExistedWithMaxWaitTime(maxWaitTimeInt);
    }

    @When("alert dismissed if existed")
    public void whenAlertDismissedIfExisted()
    {
        pageSubSteps.stepAlertDismissedIfExisted();
    }

    @When("alert dismissed if existed (max wait time = \"$maxWaitTime\" seconds)")
    public void whenAlertDismissedIfExistedWithMaxWaitTime(String maxWaitTime)
    {
        int maxWaitTimeInt = Integer.parseInt(maxWaitTime);
        pageSubSteps.stepAlertDismissedIfExistedWithMaxWaitTime(maxWaitTimeInt);
    }

    @When("current page frame is reloaded (refreshed)")
    public void whenCurrentPageIsReloadedRefreshed()
    {
        pageSubSteps.stepCurrentPageIsReloadedRefreshed();
    }

}
