package service;

public class ClearRequest {

    private RequestMethod method;
    private String errorMessage;

    public ClearRequest(RequestMethod method){
        this.method = method;
    }

    public RequestMethod getMethod(){
        return this.method;
    }

}
