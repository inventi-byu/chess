package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import service.UserService;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

public class WebSocketHandler {
    UserService userService;

    private final ConnectionsManager connections = new ConnectionsManager();

    public WebSocketHandler(UserService userService){
        this.userService = userService;
    }

    @OnWebSocketMessage
    public void OnMessage(Session session, String message) throws IOException {
        UserGameCommand userCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userCommand.getCommandType()){
            case CONNECT -> {
                ConnectCommand command = new Gson().fromJson(message, ConnectCommand.class);
                this.connectUser(command);
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

    public void connectUser(ConnectCommand command){
        if(command.getGameID() == null || command.getAuthToken() == null){
            this.sendError("Invalid gameID.", command.getUsername());
        }
        throw new RuntimeException("Not implemented.");
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
