package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    private String teamColor;
    private boolean observing;
    private String username;
    public ConnectCommand(String username, String authToken, Integer gameID, String teamColor, boolean observing) {
        super(CommandType.CONNECT, authToken, gameID);
        this.teamColor = teamColor;
        this.observing = observing;
        this.username = username;
    }

    public String getTeamColor(){
        return this.teamColor;
    }

    public boolean isObserving(){
        return this.observing;
    }

    public String getUsername(){
        return this.username;
    }
}
