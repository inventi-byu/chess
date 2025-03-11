package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;

public class MySQLUserDao implements UserDAO {

    public boolean createUser(UserData userData){
        // Add user to the database
        // Return true

        String username = userData.username();
        String password = userData.password();
        String email = userData.email();

        // Execute SQL command to add the user to the database.
        // If succesful, return true,
        // If failed, throw database exception

        throw new RuntimeException("MySQLUserDAO not implemented.");
    };

    public UserData getUser(String username){
        // Give me the UserData for that username:
        // Perform a search by username
        // Get the user data associated with that username (username, password, email)

        throw new RuntimeException("Not implemented.");
    };

}
