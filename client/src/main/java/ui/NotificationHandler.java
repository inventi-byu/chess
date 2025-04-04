package ui;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.GameData;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

/**
 * When the following events occur, notification messages should be displayed on the screen of each player that is involved in the game (player or observer).
 *
 *     A user connected to the game as a player (black or white). The notification message should include the player’s name and which side they are playing (black or white).
 *     A user connected to the game as an observer. The notification message should include the observer’s name.
 *     A player made a move. The notification message should include the player’s name and a description of the move that was made. (This is in addition to the board being updated on each player’s screen.)
 *     A player left the game. The notification message should include the player’s name.
 *     A player resigned the game. The notification message should include the player’s name.
 *     A player is in check. The notification message should include the player’s name (this notification is generated by the server).
 *     A player is in checkmate. The notification message should include the player’s name (this notification is generated by the server).
 */
public class NotificationHandler {

    private GameData currentGameData;
    private ChessUI chessUI;

    public NotificationHandler(ChessUI chessUI){
        this.currentGameData = null;
        this.chessUI = chessUI;
    }

    // TODO: this should NOT be using the exclude parameters, that is for the server to handle.

    /**
     * Listens for notifications coming from the websocket.
     * @param message the json of the ServerMessage that came in
     */
    public void handle(String message){
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()){
            case LOAD_GAME:
                LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                this.handleLoadGameMessage(loadGameMessage);
                break;
            case NOTIFICATION:
                NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                this.handleNotificationMessage(notificationMessage);
                break;
            case ERROR:
                ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                this.handleErrorMessage(errorMessage);
                break;
        }
    }

    public void handleLoadGameMessage(LoadGameMessage loadGameMessage){
        this.currentGameData = loadGameMessage.getGame();
        if(loadGameMessage.isObserving()){
            this.chessUI.client.updateObservingGameInfo(this.currentGameData);
            this.chessUI.displayln("");
            this.chessUI.displayChessBoard(this.chessUI.client.getObservingBoard(), ChessGame.TeamColor.WHITE, null);
            this.chessUI.displayPrompt();
        } else {
            this.chessUI.client.updateGameInfo(this.currentGameData);
            this.chessUI.displayln("");
            this.chessUI.displayChessBoard(this.chessUI.client.getBoard(), this.chessUI.client.getTeamColor(), null);
            this.chessUI.displayPrompt();
        }
    }

    public void handleNotificationMessage(NotificationMessage notificationMessage){
        throw new RuntimeException("Not implemented.");
    }

    public void handleErrorMessage(ErrorMessage errorMessage){
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Notifies that a user joined, with name and color.
     * @param exclude if not null, exclude that user from the message
     * @param username user that joined
     * @param teamColor color that user joined with
     */
    public void notifyUserConnected(String exclude, String username, ChessGame.TeamColor teamColor){
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Notifies that an observer joined, with name.
     * @param exclude if not null, exclude that user from the message
     * @param username user that joined as observer
     */
    public void notifyObserverConnected(String exclude, String username){
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Notifies that a player made a move, with name.
     * @param exclude if not null, exclude that user from the message
     * @param username user that moved
     * @param move the move that was made by the user
     */
    public void notifyMadeMove(String exclude, String username, ChessMove move){
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Notifies that a user or observer left the game or stopped observing.
     * @param exclude if not null, exclude that user from the message
     * @param username the user or observer that left of stopped observing.
     */
    public void notifyUserLeft(String exclude, String username){
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Notifies that a user resigned the game.
     * @param exclude if not null, exclude that user from the message
     * @param username the user that resigned
     */
    public void notifyUserResigned(String exclude, String username){
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Notifies that a user is now in check. Generated by the server.
     * @param exclude if not null, exclude that user from the message
     * @param username the user that is now in check
     */
    public void notifyUserInCheck(String exclude, String username){
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Notifies that a user is now in checkmate. Generated by the server.
     * @param exclude if not null, exclude that user from the message
     * @param username the user that is now in check
     */
    public void notifyUserInCheckmate(String exclude, String username){
        throw new RuntimeException("Not implemented.");
    }

}
