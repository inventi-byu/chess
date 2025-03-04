package service;
import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class Service {

    protected AuthDAO authDAO;
    protected UserDAO userDAO;
    protected GameDAO gameDAO;

    public Service(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO= gameDAO;
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData authenticateWithCredentials(String username, String password) throws ResponseException {
        UserData userData = this.userDAO.getUser(username);
        if (userData != null){
            if (userData.password().equals(password)){
                AuthData newAuthData = new AuthData(this.generateToken(), username);
                this.authDAO.createAuth(newAuthData);
                return newAuthData;
            }
            // Invalid password, not authorized
            throw new ResponseException(401, "Error: unauthorized");
        }
        throw new ResponseException(500, "Error: user could not be found");
    }

    public AuthData authenticateWithToken(String authToken) throws ResponseException {
        // Verify that the user is authenticated
        AuthData authData = this.authDAO.getAuth(authToken);

        if (authData == null) {
            // Invalid token, not authorized
            throw new ResponseException(401, "Error: unauthorized");
        }
        return authData;
        //throw new ResponseException(500, "Error: user could not be found");
        // NOTE: I don't think there is any reason for a 500 error to occur
    }
}
