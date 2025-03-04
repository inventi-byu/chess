package handler;

import com.google.gson.Gson;
import service.*;
import service.request.CreateGameRequest;
import service.request.JoinGameRequest;
import service.request.ListGamesRequest;
import service.result.CreateGameResult;
import service.result.JoinGameResult;
import service.result.ListGamesResult;
import spark.Request;
import spark.Response;

import java.util.Map;

public class GameHandler extends Handler {

    private GameService gameService;

    public GameHandler(GameService gameService){
        this.gameService = gameService;
    }

    /**
     * Handles a create game request via the game service.
     * @param req the Spark request from the client
     * @param res the Spark response to send back to the client
     * @return A JSON string of a Map with the "gameID" label and the gameID.
     */
    public String handleCreateGame(Request req, Response res){
        String authToken = req.headers("authorization");
        CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);
        request.setAuthorization(authToken);

        CreateGameResult result = this.gameService.createGame(request);
        res.status(result.getStatus());
        return new Gson().toJson(Map.of("gameID", result.getGameID()));
    }

    /**
     * Handles a list games request via the game service.
     * @param req the Spark request from the client
     * @param res the Spark response to send back to the client
     * @return A JSON string of a Map of the "games" label and a list of all the games in the database.
     */
    public String handleListGames(Request req, Response res){
        String authToken = req.headers("authorization");
        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResult result = this.gameService.listGames(request);
        res.status(result.getStatus());
        return new Gson().toJson(Map.of("games", result.getGames()));
    }

    /**
     * Handles a join game request via the game service.
     * @param req the Spark request from the client
     * @param res the Spark response to send back to the client
     * @return an empty string (200 success response).
     */
    public String handleJoinGame(Request req, Response res){
        String authToken = req.headers("authorization");
        JoinGameRequest request = new Gson().fromJson(req.body(), JoinGameRequest.class);
        request.setAuthorization(authToken);
        JoinGameResult result = this.gameService.joinGame(request);
        res.status(result.getStatus());
        return "";
    }
}