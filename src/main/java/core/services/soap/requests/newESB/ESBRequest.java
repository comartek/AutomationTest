package core.services.soap.requests.newESB;

import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.services.soap.AbstractRequest;

/**
 * Created by Denis on 20.04.2018.
 */
@RequestEndpoint("http://10.37.16.126:8110/OCBOut")
public abstract class ESBRequest extends AbstractRequest {
    @RequestField
    public String sessionId = java.util.UUID.randomUUID().toString();

}
