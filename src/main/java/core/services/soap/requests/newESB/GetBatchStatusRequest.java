package core.services.soap.requests.newESB;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;

@RequestTemplate("getBatchStatus")
public class GetBatchStatusRequest extends ESBRequest {
    @RequestField
    String referenceId;


    public GetBatchStatusRequest(String referID) {
        this.referenceId = referID;
    }

    @ResponseField("Envelope.Body.getBatchStatusRs.respondStatus")
    public String Status;
}
