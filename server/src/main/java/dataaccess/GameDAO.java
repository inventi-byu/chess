package dataaccess;

import com.google.gson.Gson;
import model.GameData;
import model.GameMetaData;
import server.service.exception.ResponseException;

import java.util.ArrayList;
import java.util.List;

import static dataaccess.DatabaseManager.*;

public interface GameDAO {

    /**
     * Creates a new game and adds it to the database.
     * @param gameName the name of the game to create.
     * @return the gameID as an int.
     */
    public int addGame(String gameName);

    /**
     * Fetches a game from the database by gameID.
     * @param gameID the gameID of the game to fetch.
     * @return the GameData representing the game.
     */
    public GameData getGame(int gameID);

    /**
     * Fetches a list of all the games' metadata (without the ChessGame object).
     * @return Returns a list of GameMetaData for all the games in the database.
     */
    public GameMetaData[] getAllGames();

    /**
     * Adds a user to a game in the game database with the specified playerColor.
     * @param gameID the gameID of the game to add the user to.
     * @param playerColor which color to add the user as.
     * @param username the username of the player to add.
     * @return true if the user was added to the game.
     */
    public boolean addUserToGame(int gameID, String playerColor, String username);

    /**
     * Updates the game information in the game database.
     * @param gameData the gameData with the information
     * @return true if the game was successfully updated.
     */
    public boolean updateGame(GameData gameData);

    public boolean removeUserFromGame(int gameID, String username);

    public String[] getObserverList(int gameID);

    public boolean addObserverToGame(String username, int gameID);

    public boolean removeObserverFromGame(String username, int gameID);
}
