package service.result;

import model.GameMetaData;

import java.util.List;

public class ListGamesResult {

    private int status;
    private List<GameMetaData> games;

    public ListGamesResult(int status, List<GameMetaData> games){
        this.status = status;
        this.games = games;
    }

    public int getStatus(){
        return this.status;
    }

    public List<GameMetaData> getGames(){
        return this.games;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ClearResult){
            ListGamesResult obj = (ListGamesResult)o;
            if(this.getStatus() == obj.getStatus() && this.getGames().equals(obj.getGames())){
                return true;
            }
        }
        return false;
    }
}
