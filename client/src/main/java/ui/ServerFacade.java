package ui;

import exceptions.ServerFacadeException;
import model.GameMetaData;

public class ServerFacade {
    public ServerFacade(){
        //throw new RuntimeException("Not implemented.");
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public boolean clear() throws ServerFacadeException {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Accesses the register endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public boolean register(String username, String password, String email) throws ServerFacadeException {
        throw new RuntimeException("Not implemented.");
    }
    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public boolean login(String username, String password) throws ServerFacadeException {
        throw new RuntimeException("Not implemented.");
    }
    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public boolean logout(String authToken) throws ServerFacadeException {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public GameMetaData[] listGames() throws ServerFacadeException {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public boolean createGame() throws ServerFacadeException {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * Accesses the clear endpoint.
     * @return boolean if the clear was successful, false if it was not.
     */
    public boolean joinGame(String playerColor, String gameID) throws ServerFacadeException {
        throw new RuntimeException("Not implemented.");
    }
}
