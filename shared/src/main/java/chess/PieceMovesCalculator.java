package chess;

import java.util.*;
public class PieceMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private ChessPiece piece;

    public PieceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.piece = this.board.getPiece(this.position);
    }

    public List<ChessMove> moves() {
        List<ChessMove> moves = new ArrayList<ChessMove>();
        switch (this.piece.getPieceType()) {
            case ChessPiece.PieceType.ROOK:
                moves.addAll(this.checkPath(ChessMove.Direction.UP, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.DOWN, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.LEFT, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.RIGHT, 0));
                break;
            case ChessPiece.PieceType.KNIGHT:
                moves.addAll(this.checkPath(ChessMove.Direction.L, 0));
                break;
            case ChessPiece.PieceType.BISHOP:
                moves.addAll(this.checkPath(ChessMove.Direction.DIAGur, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.DIAGul, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.DIAGdl, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.DIAGdr, 0));
                break;
            case ChessPiece.PieceType.KING:
                moves.addAll(this.checkPath(ChessMove.Direction.UP, 1));
                moves.addAll(this.checkPath(ChessMove.Direction.DOWN, 1));
                moves.addAll(this.checkPath(ChessMove.Direction.LEFT, 1));
                moves.addAll(this.checkPath(ChessMove.Direction.RIGHT, 1));
                moves.addAll(this.checkPath(ChessMove.Direction.DIAGur, 1));
                moves.addAll(this.checkPath(ChessMove.Direction.DIAGul, 1));
                moves.addAll(this.checkPath(ChessMove.Direction.DIAGdl, 1));
                moves.addAll(this.checkPath(ChessMove.Direction.DIAGdr, 1));
                break;
            case ChessPiece.PieceType.QUEEN:
                moves.addAll(this.checkPath(ChessMove.Direction.UP, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.DOWN, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.LEFT, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.RIGHT, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.DIAGur, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.DIAGul, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.DIAGdl, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.DIAGdr, 0));
                break;
            case ChessPiece.PieceType.PAWN:
                moves.addAll(this.checkPath(ChessMove.Direction.P, 0));
                break;


        }
        return moves;
    }

    public ChessPosition getNewPosition(ChessPosition old_pos, ChessMove.Direction dir) {
        int row = old_pos.getRow();
        int col = old_pos.getColumn();

        switch (dir) {
            case ChessMove.Direction.UP:
                row++;
                break;
            case ChessMove.Direction.DOWN:
                row--;
                break;
            case ChessMove.Direction.LEFT:
                col--;
                break;
            case ChessMove.Direction.RIGHT:
                col++;
            case ChessMove.Direction.DIAGur:
                row++;
                col++;
                break;
            case ChessMove.Direction.DIAGul:
                row++;
                col--;
                break;
            case ChessMove.Direction.DIAGdl:
                row--;
                col--;
                break;
            case ChessMove.Direction.DIAGdr:
                row--;
                col++;
                break;
            case ChessMove.Direction.Lur:
                row+=2;
                col++;
            case ChessMove.Direction.Lul:
                row+=2;
                col--;
            case ChessMove.Direction.Lrr:
                row--;
                col+=2;
            case ChessMove.Direction.Lrl:
                row++;
                col+=2;
            case ChessMove.Direction.Ldr:
                row-=2;
                col--;
            case ChessMove.Direction.Ldl:
                row-=2;
                col++;
            case ChessMove.Direction.Llr:
                row++;
                col-=2;
            case ChessMove.Direction.Lll:
                row--;
                col-=2;
        }
        return new ChessPosition(row, col);
    }
    public List<ChessMove> checkPath(ChessMove.Direction direction, int limit) {
        List<ChessMove> moves = new ArrayList<ChessMove>();

        int row = this.position.getRow() - 1;
        int col = this.position.getColumn() - 1;

        int true_limit = 8 - row;

        while (row < true_limit) {


            }

            return moves;
        }
    }

    public someMethod() {
        ChessMove move = this.checkPosition(new ChessPosition(i, col));
        if (move == null) {
            // We hit a friend piece
            break;
        }
        moves.add(move);
        if (this.piece.isEnemy(this.board.getPiece(new ChessPosition(i, col)))) {
            break;
        }
    }
    public ChessMove checkPosition(ChessPosition check_position){
        if (piece != null) {
            // there's a piece in the way
            if (piece.isEnemy(board.getPiece(check_position))) {
                return new ChessMove(this.position, check_position, null);
            }
            return null;
        } else {
            return new ChessMove(this.position, check_position, null);
        }
    }
}


/*
How are the pieces all fundamentally the same?
Process:
1. They need to know what piece they are (set defined rules)
2. Know where they are
3. Check to see where they can go if there were no other pieces
4. Check for what pieces there are and limit the representation accordingly
5. Return those possible moves

type of piece
locations of enemy and friendly pieces
does not honor turn nor anything involving the king

How will we check if they are friendly or enemy pieces?

 */