package dataaccess;

import model.GameData;

public interface GameDAO {

    public GameData getGame(int gameID);

    public int addGame(String gameName);

    public int getNewID();
}
