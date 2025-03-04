package server;

import dataaccess.MemoryAdminDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

import handler.ClearHandler;
import handler.UserHandler;
import handler.SessionHandler;
import handler.GameHandler;

import service.ClearService;
import service.UserService;
import service.LoginService;
import service.LogoutService;
import service.GameService;
import service.exception.ResponseException;

import spark.*;


public class Server {

    private MemoryAuthDAO authDAO;
    private MemoryUserDAO userDAO;
    private MemoryAdminDAO adminDAO;
    private MemoryGameDAO gameDAO;

    private ClearService ClearService;
    private UserService UserService;
    private LoginService LoginService;
    private LogoutService LogoutService;
    private GameService GameService;

    private ClearHandler ClearHandler;
    private UserHandler UserHandler;
    private SessionHandler SessionHandler;
    private GameHandler GameHandler;

    public Server(){

        this.authDAO = new MemoryAuthDAO();
        this.userDAO = new MemoryUserDAO();
        this.gameDAO = new MemoryGameDAO();
        this.adminDAO = new MemoryAdminDAO(this.authDAO, this.userDAO, this.gameDAO);

        this.ClearService = new ClearService(this.authDAO, this.userDAO, this.gameDAO, this.adminDAO);
        this.UserService = new UserService(this.authDAO, this.userDAO, this.gameDAO);
        this.LoginService = new LoginService(this.authDAO, this.userDAO, this.gameDAO);
        this.LogoutService = new LogoutService(this.authDAO, this.userDAO, this.gameDAO);
        this.GameService = new GameService(this.authDAO, this.userDAO, this.gameDAO);

        this.ClearHandler = new ClearHandler(this.ClearService);
        this.UserHandler = new UserHandler(this.UserService);
        this.SessionHandler = new SessionHandler(this.LoginService, this.LogoutService);
        this.GameHandler = new GameHandler(this.GameService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);
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
     * @param req the http register request from the client.
     * @param res the http register response that the server should give.
     * @return JSON string of the RegisterUserResult body.
     */
    public Object registerUser(Request req, Response res) throws ResponseException {
        return this.UserHandler.handleRegister(req, res);
    }

    /**
     * Sends the login request information from the client to the handler.
     * @param req the http login request from the client.
     * @param res the http login response that the server should give
     * @return JSON string of the LoginUserResult body.
     */
    public Object loginUser(Request req, Response res){
        return this.SessionHandler.handleLogin(req, res);
        //throw new RuntimeException("Server.loginUser() is not implemented!");
    }

    /**
     * Sends the logout request from the client to the handler.
     * @param req the http logout request from the client.
     * @param res the http logout response that the server should give.
     * @return JSON string of the LogoutUserResult body.
     */
    public Object logoutUser(Request req, Response res){
        return this.SessionHandler.handleLogout(req, res);
    }

    /**
     * Sends the list games request from the client to the handler.
     * @param req the http list games request from the client.
     * @param res the http list games response that the server should give.
     * @return JSON string of the ListGamesResult body.
     */
    public Object listGames(Request req, Response res){
        return this.GameHandler.handleListGames(req, res);
    }

    /**
     * Sends the create game request from the client to the handler.
     * @param req the http create game request from the client
     * @param res the http create game response that the server should give.
     * @return JSON string of the CreateGameResult body.
     */
    public Object createGame(Request req, Response res){
        return this.GameHandler.handleCreateGame(req, res);
    }

    /**
     * Sends the http join request from the client to the handler.
     * @param req the http join game request from the client
     * @param res the http join game response that the server should give.
     * @return JSON string of the JoinGameResult body.
     */
    public Object joinGame(Request req, Response res){
        return this.GameHandler.handleJoinGame(req, res);
    }

    /**
     * Sends the http clear request from the client to the handler.
     * @param req the http clear request from the client
     * @param res the http clear response that the server should give.
     * @return JSON string of the ClearResult body.
     */
    public Object clear(Request req, Response res) throws ResponseException {
        return this.ClearHandler.handle(req, res);
    }
}
