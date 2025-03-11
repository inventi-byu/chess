package dataaccess;
import model.UserData;

public interface UserDAO {

    /**
     * Create a new user
     * @return true if the user was created in the database
     */
    public boolean createUser(UserData userData) throws DataAccessException;

    /**
     * Retrieve a user with the given username
     * @return the UserData associated with the username.
     */
    public UserData getUser(String username);
}
