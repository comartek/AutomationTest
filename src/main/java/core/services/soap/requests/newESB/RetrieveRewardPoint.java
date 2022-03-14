package core.services.soap.requests.newESB;


import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.utils.evaluationManager.ScriptUtils;

@RequestEndpoint("http://10.37.16.126:7800/service/vn/ba180/bd192/RewardPointsAccount/1")
@RequestTemplate("retrieveRewardPoint")
public class RetrieveRewardPoint extends ESBRequest {

    @RequestField
    public String cif;

    @RequestField
    public String MessageTimestamp;

    public RetrieveRewardPoint(String cif) {
        this.cif = cif;
        this.MessageTimestamp = ScriptUtils.now("yyyy-MM-dd'T'hh:mm:ss.SSS");
    }

    @ResponseField("Envelope.Body.retrieveRewardPointResponse.BodyResponse.Points.RedeemablePoints")
    public String redeemablePoints;

    @ResponseField("Envelope.Body.retrieveRewardPointResponse.BodyResponse.Points.AccummulatedPoints")
    public String accummulatedPoints;

    @ResponseField("Envelope.Body.retrieveRewardPointResponse.BodyResponse.Points.RedeemedPoints")
    public String redeemedPoints;

    @ResponseField("Envelope.Body.retrieveRewardPointResponse.BodyResponse.Points.RecordedPoints")
    public String recordedPoints;
}
