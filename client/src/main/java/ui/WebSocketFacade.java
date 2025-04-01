package ui;

import chess.ChessGame;
import chess.ChessPosition;
import exceptions.ServerFacadeException;
import model.GameData;

import javax.websocket.Session;

public class WebSocketFacade {

    Session session;
    NotificationHandler notificationHandler;
    private String webSocketURL;

    public WebSocketFacade(String webSocketURL, NotificationHandler notificationHandler){
        this.webSocketURL = webSocketURL;
        this.notificationHandler = notificationHandler;
    }

    public GameData joinGame(String teamColor, Integer gameID, String authToken){
        throw new RuntimeException("Not implemented.");
    }

    public void observeGame(String gameID, String authToken) throws ServerFacadeException {
        return;
        // Not implemented until phase 6
    }

    public GameData makeMove(ChessGame.TeamColor teamColor, ChessPosition start, ChessPosition end) throws ServerFacadeException {
        throw new ServerFacadeException(0, "Not implemented");
    }

    public void leaveGame(String authToken, String username) throws ServerFacadeException {
        throw new ServerFacadeException(0, "Not implemented.");
    }

    public void resignGame(String authToken, String username) throws ServerFacadeException {
        throw new ServerFacadeException(0, "Not implemented.");
    }

    private void connect(String authoken, Integer gameID) {
        throw new RuntimeException("Not implemented.");
    }

    private void disconnect(String authToken, Integer gameID){
        throw new RuntimeException("Not implemented.");
    }

}
