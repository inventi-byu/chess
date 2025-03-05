package service.result;

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

    @Override
    public boolean equals(Object o){
        if (o instanceof CreateGameResult){
            CreateGameResult obj = (CreateGameResult)o;
            if(this.getStatus() == obj.getStatus() && this.getGameID() == obj.getGameID()){
                return true;
            }
        }
        return false;
    }

}
