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

    ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
        //this.resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board[position.getRow()-1][position.getColumn()-1] = piece;
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */

    public void removePiece(ChessPosition position){
        this.board[position.getRow()-1][position.getColumn()-1] = null;
    }

    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Tells you if there is a piece at a given ChessPosition, or if it is null.
     * @param position the ChessPosition to check.
     * @return True if there is a piece, False if it is null.
     */
    public boolean isPieceHere(ChessPosition position){
        return (this.getPiece(position) != null);
    }
    /**
     * A function to get the ChessPosition of the king of a given team.
     *
     * @param teamColor the color of the king wanted
     * @returns A ChessPosition representing the position of the king of the color specified by the teamColor
     */
    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor){
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition cur_pos = new ChessPosition(i, j);
                ChessPiece king_copy =  new ChessPiece(teamColor, ChessPiece.PieceType.KING);
                ChessPiece cur_piece = this.getPiece(cur_pos);
                if (cur_piece != null){
                    if (cur_piece.equals(king_copy)){
                        return cur_pos;
                    }
                }
            }
        }
        return null;
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
                if (this.isPieceHere(cur_pos)){
                    ChessPiece cur_piece = this.getPiece(cur_pos);

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
     * A function that returns a list of enemyMoves to a specific point.
     * @param teamColor ChessGame.TeamColor - the piece's color (will get the enemy of this piece's moves).
     * @param position ChessPosition - position potentially under attack you want to check.
     * @return An ArrayList of all moves of the enemy of teamColor that end at that position.
     */
    public Collection<ChessMove> getEnemyMovesHere(ChessGame.TeamColor teamColor, ChessPosition position) {
        ArrayList<ChessMove> enemy_moves = new ArrayList<ChessMove>();

        ChessGame.TeamColor enemy_color;
        if (teamColor == ChessGame.TeamColor.WHITE){
            enemy_color = ChessGame.TeamColor.BLACK;
        } else{
            enemy_color = ChessGame.TeamColor.WHITE;
        }

        ArrayList<ChessPosition> enemy_locations = this.getPieceLocations(enemy_color);
        for (ChessPosition enemy_location : enemy_locations){
            ChessPiece cur_enemy_piece = this.getPiece(enemy_location);
            Collection<ChessMove> moves = cur_enemy_piece.pieceMoves(this, enemy_location);
            for (ChessMove move : moves){
                if (move.getEndPosition().equals(position)) {
                    enemy_moves.add(move);
                }
            }
        }
        return enemy_moves;
    }


    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.board = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++){
            switch (i){
                case 0:
                    // Setup WSR
                    board[i][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                    board[i][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                    board[i][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                    board[i][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                    board[i][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
                    board[i][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                    board[i][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                    board[i][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                    break;
                case 1:
                    // Setup WPR
                    for (int j = 0; j < 8; j++){
                        board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
                    }
                    break;
                case 6:
                    //BPR
                    for (int j = 0; j < 8; j++){
                        board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
                    }
                    break;
                case 7:
                    //BSR
                    board[i][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
                    board[i][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                    board[i][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                    board[i][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                    board[i][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
                    board[i][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                    board[i][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                    board[i][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
                    break;

            }
        }

    }

    /**
     * Copies the ChessBoard array and puts it into a new ChessBoard, and returns that ChessBoard.
     *
     * @return A copied version of the ChessBoard.
     */
    public ChessBoard copy(){
        ChessBoard newBoard = new ChessBoard();
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition cur_pos = new ChessPosition(i,j);
                ChessPiece cur_piece = this.getPiece(cur_pos);
                if (cur_piece != null){
                    newBoard.addPiece(cur_pos, cur_piece);
                }
            }
        }
        return newBoard;
    }


    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-----------------\n");
        // The rows must be constructed in reverse, since row 1 is at the bottom
        // (it is at the end of the string)
        // But columns are constructed as normal, left to right since that is how
        // the string reads/is constructed
        for (int i = 8; i > 0; i--){
            sb.append('|');
            for (int j = 1; j < 9; j++){
                ChessPosition cur_pos = new ChessPosition(i,j);
                ChessPiece cur_piece = this.getPiece(cur_pos);
                // If there is no piece there, add a space.
                if (cur_piece == null) {
                    sb.append(" |");
                    continue;
                }
                if (cur_piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                    switch (cur_piece.getPieceType()){
                        case ChessPiece.PieceType.KING:
                            sb.append("K|");
                            break;
                        case ChessPiece.PieceType.QUEEN:
                            sb.append("Q|");
                            break;
                        case ChessPiece.PieceType.BISHOP:
                            sb.append("B|");
                            break;
                        case ChessPiece.PieceType.KNIGHT:
                            sb.append("N|");
                            break;
                        case ChessPiece.PieceType.ROOK:
                            sb.append("R|");
                            break;
                        case ChessPiece.PieceType.PAWN:
                            sb.append("P|");
                            break;
                    }
                } else {
                    // Team is Black
                    switch (cur_piece.getPieceType()){
                        case ChessPiece.PieceType.KING:
                            sb.append("k|");
                            break;
                        case ChessPiece.PieceType.QUEEN:
                            sb.append("q|");
                            break;
                        case ChessPiece.PieceType.BISHOP:
                            sb.append("b|");
                            break;
                        case ChessPiece.PieceType.KNIGHT:
                            sb.append("n|");
                            break;
                        case ChessPiece.PieceType.ROOK:
                            sb.append("r|");
                            break;
                        case ChessPiece.PieceType.PAWN:
                            sb.append("p|");
                            break;
                    }
                }
            }
            sb.append('\n');
        }
        sb.append("-----------------");
        return sb.toString();
    }








}


