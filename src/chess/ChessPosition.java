package src.chess;

import src.boardgame.Position;

public class ChessPosition {
    private char column;
    private int row;


    public ChessPosition(char column, int row) {
        this.column=column;
        this.row = row;
        if (column > 'h' || column < 'a' || row > 8 || row < 1) {
            throw new ChessException("Erro ao instanciar posição da peça. Não existem essas coordenadas no tabuleiro.");
        }

    }

    protected Position toPosition() {
        return new Position(8-row, column - 'a');
    }
    protected static ChessPosition fromPosition(Position p) {
        return new ChessPosition((char)('a' + p.getColumn()), 8 - p.getRow());
    }

    @Override
    public String toString(){
        return " " + column + row;
    }


    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
