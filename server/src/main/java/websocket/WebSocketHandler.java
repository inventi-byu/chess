package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.commands.*;

import java.io.IOException;

public class WebSocketHandler {
    private final ConnectionsManager connections = new ConnectionsManager();

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
}
