package dataaccess;

import service.exception.ResponseException;

public class MySQLAdminDAO implements AdminDAO {
    public boolean clear(){
        try{
            // TODO: DELETE the table as * with no where clause instead of dropping and recreating the tables
            DatabaseManager.resetDatabase();
            return true;
        } catch (Exception exception){
            throw new ResponseException(500, String.format("Could not clear database. Message from database: %s", exception));
        }
    }

}
