package handler;

import com.google.gson.Gson;
import model.UserData;
import service.*;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameHandler {

    private GameService gameService;

    public GameHandler(GameService gameService){
        this.gameService = gameService;
    }

    public String handleCreateGame(Request req, Response res){
        String authToken = req.headers("authorization");
        CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);
        request.setAuthorization(authToken);
        CreateGameResult result = this.gameService.createGame(request);
        res.status(result.getStatus());
        return new Gson().toJson(Map.of("gameID", result.getGameID()));
    }

    public String handleListGames(Request req, Response res){
        String authToken = req.headers("authorization");
        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResult result = this.gameService.listGames(request);
        res.status(result.getStatus());
        return new Gson().toJson(result.getGames());
    }
}