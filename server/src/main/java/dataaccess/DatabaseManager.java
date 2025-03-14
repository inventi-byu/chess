package dataaccess;

import service.exception.ResponseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;


    // These are constants to use in SQL statements so the implementation below can be changed
    // Without having to alter the code
    public static final String USER_TABLE = "user_table";
    public static final String AUTH_TABLE = "auth_table";
    public static final String GAME_TABLE = "game_table";

    public static final String USER_TABLE_USERNAME = "username";
    public static final String USER_TABLE_PASSWORD = "password";
    public static final String USER_TABLE_EMAIL = "email_address";
    public static final String AUTH_TABLE_USERNAME = USER_TABLE_USERNAME;
    public static final String AUTH_TABLE_AUTH_TOKEN = "auth_token";
    public static final String GAME_TABLE_GAME_ID = "game_id";
    public static final String GAME_TABLE_WHITE_USERNAME = "white_username";
    public static final String GAME_TABLE_BLACK_USERNAME = "black_username";
    public static final String GAME_TABLE_GAME_NAME = "game_name";
    public static final String GAME_TABLE_GAME = "game";

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
    public static void createDatabase() throws DataAccessException {
        try {
            // Create the database
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }

            // Check to see if there is stuff in the database or not
            String[] checkStatements = {
                    "SELECT * FROM user_table;",
                    "SELECT * FROM auth_table;",
                    "SELECT * FROM game_table;"
            };
            for (String checkStatement : checkStatements) {
                try {
                    String[] labels = {};
                    ArrayList<String> results = DatabaseManager.queryDB(checkStatement, labels);
                } catch (Exception exception) {
                    DatabaseManager.resetDatabase();
                    conn.close();
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static void resetDatabase() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();){
            // Drop all the tables
            for (String createStatement : DatabaseManager.CREATE_STATEMENTS){
                try (var preparedStatement = conn.prepareStatement(createStatement)){
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
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
                    if (param instanceof String obj){
                        db.setString(i+1, obj);
                    } else if (param instanceof Integer obj) {
                        db.setInt(i+1, obj);
                    } else if (param == null) {
                        db.setNull(i+1, NULL);
                    }
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

    /**
     * Makes a query to the database and returns any results as string (methods that call this
     * must handle converting strings into whatever datatype they need.
     * @param statement the SQL command statement to run.
     * @param labels the column labels expected in the result.
     * @param params the parameters to fill in where wildcards were left in the SQL statement, in order of appearance
     * @return an ArrayList<String> of all the results from the result set, irrespective of their actual datatype.
     * @throws DataAccessException if there was en exception thrown while trying to access the database with these commands.
     */
    static ArrayList<String> queryDB(String statement, String[] labels,  Object... params) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var db = connection.prepareStatement(statement)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String obj) {
                        db.setString(i + 1, obj);
                    } else if (param instanceof Integer obj) {
                        db.setInt(i + 1, obj);
                    } else if (param == null) {
                        db.setNull(i + 1, NULL);
                    }
                }
                try (var response = db.executeQuery()) {
                    assert response != null;
                    ArrayList<String> results = new ArrayList<String>();
                    while(response.next()){
                        for (String label : labels) {
                            results.add(response.getString(label));
                        }
                    }
                    return results;
                }
            }
        } catch (Exception exception) {
            throw new DataAccessException(String.format("Failed to execute query: %s. Error Message: %s", statement, exception));
        }
    }


    static private final String[] CREATE_STATEMENTS = {
            "USE " + DATABASE_NAME,
            "DROP TABLE IF EXISTS " + AUTH_TABLE + ";",
            "DROP TABLE IF EXISTS " + GAME_TABLE + ";",
            "DROP TABLE IF EXISTS " + USER_TABLE + ";",
            "CREATE TABLE user_table(" +
                    USER_TABLE_USERNAME + " VARCHAR(255) NOT NULL PRIMARY KEY," +
                    USER_TABLE_PASSWORD + " VARCHAR(255) NOT NULL," +
                    USER_TABLE_EMAIL + " VARCHAR(255) NOT NULL" +
                    ");",
            "CREATE TABLE auth_table(" +
                    AUTH_TABLE_USERNAME + " VARCHAR(255) NOT NULL," +
                    AUTH_TABLE_AUTH_TOKEN + " VARCHAR(255) NOT NULL," +
                    "FOREIGN KEY(" + AUTH_TABLE_USERNAME + ") REFERENCES user_table(" + USER_TABLE_USERNAME + ")" +
                    ");",
            "CREATE TABLE game_table(" +
                    GAME_TABLE_GAME_ID + " INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                    GAME_TABLE_WHITE_USERNAME + " VARCHAR(255)," +
                    GAME_TABLE_BLACK_USERNAME + " VARCHAR(255)," +
                    GAME_TABLE_GAME_NAME + " VARCHAR(255) NOT NULL," +
                    GAME_TABLE_GAME + " MEDIUMTEXT," +
                    "FOREIGN KEY(" + GAME_TABLE_WHITE_USERNAME +  ") REFERENCES user_table(" + USER_TABLE_USERNAME + ")," +
                    "FOREIGN KEY(" + GAME_TABLE_BLACK_USERNAME + ") REFERENCES user_table(" + USER_TABLE_USERNAME + ")" +
                    ");"
    };
}
