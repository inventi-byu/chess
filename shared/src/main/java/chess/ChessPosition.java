package chess;

import java.util.*;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    int r;
    int c;

    public ChessPosition(int row, int col) {
        this.r = row;
        this.c = col;
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

    @Override
    public boolean equals(Object o){
        if(o instanceof ChessPosition){
            ChessPosition obj = (ChessPosition)o;
            return obj.r == this.r && obj.c == this.c;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.r, this.c);
    }
}
