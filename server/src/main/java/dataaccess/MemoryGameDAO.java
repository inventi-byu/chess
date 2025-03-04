package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    private ArrayList<GameData> gameDB;
    private int lastID;


    public MemoryGameDAO(){
        this.gameDB = new ArrayList<GameData>();
        this.lastID = 0;
    }

    public GameData getGame(int gameID){
        for (GameData game: gameDB){
            // int is primitive, no equals()
            if(game.gameID() == gameID){
                return game;
            }
        }
        return null;
    }

    public int addGame(String gameName){
        int newGameID = this.getNewID();
        GameData gameData = new GameData(newGameID, null, null, gameName, new ChessGame());
        this.gameDB.add(gameData);
        return newGameID;

    }

    public int getNewID(){
        // Increment lastID, then return the incremented value
        return ++this.lastID;
    }

    public void clearGameTable(){
        this.gameDB = new ArrayList<GameData>();
    }
}
