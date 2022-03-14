package core.services.soap.requests.oldESB;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import io.restassured.path.xml.element.Node;

import java.util.HashMap;
import java.util.Map;

@RequestTemplate("retrieveCustomerAgreementCard")
public class GetCardListRequest extends AccountListRequest {

    @RequestField
    protected String LegalId;

    public GetCardListRequest(String legalId){
        this.LegalId = legalId;
    }
    public GetCardListRequest(String legalId,String excludedAccounts,String includedAccounts){
        this.LegalId = legalId;
        this.excludedAccounts = excludedAccounts;
        this.includedAccounts = includedAccounts;
    }
    public GetCardListRequest(String legalId,String excludedAccounts,String includedAccounts,String additionalConditions){
        this(legalId, excludedAccounts, includedAccounts);
        this.additionalConditions=additionalConditions;
    }

    @Override
    protected String getAccountXmlPath() {
        return "Envelope.Body.retrieveCustomerAgreementCardResponse.BodyResponse.CardSummary";
    }

    @Override
    protected String getAccountNumberXmlPath() {
        return "CardInfo.CardNumber";
    }


    @Override
    protected boolean isVisibleAccount(Node card) {
        String Type_str = card.getPath("CardInfo.Type").toString();
        String CardType_str = card.getPath("CardInfo.CardType").toString();
        String CardStatus_str = card.getPath("CardInfo.CardStatus").toString();
        String ProductId_str = card.getPath("CardInfo.ProductInfo.ProductId").toString();

        if (ProductId_str.equals("ISSMCCC"))
            return false;
        if (Type_str.equals("CC")  && (CardType_str.equals("PRIN") || CardType_str.equals("SUP"))
                &&  (CardStatus_str.equals("Card OK")||CardStatus_str.equals("Call Issuer")))
            return true;

        return false;
    }

    @Override
    protected Map<String, String> parseAccountInfo(Node account) {
        //RESTASSURED XMLPATH

        HashMap<String, String> map = new HashMap<>();
        map.put("soap_AccountNo", account.getPath("CardInfo.CardNumber").toString());
        map.put("soap_AccountType", account.getPath("CardInfo.CardType").toString());
        map.put("soap_CardStatus", account.getPath("CardInfo.CardStatus").toString());
        map.put("soap_AccountBalance", account.getPath("CardInfo.CardBalance.AvailableBalance").toString());
        map.put("soap_Type", account.getPath("CardInfo.Type").toString());
        map.put("soap_AccountCurrency", account.getPath("CardInfo.CardBalance.Currency").toString());
        map.put("soap_ProductName", account.getPath("CardInfo.ProductInfo.ProductName").toString());
        map.put("soap_ProductId", account.getPath("CardInfo.ProductInfo.ProductId").toString());
        map.put("soap_EcommerceStatus", account.getPath("CardInfo.EcommerceStatus").toString());
        map.put("soap_BranchNo", account.getPath("CustomerInfo.BranchInfo.BranchNo").toString());
        map.put("soap_IDNo", account.getPath("CustomerInfo.IDInfo.IDNo").toString());


        return map;
    }
}