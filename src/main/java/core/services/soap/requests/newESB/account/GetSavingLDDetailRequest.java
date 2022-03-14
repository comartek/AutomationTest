package core.services.soap.requests.newESB.account;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.requests.newESB.ESBRequest;

/**
 * Created by Denis on 21.04.2018.
 */
@RequestTemplate("getESADetail")
public class GetSavingLDDetailRequest extends ESBRequest {
    @RequestField
    String contractNo;

    public GetSavingLDDetailRequest(String contractNo) {
        this.contractNo = contractNo;
    }

    @ResponseField("Envelope.Body.getESADetailRs.easySavingAccount.productInfo.productId")
    public String ProductId;

    @ResponseField("Envelope.Body.getESADetailRs.easySavingAccount.branchInfo.branchNo")
    public String BranchNo;

    @ResponseField("Envelope.Body.getESADetailRs.easySavingAccount.accountInfo.valueDate")
    public String ValueDate;

    @ResponseField("Envelope.Body.getESADetailRs.easySavingAccount.amountInfo.principleAmount")
    public String PrincipleAmount;

    @ResponseField("Envelope.Body.getESADetailRs.easySavingAccount.accountInfo.interestRate")
    public String InterestRate;

    @ResponseField("Envelope.Body.getESADetailRs.easySavingAccount.accountInfo.tenor")
    public String Tenor;

    @ResponseField("Envelope.Body.getESADetailRs.easySavingAccount.amountInfo.accruedInterest")
    public String AccruedInterest;

    @ResponseField("Envelope.Body.getESADetailRs.easySavingAccount.accountInfo.maturityDate")
    public String MaturityDate;

    @Override
    protected void parseResponse(){
        super.parseResponse();
        if (AccruedInterest.isEmpty())
            AccruedInterest = "0";
    }
}
