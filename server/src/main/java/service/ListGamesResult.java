package service;

import model.GameMetaData;

import java.util.List;

public class ListGamesResult {

    private int status;
    private List<GameMetaData> games;

    ListGamesResult(int status, List<GameMetaData> games){
        this.status = status;
        this.games = games;
    }

    public int getStatus(){
        return this.status;
    }

    public List<GameMetaData> getGames(){
        return this.games;
    }


}
