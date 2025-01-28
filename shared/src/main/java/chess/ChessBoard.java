package chess;
import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ArrayList<ArrayList<ChessPiece>> board;

    public ChessBoard() {
        this.board = new ArrayList<ArrayList<ChessPiece>>(8);
        this.resetBoard();
        }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board.get(position.getRow()-1).add(position.getColumn()-1, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board.get(position.getRow()-1).get(position.getColumn()-1);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.board = new ArrayList<ArrayList<ChessPiece>>(8);
        this.board.add(0, new ArrayList<ChessPiece>(8));
        this.board.add(1, new ArrayList<ChessPiece>(8));
        this.board.add(2, new ArrayList<ChessPiece>(8));
        this.board.add(3, new ArrayList<ChessPiece>(8));
        this.board.add(4, new ArrayList<ChessPiece>(8));
        this.board.add(5, new ArrayList<ChessPiece>(8));
        this.board.add(6, new ArrayList<ChessPiece>(8));
        this.board.add(7, new ArrayList<ChessPiece>(8));

        this.board.get(0).add(0, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        this.board.get(0).add(1, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.board.get(0).add(2, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.board.get(0).add(3, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        this.board.get(0).add(4, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        this.board.get(0).add(5, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.board.get(0).add(6, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.board.get(0).add(7, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        this.board.get(1).add(0, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.board.get(1).add(1, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.board.get(1).add(2, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.board.get(1).add(3, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.board.get(1).add(4, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.board.get(1).add(5, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.board.get(1).add(6, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.board.get(1).add(7, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));

        this.board.get(6).add(0, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.board.get(6).add(1, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.board.get(6).add(2, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.board.get(6).add(3, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.board.get(6).add(4, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.board.get(6).add(5, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.board.get(6).add(6, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.board.get(6).add(7, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

        this.board.get(7).add(0, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        this.board.get(7).add(1, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.board.get(7).add(2, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.board.get(7).add(3, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        this.board.get(7).add(4, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        this.board.get(7).add(5, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.board.get(7).add(6, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.board.get(7).add(7, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));


        /*
        for (int i = 0; i < 8; i++){
            switch (i) {
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
         */

    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ChessBoard){
            ChessBoard obj = (ChessBoard)o;
            for (int i = 1; i < 9; i++){
                for(int j = 1; j < 9; j++){
                    if(obj.getPiece(new ChessPosition(i, j)) != this.getPiece(new ChessPosition(i, j))){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
