package core.services.soap.requests.newESB.account;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.requests.newESB.ESBRequest;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by Denis on 21.04.2018.
 */

@RequestTemplate("getLDADetail")
public class GetLoanDetailRequest extends ESBRequest {
    @RequestField
    String contractNumber;

    public GetLoanDetailRequest(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.accountInfo.accountCurrency")
    public String AccountCurrency;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.branchInfo.branchNo")
    public String BranchNo;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.accountInfo.tenor")
    public String Tenor;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.amountInfo.principleAmount")
    public String PrincipleAmount;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.accountInfo.interestRate")
    public String InterestRate;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.accountInfo.maturityDate")
    public String MaturityDate;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.amountInfo.outstandingAmount")
    public String OutstandingAmount;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.amountInfo.thisPeriodPrincipleAmount")
    public String ThisPeriodPrincipleAmount;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.amountInfo.thisPeriodInterestAmount")
    public String ThisPeriodInterestAmount;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.accountInfo.nextPayPrincipleDate")
    public String NextPayPrincipleDate;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.accountInfo.nextPayInterestDate")
    public String NextPayInterestDate;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.amountInfo.principleAmountPastDue")
    public String PrincipleAmountPastDue;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.amountInfo.interestAmountPastDue")
    public String InterestAmountPastDue;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.amountInfo.penaltyInterestAmount")
    public String PenaltyInterestAmount;

    @ResponseField("Envelope.Body.getLDADetailRs.loanAccount.amountInfo.totalPrincipleAndInterestPastDue")
    public String TotalPrincipleAndInterestPastDue;
}
