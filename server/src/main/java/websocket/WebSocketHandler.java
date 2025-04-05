package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import model.AuthData;

import model.GameData;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import server.service.exception.ResponseException;
import service.GameService;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                this.makeMove(session, command);
            }
            case LEAVE -> {
                LeaveCommand command = new Gson().fromJson(message, LeaveCommand.class);
                this.leave(session, command);
            }
            case RESIGN -> {
                ResignCommand command = new Gson().fromJson(message, ResignCommand.class);
                this.resign(session, command);
            }
        }
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error){
        try {
            session.getRemote().sendString(new Gson().toJson(
                    new ErrorMessage(error.getMessage())
            ));
        } catch (IOException exception){
            throw new ResponseException(0, exception.getMessage());
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

            if(command.isObserving()){
                this.gameService.gameDAO.addObserverToGame(command.getUsername(), command.getGameID());
            }
            // Add the connection
            this.connections.addConnection(new Connection(username, session));

            // Get the GameData
            GameData gameData = this.gameService.gameDAO.getGame(command.getGameID());
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData, command.isObserving());

            this.connections.notify(username, loadGameMessage);

            // Notify that someone joined
            // TODO: Right now this only works for those who joined, not including the people who are observing
            String opponentUsername = null;
            String teamColor = command.getTeamColor();
            String message = "";

            if (teamColor != null) {
                message = username + " joined the game as " + teamColor + "!";
                if (teamColor.equals("WHITE")) {
                    opponentUsername = gameData.blackUsername();
                } else {
                    opponentUsername = gameData.whiteUsername();
                }
                if (opponentUsername != null) {
                    this.connections.notify(opponentUsername, new NotificationMessage(message));
                    String[] observerList = this.gameService.gameDAO.getObserverList(command.getGameID());
                    this.connections.notify(observerList, new NotificationMessage(message));
                }
            } else if (command.isObserving()){
                // The person who joined is observing
                message = username + " joined the game as an observer!";
                ArrayList<String> notifyList = new ArrayList<>();
                notifyList.add(gameData.whiteUsername());
                notifyList.add(gameData.blackUsername());
                String[] observerList = this.gameService.gameDAO.getObserverList(command.getGameID());
                notifyList.addAll(List.of(observerList));

                this.connections.notify(notifyList.toArray(new String[0]), new NotificationMessage(message));
            }

        } catch (ResponseException exception) {
            this.sendError("Invalid credentials.", username);

        } catch (IOException exception){
            this.sendError("Sorry could not connect to game.", username);
        }
    }

    public void makeMove(Session session, MakeMoveCommand command){
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

            // Get the GameData
            GameData gameData = this.gameService.gameDAO.getGame(command.getGameID());
            try {

                // Make the move and set the new team turn
                gameData.game().makeMove(command.getMove());
                if (command.getTeamColor().equals("WHITE")){
                    gameData.game().setTeamTurn(ChessGame.TeamColor.BLACK);
                } else {
                    gameData.game().setTeamTurn(ChessGame.TeamColor.WHITE);
                }

            } catch (InvalidMoveException exception){
                this.sendError("Invalid move.", username);
            }
            this.gameService.gameDAO.updateGame(gameData);

            LoadGameMessage loadGameMessagePlayers = new LoadGameMessage(gameData, false);

            // TODO: Load game from observers
            LoadGameMessage loadGameMessageObservers = new LoadGameMessage(gameData, true);

            this.connections.notify(username, loadGameMessagePlayers);

            // Notify that someone joined
            // TODO: Right now this only works for those who joined, not including the people who are observing
            String opponentUsername = null;
            String teamColor = command.getTeamColor();
            String message = "";

            ChessMove move = command.getMove();
            String start = PositionConverter.positionToLocation(move.getStartPosition());
            String end = PositionConverter.positionToLocation(move.getEndPosition());

            message = username + " made a move from " + start + " to " + end + ".";
            if (teamColor.equals("WHITE")) {
                opponentUsername = gameData.blackUsername();
            } else {
                opponentUsername = gameData.whiteUsername();
            }
            if (opponentUsername != null) {
                this.connections.notify(opponentUsername, loadGameMessagePlayers);
                this.connections.notify(opponentUsername, new NotificationMessage(message));
            }
            // Notify observers
            String[] observerList = this.gameService.gameDAO.getObserverList(command.getGameID());
            this.connections.notify(observerList, new NotificationMessage(message));

        } catch (ResponseException exception) {
            this.sendError("Invalid credentials.", username);

        } catch (IOException exception){
            this.sendError("Sorry could not connect to game.", username);
        }
    }

    public void leave(Session session, LeaveCommand command){
        throw new RuntimeException("Not implemented.");
    }

    public void resign(Session session, ResignCommand command){
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
