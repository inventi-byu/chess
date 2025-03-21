package client;

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

    public ChessClient(ServerFacade serverFacade){
        this.loginStatus = ChessClient.STATUS_LOGGED_OUT;
        this.serverFacade = serverFacade;
        this.menuState = STATE_PRELOGIN;
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

    public String evalLine(String line){
        String result = "";
        String[] command = line.split(" ");
        switch (command[0]){
            case "quit":
                result = "quit";
                break;

            case "help":
                if (this.getMenuState().equals(STATE_PRELOGIN)){
                    result = "helpPreLogin";
                } else if (this.getMenuState().equals(STATE_POSTLOGIN)){
                    result = "helpPostLogin";
                } else if (this.getMenuState().equals(STATE_GAME)){
                    result = "helpGame";
                }
                break;

            case "register":
                this.loginStatus = STATUS_LOGGED_IN;
                result = "register";
                break;

            case "login":
                this.loginStatus = STATUS_LOGGED_IN;
                result = "login";
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
                result = "Logout not implemented. :(";
                break;

            default:
                result = "Unknown command.";
        }
        return result;
    }
}
