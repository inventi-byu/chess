package service;

public class ListGamesRequest {
    private String authorization;

    public ListGamesRequest(String authorization){
        this.authorization = authorization;
    }

    public String getAuthorization(){
        return this.authorization;
    }
    
}
