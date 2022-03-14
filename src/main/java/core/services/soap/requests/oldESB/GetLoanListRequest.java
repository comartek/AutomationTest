package core.services.soap.requests.oldESB;

import core.annotations.RequestTemplate;
import io.restassured.path.xml.element.Node;

import java.util.HashMap;
import java.util.Map;

@RequestTemplate("retrieveCustomerAgreementLoan")
public class GetLoanListRequest extends AccountListRequest {
    public GetLoanListRequest(String cif){
        this.CIFNo = cif;
    }
    public GetLoanListRequest(String cif,String excludedAccounts,String includedAccounts){
        this.CIFNo = cif;
        this.excludedAccounts = excludedAccounts;
        this.includedAccounts = includedAccounts;
    }
    public GetLoanListRequest(String cif,String excludedAccounts,String includedAccounts,String additionalConditions){
        this(cif, excludedAccounts, includedAccounts);
        this.additionalConditions=additionalConditions;
    }

    @Override
    protected String getAccountXmlPath() {
        return "Envelope.Body.retrieveCustomerAgreementLoanResponse.BodyResponse.LoanAccountDetail";
    }

    @Override
    protected String getAccountNumberXmlPath() {
        return "Account.AccountNo";
    }

    @Override
    protected boolean isVisibleAccount(Node account) {
        String ProductId_str = account.getPath("ProductInfo.ProductId").toString();
        Integer ProductId = Integer.parseInt(ProductId_str);
        if (ProductId == 21050 || ProductId == 21055 || ProductId == 21052)
            return true;

        return false;
    }

    @Override
    protected Map<String, String> parseAccountInfo(Node account) {
        HashMap<String, String> map = new HashMap<>();
        map.put("soap_AccountNo", account.getPath("Account.AccountNo").toString());
        map.put("soap_AccountType", account.getPath("Account.AccountType").toString());
        //map.put("soap_AccountStatus", account.getPath("AccountStatus").toString());
        map.put("soap_AccountCurrency", account.getPath("AccountCurrency").toString());
        map.put("soap_AccountBalance", account.getPath("AccountBalance").toString());
        //map.put("soap_WorkingBalance", account.getPath("WorkingBalance").toString());
        map.put("soap_ProductId", account.getPath("ProductInfo.ProductId").toString());

        return map;
    }
}