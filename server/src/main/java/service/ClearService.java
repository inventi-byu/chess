package service;

import dataaccess.*;
import service.exception.ResponseException;
import service.request.ClearRequest;
import service.result.ClearResult;

public class ClearService extends Service {

    private AdminDAO adminDAO;
    public ClearService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO, AdminDAO adminDAO){
        super(authDAO, userDAO, gameDAO);
        this.adminDAO = adminDAO;
    }

    public ClearResult clear(ClearRequest request) {
        this.adminDAO.clear();
        return new ClearResult(200, "");
    }
}
