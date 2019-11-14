
import java.io.*;
import java.util.*;
import java.util.Scanner;
import boxtakehome.ShogiGame;
import boxtakehome.Utils;

public class myShogi {

  public static String[][] getBoard (List<Utils.InitialPosition> initialPieces) {
    String[][] board = new String[5][5];
    for (int i = 0; i < 5; i++) {
      Arrays.fill(board[i], "");
    }
    for (Utils.InitialPosition i : initialPieces) {
      int col = i.getPosition().charAt(0) - 'a';
      int row = i.getPosition().charAt(1) - '1';
      board[row][col] = i.getPiece();
    }
    return board;
  }

  public static String listToString (List<String> list, boolean upper) {
    String str = "";
    if (upper) {
      for (String s : list) {
        str = str + " " + s.toUpperCase();
      }
    }else {
      for (String s : list) {
        str = str + " " + s.toLowerCase();
      }
    }
    return str.trim();
  }

  public static String stringLastMove (List<String> moves, int index) {
    if (moves.size() == 0) return "";
    if (index % 2 == 0) {
      return "lower player action: " + moves.get(index);
    }else {
      return "UPPER player action: " + moves.get(index);
    }
  }

  public static void main(String[] args) throws Exception {
    String s = args[0];

    // interactive mode
    if (s.equals("-i")) {
      ShogiGame shogiGame = new ShogiGame();
      Scanner scanner = new Scanner(System.in);
      while (true) {
        System.out.print(Utils.stringifyBoard(shogiGame.board));
        System.out.println("");
        System.out.println("Captures UPPER: "+ listToString(shogiGame.upper.capturedPieces, true));
        System.out.println("Captures lower: "+ listToString(shogiGame.lower.capturedPieces, false));
        System.out.println("");
        System.out.print(shogiGame.currentPlayer.name + "> ");
        String value = scanner.nextLine();
        if (!shogiGame.interactivePlay(value)) break;
        if (shogiGame.inCheck == true) {
          System.out.println(shogiGame.currentPlayer.name+" player is in check!");
          System.out.println("Available moves:");
          java.util.Collections.sort(shogiGame.availableMoves);
          for (String m : shogiGame.availableMoves) {
            System.out.println(m);
          }
          System.out.println(shogiGame.currentPlayer.name + "> ");
        }
      }
      if (shogiGame.win == true) {
        System.out.println(shogiGame.currentPlayer.name + " player wins.  Checkmate.");
      }
      else if (shogiGame.lose == true) {
        System.out.println(shogiGame.currentPlayer.opponent.name + " player wins.  Checkmate.");
      }
      else if(shogiGame.legal == false) {
        System.out.println(shogiGame.currentPlayer.opponent.name + " player wins.  Illegal move.");
      }
      else if (shogiGame.currentMoveIndex >= 400) {
        System.out.println("Tie game.  Too many moves.");
      }
    }
    // file mode
    else if (s.equals("-f")) {
      Utils.TestCase testCase = Utils.parseTestCase(args[1]);
      List<Utils.InitialPosition> initialPieces = testCase.getInitialPieces();
      List<String> upperCaptures = testCase.getUpperCaptures();
      List<String> lowerCaptures = testCase.getLowerCaptures();
      List<String> moves = testCase.getMoves();
      String[][] board = getBoard(initialPieces);

      ShogiGame shogiGame = new ShogiGame(board, upperCaptures, lowerCaptures, moves);
      shogiGame.play();
      System.out.println(stringLastMove(moves, shogiGame.currentMoveIndex-1));
      System.out.print(Utils.stringifyBoard(shogiGame.board));
      System.out.println("");
      System.out.println("Captures UPPER: "+ listToString(shogiGame.upper.capturedPieces, true));
      System.out.println("Captures lower: "+ listToString(shogiGame.lower.capturedPieces, false));
      System.out.println("");
      if (shogiGame.win == true) {
        System.out.println(shogiGame.currentPlayer.name + " player wins.  Checkmate.");
      }
      else if (shogiGame.lose == true) {
        System.out.println(shogiGame.currentPlayer.opponent.name + " player wins.  Checkmate.");
      }
      else if(shogiGame.legal == false) {
        System.out.println(shogiGame.currentPlayer.opponent.name + " player wins.  Illegal move.");
      }
      else if (shogiGame.currentMoveIndex >= 400) {
        System.out.println("Tie game.  Too many moves.");
      }
      else if (shogiGame.inCheck == true) {
        System.out.println(shogiGame.currentPlayer.name+" player is in check!");
        System.out.println("Available moves:");
        java.util.Collections.sort(shogiGame.availableMoves);
        for (String m : shogiGame.availableMoves) {
          System.out.println(m);
        }
        System.out.println(shogiGame.currentPlayer.name + "> ");
      }
      else {
        System.out.println(shogiGame.currentPlayer.name + "> ");
      }
    }
    else {
      System.out.println("input is not valid, please input again");
    }

  }
}
