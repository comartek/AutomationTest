package core.services.soap.requests.newESB;

import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.AbstractRequest;
import core.utils.evaluationManager.ScriptUtils;

@RequestTemplate("getEvoucherInfo")
public class GetEvoucherInfoRequest extends ESBRequest {

    @RequestField
    public String evoucherCode;

    @RequestField
    public String transactionType;

    @RequestField
    public String transactionDetail;

    @RequestField
    public String amount;

    @RequestField
    public String currency;

    @RequestField
    public String customerCode;

    @RequestField
    public String accNumber;

    @RequestField
    public String category;

    @RequestField
    public String accType;

    @RequestField
    public String transactionDate;

    public GetEvoucherInfoRequest(String evoucherCode, String transactionType, String transactionDetail, String amount, String currency, String customerCode, String accNumber, String category, String accType, String transactionDate) {
        this.evoucherCode = evoucherCode;
        this.transactionType = transactionType;
        this.transactionDetail = transactionDetail;
        this.amount = amount;
        this.currency = currency;
        this.customerCode = customerCode;
        this.accNumber = accNumber;
        this.category = category;
        this.accType = accType;
        this.transactionDate = transactionDate;
    }

    @ResponseField("Envelope.Body.getEvoucherInfoRs.description")
    public String description;

}
