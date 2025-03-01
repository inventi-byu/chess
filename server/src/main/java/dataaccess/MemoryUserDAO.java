package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO {

    public MemoryUserDAO(){}

    public boolean createUser(UserData userData){throw new RuntimeException("Not Implemented!");}

    public UserData getUser(String username){throw new RuntimeException("Not Implemented!");}


}
