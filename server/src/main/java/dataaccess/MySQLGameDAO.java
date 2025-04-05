package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameMetaData;
import server.service.exception.ResponseException;

import java.util.ArrayList;
import java.util.List;

import static dataaccess.DatabaseManager.*;

public class MySQLGameDAO implements GameDAO {

    public int addGame(String gameName){
        String whiteUsername = null;
        String blackUsername = null;
        String game = new Gson().toJson(new ChessGame());

        String statement = "INSERT INTO " + GAME_TABLE + " (" +
                GAME_TABLE_WHITE_USERNAME + ", " +
                GAME_TABLE_BLACK_USERNAME + ", "  +
                GAME_TABLE_GAME_NAME + ", " +
                GAME_TABLE_GAME + ") VALUES (?, ?, ?, ?)";

        try {
            int gameID = DatabaseManager.updateDB(statement, whiteUsername, blackUsername, gameName, game);
            return gameID;

        } catch (DataAccessException exception){
            throw new ResponseException(500, String.format("Could not add game. Message from database: %s", exception));
        }
    }

    public GameData getGame(int gameID){

        String statement = "SELECT " +
                GAME_TABLE_GAME_ID + ", " +
                GAME_TABLE_WHITE_USERNAME + ", " +
                GAME_TABLE_BLACK_USERNAME + ", " +
                GAME_TABLE_GAME_NAME + ", " +
                GAME_TABLE_GAME +
                " FROM " + GAME_TABLE +
                " WHERE " + GAME_TABLE_GAME_ID + "=?;";

        try {
            String[] expectedLabels = {
                    GAME_TABLE_GAME_ID,
                    GAME_TABLE_WHITE_USERNAME,
                    GAME_TABLE_BLACK_USERNAME,
                    GAME_TABLE_GAME_NAME,
                    GAME_TABLE_GAME
            };

            ArrayList<String> data = DatabaseManager.queryDB(statement, expectedLabels, gameID);

            if(data.isEmpty()){
                return null;
            }

            return new GameData(
                    Integer.parseInt(data.get(0)),
                    data.get(1),
                    data.get(2),
                    data.get(3),
                    new Gson().fromJson(data.get(4), ChessGame.class)
            );
        } catch (DataAccessException exception){
            throw new ResponseException(500, String.format("Error: Something went wrong getting the game. Message: %s", exception));
        }
    }

    public GameMetaData[] getAllGames(){
        String statement = "SELECT " +
                GAME_TABLE_GAME_ID + ", " +
                GAME_TABLE_WHITE_USERNAME + ", " +
                GAME_TABLE_BLACK_USERNAME + ", " +
                GAME_TABLE_GAME_NAME +
                " FROM " + GAME_TABLE + ";";

        try {
            String[] expectedLabels = {GAME_TABLE_GAME_ID, GAME_TABLE_WHITE_USERNAME, GAME_TABLE_BLACK_USERNAME, GAME_TABLE_GAME_NAME};
            ArrayList<String> data = DatabaseManager.queryDB(statement, expectedLabels);

            ArrayList<GameMetaData> games = new ArrayList<GameMetaData>();

            if(data.isEmpty()){
                // If there is no data, we still need to return an empty list for the response to be correct
                return games.toArray(new GameMetaData[0]);
            }

            // Make sure that we have the right amount of stuff
            assert data.size() % 4 == 0;

            for (int i = 0; i < data.size(); i+=4) {
               games.add(new
                       GameMetaData(
                        Integer.parseInt(data.get(i)),
                        data.get(i+1),
                        data.get(i+2),
                        data.get(i+3)
                       )
               );
            }
            return games.toArray(new GameMetaData[0]);

        } catch (DataAccessException exception){
            throw new ResponseException(500, String.format("Error: Something went wrong getting all games. Message: %s", exception));
        }
    }

    public boolean addUserToGame(int gameID, String playerColor, String username){
        String statement = null;
        switch (playerColor){
            case "WHITE" -> statement = "UPDATE " + GAME_TABLE + " SET " +
                    GAME_TABLE_WHITE_USERNAME +
                    "=? WHERE " + GAME_TABLE_GAME_ID + "=?;";
            case "BLACK" -> statement = "UPDATE " + GAME_TABLE + " SET " +
                    GAME_TABLE_BLACK_USERNAME +
                    "=? WHERE " + GAME_TABLE_GAME_ID + "=?;";
        }
        try {
            DatabaseManager.updateDB(statement, username, gameID);
            return true;

        } catch (DataAccessException exception){
            throw new ResponseException(500, String.format("Could not add user %s to game id=%s as %s player. " +
                    "Message from database: %s",
                    username,
                    gameID,
                    playerColor,
                    exception)
            );
        }
    }

    public boolean updateGame(GameData gameData){
        String statement = "UPDATE " + GAME_TABLE + " SET " +
                GAME_TABLE_GAME +
                "=? WHERE " + GAME_TABLE_GAME_ID + "=?;";
        try {
            String gameAsJSON = new Gson().toJson(gameData.game());
            DatabaseManager.updateDB(statement, gameAsJSON, gameData.gameID());
            return true;

        } catch (DataAccessException exception){
            throw new ResponseException(500, String.format("Could not update game with id %s. " +
                            "Message from database: %s",
                    gameData.gameID(),
                    exception)
            );
        }
    }

    public boolean removeUserFromGame(String username){
        throw new RuntimeException("Not implemented.");
    }

    public String[] getObserverList(int gameID){
        throw new ResponseException(0, "Get observer list Not implemented.");
    }

    public boolean addObserverToGame(){
        throw new RuntimeException("Not implemented.");
    }

    public boolean removeObserverFromGame(String username){
        throw new RuntimeException("Not implemented").
    }
}
