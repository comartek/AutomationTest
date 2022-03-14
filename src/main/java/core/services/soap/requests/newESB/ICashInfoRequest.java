package core.services.soap.requests.newESB;

import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.AbstractRequest;
import core.utils.evaluationManager.ScriptUtils;

@RequestTemplate("getICashInfo")
public class ICashInfoRequest extends ESBRequest {

    @RequestField
    public String cif;

    @RequestField
    public String amount;

    public ICashInfoRequest(String cif,String amount){
        this.cif= cif;
        this.amount= amount;
    }

    @ResponseField("Envelope.Body.getICashInfoRs.iCastVnd")
    public String iCastVnd;

    @ResponseField("Envelope.Body.getICashInfoRs.iCashResidual")
    public String iCashResidual;

    @ResponseField("Envelope.Body.getICashInfoRs.iCastRate")
    public String iCastRate;

    @ResponseField("Envelope.Body.getICashInfoRs.iCashPay")
    public String iCashPay;

}
