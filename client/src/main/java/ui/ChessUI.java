package ui;

import chess.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import client.ChessClient;
import model.GameMetaData;
public class ChessUI {

    public ChessClient client;

    private static final String SPACE = " ";
    private static final String EMPTY_SPACE = "   ";
    private boolean resignState;

    public String menuBGColor;
    private String menuTextColorPreLogin;
    private String menuTextColorLoggedIn;
    private String menuTextColorPostLogin;
    public String menuTextColorGame;
    public String menuTextColorObserve;

    private String emptyColor;
    private String bgColor;
    private String boardLetterColor;
    private String whiteTileColor;
    private String blackTileColor;
    private String whitePieceColor;
    private String blackPieceColor;
    private String whiteEmptyTextColor;
    private String blackEmptyTextColor;

    private String whiteTileHighlightColor;
    private String blackTileHighlightColor;
    private String startTileHighlightColor;

    private Random rand;

    public ChessUI(ChessClient client) {
        this.client = client;

        this.resignState = false;
        this.menuBGColor = EscapeSequences.SET_BG_COLOR_BLACK;
        this.menuTextColorPreLogin = EscapeSequences.SET_TEXT_COLOR_WHITE;
        this.menuTextColorLoggedIn = EscapeSequences.SET_TEXT_COLOR_GREEN;
        this.menuTextColorPostLogin = EscapeSequences.SET_TEXT_COLOR_MAGENTA;
        this.menuTextColorGame = EscapeSequences.SET_TEXT_COLOR_BLUE;
        this.menuTextColorObserve = EscapeSequences.SET_TEXT_COLOR_YELLOW;

        this.emptyColor = EscapeSequences.SET_BG_COLOR_BLACK;
        this.bgColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        this.boardLetterColor = EscapeSequences.SET_TEXT_COLOR_BLACK;
        this.whiteTileColor = EscapeSequences.SET_BG_COLOR_WHITE;
        this.blackTileColor = EscapeSequences.SET_BG_COLOR_BLACK;
        this.whitePieceColor = EscapeSequences.SET_TEXT_COLOR_RED;
        this.blackPieceColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
        this.whiteEmptyTextColor = EscapeSequences.SET_TEXT_COLOR_WHITE;
        this.blackEmptyTextColor = EscapeSequences.SET_TEXT_COLOR_BLACK;

        this.whiteTileHighlightColor = EscapeSequences.SET_BG_COLOR_GREEN;
        this.blackTileHighlightColor = EscapeSequences.SET_BG_COLOR_DARK_GREEN;
        this.startTileHighlightColor = EscapeSequences.SET_BG_COLOR_YELLOW;

        this.rand = new Random(1221830);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String result = "";
        this.displayPreLoginMenu();
        while (!result.equals("quit")){
            this.displayPrompt();
            String line = "";
            try {
                line = scanner.nextLine();
            } catch (Exception exception) {this.println("Sorry that\'s an invalid command!");}
            try{
                if (line.equals("y")){
                    this.client.evalResign();
                    resignState = false;
                } else if (line.equals("n")){
                    continue;
                }
                else{
                    result = this.client.evalLine(line);
                    this.evalResult(result);
                }

            } catch (Exception ex){this.print(ex.toString());}
        }
    }

    public void displayHelpGame(){
        this.print(
                this.menuBGColor + this.menuTextColorGame +
                """
                redraw - Redraws the chessboard
                highlight <LOCATION> - Highlights legal moves for the piece at LOCATION.
                                       Location should be in chess notation (i.e. a1 or A1).
                move <START> <END> - Move chess piece at START to the END position.
                                     START and END should be in lowercase chess notation (i.e. a1).
                leave - Leave the game without forfeiting.
                resign - Forfeit and end the game without leaving.
                help - Get help with possible commands
                """);
    }

    public void displayHelpObserve(){
        this.print(
                this.menuBGColor + this.menuTextColorObserve +
                """
                redraw - Redraws the chessboard
                highlight <LOCATION> - Highlights legal moves for the piece at LOCATION.
                                       Location should be in chess notation (i.e. a1 or A1).
                leave - Leave the game you are observing.
                help - Get help with possible commands
                """);
    }

    public void displayPostLoginMenu(){
        this.print(
                this.menuBGColor + this.menuTextColorPostLogin +
                """
                
                Welcome to Chess! To start, choose an option below:
                
                """);
    }

