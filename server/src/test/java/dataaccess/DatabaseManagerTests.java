package dataaccess;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import server.service.exception.ResponseException;

import java.util.ArrayList;

import static dataaccess.DatabaseManager.*;

public class DatabaseManagerTests {

    @Test
    public void createDatabaseTest() {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.resetDatabase();
            String[] statements = {
                    "SELECT * FROM " + USER_TABLE + ";",
                    "SELECT * FROM " + AUTH_TABLE + ";",
                    "SELECT * FROM " + GAME_TABLE + ";",
            };
            for (String statement : statements) {
                ArrayList<String> results = DatabaseManager.queryDB(statement, null);
                // This seems trivial, but basically if no errors are thrown by those statements
                // it means that the tables were succesfully created
                Assertions.assertTrue(results.isEmpty());
            }
        } catch (Exception exception) {
            throw new ResponseException(500, String.format("createDatabase Test failed! Error Message: %s", exception));
        }
    }
}
