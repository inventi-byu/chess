package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    private String teamColor;
    private boolean observing;
    public ConnectCommand(String authToken, Integer gameID, String teamColor, boolean observing) {
        super(CommandType.CONNECT, authToken, gameID);
        this.teamColor = teamColor;
        this.observing = observing;
    }
    public ConnectCommand(){
        super(CommandType.CONNECT);
    }

    public String getTeamColor(){
        return this.teamColor;
    }

    public boolean isObserving(){
        return this.observing;
    }
}
