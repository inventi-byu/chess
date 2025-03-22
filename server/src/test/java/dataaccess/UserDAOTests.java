package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import server.service.exception.ResponseException;
import java.util.ArrayList;

import static dataaccess.DatabaseManager.*;

public class UserDAOTests {

    UserDAO userDAO;

    @BeforeEach
    void setup(){
        this.userDAO = new MySQLUserDAO();

        try {
            DatabaseManager.createDatabase();
            DatabaseManager.resetDatabase();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to create database. Message: %s", exception);
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
        String username = null;
        String password = "mysecurepassword";
        String email = "bob@bob.com";
        UserData userData = new UserData(username, password, email);
        ResponseException exception  = Assertions.assertThrows(ResponseException.class, () -> this.userDAO.createUser(userData));
        Assertions.assertEquals("Could not create user. Message from database: " +
                "dataaccess.DataAccessException: Failed to execute update: INSERT INTO user_table(username, password, email_address) " +
                "VALUES (?, ?, ?). Error Message: java.sql.SQLIntegrityConstraintViolationException: " +
                "Column 'username' cannot be null",
                exception.getMessage());

        // Just do a double check on the database to see if anything was added (there should be nothing there)
        String statement = "SELECT " +
                USER_TABLE_USERNAME + ", " +
                USER_TABLE_PASSWORD + ", " +
                USER_TABLE_EMAIL + " FROM " +
                USER_TABLE + ";";
        try {
            String[] expectedLabels = {USER_TABLE_USERNAME, USER_TABLE_PASSWORD, USER_TABLE_EMAIL};
            ArrayList<String> results = DatabaseManager.queryDB(statement, expectedLabels);
            Assertions.assertTrue(results.isEmpty());
        } catch (Exception ex){
            throw new RuntimeException(String.format("createUser Bad Input Test failed. Message: %s", ex));
        }
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
        String password = "mysecurepassword";
        String email = "bob@bob.com";

        UserData userData = new UserData(username, null, email);
        Assertions.assertTrue(this.userDAO.createUser(userData));

        UserData actualUserData = this.userDAO.getUser("userThatDoesntExist");
        Assertions.assertNull(actualUserData);
    }
}
