package client;

import ui.ServerFacade;

public class ChessClient {
    private String loginStatus;

    private ServerFacade serverFacade;

    private static final String STATUS_LOGGED_IN = "LOGGED IN";
    private static final String STATUS_LOGGED_OUT = "LOGGED OUT";

    public ChessClient(ServerFacade serverFacade){
        this.loginStatus = ChessClient.STATUS_LOGGED_OUT;
        this.serverFacade = serverFacade;
    }

    public String getLoginStatus(){
        return this.loginStatus;
    }

    public boolean isLoggedIn(){
        return this.loginStatus.equals(STATUS_LOGGED_IN);
    }

    public String evalLine(String line){
        String result = "";
        switch (line){
            case "quit" -> result = "quit";
        }
        return result;
    }
}
