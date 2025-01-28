package chess;
import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {
        ArrayList<ArrayList<ChessPiece>> board = new ArrayList<ArrayList<ChessPiece>>(8);
        for (int i = 0; i < 8; i++){
            if (i==0) {
                for (int j = 0; j < 8; j++) {
                // set up bottom row of white
                }
            } else if (i==1){
                // Set up the row of pawns.
                ArrayList<ChessPiece> r1 = new ArrayList<ChessPiece>(8);
                for (int j = 0; j < 8; j++) {
                    r1.add(j, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
                }
            } else if (i==6) {
                //set up black pawn row
            } else if (i==7) {
                // set up black special row
            } else {
                // add null
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
