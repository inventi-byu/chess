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

            /*
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
            */
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
            if (this.pieceIsInDanger(this.board.getKingPosition(teamColor))){
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
            if (this.isInCheck(teamColor)){
                if (this.safeMoves(this.board.getKingPosition(teamColor)).isEmpty()){
                    return true;
                }
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

            ArrayList<ChessMove> safe_moves = new ArrayList<ChessMove>();
            ChessPiece piece = this.board.getPiece(position);
            Collection<ChessMove> piece_moves = piece.pieceMoves(this.board, position);
            for (ChessMove move : piece_moves){
                // Put the piece in the new hypothetical end position of the move
                ChessBoard possibleBoard = this.getPossibleBoard(position, move.getEndPosition());
                Collection<ChessMove> enemy_moves = this.getEnemyMovesHere(piece.getTeamColor(), move.getEndPosition(), possibleBoard);
                if ( enemy_moves.isEmpty() ){
                    safe_moves.add(move);
                }
            }




            //        if (kingIsInDanger(teamColor)){
            //            // Find out where the king is, and get the king so you can call pieceMoves() on it
            //            ChessPosition king_position = this.board.getKingPosition(teamColor);
            //            ChessPiece king = this.board.getPiece(king_position);
            //            Collection<ChessMove> king_moves = king.pieceMoves(this.board, king_position);
            //
            //            // The only time this will be true is when the king is surrounded by friendly pieces
            //            // The only piece that could attack it is a knight
            //            if (king_moves.isEmpty()){
            //                // If your king is in danger but he can't move, you're in checkmate
            //                return true;
            //            }
            //            // Must handle other cases
            //            // Check to see if pieceMoves given put you in danger
            //            for (ChessMove move : king_moves){
            //                // If there is a move the king can make that ends up with no enemies going to that spot,
            //                // There is somewhere the king can go so it is not in checkmate
            //                Collection<ChessMove> enemy_moves = this.getEnemyMovesHere(teamColor, move.getEndPosition());
            //                if ( enemy_moves.isEmpty() ){
            //                    return false;
            //                }
            //            }
            //            // Go through the whole loop, if none are empty, the king cannot escape
            //            return true;
            //        } else {
            //            return false;
            //        }




            return safe_moves;
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
            ChessPosition king_pos = this.board.getKingPosition(teamColor);
            if (!this.pieceIsInDanger(king_pos)){
                
            }
            return false;
            if ( !this.isInCheck(teamColor) ){
                Collection<ChessPosition> piece_locations = this.getPieceLocations(teamColor);
                for (ChessPosition piece_location : piece_locations) {
                    Collection<ChessMove> moves = this.validMoves(piece_location);
                    if(!moves.isEmpty()){
                            return false;
                    }
                }
                return true;
            }
            return false;
        }


        /**
         * A function that returns a list of enemyMoves to a specific point.
         * @param teamColor ChessGame.TeamColor - the piece's color (will get the enemy of this piece's moves).
         * @param position ChessPosition - position potentially under attack you want to check.
         * @returns An ArrayList of all moves of the enemy of teamColor that end at that position.
         */
        public Collection<ChessMove> getEnemyMovesHere(TeamColor teamColor, ChessPosition position, ChessBoard board) {
            ArrayList<ChessMove> enemy_moves = new ArrayList<ChessMove>();

            ChessGame.TeamColor enemy_color;
            if (teamColor == TeamColor.WHITE){
                enemy_color = TeamColor.BLACK;
            } else{
                enemy_color = TeamColor.WHITE;
            }

            ArrayList<ChessPosition> enemy_locations = this.getPieceLocations(enemy_color);
            for (ChessPosition enemy_location : enemy_locations){
                ChessPiece cur_enemy_piece = board.getPiece(enemy_location);
                Collection<ChessMove> moves = cur_enemy_piece.pieceMoves(board, enemy_location);
                for (ChessMove move : moves){
                    if (move.getEndPosition().equals(position)) {
                        enemy_moves.add(move);
                    }
                }
            }


//            ChessPiece generic_test_piece = new ChessPiece(teamColor, null);
//         //   ChessBoard potentialBoard = this.getPossibleBoard(teamColor, position;
//            ChessGame.TeamColor enemyColor = this.
//             ArrayList<ChessPosition> enemyLocations = this.getPieceLocations(teamColor);
//            for (int i = 1; i < 9; i++) {
//                for (int j = 1; j < 9; j++) {
//                    ChessPosition cur_pos = new ChessPosition(i, j);
//                    ChessPiece piece = this.board.getPiece(cur_pos);
//                    if (piece != null && piece.isEnemy(generic_test_piece)) {
//                        Collection<ChessMove> moves = piece.pieceMoves(potentialBoard, cur_pos);
//                        for (ChessMove move : moves) {
//                            // OK so the problem with this right now, is that when you call pieceMoves on the piece, it will only return valid moves
//                            // for where the pieces currently are. If I ask it for its moves, it will not be calculating based on if
//                            // the king were at the position we want to look at where if could potentially go, because
//                            // the king is not currently there. Somehow we need to tell pieceMoves that the king is
//                            // no longer "at" it's current position, but to calculate moves based on the board where
//                            // the king is in a different position, the position in question that we get passed in as one of the arguments.
//                            if (move.getEndPosition().equals(position)) {
//                                enemy_moves.add(move);
//                            }
//                        }
//                    }
//                }
//            }
            return enemy_moves;
        }

    /**
     * Gives a list of all the piece ChessPositions for the specified team.
     * @param teamColor the TeamColor to check.
     * @return an ArrayList of all the piece ChessPositions for the specified team.
     */
    public ArrayList<ChessPosition> getPieceLocations(ChessGame.TeamColor teamColor) {
        ArrayList<ChessPosition> locations = new ArrayList<ChessPosition>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition cur_pos = new ChessPosition(i,j);
                if (this.board.pieceIsHere(cur_pos)){
                    ChessPiece cur_piece = this.board.getPiece(cur_pos);

                    // If this piece is the color we want to get...add it to the list.
                    if (cur_piece.getTeamColor() == teamColor){
                        locations.add(cur_pos);
                    }

                }
            }
        }
        return locations;
    }

    /**
     * Checks to see if the piece at the given position is in danger of attack where it currently is.
     * @param position the ChessPosition of the piece that is being inquired.
     * @return True if the piece is in danger of attack by an enemy, returns false otherwise.
     */
    public boolean pieceIsInDanger(ChessPosition position){
            ChessPiece piece = this.board.getPiece(position);
            Collection<ChessMove> enemy_moves = this.getEnemyMovesHere(piece.getTeamColor(), position, this.board);
            return !(enemy_moves.isEmpty());
    }


    //    /**
    //     * Determines if a given king is in danger of being attacked
    //     *
    //     * @param teamColor which team's king to see if they are in danger
    //     * @return true if there is a move from the enemy that can be done against the king
    //     */
    //    private boolean kingIsInDanger(ChessGame.TeamColor teamColor) {
    //        ChessPosition king_pos = this.board.getKingPosition(teamColor);
    //        // We'll need to use a king piece, but not put it on the board just to refer to it for isEnemy()
    //        ChessPiece king_copy = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
    //        Collection<ChessMove> enemy_moves = this.getEnemyMovesHere(teamColor, king_pos);
    //        // If the enemy has no moves to the king space, it is not in danger
    //        return !(enemy_moves.isEmpty());
    //    }

        /**
         * Moves a piecein a copy of the ChessBoard to a new position to test
         * moves as if the piece were at that position (to meet requirements for
         * checkmate)
         *
         * @param newPosition A ChessPosition representing the position of the piece to be moved.
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
}
