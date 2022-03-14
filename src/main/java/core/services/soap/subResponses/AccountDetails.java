package core.services.soap.subResponses;

import io.restassured.path.xml.XmlPath;

import java.util.HashMap;

/**
 * Created by Akarpenko on 19.02.2018.
 */
public class AccountDetails {

    public static HashMap<String, String> getCurrentAccountInfo(String response){

        HashMap<String, String> map = new HashMap<>();

        //RESTASSURED XMLPATH

        XmlPath path = XmlPath.from(response);

        map.put("soap_AccountCurrency", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CurrentAccountInfo.AccountInfo.AccountCurrency").toString());

        map.put("soap_ProductId", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CurrentAccountInfo.AccountInfo.ProductInfo.ProductId").toString());

        map.put("soap_BranchNo", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CurrentAccountInfo.AccountInfo.BranchInfo.BranchNo").toString());

        map.put("soap_Balance", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CurrentAccountInfo.Balance").toString());

        map.put("soap_AvailableBalance", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CurrentAccountInfo.AvailableBalance").toString());

        map.put("soap_AverageBalancePerMonth", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CurrentAccountInfo.AverageBalancePerMonth").toString());

        map.put("soap_OpenDate", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CurrentAccountInfo.AccountInfo.OpenDate").toString());

        return map;
    }

    public static HashMap<String, String> getCreditCardInfo(String response){

        HashMap<String, String> map = new HashMap<>();

        //RESTASSURED XMLPATH

        XmlPath path = XmlPath.from(response);

        map.put("soap_ContractNumber", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.ContractNumber").toString());

        map.put("soap_ProductName", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.ProductInfo.ProductName").toString());

        map.put("soap_CardStatus", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.CardStatus").toString());

        map.put("soap_CreditLimit_Amount", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.CardBalance.CreditLimit").toString());

        map.put("soap_Available_Amount", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.CardBalance.AvailableBalance").toString());

        map.put("soap_BlockAmount_Amount", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.CardBalance.BlockAmount").toString());

        map.put("soap_OutstandingBalance", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.CardBalance.SumOfOSAmount").toString());

        map.put("soap_ProductName", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.ProductInfo.ProductName").toString());

        map.put("soap_CardNumber", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.CardNumber").toString());

        map.put("soap_BlockedAmount", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.CardBalance.BlockAmount").toString());

        map.put("soap_StatementBalance", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.CardBalance.OutstandingAmount").toString());

        map.put("soap_BranchNo", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.BranchInfo.BranchNo").toString());

        map.put("soap_InterestRate", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.InterestRate").toString());

        map.put("soap_CardID", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "CreditCardInfo.CardID").toString());

        return map;
    }

    public static HashMap<String, String> getSavingAccountInfo(String response){

        HashMap<String, String> map = new HashMap<>();

        //RESTASSURED XMLPATH

        XmlPath path = XmlPath.from(response);

        map.put("soap_ProductId", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "SavingAccountInfo.AccountInfo.ProductInfo.ProductId").toString());

        map.put("soap_BranchNo", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "SavingAccountInfo.AccountInfo.BranchInfo.BranchNo").toString());

        map.put("soap_ValueDate", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "SavingAccountInfo.AccountInfo.ValueDate").toString());

        map.put("soap_PrincipleAmount", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "SavingAccountInfo.PrincipleAmount").toString());

        map.put("soap_InterestRate", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "SavingAccountInfo.InterestRate").toString());

        map.put("soap_Tenor", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "SavingAccountInfo.Tenor").toString());


        String s_AI = path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "SavingAccountInfo.AccruedInterest").toString();

        map.put("soap_AccruedInterest", s_AI.equals("")?"0":s_AI);

        map.put("soap_MaturityDate", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "SavingAccountInfo.MaturityDate").toString());



        return map;
    }

    public static HashMap<String, String> getLoanContractInfo(String response){

        HashMap<String, String> map = new HashMap<>();

        //RESTASSURED XMLPATH

        XmlPath path = XmlPath.from(response);

        map.put("soap_AccountCurrency", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.AccountInfo.AccountCurrency").toString());

        map.put("soap_BranchNo", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.AccountInfo.BranchInfo.BranchNo").toString());

        map.put("soap_Tenor", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.Tenor").toString());

        map.put("soap_PrincipleAmount", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.PrincipleAmount").toString());

        map.put("soap_InterestRate", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.InterestRate").toString());

        map.put("soap_MaturityDate", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.MaturityDate").toString());

        map.put("soap_OutstandingAmount", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.OutstandingAmount").toString());

        map.put("soap_ThisPeriodPrincipleAmount", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.ThisPeriodPrincipleAmount").toString());

        map.put("soap_ThisPeriodInterestAmount", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.ThisPeriodInterestAmount").toString());

        map.put("soap_NextPayPrincipleDate", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.NextPayPrincipleDate").toString());

        map.put("soap_NextPayInterestDate", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.NextPayInterestDate").toString());

        map.put("soap_PrincipleAmountPastDue", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.PrincipleAmountPastDue").toString());

        map.put("soap_InterestAmountPastDue", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.InterestAmountPastDue").toString());

        map.put("soap_PenaltyInterestAmount", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.PenaltyInterestAmount").toString());

        map.put("soap_TotalPrincipleAndInterestPastDue", path.get(
                "Envelope.Body.retrieveSalesProductServiceAgreementResponse.BodyResponse." +
                        "LoanContractInfo.TotalPrincipleAndInterestPastDue").toString());



        return map;
    }



}
