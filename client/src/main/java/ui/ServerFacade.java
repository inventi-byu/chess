package ui;

import chess.ChessBoard;
import exceptions.ServerFacadeException;
import model.AuthData;
import model.GameData;
import model.GameMetaData;
import model.UserData;

public class ServerFacade {
    public ServerFacade(){
        //throw new RuntimeException("Not implemented.");
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public boolean clear() throws ServerFacadeException {
        String path = "/db";
        this.makeHttpRequest("DELETE", path, null, null);
        return true;
    }

    /**
     * Accesses the register endpoint.
     * @return the user's AuthData
     */
    public AuthData register(String username, String password, String email) throws ServerFacadeException {
        String path = "/user";
        UserData userData = new UserData(username, password, email);
        AuthData authData = this.makeHttpRequest("POST", path, userData, AuthData.class);
        return authData;
    }
    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public AuthData login(String username, String password) throws ServerFacadeException {
        String path = "/session";

        record UserCredentials(String username, String password){}
        UserCredentials credentials = new UserCredentials(username, password)

        AuthData authData = this.makeHttpRequest("POST", path, credentials, AuthData.class);
        return authData;
    }
    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public boolean logout(String authToken) throws ServerFacadeException {

        throw new ServerFacadeException(500, "Not implemented.");
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public GameMetaData[] listGames() throws ServerFacadeException {
        throw new ServerFacadeException(500, "Not implemented.");
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public int createGame(int gameID) throws ServerFacadeException {
        throw new ServerFacadeException(500, "Not implemented.");
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public GameData joinGame(String playerColor, int gameID) throws ServerFacadeException {
        throw new ServerFacadeException(500, "Not implemented.");
    }

    public ChessBoard observe(String gameID) throws ServerFacadeException {
        throw new ServerFacadeException(500, "Not implemented.");
    }

    public <T> T makeHttpRequest(String method, String path, Object request, Class<T> responseClass) throws ServerFacadeException {
        try {

        } catch (ResponseException exception)
        throw new ServerFacadeException(500, "Not implemented.");
    }
}
