package core.services.soap.requests.newESB;

import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.AbstractRequest;
import core.utils.evaluationManager.ScriptUtils;

@RequestEndpoint("http://10.37.16.126:6100/prod/ibank/soap/vn/service/v2") //v1
@RequestTemplate("getCardUsage")
public class CardUsageRequest extends AbstractRequest {
    @RequestField
    public String SessionId = java.util.UUID.randomUUID().toString();
    @RequestField
    public String ClientId = java.util.UUID.randomUUID().toString();
    @RequestField
    public String RequestTime = ScriptUtils.now("yyyy-MM-dd'T'hh:mm:ss");

    @RequestField
    public String CardNo;

    public CardUsageRequest(String cardNo){
        this.CardNo = cardNo;
    }

    @ResponseField("Envelope.Body.getCardUsageRs.CardUsageInfo.ProductName")
    public String CardName;

    @ResponseField("Envelope.Body.getCardUsageRs.CardUsageInfo.CardSpdAmtMaxAmount")
    public String CurrentSpendingLimit;

    @ResponseField("Envelope.Body.getCardUsageRs.CardUsageInfo.OpenDate")
    public String FromDate;

    @ResponseField("Envelope.Body.getCardUsageRs.CardUsageInfo.PrSpdAmtMaxAmount")
    public String NotExceedingLimit;
}
