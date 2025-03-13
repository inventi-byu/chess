package dataaccess;

import model.AuthData;

import java.util.Map;
import java.util.HashMap;


public class MemoryAuthDAO implements AuthDAO {

    Map<String, String> authDB;

    public MemoryAuthDAO(){
        this.authDB = new HashMap<String, String>();
    }

    public AuthData getAuth(String authToken){
        String username = authDB.get(authToken);
        if (username != null){
            return new AuthData(authToken, username);
        }
        return null;
    };

    public boolean createAuth(AuthData authData){
        if(this.authExists(authData)){
            return false;
        }
        this.authDB.put(authData.authToken(), authData.username());
        return true;

    };

    public boolean deleteAuth(AuthData authData){
        if(this.authExists(authData)){
            this.authDB.remove(authData.authToken());
            return true;
        }
        return false;
    };

    public void clearAuthTable(){
        this.authDB = new HashMap<String, String>();
    }

}
