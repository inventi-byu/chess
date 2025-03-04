package service;

public class CreateGameResult {

    private int status;
    private int gameID;

    public CreateGameResult(int status, int gameID){
        this.status = status;
        this.gameID = gameID;
    }

    public int getStatus(){
        return this.status;
    }

    public int getGameID(){
        return this.gameID;
    }

}
