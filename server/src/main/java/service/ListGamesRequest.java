package service;

public class ListGamesRequest {
    private String authorization;
}


private String authorization;
private String gameName;

public CreateGameRequest(String authorization){
    this.authorization = authorization;
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
