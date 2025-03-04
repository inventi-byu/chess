package dataaccess;

public class AuthDoesNotExistException extends DataAccessException{
    public AuthDoesNotExistException(String message) {
        super(message);
    }
}
