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
import java.util.ArrayList;
import java.util.HashMap;

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
    public void onMessage(Session session, String message) throws IOException {
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

    public void connectUser(Session session, ConnectCommand command){
        if(command.getAuthToken() == null) {
            this.sendError(session, "Invalid credentials.");
            return;
        }
        if(command.getGameID() == null){
            this.sendError(session, "Invalid game.");
            return;
        }

        // Authenticate
        AuthData authData = null;
        String username = null;
        String whiteUsername = null;
        String blackUsername = null;
        boolean observing = false;
        ChessGame.TeamColor teamColor = null;

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
            } else if (checkBlack && username.equals(blackUsername)){
                teamColor = ChessGame.TeamColor.BLACK;
            } else {
                // You aren't white or black, so observe
                observing = true;
            }

            this.addActorToGame(command.getGameID(), session);

            // Add the connection and create a LoadGameMessage
            this.connections.addConnection(new Connection(username, session));
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData);
            this.connections.notify(session, loadGameMessage);

            // Notify that someone joined
            String message = "";

            ArrayList<Session> actorList = this.getActorList(command.getGameID());
            //String[] observerList = this.gameService.gameDAO.getObserverList(command.getGameID());

            if (!observing) {
                message = username + " joined the game as " + teamColor + "!";
            } else {
                // The person who joined is observing
                message = username + " joined the game as an observer!";
            }
            if(actorList != null) {
                this.connections.notifyExcept(actorList.toArray(new Session[0]), session, new NotificationMessage(message));
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
            } else if (username.equals(blackUsername)){
                teamColor = ChessGame.TeamColor.BLACK;
            } else {
                this.sendError(session, "Sorry you can't move when you're observing!");
                return;
            }

            // Make sure the game is still going
            if(gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE) || gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
                this.sendError(session, "Sorry you can't move, the game is already over!");
                return;
            }

            if(gameData.game().getTeamTurn() != teamColor){
                this.sendError(session, "Sorry you can't move, its not your turn!");
                return;
            }

            try {
                // Make the move and set the new team turn
                gameData.game().makeMove(command.getMove());

                // Set team turn
                if (teamColor == ChessGame.TeamColor.WHITE){
                    gameData.game().setTeamTurn(ChessGame.TeamColor.BLACK);
                } else {
                    gameData.game().setTeamTurn(ChessGame.TeamColor.WHITE);
                }

            } catch (InvalidMoveException exception){
                this.sendError(session, "Invalid move.");
                return;
            }

            // Update the game and get message ready
            this.gameService.gameDAO.updateGame(gameData);

            // Get information about the move
            ChessMove move = command.getMove();
            String start = PositionConverter.positionToLocation(move.getStartPosition());
            String end = PositionConverter.positionToLocation(move.getEndPosition());
            ArrayList<Session> actorList = this.getActorList(command.getGameID());

            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData);

            if (actorList != null) {
                this.connections.notify(actorList.toArray(new Session[0]), loadGameMessage);
            }

            // Notify people a move was made
            String message = null;
            message = username + " made a move from " + start + " to " + end + ".";

            if (actorList != null) {
                this.connections.notifyExcept(actorList.toArray(new Session[0]), session, new NotificationMessage(message));
            }

            if (!gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE) && !gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
                message = null;
                if(gameData.game().isInCheck(ChessGame.TeamColor.WHITE)){
                    message = whiteUsername + " is now in check.";
                } else if(gameData.game().isInCheck(ChessGame.TeamColor.BLACK)) {
                    message = blackUsername + " is now in check.";
                }
                if (message != null && actorList != null) {
                    this.connections.notify(actorList.toArray(new Session[0]), new NotificationMessage(message));
                }
                return;
            } else {
                String winnerUser = gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK) ?  whiteUsername : blackUsername;
                String loserUser = gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK) ?  blackUsername : whiteUsername;
                message = loserUser + " is in checkmate. " + winnerUser + " won the match!";
                if (actorList != null) {
                    this.connections.notify(actorList.toArray(new Session[0]), new NotificationMessage(message));
                }
            }

        } catch (ResponseException exception) {
            this.sendError(session, "Invalid credentials.");

        } catch (IOException exception){
            this.sendError(session, "Sorry could not connect to game.");
        }
    }

    public void leave(Session session, LeaveCommand command){
        if(command.getGameID() == null){
            this.sendError(session, "Invalid game.");
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
            String username = authData.username();

            if(gameData == null){
                this.sendError(session, "Could not leave game.");
                return;
            }
            this.removeActor(command.getGameID(), session);
            this.connections.removeConnection(session);

            String userToRemove = null;
            boolean observing = false;
            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();

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
                userToRemove = whiteUsername;
            } else if (checkBlack && username.equals(blackUsername)){
                userToRemove = blackUsername;
            } else {
                observing = true;
            }

            if(userToRemove != null){
                this.gameService.gameDAO.removeUserFromGame(command.getGameID(), userToRemove);
            }

            // Notify that someone left
            String message = "";
            ArrayList<Session> actorList = this.getActorList(command.getGameID());

            if (!observing) {
                message = username + " left the game.";
            } else {
                // The person who left is observing
                message = username + " stopped observing the game.";
            }
            if(actorList != null) {
                this.connections.notifyExcept(actorList.toArray(new Session[0]),
                        session, new NotificationMessage(message));
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
            if(gameData == null){
                this.sendError(session, "Could not resign from game.");
                return;
            }

            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();
            String opponentUsername = null;
            ChessGame.TeamColor opponentColor = null;
            boolean observing = false;

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
                opponentUsername = blackUsername;
                opponentColor = ChessGame.TeamColor.BLACK;
            } else if (checkBlack && username.equals(blackUsername)){
                opponentUsername = whiteUsername;
                opponentColor = ChessGame.TeamColor.WHITE;
            } else {
                observing = true;
            }

            if (observing){
                this.sendError(session, "You can't resign when you are just observing!");
                return;
            }
            if (gameData.game().isCompleted()){
                this.sendError(session, "You cannot resign, the game is already over");
                return;
            }

            try {
                // Set the game as completed
                gameData.game().setCompleted();
                gameData.game().winner = opponentColor;
                if (whiteUsername == null && blackUsername == null){
                    this.sendError(session, "Sorry you can't resign when you're observing.");
                    return;
                }
            } catch (Exception exception){
                this.sendError(session, "Failed to resign.");
            }

            // Update the game data
            this.gameService.gameDAO.updateGame(gameData);

            // Get people
            ArrayList<Session> actorList = this.getActorList(command.getGameID());
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData);
            String message = username + " resigned. " + opponentUsername + " won the game!";

            // Notify the user and observers to load the game
            if(actorList != null) {
                this.connections.notify(actorList.toArray(new Session[0]), new NotificationMessage(message));
            }

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

    private void removeActor(Integer gameID, Session session){
        try {
            ArrayList<Session> actorsInGame = this.getActorList(gameID);
            if (actorsInGame != null){
                actorsInGame.remove(session);
                this.updateActorList(gameID, actorsInGame);
            }
        } catch (Exception exception){
            return;
        }
    }
}