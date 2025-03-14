package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Service;
import service.exception.ResponseException;

import java.util.ArrayList;

import static dataaccess.DatabaseManager.*;

public class AuthDAOTests {

    AuthDAO authDAO;
    UserDAO userDAO;

    String username;
    String password;
    String email;
    String authToken;


    private boolean assertDataPersisted(){
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
            Assertions.assertEquals(this.authToken, actualAuthToken);
        } catch (Exception exception){
            throw new RuntimeException(String.format("assertDataPersisted failed. Message: %s", exception));
        }
        return true;
    }

    @BeforeAll
    static void setupDatabase(){
        try {
            DatabaseManager.createDatabase();
        } catch (Exception exception) {
            throw new RuntimeException("Could not create database. Message: ", exception);
        }
    }

    @BeforeEach
    void setup(){
        this.authDAO = new MySQLAuthDAO();
        this.userDAO = new MySQLUserDAO();
        try {
            DatabaseManager.clearDatabase();
            this.username = "bobsmith100";
            this.password = "mysecurepassword";
            this.email = "bob@bob.com";
            this.authToken = Service.generateToken();
            UserData userData = new UserData(this.username, this.password, this.email);
            Assertions.assertTrue(this.userDAO.createUser(userData));
        } catch (Exception exception) {
            throw new RuntimeException(String.format("Failed to create database. Message: %s", exception));
        }
    }

    @Test
    public void createAuthTestGoodInput(){
        AuthData authData = new AuthData(this.authToken, this.username);
        Assertions.assertTrue(this.authDAO.createAuth(authData));

        // Make sure the Auth Data is in the database
        Assertions.assertTrue(this.assertDataPersisted());
    }

    @Test
    public void createAuthTestBadInput(){
        AuthData authData = new AuthData(this.authToken, null);

        ResponseException exception  = Assertions.assertThrows(ResponseException.class, () -> this.authDAO.createAuth(authData));
        Assertions.assertEquals("Could not create auth. Message from database: " +
                        "dataaccess.DataAccessException: Failed to execute update: " +
                        "INSERT INTO auth_table (username, auth_token) VALUES (?, ?);. " +
                        "Error Message: java.sql.SQLIntegrityConstraintViolationException: " +
                        "Column 'username' cannot be null",
                exception.getMessage()
        );

        // Just do a double check on the database to see if anything was added (there should be nothing there)
        String statement = "SELECT " +
                AUTH_TABLE_USERNAME + ", " +
                AUTH_TABLE_AUTH_TOKEN +
                " FROM " + AUTH_TABLE + ";";
        try {
            String[] expectedLabels = {AUTH_TABLE_USERNAME, AUTH_TABLE_AUTH_TOKEN};
            ArrayList<String> results = DatabaseManager.queryDB(statement, expectedLabels);
            Assertions.assertTrue(results.isEmpty());
        } catch (Exception ex){
            throw new RuntimeException(String.format("createAuth Bad Input Test failed. Message: %s", ex));
        }
    }

    @Test
    public void getAuthTestGoodInput(){
        AuthData authData = new AuthData(this.authToken, this.username);
        Assertions.assertTrue(this.authDAO.createAuth(authData));

       AuthData actualAuthData = this.authDAO.getAuth(authToken);
       Assertions.assertEquals(authData, actualAuthData);

    }

    @Test
    public void getAuthTestBadInput(){
        // Auth data doesn't exist
        AuthData authData = new AuthData(this.authToken, this.username);
        Assertions.assertTrue(this.authDAO.createAuth(authData));

        AuthData actualAuthData = this.authDAO.getAuth("FaKeToKeN");
        Assertions.assertNull(actualAuthData);
    }

    @Test
    public void deleteAuthTestGoodInput(){
        AuthData authData = new AuthData(this.authToken, this.username);
        Assertions.assertTrue(this.authDAO.createAuth(authData));

        Assertions.assertTrue(this.authDAO.deleteAuth(authData));

        // Just do a double check on the database to see if anything was added (there should be nothing there)
        String statement = "SELECT " +
                AUTH_TABLE_USERNAME + ", " +
                AUTH_TABLE_AUTH_TOKEN +
                " FROM " + AUTH_TABLE + ";";
        try {
            String[] expectedLabels = {AUTH_TABLE_USERNAME, AUTH_TABLE_AUTH_TOKEN};
            ArrayList<String> results = DatabaseManager.queryDB(statement, expectedLabels);
            Assertions.assertTrue(results.isEmpty());
        } catch (Exception ex) {
            throw new RuntimeException(String.format("deleteAuth Good Input Test failed. Message: %s", ex));
        }
    }

    @Test
    public void deleteAuthTestBadInput(){
        // Auth doesn't exist
        AuthData authData = new AuthData(this.authToken, this.username);
        Assertions.assertTrue(this.authDAO.createAuth(authData));

        AuthData nonExistentAuthData = new AuthData("FaKeToKeN", "fakeUsername");
        Assertions.assertFalse(this.authDAO.deleteAuth(nonExistentAuthData));

        // Make sure the Auth Data is still in the database
        Assertions.assertTrue(this.assertDataPersisted());
    }
}
