package dataaccess;

import service.exception.ResponseException;

import java.sql.*;
import java.util.Properties;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Executes SQL statement for INSERT, UPDATE, and DELETE with given parameters passed in.
     * @param statement the SQL statement with placeholders (?) for parameters (must include a table to operate on).
     * @param params the actual parameters to pass in
     * @return an int representing keys generated. If there were no keys generated return 0.
     * @throws ResponseException if the command fails.
     */
    static int updateDB(String statement, Object... params) throws DataAccessException{
        try(var connection = DatabaseManager.getConnection()){
            try(var db = connection.prepareStatement(statement, RETURN_GENERATED_KEYS)){
                for (int i = 0; i < params.length; i++){
                    var param = params[i];
                    if (param instanceof String obj) db.setString(i+1, obj);
                    else if (param instanceof Integer obj) db.setInt(i+1, obj);
                    else if (param == null) db.setNull(i+1, NULL);
                }
                db.executeUpdate();

                var genKeys = db.getGeneratedKeys();
                // If something was produced.
                if(genKeys.next()){
                    return genKeys.getInt(1); // SQL is 1-indexed, not 0-indexed
                }
                return 0;
            }
        } catch (Exception exception){
            throw new DataAccessException(String.format("Failed to execute update: %s. Error Message: %s", statement, exception));
        }
    }

    static ResultSet queryDB(String statement, Object... params) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var db = connection.prepareStatement(statement)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String obj) db.setString(i + 1, obj);
                    else if (param instanceof Integer obj) db.setInt(i + 1, obj);
                    else if (param == null) db.setNull(i + 1, NULL);
                }
                try (var response = db.executeQuery()) {
                    assert response != null;
                    return response;
                }
            }
        } catch (Exception exception) {
            throw new DataAccessException(String.format("Failed to execute query: %s. Error Message: %s", statement, exception));
        }
    }
}
