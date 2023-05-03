package src.chess;
import src.boardgame.Board;
import src.boardgame.Piece;
import src.boardgame.Position;
import src.chess.pieces.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {

    private int turn;
    private ChessPiece promoted;
    private ChessPiece enPassantVulnerable;
    private Color currentPlayer;
    private Board board;
    private boolean check;
    private boolean checkMate;
    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {

        board = new Board(8,8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    private Piece makeMove(Position sourcePosition, Position targetPosition) {
        ChessPiece p = (ChessPiece) board.removePiece(sourcePosition);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(targetPosition);
        board.placePiece(p, targetPosition);
        if(capturedPiece!=null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }
        if(p instanceof King && targetPosition.getColumn() == sourcePosition.getColumn() + 2) {
            Position sourceRook = new Position(sourcePosition.getRow(), sourcePosition.getColumn()+3);
            Position targetRook = new Position(sourcePosition.getRow(), sourcePosition.getColumn()+1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceRook);
            board.placePiece(rook,targetRook);
            rook.increaseMoveCount();
        }
        if(p instanceof King && targetPosition.getColumn() == sourcePosition.getColumn() - 2) {
            Position sourceRook = new Position(sourcePosition.getRow(), sourcePosition.getColumn()-4);
            Position targetRook = new Position(sourcePosition.getRow(), sourcePosition.getColumn()-1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceRook);
            board.placePiece(rook,targetRook);
            rook.increaseMoveCount();
        }
        //en passant
        if(p instanceof Pawn) {
            if(sourcePosition.getColumn() != targetPosition.getColumn() && capturedPiece == null){
                Position pawnPosition;
                if(p.getColor() == Color.WHITE){
                    pawnPosition = new Position(targetPosition.getRow()+1, targetPosition.getColumn());
                } else {
                    pawnPosition = new Position(targetPosition.getRow()-1, targetPosition.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }
        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece){
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p,source);

        if(capturedPiece!=null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
        if(p instanceof King && target.getColumn() == source.getColumn() + 2) {
        Position sourceRook = new Position(source.getRow(), source.getColumn()+3);
        Position targetRook = new Position(source.getRow(), source.getColumn()+1);
        ChessPiece rook = (ChessPiece) board.removePiece(targetRook);
        board.placePiece(rook,sourceRook);
        rook.decreaseMoveCount();
    }
        if(p instanceof King && target.getColumn() == source.getColumn() - 2) {
        Position sourceRook = new Position(source.getRow(), source.getColumn()-4);
        Position targetRook = new Position(source.getRow(), source.getColumn()-1);
        ChessPiece rook = (ChessPiece) board.removePiece(targetRook);
        board.placePiece(rook,sourceRook);
        rook.decreaseMoveCount();
    }
        //en passant
        if(p instanceof Pawn) {
            if(source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable){
                ChessPiece pawn = (ChessPiece)board.removePiece(target);
                Position pawnPosition;
                if(p.getColor() == Color.WHITE){
                    pawnPosition = new Position(3, target.getColumn());
                } else {
                    pawnPosition = new Position(4, target.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
                capturedPiece = board.removePiece(pawnPosition);
            }
        }
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

    private void nextTurn(){
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLUE : Color.WHITE;
    }
    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }
    public boolean[][]possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source,target);
        Piece capturedPiece = makeMove(source,target);
        if(testCheck(currentPlayer)) {
            undoMove(source,target,capturedPiece);
            throw new ChessException("Você não pode se colocar em check");
        }
        ChessPiece movedPiece = (ChessPiece) board.piece(target);
        //promotion

        promoted = null;
        if(movedPiece instanceof Pawn) {
            if((movedPiece.getColor() == Color.WHITE && targetPosition.getRow() == 0) || (movedPiece.getColor() == Color.BLUE && targetPosition.getRow() == 7)){
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q");

            }
        }


        check = (testCheck(opponent(currentPlayer))) ? true : false;
        if(testCheck(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }
        //specialmove en passant
        if(movedPiece instanceof Pawn && (target.getRow() == source.getRow() +2 || target.getRow() == source.getRow() -2)){
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }
        return (ChessPiece) capturedPiece;
    }
    public ChessPiece replacePromotedPiece(String type) {
        if(promoted == null) {
            throw new IllegalStateException("Não há peça para ser promovida");
        }
        if(!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")){
            return promoted;
        }
        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);
        ChessPiece newPiece = newPiece(type,promoted.getColor());
        board.placePiece(newPiece,pos);
        piecesOnTheBoard.add(newPiece);
        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        if(type.equals("B")){
            return new Bishop(board,color);
        }
        if(type.equals("N")) {
            return new Knight(board,color);
        }
        if(type.equals("Q")){
            return new Queen(board,color);
        }
      return new Rook(board,color);
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
        if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
            throw new ChessException("A peça escolhida não é sua");
        }
        if(!(board.piece(position).isThereAnyPossibleMove())){
            throw new ChessException("Não existem movimentos disponíveis");
        }
    }

    private ChessPiece king(Color color) {
        List<Piece> pieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for (Piece p : pieces) {
            if(p instanceof King) {
                return (ChessPiece)p;
            }
        }
        throw new IllegalStateException("Não existe o rei da cor no tabuleiro");
    }

    private Color opponent(Color color) {
        if(color == Color.WHITE) {
            return Color.BLUE;
        } else {
            return Color.WHITE;
        }
    }
    private void initialSetup(){
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE,this)); //instancia a partida que ja esta
        placeNewPiece('a', 2, new Pawn(board,Color.WHITE,this));
        placeNewPiece('b', 2, new Pawn(board,Color.WHITE,this));
        placeNewPiece('c', 2, new Pawn(board,Color.WHITE,this));
        placeNewPiece('d', 2, new Pawn(board,Color.WHITE,this));
        placeNewPiece('e', 2, new Pawn(board,Color.WHITE,this));
        placeNewPiece('f', 2, new Pawn(board,Color.WHITE,this));
        placeNewPiece('g', 2, new Pawn(board,Color.WHITE,this));
        placeNewPiece('h', 2, new Pawn(board,Color.WHITE,this));
        placeNewPiece('f', 1, new Bishop(board,Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board,Color.WHITE));
        placeNewPiece('g',1,new Knight(board, Color.WHITE));
        placeNewPiece('b',1,new Knight(board, Color.WHITE));
        placeNewPiece('g',8,new Knight(board, Color.BLUE));
        placeNewPiece('b',8,new Knight(board, Color.BLUE));
        placeNewPiece('d',1, new Queen(board,Color.WHITE));
        placeNewPiece('d',8, new Queen(board,Color.BLUE));
        placeNewPiece('a', 8, new Rook(board, Color.BLUE));
        placeNewPiece('h', 8, new Rook(board, Color.BLUE));
        placeNewPiece('e', 8, new King(board,Color.BLUE,this));
        placeNewPiece('c', 8, new Bishop(board,Color.BLUE));
        placeNewPiece('f', 8, new Bishop(board,Color.BLUE));
        placeNewPiece('a', 7, new Pawn(board,Color.BLUE, this));
        placeNewPiece('b', 7, new Pawn(board,Color.BLUE, this));
        placeNewPiece('c', 7, new Pawn(board,Color.BLUE, this));
        placeNewPiece('d', 7, new Pawn(board,Color.BLUE,this));
        placeNewPiece('e', 7, new Pawn(board,Color.BLUE,this));
        placeNewPiece('f', 7, new Pawn(board,Color.BLUE,this));
        placeNewPiece('g', 7, new Pawn(board,Color.BLUE,this));
        placeNewPiece('h', 7, new Pawn(board,Color.BLUE,this));

    }

    private boolean testCheck(Color color){
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x->((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
            return false;
    }

    private boolean testCheckMate(Color color){
        if(!testCheck(color)) {
            return false;
        }
        List<Piece> colorList = piecesOnTheBoard.stream().filter(x->((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for (Piece p : colorList){
            boolean[][] mat = p.possibleMoves();
            for (int i= 0; i< board.getRows();i++) {
                for (int j=0;j< board.getColumns(); j++) {
                    if(mat[i][j]) {
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i,j);
                        Piece capturedPiece = makeMove(source,target);
                        boolean testCheck = testCheck(color);
                        undoMove(source,target,capturedPiece);
                        if(!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public int getTurn(){
        return turn;
    }
    public boolean getCheckMate(){
        return checkMate;
    }
    public boolean getCheck(){
        return check;
    }
    public void setTurn(int turn){
        this.turn=turn;
    }
    public Color getCurrentPlayer(){
        return currentPlayer;
    }
    public ChessPiece getEnPassantVulnerable(){
        return enPassantVulnerable;
    }
    public ChessPiece getPromoted(){
        return promoted;
    }

}
