package core.services.soap.requests.W4Request;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;

@RequestTemplate("GetW4CurrentDate")
public class GetW4CurrentDate extends W4Request{

    public GetW4CurrentDate(){}

    @ResponseField("Envelope.Body.GetW4CurrentDateResponse.GetW4CurrentDateResult")
    public String W4CurrentDate;
}
