package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    public String username;
    public String teamColor;
    public ChessMove move;

    public MakeMoveCommand(String username, String teamColor, ChessMove move, String authToken, Integer gameID) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.teamColor = teamColor;
        this.move = move;
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public String getTeamColor(){
        return this.teamColor;
    }

    public ChessMove getMove(){
        return this.move;
    }

}
