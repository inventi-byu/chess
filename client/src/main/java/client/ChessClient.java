package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import websocket.PositionConverter;
import websocket.exception.ChessPositionException;
import exceptions.ServerFacadeException;
import exceptions.WebSocketFacadeException;
import model.AuthData;
import model.GameData;
import model.GameMetaData;
import ui.ServerFacade;
import ui.WebSocketFacade;

import java.util.Collection;
import java.util.HashMap;

public class ChessClient {
    private String loginStatus;
    private String menuState; // Which menu you are currently in

    private ServerFacade serverFacade;
    private WebSocketFacade webSocketFacade;

    public static final String STATUS_LOGGED_IN = "LOGGED IN";
    public static final String STATUS_LOGGED_OUT = "LOGGED OUT";

    public static final String STATE_PRELOGIN= "PRELOGIN";
    public static final String STATE_POSTLOGIN = "POSTLOGIN";
    public static final String STATE_GAME = "GAME";
    public static final String STATE_OBSERVE = "OBSERVE";

    // User info
    private String username;
    private AuthData authData;
    private ChessGame.TeamColor teamColor;

    // Info about games
    private HashMap<Integer, GameMetaData> gamesMap;
    private GameMetaData[] currentGames;
    private int lastCreatedGameID;

    // Joined Game info
    private ChessBoard board;
    private ChessGame game;
    private GameData gameData;
    private int gameID;

    // Observe info
    private ChessBoard observingBoard;
    private ChessGame observingGame;
    private GameData observingGameData;

    // UI info
    private ChessPosition highlightPosition;

    public ChessClient(ServerFacade serverFacade, WebSocketFacade webSocketFacade){
        // Client info
        this.serverFacade = serverFacade;
        this.webSocketFacade = webSocketFacade;
        this.loginStatus = ChessClient.STATUS_LOGGED_OUT;
        this.menuState = STATE_PRELOGIN;

        // User info
        this.teamColor = null;
        this.username = null;
        this.authData = null;

        // Info about games
        this.gamesMap = new HashMap<>();
        this.currentGames = null;
        this.lastCreatedGameID = 0; // No game created, because the IDs from the database start at 1.

        // Joined game info
        this.board = null;
        this.game = null;
        this.gameData = null;
        this.gameID = 0; // No game at the beginning

        // Observe info
        this.observingBoard = null;
        this.observingGame = null;
        this.observingGameData = null;

        // UI info
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
        return this.observingBoard;
    }

