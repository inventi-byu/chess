package ui;

import chess.ChessBoard;

import java.util.HashMap;
import java.util.Map;

public class ChessUI {

    private String menuBGColor;
    private String menuTextColor;

    private String emptyColor;
    private String bgColor;
    private String boardLetterColor;
    private String whiteTileColor;
    private String blackTileColor;
    private String whitePieceColor;
    private String blackPieceColor;

    public HashMap<String, String> defaultColors = new HashMap<>(9);

    {
        this.defaultColors.put("menuBGColor", EscapeSequences.SET_BG_COLOR_BLACK);
        this.defaultColors.put("menuTextColor", EscapeSequences.SET_TEXT_COLOR_WHITE);
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

        this.menuBGColor = colors.get("menuBGColor");
        this.menuTextColor = colors.get("menuTextColor");
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
        while command is not quit
            display main menu
            switch between help, quit, login, register
         */
        throw new RuntimeException("Not implemented.");
    }

    public void mainMenu(){
        System.out.printf("");
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
        StringBuilder sb = new StringBuilder();
        int startRow = 0;
        int numRows = 10;
        String emptySpace = "   "; // An empty tile, either three spaces or an m space
        String space = " "; // A space, either a space or an m space
        for (int i = 0; i < (numRows + startRow); i++){
            switch (i){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
            }
        }

        throw new RuntimeException("Not implemented.");
        //return sb.toString();

    }

}
