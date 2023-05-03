package src.chess.pieces;

import src.boardgame.Board;
import src.boardgame.Position;
import src.chess.ChessMatch;
import src.chess.ChessPiece;
import src.chess.Color;

public class Pawn extends ChessPiece {
    private ChessMatch chessMatch;
    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board,color);
        this.chessMatch=chessMatch;
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
            //en passant white
            if(position.getRow() == 3){
                Position left = new Position(position.getRow(), position.getColumn()-1);
                if(getBoard().positionExists(left)&&isThereOpponentPiece(left)&&getBoard().piece(left)== chessMatch.getEnPassantVulnerable()) {
                    mat[left.getRow()-1][left.getColumn()] = true;
                }
                Position right = new Position(position.getRow(), position.getColumn()+1);
                if(getBoard().positionExists(right)&&isThereOpponentPiece(right)&&getBoard().piece(right)== chessMatch.getEnPassantVulnerable()) {
                    mat[right.getRow()-1][right.getColumn()] = true;
                }
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
                //en passant blue
                if(position.getRow() == 4){
                    Position leftBlue = new Position(position.getRow(), position.getColumn()-1);
                    if(getBoard().positionExists(leftBlue)&&isThereOpponentPiece(leftBlue)&&getBoard().piece(leftBlue)== chessMatch.getEnPassantVulnerable()) {
                        mat[leftBlue.getRow()+1][leftBlue.getColumn()] = true;
                    }
                    Position rightBlue = new Position(position.getRow(), position.getColumn()+1);
                    if(getBoard().positionExists(rightBlue)&&isThereOpponentPiece(rightBlue)&&getBoard().piece(rightBlue)== chessMatch.getEnPassantVulnerable()) {
                        mat[rightBlue.getRow()+1][rightBlue.getColumn()] = true;
                    }
                }
            }
        }


        return mat;
    }
}
