package client;


import chess.ChessBoard;
import chess.ChessGame;
import exceptions.WebSocketFacadeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.ChessUI;
import ui.NotificationHandler;
import ui.ServerFacade;
import ui.WebSocketFacade;

import java.util.HashMap;

public class ChessUITests {

    ChessBoard board;
    ServerFacade serverFacade;
    WebSocketFacade webSocketFacade;
    ChessClient client;
    ChessUI chessUI;

    @BeforeEach
    void setup(){
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.serverFacade = new ServerFacade("http://localhost:0");
        this.client = new ChessClient(this.serverFacade, null);
        this.chessUI = new ChessUI(this.client);
    }

    @Test
    public void printChessBoardWhiteTest(){
        this.chessUI.displayChessBoard(this.board, ChessGame.TeamColor.WHITE, null);
    }

    @Test
    public void printChessBoardBlackTest(){
        this.chessUI.displayChessBoard(this.board, ChessGame.TeamColor.BLACK, null);
    }

    @Test
    public void displayDisplayPreLoginMenu(){
        this.chessUI.displayPreLoginMenu();
    }

    @Test
    public void displayPromptTest(){
        this.chessUI.displayPrompt();
        System.out.println(); // To make sure that the rest of the tests print correctly below
    }

    @Test
    public void runTest(){
        // This is here for testing the REPL, but it runs forever in test mode, so
        // all tests will fail if I run it.
        //chessUI.run();
        Assertions.assertTrue(true);
    }

}
