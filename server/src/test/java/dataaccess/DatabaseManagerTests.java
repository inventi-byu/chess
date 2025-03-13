package dataaccess;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import service.exception.ResponseException;

import java.sql.ResultSet;

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
                try (ResultSet results = DatabaseManager.queryDB(statement);) {
                    // This seems trivial, but basically if no errors are thrown by those statements
                    // it means that the tables were succesfully created
                    Assertions.assertTrue(true);
                }
            }
        } catch (Exception exception) {
            throw new ResponseException(500, String.format("createDatabase Test failed! Error Message: %s", exception));
        }
    }
}
