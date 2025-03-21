package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

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

        try {
            colors.get("menuBGColor");
        } catch (Exception exception) {
            colors = this.defaultColors;
        }

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
    public void displayChessBoard(ChessBoard board, ChessGame.TeamColor perspective){
        this.print(getBoardGraphic(board, perspective));
    }

    /**
     * Prints a given string to the console.
     * @param buf the String to print to the console (the buffer to print).
     */
    private void print(String buf) {
        System.out.print(buf);
    }

    /**
     * Creates a String that will be used to display the board (with colors, pieces, etc).
     * @param board the ChessBoard to print.
     * @return the String that represents the board image to display as console output.
     */
    private String getBoardGraphic(ChessBoard board, ChessGame.TeamColor perspective){
        if (perspective == ChessGame.TeamColor.WHITE){
            return this.drawWhiteBoardGraphic(board);
        } else {
            return this.drawBlackBoardGraphic(board);
        }
    }

    private String drawWhiteBoardGraphic(ChessBoard board){
        StringBuilder sb = new StringBuilder();
        int startRow = 0;
        int numRows = 10;
        String[] boardLabels = {"a", "b", "c", "d", "e", "f", "g", "h"};

        String emptySpace = "   "; // An empty tile, either three spaces or an m space
        String space = " "; // A space, either a space or an m space

        // if perspective is black
        //this.drawBlackBoardGraphic(board);
        // this is for black
        for (int i = (numRows + startRow); i > 0 ; i--){
            switch (i){
                // Both 0 and 9 have the same string
                case 0:
                case 9:
                    sb.append(this.bgColor);
                    sb.append(this.boardLetterColor);
                    sb.append(emptySpace);
                    for (String label : boardLabels){
                        sb.append(
                                this.sandwichString(label, space)
                        );
                    }
                    sb.append(emptySpace);
                    sb.append(this.emptyColor);
                    sb.append("\n");
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    sb.append(this.bgColor);
                    sb.append(this.boardLetterColor);
                    sb.append(
                            this.sandwichString(Integer.toString(i), space)
                    );
                    for (int j = 8; j > 0; j--){
                        ChessPosition curPos = new ChessPosition(i, j);
                        ChessPiece curPiece = board.getPiece(curPos);
                        String pieceToDraw = "";

                        if (curPiece == null){
                            if(this.getEmptyTileColor(curPos).equals("WHTIE")){
                                sb.append(whiteTileColor);
                                sb.append(whitePieceColor);
                                pieceToDraw = emptySpace;
                            } else {
                                sb.append(blackTileColor);
                                sb.append(blackPieceColor);
                                pieceToDraw = emptySpace;
                            }
                        } else if(curPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                            sb.append(whiteTileColor);
                            sb.append(whitePieceColor);
                            pieceToDraw = curPiece.draw();
                        } else {
                            sb.append(blackTileColor);
                            sb.append(blackPieceColor);
                            pieceToDraw = curPiece.draw();
                        }

                        sb.append(
                                this.sandwichString(pieceToDraw, space)
                        );
                    }

                    sb.append(this.bgColor);
                    sb.append(this.boardLetterColor);
                    sb.append(
                            this.sandwichString(Integer.toString(i), space)
                    );
                    sb.append(this.emptyColor);
                    sb.append("\n");
                    break;
            }
        }
        return sb.toString();
    }

    private String drawBlackBoardGraphic(ChessBoard board){
        StringBuilder sb = new StringBuilder();
        int startRow = 0;
        int numRows = 10;
        String[] boardLabelsForward = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] boardLabels = {"h", "g", "f", "e", "d", "c", "b", "a"};

        String emptySpace = "   "; // An empty tile, either three spaces or an m space
        String space = " "; // A space, either a space or an m space

        // if perspective is black
        //this.drawBlackBoardGraphic(board);
        // this is for black
        for (int i = 0; i < (numRows + startRow); i++){
            switch (i){
                // Both 0 and 9 have the same string
                case 0:
                case 9:
                    sb.append(this.bgColor);
                    sb.append(this.boardLetterColor);
                    sb.append(emptySpace);
                    for (String label : boardLabels){
                        sb.append(
                                this.sandwichString(label, space)
                        );
                    }
                    sb.append(emptySpace);
                    sb.append(this.emptyColor);
                    sb.append("\n");
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    sb.append(this.bgColor);
                    sb.append(this.boardLetterColor);
                    sb.append(
                            this.sandwichString(Integer.toString(i), space)
                    );
                    for (int j = 8; j > 0; j--){
                        ChessPosition curPos = new ChessPosition(i, j);
                        ChessPiece curPiece = board.getPiece(curPos);
                        String pieceToDraw = "";

                        if (curPiece == null){
                            if(this.getEmptyTileColor(curPos).equals("WHTIE")){
                                sb.append(whiteTileColor);
                                sb.append(whitePieceColor);
                                pieceToDraw = emptySpace;
                            } else {
                                sb.append(blackTileColor);
                                sb.append(blackPieceColor);
                                pieceToDraw = emptySpace;
                            }
                        } else if(curPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                            sb.append(whiteTileColor);
                            sb.append(whitePieceColor);
                            pieceToDraw = curPiece.draw();
                        } else {
                            sb.append(blackTileColor);
                            sb.append(blackPieceColor);
                            pieceToDraw = curPiece.draw();
                        }

                        sb.append(
                                this.sandwichString(pieceToDraw, space)
                        );
                    }

                    sb.append(this.bgColor);
                    sb.append(this.boardLetterColor);
                    sb.append(
                            this.sandwichString(Integer.toString(i), space)
                    );
                    sb.append(this.emptyColor);
                    sb.append("\n");
                    break;
            }
        }

        return sb.toString();
    }

    private String sandwichString(String str, String space){
        return space + str + space;
    }

    private String getEmptyTileColor(ChessPosition position){
        int row = position.getRow();
        int col = position.getColumn();

        boolean white = (row % 2 != col % 2);

        return white ? "WHITE" : "BLACK";
    }

}
