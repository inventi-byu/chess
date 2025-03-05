package service;

import dataaccess.MemoryAdminDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.request.ClearRequest;
import service.result.ClearResult;

public class GameServiceTests {

    private GameService gameService;

    @BeforeEach
    void setUp(){
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        this.gameService = new GameService(authDAO, userDAO, gameDAO);
    }

    @Test
    @DisplayName("Valid List Games Request")
    public void testListGamesGoodInput(){


    }

    @Test
    @DisplayName("Invalid List Games Request")
    public void testListGamesBadInput(){

    }

    @Test
    @DisplayName("Valid Create Game Request")
    public void testCreateGameGoodInput(){

    }

    @Test
    @DisplayName("Invalid Create Game Request")
    public void testCreateGameBadInput(){

    }

    @Test
    @DisplayName("Valid Join Game Request")
    public void testJoinGameGoodInput(){

    }

    @Test
    @DisplayName("Invalid Join Game Request")
    public void testJoinGameBadInput(){

    }

}