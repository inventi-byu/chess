package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameMetaData;
import service.exception.ResponseException;

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
            throw new ResponseException(500, String.format("Could not create user. Message from database: %s", exception));
        }
    }

    public GameData getGame(int gameID){
        throw new RuntimeException("Not implemented.");
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
