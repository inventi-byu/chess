package service;

import dataaccess.MemoryAdminDAO;

public class ClearService {

    public ClearService(){

    }

    public ClearResult clear(ClearRequest request) throws ResponseException {
        MemoryAdminDAO adminDAO = new MemoryAdminDAO();
        adminDAO.clear();
        return new ClearResult(200, "");
    }


}
