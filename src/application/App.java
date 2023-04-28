package src.application;

import src.chess.ChessMatch;
import src.chess.ChessPiece;
import src.chess.ChessPosition;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        ChessMatch chessMatch1 = new ChessMatch();
        while(true){
            UI.printBoard(chessMatch1.getPieces());
            System.out.println(" ");
            System.out.print("Source: ");
            ChessPosition source = UI.readChessPosition(teclado);
            System.out.println(" ");
            System.out.print("Target: ");
            ChessPosition target = UI.readChessPosition(teclado);
            ChessPiece capturedPiece = chessMatch1.performChessMove(source,target);

        }
    }
}
