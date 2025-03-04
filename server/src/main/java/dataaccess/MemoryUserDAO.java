package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    private ArrayList<UserData> userDB;

    public MemoryUserDAO(){
        this.userDB = new ArrayList<UserData>();

    }

    public boolean createUser(UserData userData){
        this.userDB.add(userData);
        return true;
    }

    public UserData getUser(String username){
        for (UserData user : userDB){
            if (username.equals(user.username())){
                return user;
            }
        }
        return null;
    }

    public void clearUserTable(){
        this.userDB = new ArrayList<UserData>();
    }

}
