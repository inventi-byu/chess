package dataaccess;

public class MemoryAdminDAO implements AdminDAO {
    /**
     * Clears the in-memory "database".
     * @return true if the database was successfully cleared.
     */
    public boolean clear(){
        return true;
    }
}
