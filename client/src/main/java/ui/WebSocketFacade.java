package ui;

import chess.ChessGame;
import chess.ChessPosition;
import exceptions.ServerFacadeException;
import exceptions.WebSocketFacadeException;
import model.GameData;

import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    private String webSocketURL;

    public WebSocketFacade(String webSocketURL, NotificationHandler notificationHandler) throws WebSocketFacadeException {
        this.webSocketURL = webSocketURL;
        this.notificationHandler = notificationHandler;

        try {
            webSocketURL = webSocketURL.replace("http", "ws");
            URI socketURI = new URI(webSocketURL + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

        } catch (Exception exception){
            throw new WebSocketFacadeException(500, "Unknown error occured.");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){}

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
