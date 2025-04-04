package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    String username;
    ChessGame.TeamColor teamColor;
    ChessMove move;

    public MakeMoveCommand(String username, ChessGame.TeamColor teamColor, ChessMove move, String authToken, Integer gameID) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.teamColor = teamColor;
        this.move = move;
        this.username = username;
    }

}
