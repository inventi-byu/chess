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
                // CHECK THIS -> is an empty ArrayList or Collection considered null? If not, the if logic above this line will not work!
                return true;
            }
            // Must handle other cases
            // Check to see if piecemoves given put you in danger
        }
        return false;
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
     * A function that returns a list of enemyMoves to a specific point.
     * @param teamColor ChessGame.TeamColor - the piece's color (will get the enemy of this piece's moves).
     * @param position ChessPosition - position potentially under attack you want to check.
     * @returns An ArrayList of all moves of the enemy of teamColor that end at that position.
     */
    public List<ChessMove> getEnemyMoves(TeamColor teamColor, ChessPosition position){

        throw new RuntimeException("getEnemyMoves() not implemented!");
    }


    /**
     * Determines if a given king is in danger of being attacked
     *
     * @param teamColor which team's king to see if they are in danger
     * @return true if there is a move from the enemy that can be done against the king
    */
    private boolean kingIsInDanger(ChessGame.TeamColor teamColor){
        ChessPosition king_pos = this.board.getKingPosition(teamColor);
        // We'll need to use a king piece, but not put it on the board just to refer to it for isEnemy()
        ChessPiece king_copy = new ChessPiece(teamColor, ChessPiece.PieceType.KING);

        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j ++) {
                ChessPosition cur_pos = new ChessPosition(i, j);
                ChessPiece piece = this.board.getPiece(cur_pos);
                if(piece != null && piece.isEnemy(king_copy)) {
                    Collection<ChessMove> moves = piece.pieceMoves(this.board, cur_pos);
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(king_pos)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
