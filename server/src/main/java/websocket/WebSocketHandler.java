package websocket;

import com.google.gson.Gson;
import model.AuthData;

import model.GameData;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import server.service.exception.ResponseException;
import service.GameService;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    GameService gameService;

    private final ConnectionsManager connections = new ConnectionsManager();

    public WebSocketHandler(GameService gameService){
        this.gameService = gameService;
    }

    @OnWebSocketMessage
    public void OnMessage(Session session, String message) throws IOException {
        UserGameCommand userCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userCommand.getCommandType()){
            case CONNECT -> {
                ConnectCommand command = new Gson().fromJson(message, ConnectCommand.class);
                this.connectUser(session, command);
            }
            case MAKE_MOVE -> {
                MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
                this.makeMove(command);
            }
            case LEAVE -> {
                LeaveCommand command = new Gson().fromJson(message, LeaveCommand.class);
                this.leave(command);
            }
            case RESIGN -> {
                ResignCommand command = new Gson().fromJson(message, ResignCommand.class);
                this.resign(command);
            }
        }
    }

    public void connectUser(Session session, ConnectCommand command){
        if (command.getUsername() == null){
            try {
                session.getRemote().sendString(new Gson().toJson(
                        new ErrorMessage("You don't have a username, are you logged in?"))
                );
            } catch (IOException exception){
                throw new ResponseException(0, exception.getMessage());
            }
        }
        String username = command.getUsername();
        if(command.getGameID() == null){
            this.sendError("Invalid gameID.", username);
        }
        if(command.getAuthToken() == null) {
            this.sendError("Invalid credentials.", username);
        }
        // Authenticate
        AuthData authData = null;
        try{
            authData = this.gameService.authenticateWithToken(command.getAuthToken());

            // Add the connection
            this.connections.addConnection(new Connection(username, session));

            // Get the GameData
            GameData gameData = this.gameService.gameDAO.getGame(command.getGameID());
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData, command.isObserving());

            this.connections.notify(username, loadGameMessage);

        } catch (ResponseException exception) {
            this.sendError("Invalid credentials.", username);

        } catch (IOException exception){
            this.sendError("Sorry could not connect to game.", username);
        }
    }

    public void makeMove(MakeMoveCommand command){
        throw new RuntimeException("Not implemented.");
    }

    public void leave(LeaveCommand command){
        throw new RuntimeException("Not implemented.");
    }

    public void resign(ResignCommand command){
        throw new RuntimeException("Not implemented.");
    }

    private void sendError(String message, String username){
        try {
            connections.notify(username, new ErrorMessage("Invalid gameID."));
        } catch (IOException exception){
            throw new WebSocketException(exception);
        }
    }

}
