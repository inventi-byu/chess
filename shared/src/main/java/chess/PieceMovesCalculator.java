package chess;

import java.util.*;

public class PieceMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private ChessPiece piece;

    public PieceMovesCalculator(ChessBoard board, ChessPiece piece, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.piece = piece;
    }

    public List<ChessMove> moves() {

        // Create empty list
        List<ChessMove> moves = new ArrayList<ChessMove>();

        switch (this.piece.getPieceType()) {
            // Remember the limit means how far you can go in that direction
            // For knight, you can only go one time in each direction
            case ChessPiece.PieceType.ROOK:
                moves.addAll(this.checkPath(ChessMove.Direction.UP, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.DOWN, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.LEFT, 0));
                moves.addAll(this.checkPath(ChessMove.Direction.RIGHT, 0));
                break;
            case ChessPiece.PieceType.KNIGHT:
                moves.addAll(this.checkPath(ChessMove.Direction.Lur, 1));
                moves.addAll(this.checkPath(ChessMove.Direction.Lul, 1));
                moves.addAll(this.checkPath(ChessMove.Direction.Lrr, 1));
                moves.addAll(this.checkPath(ChessMove.Direction.Lrl, 1));
                //
                moves.addAll(this.checkPath(ChessMove.Direction.Ldr, 1));
                moves.addAll(this.checkPath(ChessMove.Direction.Ldl, 1));
                moves.addAll(this.checkPath(ChessMove.Direction.Llr, 1));
                moves.addAll(this.checkPath(ChessMove.Direction.Lll, 1));
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
                switch (piece.getTeamColor()){
                    case ChessGame.TeamColor.WHITE:
                        if(position.getRow() == 2){
                            moves.addAll(this.checkPath(ChessMove.Direction.UP, 2));
                        } else {
                            moves.addAll(this.checkPath(ChessMove.Direction.UP, 1));
                        }
                        moves.addAll(this.checkPath(ChessMove.Direction.DIAGur, 1));
                        moves.addAll(this.checkPath(ChessMove.Direction.DIAGul, 1));
                        break;
                    case ChessGame.TeamColor.BLACK:
                        if(position.getRow() == 7){
                            moves.addAll(this.checkPath(ChessMove.Direction.DOWN, 2));
                        } else {
                            moves.addAll(this.checkPath(ChessMove.Direction.DOWN, 1));
                        }
                        moves.addAll(this.checkPath(ChessMove.Direction.DIAGdr, 1));
                        moves.addAll(this.checkPath(ChessMove.Direction.DIAGdl, 1));
                        break;
                }
                break;
        }
        return moves;
    }

    public List<ChessMove> checkPath(ChessMove.Direction direction, int limit) {

        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        ChessPosition curPos = new ChessPosition(position.getRow(), position.getColumn());
        boolean noLimit = (limit == 0);
        boolean dirIsDiagonal = (direction == ChessMove.Direction.DIAGur || direction == ChessMove.Direction.DIAGul || direction == ChessMove.Direction.DIAGdl || direction == ChessMove.Direction.DIAGdr);
        int i = 0;

        // While it is in bounds, and there are either no limits or it is in limit
        while (curPos.isInBounds() && (noLimit || i < limit) ) {

            curPos = this.getNewPosition(curPos, direction);

            // If the new position is off the board, don't use it
            if (!curPos.isInBounds()){
                break;
            }

            // The position is in bounds, get the piece
            ChessPiece curPiece = board.getPiece(curPos);
            // If there's an enemy, keep that move and stop then break
            // Otherwise just break
            if (curPiece != null){
                if(piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    if (direction == ChessMove.Direction.UP || direction == ChessMove.Direction.DOWN) {
                        // It can't move forward if there is a piece in the way
                        break;
                    }
                }
                if(curPiece.isEnemy(this.piece)){
                    if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
                        if (dirIsDiagonal && curPos.isPromotionEdge(piece.getTeamColor())) {
                            moves = this.addPawnPromotionMoves(position, curPos, moves);
                            break;
                        }
                    }
                    moves.add(new ChessMove(position, curPos, null));
                }
                break;
            }

            // Pawns can't move to an empty square diagonally, only if there's an enemy piece there
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                if (dirIsDiagonal) {
                    break;
                }
                if (curPos.isPromotionEdge(piece.getTeamColor())){
                    moves = this.addPawnPromotionMoves(position, curPos, moves);
                    break;
                }
            }
            // There is no piece so you can move there
            moves.add(new ChessMove(position, curPos, null));
            // Increment move counter
            i++;
        }
        return moves;
    }

    public ArrayList<ChessMove> addPawnPromotionMoves(ChessPosition startPosition, ChessPosition endPosition, ArrayList<ChessMove> movesList) {
        movesList.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        movesList.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        movesList.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        movesList.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        return movesList;
    }

    public ChessPosition getNewPosition(ChessPosition oldPos, ChessMove.Direction dir) {
        int row = oldPos.getRow();
        int col = oldPos.getColumn();

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
                break;
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
                row += 2;
                col++;
                break;
            case ChessMove.Direction.Lul:
                row += 2;
                col--;
                break;
            case ChessMove.Direction.Lrr:
                row--;
                col += 2;
                break;
            case ChessMove.Direction.Lrl:
                row++;
                col += 2;
                break;
            case ChessMove.Direction.Ldr:
                row -= 2;
                col--;
                break;
            case ChessMove.Direction.Ldl:
                row -= 2;
                col++;
                break;
            case ChessMove.Direction.Llr:
                row++;
                col -= 2;
                break;
            case ChessMove.Direction.Lll:
                row--;
                col -= 2;
                break;
        }
        return new ChessPosition(row, col);
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