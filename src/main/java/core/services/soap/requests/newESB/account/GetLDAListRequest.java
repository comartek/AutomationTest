package core.services.soap.requests.newESB.account;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.requests.newESB.ESBRequest;

@RequestTemplate("getLDAList")
public class GetLDAListRequest extends ESBRequest {
    @RequestField
    String cifNo;

    public GetLDAListRequest(String cifNo) {
        this.cifNo = cifNo;
    }

    @ResponseField("Envelope.Body.getLDAListRs.loanList.loan.loanType")
    public String loanType;

    @ResponseField("Envelope.Body.getLDAListRs.loanList.loan.lendingAmount")
    public String approved_Amount;

    @ResponseField("Envelope.Body.getLDAListRs.loanList.loan.currentOutstanding")
    public String currentOutstanding_Amount;

    @ResponseField("Envelope.Body.getLDAListRs.loanList.loan.maturityDate")
    public String maturity_Date;

}
