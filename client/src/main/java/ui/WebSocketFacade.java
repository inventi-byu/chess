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
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;

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

    public void observeGame(String username, String gameID, String authToken) throws WebSocketFacadeException {
        try {
            ConnectCommand command = new ConnectCommand(username, authToken, Integer.valueOf(gameID), "WHITE", true);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException exception){
            throw new WebSocketFacadeException(500, exception.getMessage());
        }
}

    public void makeMove(AuthData authData, Integer gameID, ChessGame.TeamColor teamColor, ChessPosition start, ChessPosition end, ChessPiece.PieceType promotionPiece) throws WebSocketFacadeException {
        try {
            String teamColorString = "";
            if(teamColor == ChessGame.TeamColor.WHITE){
                teamColorString = "WHITE";
            } else {
                teamColorString = "BLACK";
            }
            MakeMoveCommand command = new MakeMoveCommand(authData.username(), teamColorString, new ChessMove(start, end, promotionPiece),authData.authToken(), gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException exception){
            throw new WebSocketFacadeException(500, exception.getMessage());
        }
    }

    public void leaveGame(String username, String teamColor, String authToken, int gameID, boolean observing) throws WebSocketFacadeException {
        try {
            LeaveCommand command = new LeaveCommand(username, teamColor, authToken, gameID, observing);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException exception){
            throw new WebSocketFacadeException(500, exception.getMessage());
        }
    }

    public void resignGame(String authToken, int gameID) throws WebSocketFacadeException {
        try {
            ResignCommand command = new ResignCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException exception){
            throw new WebSocketFacadeException(500, exception.getMessage());
        }
    }

    private void connect(String authoken, Integer gameID) {
        throw new RuntimeException("Not implemented.");
    }

    private void disconnect(String authToken, Integer gameID){
        throw new RuntimeException("Not implemented.");
    }

}