    public void displayPreLoginMenu() {
        this.print(
            this.menuBGColor + this.menuTextColorPreLogin +
            """
            ====== Welcome to Chess ======
            Type "help" to get started!
            """);
    }

    public void displayPrompt(){
        if (this.client.getLoginStatus().equals(ChessClient.STATUS_LOGGED_IN)){
            this.print(
                    this.menuBGColor + this.menuTextColorPreLogin +
                            "[ "
                            + this.menuTextColorLoggedIn +
                            this.client.getLoginStatus() +
                            this.menuTextColorPreLogin +
                            " ]"
                            + " >>> "
            );
        } else {
            this.print(
                    this.menuBGColor + this.menuTextColorPreLogin + "[ " + this.client.getLoginStatus() + " ]" + " >>> "
            );
        }
    }

    public void displayHelpPreLogin() {
        this.print(
                this.menuTextColorPreLogin +
        """
        register <USERNAME> <PASSWORD> <EMAIL> - Create an account
        login <USERNAME> <PASSWORD> - Login to play chess
        quit - Quit playing chess
        help - Get help with possible commands
        """
        );
    }

    public void displayHelpPostLogin() {
        this.print(
                this.menuTextColorPostLogin +
                """
                create <NAME> - Create a game
                list - List all games.
                join <ID> [WHITE|BLACK] - join a game
                observe <ID> - Observe a game
                logout - Logout of chess
                quit - Quit playing chess
                help - Get help with possible commands
                """
        );
    }

    public void displayListOfGames(){
        GameMetaData[] currentGames = this.client.getCurrentGames();

        StringBuilder sb = new StringBuilder();
        sb.append("======= BEGIN LIST OF ALL GAMES =======");
        sb.append("\n");
        int i = 1;
        for (GameMetaData game: currentGames){
            sb.append(String.format("""
                    (%d)
                    GAME: "%s"
                    WHITE PLAYER: %s
                    BLACK PLAYER: %s
                    """,
                    i, game.gameName(), game.whiteUsername(), game.blackUsername()
                    )
            );
            sb.append("\n");
            i++;
        }
        sb.append("======== END LIST OF ALL GAMES ========");
        sb.append("\n");
        this.print(sb.toString());
    }

    /**
     * Displays the given ChessBoard.
     *
     * @param board the ChessBoard to display.
     * @param perspective the TeamColor from whose perspective to draw the board (that color will be on the bottom).
     */
    public void displayChessBoard(ChessBoard board, ChessGame.TeamColor perspective, ChessMove[] legalMoves) {
        this.print(getBoardGraphic(
                board,
                perspective,
                this.getTilesToHighlight(legalMoves)
        ));
    }

    /**
     * Prints a given string to the console.
     *
     * @param buf the String to print to the console (the buffer to print).
     */
    private void print(String buf) {
        System.out.print(buf);
    }

    private void println(String buf) {
        System.out.println(buf);
    }

    /**
     * Creates a String that will be used to display the board (with colors, pieces, etc.).
     *
     * @param board the ChessBoard to print.
     * @return the String that represents the board image to display as console output.
     */
    private String getBoardGraphic(ChessBoard board, ChessGame.TeamColor perspective, ChessPosition[] tilesToHighlight) {
        if (perspective == ChessGame.TeamColor.WHITE) {
            return this.drawWhiteBoardGraphic(board, tilesToHighlight);
        } else {
            return this.drawBlackBoardGraphic(board, tilesToHighlight);
        }
    }

    private String drawWhiteBoardGraphic(ChessBoard board, ChessPosition[] tilesToHighlight) {
        StringBuilder sb = new StringBuilder();
        int startRow = 0;
        int numRows = 10;
        String[] boardLabels = {"a", "b", "c", "d", "e", "f", "g", "h"};

        ChessPosition startTile = null;
        if (tilesToHighlight != null){
            startTile = tilesToHighlight[0];
        }

        // It needs to be 9 here
        for (int i = (numRows + startRow)-1; i > -1; i--) {
            sb = drawChessBoard(sb, board, i, boardLabels, ChessGame.TeamColor.WHITE, this.getRowTilesToHighlight(tilesToHighlight, i), startTile);
        }
        return sb.toString();
    }

