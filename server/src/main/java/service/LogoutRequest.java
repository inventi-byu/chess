package service;

public class LogoutRequest {
    private String authorization;

    public LogoutRequest(String authorization){
        this.authorization = authorization;
    }

    public String getAuthToken(){
        return this.authorization;
    }
}
