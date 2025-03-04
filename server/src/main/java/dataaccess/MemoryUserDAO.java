package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {

    private ArrayList<UserData> database;

    public MemoryUserDAO(){
        this.database = new ArrayList<UserData>();
        //this.database.add(new UserData("username", "password", "email"));

    }

    public boolean createUser(UserData userData){
        this.database.add(userData);
        return true;
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
