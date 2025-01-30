package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    public ChessPosition start;
    public ChessPosition end;
    public ChessPiece.PieceType promp;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.start = startPosition;
        this.end = endPosition;
        this.promp = promotionPiece;
    }
    public enum Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT,
        DIAGur,
        DIAGul,
        DIAGdl,
        DIAGdr,
        Lur,
        Lul,
        Lrr,
        Lrl,
        Ldr,
        Ldl,
        Llr,
        Lll,
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return this.promp;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ChessMove){
            ChessMove obj = (ChessMove)o;
            return obj.start.equals(this.start) && obj.end.equals(this.end) && (obj.promp == this.promp);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.start, this.end, this.promp);
    }
}


