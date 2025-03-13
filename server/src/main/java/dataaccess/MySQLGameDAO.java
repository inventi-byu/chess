package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameMetaData;
import model.UserData;
import service.exception.ResponseException;

import java.util.ArrayList;
import java.util.List;

import static dataaccess.DatabaseManager.*;
import static dataaccess.DatabaseManager.USER_TABLE_EMAIL;

public class MySQLGameDAO implements GameDAO {

    public int addGame(String gameName){
        String white_username = null;
        String black_username = null;
        String game = new Gson().toJson(new ChessGame());

        String statement = "INSERT INTO " + GAME_TABLE + " (" +
                GAME_TABLE_WHITE_USERNAME + ", " +
                GAME_TABLE_BLACK_USERNAME + ", "  +
                GAME_TABLE_GAME_NAME + ", " +
                GAME_TABLE_GAME + ") VALUES (?, ?, ?, ?)";

        try {
            int gameID = DatabaseManager.updateDB(statement, white_username, black_username, gameName, game);
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
            String[] expectedLabels = {GAME_TABLE_GAME_ID, GAME_TABLE_WHITE_USERNAME, GAME_TABLE_BLACK_USERNAME, GAME_TABLE_GAME_NAME, GAME_TABLE_GAME};
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

    public List<GameMetaData> getAllGames(){
        throw new RuntimeException("Not implemented.");
    }

    public boolean addUserToGame(int gameID, String playerColor, String username){
        throw new RuntimeException("Not implemented.");
    }

    public int getNewID(){
        // NOTE: This might be handled automatically by the database!
        throw new RuntimeException("Not implemented.");
    }
}
