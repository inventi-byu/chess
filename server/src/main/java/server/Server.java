package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

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

    public Object registerUser(){
        throw RuntimeException("Server.registerUser() is not implemented!");
    }
    public Object loginUser(){
        throw RuntimeException("Server.loginUser() is not implemented!");
    }
    public Object logoutUser(){
        throw RuntimeException("Server.logoutUser() is not implemented!");
    }
    public Object listGames(){
        throw RuntimeException("Server.listGames() is not implemented!");
    }
    public Object createGame(){
        throw RuntimeException("Server.createGame() is not implemented!");
    }
    public Object joinGame(){
        throw RuntimeException("Server.joinGame() is not implemented!");
    }
    public Object clear(){
        throw RuntimeException("Server.clear() is not implemented!");
    }
}
