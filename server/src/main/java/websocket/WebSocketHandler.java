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
import java.util.Arrays;
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

    /*
    @OnWebSocketError
    public void onError(Session session, Throwable error){
        try {
            String[] stackTrace = (String[])Arrays.stream(error.getStackTrace()).toArray();
            StringBuilder sb = new StringBuilder();
            for (String line : stackTrace){
                sb.append(line);
                sb.append("\n");
            }
            session.getRemote().sendString(new Gson().toJson(
                    new ErrorMessage(error.toString() +
                            "\n" +
                            sb.toString())
            ));
        } catch (IOException exception){
            String[]  stackTrace = (String[])Arrays.stream(exception.getStackTrace()).toArray();
            StringBuilder sb = new StringBuilder();
            for (String line : stackTrace){
                sb.append(line);
                sb.append("\n");
            }
            throw new ResponseException(0, exception.toString() + "" +
                    "\n" +
                    sb.toString());
        }
    }
     */

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
            this.sendError(session, "Invalid gameID.");
            return;
        }
        if(command.getAuthToken() == null) {
            this.sendError(session, "Invalid credentials.");
            return;
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
            String opponentUsername = null;
            String teamColor = command.getTeamColor();
            String message = "";

            String[] observerList = this.gameService.gameDAO.getObserverList(command.getGameID());

            if (!command.isObserving()) {
                message = username + " joined the game as " + teamColor + "!";
                if (teamColor.equals("WHITE")) {
                    opponentUsername = gameData.blackUsername();
                } else {
                    opponentUsername = gameData.whiteUsername();
                }
                if (opponentUsername != null) {
                    this.connections.notify(opponentUsername, new NotificationMessage(message));
                }
                if(observerList != null) {
                    this.connections.notify(observerList, new NotificationMessage(message));
                }
            } else {
                // The person who joined is observing
                message = username + " joined the game as an observer!";
                ArrayList<String> notifyList = new ArrayList<>();
                String whiteUsername = gameData.whiteUsername();
                String blackUsername = gameData.blackUsername();
                if (whiteUsername != null){
                    notifyList.add(gameData.whiteUsername());
                }
                if (blackUsername != null){
                    notifyList.add(gameData.blackUsername());
                }
                if(observerList != null){
                    notifyList.addAll(List.of(observerList));
                }
                this.connections.notifyExcept(notifyList.toArray(new String[0]), username, new NotificationMessage(message));
            }

        } catch (ResponseException exception) {
            this.sendError(session, "Invalid credentials.");

        } catch (IOException exception){
            this.sendError(session, "Sorry could not connect to game.");
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
            this.sendError(session, "Invalid gameID.");
            return;
        }
        if(command.getAuthToken() == null) {
            this.sendError(session, "Invalid credentials.");
            return;
        }

        // Authenticate
        AuthData authData = null;
        try{
            authData = this.gameService.authenticateWithToken(command.getAuthToken());

            // Get the GameData
            GameData gameData = this.gameService.gameDAO.getGame(command.getGameID());

            if(gameData.game().isCompleted()){
                this.sendError(session, "Sorry you can't move, the game is already over!");
                return;
            }

            try {

                // Make the move and set the new team turn
                gameData.game().makeMove(command.getMove());
                if (command.getTeamColor().equals("WHITE")){
                    gameData.game().setTeamTurn(ChessGame.TeamColor.BLACK);
                } else {
                    gameData.game().setTeamTurn(ChessGame.TeamColor.WHITE);
                }

            } catch (InvalidMoveException exception){
                this.sendError(session, "Invalid move.");
            }
            this.gameService.gameDAO.updateGame(gameData);

            LoadGameMessage loadGameMessagePlayers = new LoadGameMessage(gameData, false);

            // TODO: Load game from observers
            String[] observerList = this.gameService.gameDAO.getObserverList(command.getGameID());
            LoadGameMessage loadGameMessageObservers = new LoadGameMessage(gameData, true);

            this.connections.notify(username, loadGameMessagePlayers);
            this.connections.notify(observerList, loadGameMessageObservers);

            // Notify opponent that someone joined
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
            // Notify observers someone joined the game
            this.connections.notify(observerList, new NotificationMessage(message));

        } catch (ResponseException exception) {
            this.sendError(session, "Invalid credentials.");

        } catch (IOException exception){
            this.sendError(session, "Sorry could not connect to game.");
        }
    }

    public void leave(Session session, LeaveCommand command){
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
            this.sendError(session, "Invalid gameID.");
            return;
        }
        if(command.getAuthToken() == null) {
            this.sendError(session, "Invalid credentials.");
            return;
        }
        // Authenticate
        AuthData authData = null;
        try{
            authData = this.gameService.authenticateWithToken(command.getAuthToken());
            GameData gameData = this.gameService.gameDAO.getGame(command.getGameID());

            if (command.isObserving()) {
                this.gameService.gameDAO.removeObserverFromGame(command.getUsername(), command.getGameID());
            }

            // Tell the user to clear their game or observing game data
            LoadGameMessage loadGameMessage = new LoadGameMessage(null, command.isObserving());
            this.connections.notify(username, loadGameMessage);

            // Remove the connection after notifying the user that the connection is closed
            this.connections.removeConnection(username);
            this.gameService.gameDAO.removeUserFromGame(command.getGameID(), username);

            // Notify that someone left
            String opponentUsername = null;
            String message = "";

            String[] observerList = this.gameService.gameDAO.getObserverList(command.getGameID());

            if (!command.isObserving()) {
                message = username + " left the game.";
                if (command.getTeamColor() != null && command.getTeamColor().equals("WHITE")) {
                    opponentUsername = gameData.blackUsername();
                } else {
                    opponentUsername = gameData.whiteUsername();
                }
                if (opponentUsername != null) {
                    this.connections.notify(opponentUsername, new NotificationMessage(message));
                }
                if (observerList != null) {
                    this.connections.notify(observerList, new NotificationMessage(message));
                }
            } else {
                // The person who joined is observing
                message = username + " stopped observing the game.";
                ArrayList<String> notifyList = new ArrayList<>();
                String whiteUsername = gameData.whiteUsername();
                String blackUsername = gameData.blackUsername();
                if (whiteUsername != null) {
                    notifyList.add(gameData.whiteUsername());
                }
                if (blackUsername != null) {
                    notifyList.add(gameData.blackUsername());
                }
                if (observerList != null) {
                    notifyList.addAll(List.of(observerList));
                }
                this.connections.notifyExcept(notifyList.toArray(new String[0]), username, new NotificationMessage(message));
            }

        } catch (ResponseException exception) {
            this.sendError(session, "Invalid credentials.");

        } catch (IOException exception){
            this.sendError(session, "Sorry could not connect to game.");
        }
    }

    public void resign(Session session, ResignCommand command){
        if (command.getAuthToken() == null){
            try {
                session.getRemote().sendString(new Gson().toJson(
                        new ErrorMessage("Could not resign, are you still logged in?"))
                );
            } catch (IOException exception){
                throw new ResponseException(0, exception.getMessage());
            }
        }
        if(command.getGameID() == null) {
            try {
                session.getRemote().sendString(new Gson().toJson(
                        new ErrorMessage("Could not resign, are you still playing a game?"))
                );
            } catch (IOException exception){
                throw new ResponseException(0, exception.getMessage());
            }
        }

        // Authenticate
        AuthData authData = null;
        String username = null;
        try{
            authData = this.gameService.authenticateWithToken(command.getAuthToken());
            username = authData.username();

            // Get the GameData
            GameData gameData = this.gameService.gameDAO.getGame(command.getGameID());
            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();

            try {

                // Set the game as completed
                gameData.game().setCompleted();
                if (whiteUsername == null && blackUsername == null){
                    this.sendError(session, "Sorry you can't resign when you're playing by yourself.");
                    return;
                }

            } catch (Exception exception){
                this.sendError(session, "Failed to resign.");
            }

            // Update the game data
            this.gameService.gameDAO.updateGame(gameData);


            // Get opponent name
            String opponentUsername = null;
            if (username.equals(whiteUsername)) {
                opponentUsername = blackUsername;
            } else {
                opponentUsername = whiteUsername;
            }

            // Get observers
            String[] observerList = this.gameService.gameDAO.getObserverList(command.getGameID());

            // Set up load game messages
            LoadGameMessage loadGameMessagePlayers = new LoadGameMessage(gameData, false);
            LoadGameMessage loadGameMessageObservers = new LoadGameMessage(gameData, true);

            String messageForOpponent = username + " resigned. You won the game!";
            String messageForObservers = username + " resigned. " + opponentUsername + " won the game!";

            // Notify the user and observers to load the game
            this.connections.notify(username, loadGameMessagePlayers);
            this.connections.notify(observerList, loadGameMessageObservers);


            // Notify the opponent to load the game, and give them their message
            if (opponentUsername != null) {
                this.connections.notify(opponentUsername, loadGameMessagePlayers);
                this.connections.notify(opponentUsername, new NotificationMessage(messageForOpponent));
            }

            // Notify the observers that the user resigned
            this.connections.notify(observerList, new NotificationMessage(messageForObservers));

        } catch (ResponseException exception) {
            this.sendError(session, "Invalid credentials.");

        } catch (IOException exception){
            this.sendError(session, "Sorry could not connect to game.");
        }
    }

    private void sendError(Session session, String message){
        try {
            session.getRemote().sendString(new Gson().toJson(
                    new ErrorMessage(message))
            );
        } catch (IOException exception){
            throw new ResponseException(0, exception.getMessage());
        }
    }

}
