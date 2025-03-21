package client;


import chess.ChessBoard;
import chess.ChessGame;
import org.junit.jupiter.api.Test;
import ui.ChessUI;
import ui.ServerFacade;

import java.util.HashMap;

public class ChessUITests {

    @Test
    public void printChessBoardWhiteTest(){
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        ServerFacade serverFacade = new ServerFacade();
        HashMap<String, String> colors = new HashMap<>();
        ChessUI chessUI = new ChessUI(serverFacade, colors);

        chessUI.displayChessBoard(board, ChessGame.TeamColor.WHITE);
    }

    @Test
    public void printChessBoardBlackTest(){
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        ServerFacade serverFacade = new ServerFacade();
        HashMap<String, String> colors = new HashMap<>();
        ChessUI chessUI = new ChessUI(serverFacade, colors);

        chessUI.displayChessBoard(board, ChessGame.TeamColor.BLACK);
    }
}
