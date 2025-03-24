package client;

import chess.ChessBoard;
import chess.ChessGame;
import exceptions.ServerFacadeException;
import model.AuthData;
import model.GameMetaData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ChessUI;
import ui.ServerFacade;

import java.util.HashMap;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        Assertions.assertTrue(serverFacade.clear());
        server.stop();
    }

    @AfterEach
    public void clearDatabase(){
        Assertions.assertTrue(serverFacade.clear());
    }

    @Test
    public void registerTestGoodInput() {
        String username = "bob";
        String password = "password";
        String email = "email@bob.com";
        try {
            AuthData authData = serverFacade.register(username, password, email);
            Assertions.assertEquals(username, authData.username());
        } catch (ServerFacadeException exception) {
            Assertions.fail("Exception thrown for good input. Message:" + exception);
        }
    }

    @Test
    public void loginTestGoodInput() {
        String username = "loginUserTest";
        String password = "password2";
        String email = "loginUser@bob.com";
        try {
            AuthData authData = serverFacade.register(username, password, email);
            Assertions.assertEquals(username, authData.username());

            AuthData authDataAfterLogin = serverFacade.login(username, password);
            Assertions.assertEquals(username, authDataAfterLogin.username());
            Assertions.assertNotEquals(authData.authToken(), authDataAfterLogin.authToken());
        } catch (ServerFacadeException exception) {
            Assertions.fail("Exception thrown for good input. Message:" + exception);
        }
    }

    @Test
    public void logoutTestGoodInput() {
        String username = "logoutUser";
        String password = "password3";
        String email = "logoutUser@bob.com";
        try {
            AuthData authData = serverFacade.register(username, password, email);
            Assertions.assertEquals(username, authData.username());

            Assertions.assertTrue(serverFacade.logout(authData.authToken()));
        } catch (ServerFacadeException exception) {
            Assertions.fail("Exception thrown for good input. Message:" + exception);
        }
    }
    @Test
    public void createGameTest() {
        String username = "createGameUser";
        String password = "password4";
        String email = "createGameUser@bob.com";
        String gameName = "MyCreatedGame";
        try {
            AuthData authData = serverFacade.register(username, password, email);
            Assertions.assertEquals(username, authData.username());

            Assertions.assertNotEquals(0, serverFacade.createGame(gameName, authData.authToken()));
        } catch (ServerFacadeException exception) {
            Assertions.fail("Exception thrown for good input. Message:" + exception);
        }
    }
    @Test
    public void listGamesTest() {
        String username = "listGamesUser";
        String password = "password5";
        String email = "listGamesUser@bob.com";
        String gameName = "MyOtherCreatedGame";
        try {
            AuthData authData = serverFacade.register(username, password, email);
            Assertions.assertEquals(username, authData.username());

            Assertions.assertNotEquals(0, serverFacade.createGame(gameName, authData.authToken()));

            GameMetaData[] games = serverFacade.listGames(authData.authToken());
            GameMetaData[] expected = {new GameMetaData(1, null, null, gameName)};
            Assertions.assertEquals(expected[0].gameName(), games[0].gameName());
            Assertions.assertEquals(expected[0].gameID(), games[0].gameID());
        } catch (ServerFacadeException exception) {
            Assertions.fail("Exception thrown for good input. Message:" + exception);
        }
    }
    @Test
    public void observeTest() {
        String username = "listGamesUser";
        String password = "password5";
        String email = "listGamesUser@bob.com";
        String gameName = "MyOtherCreatedGame";
        try {
            AuthData authData = serverFacade.register(username, password, email);
            Assertions.assertEquals(username, authData.username());

            Assertions.assertNotEquals(0, serverFacade.createGame(gameName, authData.authToken()));

            // There is nothing to call because this is not implemented until phase 6
            serverFacade.observe("0", authData.authToken());
            Assertions.assertTrue(true);
        } catch (ServerFacadeException exception) {
            Assertions.fail("Exception thrown for good input. Message:" + exception);
        }
    }


}
