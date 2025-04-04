package websocket;

import chess.ChessPosition;
import websocket.exception.ChessPositionException;

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

    /**
     * Convert a ChessPosition to a location (i.e. "a1")
     * @param position the ChessPosition to convert
     * @return a String version of the position as a chess location (i.e. "a1")
     * @throws ChessPositionException
     */
    private static String positionToLocation(ChessPosition position) throws ChessPositionException {
        int row = position.getRow();
        int col = position.getColumn();
        StringBuilder sb = new StringBuilder();
        try {
            // Convert col number to letter
            switch (col) {
                case 1 -> sb.append("a");
                case 2 -> sb.append("b");
                case 3 -> sb.append("c");
                case 4 -> sb.append("d");
                case 5 -> sb.append("e");
                case 6 -> sb.append("f");
                case 7 -> sb.append("g");
                case 8 -> sb.append("h");
                default -> {
                    throw new ChessPositionException("Invalid position.");
                }
            }
            sb.append(row);
            return sb.toString();

        } catch (Exception exception) {
            throw new ChessPositionException("Invalid position");
        }
    }


}
