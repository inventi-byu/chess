package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import model.AuthData;

import model.GameData;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebSocket
public class WebSocketHandler {
    GameService gameService;
    HashMap<Integer, ArrayList<Session>> actors;

    private final ConnectionsManager connections = new ConnectionsManager();

    public WebSocketHandler(GameService gameService){
        this.gameService = gameService;
        this.actors = new HashMap<>();
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
        if(command.getAuthToken() == null) {
            this.sendError(session, "Invalid credentials.");
            return;
        }
        if(command.getGameID() == null){
            this.sendError(session, "Invalid gameID.");
            return;
        }

        // Authenticate
        AuthData authData = null;
        String username = null;
        String whiteUsername = null;
        String blackUsername = null;
        boolean observing = false;
        ChessGame.TeamColor teamColor = null;
        String opponentUsername = null;

        try{
            authData = this.gameService.authenticateWithToken(command.getAuthToken());
            username = authData.username();

            // Get the GameData
            GameData gameData = this.gameService.gameDAO.getGame(command.getGameID());

            if (gameData == null){
                this.sendError(session, "Invalid game. Could not connect.");
                return;
            }

            whiteUsername = gameData.whiteUsername();
            blackUsername = gameData.blackUsername();

            // Make sure .equals does not throw null pointer exception
            boolean checkWhite = false;
            boolean checkBlack = false;

            if (whiteUsername != null){
                checkWhite = true;
            }
            if (blackUsername != null){
                checkBlack = true;
            }

            // Observing if we are not white username, and we are not black username

            if (checkWhite && username.equals(whiteUsername)){
                teamColor = ChessGame.TeamColor.WHITE;
                opponentUsername = blackUsername;
            } else if (checkBlack && username.equals(blackUsername)){
                teamColor = ChessGame.TeamColor.BLACK;
                opponentUsername = whiteUsername;
            } else {
                // You aren't white or black, so observe
                observing = true;
            }

            if(observing){
                this.actors.addActor(command.getGameID(), session);

                //this.gameService.gameDAO.addObserverToGame(username, command.getGameID());
            }
            // Add the connection and create a LoadGameMessage
            this.connections.addConnection(new Connection(username, session));
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData, observing);
            this.connections.notify(session, loadGameMessage);

            // Notify that someone joined
            String message = "";

            ArrayList<Session> observerList = this.getObserverList(command.getGameID());
            //String[] observerList = this.gameService.gameDAO.getObserverList(command.getGameID());

            if (!observing) {
                message = username + " joined the game as " + teamColor + "!";
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
        if (command.getAuthToken() == null) {
            this.sendError(session, "Invalid credentials.");
            return;
        }
        if (command.getGameID() == null){
            this.sendError(session, "Invalid gameID.");
            return;
        }

        if (command.getMove() == null){
            this.sendError(session, "Invalid move.");
            return;
        }

        // Authenticate
        AuthData authData = null;
        String username = null;
        String whiteUsername = null;
        String blackUsername = null;
        ChessGame.TeamColor teamColor = null;
        String opponentUsername = null;

        try{
            // Authenticate and get GameData
            authData = this.gameService.authenticateWithToken(command.getAuthToken());

            // Get the GameData
            GameData gameData = this.gameService.gameDAO.getGame(command.getGameID());

            // Grab username and find the team color
            username = authData.username();

            whiteUsername = gameData.whiteUsername();
            blackUsername = gameData.blackUsername();

            if (username.equals(whiteUsername)){
                teamColor = ChessGame.TeamColor.WHITE;
                opponentUsername = blackUsername;
            } else if (username.equals(blackUsername)){
                teamColor = ChessGame.TeamColor.BLACK;
                opponentUsername = whiteUsername;
            } else {
                this.sendError(session, "Sorry you can't move when you're observing!");
                return;
            }

            // Make sure the game is still going
            if(gameData.game().isCompleted()){
                this.sendError(session, "Sorry you can't move, the game is already over!");
                return;
            }

            try {
                // Make the move and set the new team turn
                gameData.game().makeMove(command.getMove());

                if (teamColor == ChessGame.TeamColor.WHITE){
                    gameData.game().setTeamTurn(ChessGame.TeamColor.BLACK);
                } else {
                    gameData.game().setTeamTurn(ChessGame.TeamColor.WHITE);
                }

            } catch (InvalidMoveException exception){
                this.sendError(session, "Invalid move.");
            }

            // Update the game
            this.gameService.gameDAO.updateGame(gameData);

            LoadGameMessage loadGameMessagePlayers = new LoadGameMessage(gameData, false);
            LoadGameMessage loadGameMessageObservers = new LoadGameMessage(gameData, true);

            String[] observerList = this.gameService.gameDAO.getObserverList(command.getGameID());

            this.connections.notify(username, loadGameMessagePlayers);
            this.connections.notify(observerList, loadGameMessageObservers);

            // Get information about the move
            ChessMove move = command.getMove();
            String start = PositionConverter.positionToLocation(move.getStartPosition());
            String end = PositionConverter.positionToLocation(move.getEndPosition());

            String message = username + " made a move from " + start + " to " + end + ".";

            if (opponentUsername != null) {
                this.connections.notify(opponentUsername, loadGameMessagePlayers);
                this.connections.notify(opponentUsername, new NotificationMessage(message));
            }

            // Notify observers a move was made
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

    private ArrayList<Session> getActorList(Integer gameID){
        try {
            return this.actors.get(gameID);
        } catch (Exception exception){
            // No observer list
            return null;
        }
    }

    private void updateActorList(Integer gameID, ArrayList<Session> newObserverList){
        try{
            this.actors.replace(gameID, newObserverList);
        } catch (Exception exception){
            return;
        }
    }

    private void addActorToGame(Integer gameID, Session session){
        try {
            ArrayList<Session> actorsInGame = this.getActorList(gameID);
            if (actorsInGame != null){
                actorsInGame.add(session);
                this.updateActorList(gameID, actorsInGame);
            } else {
                // Add a new list because there are no actors yet
                ArrayList<Session> freshActorList = new ArrayList<Session>();
                freshActorList.add(session);
                this.actors.put(gameID, freshActorList);
            }
        } catch (Exception exception){
            return;
        }
    }



}
