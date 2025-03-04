package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.UserDAO;
import model.AuthData;

public class LogoutService extends Service {
    public LogoutService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        super(authDAO, userDAO, gameDAO);
    }

    public LogoutResult logout(LogoutRequest request) throws ResponseException {
        AuthData authData = this.authenticateWithToken(request.getAuthToken());
        // authenticateWithCredentials throws the errors
        if (this.authDAO.deleteAuth(authData)){
            return new LogoutResult(200);
        }
        throw new ResponseException(500, "Error: an unknown error occured.");
    }
}
