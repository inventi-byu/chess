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
        ChessClient client = new ChessClient(serverFacade, webSocketFacade);
        ChessUI chessUI = new ChessUI(client);
        NotificationHandler notificationHandler = new NotificationHandler(chessUI);

        try {
            webSocketFacade = new WebSocketFacade("http://localhost:8080", notificationHandler);
        } catch (WebSocketFacadeException exception) {
            throw new RuntimeException(exception.toString());
        }
        client.updateWebSocketFacade(webSocketFacade);
        chessUI.run();
    }
}