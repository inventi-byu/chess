package dataaccess;

import model.UserData;
import service.exception.ResponseException;

public class MySQLUserDAO implements UserDAO {

    public boolean createUser(UserData userData){
        // Add user to the database
        // Return true

        String username = userData.username();
        String hashedPassword = this.hashUserPassword(userData.password());
        String email = userData.email();

        String statement = "INSERT INTO user_table (username, password, email) VALUES (?, ?, ?)";

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

        String statement = "SELECT username, password, email FROM user_table WHERE username=?";

        try {
            var data = DatabaseManager.queryDB(statement, username);
            // TODO: convert the data into UserData;
            // Return the user data
            throw new RuntimeException("Converting user data not implemented.");

        } catch (DataAccessException exception){
            // TODO: This may just need to be return null to work with userservice code
            throw new ResponseException(500, String.format("Could not find. Message from database: %s", exception));
        }

    };

}
