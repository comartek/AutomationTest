package core.services.soap.requests.newESB;

import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.AbstractRequest;
import core.utils.evaluationManager.ScriptUtils;

@RequestEndpoint("http://10.37.16.126:6100/prod/ibank/soap/vn/service/v2") //v1
@RequestTemplate("getCardCashWithdrawalFee")
public class CardCashWithdrawalFeeRequest extends AbstractRequest {
    @RequestField
    public String SessionId = java.util.UUID.randomUUID().toString();
    @RequestField
    public String ClientId = java.util.UUID.randomUUID().toString();
    @RequestField
    public String RequestTime = ScriptUtils.now("yyyy-MM-dd'T'hh:mm:ss");

    @RequestField
    public String CardNumber;

    public CardCashWithdrawalFeeRequest(String cardNo){
        this.CardNumber = cardNo;
    }

    @ResponseField("Envelope.Body.getCardCashWithdrawalFeeRs.FeeInfo.CardStatus")
    public String CardStatus;

    @ResponseField("Envelope.Body.getCardCashWithdrawalFeeRs.FeeInfo.CashLimit")
    public String CashLimit;

    @ResponseField("Envelope.Body.getCardCashWithdrawalFeeRs.FeeInfo.Fee")
    public String Fee;

    @ResponseField("Envelope.Body.getCardCashWithdrawalFeeRs.FeeInfo.FeeMinAmount")
    public String FeeMinAmount;

    @ResponseField("Envelope.Body.getCardCashWithdrawalFeeRs.FeeInfo.BranchCode")
    public String BranchCode;
}
