package dataaccess;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public interface UserDAO {

    /**
     * Create a new user
     * @return true if the user was created in the database
     */
    public boolean createUser(UserData userData);

    /**
     * Retrieve a user with the given username
     * @return the UserData associated with the username.
     */
    public UserData getUser(String username);

    /**
     * Hashes a user's password with the bcyrpt algorithm for storing the database.
     * @param password the user's password as a String
     * @return the hashed password as a String
     */
    public default String hashUserPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
