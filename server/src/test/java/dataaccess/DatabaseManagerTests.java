package dataaccess;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import service.exception.ResponseException;

import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseManagerTests {

    @Test
    public void createDatabaseTest() {
        try {
            DatabaseManager.createDatabase();
            String[] statements = {
                    "SELECT * FROM user_table;",
                    "SELECT * FROM auth_table;",
                    "SELECT * FROM game_table;"
            };
            for (String statement : statements) {
                ArrayList<String> results = DatabaseManager.queryDB(statement);
                // This seems trivial, but basically if no errors are thrown by those statements
                // it means that the tables were succesfully created
                Assertions.assertTrue(results.isEmpty());
            }
        } catch (Exception exception) {
            throw new ResponseException(500, String.format("createDatabase Test failed! Error Message: %s", exception));
        }
    }
}
