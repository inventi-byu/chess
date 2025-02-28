package dataaccess;
import model.AuthData;

public interface AuthDAO {
    /**
     * Retrives the AuthData associated with the authToken
     * @param authToken the token associated with the AuthData
     * @return the AuthData associated with the token
     */
    public AuthData getAuth(String authToken);

    /**
     * Adds an AuthData to the database.
     * @param authData the AuthData to add
     * @return true if the AuthData was added to the database
     */
    public boolean createAuth(AuthData authData);

    /**
     * Removes an AuthData from the database
     * @param authData the AuthData to remove
     * @return true if the AuthData was successfully removed
     */
    public boolean deleteAuth(AuthData authData);

}
