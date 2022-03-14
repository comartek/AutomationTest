package core.services.soap.requests.newESB.account;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.requests.newESB.ESBRequest;

@RequestTemplate("getSavingDetail")
public class GetSavingDetailRequest extends ESBRequest {
    @RequestField
    String contractId;


    public GetSavingDetailRequest(String accountNumber) {
        this.contractId = accountNumber;
    }

    @ResponseField("Envelope.Body.getSavingDetailRs.nominatedAcct")
    public String nominatedAcct;

    @ResponseField("Envelope.Body.getSavingDetailRs.totalInt")
    public String totalInt;

    @ResponseField("Envelope.Body.getSavingDetailRs.totalAmtEndTerm")
    public String totalAmtEndTerm;

    @ResponseField("Envelope.Body.getSavingDetailRs.totalAmtLost")
    public String totalAmtLost;

    @ResponseField("Envelope.Body.getSavingDetailRs.intRateKKH")
    public String intRateKKH;

    @ResponseField("Envelope.Body.getSavingDetailRs.principal")
    public String principal;

    @ResponseField("Envelope.Body.getSavingDetailRs.currInt")
    public String currInt;

    @ResponseField("Envelope.Body.getSavingDetailRs.term")
    public String term;

    @ResponseField("Envelope.Body.getSavingDetailRs.valueDate")
    public String valueDate;

    @ResponseField("Envelope.Body.getSavingDetailRs.maturityDate")
    public String maturityDate;


}
