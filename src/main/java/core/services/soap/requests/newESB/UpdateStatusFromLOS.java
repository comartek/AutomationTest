package core.services.soap.requests.newESB;

import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.AbstractRequest;
import core.utils.evaluationManager.ScriptUtils;

@RequestEndpoint("http://10.37.16.149:8080/cb/cxfservices/ns/NSUPLEndPoint")
@RequestTemplate("updateStatusFromLOS")
public class UpdateStatusFromLOS extends AbstractRequest {

    @RequestField
    public String loanDocNumber;

    @RequestField
    public String ocbReferenceNumber;

    @RequestField
    public String customerName;

    @RequestField
    public String legalId;

    @RequestField
    public String permanentAddress;

    @RequestField
    public String customerEmail;

    @RequestField
    public String amountApproved;

    @RequestField
    public String termAprroved;

    @RequestField
    public String rateApproved;

    public UpdateStatusFromLOS(String loanDocNumber, String ocbReferenceNumber, String customerName, String legalId, String permanentAddress, String customerEmail, String amountApproved, String termAprroved, String rateApproved) {
        this.loanDocNumber = loanDocNumber;
        this.ocbReferenceNumber = ocbReferenceNumber;
        this.customerName = customerName;
        this.legalId = legalId;
        this.permanentAddress = permanentAddress;
        this.customerEmail = customerEmail;
        this.amountApproved = amountApproved;
        this.termAprroved = termAprroved;
        this.rateApproved = rateApproved;
    }

    @ResponseField("Envelope.Body.updateUPLStatusFromLosResponse.updateUPLStatusFromLosResponse.statusCode")
    public String UPLStatus;
}
