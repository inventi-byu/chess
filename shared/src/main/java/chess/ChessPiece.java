package chess;

import java.util.Collection;
import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor color;
    ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public boolean isEnemy(ChessPiece piece){
        return this.getTeamColor() != piece.getTeamColor();
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculator calc = new PieceMovesCalculator(board, this, myPosition);
        return calc.moves();
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ChessPiece){
            ChessPiece obj = (ChessPiece)o;
            return this.color == obj.color && this.type == obj.type;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(null);
    }

    @Override
    public String toString() {
        return "ChessPiece{" + color + " " + type +
                '}';
    }
}

