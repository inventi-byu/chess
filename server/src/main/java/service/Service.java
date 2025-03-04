package service;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class Service {
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData authenticateWithCredentials(String username, String password) throws ResponseException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        UserData userData = userDAO.getUser(username);
        if (userData != null){
            if (userData.password().equals(password)){
                AuthData newAuthData = new AuthData(username, this.generateToken());
                MemoryAuthDAO authDAO = new MemoryAuthDAO();
                authDAO.createAuth(newAuthData);
                return newAuthData;
            }
            // Invalid password, not authorized
            throw new ResponseException(401, "Error: unauthorized");
        }
        throw new ResponseException(500, "Error: user could not be found");
    }

    public AuthData authenticateWithToken(String authToken) throws ResponseException {
        // Verify that the user is authenticated
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        AuthData authData = authDAO.getAuth(authToken);

        if (authData == null) {
            // Invalid token, not authorized
            throw new ResponseException(401, "Error: unauthorized");
        }
        return authData;
        //throw new ResponseException(500, "Error: user could not be found");
        // NOTE: I don't think there is any reason for a 500 error to occur
    }
}
