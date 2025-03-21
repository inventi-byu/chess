package service;

import dataaccess.*;
import model.AuthData;
import server.service.exception.ResponseException;
import service.request.RegisterRequest;
import service.result.RegisterResult;

public class UserService extends Service {

    public UserService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        super(authDAO, userDAO, gameDAO);
    }

    /**
     * Registers a user.
     * @param request the RegisterRequest passed in by the handler.
     * @return a RegisterResult with status code 200, and the new AuthData for that user.
     * @throws ResponseException if registration failed.
     */
    public RegisterResult register(RegisterRequest request) throws ResponseException {
        String username = request.getUserData().username();
        String password = request.getUserData().password();
        String email = request.getUserData().email();

        // Make sure all the info needed to register was provided
        if(username == null || password == null || email == null){
            throw new ResponseException(400, "Error: bad request");
        }

        if (this.userDAO.getUser(username) != null) {
            throw new ResponseException(403, "Error: already taken");
        }

        this.userDAO.createUser(request.getUserData());
        AuthData newAuthData = new AuthData(this.generateToken(), username);
        this.authDAO.createAuth(newAuthData);

        return new RegisterResult(200, newAuthData);
    }
}