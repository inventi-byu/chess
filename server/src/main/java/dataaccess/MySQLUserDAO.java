package dataaccess;

import model.UserData;
import service.exception.ResponseException;

import java.util.ArrayList;

public class MySQLUserDAO implements UserDAO {

    public boolean createUser(UserData userData){
        // Add user to the database
        // Return true
        // Remember that the UserService handles all the logic for making sure this will be a valid thing to do

        String username = userData.username();
        String hashedPassword = this.hashUserPassword(userData.password());
        String email = userData.email();

        String statement = "INSERT INTO user_table (username, password, email_address) VALUES (?, ?, ?)";

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

        String statement = "SELECT username, password, email_address FROM user_table WHERE username=?";

        try {
            String[] expectedLabels = {"username", "password", "email_address"};
            ArrayList<String> data = DatabaseManager.queryDB(statement, expectedLabels, username);
            return new UserData(
                    data.get(0),
                    data.get(1),
                    data.get(2)
            );
        } catch (DataAccessException exception){
            return null;
        }
    }

}
