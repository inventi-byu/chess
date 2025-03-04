package service;

public class JoinGameRequest {

    private String authorization;
    private String playerColor;
    private int gameID;

    public JoinGameRequest(String playerColor, int gameID, String authorization){
        this.playerColor = playerColor;
        this.gameID = gameID;
    }
}
