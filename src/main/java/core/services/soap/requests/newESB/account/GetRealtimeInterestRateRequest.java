package core.services.soap.requests.newESB.account;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.requests.newESB.ESBRequest;

@RequestTemplate("getRealtimeInterestRate")
public class GetRealtimeInterestRateRequest extends ESBRequest {

    @RequestField
    String productCategory;
    @RequestField
    String productCode;
    @RequestField
    String amount;
    @RequestField
    String currency;
    @RequestField
    String term;
    @RequestField
    String openDate;
    @RequestField
    String customerCode;
    @RequestField
    String channel;

    public GetRealtimeInterestRateRequest(String productCategory,String productCode,
                                 String amount,String currency,String term,
                                 String openDate,String customerCode,String channel){
        this.productCategory = productCategory;
        this.productCode = productCode;
        this.amount = amount;
        this.currency = currency;
        this.term = term;
        this.openDate = openDate;
        this.customerCode = customerCode;
        this.channel = channel;
    }

    @ResponseField("Envelope.Body.getRealtimeInterestRateRs.interestRate")
    public String interestRate;

    @ResponseField("Envelope.Body.getRealtimeInterestRateRs.baseRate")
    public String baseRate;
}
