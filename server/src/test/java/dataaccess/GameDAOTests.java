package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        throw new RuntimeException("Not implemented.");
    }

    @Test
    public void getGameTestGoodInput(){
        throw new RuntimeException("Not implemented.");
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
