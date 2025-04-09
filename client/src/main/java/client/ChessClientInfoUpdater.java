package client;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

public class ChessClientInfoUpdater {
    private final ChessClient client;

    public ChessClientInfoUpdater(ChessClient client){
        this.client = client;
    }
    // Public because they are used by the NotificationHandler
    public void updateGameInfo(GameData newGameData) {
        ChessGame updatedGame = newGameData.game();
        ChessBoard updatedBoard = updatedGame.getBoard();
        int updatedGameID = newGameData.gameID();
        this.client.setGameData(newGameData);
        this.client.setGame(updatedGame);
        this.client.setBoard(updatedBoard);
        this.client.setGameID(updatedGameID);
    }
    public void updateObservingGameInfo(GameData newObservingGameData) {
        ChessGame updatedObservingGame = newObservingGameData.game();
        ChessBoard updatedObservingBoard = updatedObservingGame.getBoard();
        this.client.setObservingGameData(newObservingGameData);
        this.client.setObservingGame(updatedObservingGame);
        this.client.setObservingBoard(updatedObservingBoard);
    }
    public void clearGameInfo(){
        this.client.setGameData(null);
        this.client.setGame(null);
        this.client.setBoard(null);
        this.client.setGameID(0);
    }
    public void clearObservingGameInfo(){
        this.client.setObservingGameData(null);
        this.client.setObservingGame(null);
        this.client.setObservingBoard(null);
    }
}
