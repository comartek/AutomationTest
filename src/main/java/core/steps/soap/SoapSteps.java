package core.steps.soap;

import core.annotations.NeverTakeAllureScreenshotsForThisStep;
import core.steps.BaseSteps;
import core.utils.report.ReportUtils;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.junit.Assert;

import static core.utils.evaluationManager.EvaluationManager.evalVariable;
import static core.utils.evaluationManager.EvaluationManager.evaluateExampleTable;
import static core.utils.timeout.TimeoutUtils.sleep;

/**
 * Created by Akarpenko on 12.02.2018.
 */
public class SoapSteps extends BaseSteps {

    SoapSubSteps soapSubSteps = new SoapSubSteps();

    @NeverTakeAllureScreenshotsForThisStep
    @When("information detail about account with type \"$accountType\" is received by account number \"$accountNo\"")
    //Variable "Type" should be one of "CurrentAccount", "CreditCard", "SavingAccount", "LoanContract"
    public void whenInformationDetailIsReceived(String accountType, String accountNo) {
        String accountType_ev = evalVariable(accountType);
        String accountNo_ev = evalVariable(accountNo);

        soapSubSteps.stepInformationDetailIsReceived(accountType_ev, accountNo_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("list of loan UPL is received by cif \"$cifNo\"")
    public void whenListOfLoanListIsReceived(String cifNo) {
        String cifNo_ev = evalVariable(cifNo);

        soapSubSteps.stepGetListOfLoanUPLIsReceived(cifNo_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("info about \"$requestType\" is received by CIF \"$cif\"")
    //Variable "requestType" should equals "CurrentAccount" or "OnlineSavingAZ" or "Loan" or "OnlineSavingLD"
    public void whenInfoIsReceived(String requestType, String cif) {

        String requestType_ev = evalVariable(requestType);
        String cif_ev = evalVariable(cif);

        soapSubSteps.stepInfoIsReceived(requestType_ev, cif_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("info about Overdraft Status is received by reference ID \"$referID\"")
    public void stepODStatusIsReceived(String referID) {

        String referID_ev = evalVariable(referID);

        soapSubSteps.stepODStatusIsReceived(referID_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("info about Overdue loan is received by CIF \"$cif\"")
    public void stepInfoOverdueIsReceived(String cif) {

        String cif_ev = evalVariable(cif);

        soapSubSteps.stepInfoOverdueIsReceived(cif_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("wait until resp \"$respParameter\" equals \"$value\" info about \"$requestType\" is received by CIF \"$cif\" waiting time \"$WaitTimeInSeconds\" interval \"$IntervalInSeconds\"")
    //Variable "requestType" should equals "CurrentAccount" or "OnlineSavingAZ" or "Loan" or "OnlineSavingLD"
    public void whenwaitInfoIsReceived(String respParameter,String value, String requestType, String cif,int WaitTimeInSeconds,int IntervalInSeconds) {
        long maxDateTimeInMillis = System.currentTimeMillis() + WaitTimeInSeconds * 1000;
        long curmilsec=System.currentTimeMillis();
        String requestType_ev = evalVariable(requestType);
        String cif_ev = evalVariable(cif);
        respParameter=evalVariable(respParameter);
        boolean find=false;
        while(System.currentTimeMillis()<maxDateTimeInMillis) {
            soapSubSteps.stepInfoIsReceived(requestType_ev, cif_ev);
            find=evalVariable("%{"+respParameter+"}%").equals(value);
            if(find) {
                break;
            }
             sleep(IntervalInSeconds);
        }
        Assert.assertTrue(String.format("In response have no parameter %s with value %s",evalVariable("%{"+respParameter+"}%"),value),find);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("search for unsecured overdraft by CIF \"$cif\"")
    public void whenMultipleInfoIsReceived(String cif) {

        String cif_ev = evalVariable(cif);

        soapSubSteps.stepMultipleInfoIsReceived(cif_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("receive account info with parameters: $parameters")
    //Variable "requestType" should equals "CurrentAccount" or "OnlineSavingAZ" or "Loan" or "OnlineSavingLD"
    public void whenReceiveAccountInfoWithParameters(ExamplesTable parameters) {
        ReportUtils.attachExampletable(parameters);
        ExamplesTable params_ev = evaluateExampleTable(parameters);

        soapSubSteps.stepReceiveAccountInfoWithParameters(params_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("receive gift list with parameters: $parameters")
    public void whenReceiveGiftListWithParameters(ExamplesTable parameters) {
        ReportUtils.attachExampletable(parameters);
        ExamplesTable params_ev = evaluateExampleTable(parameters);

        soapSubSteps.stepReceiveGiftListWithParameters(params_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("saving account detail is received by account number \"$accountNo\"")
    //Variable "Type" should be one of "CurrentAccount", "CreditCard", "SavingAccount", "LoanContract"
    public void whenSavingDetailIsReceived(String accountNo) {
        String accountNo_ev = evalVariable(accountNo);

        soapSubSteps.stepSavingDetailIsReceived(accountNo_ev);
    }


    @NeverTakeAllureScreenshotsForThisStep
    @When("info about card is received by LegalID/Visa Passport \"$id\"")
    public void whenInfoAboutCardIsReceived(String id) {

        String id_ev = evalVariable(id);

        soapSubSteps.stepInfoAboutCardIsReceived(id_ev);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("receive card info with parameters: $parameters")
    //Variable "requestType" should equals "CurrentAccount" or "OnlineSavingAZ" or "Loan" or "OnlineSavingLD"
    public void whenReceiveCardInfoWithParameters(ExamplesTable parameters) {
        ReportUtils.attachExampletable(parameters);
        ExamplesTable params_ev = evaluateExampleTable(parameters);

        soapSubSteps.stepReceiveCardInfoWithParameters(params_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("receive product list info with parameters: $parameters")
    public void whenReceiveProductListInfoWithParameters(ExamplesTable prameters) {
        ReportUtils.attachExampletable(prameters);
        ExamplesTable params_ev = evaluateExampleTable(prameters);

        soapSubSteps.stepReceiveProductListInfoWithParameters(params_ev);
    }


    @NeverTakeAllureScreenshotsForThisStep
    @When("DAO information is received by employeeID \"$id\" and saved to variable \"$variable\"")
    public void whenDAOInformationIsReceivedAndSavedToVar(String id, String variable) {

        String id_ev = evalVariable(id);

        soapSubSteps.stepDAOInformationIsReceivedAndSavedToVar(id_ev, variable);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("number of visible accounts with type \"$requestType\" is received by CIF \"$cif\" and saved to variable \"$varName\"")
    //Variable "requestType" should equals "CurrentAccount" or "OnlineSavingAZ" or "Loan" or "OnlineSavingLD"
    public void whenReceiveVisibleAccountCountWithParameters(String requestType, String cif, String varName) {

        String requestType_ev = evalVariable(requestType);
        String cif_ev = evalVariable(cif);

        soapSubSteps.stepNumberOfVisibleAccountsSavedToVariable(requestType_ev, cif_ev, varName);
    }


    @NeverTakeAllureScreenshotsForThisStep
    @When("number of visible credit cards is received by LegalID \"$legalID\" and saved to variable \"$variable\"")
    public void whenNumberOfVisibleCreditCardsIsReceivedByCIFAndSavedToVariable(String legalId, String varName) {
        String legalId_ev = evalVariable(legalId);

        soapSubSteps.stepNumberOfVisibleCreditCardsIsReceivedByLegalIdAndSavedToVariable(legalId_ev, varName);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("get card usage details by card number \"$cardNo\"")
    public void whenGetCardUsageDetailsByCardNumber(String cardNo) {

        String cardNo_ev = evalVariable(cardNo);

        soapSubSteps.stepGetCardUsageDetail(cardNo_ev);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("get cash withdrawal fee info by card number \"$cardNo\"")
    public void whenGetCashWithdrawalFeeByCardNumber(String cardNo) {

        String cardNo_ev = evalVariable(cardNo);

        soapSubSteps.stepGetCashWithdrawalFeeByCardNumber(cardNo_ev);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("get iCash info by CIF \"$cif\" and amount \"$amount\"")
    public void whenGetICashInfo(String cif, String amount) {

        String cif_ev = evalVariable(cif);
        String amount_ev = evalVariable(amount);

        soapSubSteps.stepGetICashInfo(cif_ev, amount_ev);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("get Current Account Details Request \"$accountNumber\"")
    public void whenGetCADetails(String accountNumber) {

        String accountNumber_ev = evalVariable(accountNumber);

        soapSubSteps.stepGetCADetails(accountNumber_ev);

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("get reward points by CIF \"$cif\"")
    public void whenRetrieveRewardPoint(String cif) {

        String cif_ev = evalVariable(cif);

        soapSubSteps.stepRetrieveRewardPoint(cif_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("get customer information by CIF \"$cif\"")
    public void whengetCustomerInfo(String cif) {

        String cif_ev = evalVariable(cif);

        soapSubSteps.stepGetCustomerInfo(cif_ev);
    }


    @NeverTakeAllureScreenshotsForThisStep
    @When("get minimum and maximum amount by card number \"$cardNo\"")
    public void whenReceiveMinimumAndMaximumAmountByCardNumber(String cardNo) {
        String cardNo_ev = evalVariable(cardNo);
        soapSubSteps.stepGetMinimumAndMaximumAmountByCardNumber(cardNo_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("get interest rate with parameters: $parameters")
    public void whenGetInterestRateWithParameters(ExamplesTable parameters) {
        ReportUtils.attachExampletable(parameters);
        ExamplesTable params_ev = evaluateExampleTable(parameters);

        soapSubSteps.stepGetInterestRateWithParameters(params_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("get Evoucher Info with parameters: $parameters")
    public void whenGetEvoucherInfo(ExamplesTable parameters) {

        ReportUtils.attachExampletable(parameters);
        ExamplesTable params_ev = evaluateExampleTable(parameters);

        soapSubSteps.stepGetEvoucherInfo(params_ev);
    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("get W4 current date")
    public void whenGetW4CurrentDate() {
        soapSubSteps.stepGetW4CurrentDate();

    }

    @NeverTakeAllureScreenshotsForThisStep
    @When("cancel application in LOS with parameters: $parameters")
    public void whenUpdateOverdraftUPLStatus(ExamplesTable parameters){
        ReportUtils.attachExampletable(parameters);
        ExamplesTable params_ev=evaluateExampleTable(parameters);
        soapSubSteps.stepCancelApplication(params_ev);
    }
}
