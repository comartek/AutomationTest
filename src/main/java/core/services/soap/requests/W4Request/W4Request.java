package core.services.soap.requests.W4Request;

import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.services.soap.AbstractRequest;

/**
 * Created by thuynt152.
 */
@RequestEndpoint("http://10.37.0.118/CBIWebservice_0125/cbiWebService.asmx")
public abstract class W4Request extends AbstractRequest {
    @RequestField
    public String sessionId = java.util.UUID.randomUUID().toString();

}
