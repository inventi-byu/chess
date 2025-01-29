package chess;

import java.util.*;
public class PieceMovesCalculator {


    public PieceMovesCalculator(){

    }

    public List<ChessMove> moves(ChessBoard board, ChessPosition position, ChessPiece.PieceType type){
        return null;
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