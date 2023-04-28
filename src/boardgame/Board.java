package src.boardgame;

public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Necessário que tenha pelo menos uma linha e uma coluna");
        } else if (rows != columns) {
            throw new BoardException("O tabuleiro deve ser quadrado");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }


    public Piece piece(int row, int column) {
        if(!positionExists(row,column)) {
            throw new BoardException("Posição inexistente");
        }
        return pieces[row][column];
    }

    public Piece piece(Position position) {
        if(!positionExists(position)) {
            throw new BoardException("Posição inexistente");
        }
        return pieces[position.getRow()][position.getColumn()];
    }

    public Piece removePiece(Position p) {
        if(!positionExists(p)){
            throw new BoardException("Posição inexistente");
        }
        if(piece(p)==null){
            return null;
        } else{
            Piece aux = piece(p);
            aux.position=null;
            pieces[p.getRow()][p.getColumn()] = null;
            return aux;
        }
    }

    public void placePiece(Piece piece, Position position) {
       if(thereIsAPiece(position)) {
           throw new BoardException("Já existe uma peça nessa posição");
       }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    private boolean positionExists(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column<columns;
    }
    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    public boolean thereIsAPiece(Position position) {
        if(!positionExists(position)){
            throw new BoardException("Posição inexistente");
        }
        return piece(position) != null;
    }

    public int getRows() {
        return rows;
    }


    public int getColumns() {
        return columns;
    }

}
