package service;

import dataaccess.*;

public class ClearService extends Service {

    private AdminDAO adminDAO;
    public ClearService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO, AdminDAO adminDAO){
        super(authDAO, userDAO, gameDAO);
    }

    public ClearResult clear(ClearRequest request) throws ResponseException {
        this.adminDAO.clear();
        return new ClearResult(200, "");
    }
}
