package core.services.soap.requests.oldESB;

import core.annotations.RequestTemplate;
import io.restassured.path.xml.element.Node;

import java.util.HashMap;
import java.util.Map;

@RequestTemplate("retrieveCustomerAgreementAccount")
public class GetSavingAZListRequest extends AccountListRequest {
    public GetSavingAZListRequest(String cif){
        this.CIFNo = cif;
    }
    public GetSavingAZListRequest(String cif,String excludedAccounts,String includedAccounts){
        this.CIFNo = cif;
        this.excludedAccounts = excludedAccounts;
        this.includedAccounts = includedAccounts;
    }
    public GetSavingAZListRequest(String cif,String excludedAccounts,String includedAccounts,String additionalConditions){
        this(cif, excludedAccounts, includedAccounts);
        this.additionalConditions=additionalConditions;
    }

    @Override
    protected String getAccountXmlPath() {
        return "Envelope.Body.retrieveCustomerAgreementAccountResponse.BodyResponse.AccountDetail";
    }

    @Override
    protected String getAccountNumberXmlPath() {
        return "Account.AccountNo";
    }

    @Override
    protected boolean isVisibleAccount(Node account) {
        String AccountType = account.getPath("Account.AccountType").toString();
        if (!AccountType.equals("AC"))
            return false;
        String ProductId_str = account.getPath("ProductInfo.ProductId").toString();
        Integer ProductId = Integer.parseInt(ProductId_str);
        if ((ProductId == 6601 || ProductId == 6602 || ProductId == 6603 || ProductId == 6604 ||
                ProductId == 6610 || ProductId == 6611))
            return true;

        return false;
    }

    @Override
    protected Map<String, String> parseAccountInfo(Node account) {
        HashMap<String, String> map = new HashMap<>();

        map.put("soap_AccountNo", account.getPath("Account.AccountNo").toString());
        map.put("soap_AccountType", account.getPath("Account.AccountType").toString());
        map.put("soap_AccountStatus", account.getPath("AccountStatus").toString());
        map.put("soap_AccountCurrency", account.getPath("AccountCurrency").toString());
        map.put("soap_AccountBalance", account.getPath("AccountBalance").toString());
        map.put("soap_WorkingBalance", account.getPath("WorkingBalance").toString());
        map.put("soap_ProductId", account.getPath("ProductInfo.ProductId").toString());
        map.put("soap_ProductName", account.getPath("ProductInfo.ProductName").toString());

        return map;
    }
}
