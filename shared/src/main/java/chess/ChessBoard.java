package chess;

import java.util.Objects;
import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ArrayList<ArrayList<ChessPiece>> board;

    public ChessBoard() {
        this.board = new ArrayList<ArrayList<ChessPiece>>(8);
        this.resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        ArrayList<ChessPiece> r = board.get(position.getRow());
        if r.get()
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board.get(position.getRow()).get(position.getColumn());
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.board = new ArrayList<ArrayList<ChessPiece>>(8);

        for(int i = 0; i < 8; i++){
            this.board.add(i, new ArrayList<ChessPiece>(8));
        }
        for (int i = 0; i < 8; i++){
            switch (i){
                case 0:
                    // Setup WSR
                    ArrayList<ChessPiece> r0 = this.board.get(i);
                    r0.add(0, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                    r0.add(1, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
                    r0.add(2, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
                    r0.add(3, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
                    r0.add(4, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
                    r0.add(5, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
                    r0.add(6, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
                    r0.add(7, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                    break;
                case 1:
                    // Setup WPR
                    ArrayList<ChessPiece> r1 = this.board.get(i);
                    for (int j = 0; j < 8; j++){
                        r1.add(j, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
                    }
                    break;
                case 6:
                    //BPR
                    ArrayList<ChessPiece> r6 = this.board.get(i);
                    for (int j = 0; j < 8; j++){
                        r6.add(j, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
                    }
                    break;
                case 7:
                    //BSR
                    ArrayList<ChessPiece> r7 = this.board.get(i);
                    r7.add(0, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
                    r7.add(1, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
                    r7.add(2, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
                    r7.add(3, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
                    r7.add(4, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
                    r7.add(5, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
                    r7.add(6, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
                    r7.add(7, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
                    break;

            }
        }

    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ChessBoard){
            ChessBoard obj =(ChessBoard)o;
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    if (this.board.get(i).get(j) != obj.board.get(i).get(j)){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(board);
    }
}


