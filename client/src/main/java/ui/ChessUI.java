package ui;

import chess.*;

import java.util.Random;
import java.util.Scanner;

import client.ChessClient;
import model.GameMetaData;

public class ChessUI {

    private ChessClient client;

    private static final String SPACE = " ";
    private static final String EMPTY_SPACE = " ";

    private String menuBGColor;
    private String menuTextColor;
    private String menuTextColorLoggedIn;
    private String menuTextColorPostLogin;
    private String menuTextColorGame;

    private String emptyColor;
    private String bgColor;
    private String boardLetterColor;
    private String whiteTileColor;
    private String blackTileColor;
    private String whitePieceColor;
    private String blackPieceColor;
    private String whiteEmptyTextColor;
    private String blackEmptyTextColor;

    private Random rand;

    private ServerFacade serverFacade;

    public ChessUI(ServerFacade serverFacade, ChessClient client) {
        this.serverFacade = serverFacade;
        this.client = client;

        this.menuBGColor = EscapeSequences.SET_BG_COLOR_BLACK;
        this.menuTextColor = EscapeSequences.SET_TEXT_COLOR_WHITE;
        this.menuTextColorLoggedIn = EscapeSequences.SET_TEXT_COLOR_GREEN;
        this.menuTextColorPostLogin = EscapeSequences.SET_TEXT_COLOR_MAGENTA;
        this.menuTextColorGame = EscapeSequences.SET_TEXT_COLOR_BLUE;

        this.emptyColor = EscapeSequences.SET_BG_COLOR_BLACK;
        this.bgColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        this.boardLetterColor = EscapeSequences.SET_TEXT_COLOR_BLACK;
        this.whiteTileColor = EscapeSequences.SET_BG_COLOR_WHITE;
        this.blackTileColor = EscapeSequences.SET_BG_COLOR_BLACK;
        this.whitePieceColor = EscapeSequences.SET_TEXT_COLOR_RED;
        this.blackPieceColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
        this.whiteEmptyTextColor = EscapeSequences.SET_TEXT_COLOR_WHITE;
        this.blackEmptyTextColor = EscapeSequences.SET_TEXT_COLOR_BLACK;

        this.rand = new Random(1221830);
    }

    public void run() {
        /*
        while command is not quit
            display main menu
            switch between help, quit, login, register
         */

        Scanner scanner = new Scanner(System.in);
        String result = "";
        this.displayPreLoginMenu();
        while (!result.equals("quit")){
            this.displayPrompt();
            String line = "";
            try {
                line = scanner.nextLine();
            } catch (Exception exception) {
                this.println("Sorry that\'s an invalid command!");
            }
            try{
                result = this.client.evalLine(line);

                switch (result) {
                    case "helpPreLogin":
                        this.displayHelpPreLogin();
                        break;

                    case "helpPostLogin":
                        this.displayHelpPostLogin();
                        break;

                    case "helpGame":
                        this.displayHelpGame();
                        break;

                    case "quit":
                        break;

                    case "register":
                        this.println("Successfully registered. You are now logged in.");
                        this.displayPostLoginMenu();
                        this.displayHelpPostLogin();
                        break;

                    case "login":
                        this.println("Successfully logged in.");
                        this.displayPostLoginMenu();
                        this.displayHelpPostLogin();
                        break;

                    case "logout":
                        this.println("You successfully logged out.");
                        this.print("\n");
                        this.displayPreLoginMenu();
                        break;

                    case "create":
                        this.println("Successfully created game.");
                        break;

                    case "list":
                        this.displayListOfGames();
                        break;

                    case "join":
                        this.displayGameMenu(client.getBoard(), client.getTeamColor());
                        this.displayHelpGame();
                        break;

                    case "observe":
                        this.displayGameMenu(client.getObservingBoard(), ChessGame.TeamColor.WHITE);
                        break;

                    case "redraw":
                        this.displayChessBoard(client.getBoard(), client.getTeamColor(), null);
                        break;

                    case "leave":
                        this.displayPostLoginMenu();
                        this.displayHelpPostLogin();
                        break;

                    case "move":
                        // Just reprint the chessboard with a fun message
                        this.displayChessBoard(client.getBoard(), client.getTeamColor(), null);

                        switch (this.rand.nextInt(5)){
                            case 0 -> this.println("You moved! One more move towards chess victory!");
                            case 1 -> this.println("You moved! Wow! That was quite a move!");
                            case 2 -> this.println("You moved! Only an expert would have known to make that move!");
                            case 3 -> this.println("You moved! And so the tables turn...");
                            case 4 -> this.println("You moved! And the battle continues...");
                        }
                        break;
                    case "resign":
                        switch (this.rand.nextInt(5)){
                            case 0 -> this.println("You resigned! Wow...just...wow.");
                            case 1 -> this.println("You resigned! But you were so close :(");
                            case 2 -> this.println("You resigned! Don't give up next time!");
                            case 3 -> this.println("You resigned! It\'s all in the mind...");
                            case 4 -> this.println("You resigned! Well that was unexpected.");
                        }
                        break;
                        
                    case "highlight":
                        this.displayChessBoard(client.getBoard(), client.getTeamColor(), client.getLegalMoves(client.getHighlightPosition()));
                        break;

                    default:
                        // If there was an error, print the user-friendly message.
                        if(client.getMenuState().equals(ChessClient.STATE_POSTLOGIN)){
                            this.println( (this.menuTextColorPostLogin + result) );
                        } else if ((client.getMenuState().equals(ChessClient.STATE_GAME))){
                            this.println( (this.menuTextColorGame + result) );
                        } else {
                            this.println( (this.menuTextColor + result) );
                        }

                }

            } catch (Exception ex){
                this.print(ex.toString());
            }
        }
    }

