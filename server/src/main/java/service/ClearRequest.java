package service;

public class ClearRequest {

    private String requestMethod;

    public ClearRequest(String requestMethod){
        this.requestMethod = requestMethod;
    }

    public String getRequestMethod(){
        return this.requestMethod;
    }

}
