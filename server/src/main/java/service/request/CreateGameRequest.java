package service.request;

public class CreateGameRequest {

    private String authorization;
    private String gameName;

    public CreateGameRequest(String authorization, String gameName){
        this.authorization = authorization;
        this.gameName = gameName;
    }

    public String getAuthorization(){
        return this.authorization;
    }

    public String getGameName(){
        return this.gameName;
    }

    public void setAuthorization(String authToken){
        this.authorization = authToken;
    }
}
