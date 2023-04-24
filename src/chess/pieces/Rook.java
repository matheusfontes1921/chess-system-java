package src.chess.pieces;

import src.boardgame.Board;
import src.chess.ChessPiece;
import src.chess.Color;

public class Rook extends ChessPiece {
    public Rook(Board board, Color color) {
        super(board,color);
    }
    public String toString() {
        return "R";
    }
}
