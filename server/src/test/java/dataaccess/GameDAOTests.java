package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.exception.ResponseException;

public class GameDAOTests {

    GameDAO gameDAO;

    @BeforeEach
    void setup(){
        this.gameDAO = new MySQLGameDAO();

        try {
            DatabaseManager.createDatabase();
        } catch (Exception exception) {
            throw new RuntimeException(String.format("Failed to create database. Message: %s"), exception);
        }
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
        throw new RuntimeException("Not implemented.");
    }

    @Test
    public void getAllGamesTestGoodInput(){
        throw new RuntimeException("Not implemented.");
    }

    @Test
    public void getAllGamesTestBadInput(){
        throw new RuntimeException("Not implemented.");
    }

    @Test
    public void addUserToGameTestGoodInput(){
        throw new RuntimeException("Not implemented.");
    }

    @Test
    public void addUserToGameTestBadInput(){
        throw new RuntimeException("Not implemented.");
    }

    @Test
    public void getNewIDTest(){
        // May be handled by the database automatically
        throw new RuntimeException("Not implemented.");
    }
}
