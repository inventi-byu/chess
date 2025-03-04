package dataaccess;

import model.GameData;
import model.GameMetaData;

import java.util.List;

public interface GameDAO {

    public GameData getGame(int gameID);

    public List<GameMetaData> getAllGames();

    public int addGame(String gameName);

    public int getNewID();
}
