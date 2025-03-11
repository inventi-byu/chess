package service;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.exception.ResponseException;

import java.util.UUID;

public class Service {

    protected AuthDAO authDAO;
    protected UserDAO userDAO;
    protected GameDAO gameDAO;

    protected AdminDAO adminDAO;

    public Service(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public Service(AdminDAO adminDAO){
        this.adminDAO = adminDAO;
    }

    /**
     * Authenticates a user by their credentials (i.e. username and password).
     * @param username the user's username as a String.
     * @param password the user's password as a String.
     * @return the AuthData associated with the user if it exists (i.e. is valid).
     * @throws ResponseException if the credentials were invalid.
     */
    public AuthData authenticateWithCredentials(String username, String password) throws ResponseException {
        UserData userData = this.userDAO.getUser(username);
        if (userData != null){
            if (BCrypt.checkpw(password, userData.password())){
                AuthData newAuthData = new AuthData(this.generateToken(), username);
                this.authDAO.createAuth(newAuthData);
                return newAuthData;
            }
            // Invalid password, not authorized
            throw new ResponseException(401, "Error: unauthorized");
        }
        throw new ResponseException(401, "Error: unauthorized");
    }

    /**
     * Authenticates a user by an authToken.
     * @param authToken the authToken allegedly associated with the user to verify.
     * @return the AuthData associated with that authToken if it exits
     * @throws ResponseException if the authToken is invalid.
     */
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

    /**
     * Generates a unique authToken for a user.
     * @return a String representing the unique authToken.
     */
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

}
