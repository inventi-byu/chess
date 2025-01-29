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
        this.resetBoard();
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
    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getRow()-1][position.getColumn()-1];
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
                    board[i][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
                    board[i][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
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
                    board[i][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
                    board[i][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                    board[i][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                    board[i][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                    board[i][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
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
                    if (this.board[i][j] != obj.board[i][j]){
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
        return Objects.hash((java.lang.Object)board);
    }

    @Override
    public toString() {
        for(int i=0; i < 8; i++){
            StringBuilder sb = new StringBuilder();

        }
    }
}


