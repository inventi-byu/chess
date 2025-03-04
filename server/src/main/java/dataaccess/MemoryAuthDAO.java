package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;


public class MemoryAuthDAO implements AuthDAO {

    Map<String, String> authDB;

    public MemoryAuthDAO(){
        this.authDB = new HashMap<String, String>();
    }

    /**
     * Retrives the AuthData associated with the authToken
     * @param authToken the token associated with the AuthData
     * @return the AuthData associated with the token
     */
    public AuthData getAuth(String authToken){
        String username = authDB.get(authToken);
        if (username != null){
            return new AuthData(authToken, username);
        }
        return null;
    };


    /**
     * Adds an AuthData to the database.
     * @param authData the AuthData to add
     * @return true if the AuthData was added to the database
     */
    public boolean createAuth(AuthData authData){
        if(this.authExists(authData)){
            return false;
        }
        this.authDB.put(authData.authToken(), authData.username());
        return true;

    };

    /**
     * Removes an AuthData from the database
     * @param authData the AuthData to remove
     * @return true if the AuthData was successfully removed, return false if nothing to remove
     */
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
