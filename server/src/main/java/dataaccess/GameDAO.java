package dataaccess;

import model.GameData;
import model.GameMetaData;

import java.util.List;

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
    public List<GameMetaData> getAllGames();

    /**
     * Adds a user to a game in the game database with the specified playerColor.
     * @param gameID the gameID of the game to add the user to.
     * @param playerColor which color to add the user as.
     * @param username the username of the player to add.
     * @return true if the user was added to the game.
     */
    public boolean addUserToGame(int gameID, String playerColor, String username);
}
