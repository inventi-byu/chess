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
            /*
                Do piece moves
                For every move
                    if the board created by that move is NOT in check or checkmate
                        add that move to valid moves
             */

            // Get the piece
            ChessPiece piece = this.getBoard().getPiece(startPosition);
            if (piece == null) {
                return null;
            }

            Collection<ChessMove> validMoves = new ArrayList<ChessMove>();
            Collection<ChessMove> pieceMoves = piece.pieceMoves(this.board, startPosition);

            for (ChessMove move : pieceMoves){
                // Get the endPos, and move the piece to its potential position after this move.
                // Then find the piece's king on the possibleBoard.
                ChessPosition endPos = move.getEndPosition();
                ChessBoard possibleBoard = this.getPossibleBoard(startPosition, endPos);
                ChessPosition kingPos = possibleBoard.getKingPosition(piece.getTeamColor());
                // If that move did not put the king in danger, it is valid
                if (!possibleBoard.pieceIsInDanger(kingPos)){
                    validMoves.add(move);
                }
            }
            return validMoves;
        }

        /**
         * Makes a move in a chess game
         *
         * @param move chess move to perform
         * @throws InvalidMoveException if move is invalid
         */
        public void makeMove(ChessMove move) throws InvalidMoveException {
            /*
            Get the piece at the start location
            If the piece's color is not having it's turn
                throw invalid move exception
            run valid moves on the piece
            if move is not in valid moves
                throw invalid move exception
            get the possible board with moving that piece
            set this.board to the possible board
             */
            TeamColor curTeamTurn = this.getTeamTurn();
            ChessPosition pieceStartPos = move.getStartPosition();
            ChessPiece piece = this.board.getPiece(pieceStartPos);
            if (piece == null){
                throw new InvalidMoveException("There is no piece there.");
            }
            if (piece.getTeamColor() != curTeamTurn){
                throw new InvalidMoveException("It is not this team's turn!");
            }

            Collection<ChessMove> validMoves = this.validMoves(pieceStartPos);

            boolean isAValidMove = false;
            for (ChessMove validMove : validMoves){
                if (move.equals(validMove)){
                    isAValidMove = true;
                }
            }
            // If it's not a valid move, throw an error
            if (!isAValidMove){
                throw new InvalidMoveException("That is not a valid move for this piece!");
            }

            ChessPiece.PieceType promp = move.getPromotionPiece();
            if (promp != null){
                piece.setPieceType(promp);
            }

            ChessBoard newBoard = this.getPossibleBoard(pieceStartPos, move.getEndPosition());
            this.board = newBoard;
            if (curTeamTurn == TeamColor.WHITE){
                this.setTeamTurn(TeamColor.BLACK);
            } else {
                this.setTeamTurn(TeamColor.WHITE);
            }
        }

        /**
         * Determines if the given team is in check
         *
         * @param teamColor which team to check for check
         * @return True if the specified team is in check
         */
        public boolean isInCheck(TeamColor teamColor) {
            if (this.board.pieceIsInDanger(this.board.getKingPosition(teamColor))){
                return true;
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
            /*
            if the king is in check
            and no king move is safe
            return true
            otherwise
            return false
             */

            if (!this.isInCheck(teamColor)) {
                return false;
            }

            if (this.safeMoves(this.board.getKingPosition(teamColor)).isEmpty() ){
                /*
                for every friendly piece
                    find out if it can move in a way that the king is safe
                    if it can
                        we are not in checkmate
                we're in checkmate because we never passed the test above.
                 */
                for (ChessPosition pieceLocation : this.board.getPieceLocations(teamColor)){
                    ChessPiece piece = this.board.getPiece(pieceLocation);
                    Collection<ChessMove> moves = piece.pieceMoves(this.board, pieceLocation);
                    for (ChessMove move : moves){
                        ChessBoard possibleBoard = this.getPossibleBoard(move.getStartPosition(), move.getEndPosition());
                        if (!possibleBoard.pieceIsInDanger(possibleBoard.getKingPosition(teamColor))){
                            return false;
                        }
                    }
                }
                return true;
            }
            return false;
        }

        /**
         * Checks if a piece has any moves where it will not be captured, and returns an ArrayList of all the safe moves.
         * @param position a ChessPosition that you want to check
         * @return An ArrayList of safe moves, if there are any, and an empty list if there are none.
         */
        public Collection<ChessMove> safeMoves(ChessPosition position){
            /*
            make the safe move list
            get the piece
            for every move this piece can make right now
                if an enemy piece couldn't attack you there
                    it's a safe move so add it to the list
            return the safe move list
             */
            ArrayList<ChessMove> safeMoves = new ArrayList<ChessMove>();
            ChessPiece piece = this.board.getPiece(position);
            Collection<ChessMove> pieceMoves = piece.pieceMoves(this.board, position);
            for (ChessMove move : pieceMoves){
                // Put the piece in the new hypothetical end position of the move
                ChessBoard possibleBoard = this.getPossibleBoard(position, move.getEndPosition());
                Collection<ChessMove> enemyMoves = possibleBoard.getEnemyMovesHere(piece.getTeamColor(), move.getEndPosition());
                if ( enemyMoves.isEmpty() ){
                    safeMoves.add(move);
                }
            }
            return safeMoves;
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
            get all the pieces of the team
            if each piece has no validmoves
                return true
            otherwise return false
             */
            ChessPosition kingPos = this.board.getKingPosition(teamColor);
            if (!this.board.pieceIsInDanger(kingPos)){
                Collection<ChessPosition> pieceLocations = this.board.getPieceLocations(teamColor);
                for (ChessPosition pieceLocation : pieceLocations) {
                    Collection<ChessMove> moves = this.validMoves(pieceLocation);
                    if(!moves.isEmpty()){
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

    /**
     * Moves a piece in a copy of the ChessBoard to a new position to test
     * moves as if the piece were at that position (to meet requirements for
     * checkmate)
     *
     * @param oldPosition A ChessPosition representing the position of the piece to be moved.
     * @param newPosition The new position to move the piece to.
     *
     * @return A ChessBoard that is the current board with the king moved to a different position.
     */
    public ChessBoard getPossibleBoard(ChessPosition oldPosition, ChessPosition newPosition) {
        ChessBoard possibleBoard = this.board.copy();
        ChessPiece piece = possibleBoard.getPiece(oldPosition);
        possibleBoard.removePiece(oldPosition);
        possibleBoard.addPiece(newPosition, piece);
        return possibleBoard;
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

    @Override
    public boolean equals(Object o){
        if (o instanceof ChessGame){
            ChessGame obj = (ChessGame)o;
            // Remember it checks the board and the current team turn for equality!
            return this.board.equals(obj.getBoard()) && this.getTeamTurn() == obj.getTeamTurn();
        }
        return false;
    }
}
