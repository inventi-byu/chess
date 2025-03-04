package service;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class Service {
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData authenticate(String username, String password) throws ResponseException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        UserData userData = userDAO.getUser(username);
        if (userData != null){
            if (userData.password().equals(password)){
                AuthData newAuthData = new AuthData(username, this.generateToken());
                return newAuthData;
            }
            throw new ResponseException(401, "Error: unauthorized");
            // Invalid password
        }
    }
}
