package core.services.soap.requests.newESB.account;

import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.AbstractRequest;
import core.services.soap.requests.newESB.ESBRequest;

@RequestTemplate("getCardDetailRq")
public class CardMinimumMaximumAmountRequest extends ESBRequest {
    @RequestField
    public String cardNo;

    public CardMinimumMaximumAmountRequest(String cardNo) {
        this.cardNo = cardNo;
    }

    @ResponseField("Envelope.Body.getCardDetailRs.balanceInfo.minimumPayDue")
    public String minimumPayDue;

    @ResponseField("Envelope.Body.getCardDetailRs.balanceInfo.outstandingAmount")
    public String outstandingAmount;
}
