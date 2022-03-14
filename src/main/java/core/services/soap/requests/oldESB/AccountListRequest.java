package core.services.soap.requests.oldESB;

import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.services.soap.AbstractRequest;
import core.utils.evaluationManager.ScriptUtils;
import groovy.lang.GroovyShell;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.element.Node;
import io.restassured.path.xml.element.NodeChildren;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Denis on 21.04.2018.
 */
@RequestEndpoint("http://10.37.16.126:7800/service/vn/ba34/bd85/customeragreement/1")
public abstract class AccountListRequest extends AbstractRequest {

    @RequestField
    String ServiceVersion="1";
    @RequestField
    String MessageId=java.util.UUID.randomUUID().toString();
    @RequestField
    String TransactionId=java.util.UUID.randomUUID().toString();
    @RequestField
    String MessageTimestamp=ScriptUtils.now("yyyy-MM-dd'T'hh:mm:ss");
    @RequestField
    String SourceAppID="VSII";
    @RequestField
    String TargetAppID="ESB";
    @RequestField
    String UserId="VSII";
    @RequestField
    String UserPassword="FY17fVvlKXseKr9O/RNolQ==";

    @RequestField
    protected String CIFNo;

    protected abstract String getAccountXmlPath();
    protected abstract String getAccountNumberXmlPath();
    protected abstract boolean isVisibleAccount(Node account);
    protected abstract Map<String,String> parseAccountInfo(Node account);

    protected String excludedAccounts;
    protected String includedAccounts;

    protected String additionalConditions;

    protected List<Map<String, String>> getListOfAccounts(boolean checkVisibility) {
        List<Map<String, String>> accounts = new ArrayList<>();
        XmlPath path = XmlPath.from(getResponse());
        NodeChildren accs = path.getNodeChildren(getAccountXmlPath());

        for (int i = 0; i < accs.size(); i++) {
            String accountNo = accs.get(i).getPath(getAccountNumberXmlPath()).toString();
            if (excludedAccounts != null && excludedAccounts.contains(accountNo))
                continue;
            if (includedAccounts != null && !includedAccounts.contains(accountNo))
                continue;

            if (!checkVisibility || isVisibleAccount(accs.get(i)))
            {
                Map<String,String> map = parseAccountInfo(accs.get(i));
                if (checkAdditionalConditions(map))
                    accounts.add(map);
            }

        }
        return accounts;
    }

    protected static final GroovyShell groovyShell=new GroovyShell();

    protected boolean checkAdditionalConditions(Map<String,String> map){
        if (StringUtils.isNotEmpty(additionalConditions)) {
            groovyShell.setVariable("response", map);
            return (Boolean) (groovyShell.evaluate(
                    String.format("response.with{%s}",additionalConditions)
            ));
        }
        return true;

    }

    public List<Map<String, String>> getListOfAccounts(){
        return getListOfAccounts(false);
    }
    public List<Map<String, String>> getListOfVisibleAccounts(){
        return getListOfAccounts(true);
    }
}
