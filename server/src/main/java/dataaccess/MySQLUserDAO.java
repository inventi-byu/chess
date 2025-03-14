package dataaccess;

import model.UserData;
import service.exception.ResponseException;

import java.util.ArrayList;

import static dataaccess.DatabaseManager.*;

public class MySQLUserDAO implements UserDAO {

    public boolean createUser(UserData userData){
        // Add user to the database
        // Return true
        // Remember that the UserService handles all the logic for making sure this will be a valid thing to do

        String username = userData.username();
        String hashedPassword = this.hashUserPassword(userData.password());
        String email = userData.email();

        String statement = "INSERT INTO " +
                USER_TABLE + "(" +
                USER_TABLE_USERNAME + ", "
                + USER_TABLE_PASSWORD + ", "
                + USER_TABLE_EMAIL + ") VALUES (?, ?, ?)";

        try {
            DatabaseManager.updateDB(statement, username, hashedPassword, email);
            return true;
        } catch (DataAccessException exception){
            throw new ResponseException(500, String.format("Could not create user. Message from database: %s", exception));
        }
    }

    public UserData getUser(String username){
        // Give me the UserData for that username:
        // Perform a search by username
        // Get the user data associated with that username (username, password, email)

        String statement = "SELECT " +
                USER_TABLE_USERNAME + ", " +
                USER_TABLE_PASSWORD + ", " +
                USER_TABLE_EMAIL +
                " FROM " + USER_TABLE +
                " WHERE " + USER_TABLE_USERNAME + "=?;";

        try {
            String[] expectedLabels = {USER_TABLE_USERNAME, USER_TABLE_PASSWORD, USER_TABLE_EMAIL};
            ArrayList<String> data = DatabaseManager.queryDB(statement, expectedLabels, username);

            if(data.isEmpty()){
                return null;
            }

            return new UserData(
                    data.get(0),
                    data.get(1),
                    data.get(2)
            );
        } catch (DataAccessException exception){
            throw new ResponseException(500, String.format("Error: Something went wrong. Message: %s", exception));
        }
    }

}
