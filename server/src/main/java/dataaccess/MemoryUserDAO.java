package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private ArrayList<UserData> database;

    public MemoryUserDAO(){
        this.database = new ArrayList<UserData>();
    }

    public boolean createUser(UserData userData){

    }

    public UserData getUser(String username){
        for (UserData user : database){
            if (username.equals(user.username())){
                return user;
            }
        }
        return null;
    }


}
