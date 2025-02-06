package chess;

import java.util.Collection;
import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private ChessGame.TeamColor turn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // Get the piece
        ChessPiece piece = this.getBoard().getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> moves = piece.pieceMoves(this.board, startPosition);
        if (this.isInCheckmate(this.getTeamTurn())){
            // Do something
        }
        if (this.isInCheck(this.getTeamTurn())){
            // Do something
        }
        if (this.isInStalemate(this.getTeamTurn())) {
            // Do something
        }
        // Everything ok
        for (int i = 0; i < moves.size(); i++){
            //some other stuffa
        }
        // Not completed
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        if(!this.isInCheckmate(teamColor)){
            if (this.kingIsInDanger(teamColor)){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (kingIsInDanger(teamColor)){
            // Find out where the king is, and get the king so you can call pieceMoves() on it
            ChessPosition king_position = this.board.getKingPosition(teamColor);
            ChessPiece king = this.board.getPiece(king_position);

            if (king.pieceMoves(this.board, king_position) == null){
                return true;
            }
        }
        return false;
        /*
        List attackingMoves = new ArrayList<ChessMove>();
         */

            /*
            for every enemy piece:
                do piece moves
                for every move:
                    if one of the end positions is the king
                        add that move to the list of moves


             */
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        /*
        if ( !(this.isInCheck(teamColor) && this.isInCheckmate(teamColor)) ){

            // for every friend piece:
            //    do pieceMoves
            //    if pieceMoves != null
            //        return true
            //return false;
        }
        return false;
        */
        throw new RuntimeException("isInStalemate() is not implemented!");
    }
    /**
     * Determines if a given king is in danger of being attacked
     *
     * @param teamColor which team's king to see if they are in danger
     * @return true if there is a move from the enemy that can be done against the king
    */
    private boolean kingIsInDanger(ChessGame.TeamColor teamColor){
        /*
        for every enemy piece on the board:
            run pieceMoves and figure out where they can go
            for every move it can make
                if the end position is the same as the position of the king
                    return true
         return false
         */
        throw new RuntimeException("kingIsInDanger() not implemented!");
    }



    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
