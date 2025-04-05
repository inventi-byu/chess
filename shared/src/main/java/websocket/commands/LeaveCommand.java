package websocket.commands;

public class LeaveCommand extends UserGameCommand {
    public String username;
    boolean observing;
    String teamColor;

    public LeaveCommand(String username, String teamColor, String authToken, Integer gameID, boolean observing) {
        super(CommandType.LEAVE, authToken, gameID);
        this.username = username;
        this.observing = observing;
        this.teamColor = teamColor;
    }

    public String getUsername(){
        return this.username;
    }

    public boolean isObserving(){
        return this.observing;
    }

    public String getTeamColor(){
        return this.teamColor;
    }
}

