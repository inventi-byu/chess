package dataaccess;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import service.UserService;
import service.exception.ResponseException;
import service.request.RegisterRequest;
import service.result.RegisterResult;
import spark.utils.Assert;

import java.sql.ResultSet;
import java.util.ArrayList;

import static dataaccess.DatabaseManager.*;

// TODO: Change this code so it is not the same as UserService Tests

public class UserDAOTests {

    UserDAO userDAO;

    @BeforeEach
    void setup(){
        this.userDAO = new MySQLUserDAO();

        try {
            DatabaseManager.createDatabase();
        } catch (Exception exception) {
            throw new RuntimeException(String.format("Failed to create database. Message: %s"), exception);
        }
    }

    @Test
    public void createUserTestGoodInput(){
        String username = "bobsmith100";
        String password = "mysecurepassword";
        String email = "bob@bob.com";
        UserData userData = new UserData(username, password, email);
        Assertions.assertTrue(this.userDAO.createUser(userData));

        String statement = "SELECT " +
                USER_TABLE_USERNAME + ", " +
                USER_TABLE_PASSWORD + ", " +
                USER_TABLE_EMAIL + " FROM " +
                USER_TABLE + ";";
        try {
            String[] expectedLabels = {USER_TABLE_USERNAME, USER_TABLE_PASSWORD, USER_TABLE_EMAIL};
            ArrayList<String> results = DatabaseManager.queryDB(statement, expectedLabels);
            String actualUsername = results.get(0);
            String actualPassword = results.get(1);
            String actualEmail = results.get(2);
            Assertions.assertEquals(username, actualUsername);
            Assertions.assertTrue(BCrypt.checkpw(password, actualPassword));
            Assertions.assertEquals(email, actualEmail);
        } catch (Exception exception){
            throw new RuntimeException(String.format("createUser Good Input Test failed. Message: %s", exception));
        }

    }

    @Test
    public void createUserTestBadInput(){
        throw new RuntimeException("Not implemented.");
    }

    @Test
    public void getUserTestGoodInput(){
        String username = "bobsmith100";
        String password = "mysecurepassword";
        String email = "bob@bob.com";
        UserData userData = new UserData(username, password, email);
        Assertions.assertTrue(this.userDAO.createUser(userData));

        UserData actualUserData = this.userDAO.getUser(username);
        Assertions.assertNotNull(actualUserData);

        Assertions.assertEquals(username, actualUserData.username());
        Assertions.assertTrue(BCrypt.checkpw(password, actualUserData.password()));
        Assertions.assertEquals(email, actualUserData.email());
    }

    @Test
    public void getUserTestBadInput(){
        String username = "bobsmith100";
        String password = null;
        String email = "bob@bob.com";

        UserData userData = new UserData(username, null, email);
        Assertions.assertTrue(this.userDAO.createUser(userData));

        UserData actualUserData = this.userDAO.getUser("userThatDoesntExist");
        Assertions.assertNull(actualUserData);
    }
}
