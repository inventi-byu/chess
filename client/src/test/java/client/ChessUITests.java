package client;


import chess.ChessBoard;
import chess.ChessGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.ChessUI;
import ui.ServerFacade;

import java.util.HashMap;

public class ChessUITests {

    ServerFacade serverFacade;
    ChessUI chessUI;

    @BeforeEach
    void setup(){
        serverFacade = new ServerFacade();
        chessUI = new ChessUI(serverFacade);
    }

    @Test
    public void printChessBoardWhiteTest(){
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        ServerFacade serverFacade = new ServerFacade();
        HashMap<String, String> colors = new HashMap<>();
        ChessUI chessUI = new ChessUI(serverFacade);

        chessUI.displayChessBoard(board, ChessGame.TeamColor.WHITE);
    }

    @Test
    public void printChessBoardBlackTest(){
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        ServerFacade serverFacade = new ServerFacade();
        ChessUI chessUI = new ChessUI(serverFacade);

        chessUI.displayChessBoard(board, ChessGame.TeamColor.BLACK);
    }

    @Test
    public void displayDisplayPreLoginMenu(){
        chessUI.displayPreLoginMenu();
    }

    @Test
    public void displayPromptTest(){
        chessUI.displayPrompt();
    }
}
