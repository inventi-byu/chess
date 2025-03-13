package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import service.Service;
import spark.utils.Assert;

import java.util.ArrayList;

import static dataaccess.DatabaseManager.*;

public class AuthDAOTests {

    AuthDAO authDAO;
    UserDAO userDAO;
    Service service;

    String username;
    String password;
    String email;

    @BeforeEach
    void setup(){
        this.authDAO = new MySQLAuthDAO();
        this.userDAO = new MySQLUserDAO();
        try {
            DatabaseManager.createDatabase();
            this.username = "bobsmith100";
            this.password = "mysecurepassword";
            this.email = "bob@bob.com";
            UserData userData = new UserData(this.username, this.password, this.email);
            Assertions.assertTrue(this.userDAO.createUser(userData));
        } catch (Exception exception) {
            throw new RuntimeException(String.format("Failed to create database. Message: %s"), exception);
        }
    }

    @Test
    public void createAuthTestGoodInput(){
        String authToken = Service.generateToken();
        AuthData authData = new AuthData(authToken, this.username);
        Assertions.assertTrue(this.authDAO.createAuth(authData));

        String statement = "SELECT " +
                AUTH_TABLE_USERNAME + ", " +
                AUTH_TABLE_AUTH_TOKEN +
                " FROM " + AUTH_TABLE + ";";
        try {
            String[] expectedLabels = {AUTH_TABLE_USERNAME, AUTH_TABLE_AUTH_TOKEN};
            ArrayList<String> results = DatabaseManager.queryDB(statement, expectedLabels);
            String actualUsername = results.get(0);
            String actualAuthToken = results.get(1);
            Assertions.assertEquals(this.username, actualUsername);
            Assertions.assertEquals(authToken, actualAuthToken);
        } catch (Exception exception){
            throw new RuntimeException(String.format("createAuth Good Input Test failed. Message: %s", exception));
        }
    }

    @Test
    public void createAuthTestBadInput(){
        // User doesn't exist
        throw new RuntimeException("Not implemented.");
    }

    @Test
    public void getAuthTestGoodInput(){
        String authToken = Service.generateToken();
        AuthData authData = new AuthData(authToken, this.username);
        Assertions.assertTrue(this.authDAO.createAuth(authData));

       AuthData actualAuthData = this.authDAO.getAuth(authToken);
       Assertions.assertEquals(authData, actualAuthData);

    }

    @Test
    public void getAuthTestBadInput(){
        // Auth data doesn't exist
        String authToken = Service.generateToken();
        AuthData authData = new AuthData(authToken, this.username);
        Assertions.assertTrue(this.authDAO.createAuth(authData));

        AuthData actualAuthData = this.authDAO.getAuth("FaKeToKeN");
        Assertions.assertNull(actualAuthData);
    }

    @Test
    public void deleteAuthTestGoodInput(){
        throw new RuntimeException("Not implemented.");
    }

    @Test
    public void deleteAuthTestBadInput(){
        throw new RuntimeException("Not implemented.");
    }
}
