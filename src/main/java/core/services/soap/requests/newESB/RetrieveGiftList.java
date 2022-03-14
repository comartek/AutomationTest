package core.services.soap.requests.newESB;

import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.utils.evaluationManager.ScriptUtils;

@RequestEndpoint("http://10.37.16.126:7800/service/vn/ba180/bd192/RewardPointsAccount/1")
@RequestTemplate("retrieveGiftList")
public class RetrieveGiftList extends ESBRequest {

    @RequestField
    public String iCashFrom;

    @RequestField
    public String iCashTo;

    @RequestField
    public String MessageTimestamp;

    public RetrieveGiftList(String iCashFrom, String iCashTo) {
        this.iCashFrom = iCashFrom;
        this.iCashTo = iCashTo;
        this.MessageTimestamp = ScriptUtils.now("yyyy-MM-dd'T'hh:mm:ss.SSS");
    }

    @ResponseField("Envelope.Body.retrieveGiftListResponse.BodyResponse.Gift.GiftType")
    public String Category;

    @ResponseField("Envelope.Body.retrieveGiftListResponse.BodyResponse.Gift.GiftName")
    public String GiftName;

    @ResponseField("Envelope.Body.retrieveGiftListResponse.BodyResponse.Gift.Description")
    public String RedeemInformation;

}
