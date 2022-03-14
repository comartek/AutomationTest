package core.services.soap.requests.newESB.account;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.requests.newESB.ESBRequest;

/**
 * Created by Denis on 21.04.2018.
 */
@RequestTemplate("getCardDetail")
public class GetCardDetailRequest extends ESBRequest {

    @RequestField
    String cardNo;

    public GetCardDetailRequest(String cardNo){
        this.cardNo = cardNo;
    }

    @ResponseField("Envelope.Body.getCardDetailRs.cardInfo.contractNumber")
    public String ContractNumber;

    @ResponseField("Envelope.Body.getCardDetailRs.productInfo.name")
    public String ProductName;

    @ResponseField("Envelope.Body.getCardDetailRs.cardInfo.shortName")
    public String ShortName;

    @ResponseField("Envelope.Body.getCardDetailRs.cardInfo.holderName")
    public String HolderName;

    @ResponseField("Envelope.Body.getCardDetailRs.cardInfo.status")
    public String CardStatus;

    @ResponseField("Envelope.Body.getCardDetailRs.cardInfo.plasticStatus")
    public String PlasticStatus;

    @ResponseField("Envelope.Body.getCardDetailRs.cardInfo.id")
    public String CardId;

    @ResponseField("Envelope.Body.getCardDetailRs.balanceInfo.creditLimit")
    public String CreditLimit_Amount;

    @ResponseField("Envelope.Body.getCardDetailRs.balanceInfo.availableBalance")
    public String Available_Amount;

    @ResponseField("Envelope.Body.getCardDetailRs.balanceInfo.cardBalance")
    public String OutstandingBalance;

    @ResponseField("Envelope.Body.getCardDetailRs.balanceInfo.minimumPayDue")
    public String minimumPayDue;

//    @ResponseField("Envelope.Body.getCardDetailRs.balanceInfo.currency")
//    public String CardCurrency;

    @ResponseField("Envelope.Body.getCardDetailRs.cardInfo.currency")
    public String CardCurrency;

    @ResponseField("Envelope.Body.getCardDetailRs.balanceInfo.blockAmount")
    public String BlockedAmount;

    @ResponseField("Envelope.Body.getCardDetailRs.balanceInfo.outstandingAmount")
    public String StatementBalance;

    @ResponseField("Envelope.Body.getCardDetailRs.cardInfo.branchNo")
    public String BranchNo;

    @ResponseField("Envelope.Body.getCardDetailRs.cardInfo.interestRate")
    public String InterestRate;

    @ResponseField("Envelope.Body.getCardDetailRs.cardInfo.EComStatus")
    public String EcomStatus;

    
}
