package core.services.soap.requests.newESB;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;

@RequestTemplate("getCustomerOverdueList")
public class GetCustomerOverdueListRequest extends ESBRequest {
    @RequestField
    String cif;


    public GetCustomerOverdueListRequest(String cif) {
        this.cif = cif;
    }

    @ResponseField("Envelope.Body.getCustomerOverdueListRs.overdueList.overdueInfo.account")
    public String Account;

    @ResponseField("Envelope.Body.getCustomerOverdueListRs.overdueList.overdueInfo.overduePrinAmount")
    public String overduePrinAmount;

    @ResponseField("Envelope.Body.getCustomerOverdueListRs.overdueList.overdueInfo.overdueInterestAmount")
    public String overdueInterestAmount;

    @ResponseField("Envelope.Body.getCustomerOverdueListRs.overdueList.overdueInfo.penaltyPrinAmount")
    public String penaltyPrinAmount;

    @ResponseField("Envelope.Body.getCustomerOverdueListRs.overdueList.overdueInfo.penaltyInterestAmount")
    public String penaltyInterestAmount;

    @ResponseField("Envelope.Body.getCustomerOverdueListRs.overdueList.overdueInfo.totalOverdueAmount")
    public String totalOverdueAmount;

    @ResponseField("Envelope.Body.getCustomerOverdueListRs.overdueList.overdueInfo.overdueDays")
    public String overdueDays;

    @ResponseField("Envelope.Body.getCustomerOverdueListRs.overdueList.overdueInfo.interestRate")
    public String interestRate;

}
