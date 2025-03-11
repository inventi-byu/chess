package dataaccess;

import model.UserData;

public class MySQLUserDao implements UserDAO {

    public boolean createUser(UserData userData);

    public UserData getUser(String username);
}

}
