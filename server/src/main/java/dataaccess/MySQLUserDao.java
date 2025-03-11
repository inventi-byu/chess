package dataaccess;

import model.UserData;
import service.exception.ResponseException;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLUserDao implements UserDAO {

    public boolean createUser(UserData userData){
        // Add user to the database
        // Return true

        String username = userData.username();
        String hashedPassword = this.hashUserPassword(userData.password());
        String email = userData.email();

        String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        int id = this.updateDB(statement, username, hashedPassword, email);


        // Execute SQL command to add the user to the database.
        // If successful, return true,
        // If failed, throw database exception

        throw new RuntimeException("MySQLUserDAO not implemented.");
    };

    /**
     * Executes SQL statement for INSERT, UPDATE, and DELETE with given parameters passed in.
     * @param statement the SQL statement with placeholders (?) for parameters (must include a table to operate on).
     * @param params the actual parameters to pass in
     * @return an int representing keys generated. If there were no keys generated return 0.
     * @throws ResponseException if the command fails.
     */
    public int updateDB(String statement, Object... params) throws ResponseException{
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
        } catch (SQLException exception){
            throw new ResponseException(500, String.format("Unable to add user. %s, %s", statement, exception));
        } catch (DataAccessException exception){
            throw new ResponseException(500, String.format("Something went wrong while accessing the database: %s, %s", statement, exception));
        }
    }

    public UserData getUser(String username){
        // Give me the UserData for that username:
        // Perform a search by username
        // Get the user data associated with that username (username, password, email)

        throw new RuntimeException("Not implemented.");
    };

}
