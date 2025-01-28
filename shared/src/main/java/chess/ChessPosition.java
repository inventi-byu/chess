package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int r;
    private int c;
    public ChessPiece piece;

    public ChessPosition(int row, int col) {
        r = row;
        c = col;
        piece = null;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.r;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.c;
    }

    public void setPiece(ChessPiece piece){
        this.piece = piece;
    }
    public ChessPiece getPiece(ChessPiece piece){
        return this.piece;
    }
}
