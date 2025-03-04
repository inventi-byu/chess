package service;

import dataaccess.MemoryAuthDAO;
import model.AuthData;

public class LogoutService extends Service {
    public LogoutService(){}

    public LogoutResult logout(LogoutRequest request) throws ResponseException {
        AuthData authData = this.authenticateWithToken(request.getAuthToken());
        // authenticateWithCredentials throws the errors
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        if (authDAO.deleteAuth(authData)){
            return new LogoutResult(200);
        }
        throw new ResponseException(500, "Error: an unknown error occured.");
    }
}
