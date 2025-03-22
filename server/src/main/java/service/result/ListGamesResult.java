package service.result;

import model.GameMetaData;

import java.util.List;

public class ListGamesResult {

    private int status;
    private GameMetaData[] games;

    public ListGamesResult(int status, GameMetaData[] games){
        this.status = status;
        this.games = games;
    }

    public int getStatus(){
        return this.status;
    }

    public GameMetaData[] getGames(){
        return this.games;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ListGamesResult){
            ListGamesResult obj = (ListGamesResult)o;
            if(this.getStatus() == obj.getStatus() && this.getGames().equals(obj.getGames())){
                return true;
            }
        }
        return false;
    }
}
