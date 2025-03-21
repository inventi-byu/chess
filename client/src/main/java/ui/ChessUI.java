package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
    private String whiteEmptyTextColor;
    private String blackEmptyTextColor;

    private ServerFacade serverFacade;

    public ChessUI (ServerFacade serverFacade, Map<String, String> colors){
        this.serverFacade = serverFacade;

        this.menuBGColor = EscapeSequences.SET_BG_COLOR_BLACK;
        this.menuTextColor = EscapeSequences.SET_TEXT_COLOR_WHITE;
        this.emptyColor = EscapeSequences.SET_BG_COLOR_BLACK;
        this.bgColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        this.boardLetterColor = EscapeSequences.SET_TEXT_COLOR_BLACK;
        this.whiteTileColor = EscapeSequences.SET_BG_COLOR_WHITE;
        this.blackTileColor = EscapeSequences.SET_BG_COLOR_BLACK;
        this.whitePieceColor = EscapeSequences.SET_TEXT_COLOR_RED;
        this.blackPieceColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
        this.whiteEmptyTextColor = EscapeSequences.SET_TEXT_COLOR_WHITE;
        this.blackEmptyTextColor = EscapeSequences.SET_TEXT_COLOR_BLACK;
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
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        String emptySpace = "   "; // An empty tile, either three spaces or an m space
        String space = " "; // A space, either a space or an m space

        for (int i = (numRows + startRow); i > -1 ; i--){
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
                    for (int j = 1; j < 9; j++){
                        ChessPosition curPos = new ChessPosition(i, j);
                        ChessPiece curPiece = board.getPiece(curPos);
                        String pieceToDraw = "";
                        String curTileColor = this.getTileColor(curPos);

                        if (curTileColor.equals("WHITE")){
                            sb.append(whiteTileColor);
                        } else {
                            sb.append(blackTileColor);
                        }

                        if (curPiece == null){
                            if(curTileColor.equals("WHTIE")){
                                sb.append(this.whiteEmptyTextColor);
                                pieceToDraw = space;
                            } else {
                                sb.append(this.blackEmptyTextColor);
                                pieceToDraw = space;
                            }
                        } else if(curPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                            sb.append(whitePieceColor);
                            pieceToDraw = curPiece.draw();
                        } else {
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
                        String curTileColor = this.getTileColor(curPos);

                        if (curTileColor.equals("WHITE")){
                            sb.append(whiteTileColor);
                        } else {
                            sb.append(blackTileColor);
                        }

                        if (curPiece == null){
                            if(curTileColor.equals("WHTIE")){
                                sb.append(this.whiteEmptyTextColor);
                                pieceToDraw = space;
                            } else {
                                sb.append(this.blackEmptyTextColor);
                                pieceToDraw = space;
                            }
                        } else if(curPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                            sb.append(whitePieceColor);
                            pieceToDraw = curPiece.draw();
                        } else {
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

    private String getTileColor(ChessPosition position){
        int row = position.getRow();
        int col = position.getColumn();

        boolean white = (row % 2 != col % 2);

        return white ? "WHITE" : "BLACK";
    }

}
