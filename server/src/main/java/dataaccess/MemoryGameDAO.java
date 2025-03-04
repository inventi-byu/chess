package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameMetaData;
import service.ResponseException;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    private ArrayList<GameData> gameDB;
    private int lastID;


    public MemoryGameDAO(){
        this.gameDB = new ArrayList<GameData>();
        this.lastID = 0;
    }


    public int addGame(String gameName){
        int newGameID = this.getNewID();
        GameData gameData = new GameData(newGameID, null, null, gameName, new ChessGame());
        this.gameDB.add(gameData);
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
        if(game == null){
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

    public List<GameMetaData> getAllGames(){
        ArrayList<GameMetaData> games = new ArrayList<GameMetaData>();
        for (GameData game : gameDB){
            games.add(new GameMetaData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return games;
    }


    public int getNewID(){
        // Increment lastID, then return the incremented value
        return ++this.lastID;
    }

    public void clearGameTable(){
        this.gameDB = new ArrayList<GameData>();
    }
}
