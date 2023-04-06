package src.application;

import src.boardgame.Position;
import src.chess.ChessMatch;

public class App {
    public static void main(String[] args) {
        ChessMatch chessMatch1 = new ChessMatch();
        UI.printBoard(chessMatch1.getPieces());
    }
}
