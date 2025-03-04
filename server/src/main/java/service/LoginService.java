package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import service.exception.ResponseException;
import service.request.LoginRequest;
import service.result.LoginResult;

public class LoginService extends Service{

    public LoginService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        super(authDAO, userDAO, gameDAO);
    }

    /**
     * Logs in a user by fetching a new authToken.
     * @param request the LoginRequest passed in by the handler.
     * @return a LoginResult with status code 200 and the authData returned authDAO.
     */
    public LoginResult login(LoginRequest request) throws ResponseException {
        AuthData authData = this.authenticateWithCredentials(request.getUsername(), request.getPassword());
        // authenticateWithCredentials throws the errors
        return new LoginResult(200, authData);
    }
}
