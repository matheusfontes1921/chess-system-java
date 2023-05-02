package src.chess;

import src.boardgame.Piece;
import src.boardgame.Board;
import src.boardgame.Position;

public abstract class ChessPiece extends Piece {

    private Color color;

    public Color getColor() {
        return color;
    }


    public ChessPiece(Board board, Color color) {
        super(board);
        this.color=color;
    }
    protected boolean isThereOpponentPiece(Position position){
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null && p.getColor() != color;
    }
}
