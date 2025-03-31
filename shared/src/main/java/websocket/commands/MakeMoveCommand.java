package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    ChessMove move;
    public MakeMoveCommand(ChessMove move){
        super(CommandType.MAKE_MOVE);
        this.move = move;
    }
}
