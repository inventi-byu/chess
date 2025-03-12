package dataaccess;

import model.UserData;
import service.exception.ResponseException;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLUserDao implements UserDAO {

    public boolean createUser(UserData userData){
        // Add user to the database
        // Return true

        String username = userData.username();
        String hashedPassword = this.hashUserPassword(userData.password());
        String email = userData.email();

        String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        int id = this.updateDB(statement, username, hashedPassword, email);


        // Execute SQL command to add the user to the database.
        // If successful, return true,
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
