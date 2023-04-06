package src.chess;

import src.boardgame.Piece;
import src.boardgame.Board;

public class ChessPiece extends Piece {

    private Color color;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color=color;
    }
}
