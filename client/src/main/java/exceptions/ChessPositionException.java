package exceptions;

/**
 * A special exception class to handle invalid positions in the client code.
 */
public class ChessPositionException extends RuntimeException {
    public ChessPositionException(String message) {
        super(message);
    }
}
