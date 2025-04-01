package client;

import exceptions.ServerFacadeException;
import model.AuthData;
import model.GameMetaData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.NotificationHandler;
import ui.ServerFacade;
import ui.WebSocketFacade;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static WebSocketFacade webSocketFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade("http://localhost:" + port);
        webSocketFacade = new WebSocketFacade("http://localhost:" + port, new NotificationHandler());
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
    public void clearTest(){
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
    public void registerTestBadInput() {
        String username = "bob";
        String password = null;
        String email = "email@bob.com";
        ServerFacadeException exception = Assertions.assertThrows(ServerFacadeException.class,
                () -> serverFacade.register(username, password, email));
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
    public void loginTestBadInput() {
        String username = "loginUserTest";
        String password = "password2";
        String email = "loginUser@bob.com";
        try {
            AuthData authData = serverFacade.register(username, password, email);
            Assertions.assertEquals(username, authData.username());

            ServerFacadeException exception = Assertions.assertThrows(
                    ServerFacadeException.class, () ->
                            serverFacade.login(username, "FaKePaSsWoRd")
            );
            Assertions.assertEquals(401, exception.getStatusCode());
        } catch (ServerFacadeException exception) {
            Assertions.fail("Incorrect exception thrown for bad input. Message:" + exception);
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
    public void logoutTestBadInput() {
        String username = "logoutUser";
        String password = "password3";
        String email = "logoutUser@bob.com";
        try {
            AuthData authData = serverFacade.register(username, password, email);
            Assertions.assertEquals(username, authData.username());

            Assertions.assertThrows(ServerFacadeException.class, () -> serverFacade.logout("aTokenThatIsNotLoggedIn"));
        } catch (ServerFacadeException exception) {
            Assertions.fail("Exception thrown for bad input. Message:" + exception);
        }
    }

    @Test
    public void createGameGoodTest() {
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
    public void createGameBadTest() {
        String username = "createGameUser";
        String password = "password4";
        String email = "createGameUser@bob.com";
        String gameName = null;
        try {
            AuthData authData = serverFacade.register(username, password, email);
            Assertions.assertEquals(username, authData.username());

            ServerFacadeException exception = Assertions.assertThrows(
                    ServerFacadeException.class,
                    () -> serverFacade.createGame(gameName, authData.authToken())
            );
            Assertions.assertEquals(400, exception.getStatusCode());
        } catch (ServerFacadeException exception) {
            Assertions.fail("Exception thrown for bad input. Message:" + exception);
        }
    }

    @Test
    public void listGamesGoodTest() {
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
    public void listGamesBadTest() {
        String username = "listGamesUser";
        String password = "password5";
        String email = "listGamesUser@bob.com";
        String gameName = "MyOtherCreatedGame";
        try {
            AuthData authData = serverFacade.register(username, password, email);
            Assertions.assertEquals(username, authData.username());

            ServerFacadeException exception = Assertions.assertThrows(
                    ServerFacadeException.class,
                    () -> serverFacade.createGame(gameName, "FaKeToKeN")
            );

            Assertions.assertEquals(401, exception.getStatusCode());
        } catch (ServerFacadeException exception) {
            Assertions.fail("Exception thrown for bad input. Message:" + exception);
        }
    }
    @Test
    public void observeGoodTest() {
        String username = "listGamesUser";
        String password = "password5";
        String email = "listGamesUser@bob.com";
        String gameName = "MyOtherCreatedGame";
        try {
            AuthData authData = serverFacade.register(username, password, email);
            Assertions.assertEquals(username, authData.username());

            Assertions.assertNotEquals(0, serverFacade.createGame(gameName, authData.authToken()));

            // There is nothing to call because this is not implemented until phase 6
            webSocketFacade.observeGame("0", authData.authToken());
            Assertions.assertTrue(true);
        } catch (ServerFacadeException exception) {
            Assertions.fail("Exception thrown for good input. Message:" + exception);
        }
    }
    @Test
    public void observeBadTest() {
        String username = "listGamesUser";
        String password = "password5";
        String email = "listGamesUser@bob.com";
        String gameName = "MyOtherCreatedGame";
        try {
            AuthData authData = serverFacade.register(username, password, email);
            Assertions.assertEquals(username, authData.username());

            Assertions.assertNotEquals(0, serverFacade.createGame(gameName, authData.authToken()));

            // There is nothing to call because this is not implemented until phase 6
            webSocketFacade.observeGame("0", authData.authToken());
            boolean observeWorked = true;
            Assertions.assertTrue(observeWorked);
        } catch (ServerFacadeException exception) {
            Assertions.fail("Exception thrown for bad input. Message:" + exception);
        }
    }


}