    private String drawBlackBoardGraphic(ChessBoard board, ChessPosition[] tilesToHighlight) {
        StringBuilder sb = new StringBuilder();
        int startRow = 0;
        int numRows = 10;
        String[] boardLabels = {"h", "g", "f", "e", "d", "c", "b", "a"};

        ChessPosition startTile = null;
        if (tilesToHighlight != null){
            startTile = tilesToHighlight[0];
        }
        for (int i = 0; i < (numRows + startRow); i++) {
            sb = drawChessBoard(sb, board, i, boardLabels, ChessGame.TeamColor.BLACK, this.getRowTilesToHighlight(tilesToHighlight, i), startTile);
        }

        return sb.toString();
    }

    private StringBuilder drawChessBoard(StringBuilder sb, ChessBoard board, int i, String[] boardLabels,
                                         ChessGame.TeamColor perspective, ChessPosition[] tilesToHighlight, ChessPosition startTile) {
        switch (i) {
            case 0, 9:
                sb.append(this.bgColor);
                sb.append(this.boardLetterColor);
                sb.append(ChessUI.EMPTY_SPACE);
                for (String label : boardLabels) {
                    sb.append(this.sandwichString(label));
                }
                sb.append(ChessUI.EMPTY_SPACE);
                sb.append(this.emptyColor);
                sb.append("\n");
                break;
            case 1, 2, 3, 4, 5, 6, 7, 8:
                sb.append(this.bgColor);
                sb.append(this.boardLetterColor);
                sb.append(
                        this.sandwichString(Integer.toString(i))
                );

                if (perspective == ChessGame.TeamColor.WHITE) {
                    for (int j = 1; j < 9; j++) {
                        sb = this.drawTile(sb, board, i, j, this.findOutIfDoHighlight(tilesToHighlight, i, j), startTile);
                    }
                } else {
                    for (int j = 8; j > 0; j--) {
                        sb = this.drawTile(sb, board, i, j, this.findOutIfDoHighlight(tilesToHighlight, i, j), startTile);
                    }
                }
                sb.append(this.bgColor);
                sb.append(this.boardLetterColor);
                sb.append(
                        this.sandwichString(Integer.toString(i))
                );
                sb.append(this.emptyColor);
                sb.append("\n");
                break;
        }
        return sb;
    }

    private boolean findOutIfDoHighlight(ChessPosition[] tilesToHighlight, int i, int j){
        if(tilesToHighlight != null) {
            for (ChessPosition tile : tilesToHighlight) {
                if (tile.getRow() == i && tile.getColumn() == j) {
                    return true;
                }
            }
        }
        return false;
    }

    private StringBuilder drawTile(StringBuilder sb, ChessBoard board, int row, int col, boolean doHighlight, ChessPosition startHighlightTile) {
        ChessPosition curPos = new ChessPosition(row, col);
        ChessPiece curPiece = board.getPiece(curPos);
        String pieceToDraw = "";
        String curTileColor = this.getTileColor(curPos, doHighlight, startHighlightTile);

        switch (curTileColor) {
            case "WHITE" -> sb.append(this.whiteTileColor);
            case "BLACK" -> sb.append(this.blackTileColor);
            case "WHITE_HIGHLIGHT" -> sb.append(this.whiteTileHighlightColor);
            case "BLACK_HIGHLIGHT" -> sb.append(this.blackTileHighlightColor);
            case "START_HIGHLIGHT" -> sb.append(this.startTileHighlightColor);
        }
        if (curPiece == null) {
            if (curTileColor.equals("WHITE")) {
                sb.append(this.whiteEmptyTextColor);
                pieceToDraw = ChessUI.SPACE;
            } else {
                sb.append(this.blackEmptyTextColor);
                pieceToDraw = ChessUI.SPACE;
            }
        } else if (curPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            sb.append(whitePieceColor);
            pieceToDraw = curPiece.draw();
        } else {
            sb.append(blackPieceColor);
            pieceToDraw = curPiece.draw();
        }
        sb.append(this.sandwichString(pieceToDraw));
        return sb;
    }

    private String sandwichString(String str){
        return ChessUI.SPACE + str + ChessUI.SPACE;
    }

    private String getTileColor(ChessPosition position, boolean doHighlight, ChessPosition startTile){
        int row = position.getRow();
        int col = position.getColumn();

        boolean white = (row % 2 != col % 2);

        if (startTile != null){
            if(startTile.getRow() == row && startTile.getColumn() == col){
                return "START_HIGHLIGHT";
            }
        }
        if (white) {
            if (doHighlight) {
                return "WHITE_HIGHLIGHT";
            } else {
                return "WHITE";
            }
        } else {
            if (doHighlight) {
                return "BLACK_HIGHLIGHT";
            } else {
                return "BLACK";
            }
        }
    }

