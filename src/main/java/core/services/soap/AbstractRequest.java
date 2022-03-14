package core.services.soap;

import core.annotations.RequestEndpoint;
import core.annotations.RequestField;
import core.annotations.RequestTemplate;
import core.annotations.ResponseField;
import core.sql.SQLManager;
import core.utils.report.ReportUtils;
import io.restassured.path.xml.XmlPath;
import org.apache.log4j.Logger;
import org.reficio.ws.client.core.SoapClient;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractRequest {

    protected String getTemplate()
    {
        RequestTemplate annotation = this.getClass().getAnnotation(RequestTemplate.class);
        if (annotation!=null)
        {
            String templateName= annotation.value();
            String template= SQLManager.getInstance().get().LocalDB.executeQueryWithResults(
                    String.format("select template_text from soap_templates where template_name='%s'",templateName)).get(0).get("template_text");
            return template;
        }
        else
            throw new RuntimeException("RequestTemplate annotation should be defined");
    }

    protected String fillTemplate(String template){

        for (Field field : ReflectionUtils.getAllFields(getClass())) {
            field.setAccessible(true);
            RequestField annotation = field.getAnnotation(RequestField.class);
            if (annotation != null) {
                try {
                    String fieldName = annotation.value().isEmpty() ? field.getName() : annotation.value();
                    String fieldValue = Optional.ofNullable(field.get(this)).orElse("").toString();
                    template = template.replace(String.format("{%s}", fieldName), fieldValue);
                } catch (IllegalAccessException e) {
                }
            }
        }
        return template;
    }

    protected String request;
    public String getRequest() {
        if (request ==null)
            build();
        return request;
    }

    protected String response;
    public String getResponse() {
        if (response == null)
            execute();
        return response;
    }

    public String build() {
        String template = getTemplate();
        request = fillTemplate(template);
        return request;
    }


    public String getEndpoint() {
        RequestEndpoint annotation = this.getClass().getAnnotation(RequestEndpoint.class);
        if (annotation!=null){
            String endpoint= annotation.value();
            return endpoint;
        }
        else
            throw new RuntimeException("Endpoint should be defined");
    }

    public <T extends AbstractRequest> T execute(){
        SoapClient client = SoapClient.builder()
                .endpointUri(getEndpoint())
                .build();
        Logger.getRootLogger().info(getRequest());
        ReportUtils.attachXml(this.getClass().getSimpleName() + " - Request", getRequest());
        response = client.post(getRequest());
        Logger.getRootLogger().info(getResponse());
        ReportUtils.attachXml(this.getClass().getSimpleName() + " - Response", getResponse());
        parseResponse();
        return (T)this;
    }

    protected void parseResponse() {
        XmlPath path = XmlPath.from(getResponse());
        for (Field field : ReflectionUtils.getAllFields(getClass())) {
            field.setAccessible(true);
            ResponseField annotation = field.getAnnotation(ResponseField.class);
            if (annotation != null) {
                try {
                    String fieldPath = annotation.value();
                    String fieldValue = path.getString(fieldPath);
                    field.set(this,fieldValue);
                } catch (IllegalAccessException e) {
                }
            }
        }
    }

    public Map<String,String> responseAsMap(String prefix) {
        getResponse();
        if (prefix == null)
            prefix = "";
        Map<String, String> map = new HashMap<String, String>();
        for (Field field : ReflectionUtils.getAllFields(getClass())) {
            field.setAccessible(true);
            ResponseField annotation = field.getAnnotation(ResponseField.class);
            if (annotation != null) {
                try {
                    String fieldName = field.getName();
                    String fieldValue = field.get(this).toString();
                    map.put(prefix + fieldName, fieldValue);
                } catch (IllegalAccessException e) {
                }
            }
        }
        return map;
    }

    public Map<String,String> responseAsMap() {
        return responseAsMap("soap_");
    }

    public String responseAsString() {
        return getResponse();
    }


}
