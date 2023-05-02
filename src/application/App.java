package src.application;

import src.chess.ChessException;
import src.chess.ChessMatch;
import src.chess.ChessPiece;
import src.chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        ChessMatch chessMatch1 = new ChessMatch();
        while(true){
          try {
              UI.clearScreen();
              UI.printBoard(chessMatch1.getPieces());
              System.out.println(" ");
              System.out.print("Source: ");
              ChessPosition source = UI.readChessPosition(teclado);
              System.out.println(" ");
              System.out.print("Target: ");
              ChessPosition target = UI.readChessPosition(teclado);
              ChessPiece capturedPiece = chessMatch1.performChessMove(source, target);
          }
          catch(ChessException e){
              System.out.println(e.getMessage() + ". Aperte ENTER para continuar.");
              teclado.nextLine();
          }
          catch (InputMismatchException e){
              System.out.println(e.getMessage() + ". Aperte ENTER para continuar. ");
              teclado.nextLine();
          }
        }
    }
}
