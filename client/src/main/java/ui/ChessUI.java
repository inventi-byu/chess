package ui;

import chess.ChessBoard;

import java.util.HashMap;
import java.util.Map;

public class ChessUI {

    private String emptyColor;
    private String bgColor;
    private String boardLetterColor;
    private String whiteTileColor;
    private String blackTileColor;
    private String whitePieceColor;
    private String blackPieceColor;

    public HashMap<String, String> defaultColors = new HashMap<>(7);
    {
        this.defaultColors.put("emptyColor", EscapeSequences.SET_BG_COLOR_BLACK);
        this.defaultColors.put("bgColor", EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        this.defaultColors.put("boardLetterColor", EscapeSequences.SET_TEXT_COLOR_BLACK);
        this.defaultColors.put("whiteTileColor", EscapeSequences.SET_BG_COLOR_WHITE);
        this.defaultColors.put("blackTileColor", EscapeSequences.SET_BG_COLOR_BLACK);
        this.defaultColors.put("whitePieceColor", EscapeSequences.SET_TEXT_COLOR_WHITE);
        this.defaultColors.put("blackPieceColor", EscapeSequences.SET_TEXT_COLOR_BLACK);
    }


    private ServerFacade serverFacade;

    public ChessUI (ServerFacade serverFacade, Map<String, String> colors){
        this.serverFacade = serverFacade;
        this.emptyColor = colors.get("emptyColor");
        this.bgColor = colors.get("bgColor");
        this.boardLetterColor = colors.get("boardLetterColor");
        this.whiteTileColor = colors.get("whiteTileColor");
        this.blackTileColor = colors.get("blackTileColor");
        this.whitePieceColor = colors.get("whitePieceColor");
        this.blackPieceColor = colors.get("blackPieceColor");
    }
    public void run(){
        /*
        while command is not quite
         */
        while command != "quit")
        throw new RuntimeException("Not implemented.");
    }
    public void help(){
        throw new RuntimeException("Not implemented.");
    }
    public void quit(){
        throw new RuntimeException("Not implemented.");
    }
    public void login(){
        throw new RuntimeException("Not implemented.");
    }
    public void register(){
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Returns the
     * @param board
     * @return
     */
    public void displayChessBoard(ChessBoard board){
        this.print(getBoardGraphic(board));
    }

    /**
     * Prints a given string to the console.
     * @param buf the String to print to the console (the buffer to print).
     */
    private void print(String buf) {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Creates a String that will be used to display the board (with colors, pieces, etc).
     * @param board the ChessBoard to print.
     * @return the String that represents the board image to display as console output.
     */
    private String getBoardGraphic(ChessBoard board){
        throw new RuntimeException("Not implemented.");
    }

}
