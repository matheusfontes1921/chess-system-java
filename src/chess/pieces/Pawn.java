package src.chess.pieces;

import src.boardgame.Board;
import src.boardgame.Position;
import src.chess.ChessPiece;
import src.chess.Color;

public class Pawn extends ChessPiece {
    public Pawn(Board board, Color color) {
        super(board,color);
    }
    public String toString() {
        return("P");
    }
    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();

    }
    @Override
    public boolean[][] possibleMoves(){
        boolean[][]mat=new boolean[getBoard().getRows()][getBoard().getRows()];
        Position p = new Position(0,0);
        if (getColor() == Color.WHITE) {
            p.setValues(position.getRow()-1, position.getColumn());
            if(getBoard().positionExists(p)&&canMove(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
            if(getMoveCount() == 0){
                p.setValues(position.getRow()-2, position.getColumn());
                Position p2 = new Position(position.getRow()-1, position.getColumn());
                if(getBoard().positionExists(p) && canMove(p) && getBoard().positionExists(p2)&&canMove(p2)) {
                    mat[p.getRow()][p.getColumn()] = true;
                }
            }
            p.setValues(position.getRow()-1, position.getColumn()-1);
            if(getBoard().positionExists(p)&&isThereOpponentPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow()-1, position.getColumn()+1);
            if(getBoard().positionExists(p)&&isThereOpponentPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
        } else {
            if (getColor() == Color.BLUE) {
                p.setValues(position.getRow() + 1, position.getColumn());
                if (getBoard().positionExists(p) && canMove(p)) {
                    mat[p.getRow()][p.getColumn()] = true;
                }
                if (getMoveCount() == 0) {
                    p.setValues(position.getRow() + 2, position.getColumn());
                    Position p2 = new Position(position.getRow() + 1, position.getColumn());
                    if (getBoard().positionExists(p) && canMove(p) && getBoard().positionExists(p2) && canMove(p2)) {
                        mat[p.getRow()][p.getColumn()] = true;
                    }
                }
                p.setValues(position.getRow() + 1, position.getColumn() - 1);
                if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                    mat[p.getRow()][p.getColumn()] = true;
                }
                p.setValues(position.getRow() + 1, position.getColumn() + 1);
                if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                    mat[p.getRow()][p.getColumn()] = true;
                }
            }
        }


        return mat;
    }
}
