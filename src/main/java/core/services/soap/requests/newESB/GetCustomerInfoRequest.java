package core.services.soap.requests.newESB;

import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;

@RequestTemplate("getCustomerInfo")
public class GetCustomerInfoRequest extends ESBRequest {

    @RequestField
    String cif;

    public GetCustomerInfoRequest (String cif) {
        this.cif = cif;
    }

    @ResponseField("Envelope.Body.getCustomerInfoRs.customerName")
    public String customerName;

    @ResponseField("Envelope.Body.getCustomerInfoRs.currentAddress")
    public String currentAddress;

    @ResponseField("Envelope.Body.getCustomerInfoRs.documentList.document.type")
    public String documentType;

    @ResponseField("Envelope.Body.getCustomerInfoRs.documentList.document.issuePlace")
    public String documentIssuePlace;

    @ResponseField("Envelope.Body.getCustomerInfoRs.documentList.document.number")
    public String documentNumber;

    @ResponseField("Envelope.Body.getCustomerInfoRs.documentList.document.issueDate")
    public String documentIssueDate;

    @ResponseField("Envelope.Body.getCustomerInfoRs.contactList.contact.contactInfo")
    public String contactInfo;

    @ResponseField("Envelope.Body.getCustomerInfoRs.dateOfBirth")
    public String dateOfBirth;

    @ResponseField("Envelope.Body.getCustomerInfoRs.permanentAddress")
    public String permanentAddress;

    @ResponseField("Envelope.Body.getCustomerInfoRs.gender")
    public String gender;

    @ResponseField("Envelope.Body.getCustomerInfoRs.residence")
    public String residence;

}
