package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import service.exception.ResponseException;
import service.request.LogoutRequest;
import service.result.LogoutResult;

public class LogoutService extends Service {

    public LogoutService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        super(authDAO, userDAO, gameDAO);
    }

    /**
     * Logs out a user.
     * @param request the LogoutRequest passed in by the handler.
     * @return a LogoutResult with status code 200.
     */
    public LogoutResult logout(LogoutRequest request) throws ResponseException {
        // authenticateWithToken throws the errors
        AuthData authData = this.authenticateWithToken(request.getAuthToken());
        if (this.authDAO.deleteAuth(authData)){
            return new LogoutResult(200);
        }
        throw new ResponseException(500, "Error: an unknown error occurred.");
    }
}
