package core.services.soap.requests.newESB.transfer;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.requests.newESB.ESBRequest;

@RequestTemplate("getFeeInfo")
public class GetTransferFeeRequest extends ESBRequest {

    @RequestField
    String servicePackage;
    @RequestField
    String debitAccount;
    @RequestField
    String channel;
    @RequestField
    String debitLocation;
    @RequestField
    String creditLocation;
    @RequestField
    String transactionAmount;
    @RequestField
    String transactionType;

    public GetTransferFeeRequest(String servicePackage,String debitAccount,
                                 String channel,String debitLocation,String creditLocation,
                                 String transactionAmount,String transactionType){
        this.servicePackage = servicePackage;
        this.debitAccount = debitAccount;
        this.channel = channel;
        this.debitLocation = debitLocation;
        this.creditLocation = creditLocation;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
    }

    @ResponseField("Envelope.Body.getFeeInfoRs.commission.amount")
    public String amount;
    @ResponseField("Envelope.Body.getFeeInfoRs.commission.code")
    public String code;
    @ResponseField("Envelope.Body.getFeeInfoRs.commission.type")
    public String type;
    @ResponseField("Envelope.Body.getFeeInfoRs.commission.VAT")
    public String VAT;
    @ResponseField("Envelope.Body.getFeeInfoRs.commission.description")
    public String description;

    @Override
    public String responseAsString(){
        getResponse();
        return amount;

    }

}
