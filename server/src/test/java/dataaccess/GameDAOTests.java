package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameMetaData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.service.exception.ResponseException;

import java.util.List;

public class GameDAOTests {
    GameDAO gameDAO;
    UserDAO userDAO;
    String username;
    String password;
    String email;
    String otherUsername;
    String otherPassword;
    String otherEmail;

    @BeforeEach
    void setup(){
        this.gameDAO = new MySQLGameDAO();
        this.userDAO = new MySQLUserDAO();

        try {
            DatabaseManager.createDatabase();
            DatabaseManager.resetDatabase();
        } catch (Exception exception) {
            throw new RuntimeException(String.format("Failed to create database. Message: %s"), exception);
        }

        // Add two users to play the game
        this.username = "bobsmith100";
        this.password = "mysecurepassword";
        this.email = "bob@bob.com";
        UserData userData = new UserData(username, password, email);
        Assertions.assertTrue(this.userDAO.createUser(userData));

        this.otherUsername = "friendofbob";
        this.otherPassword = "supersecure";
        this.otherEmail = "friend@friendsofbob.org";
        UserData otherUserData = new UserData(otherUsername, otherPassword, otherEmail);
        Assertions.assertTrue(this.userDAO.createUser(otherUserData));

    }


    @Test
    public void addGameTestGoodInput(){
        Assertions.assertEquals(1, this.gameDAO.addGame("myFirstGame"));
    }

    @Test
    public void addGameTestBadInput(){
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> this.gameDAO.addGame(null));
        Assertions.assertEquals("Could not add game. Message from database: " +
                "dataaccess.DataAccessException: Failed to execute update: " +
                "INSERT INTO game_table (white_username, black_username, game_name, game) VALUES (?, ?, ?, ?). " +
                "Error Message: java.sql.SQLIntegrityConstraintViolationException: " +
                "Column 'game_name' cannot be null",
                exception.getMessage()
        );
    }

    @Test
    public void getGameTestGoodInput(){
        String gameName = "myGame";
        int gameID = this.gameDAO.addGame(gameName);
        Assertions.assertEquals(1, gameID);

        GameData actualGameData = this.gameDAO.getGame(gameID);

        GameData expectedGameData = new GameData(gameID, null, null, gameName, new ChessGame());
        Assertions.assertEquals(expectedGameData, actualGameData);

    }

    @Test
    public void getGameTestBadInput(){
        String gameName = "myGame";
        int gameID = this.gameDAO.addGame(gameName);
        Assertions.assertEquals(1, gameID);

        int fakeID = 24234324;
        GameData actualGameData = this.gameDAO.getGame(fakeID);

        Assertions.assertNull(actualGameData);
    }

    @Test
    public void getAllGamesTestGoodInput(){

        int[] gameIDArray = new int[4];
        String[] gameNameArray = new String[4];

        for (int i = 0; i < 4; i++){
            String gameName = "theBestGame_" + i;
            gameIDArray[i] = this.gameDAO.addGame(gameName);
            gameNameArray[i] = gameName;
        }

        GameMetaData[] games = this.gameDAO.getAllGames();

        int i = 0;
        for (GameMetaData gameMetaData : games){
            GameMetaData expectedGameMetaData = new GameMetaData(gameIDArray[i], null, null, gameNameArray[i]);
            Assertions.assertEquals(expectedGameMetaData, gameMetaData);
            i++;
        }
        // Make sure that all the games were actually listed, and not just some
        // Just i and not i++ because i++ is at the end, so it will be the actual length
        Assertions.assertEquals(gameNameArray.length, games.length);
    }

    @Test
    public void getAllGamesTestBadInput(){
        GameMetaData[] games = this.gameDAO.getAllGames();
        Assertions.assertEquals(0, games.length);
    }

    @Test
    public void addUserToGameTestGoodInput(){
        // Add some games
        int[] gameIDArray = new int[4];
        String[] gameNameArray = new String[4];

        for (int i = 0; i < 4; i++){
            String gameName = "theBestGame_" + i;
            gameIDArray[i] = this.gameDAO.addGame(gameName);
            gameNameArray[i] = gameName;
        }

        // Make sure all the games were properly added
        GameMetaData[] games = this.gameDAO.getAllGames();
        Assertions.assertEquals(gameNameArray.length, games.length);

        // Test
        int chosenGameIDIndex = 0;
        int chosenGameID = gameIDArray[chosenGameIDIndex];
        // This expects a string for the player color
        Assertions.assertTrue(this.gameDAO.addUserToGame(chosenGameID, "WHITE", username));
        Assertions.assertTrue(this.gameDAO.addUserToGame(chosenGameID, "BLACK", otherUsername));

        // Double check database to see if the data persisted
        GameData updatedGameMetaData = this.gameDAO.getGame(chosenGameID);
        Assertions.assertEquals(username, updatedGameMetaData.whiteUsername());
        Assertions.assertEquals(otherUsername, updatedGameMetaData.blackUsername());
    }

    @Test
    public void addUserToGameTestBadInput(){
        // Add some games
        int[] gameIDArray = new int[4];
        String[] gameNameArray = new String[4];

        for (int i = 0; i < 4; i++){
            String gameName = "theBestGame_" + i;
            gameIDArray[i] = this.gameDAO.addGame(gameName);
            gameNameArray[i] = gameName;
        }

        // Make sure all the games were properly added
        GameMetaData[] games = this.gameDAO.getAllGames();
        Assertions.assertEquals(gameNameArray.length, games.length);

        // Add the users to the game
        int chosenGameIDIndex = 0;
        int chosenGameID = gameIDArray[chosenGameIDIndex];
        // This expects a string for the player color
        Assertions.assertTrue(this.gameDAO.addUserToGame(chosenGameID, "WHITE", username));
        Assertions.assertTrue(this.gameDAO.addUserToGame(chosenGameID, "BLACK", otherUsername));

        // Double check database to see if the data persisted
        GameData updatedGameMetaData = this.gameDAO.getGame(chosenGameID);
        Assertions.assertEquals(username, updatedGameMetaData.whiteUsername());
        Assertions.assertEquals(otherUsername, updatedGameMetaData.blackUsername());

        // TESTS

        // ADD USER THAT DOESN'T EXIST (taken color is handled by the GameService)
        int otherChosenGameIdIndex = 1;
        int otherChosenGameID = gameIDArray[otherChosenGameIdIndex];
        String fakeUser = "FakeUser3000";

        ResponseException exceptionUserNoExist = Assertions.assertThrows(ResponseException.class,
                () -> this.gameDAO.addUserToGame(otherChosenGameID, "WHITE", fakeUser)
        );
    }
}
