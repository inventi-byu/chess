package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;

public class MySQLUserDao implements UserDAO {

    public boolean createUser(UserData userData) throws DataAccessException{
        // Add user to the database
        // Return true

        String username = userData.username();
        String hashedPassword = this.hashUserPassword(userData.password());
        String email = userData.email();

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

    /**
     * Hashes a user's password with the bcyrpt algorithm for storing the database.
     * @param password the user's password as a String
     * @return the hashed password as a String
     */
    public String hashUserPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
