package service;

import dataaccess.MemoryAdminDAO;

public class ClearService extends Service {

    public ClearService(){

    }

    public ClearResult clear(ClearRequest request) throws ResponseException {
        MemoryAdminDAO adminDAO = new MemoryAdminDAO();
        adminDAO.clear();
        return new ClearResult(200, "");
    }


}
