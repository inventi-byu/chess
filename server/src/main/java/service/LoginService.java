package service;

import model.AuthData;

public class LoginService extends Service{

    public LoginService(){}

    public LoginResult login(LoginRequest request) throws ResponseException {
        AuthData authData = this.authenticateWithCredentials(request.getUsername(), request.getPassword());
        // authenticateWithCredentials throws the errors
        return new LoginResult(200, authData);
    }
}
