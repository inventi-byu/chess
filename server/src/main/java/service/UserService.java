package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;

public class UserService extends Service {
    public UserService(){}

    /**
     * Registers a user
     * @param request the RegisterRequest from th RegisterHandler
     * @return RegisterResult of the result if valid result returned.
     * @throws ResponseException
     */
    public RegisterResult register(RegisterRequest request) throws ResponseException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        String username = request.getUserData().username();

        if (userDAO.getUser(username) != null) {
            throw new ResponseException(403, "Error: already taken");
        }

        userDAO.createUser(request.getUserData());

        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        AuthData newAuthData = new AuthData(username, this.generateToken());
        authDAO.createAuth(newAuthData);

        return new RegisterResult(200, newAuthData);
    }
}