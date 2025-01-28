package chess;
import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public ArrayList<ArrayList<ChessPiece>> board;

    public ChessBoard() {
        ArrayList<ArrayList<ChessPiece>> board;
        this.resetBoard();
        }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        ( board.get(position.getRow()-1) ).add(position.getColumn()-1, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board.get(position.getRow()-1).get(position.getColumn()-1);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.board = new ArrayList<ArrayList<ChessPiece>>(8);
        for (int i = 0; i < 8; i++){
            board.add(i, new ArrayList<ChessPiece>(8));
        }
        for (int i = 0; i < 8; i++){
            switch (i){
                case 0:
                    this.addPiece(new ChessPosition(i, 0), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                    this.addPiece(new ChessPosition(i, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
                    this.addPiece(new ChessPosition(i, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
                    this.addPiece(new ChessPosition(i, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
                    this.addPiece(new ChessPosition(i, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
                    this.addPiece(new ChessPosition(i, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
                    this.addPiece(new ChessPosition(i, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
                    this.addPiece(new ChessPosition(i, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                    break;
                case 1:
                    // WPR
                    for (int j = 1; j < 8; j++){
                        this.addPiece(new ChessPosition(i, j), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
                    }
                    break;
                case 6:
                    // BPR
                    for (int j = 1; j < 8; j++){
                        this.addPiece(new ChessPosition(i, j), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
                    }
                    break;
                case 7:
                    // BSR
                    this.addPiece(new ChessPosition(i, 0), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
                    this.addPiece(new ChessPosition(i, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
                    this.addPiece(new ChessPosition(i, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
                    this.addPiece(new ChessPosition(i, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
                    this.addPiece(new ChessPosition(i, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
                    this.addPiece(new ChessPosition(i, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
                    this.addPiece(new ChessPosition(i, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
                    this.addPiece(new ChessPosition(i, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
                    break;
                default:
                    break;
            }
        }

    }
}
