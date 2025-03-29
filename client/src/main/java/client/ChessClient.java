package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import exceptions.ServerFacadeException;
import model.AuthData;
import model.GameData;
import model.GameMetaData;
import ui.ServerFacade;

import java.util.Collection;
import java.util.HashMap;

public class ChessClient {
    private String loginStatus;
    private String menuState; // Which menu you are currently in

    private ServerFacade serverFacade;

    public static final String STATUS_LOGGED_IN = "LOGGED IN";
    public static final String STATUS_LOGGED_OUT = "LOGGED OUT";
    public static final String STATE_PRELOGIN= "PRELOGIN";
    public static final String STATE_POSTLOGIN = "POSTLOGIN";
    public static final String STATE_GAME = "GAME";
    public static final String STATE_OBSERVE = "OBSERVE";

    private GameData gameData;
    private ChessBoard board;
    private ChessGame game;
    private ChessGame observingGame;
    private ChessGame.TeamColor teamColor;
    private String username;
    private AuthData authData;
    private GameMetaData[] currentGames;
    private int lastCreatedGameID;
    private ChessBoard currentObservingBoard;
    private int gameID;
    private HashMap<Integer, GameMetaData> gamesMap;
    private ChessPosition highlightPosition;
    private GameData observingGameData;

    public ChessClient(ServerFacade serverFacade){
        this.loginStatus = ChessClient.STATUS_LOGGED_OUT;
        this.serverFacade = serverFacade;
        this.menuState = STATE_PRELOGIN;

        this.board = null;
        this.teamColor = null;
        this.username = null;
        this.authData = null;
        this.currentGames = null;
        this.lastCreatedGameID = 0; // No game created, because the IDs from the database start at 1.
        this.currentObservingBoard = null;
        this.gameData = null;
        this.game = null;
        this.gameID = 0; // No game at the beginning
        this.gamesMap = new HashMap<>();
        this.observingGameData = null;
        this.observingGame = null;

        this.highlightPosition = null;
    }

    public String getLoginStatus(){
        return this.loginStatus;
    }

    public void setLoginStatus(String loginStatus){
        this.loginStatus = loginStatus;
    }

    public boolean isLoggedIn(){
        return this.loginStatus.equals(STATUS_LOGGED_IN);
    }

    public String getMenuState(){
        return this.menuState;
    }

    public void setMenuState(String menuState){
        this.menuState = menuState;
    }

    public int getLastCreatedGameID(){
        return this.lastCreatedGameID;
    }

    public void setLastCreatedGameID(int gameID){
        this.lastCreatedGameID = gameID;
    }

    public ChessBoard getBoard(){
        return this.board;
    }

    public void setBoard(ChessBoard board){
        this.board = board;
    }

    public ChessGame.TeamColor getTeamColor(){
        return this.teamColor;
    }

    public void setTeamColor(ChessGame.TeamColor teamColor){
        this.teamColor = teamColor;
    }

    public GameMetaData[] getCurrentGames(){
        return this.currentGames;
    }
    public void setCurrentGames(GameMetaData[] currentGames){
        this.currentGames = currentGames;
    }

    public ChessBoard getObservingBoard(){
        return this.currentObservingBoard;
    }

    public void setCurrentObservingBoard(ChessBoard observingBoard){
        this.currentObservingBoard = observingBoard;
    }

