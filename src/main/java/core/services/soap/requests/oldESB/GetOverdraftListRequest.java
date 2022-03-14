package core.services.soap.requests.oldESB;

import core.annotations.RequestTemplate;
import core.services.soap.requests.oldESB.AccountListRequest;
import io.restassured.path.xml.element.Node;

import java.util.HashMap;
import java.util.Map;


@RequestTemplate("retrieveCustomerAgreementOverDraft")
public class GetOverdraftListRequest extends AccountListRequest {

    public GetOverdraftListRequest(String cif){
        this.CIFNo = cif;
    }
    public GetOverdraftListRequest(String cif,String excludedAccounts,String includedAccounts){
        this.CIFNo = cif;
        this.excludedAccounts = excludedAccounts;
        this.includedAccounts = includedAccounts;
    }
    public GetOverdraftListRequest(String cif,String excludedAccounts,String includedAccounts,String additionalConditions){
        this(cif, excludedAccounts, includedAccounts);
        this.additionalConditions=additionalConditions;
    }


    @Override
    protected String getAccountXmlPath() {
        return "Envelope.Body.retrieveCustomerAgreementOverDraftResponse.BodyResponse.OverDraftAccount";
    }

    @Override
    protected String getAccountNumberXmlPath() {
        return "AccountInfo.Account.AccountNo";
    }

    @Override
    protected boolean isVisibleAccount(Node account) {
        return true;
    }

    @Override
    protected Map<String, String> parseAccountInfo(Node account) {
        HashMap<String, String> map = new HashMap<>();
        map.put("soap_AccountNo", account.getPath("AccountInfo.Account.AccountNo").toString());
        map.put("soap_Balance", account.getPath("Balance").toString());
        map.put("soap_LinkedAccountNo", account.getPath("OverDraftInfo.CollateralRef.OnlineSavingAccount.AccountInfo.Account.AccountNo").toString());
        map.put("soap_StartingBalance", account.getPath("Balance").toString());
        map.put("soap_AmountPay", account.getPath("OverDraftInfo.AmountPay").toString());
        map.put("soap_Interest", account.getPath("OverDraftInfo.Interest").toString());
        map.put("soap_PaymentInterest", account.getPath("OverDraftInfo.PreIntAmt").toString());
        map.put("soap_IntOvd", account.getPath("OverDraftInfo.IntOvd").toString());
        map.put("soap_ExpiredDate", account.getPath("OverDraftInfo.ExpiredDate").toString());
        map.put("soap_AccountCurrency", account.getPath("AccountInfo.AccountCurrency").toString());
        map.put("soap_OverDraftType", account.getPath("OverDraftInfo.OverDraftType").toString());
        map.put("soap_OverdraftLimit", account.getPath("OverdraftLimit").toString());
        map.put("soap_ActiveDate", account.getPath("OverDraftInfo.ActiveDate").toString());
        map.put("soap_PrincipleAmount", account.getPath("OverDraftInfo.CollateralRef.OnlineSavingAccount.PrincipleAmount").toString());
        map.put("soap_Secured", account.getPath("OverDraftInfo.Secured").toString());
        map.put("soap_MinPayableAmount", account.getPath("OverDraftInfo.MinAmtPay"));
        map.put("soap_PaymentDate", account.getPath("OverDraftInfo.DateMinPay"));
        return map;
    }
}
