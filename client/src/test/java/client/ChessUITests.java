package client;


import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.ChessUI;
import ui.ServerFacade;

import java.util.HashMap;

public class ChessUITests {

    ChessBoard board;
    ServerFacade serverFacade;
    ChessClient client;
    ChessUI chessUI;

    @BeforeEach
    void setup(){
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        serverFacade = new ServerFacade("http://localhost:0");
        client = new ChessClient(serverFacade);
        chessUI = new ChessUI(serverFacade, client);
    }

    @Test
    public void printChessBoardWhiteTest(){
        chessUI.displayChessBoard(board, ChessGame.TeamColor.WHITE);
    }

    @Test
    public void printChessBoardBlackTest(){
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

    @Test
    public void runTest(){
        chessUI.run();
    }
}
