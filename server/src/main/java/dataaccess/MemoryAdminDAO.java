package dataaccess;

public class MemoryAdminDAO implements AdminDAO {

    private MemoryAuthDAO authDAO;
    private MemoryUserDAO userDAO;
    private MemoryGameDAO gameDAO;

    public MemoryAdminDAO(MemoryAuthDAO authDAO, MemoryUserDAO userDAO, MemoryGameDAO gameDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public boolean clear(){
        this.authDAO.clearAuthTable();
        this.userDAO.clearUserTable();
        this.gameDAO.clearGameTable();
        return true;
    }
}
