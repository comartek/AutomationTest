package core.services.soap.subResponses;

import io.restassured.path.xml.element.Node;

import java.util.HashMap;


/**
 * Created by Akarpenko on 19.02.2018.
 */
public class MainAccountInfo {

    public static boolean isValidCurrentAccount(Node acc)
    {
        String AccountType = acc.getPath("Account.AccountType").toString();
        if (!AccountType.equals("AC"))
            return false;
        String ProductId_str =acc.getPath("ProductInfo.ProductId").toString();
        Integer ProductId = Integer.parseInt(ProductId_str);
        if (!((ProductId!=1002 && ProductId!=1003 && ProductId!=1018 && ProductId<2000)||(ProductId==6614)))
            return false;

        String AccountStatus = acc.getPath("AccountStatus").toString();

//        if (!(AccountStatus.toUpperCase().equals("ACTIVE")||AccountStatus.toUpperCase().equals("INACTIVE")))
        if (!(AccountStatus.toUpperCase().equals("ACTIVE")))
            return false;


        String ProductName = acc.getPath("ProductInfo.ProductName").toString();

//        TODO: undocumented condition, added to fit actual results of Transfer testcases. Need to check condition list!!
//        if (!(ProductName.toUpperCase().equals("DEMAND DEPOSIT")))
//            return false;

        return true;

    }

    public static boolean isValidOnlineSavingAZ(Node acc) {
        String AccountType = acc.getPath("Account.AccountType").toString();
        if (!AccountType.equals("AC"))
            return false;
        String ProductId_str = acc.getPath("ProductInfo.ProductId").toString();
        Integer ProductId = Integer.parseInt(ProductId_str);
        if ((ProductId == 6601 || ProductId == 6602 || ProductId == 6603 || ProductId == 6604 ||
                ProductId == 6610 || ProductId == 6611))
            return true;

        return false;
    }

    public static boolean isValidOnlineSavingLD(Node acc) {
        String ProductId_str = acc.getPath("ProductInfo.ProductId").toString();
        Integer ProductId = Integer.parseInt(ProductId_str);
        if (ProductId==21017||ProductId==21018||ProductId==21014)
            return true;

        return false;
    }

    public static boolean isValidLoan(Node acc) {
        String ProductId_str = acc.getPath("ProductInfo.ProductId").toString();
        Integer ProductId = Integer.parseInt(ProductId_str);
        if (ProductId == 21050 || ProductId == 21055 )
            return true;

        return false;
    }

    public static boolean isValidCard(Node acc) {
        String Type_str = acc.getPath("CardInfo.Type").toString();
        String CardType_str = acc.getPath("CardInfo.CardType").toString();
        String CardStatus_str = acc.getPath("CardInfo.CardStatus").toString();

        if (Type_str.equals("CC")  && (CardType_str.equals("PRIN") || CardType_str.equals("SUB"))
                &&  CardStatus_str.equals("Card OK"))
            return true;

        return false;
    }

    public static HashMap<String, String> getInfoAccountAndOnlineSavingAZ(Node acc){

        HashMap<String, String> map = new HashMap<>();

        //RESTASSURED XMLPATH

        map.put("soap_AccountNo", acc.getPath("Account.AccountNo").toString());
        map.put("soap_AccountType", acc.getPath("Account.AccountType").toString());
        map.put("soap_AccountStatus", acc.getPath("AccountStatus").toString());
        map.put("soap_AccountCurrency", acc.getPath("AccountCurrency").toString());
        map.put("soap_AccountBalance", acc.getPath("AccountBalance").toString());
        map.put("soap_WorkingBalance", acc.getPath("WorkingBalance").toString());
        map.put("soap_ProductId", acc.getPath("ProductInfo.ProductId").toString());
        map.put("soap_ProductName", acc.getPath("ProductInfo.ProductName").toString());


        return map;
    }

    public static HashMap<String, String> getInfoLoanAndOnlineSavingLD(Node acc){

        HashMap<String, String> map = new HashMap<>();

        //RESTASSURED XMLPATH

        map.put("soap_AccountNo", acc.getPath("Account.AccountNo").toString());
        map.put("soap_AccountType", acc.getPath("Account.AccountType").toString());
        //map.put("soap_AccountStatus", acc.getPath("AccountStatus").toString());
        map.put("soap_AccountCurrency", acc.getPath("AccountCurrency").toString());
        map.put("soap_AccountBalance", acc.getPath("AccountBalance").toString());
        //map.put("soap_WorkingBalance", acc.getPath("WorkingBalance").toString());
        map.put("soap_ProductId", acc.getPath("ProductInfo.ProductId").toString());

        return map;
    }

    public static HashMap<String, String> getInfoCard(Node acc){

        //RESTASSURED XMLPATH

        HashMap<String, String> map = new HashMap<>();
        map.put("soap_AccountNo", acc.getPath("CardInfo.CardNumber").toString());
        map.put("soap_AccountType", acc.getPath("CardInfo.CardType").toString());
        map.put("soap_AccountBalance", acc.getPath("CardInfo.CardBalance.AvailableBalance").toString());
        map.put("soap_Type", acc.getPath("CardInfo.Type").toString());
        map.put("soap_AccountCurrency", acc.getPath("CardInfo.CardBalance.Currency").toString());
        map.put("soap_ProductName", acc.getPath("CardInfo.ProductInfo.ProductName").toString());
        map.put("soap_ProductId", acc.getPath("CardInfo.ProductInfo.ProductId").toString());
        map.put("soap_EcommerceStatus", acc.getPath("CardInfo.EcommerceStatus").toString());
        map.put("soap_BranchNo", acc.getPath("CustomerInfo.BranchInfo.BranchNo").toString());


        return map;
    }

}
