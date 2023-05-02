package src.application;

import src.chess.ChessException;
import src.chess.ChessMatch;
import src.chess.ChessPiece;
import src.chess.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        ChessMatch chessMatch1 = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();
        while(!chessMatch1.getCheckMate()){
          try {
              UI.clearScreen();
              UI.printMatch(chessMatch1,captured);
              System.out.println(" ");
              System.out.print("Source: ");
              ChessPosition source = UI.readChessPosition(teclado);
              boolean[][]possibleMoves = chessMatch1.possibleMoves(source);
              UI.clearScreen();
              UI.printBoard(chessMatch1.getPieces(),possibleMoves);
              System.out.println(" ");
              System.out.print("Target: ");
              ChessPosition target = UI.readChessPosition(teclado);
              ChessPiece capturedPiece = chessMatch1.performChessMove(source, target);
              if(capturedPiece!=null) {
                  captured.add(capturedPiece);
              }
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
        UI.clearScreen();
        UI.printMatch(chessMatch1,captured);
    }
}
