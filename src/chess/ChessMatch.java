package src.chess;
import src.boardgame.Board;
import src.boardgame.Piece;
import src.boardgame.Position;
import src.chess.pieces.King;
import src.chess.pieces.Rook;

public class ChessMatch {

    private Board board;

    public ChessMatch() {

        board = new Board(8,8);
        initialSetup();
    }



    public ChessPiece[][] getPieces() {
        ChessPiece [][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i=0;i<board.getRows();i++) {
            for (int j=0;j<board.getColumns();j++) {
                mat[i][j] = (ChessPiece) board.piece(i,j); //Downcasting
            }
        }
        return mat;
    }
    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source,target);
        Piece capturedPiece = makeMove(source,target);
        return (ChessPiece) capturedPiece;
    }

    private void validateTargetPosition(Position source, Position target) {
        if(!board.piece(source).possibleMove(target)) {
            throw new ChessException("A peça escolhida não pode se mover para a posição de destino");
        }
    }

    private void validateSourcePosition(Position position) {
        if(!board.thereIsAPiece(position)){
            throw new ChessException("Não existe uma peça nessa posição");
        }
        if(!(board.piece(position).isThereAnyPossibleMove())){
            throw new ChessException("Não existem movimentos disponíveis");
        }
    }
    private Piece makeMove(Position sourcePosition, Position targetPosition) {
        Piece p = board.removePiece(sourcePosition);
        Piece capturedPiece = board.removePiece(targetPosition);
        board.placePiece(p, targetPosition);
        return capturedPiece;
    }
    private void initialSetup(){
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLUE));
        placeNewPiece('c', 8, new Rook(board, Color.BLUE));
        placeNewPiece('d', 7, new Rook(board, Color.BLUE));
        placeNewPiece('e', 7, new Rook(board, Color.BLUE));
        placeNewPiece('e', 8, new Rook(board, Color.BLUE));
        placeNewPiece('d', 8, new King(board, Color.BLUE));
    }
}
