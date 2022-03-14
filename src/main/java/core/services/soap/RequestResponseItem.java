package core.services.soap;

/**
 * Created by Akarpenko on 19.03.2018.
 */
public class RequestResponseItem {

    private String name;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    private String request;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response){
        this.response = response;
    }

    public RequestResponseItem(String name, String request, String response){
        setName(name);
        setRequest(request);
        setResponse(response);
    }

    public RequestResponseItem(){

    }

}
