package server;

import com.google.gson.*;
import handler.ClearHandler;
import service.ClearService;
import service.ResponseException;
import spark.*;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.exception(ResponseException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public int port(){
        return Spark.port();
    }


    private void exceptionHandler(ResponseException exception, Request req, Response res) {
        res.status(exception.getStatusCode());
        res.body(exception.toJson());
    }

    /**
     * Sends the register request information from the client to the handler.
     * @param req the http register request from the client
     * @param res the http register response that the server should give.
     * @return Not sure yet
     */
    public Object registerUser(Request req, Response res){
        RegisterRequest request = new Gson().fromJson(res.body(), RegisterRequest.class);
        UserHandler handler = new UserHandler();
        res = handler.handleRegister(request);
        // return "";
        throw new RuntimeException("Server.registerUser() is not implemented!");
    }
    /**
     * Sends the login request information from the client to the handler.
     * @param req the http login request from the client
     * @param res the http login response that the server should give.
     * @return Not sure yet
     */
    public Object loginUser(Request req, Response res){
        throw new RuntimeException("Server.loginUser() is not implemented!");
    }
    /**
     * Sends the logout request from the client to the handler.
     * @param req the http logout request from the client
     * @param res the http logout response that the server should give.
     * @return Not sure yet
     */
    public Object logoutUser(Request req, Response res){
        throw new RuntimeException("Server.logoutUser() is not implemented!");
    }
    /**
     * Sends the list games request from the client to the handler.
     * @param req the http list games request from the client
     * @param res the http list games response that the server should give.
     * @return Not sure yet
     */
    public Object listGames(Request req, Response res){
        throw new RuntimeException("Server.listGames() is not implemented!");
    }
    /**
     * Sends the create game request from the client to the handler.
     * @param req the http create game request from the client
     * @param res the http create game response that the server should give.
     * @return Not sure yet
     */
    public Object createGame(Request req, Response res){
        throw new RuntimeException("Server.createGame() is not implemented!");
    }
    /**
     * Sends the http join request from the client to the handler.
     * @param req the http join game request from the client
     * @param res the http join game response that the server should give.
     * @return Not sure yet
     */
    public Object joinGame(Request req, Response res){
        throw new RuntimeException("Server.joinGame() is not implemented!");
    }
    /**
     * Sends the http clear request from the client to the handler.
     * @param req the http clear request from the client
     * @param res the http clear response that the server should give.
     * @return Not sure yet
     */
    public Object clear(Request req, Response res) throws ResponseException {
        ClearHandler handler = new ClearHandler();
        res = handler.handle(req, res);
        return "";
    }
}
