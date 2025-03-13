package dataaccess;

import service.exception.ResponseException;

public class MySQLAdminDAO implements AdminDAO {
    public boolean clear(){
        try{
            DatabaseManager.clearDatabase();
            return true;
        } catch (Exception exception){
            throw new ResponseException(500, String.format("Could not clear database. Message from database: %s", exception));
        }
    }

}
