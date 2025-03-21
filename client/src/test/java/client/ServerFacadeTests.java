package client;

import chess.ChessBoard;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ChessUI;
import ui.ServerFacade;

import java.util.HashMap;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
