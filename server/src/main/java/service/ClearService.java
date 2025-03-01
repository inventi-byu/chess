package service;

import dataaccess.MemoryAdminDAO;

public class ClearService {

    public ClearService(){

    }

    public ClearResult clear(ClearRequest request){
        if (request.getMethod() == RequestMethod.DELETE){
            MemoryAdminDAO adminDAO = new MemoryAdminDAO();
            if (adminDAO.clear()){
                return new ClearResult(200, "");
            };
        }
        return new ClearResult(500, "Error: Invalid method");
    }


}
