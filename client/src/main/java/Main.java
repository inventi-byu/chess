import chess.*;
import client.ChessClient;
import ui.ChessUI;
import ui.ServerFacade;
import ui.WebSocketFacade;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
        WebSocketFacade webSocketFacade = new WebSocketFacade("http://localhost:8080");
        ChessClient client = new ChessClient(serverFacade, webSocketFacade);
        ChessUI chessUI = new ChessUI(serverFacade, client);
        chessUI.run();
    }
}