package dataaccess;

import model.AuthData;
import service.exception.ResponseException;

import java.util.ArrayList;

import static dataaccess.DatabaseManager.*;

public class MySQLAuthDAO implements AuthDAO {

    public AuthData getAuth(String authToken){
        String statement = "SELECT " + AUTH_TABLE_USERNAME + ", " + AUTH_TABLE_AUTH_TOKEN + " FROM " + AUTH_TABLE + " WHERE " + AUTH_TABLE_AUTH_TOKEN + "=?;";

        try {
            String[] expectedLabels = {AUTH_TABLE_USERNAME, AUTH_TABLE_AUTH_TOKEN};
            ArrayList<String> result = DatabaseManager.queryDB(statement, expectedLabels, authToken);
            if (!result.isEmpty()){
                return new AuthData(result.get(1), result.get(0));
            }
            return null;
        } catch (DataAccessException exception){
            throw new ResponseException(500, String.format("Could not get auth. Message from database: %s", exception));
        }
    }

    public boolean createAuth(AuthData authData){
        if(this.authExists(authData)){
            return false;
        }
        String statement = "INSERT INTO " + AUTH_TABLE + " (" + AUTH_TABLE_USERNAME + ", " + AUTH_TABLE_AUTH_TOKEN + ") VALUES (?, ?);";

        try {
            DatabaseManager.updateDB(statement, authData.username(), authData.authToken());
            return true;
        } catch (DataAccessException exception){
            throw new ResponseException(500, String.format("Could not create auth. Message from database: %s", exception));
        }
    };

    public boolean deleteAuth(AuthData authData){
        if(this.authExists(authData)){
            String statement = "DELETE FROM " + AUTH_TABLE + " WHERE " + AUTH_TABLE_USERNAME + "=?;";

            try {
                DatabaseManager.updateDB(statement, authData.username());
                return true;
            } catch (DataAccessException exception){
                throw new ResponseException(500, String.format("Could not delete auth. Message from database: %s", exception));
            }
        }
        return false;
    }

    // Remember authExists is still a function
}
