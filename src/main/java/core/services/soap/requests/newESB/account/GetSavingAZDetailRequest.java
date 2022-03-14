package core.services.soap.requests.newESB.account;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.requests.newESB.ESBRequest;

/**
 * Created by Denis on 21.04.2018.
 */

@RequestTemplate("getNSADetail")
public class GetSavingAZDetailRequest extends ESBRequest {
    @RequestField
    String accountNumber;

    public GetSavingAZDetailRequest(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @ResponseField("Envelope.Body.getNSADetailRs.savingAccount.productInfo.productId")
    public String ProductId;

    @ResponseField("Envelope.Body.getNSADetailRs.savingAccount.branchInfo.branchNo")
    public String BranchNo;

    @ResponseField("Envelope.Body.getNSADetailRs.savingAccount.accountInfo.valueDate")
    public String ValueDate;

    @ResponseField("Envelope.Body.getNSADetailRs.savingAccount.amountInfo.principleAmount")
    public String PrincipleAmount;

    @ResponseField("Envelope.Body.getNSADetailRs.savingAccount.accountInfo.interestRate")
    public String InterestRate;

    @ResponseField("Envelope.Body.getNSADetailRs.savingAccount.accountInfo.tenor")
    public String Tenor;

    @ResponseField("Envelope.Body.getNSADetailRs.savingAccount.amountInfo.accruedInterest")
    public String AccruedInterest;

    @ResponseField("Envelope.Body.getNSADetailRs.savingAccount.accountInfo.maturityDate")
    public String MaturityDate;

    @Override
    protected void parseResponse(){
        super.parseResponse();
        if (AccruedInterest.isEmpty())
            AccruedInterest = "0";
    }

}
