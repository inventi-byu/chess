package service;

public class JoinGameRequest {

    private String authorization;
    private String playerColor;
    private int gameID;

    public JoinGameRequest(String authorization, String playerColor, int gameID){
        this.authorization = authorization;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public String getAuthorization(){
        return this.authorization;
    }

    public void setAuthorization(String authToken){
        this.authorization = authToken;
    }

    public String getPlayerColor(){
        return this.playerColor;
    }

    public int getGameID(){
        return this.gameID;
    }

}
