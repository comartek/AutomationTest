package core.services.soap.requests.newESB.account;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.services.soap.requests.newESB.ESBRequest;

@RequestTemplate("getProductList")
public class getProductListRequest extends ESBRequest {

    @RequestField
    public String CIF;

    @RequestField
    public String legalID;

    @RequestField
    public String productType;

    @RequestField
    public String includedAccount;

    public getProductListRequest(String CIF, String legalID, String productType, String includedAccount) {
        this.CIF = CIF;
        this.legalID = legalID;
        this.productType = productType;
        this.includedAccount = includedAccount;
    }

    @ResponseField("Envelope.Body.getProductListRs.Products.savingAccountList.account.accountNumber")
    public String accountNuumber;

    @ResponseField("Envelope.Body.getProductListRs.Products.savingAccountList.account.availableBalance")
    public String availableBalance;

    @ResponseField("Envelope.Body.getProductListRs.Products.savingAccountList.account.interestRate")
    public String interestRate;

    @ResponseField("Envelope.Body.getProductListRs.Products.savingAccountList.account.productDesc")
    public String ProductType;

}
