package client;

import chess.*;
import websocket.PositionConverter;
import websocket.exception.ChessPositionException;
import exceptions.ServerFacadeException;
import exceptions.WebSocketFacadeException;
import model.*;
import ui.*;
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
    private final ChessClientInfoUpdater infoUpdater = new ChessClientInfoUpdater(this);
    private final ChessClientEvalCaller evalCaller = new ChessClientEvalCaller(this);

    private String username;
    private AuthData authData;
    private ChessGame.TeamColor teamColor;
    private HashMap<Integer, GameMetaData> gamesMap;
    private GameMetaData[] currentGames;
    private int lastCreatedGameID;
    private ChessBoard board;
    private ChessGame game;
    private GameData gameData;
    private int gameID;
    private boolean observing;
    private ChessBoard observingBoard;
    private ChessGame observingGame;
    private GameData observingGameData;
    private ChessPosition highlightPosition;

    public ChessClient(ServerFacade serverFacade, WebSocketFacade webSocketFacade){
        this.serverFacade = serverFacade;
        this.webSocketFacade = webSocketFacade;
        this.loginStatus = ChessClient.STATUS_LOGGED_OUT;
        this.menuState = STATE_PRELOGIN;
        this.teamColor = null;
        this.username = null;
        this.authData = null;
        this.gamesMap = new HashMap<>();
        this.currentGames = null;
        this.lastCreatedGameID = 0; // No game created, IDs from database start at 1
        this.board = null;
        this.game = null;
        this.gameData = null;
        this.gameID = 0; // No game at the beginning
        this.observing = false;
        this.observingBoard = null;
        this.observingGame = null;
        this.observingGameData = null;
        this.highlightPosition = null;
    }

    public String getLoginStatus(){
        return this.loginStatus;
    }
    public void setLoginStatus(String loginStatus){
        this.loginStatus = loginStatus;
    }
    public String getMenuState(){
        return this.menuState;
    }
    public void setMenuState(String menuState){
        this.menuState = menuState;
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
    public AuthData getAuthData(){return this.authData;}
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
    public void setHighlightPosition(ChessPosition position){
        this.highlightPosition = position;
    }
    public ChessPosition getHighlightPosition(){
        return this.highlightPosition;
    }
    public void setObserving(boolean observing){this.observing = observing;}
    public boolean isObserving(){return this.observing;}
    public void setUsername(String username) {this.username = username;}

    public String evalLine(String line){
        return this.evalCaller.evalLine(line);
    }

    public String evalQuit(){
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

    public String evalHelp() {
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

    public String evalRegister(String[] command){
        if(command.length != 4){
            return "Failed to register. Did you forget to enter a username, password, and email?";
        }
        String result = "";
        try{
            this.setAuthData(this.serverFacade.register(command[1], command[2], command[3]));
            this.setUsername(this.getAuthData().username());
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

    public String evalLogin(String[] command){
        if(command.length != 3){
            return "Failed to login. Did you forget to enter a username AND password?";
        }
        String result = "";
        try{
            this.setAuthData(this.serverFacade.login(command[1], command[2]));
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

    public String evalLogout(){
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

    public String evalCreate(String[] command){
        if (command.length != 2){
            return "Could not create chess game. Did you forget to name your game?";
        }
        String result = "";
        try{
            this.setLastCreatedGameID(this.serverFacade.createGame(command[1], this.getAuthData().authToken()));
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
    public String evalList(){
        String result = "";
        try{
            this.setCurrentGames(this.serverFacade.listGames(this.getAuthData().authToken()));
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

    public String evalJoin(String[] command){
        if(command.length != 3){
            return "Could not join game. Did you forget to the game id or player color?";
        }
        String result = "";
        int chosenGameNumber = 0;
        GameMetaData chosenGameMetaData = null;
        try {
            chosenGameNumber = Integer.parseInt(command[1]);
            chosenGameMetaData = this.gamesMap.get(chosenGameNumber);
        } catch (Exception exception) {return "Sorry that game doesn\'t exist!";}
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
            this.webSocketFacade.joinGame(this.authData.authToken(), curGameID);
            if (stringTeamColor.equals("WHITE")){
                this.setTeamColor(ChessGame.TeamColor.WHITE);
            } else {
                this.setTeamColor(ChessGame.TeamColor.BLACK);
            }

            // Update the menu state
            this.setGameID(curGameID); this.setMenuState(STATE_GAME);
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
    public String evalObserve(String[] command){
        if(command.length != 2){
            return "Could not observe chess game. Did you forget to enter the game id?";
        }
        if(!this.getMenuState().equals(STATE_POSTLOGIN)){
            return "Sorry you can't use that command right now.";
        }
        String result = "";
        try{
            this.webSocketFacade.observeGame(this.username, command[1], this.authData.authToken());
            this.setObserving(true); this.setMenuState(STATE_OBSERVE);
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

    public String evalHighlight(String[] command){
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

    public String evalRedraw(){
        if(!(this.getMenuState().equals(STATE_GAME) || this.getMenuState().equals(STATE_OBSERVE))){
            return "Sorry you have to have joined or be observing a game to use that command.";
        }
        return "redraw";
    }

    public String evalMove(String[] command){
        if(!this.getMenuState().equals(STATE_GAME)){
            return "Sorry you have to have joined a game to use that command.";
        }
        if (this.game.isCompleted()){
            return "Sorry you can't move, the game is already over!";
        }
        if (!(command.length == 3 || command.length == 4)){
            return "Could not move. Did you forget to enter a start and end position?";
        }
        if (this.game.getTeamTurn() != this.getTeamColor()){
            return "You can't move, it's not your turn!";
        }
        try {
            ChessPosition start = PositionConverter.locationToPosition(command[1]);
            ChessPiece startPiece = this.getBoard().getPiece(start);
            if(startPiece == null){
                return "There's no piece at " + command[1] + " to move!";
            }
            if (startPiece.getTeamColor() != this.teamColor){
                return "You can't move your opponent's pieces.";
            }

            ChessPiece.PieceType promotionPiece = null;

            if(command.length == 4){
                if(startPiece.getPieceType() != ChessPiece.PieceType.PAWN){
                    return "You can't promote a piece that's not a pawn.";
                }
                switch (command[3]){
                    case "PAWN", "pawn", "p" -> {return "You can't promote a pawn to a pawn.";}
                    case "QUEEN", "queen", "q" -> {promotionPiece = ChessPiece.PieceType.QUEEN;}
                    case "BISHOP", "bishop", "b" -> {promotionPiece = ChessPiece.PieceType.BISHOP;}
                    case "ROOK", "rook", "r" -> {promotionPiece = ChessPiece.PieceType.ROOK;}
                    case "KNIGHT", "knight", "k" -> {promotionPiece = ChessPiece.PieceType.KNIGHT;}
                    default -> {return "\"" + command[3] + "\" is not a valid promotion piece.";}
                }
            }

            ChessPosition end = PositionConverter.locationToPosition(command[2]);
            this.webSocketFacade.makeMove(this.authData, this.gameID, start, end, promotionPiece);
        } catch (ChessPositionException exception) {
            return "Sorry that move is not valid. Check your position notation:" +
                    "each position should start with a letter from a to h," +
                    "and end with a number from 1 to 8.";
        } catch (WebSocketFacadeException exception) {
            return "Sorry that move is not valid. Did you include a start and end position?";
        }
        return "move";
    }

    public String evalLeave(){
        if(!(this.getMenuState().equals(STATE_GAME) || this.getMenuState().equals(STATE_OBSERVE))) {
            return "Sorry you have to have joined or be observing a game to use that command.";
        }
        try {
            String teamColor = null;
            if (this.teamColor == ChessGame.TeamColor.WHITE){
                teamColor = "WHITE";
            } else {
                teamColor = "BLACK";
            }
            if (this.getMenuState().equals(STATE_GAME)) {
                this.webSocketFacade.leaveGame(this.username, teamColor, this.getAuthData().authToken(), this.getGameID(), false);
                this.clearGameInfo();
            } else if (this.getMenuState().equals(STATE_OBSERVE)) {
                this.webSocketFacade.leaveGame(this.username, teamColor, this.getAuthData().authToken(), this.getObservingGameData().gameID(), true);
                this.clearObservingGameInfo();
            }
            this.setMenuState(STATE_POSTLOGIN);
        } catch (WebSocketFacadeException exception) {
            return "Cannot leave game. Are you logged in?";
        }
        return "leave";
    }

    public String evalResign1(){
        return "resign1";
    }

    public String evalResign(){
        if(!this.getMenuState().equals(STATE_GAME)){
            return "Sorry you have to have joined a game to use that command.";
        }
        try {
            this.webSocketFacade.resignGame(this.getAuthData().authToken(), this.gameID); // Don't clear any data, only resigning
        } catch (WebSocketFacadeException exception) {
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

    public void updateGameInfo(GameData newGameData) {this.infoUpdater.updateGameInfo(newGameData);}
    public void updateObservingGameInfo(GameData newObservingGameData) {this.infoUpdater.updateObservingGameInfo(newObservingGameData);}
    public void clearGameInfo(){this.infoUpdater.clearGameInfo();}
    public void clearObservingGameInfo(){this.infoUpdater.clearObservingGameInfo();}
    public void updateWebSocketFacade(WebSocketFacade updatedWebSocketFacade){this.webSocketFacade = updatedWebSocketFacade;}
}