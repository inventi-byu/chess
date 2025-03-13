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
                    Assertions.assertTrue(results.next());
                }
            }
        } catch (Exception exception) {
            throw new ResponseException(500, String.format("createDatabase Test failed! Error Message: %s", exception));
        }
    }

}
