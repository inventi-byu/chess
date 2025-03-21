package chess;

import java.util.Collection;
import java.util.*;

import static chess.ChessGame.TeamColor.WHITE;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;
    // boolean hasMoved; // Has piece moved since start of game?

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
        // this.hasMoved = false;
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
     * A method to set the piece type exclusively for promotions.
     * @param type PieceType to set the type to.
     */
    public void setPieceType(ChessPiece.PieceType type){
        this.type = type;
    }

    /**
     * Tells if the given piece is an enemy (has a different team color) to this piece.
     * @param piece the piece to check for animosity.
     * @return true if the piece is an enemy (has a different team color).
     */
    public boolean isEnemy(ChessPiece piece){
        return this.getTeamColor() != piece.getTeamColor();
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     * @param board The ChessBoard on which the piece is located
     * @param myPosition The ChessPosition of the piece on that ChessBoard.
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculator calc = new PieceMovesCalculator(board, this, myPosition);
        return calc.moves();
    }

    /**
     * Draws the piece as a string to display on a ChessBoard graphic.
     * @return a String representing the piece to display on a ChessBoard.
     */
    public String draw(){
        String piece = "";
        if(this.getTeamColor() == WHITE){
            switch (this.getPieceType()) {
                case KING -> piece = "K";
                case QUEEN -> piece = "Q";
                case BISHOP -> piece = "B";
                case KNIGHT -> piece = "N";
                case ROOK -> piece = "R";
                case PAWN -> piece = "P";

            }
        } else {
            switch (this.getPieceType()) {
                case KING -> piece = "k";
                case QUEEN -> piece = "q";
                case BISHOP -> piece = "b";
                case KNIGHT -> piece = "n";
                case ROOK -> piece = "r";
                case PAWN -> piece = "p";
            }
        }

        return piece;
    };

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
        return "ChessPiece{" + this.color + " " + this.type +
                '}';
    }
}

