package websocket;

import chess.ChessPosition;

public class PositionConverter {

    private static ChessPosition locationToPosition(String location) throws ChessPositionException {
        // Convert to lowercase in case
        location = location.toLowerCase();
        char[] locationAsArray = location.toCharArray();
        if (locationAsArray.length != 2){
            throw new ChessPositionException("Invalid notation.");
        }
        int row = 0;
        int col = 0;
        try {
            // Convert letter to number
            switch (locationAsArray[0]) {
                case 'a' -> col = 1;
                case 'b' -> col = 2;
                case 'c' -> col = 3;
                case 'd' -> col = 4;
                case 'e' -> col = 5;
                case 'f' -> col = 6;
                case 'g' -> col = 7;
                case 'h' -> col = 8;
                default -> {
                    throw new ChessPositionException("Invalid notation.");
                }
            }
            switch (locationAsArray[1]) {
                case '1' -> row = 1;
                case '2' -> row = 2;
                case '3' -> row = 3;
                case '4' -> row = 4;
                case '5' -> row = 5;
                case '6' -> row = 6;
                case '7' -> row = 7;
                case '8' -> row = 8;
                default -> {
                    throw new ChessPositionException("Invalid notation");
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new ChessPositionException("Invalid notation");
        }

        return new ChessPosition(row, col);
    }
    
}
