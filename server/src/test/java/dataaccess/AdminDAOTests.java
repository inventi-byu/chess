package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.exception.ResponseException;

import java.sql.ResultSet;

public class AdminDAOTests {

    MySQLAdminDAO adminDAO;

    @BeforeEach
    void setUp(){
        this.adminDAO = new MySQLAdminDAO();
    }

    @Test
    public void testClearDatabase(){
        adminDAO.clear();
        String[] statements = {
                "SELECT * FROM user_table;",
                "SELECT * FROM auth_table;",
                "SELECT * FROM game_table;"
        };
        for (String statement : statements){
            try (ResultSet results = DatabaseManager.queryDB(statement);){
            } catch (Exception exception){
                switch (statement){
                    case "SELECT * FROM user_table;" ->
                            Assertions.assertEquals("Failed to execute query: SELECT * FROM user_table;. " +
                                    "Error Message: " +
                                    "java.sql.SQLSyntaxErrorException: Table 'chess.user_table' doesn't exist",
                                    exception.getMessage()
                            );
                    case "SELECT * FROM auth_table;" ->
                            Assertions.assertEquals("Failed to execute query: SELECT * FROM auth_table;. " +
                                    "Error Message: " +
                                    "java.sql.SQLSyntaxErrorException: Table 'chess.auth_table' doesn't exist",
                                    exception.getMessage()
                            );
                    case "SELECT * FROM game_table;" ->
                            Assertions.assertEquals("Failed to execute query: SELECT * FROM game_table;. " +
                                    "Error Message: " +
                                    "java.sql.SQLSyntaxErrorException: Table 'chess.game_table' doesn't exist",
                                    exception.getMessage()
                            );
                }
            }
        }
    }
}
