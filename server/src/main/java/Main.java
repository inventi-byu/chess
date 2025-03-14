import chess.*;
import dataaccess.*;
import server.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        //String implementationControl = "memory";
        //String implementationControl = args[1];

        Server server = new Server();
        server.run(8080);
    }
}