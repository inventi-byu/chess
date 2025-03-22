package client;

import chess.ChessBoard;
import chess.ChessGame;
import exceptions.ServerFacadeException;
import model.AuthData;
import model.GameData;
import model.GameMetaData;
import ui.ServerFacade;

import java.lang.annotation.IncompleteAnnotationException;

public class ChessClient {
    private String loginStatus;
    private String menuState; // Which menu you are currently in

    private ServerFacade serverFacade;

    public static final String STATUS_LOGGED_IN = "LOGGED IN";
    public static final String STATUS_LOGGED_OUT = "LOGGED OUT";
    public static final String STATE_PRELOGIN= "PRELOGIN";
    public static final String STATE_POSTLOGIN = "POSTLOGIN";
    public static final String STATE_GAME = "GAME";

    private ChessBoard board;
    private ChessGame.TeamColor teamColor;
    private String username;
    private AuthData authData;
    private GameMetaData[] currentGames;
    private int lastCreatedGameID;
    private ChessBoard currentObservingBoard;

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

            default:
                result = "Unknown command.";
        }
        return result;
    }

    private String evalQuit(){
        String result = "";
        if(this.evalLogout().equals("logout")){
            result = "quit";
        } else {
            result = "You can never quit chess! Mwah ha ha ha ha!";
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
        }
        return result;
    }

    private String evalRegister(String[] command){
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
                case 400 -> result = "Failed to register. Did you forget to enter a username AND password?";
                case 403 -> result = "Sorry, username \"" + command[1] + "\" is already taken!";
                case 500 -> result = "Failed to register.";
            }
        }
        return result;
    }

    private String evalLogin(String[] command){
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
        }
        return result;
    }

    private String evalLogout(){
        String result = "";
        try{
            this.serverFacade.logout(this.authData.authToken());
            this.setLoginStatus(STATUS_LOGGED_OUT);
            this.setMenuState(STATE_PRELOGIN);
            result = "logout";
        } catch (ServerFacadeException exception){
            switch (exception.getStatusCode()){
                case 401 -> result = "You are already s out.";
                case 500 -> result = "Failed to logout.";
            }
        }
        return result;
    }

    private String evalCreate(String[] command){
        String result = "";
        try{
            this.setLastCreatedGameID(
                    this.serverFacade.createGame(
                            Integer.parseInt(command[1])
                    )
            );
            result = "create";
        } catch (ServerFacadeException exception){
            switch (exception.getStatusCode()){
                case 400 -> result = "Could not create chess game \"" + command[1] + "\". Did you forget to name your game?";
                case 500 -> result = "Failed to create game \"" + command[1] + "\".";
            }
        }
        return result;
    }
    private String evalList(){
        String result = "";
        try{
            this.setCurrentGames(
                    this.serverFacade.listGames()
            );
            result = "list";
        } catch (ServerFacadeException exception){
            switch (exception.getStatusCode()){
                case 401 -> result = "Silly chess player, you can\'t list games when you\'re logged out!";
                case 500 -> result = "Failed to get list of games.";
            }
        }
        return result;
    }
    private String evalJoin(String[] command){
        throw new RuntimeException("Not implemented.");
    }
    private String evalObserve(String[] command){
        String result = "";
        try{
            this.setCurrentObservingBoard(
                    this.serverFacade.observe(command[1])
            );
            this.setMenuState(STATE_GAME);
            result = "observe";
        } catch (ServerFacadeException exception){
            switch (exception.getStatusCode()){
                case 401 -> result = "You can\'t observe a game when you\'re logged out!";
                case 500 -> result = "Failed to get list of games.";
            }
        }
        return result;
    }
}
