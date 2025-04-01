import chess.*;
import client.ChessClient;
import exceptions.WebSocketFacadeException;
import ui.ChessUI;
import ui.NotificationHandler;
import ui.ServerFacade;
import ui.WebSocketFacade;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
        WebSocketFacade webSocketFacade = null;
        try {
            webSocketFacade = new WebSocketFacade("http://localhost:8080", new NotificationHandler());
        } catch (WebSocketFacadeException exception) {
            throw new RuntimeException(exception.toString());
        }
        ChessClient client = new ChessClient(serverFacade, webSocketFacade);
        ChessUI chessUI = new ChessUI(serverFacade, client);
        chessUI.run();
    }
}