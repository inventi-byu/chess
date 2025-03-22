package client;

import chess.ChessBoard;
import chess.ChessGame;
import exceptions.ServerFacadeException;
import ui.ServerFacade;

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

    public ChessClient(ServerFacade serverFacade){
        this.loginStatus = ChessClient.STATUS_LOGGED_OUT;
        this.serverFacade = serverFacade;
        this.menuState = STATE_PRELOGIN;

        this.board = null;
        this.teamColor = null;
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

    public ChessBoard getBoard(){
        return this.board;
    }

    public ChessGame.TeamColor getTeamColor(){
        return this.teamColor;
    }

    public String evalLine(String line){
        String result = "";
        String[] command = line.split(" ");
        switch (command[0]){
            case "quit":
                result = "quit";
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
                result = "Create not implemented. :(";
                break;

            case "list":
                result = "List not implemented. :(";
                break;

            case "join":
                result = "Join not implemented. :(";
                break;

            case "observe":
                result = "Observe not implemented. :(";
                break;

            case "logout":
                this.setLoginStatus(STATUS_LOGGED_OUT);
                this.setMenuState(STATE_PRELOGIN);
                result = "Logout not implemented. :(";
                break;

            default:
                result = "Unknown command.";
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
        if () {

            return
        } else {
            return
        }

        String result = "";
        try{
            this.serverFacade.register(command[1], command[2], command[3]);
            this.loginStatus = STATUS_LOGGED_IN;
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
            this.serverFacade.login(command[1], command[2]);
            this.loginStatus = STATUS_LOGGED_IN;
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
}
