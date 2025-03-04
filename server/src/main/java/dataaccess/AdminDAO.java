package dataaccess;

import service.exception.ResponseException;

public interface AdminDAO {

    /**
     * Clears the database for testing purposes (no authorization required).
     * @return true if the database was successfully cleared.
     */
    public boolean clear();
}

