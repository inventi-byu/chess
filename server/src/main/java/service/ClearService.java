package service;

import dataaccess.MemoryAdminDAO;

public class ClearService {

    public ClearService(){

    }

    public ClearResult clear(ClearRequest request) {
        MemoryAdminDAO adminDAO = new MemoryAdminDAO();
        if (adminDAO.clear()){
            return new ClearResult(200, "");
        }
        // TODO: Fix this error result logic. The test page is not showing it correctly.
        return new ClearResult(500, "Error: Invalid method");
        /*
        if (request.getRequestMethod().equals("DELETE")){
            MemoryAdminDAO adminDAO = new MemoryAdminDAO();
            if (adminDAO.clear()){
                return new ClearResult(200, "");
            };
        }
        return new ClearResult(500, "Error: Invalid method");
        */
    }


}
