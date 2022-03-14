package core.services.soap.requests.oldESB;

import core.annotations.RequestTemplate;
import io.restassured.path.xml.element.Node;

import java.util.HashMap;
import java.util.Map;

@RequestTemplate("retrieveCustomerAgreementOverDraft")
public class GetNoOverdraftLListRequest extends AccountListRequest {

    public GetNoOverdraftLListRequest(String cif){
        this.CIFNo = cif;
    }
    public GetNoOverdraftLListRequest(String cif,String excludedAccounts,String includedAccounts){
        this.CIFNo = cif;
        this.excludedAccounts = excludedAccounts;
        this.includedAccounts = includedAccounts;
    }
    public GetNoOverdraftLListRequest(String cif,String excludedAccounts,String includedAccounts,String additionalConditions){
        this(cif, excludedAccounts, includedAccounts);
        this.additionalConditions=additionalConditions;
    }


    @Override
    protected String getAccountXmlPath() {
        return "Envelope.Body.retrieveCustomerAgreementOverDraftResponse.ResponseStatus";
    }

    @Override
    protected String getAccountNumberXmlPath() {
        return "GlobalErrorDescription";
    }

    @Override
    protected boolean isVisibleAccount(Node account) {
        return true;
    }

    @Override
    protected Map<String, String> parseAccountInfo(Node account) {
        HashMap<String, String> map = new HashMap<>();
        map.put("soap_Status", account.getPath("Status").toString());
        map.put("soap_ErrorCode", account.getPath("GlobalErrorCode").toString());
        map.put("soap_ErrorText", account.getPath("GlobalErrorDescription").toString());
        return map;
    }
}
