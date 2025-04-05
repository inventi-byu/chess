package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameMetaData;
import server.service.exception.ResponseException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static dataaccess.DatabaseManager.*;

public class MemoryGameDAO implements GameDAO {

    private ArrayList<GameData> gameDB;
    private HashMap<Integer, String[]> observers;
    private int lastID;


    public MemoryGameDAO(){
        this.gameDB = new ArrayList<GameData>();
        this.lastID = 0;
        this.observers = new HashMap<>();
    }

    public int addGame(String gameName){
        int newGameID = this.getNewID();
        GameData gameData = new GameData(newGameID, null, null, gameName, new ChessGame());
        this.gameDB.add(gameData);
        this.observers.put(newGameID, new String[0]);
        return newGameID;

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

    public boolean addUserToGame(int gameID, String playerColor, String username){
        GameData game = this.getGame(gameID);
        if(game == null || playerColor == null){
            throw new ResponseException(400, "Error: bad request");
        }

        switch (playerColor){
            case "WHITE":
                if(game.whiteUsername() != null){
                    throw new ResponseException(403, "Error: already taken");
                }
                GameData gameWithWhiteUserAdded = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                gameDB.remove(game);
                gameDB.add(gameWithWhiteUserAdded);
                return true;
            case "BLACK":
                if(game.blackUsername() != null){
                    throw new ResponseException(403, "Error: already taken");
                }
                GameData gameWithBlackUserAdded = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                gameDB.remove(game);
                gameDB.add(gameWithBlackUserAdded);
                return true;
        }
        // If the color was not white or black, that's a bad request
        throw new ResponseException(400, "Error: bad request");

    }

    public GameMetaData[] getAllGames(){
        ArrayList<GameMetaData> games = new ArrayList<GameMetaData>();
        for (GameData game : gameDB){
            games.add(new GameMetaData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return games.toArray(new GameMetaData[0]);
    }

    /**
     * Gets a new gameID to use for creating games. Should only be accessed by the GameDAO class.
     * This is only used in the in-memory implementation, as the database creates ids for us in the MySQL implementation.
     * @return the new gameID as an int.
     */
    public int getNewID(){
        // Increment lastID, then return the incremented value
        return ++this.lastID;
    }

    public void clearGameTable(){
        this.gameDB = new ArrayList<GameData>();
    }


    public boolean updateGame(GameData gameData){
        for (GameData game : this.gameDB){
            if (game.gameID() == gameData.gameID()){
                gameDB.remove(game);
                gameDB.add(gameData);
                return true;
            }
        }
        return false;
    }

    public boolean removeUserFromGame(String username){
        throw new RuntimeException("Not implemented.");
    }

    public String[] getObserverList(int gameID){
        return this.observers.get(gameID);
    }

    public boolean addObserverToGame(String username, int gameID){
        ArrayList<String> observersAsArrayList = new ArrayList<>(List.of(this.observers.get(gameID)));
        observersAsArrayList.add(username);
        this.observers.remove(gameID);
        this.observers.put(gameID, observersAsArrayList.toArray(new String[0]));
        return true;
    }

    public boolean removeObserverFromGame(String username, int gameID){
        ArrayList<String> observersAsArrayList = new ArrayList<>(List.of(this.observers.get(gameID)));
        observersAsArrayList.remove(username);
        this.observers.remove(gameID);
        this.observers.put(gameID, observersAsArrayList.toArray(new String[0]));
        return true;
    }
}
