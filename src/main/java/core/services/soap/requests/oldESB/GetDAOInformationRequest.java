package core.services.soap.requests.oldESB;

import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.AbstractRequest;
import core.utils.evaluationManager.ScriptUtils;

@RequestEndpoint("http://10.37.16.126:7800/service/vn/ba251/bd295/employeedatamanagement/1")
@RequestTemplate("retrieveEmployeeRegistrationCoreSystemProfile")
public class GetDAOInformationRequest extends AbstractRequest {
    @RequestField
    String ServiceVersion="1";
    @RequestField
    String MessageId=java.util.UUID.randomUUID().toString();
    @RequestField
    String TransactionId=java.util.UUID.randomUUID().toString();
    @RequestField
    String MessageTimestamp= ScriptUtils.now("yyyy-MM-dd'T'hh:mm:ss");
    @RequestField
    String SourceAppID="VSII";
    @RequestField
    String TargetAppID="ESB";
    @RequestField
    String UserId="OCB";
    @RequestField
    String UserRole="tester";
    @RequestField
    String UserPassword="GkXry4DzgMcjyVU56ujinA==";

    @RequestField
    String employeeID;

    public GetDAOInformationRequest(String employeeID){
        this.employeeID = employeeID;
    }

    @ResponseField("Envelope.Body.retrieveEmployeeRegistrationCoreSystemProfileResponse." +
            "BodyResponse.EmployeeCoreSystemProfile.EmployeeName")
    public String EmployeeName;

    @Override
    public String responseAsString(){
        getResponse();
        return EmployeeName;
    }
}
