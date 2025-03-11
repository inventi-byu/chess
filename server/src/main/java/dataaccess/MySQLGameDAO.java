package dataaccess;

import model.GameData;
import model.GameMetaData;

import java.util.List;

public class MySQLGameDAO implements GameDAO {

    public int addGame(String gameName){
        throw new RuntimeException("Not implemented.");
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
