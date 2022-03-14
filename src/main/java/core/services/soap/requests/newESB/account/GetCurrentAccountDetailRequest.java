package core.services.soap.requests.newESB.account;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.requests.newESB.ESBRequest;

@RequestTemplate("getCADetails")
public class GetCurrentAccountDetailRequest extends ESBRequest {
    @RequestField
    String accountNumber;

    public GetCurrentAccountDetailRequest(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    @ResponseField("Envelope.Body.getCADetailsRs.accountDetails.currency")
    public String AccountCurrency;

    @ResponseField("Envelope.Body.getCADetailsRs.accountDetails.category")
    public String ProductId;

    @ResponseField("Envelope.Body.getCADetailsRs.accountDetails.branchCode")
    public String BranchNo;

    @ResponseField("Envelope.Body.getCADetailsRs.accountDetails.workingBalance")
    public String Balance;

    @ResponseField("Envelope.Body.getCADetailsRs.accountDetails.availableBalance")
    public String AvailableBalance;

    @ResponseField("Envelope.Body.getCADetailsRs.accountDetails.averageBalancePerMonth")
    public String AverageBalancePerMonth;

    @ResponseField("Envelope.Body.getCADetailsRs.accountDetails.openDate")
    public String OpenDate;

    @ResponseField("Envelope.Body.getCADetailsRs.accountDetails.accountName")
    public String AccountName;

    @ResponseField("Envelope.Body.getCADetailsRs.accountDetails.accountType")
    public String AccountType;

    @ResponseField("Envelope.Body.getCADetailsRs.accountDetails.branchName")
    public String BranchName;

    @ResponseField("Envelope.Body.getCADetailsRs.accountDetails.accountNumber")
    public String AccountNumber;

    @ResponseField("Envelope.Body.getCADetailsRs.customerNumber")
    public String Cif;

}
