package dataaccess;

import model.GameData;
import model.GameMetaData;

import java.util.List;

public interface GameDAO {
    public int addGame(String gameName);

    public GameData getGame(int gameID);

    public List<GameMetaData> getAllGames();

    public boolean addUserToGame(int gameID, String playerColor, String username);

    public int getNewID();
}