    public AuthData getAuthData(){
        return this.authData;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    public GameData getGameData(){
        return this.gameData;
    }

    public void setGameData(GameData gameData){
        this.gameData = gameData;
    }

    public ChessGame getGame(){
        return this.game;
    }

    public void setGame(ChessGame game){
        this.game = game;
    }

    public int getGameID(){
        return this.gameID;
    }

    public void setGameID(int gameID){
        this.gameID = gameID;
    }

    public void setGamesMap(HashMap<Integer, GameMetaData> map){
        this.gamesMap = map;
    }

    public void setObservingGame(ChessGame game){
        this.observingGame = game;
    }

    public void setObservingGameData(GameData gameData){
        this.observingGameData = gameData;
    }
    
    public GameData getObservingGameData(){
        return this.observingGameData;
    }

    public ChessGame getObservingGame(){
        return this.observingGame;
    }

    public void setHighlightPosition(ChessPosition position){
        this.highlightPosition = position;
    }

    public ChessPosition getHighlightPosition(){
        return this.highlightPosition;
    }

    public String evalLine(String line){
        String result = "";
        String[] command = line.split(" ");
        switch (command[0]){
            case "quit":
                result = this.evalQuit();
                break;

            case "help":
                result = this.evalHelp();
                break;

            case "register":
                result = this.evalRegister(command);
                break;

            case "login":
                result = this.evalLogin(command);
                break;

            case "create":
                result = this.evalCreate(command);
                break;

            case "list":
                result = this.evalList();
                break;

            case "join":
                result = this.evalJoin(command);
                break;

            case "observe":
                result = this.evalObserve(command);
                break;

            case "logout":
                result = this.evalLogout();
                break;

            case "highlight":
                result = this.evalHighlight(command);
                break;

            default:
                result = "Unknown command.";
        }
        return result;
    }

    private String evalQuit(){
        String result = "";
        switch (this.loginStatus){
            case STATUS_LOGGED_OUT:
                result = "quit";
                break;
            case STATUS_LOGGED_IN:
                if(this.evalLogout().equals("logout")){
                    result = "quit";
                } else {
                    result = "You can never quit chess! Mwah ha ha ha ha!";
                }
                break;
        }
        return result;
    }

    private String evalHelp() {
        String result = "";
        if (this.getMenuState().equals(STATE_PRELOGIN)){
            result = "helpPreLogin";
        } else if (this.getMenuState().equals(STATE_POSTLOGIN)){
            result = "helpPostLogin";
        } else if (this.getMenuState().equals(STATE_GAME)){
            result = "helpGame";
        } else if (this.getMenuState().equals(STATE_OBSERVE)){
            result = "helpObserve";
        }
        return result;
    }

    private String evalRegister(String[] command){
        if(command.length != 4){
            return "Failed to register. Did you forget to enter a username, password, and email?";
        }
        String result = "";
        try{
            this.setAuthData(
                    this.serverFacade.register(command[1], command[2], command[3])
            );
            this.setLoginStatus(STATUS_LOGGED_IN);
            this.setMenuState(STATE_POSTLOGIN);
            result = "register";
        } catch (ServerFacadeException exception){
            switch (exception.getStatusCode()){
                case 400 -> result = "Failed to register. Did you forget to enter a username, password, and email?";
                case 403 -> result = "Sorry, username \"" + command[1] + "\" is already taken!";
                case 500 -> result = "Failed to register.";
            }
        } catch (Exception exception){
            return "Failed to register.";
        }
        return result;
    }

    private String evalLogin(String[] command){
        if(command.length != 3){
            return "Failed to login. Did you forget to enter a username AND password?";
        }
        String result = "";
        try{
            this.setAuthData(
                    this.serverFacade.login(command[1], command[2])
            );
            this.setLoginStatus(STATUS_LOGGED_IN);
            this.setMenuState(STATE_POSTLOGIN);
            result = "login";
        } catch (ServerFacadeException exception){
            switch (exception.getStatusCode()){
                case 401 -> result = "Could not log you in, wrong credentials.";
                case 500 -> result = "Failed to login.";
            }
        } catch (Exception exception){
            return "Failed to login.";
        }
        return result;
    }

    private String evalLogout(){
        if (this.getLoginStatus().equals(STATUS_LOGGED_OUT)){
            return "Cannot logout, you aren\'t logged in!";
        }
        String result = "";
        try{
            this.serverFacade.logout(this.authData.authToken());
            this.setLoginStatus(STATUS_LOGGED_OUT);
            this.setMenuState(STATE_PRELOGIN);
            result = "logout";
        } catch (ServerFacadeException exception){
            switch (exception.getStatusCode()){
                case 401 -> result = "You are already logged out.";
                case 500 -> result = "Failed to logout.";
            }
        } catch (Exception exception) {
            return "Failed to logout";
        }
        return result;
    }

    private String evalCreate(String[] command){
        if (command.length != 2){
            return "Could not create chess game. Did you forget to name your game?";
        }
        String result = "";
        try{
            this.setLastCreatedGameID(
                    this.serverFacade.createGame(
                            command[1],
                            this.getAuthData().authToken()
                    )
            );
            result = "create";
        } catch (ServerFacadeException exception){
            switch (exception.getStatusCode()){
                case 400 -> result = "Could not create chess game. Did you forget to name your game?";
                case 500 -> result = "Failed to create game \"" + command[1] + "\".";
            }
        } catch (Exception exception) {
            return "Failed to create game.";
        }
        return result;
    }
    private String evalList(){
        String result = "";
        try{
            this.setCurrentGames(
                    this.serverFacade.listGames(this.getAuthData().authToken())
            );

            // Create a map
            HashMap<Integer, GameMetaData> tempGamesMap = new HashMap<>();
            for (int i = 1; i < this.currentGames.length+1; i++){
                tempGamesMap.put(i, this.currentGames[i-1]);
            }
            this.setGamesMap(tempGamesMap);

            result = "list";
        } catch (ServerFacadeException exception){
            switch (exception.getStatusCode()){
                case 401 -> result = "Silly chess player, you can\'t list games when you\'re logged out!";
                case 500 -> result = "Failed to get list of games.";
            }
        } catch (Exception exception) {
            return "Failed to get list of games";
        }
        return result;
    }

    private String evalJoin(String[] command){
        if(command.length != 3){
            return "Could not join game. Did you forget to the game id or player color?";
        }
        String result = "";
        int chosenGameNumber = 0;
        GameMetaData chosenGameMetaData = null;
        try {
            chosenGameNumber = Integer.parseInt(command[1]);
            chosenGameMetaData = this.gamesMap.get(chosenGameNumber);
        } catch (Exception exception) {
            return "Sorry that game doesn\'t exist!";
        }
        if (chosenGameMetaData == null){
            if (this.gamesMap.isEmpty()){
                return "You need to list the games before you can join one!";
            }
            return "Sorry that game does not exist!";
        }
        int curGameID = chosenGameMetaData.gameID();

        String stringTeamColor = command[2];
        if (this.currentGames == null){
            return "You need to list the games or create one before you can join one.";
        }
        try{
            this.serverFacade.joinGame(stringTeamColor, curGameID, this.authData.authToken());

            ChessGame tempGameUntilPhaseSix = new ChessGame();

            GameData tempGameDataUntilPhaseSix = new GameData(0, "NULL", "NULL", "NOT_IMPLEMENTED", tempGameUntilPhaseSix);

            // This is for phase 6 to implement gameplay

            this.setGame(tempGameDataUntilPhaseSix.game());
            this.setBoard(tempGameDataUntilPhaseSix.game().getBoard());

            if (stringTeamColor.equals("WHITE")){
                this.setTeamColor(ChessGame.TeamColor.WHITE);
            } else {
                this.setTeamColor(ChessGame.TeamColor.BLACK);
            }

            this.setGameID(curGameID);
            this.setGameData(tempGameDataUntilPhaseSix);

            this.setMenuState(STATE_GAME);
            result = "join";
        } catch (ServerFacadeException exception){
            switch (exception.getStatusCode()){
                case 400 -> result = "Could not join game. Did you forget the ID or player color?";
                case 401 -> result = "You can\'t join a game when you\'re logged out!";
                case 403 -> result = "Sorry someone is already playing as " + stringTeamColor + " on game " + gameID + "!";
                case 500 -> result = "Failed to join game " + gameID + ".";
            }
        } catch (Exception exception) {
            return "Failed to join game.";
        }
        return result;
    }
    private String evalObserve(String[] command){
        if(command.length != 2){
            return "Could not observe chess game. Did you forget to enter the game id?";
        }
        String result = "";
        try{
            this.serverFacade.observe(command[1], this.authData.authToken());

            ChessBoard tempBoardUntilPhaseSix = new ChessBoard();
            tempBoardUntilPhaseSix.resetBoard();
            this.setCurrentObservingBoard(tempBoardUntilPhaseSix);

            ChessGame tempGameUntilPhaseSix = new ChessGame();
            tempGameUntilPhaseSix.setBoard(tempBoardUntilPhaseSix);

            this.setMenuState(STATE_OBSERVE);
            result = "observe";
        } catch (ServerFacadeException exception){
            switch (exception.getStatusCode()){
                case 401 -> result = "You can\'t observe a game when you\'re logged out!";
                case 500 -> result = "Failed to observe game " + command[1] + ".";
            }
        } catch (Exception exception) {
            return "Failed to observe game.";
        }
        return result;
    }

    private String evalHighlight(String[] command){
        if(command.length != 2){
            return "Could not highlight moves. Did you forget to enter position of the piece you want to highlight?";
        }
        String chessStylePosition = command[1];
        chessStylePosition = chessStylePosition.toLowerCase();
        char[] positionAsArray = chessStylePosition.toCharArray();
        int row = 0;
        int col = 0;
        try {
            row = Integer.parseInt(
                    String.valueOf(
                            positionAsArray[1]
                    )
            );
        } catch (Exception exception) {
            return "Sorry, \"" + chessStylePosition + "\" is not valid position.";
        }

        // Convert letter to number
        switch (positionAsArray[0]){
            case 'a' -> col = 1;
            case 'b' -> col = 2;
            case 'c' -> col = 3;
            case 'd' -> col = 4;
            case 'e' -> col = 5;
            case 'f' -> col = 6;
            case 'g' -> col = 7;
            case 'h' -> col = 8;
        }
        this.setHighlightPosition(new ChessPosition(row, col));
        return "highlight";
    }

    /**
     * Get all the legal moves a given piece can make
     * @return ChessMove[] of all legal moves from validMoves
     */
    public ChessMove[] getLegalMoves(ChessPosition position){
        Collection<ChessMove> moves = null;
        if (this.currentObservingBoard == null && this.game != null){
            moves = this.game.validMoves(position);
        } else {
            moves = this.observingGame.validMoves(position);
        }
        return moves.toArray(new ChessMove[0]);
    }
}
