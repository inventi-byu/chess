package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import service.request.LoginRequest;
import service.result.LoginResult;

public class LoginService extends Service{

    public LoginService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        super(authDAO, userDAO, gameDAO);
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        AuthData authData = this.authenticateWithCredentials(request.getUsername(), request.getPassword());
        // authenticateWithCredentials throws the errors
        return new LoginResult(200, authData);
    }
}
