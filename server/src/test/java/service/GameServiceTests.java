package service;

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
import service.request.*;
import service.result.*;

import java.util.ArrayList;

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
        CreateGameResult result = this.gameService.createGame(new CreateGameRequest(this.authToken, "mygame"));
        Assertions.assertEquals(200, result.getStatus());
        Assertions.assertEquals(1, result.getGameID());
    }

    @Test
    @DisplayName("Invalid Create Game Request")
    public void testCreateGameBadInput(){
        // Unauthorized
        ResponseException authException = Assertions.assertThrows(ResponseException.class,
                () -> this.gameService.createGame(
                        new CreateGameRequest("FakeAUTHtoken9!", "mygame"))
        );
        Assertions.assertEquals(401, authException.getStatusCode());
        Assertions.assertEquals("Error: unauthorized", authException.getMessage());

        // Bad Request
        ResponseException badRequestException = Assertions.assertThrows(ResponseException.class,
                () -> this.gameService.createGame(
                        new CreateGameRequest(this.authToken, ""))
        );
        Assertions.assertEquals(400, badRequestException.getStatusCode());
        Assertions.assertEquals("Error: bad request", badRequestException.getMessage());
    }

    @Test
    @DisplayName("Valid Join Game Request")
    public void testJoinGameGoodInput(){
        CreateGameResult createGameResult = this.gameService.createGame(new CreateGameRequest(this.authToken, "mygame"));
        JoinGameResult result = this.gameService.joinGame(new JoinGameRequest(this.authToken, "BLACK", createGameResult.getGameID()));
        Assertions.assertEquals(200, result.getStatus());
    }

    @Test
    @DisplayName("Invalid Join Game Request")
    public void testJoinGameBadInput(){
        // Need to test bad authorization, bad game id, and color taken
        CreateGameResult createGameResult = this.gameService.createGame(new CreateGameRequest(this.authToken, "mygame"));

        // Invalid authorization
        ResponseException authException = Assertions.assertThrows(ResponseException.class,
                () -> this.gameService.joinGame(new JoinGameRequest("FaKeAuTh!", "BLACK", createGameResult.getGameID()))
        );
        Assertions.assertEquals(401, authException.getStatusCode());
        Assertions.assertEquals("Error: unauthorized", authException.getMessage());

        // Add a player to the game
        this.gameService.joinGame(new JoinGameRequest(this.authToken, "BLACK", createGameResult.getGameID()));


        ResponseException takenException = Assertions.assertThrows(ResponseException.class,
                () -> this.gameService.joinGame(new JoinGameRequest(this.authToken, "BLACK", createGameResult.getGameID()))
        );
        Assertions.assertEquals(403, takenException.getStatusCode());
        Assertions.assertEquals("Error: already taken", takenException.getMessage());

        // --------------------------------------------------
        ResponseException badRequestException = Assertions.assertThrows(ResponseException.class,
                () -> this.gameService.joinGame(new JoinGameRequest(this.authToken, "BLACK", 909090))
        );
        Assertions.assertEquals(400, badRequestException.getStatusCode());
        Assertions.assertEquals("Error: bad request", badRequestException.getMessage());


    }

}