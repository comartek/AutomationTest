package core.services.soap.requests.newESB;

import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.AbstractRequest;

@RequestEndpoint("http://10.37.24.45:8080/csfweb/services/JarvisAppCancel")
@RequestTemplate("getJarvisAppCancel")
public class GetJarvisAppCancel extends AbstractRequest {

    @RequestField
    public String refNo;

    @RequestField
    public String applicationID;

    @RequestField
    public String applicationNo;

    public GetJarvisAppCancel(String refNo, String applicationID, String applicationNo) {
        this.refNo = refNo;
        this.applicationID = applicationID;
        this.applicationNo = applicationNo;
    }

    @ResponseField("Envelope.Body.getJarvisAppCancelResDetails.JarvisResponseDetails.HeaderType.Status")
    public String Status;
    @ResponseField("Envelope.Body.getJarvisAppCancelResDetails.JarvisResponseDetails.HeaderType.Message")
    public String Message;
}
