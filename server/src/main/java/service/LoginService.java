package service;

import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

public class LoginService extends Service{

    public LoginService(){}

    public LoginResult login(LoginRequest request) throws ResponseException {
        AuthData authData = this.authenticate(request.getUsername(), request.getPassword());
        if (authData != null){
            return new LoginResult(200, authData);
        }

    }
}
