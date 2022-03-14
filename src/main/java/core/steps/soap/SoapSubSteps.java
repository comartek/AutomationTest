package core.steps.soap;

import core.annotations.StepExt;
import core.services.soap.requests.W4Request.GetW4CurrentDate;
import core.services.soap.requests.newESB.*;
import core.services.soap.requests.newESB.account.*;
import core.services.soap.requests.oldESB.*;
import core.steps.BaseSteps;
import core.steps.common.UtilSteps.UtilSubSteps;
import core.steps.sql.SQLSubSteps;
import core.utils.report.ReportUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.model.ExamplesTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Akarpenko on 12.02.2018.
 */
public class SoapSubSteps extends BaseSteps {

    UtilSubSteps utilSubSteps = new UtilSubSteps();

    @StepExt(value = "get list of visible accounts of type  \"{requestType}\" for CIF \"{cif}\"")
    //Variable "requestType" should equals "CurrentAccount" or "OnlineSavingAZ" or "Loan" or "OnlineSavingLD"
    public List<Map<String,String>> stepGetListOfVisibleAccounts(String requestType, String cif){
        return stepGetListOfVisibleAccounts(requestType, cif,null,null,null);
    }

    @StepExt(value = "get list of visible accounts of type  \"{requestType}\" for CIF \"{cif}\"")
    //Variable "requestType" should equals "CurrentAccount" or "OnlineSavingAZ" or "Loan" or "OnlineSavingLD" or "Overdraft"
    public List<Map<String,String>> stepGetListOfVisibleAccounts(String requestType, String cif,String excludedAccounts,String includedAccounts,String additionalConditions){

        List<Map<String,String>> visibleAccounts= null;

        switch (requestType) {
            case "CurrentAccount":
                visibleAccounts = new GetCurrentAccountListRequest(cif,excludedAccounts, includedAccounts,additionalConditions).getListOfVisibleAccounts();
                break;
            case "CreditCard":
                visibleAccounts = new GetCardListRequest(cif,excludedAccounts, includedAccounts,additionalConditions).getListOfVisibleAccounts();
                break;
            case "OnlineSavingAZ":
                visibleAccounts = new GetSavingAZListRequest(cif,excludedAccounts, includedAccounts,additionalConditions).getListOfVisibleAccounts();
                break;
            case "OnlineSavingLD":
                visibleAccounts = new GetSavingLDListRequest(cif,excludedAccounts, includedAccounts,additionalConditions).getListOfVisibleAccounts();
                break;
            case "Loan":
                visibleAccounts =  new GetLoanListRequest(cif,excludedAccounts, includedAccounts,additionalConditions).getListOfVisibleAccounts();
                break;
            case "Overdraft":
                visibleAccounts =  new GetOverdraftListRequest(cif,excludedAccounts, includedAccounts,additionalConditions).getListOfVisibleAccounts();
                break;
            case "NoOverdraft":
                visibleAccounts =  new GetNoOverdraftLListRequest(cif,excludedAccounts, includedAccounts,additionalConditions).getListOfVisibleAccounts();
                break;
            case "OverdueLoan":
//                visibleAccounts = new GetCustomerOverdueListRequest(cif,excludedAccounts, includedAccounts,additionalConditions).getListOfVisibleAccounts();
//                break;
            default:
                throw new RuntimeException("Unsupported account type: "+requestType);
        }
        return visibleAccounts;
    }

    @StepExt("information detail about account with type \"{accountType}\" is received by account number \"{accountNo}\"")
    //Variable "Type" should be one of "CurrentAccount", "CreditCard", "SavingAccount", "LoanContract"
    public void stepInformationDetailIsReceived(String accountType, String accountNo){

        Map<String,String> map;
        switch (accountType) {
            case "CurrentAccount":
                map = new GetCurrentAccountDetailRequest(accountNo).responseAsMap();
                break;
            case "CreditCard":
                map = new GetCardDetailRequest(accountNo).responseAsMap();
                break;
            case "SavingAccount":
                if (accountNo.startsWith("LD"))
                    map = new GetSavingLDDetailRequest(accountNo).responseAsMap();
                else
                    map = new GetSavingAZDetailRequest(accountNo).responseAsMap();
                break;
            case "LoanContract":
                map = new GetLoanDetailRequest(accountNo).responseAsMap();
                break;
            default:
                throw new RuntimeException("Unsupported account type: "+accountType);
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);

    }