    public void displayHelpGame(){
        this.print(
                """
                redraw - Redraws the chessboard
                highlight <LOCATION> - Highlights legal moves for the piece at LOCATION.
                                       Location should be in lowercase chess notation (i.e. a1)
                move <START> <END> - (PLAYER ONLY) Move chess piece at START to the END position.
                                     START and END should be in lowercase chess
                                     notation (i.e. a1).
                leave - leave the game (without forfeiting, if playing).
                resign - (PLAYER ONLY) Forfeit and end the game without leaving.
                help - Get help with possible commands
                """
        );
    }

    public void displayPostLoginMenu(){
        this.print(
                this.menuTextColorPostLogin + this.menuTextColorPostLogin +
                """
                
                Welcome to Chess! To start, choose an option below:
                
                """
        );
    }

    public void displayPreLoginMenu() {
        this.print(
            this.menuBGColor + this.menuTextColor +
            """
            ====== Welcome to Chess ======
            Type "help" to get started!
            """
        );
    }

    public void displayGameMenu(ChessBoard board, ChessGame.TeamColor perspective) {
        this.displayChessBoard(board, perspective,null);
    }

    public void displayPrompt(){
        if (this.client.getLoginStatus().equals(ChessClient.STATUS_LOGGED_IN)){
            this.print(
                    this.menuBGColor + this.menuTextColor +
                            "[ "
                            + this.menuTextColorLoggedIn +
                            this.client.getLoginStatus() +
                            this.menuTextColor +
                            " ]"
                            + " >>> "
            );
        } else {
            this.print(
                    this.menuBGColor + this.menuTextColor + "[ " + this.client.getLoginStatus() + " ]" + " >>> "
            );
        }
    }

    public void displayHelpPreLogin() {
        this.print(
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
        this.print(getBoardGraphic(board, perspective));
    }

    /**
     * Prints a given string to the console.
     *
     * @param buf the String to print to the console (the buffer to print).
     */
    private void print(String buf) {
//        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
//        out.print(buf);
        System.out.print(buf);
    }

    private void println(String buf) {
        System.out.println(buf);
    }

    /**
     * Creates a String that will be used to display the board (with colors, pieces, etc).
     *
     * @param board the ChessBoard to print.
     * @return the String that represents the board image to display as console output.
     */
    private String getBoardGraphic(ChessBoard board, ChessGame.TeamColor perspective) {
        if (perspective == ChessGame.TeamColor.WHITE) {
            return this.drawWhiteBoardGraphic(board);
        } else {
            return this.drawBlackBoardGraphic(board);
        }
    }

    private String drawWhiteBoardGraphic(ChessBoard board) {
        StringBuilder sb = new StringBuilder();
        int startRow = 0;
        int numRows = 10;
        String[] boardLabels = {"a", "b", "c", "d", "e", "f", "g", "h"};

        for (int i = (numRows + startRow); i > -1; i--) {
            sb = drawChessBoard(sb, board, i, boardLabels, ChessGame.TeamColor.WHITE);
        }
        return sb.toString();
    }

    private String drawBlackBoardGraphic(ChessBoard board) {
        StringBuilder sb = new StringBuilder();
        int startRow = 0;
        int numRows = 10;
        String[] boardLabels = {"h", "g", "f", "e", "d", "c", "b", "a"};

        for (int i = 0; i < (numRows + startRow); i++) {
            sb = drawChessBoard(sb, board, i, boardLabels, ChessGame.TeamColor.BLACK);
        }

        return sb.toString();
    }

    private StringBuilder drawChessBoard(StringBuilder sb,
                                         ChessBoard board, int i,
                                         String[] boardLabels,
                                         ChessGame.TeamColor perspective) {
        switch (i) {
            // Both 0 and 9 have the same string
            case 0:
            case 9:
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
                        this.sandwichString(Integer.toString(i))
                );

                if (perspective == ChessGame.TeamColor.WHITE) {
                    for (int j = 1; j < 9; j++) {
                        sb = this.drawTile(sb, board, i, j);
                    }
                } else {
                    for (int j = 8; j > 0; j--) {
                        sb = this.drawTile(sb, board, i, j);
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


    private StringBuilder drawTile(StringBuilder sb, ChessBoard board, int row, int col) {
        ChessPosition curPos = new ChessPosition(row, col);
        ChessPiece curPiece = board.getPiece(curPos);
        String pieceToDraw = "";
        String curTileColor = this.getTileColor(curPos);

        if (curTileColor.equals("WHITE")) {
            sb.append(whiteTileColor);
        } else {
            sb.append(blackTileColor);
        }

        if (curPiece == null) {
            if (curTileColor.equals("WHTIE")) {
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

        sb.append(
                this.sandwichString(pieceToDraw)
        );

        return sb;
    }

    private String sandwichString(String str){
        return ChessUI.SPACE + str + ChessUI.SPACE;
    }

    private String getTileColor(ChessPosition position){
        int row = position.getRow();
        int col = position.getColumn();

        boolean white = (row % 2 != col % 2);

        return white ? "WHITE" : "BLACK";
    }

}
