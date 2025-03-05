package service;

import dataaccess.MemoryAdminDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.GameMetaData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.exception.ResponseException;
import service.request.ClearRequest;
import service.request.CreateGameRequest;
import service.request.ListGamesRequest;
import service.request.RegisterRequest;
import service.result.ClearResult;
import service.result.CreateGameResult;
import service.result.ListGamesResult;
import service.result.RegisterResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GameServiceTests {

    private UserData userData;
    private String authToken;
    private UserService userService;
    private GameService gameService;

    @BeforeEach
    void setUp(){
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        this.gameService = new GameService(authDAO, userDAO, gameDAO);

        this.userService = new UserService(authDAO, userDAO, gameDAO);

        // Register the user then log them out to login
        String username = "bobLikesCheese";
        String password = "Wisconsin100";
        String email = "bob@bobisthebomb.com";
        this.userData = new UserData(username, password, email);

        RegisterResult result = this.userService.register(new RegisterRequest(this.userData));
        this.authToken = result.getAuthData().authToken();
    }

    @Test
    @DisplayName("Valid List Games Request")
    public void testListGamesGoodInput(){
        this.gameService.createGame(new CreateGameRequest(this.authToken, "mygame"));
        ListGamesResult result = this.gameService.listGames(new ListGamesRequest(this.authToken));
        GameMetaData gameMetaData = new GameMetaData(1, null, null, "mygame");
        ArrayList<GameMetaData> games = new ArrayList<GameMetaData>();
        games.add(gameMetaData);

        Assertions.assertEquals(200, result.getStatus());
        Assertions.assertEquals(games, result.getGames());
    }

    @Test
    @DisplayName("Invalid List Games Request")
    public void testListGamesBadInput(){
        this.gameService.createGame(new CreateGameRequest(this.authToken, "mygame"));
        ResponseException exception = Assertions.assertThrows(ResponseException.class,
                () -> this.gameService.listGames(new ListGamesRequest("fakeAuthToken!"))
        );
        Assertions.assertEquals(401, exception.getStatusCode());
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
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