    @StepExt("saving account detail is received by account number \"{accountNo}\"")
    public void stepSavingDetailIsReceived(String accountNo){
        Map<String,String> map = new GetSavingDetailRequest(accountNo).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);

    }

    @StepExt("info about \"{requestType}\" is received by CIF \"{cif}\"")
    //Variable "requestType" should equals "CurrentAccount" or "OnlineSavingAZ" or "Loan" or "OnlineSavingLD"
    public void stepInfoIsReceived(String requestType, String cif){

        Map<String,String> map=stepGetListOfVisibleAccounts(requestType, cif).get(0);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("list of loan UPL is received by cif \"{cifNo}\"")
    public void stepGetListOfLoanUPLIsReceived(String cifNo) {
        Map<String,String> map = new GetLDAListRequest(cifNo).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("info about Overdraft Status is received by reference ID \"{referID}\"")
    public void stepODStatusIsReceived(String referID){

        Map<String,String> map = new GetBatchStatusRequest(referID).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("info about Overdue loan is received by CIF \"{cif}\"")
    public void stepInfoOverdueIsReceived(String cif){

        Map<String,String> map = new GetCustomerOverdueListRequest(cif).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("search for unsecured overdraft by CIF \"{cif}\"")
    public void stepMultipleInfoIsReceived(String cif){

        List<Map<String, String>> mapList = stepGetListOfVisibleAccounts("Overdraft", cif);
        Map<String, String> map;

        A:
        for (int i = 0; i < mapList.size(); i++) {
            map = mapList.get(i);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                String y = "N";
                utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
                if(k == "soap_Secured"){
                    if(v.equals(y)) {
                        break A;
                    }
                }
            }
        }
    }

    @StepExt("receive account info with parameters")
    //Variable "requestType" should equals "CurrentAccount" or "OnlineSavingAZ" or "Loan" or "OnlineSavingLD" or "Overdraft"
    public void stepReceiveAccountInfoWithParameters(ExamplesTable parameters){
        ReportUtils.attachExampletable(parameters);

        Map<String,String> params_map=convertExampleTableToMap(parameters,"name","value");

        String requestType = params_map.get("RequestType");
        String cif = params_map.get("CIF");
        String excludedAccounts = params_map.get("ExcludedAccounts");
        String includedAccounts = params_map.get("IncludedAccounts");
        String additionalConditions = params_map.get("AdditionalConditions");

        Map<String,String> map = stepGetListOfVisibleAccounts(requestType, cif, excludedAccounts, includedAccounts,additionalConditions).get(0);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }

        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("receive account info with parameters")
    public void stepReceiveGiftListWithParameters(ExamplesTable parameters){
        ReportUtils.attachExampletable(parameters);

        Map<String,String> params_map=convertExampleTableToMap(parameters,"name","value");


        String iCashFrom = params_map.get("iCashFrom");
        String iCashTo = params_map.get("iCashTo");

        Map<String,String> map = new RetrieveGiftList(iCashFrom, iCashTo).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }

        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("receive card info with parameters")
    public void stepReceiveCardInfoWithParameters(ExamplesTable parameters) {
        ReportUtils.attachExampletable(parameters);

        Map<String, String> params_map = convertExampleTableToMap(parameters, "name", "value");

        String legalId = params_map.get("LegalID");
        String excludedAccounts = params_map.get("ExcludedCards");
        String includedAccounts = params_map.get("IncludedCards");
        //IgnoreVisibility - optional parameter - if false (default): only visible accounts will be found, if true: both visible and invisible accounts will be found
        //You only really need to specify this parameter if you want to use value "true" because value "false" is default and does not really need to be specified
        boolean ignoreVisibility = params_map.get("IgnoreVisibility") == null ? false : Boolean.valueOf(params_map.get("IgnoreVisibility"));

        Map<String, String> map = new HashMap<>();
        map = (ignoreVisibility)
                ? new GetCardListRequest(legalId,excludedAccounts, includedAccounts).getListOfAccounts().get(0)
                : new GetCardListRequest(legalId,excludedAccounts, includedAccounts).getListOfVisibleAccounts().get(0);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(), entry.getKey());
        }

        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("receive product list info with parameters")
    public void stepReceiveProductListInfoWithParameters(ExamplesTable parameters) {
        ReportUtils.attachExampletable(parameters);

        Map<String, String> params_map = convertExampleTableToMap(parameters, "name", "value");

        String CIF = params_map.get("CIF");
        String legalID = params_map.get("LegalID");
        String productType = params_map.get("ProductType");
        String includedAccount = params_map.get("IncludedAccount");

        Map<String,String> map = new getProductListRequest(CIF, legalID, productType, includedAccount).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }

        ReportUtils.attachVariables("Service variables", map);

    }

    @StepExt("info about card is received by LegalID/Visa Passport \"{id}\"")
    public void stepInfoAboutCardIsReceived(String id){
        Map<String,String> map = new GetCardListRequest(id).getListOfVisibleAccounts().get(0);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }

        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("number of visible accounts with type \"{requestType}\" is received by CIF \"{cif}\" and saved to variable \"{varName}\"")
    //Variable "requestType" should equals "CurrentAccount" or "OnlineSavingAZ" or "Loan" or "OnlineSavingLD"
    public void stepNumberOfVisibleAccountsSavedToVariable(String requestType, String cif,String varName) {

        List<Map<String, String>> visibleAccounts = stepGetListOfVisibleAccounts(requestType, cif);
        utilSubSteps.stepSaveVariable(String.valueOf(visibleAccounts.size()), varName);
    }

    @StepExt("DAO information is received by employeeID \"$id\" and saved to variable \"$variable\"")
    public void stepDAOInformationIsReceivedAndSavedToVar(String id, String variable){

        String value = new GetDAOInformationRequest(id).responseAsString();

        utilSubSteps.stepSaveVariable(value,variable);
    }

    @StepExt("number of visible credit cards is received by CIF \"{cif}\" and saved to variable \"{variable}\"")
    public void stepNumberOfVisibleCreditCardsIsReceivedByLegalIdAndSavedToVariable(String legalID, String varName){

        List<Map<String,String>> visibleCards = null;

        visibleCards = new GetCardListRequest(legalID).getListOfVisibleAccounts();

        utilSubSteps.stepSaveVariable(String.valueOf(visibleCards.size()),varName);

    }

    @StepExt("get card usage detail by card number \"{cardNo}\"")
    public void stepGetCardUsageDetail(String cardNo){
        Map<String,String> map = new CardUsageRequest(cardNo).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);

    }

    @StepExt("get cash withdrawal fee info by card number \"{cardNo}\"")
    public void stepGetCashWithdrawalFeeByCardNumber(String cardNo){
        Map<String,String> map = new CardCashWithdrawalFeeRequest(cardNo).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("get iCash info by CIF \"{cif}\" and amount \"{amount}\"")
    public void stepGetICashInfo(String cif,String amount){
        Map<String,String> map = new ICashInfoRequest(cif, amount).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("get reward points by CIF \"{cif}\"")
    public void stepRetrieveRewardPoint(String cif){
        Map<String,String> map = new RetrieveRewardPoint(cif).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("get customer information by CIF \"$cif\"")
    public void stepGetCustomerInfo(String cif){
        Map<String,String> map = new GetCustomerInfoRequest(cif).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("get Current Account Details Request \"{accountNumber}\"")
    public void stepGetCADetails(String accountNumber){
        Map<String, String> map = new GetCurrentAccountDetailRequest(accountNumber).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("get minimum and maximum amount by card number \"$cardNo\"")
    public void stepGetMinimumAndMaximumAmountByCardNumber(String cardNo){
        Map<String,String> map = new CardMinimumMaximumAmountRequest(cardNo).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);


    }

    @StepExt("get Evoucher Info with parameters")
    public void stepGetEvoucherInfo(ExamplesTable parameters){
        ReportUtils.attachExampletable(parameters);

        Map<String,String> params_map=convertExampleTableToMap(parameters,"name","value");
        String evoucherCode = params_map.get("evoucherCode");
        String transactionType = params_map.get("transactionType");
        String transactionDetail = params_map.get("transactionDetail");
        String amount = params_map.get("amount");
        String currency = params_map.get("currency");
        String customerCode = params_map.get("customerCode");
        String accNumber = params_map.get("accNumber");
        String category = params_map.get("category");
        String accType = params_map.get("accType");
        String transactionDate = params_map.get("transactionDate");

        Map<String,String> map = new GetEvoucherInfoRequest(evoucherCode, transactionType, transactionDetail, amount, currency, customerCode, accNumber, category, accType, transactionDate).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("get interest rate with parameters")
    public void stepGetInterestRateWithParameters(ExamplesTable parameters) {
        ReportUtils.attachExampletable(parameters);

        Map<String, String> params_map = convertExampleTableToMap(parameters, "name", "value");

        String productName = params_map.get("ProductName");
        String amount = params_map.get("Amount");
        String currency = params_map.get("Currency");
        String term = params_map.get("Term");
        String interestPeriod = params_map.get("InterestPeriod");
        String openDate = params_map.get("OpenDate");
        String customerCode = params_map.get("CustomerCode");
        String customerLogin = params_map.get("CustomerLogin");

        String productCategory = params_map.get("ProductCategory");
        String productCode = params_map.get("ProductCode");
        String channel = "OCB";

        if (StringUtils.isEmpty(customerCode))
            customerCode = SQLSubSteps.stepCIFisReceivedByParameterAndSavedToVariable(customerLogin, "CIF");

        if (StringUtils.isEmpty(productCategory)) {
            switch (productName) {
                case "Normal term deposit":
                case "Tiền gửi có kỳ hạn thường": {
                    productCategory = "6603";
                    switch (currency) {
                        case "VND":
                            productCode = "TD6001";
                            break;
                        case "USD":
                            productCode = "TD6002";
                            break;
                        case "EUR":
                            productCode = "TD6003";
                            break;
                    }
                }
                break;
                case "Floating rate term deposit":
                case "Tiền gửi có kỳ hạn Bảo toàn thịnh vượng":
                    productCategory = "6603";
                    productCode = "TD1079";
                    break;
                case "An Thinh Vuong term deposit":
                case "Tiền gửi có kỳ hạn An thịnh vượng":
                    productCategory = "6604";
                    switch (interestPeriod) {
                        case "Monthly":
                        case "Hàng tháng":
                            productCode = "TD6046";
                            break;
                        case "Quarterly":
                        case "Hàng quý":
                            productCode = "TD6047";
                            break;
                        case "Six-Month":
                        case "Hàng 6 tháng":
                            productCode = "TD6048";
                            break;
                        case "Yearly":
                        case "Hàng năm":
                            productCode = "TD6049";
                            break;
                    }
                    break;
                case "Interest paid periodically term deposit":
                case "Tiền gửi có kỳ hạn lĩnh lãi định kỳ":
                    productCategory = "6604";
                    switch (interestPeriod) {
                        case "Monthly":
                        case "Hàng tháng":
                            productCode = "TD6004";
                            break;
                        case "Quarterly":
                        case "Hàng quý":
                            productCode = "TD6005";
                            break;
                        case "Six-Month":
                        case "Hàng 6 tháng":
                            productCode = "TD6006";
                            break;
                        case "Yearly":
                        case "Hàng năm":
                            productCode = "TD6007";
                            break;
                    }
                    break;
                case "Easy Saving installment term deposit":
                case "Tiền gửi góp có kỳ hạn Easy Saving":
                    productCategory = "21017";
                    productCode = "21017Y";
                    break;
                case "Prepaid interest term deposit":
                case "Tiền gửi có kỳ hạn trả lãi trước":
                    productCategory = "21014";
                    productCode = "21014Y";
                    break;
                case "Phat Loc Thinh Vuong term deposit":
                case "Tiền gửi có kỳ hạn Phát Lộc Thịnh Vượng":
                    productCategory = "6603";
                    productCode = "TD6017";
                    break;
                case "Saving secured for Overdraft":
                    productCategory = "6603";
                    productCode = "TD6018";
                    break;
                case "Phat Loc Thinh Vuong":
                    channel = "BRANCH";
                    productCategory = "6601";
                    productCode = "TD1090";
                    break;
            }
        }

        if (StringUtils.isEmpty(productCategory) || StringUtils.isEmpty(productCode))
            throw new RuntimeException("Product name/Product category/Product code are not entered or invalid");

        String interestRate = new GetRealtimeInterestRateRequest(productCategory, productCode, amount, currency, term, openDate, customerCode, channel).responseAsMap().get("soap_interestRate");
        String baseRate = new GetRealtimeInterestRateRequest(productCategory, productCode, amount, currency, term, openDate, customerCode, channel).responseAsMap().get("soap_baseRate");

        utilSubSteps.stepSaveVariable(interestRate, "soap_interestRate");
        utilSubSteps.stepSaveVariable(baseRate,"soap_baseRate");

    }

    @StepExt("get W4 current date")
    public void stepGetW4CurrentDate(){

        Map<String,String> map = new GetW4CurrentDate().responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);
    }

    @StepExt("cancel application in LOS with parameters")
    public void stepCancelApplication(ExamplesTable parameters){
        ReportUtils.attachExampletable(parameters);

        Map<String,String> params_map=convertExampleTableToMap(parameters,"name","value");
        String refNo = params_map.get("refNo");
        String applicationID = params_map.get("applicationID");
        String applicationNo = params_map.get("applicationNo");
        Map<String,String> map = new GetJarvisAppCancel(refNo, applicationID, applicationNo).responseAsMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            utilSubSteps.stepSaveVariable(entry.getValue(),entry.getKey());
        }
        ReportUtils.attachVariables("Service variables", map);
    }
}
