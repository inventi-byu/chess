package server;

import dataaccess.*;

import handler.ClearHandler;
import handler.UserHandler;
import handler.SessionHandler;
import handler.GameHandler;

import org.mindrot.jbcrypt.BCrypt;
import service.ClearService;
import service.UserService;
import service.LoginService;
import service.LogoutService;
import service.GameService;
import server.service.exception.ResponseException;

import spark.*;


public class Server {

    private AuthDAO authDAO;
    private UserDAO userDAO;
    private AdminDAO adminDAO;
    private GameDAO gameDAO;

    private ClearService clearService;
    private UserService userService;
    private LoginService loginService;
    private LogoutService logoutService;
    private GameService gameService;

    private ClearHandler clearHandler;
    private UserHandler userHandler;
    private SessionHandler sessionHandler;
    private GameHandler gameHandler;

    public Server(){

        this.authDAO = new MySQLAuthDAO();
        this.userDAO = new MySQLUserDAO();
        this.gameDAO = new MySQLGameDAO();
        this.adminDAO = new MySQLAdminDAO();

        this.clearService = new ClearService(this.adminDAO);
        this.userService = new UserService(this.authDAO, this.userDAO, this.gameDAO);
        this.loginService = new LoginService(this.authDAO, this.userDAO, this.gameDAO);
        this.logoutService = new LogoutService(this.authDAO, this.userDAO, this.gameDAO);
        this.gameService = new GameService(this.authDAO, this.userDAO, this.gameDAO);

        this.clearHandler = new ClearHandler(this.clearService);
        this.userHandler = new UserHandler(this.userService);
        this.sessionHandler = new SessionHandler(this.loginService, this.logoutService);
        this.gameHandler = new GameHandler(this.gameService);
    }

    public int run(int desiredPort) {

        try {
            DatabaseManager.createDatabase();
        } catch (Exception exception){
            throw new RuntimeException("Error: could not create database. Message: ", exception);
        }

        BCrypt.hashpw("hi", BCrypt.gensalt());
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
        return this.userHandler.handleRegister(req, res);
    }

    /**
     * Sends the login request information from the client to the handler.
     * @param req the http login request from the client.
     * @param res the http login response that the server should give
     * @return JSON string of the LoginUserResult body.
     */
    public Object loginUser(Request req, Response res){
        return this.sessionHandler.handleLogin(req, res);
        //throw new RuntimeException("Server.loginUser() is not implemented!");
    }

    /**
     * Sends the logout request from the client to the handler.
     * @param req the http logout request from the client.
     * @param res the http logout response that the server should give.
     * @return JSON string of the LogoutUserResult body.
     */
    public Object logoutUser(Request req, Response res){
        return this.sessionHandler.handleLogout(req, res);
    }

    /**
     * Sends the list games request from the client to the handler.
     * @param req the http list games request from the client.
     * @param res the http list games response that the server should give.
     * @return JSON string of the ListGamesResult body.
     */
    public Object listGames(Request req, Response res){
        return this.gameHandler.handleListGames(req, res);
    }

    /**
     * Sends the create game request from the client to the handler.
     * @param req the http create game request from the client
     * @param res the http create game response that the server should give.
     * @return JSON string of the CreateGameResult body.
     */
    public Object createGame(Request req, Response res){
        return this.gameHandler.handleCreateGame(req, res);
    }

    /**
     * Sends the http join request from the client to the handler.
     * @param req the http join game request from the client
     * @param res the http join game response that the server should give.
     * @return JSON string of the JoinGameResult body.
     */
    public Object joinGame(Request req, Response res){
        return this.gameHandler.handleJoinGame(req, res);
    }

    /**
     * Sends the http clear request from the client to the handler.
     * @param req the http clear request from the client
     * @param res the http clear response that the server should give.
     * @return JSON string of the ClearResult body.
     */
    public Object clear(Request req, Response res) throws ResponseException {
        return this.clearHandler.handle(req, res);
    }

    /**
     * Creates the database and tables.
     */
    public void createDatabase() {
        throw new RuntimeException("Not implmented.");
    }
}
