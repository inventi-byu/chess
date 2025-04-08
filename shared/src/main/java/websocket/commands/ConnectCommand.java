package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    public ConnectCommand(/*String username,*/String authToken, Integer gameID/*String teamColor, boolean observing*/) {
        super(CommandType.CONNECT, authToken, gameID);
    }
}