    private ChessPosition[] getTilesToHighlight(ChessMove[] legalMoves){
        if (legalMoves == null || legalMoves.length == 0){
            return null;
        }
        ArrayList<ChessPosition> tilesToHighlight = new ArrayList<>();

        // Add the starting position only once (not in the for-each loop)
        ChessPosition startPosition = null;
        try {
            startPosition = legalMoves[0].getStartPosition();
            tilesToHighlight.add(startPosition);
        } catch (IndexOutOfBoundsException exception) {
            // The array is empty if there is no value at index 0
            return null;
        }
        for (ChessMove move : legalMoves){
            tilesToHighlight.add(move.getEndPosition());
        }
        return tilesToHighlight.toArray(new ChessPosition[0]);
    }

    private ChessPosition[] getRowTilesToHighlight(ChessPosition[] tilesToHighlight, int row){
        if(tilesToHighlight == null){
            return null;
        }
        ArrayList<ChessPosition> rowTilesToHighlightAsList = new ArrayList<>();
        // Only worry about the tiles to highlight for this row
        if (tilesToHighlight != null){
            for (ChessPosition position : tilesToHighlight){
                if (position.getRow() == row){
                    rowTilesToHighlightAsList.add(position);
                }
            }
        }
        // Return the list as a plain array
        return rowTilesToHighlightAsList.toArray(new ChessPosition[0]);
    }
    public void displayln(String string){this.println(string);}
    public void display(String string){this.print(string);}

    private void evalResult(String result){
        switch (result) {
            case "helpPreLogin" -> this.displayHelpPreLogin();
            case "helpPostLogin" -> this.displayHelpPostLogin();
            case "join", "helpGame" -> this.displayHelpGame();
            case "helpObserve", "observe" -> this.displayHelpObserve();
            case "quit" -> {}
            case "register" -> {
                this.println("Successfully registered. You are now logged in.");
                this.displayPostLoginMenu();
                this.displayHelpPostLogin();
            }
            case "login" -> {
                this.println("Successfully logged in.");
                this.displayPostLoginMenu();
                this.displayHelpPostLogin();
            }
            case "logout" -> {
                this.println("You successfully logged out.");
                this.print("\n");
                this.displayPreLoginMenu();
            }
            case "create" -> this.println("Successfully created game.");
            case "list" -> this.displayListOfGames();
            case "redraw" -> {
                if (this.client.getMenuState().equals(ChessClient.STATE_GAME)) {
                    this.displayChessBoard(client.getBoard(), client.getTeamColor(), null);
                } else if (this.client.getMenuState().equals(ChessClient.STATE_OBSERVE)) {
                    this.displayChessBoard(client.getObservingBoard(), ChessGame.TeamColor.WHITE, null);
                }
            }
            case "leave" -> {
                this.displayPostLoginMenu();
                this.displayHelpPostLogin();
            }
            case "move" -> {}
            case "resign1" -> {this.println("Are you sure want to resign? (y/n)"); this.resignState = true;}
            case "resign" -> this.println("Wow...just...wow.");
            case "highlight" -> {
                if (client.getMenuState().equals(ChessClient.STATE_GAME)) {
                    this.displayChessBoard(client.getBoard(), client.getTeamColor(), client.getLegalMoves(client.getHighlightPosition()));
                } else if (client.getMenuState().equals(ChessClient.STATE_OBSERVE)) {
                    this.displayChessBoard(client.getObservingBoard(), ChessGame.TeamColor.WHITE,
                            client.getLegalMoves(client.getHighlightPosition())
                    );
                }
            }
            default -> {
                // If there was an error, print the user-friendly message.
                if (client.getMenuState().equals(ChessClient.STATE_POSTLOGIN)) {
                    this.println((this.menuTextColorPostLogin + result));
                } else if ((client.getMenuState().equals(ChessClient.STATE_GAME)) ||
                        client.getMenuState().equals(ChessClient.STATE_OBSERVE)) {
                    this.println((this.menuTextColorGame + result));
                } else {
                    this.println((this.menuTextColorPreLogin + result));
                }
            }

        }
    }

}