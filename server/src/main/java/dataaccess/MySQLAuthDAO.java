package dataaccess;

import model.AuthData;

public class MySQLAuthDAO implements AuthDAO {

    public AuthData getAuth(String authToken){
        throw new RuntimeException("Not implemented.");
    }

    public boolean createAuth(AuthData authData){
        throw new RuntimeException("Not implemented.");};

    public boolean deleteAuth(AuthData authData){
        throw new RuntimeException("Not implemented.");
    }

    // Remember authExists is still a function
}
