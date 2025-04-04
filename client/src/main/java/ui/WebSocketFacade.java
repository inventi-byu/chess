package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import exceptions.ServerFacadeException;
import exceptions.WebSocketFacadeException;
import model.AuthData;
import model.GameData;
import websocket.commands.ConnectCommand;
import websocket.commands.MakeMoveCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WebSocketFacade extends Endpoint {

    private Session session;
    private NotificationHandler notificationHandler;
    private String webSocketURL;


    public WebSocketFacade(String webSocketURL, NotificationHandler notificationHandler) throws WebSocketFacadeException {
        this.webSocketURL = webSocketURL;
        this.notificationHandler = notificationHandler;

        try {
            webSocketURL = webSocketURL.replace("http", "ws");
            URI socketURI = new URI(webSocketURL + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message){
                    notificationHandler.handle(message);
                }

            });

        } catch (Exception exception){
            throw new WebSocketFacadeException(500, exception.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){}

    public void joinGame(String username, String teamColor, Integer gameID, String authToken) throws WebSocketFacadeException {
        try {
            ConnectCommand command = new ConnectCommand(username, authToken, gameID, teamColor, false);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException exception){
            throw new WebSocketFacadeException(500, exception.getMessage());
        }
    }

    public void observeGame(String gameID, String authToken) throws WebSocketFacadeException {
        return;
        /*
        try {
            ConnectCommand command = new ConnectCommand(authToken, Integer.parseInt(gameID), null, false);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException exception){
            throw new WebSocketFacadeException(500, exception.getMessage());
        }
        return;
        // Not implemented until phase 6
        */
}

    public void makeMove(AuthData authData, Integer gameID, ChessGame.TeamColor teamColor, ChessPosition start, ChessPosition end, ChessPiece.PieceType promotionPiece) throws WebSocketFacadeException {
        try {
            MakeMoveCommand command = new MakeMoveCommand(authData.username(), teamColor, new ChessMove(start, end, promotionPiece),authData.authToken(), gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException exception){
            throw new WebSocketFacadeException(500, exception.getMessage());
        }
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