    public void setObservingBoard(ChessBoard observingBoard){
        this.observingBoard = observingBoard;
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

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
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

            case "redraw":
                result = evalRedraw();
                break;

            case "leave":
                result = this.evalLeave();
                break;

            case "move":
                result = this.evalMove(command);
                break;

            case "resign":
                result = this.evalResign();
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
            this.setUsername(this.getAuthData().username());
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
            this.setGameID(curGameID);

            this.webSocketFacade.joinGame(this.username, stringTeamColor, curGameID, this.authData.authToken());

            //TimeUnit.SECONDS.sleep(5);

            if (stringTeamColor.equals("WHITE")){
                this.setTeamColor(ChessGame.TeamColor.WHITE);
            } else {
                this.setTeamColor(ChessGame.TeamColor.BLACK);
            }

            // Update the menu state
            this.setGameID(curGameID);
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
        if(!this.getMenuState().equals(STATE_POSTLOGIN)){
            return "Sorry you can't use that command right now.";
        }
        String result = "";
        try{
            this.webSocketFacade.observeGame(command[1], this.authData.authToken());

            // Temporary until Phase 6
            ChessGame tempObservingGameUntilPhaseSix = new ChessGame();
            GameData tempObservingGameDataUntilPhaseSix = new GameData(
                    0,
                    "white",
                    "black",
                    "tempGameUntilPhaseSix",
                    tempObservingGameUntilPhaseSix
            );

            this.updateObservingGameInfo(tempObservingGameDataUntilPhaseSix);

            this.setMenuState(STATE_OBSERVE);

            result = "observe";
        } catch (WebSocketFacadeException exception){
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
        if(!(this.getMenuState().equals(STATE_GAME) || this.getMenuState().equals(STATE_OBSERVE))){
            return "Sorry you have to have joined or be observing a game to use that command.";
        }
        if(command.length != 2){
            return "Could not highlight moves. Did you forget to enter position of the piece you want to highlight?";
        }
        String chessStylePosition = command[1];
        try {
            ChessPosition position = PositionConverter.locationToPosition(chessStylePosition);
            this.setHighlightPosition(position);
        } catch (ChessPositionException exception){
            return "Sorry, \"" + chessStylePosition + "\" is not valid position.";
        }

        return "highlight";
    }

    private String evalRedraw(){
        if(!(this.getMenuState().equals(STATE_GAME) || this.getMenuState().equals(STATE_OBSERVE))){
            return "Sorry you have to have joined or be observing a game to use that command.";
        }
        return "redraw";
    }

    private String evalMove(String[] command){
        if(!this.getMenuState().equals(STATE_GAME)){
            return "Sorry you have to have joined a game to use that command.";
        }
        if (command.length != 3){
            return "Could not observe chess game. Did you forget to enter the game id?";
        }
        try {
            ChessPosition start = PositionConverter.locationToPosition(command[1]);
            ChessPosition end = PositionConverter.locationToPosition(command[2]);
            // TODO: Add promotion piece logic (it would be required in the command)
            this.webSocketFacade.makeMove(this.authData, this.gameID, this.teamColor, start, end, null);
        } catch (ChessPositionException exception) {
            return "Sorry that move is not valid. Check your position notation: each position should start with a letter from a to h, and end with a number from 1 to 8.";
        } catch (WebSocketFacadeException exception) {
            return "Sorry that move is not valid. Did you include a start and end position?";
        }
        return "move";
    }

    private String evalLeave(){
        if(!(this.getMenuState().equals(STATE_GAME) || this.getMenuState().equals(STATE_OBSERVE))) {
            return "Sorry you have to have joined or be observing a game to use that command.";
        }
        try {
            this.webSocketFacade.leaveGame(this.getAuthData().authToken(), this.getUsername());
            if (this.getMenuState().equals(STATE_GAME)) {
                this.clearGameInfo();
            } else if (this.getMenuState().equals(STATE_OBSERVE)) {
                this.clearObservingGameInfo();
            }
            this.setMenuState(STATE_POSTLOGIN);
        } catch (ServerFacadeException exception) {
            return "Cannot leave game. Are you logged in?";
        }

        return "leave";
    }

    private String evalResign(){
        if(!this.getMenuState().equals(STATE_GAME)){
            return "Sorry you have to have joined a game to use that command.";
        }
        try {
            this.webSocketFacade.resignGame(this.getAuthData().authToken(), this.getUsername());
            // You can only resign when you are playing a game, not observing.
            this.clearGameInfo();
            this.setMenuState(STATE_POSTLOGIN);
        } catch (ServerFacadeException exception) {
            return "Cannot resign from game. Are you logged in? Did you already win? Has the game even started yet?";
        }

        return "resign";
    }

    /**
     * Get all the legal moves a given piece can make
     * @return ChessMove[] of all legal moves from validMoves
     */
    public ChessMove[] getLegalMoves(ChessPosition position){
        Collection<ChessMove> moves = null;

        if (this.getObservingBoard() == null && this.getGame() != null){
            moves = this.game.validMoves(position);
        } else if (this.getObservingBoard() != null){
            moves = this.observingGame.validMoves(position);
        } else {
            return null;
        }
        try {
            return moves.toArray(new ChessMove[0]);
        } catch (NullPointerException exception) {
            // There are no moves
            return null;
        }
    }

    // Public because they are used by the NotificationHandler
    public void updateGameInfo(GameData newGameData) {
        ChessGame updatedGame = newGameData.game();
        ChessBoard updatedBoard = updatedGame.getBoard();
        int updatedGameID = newGameData.gameID();

        this.setGameData(newGameData);
        this.setGame(updatedGame);
        this.setBoard(updatedBoard);
        this.setGameID(updatedGameID);
        return;
    }

    public void updateObservingGameInfo(GameData newObservingGameData) {
        ChessGame updatedObservingGame = newObservingGameData.game();
        ChessBoard updatedObservingBoard = updatedObservingGame.getBoard();

        this.setObservingGameData(newObservingGameData);
        this.setObservingGame(updatedObservingGame);
        this.setObservingBoard(updatedObservingBoard);
        return;
    }

    public void clearGameInfo(){
        this.setGameData(null);
        this.setGame(null);
        this.setBoard(null);
        this.setGameID(0);
        return;
    }

    public void clearObservingGameInfo(){
        this.setObservingGameData(null);
        this.setObservingGame(null);
        this.setObservingBoard(null);
        return;
    }

    public void updateWebSocketFacade(WebSocketFacade updatedWebSocketFacade){
        this.webSocketFacade = updatedWebSocketFacade;
    }
}
