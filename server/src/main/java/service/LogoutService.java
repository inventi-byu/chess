package service;

import model.AuthData;

public class LogoutService extends Service {
    public LogoutService(){}

    public LogoutResult logout(LogoutRequest request) throws ResponseException {
        AuthData authData = this.authenticateWithToken(request.getAuthToken());
        // authenticateWithCredentials throws the errors
        return new LoginResult(200, authData);
    }
}
