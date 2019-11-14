package boxtakehome;
import java.io.*;
import java.util.*;
import java.lang.StringBuilder;
import boxtakehome.Utils;
import boxtakehome.King;
import boxtakehome.GoldGeneral;
import boxtakehome.SilverGeneral;
import boxtakehome.Bishop;
import boxtakehome.Pawn;
import boxtakehome.Rook;


public class ShogiGame {
  public Player upper = new Player("UPPER");
  public Player lower = new Player("lower");
  public Player currentPlayer = lower;
  public String[][] board = new String[5][5];
  public HashMap<Character, Integer> indexes = new HashMap<>();
  public List<Piece> list = new ArrayList<>();
  public List<String> moves;
  public boolean legal = true;
  public int currentMoveIndex = 0;
  public List<String> upperCaptures;
  public List<String> lowerCaptures;
  public List<Character> boardList = new ArrayList<>();
  public boolean inCheck = false;
  public List<String> availableMoves = new ArrayList<>();
  public boolean lose = false;
  public boolean win = false;

// constructer in interactive mode
  public ShogiGame () {
    createDefaultBoard();
    upper.opponent = lower;
    lower.opponent = upper;
    this.upperCaptures = new ArrayList<>();
    this.lowerCaptures = new ArrayList<>();
    setUp();
  }

  // constructer in file mode
  public ShogiGame (String[][] board, List<String> upperCaptures, List<String> lowerCaptures, List<String> moves) {
      this.board = board;
      this.upperCaptures = upperCaptures;
      this.lowerCaptures = lowerCaptures;
      this.moves = moves;
      upper.opponent = lower;
      lower.opponent = upper;
      setUp();
  }

  // play in file mode
  public void play() {
    for (String move : moves) {
      String[] strings = move.split(" ");
      this.currentMoveIndex ++;
      if (strings[0].equals("move")) {
        readMoves(move);
      }else if(strings[0].equals("drop")){
        readDrops(move);
      }

      if (legal == false) {
        refreshBoard();
        refreshCaptureList();
        return;
      }
      else if (inCheck) {
        if (currentMoveIndex == moves.size()) {
          if (availableMoves.size() == 0) {
            this.lose = true;
            return;
          }else {
            return;
          }
        }else {
          if (!availableMoves.contains(moves.get(currentMoveIndex))) {
            inCheck = false;
            legal = false;
            return;
          }else {
            inCheck = false;
            this.availableMoves = new ArrayList<>();
          }
        }
      }
      else if (this.currentMoveIndex >= 400) {
        refreshCaptureList();
        refreshBoard();
        return;
      }
    }
    refreshBoard();
    refreshCaptureList();
  }

//play funciton for interactive mode
  public boolean interactivePlay(String move) {
    String[] strings = move.split(" ");
    this.currentMoveIndex ++;
    if (strings[0].equals("move")) {
      readMoves(move);
    }else if(strings[0].equals("drop")){
      readDrops(move);
    }
    if (legal == false) {
      refreshBoard();
      refreshCaptureList();
      return false;
    }
    else if (inCheck) {
      if (currentMoveIndex == moves.size()) {
        if (availableMoves.size() == 0) {
          this.lose = true;
          return false;
        }else {
          return false;
        }
      }else {
        if (!availableMoves.contains(moves.get(currentMoveIndex))) {
          inCheck = false;
          legal = false;
          return false;
        }else {
          inCheck = false;
          this.availableMoves = new ArrayList<>();
        }
      }
    }
    else if (this.currentMoveIndex >= 400) {
      refreshCaptureList();
      refreshBoard();
      return false;
    }
    refreshCaptureList();
    refreshBoard();
    return true;
  }

//set up beginning board
  public void setUp() {
    createIndexes();
    createList();
    refreshList();
  }

  public void createDefaultBoard() {
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        board[i][j] = "";
      }
    }
    board[0][0] = "k";
    board[0][1] = "g";
    board[0][2] = "s";
    board[0][3] = "b";
    board[0][4] = "r";
    board[1][0] = "p";

    board[4][4] = "K";
    board[4][3] = "G";
    board[4][2] = "S";
    board[4][1] = "B";
    board[4][0] = "R";
    board[3][4] = "P";
  }

  private void createIndexes() {
    indexes.put('k', 0);
    indexes.put('g', 1);
    indexes.put('s', 2);
    indexes.put('b', 3);
    indexes.put('r', 4);
    indexes.put('p', 5);

    indexes.put('K', 6);
    indexes.put('G', 7);
    indexes.put('S', 8);
    indexes.put('B', 9);
    indexes.put('R', 10);
    indexes.put('P', 11);
  }

  private void createList() {
    list.add(new King("k", 0, 0, "lower"));
    list.add(new GoldGeneral("g", 0, 1, "lower"));
    list.add(new SilverGeneral("s", 0, 2, "lower"));
    list.add(new Bishop("b", 0, 3, "lower"));
    list.add(new Rook("r", 0, 4, "lower"));
    list.add(new Pawn("p", 1, 0, "lower"));

    list.add(new King("K", 4, 4, "UPPER"));
    list.add(new GoldGeneral("G", 4, 3, "UPPER"));
    list.add(new SilverGeneral("S", 4, 2, "UPPER"));
    list.add(new Bishop("B", 4, 1, "UPPER"));
    list.add(new Rook("R", 4, 0, "UPPER"));
    list.add(new Pawn("P", 3, 4, "UPPER"));
  }

  public void refreshCaptureList() {
    for (int i = 0; i < lower.capturedPieces.size(); i++) {
      lower.capturedPieces.set(i, lower.capturedPieces.get(i).toLowerCase());
    }
    for (int i = 0; i < upper.capturedPieces.size(); i++) {
      upper.capturedPieces.set(i, upper.capturedPieces.get(i).toUpperCase());
    }
  }

  public void refreshBoard() {
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (board[i][j].length() == 0) continue;
        int index = indexes.get(board[i][j].charAt(board[i][j].length()-1));
        Piece p = list.get(index);
        if (p.owner.equals("UPPER")) {
          board[i][j] = board[i][j].toUpperCase();
        }else {
          board[i][j] = board[i][j].toLowerCase();
        }
      }
    }
  }

  public void refreshList() {
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (board[i][j].length() == 0) continue;
        boardList.add(board[i][j].charAt(board[i][j].length()-1));
        int index = indexes.get(board[i][j].charAt(board[i][j].length()-1));
        Piece p = list.get(index);
        p.row = i;
        p.col = j;
        if (board[i][j].charAt(0) == '+') p.promotion = true;
      }
    }
    for (String s : upperCaptures) {
      if (s.length() == 0) continue;
      int index;
      if (boardList.contains(s.charAt(0))) {
        index = indexes.get(Character.toLowerCase(s.charAt(0)));
      }
      else if (!upper.capturedPieces.contains(s)) {
        index = indexes.get(Character.toUpperCase(s.charAt(0)));
      }else {
        index = indexes.get(Character.toLowerCase(s.charAt(0)));
      }
      Piece p = list.get(index);
      upper.capturedPieces.add(p.pieceName);
      p.owner = "UPPER";
    }
    for (String s : lowerCaptures) {
      if (s.length() == 0) continue;
      int index;
      if (boardList.contains(s.charAt(0))) {
        index = indexes.get(Character.toUpperCase(s.charAt(0)));
      }
      else if (!upper.capturedPieces.contains(s)) {
        index = indexes.get(Character.toLowerCase(s.charAt(0)));
      }else {
        index = indexes.get(Character.toUpperCase(s.charAt(0)));
      }
      Piece p = list.get(index);
      lower.capturedPieces.add(p.pieceName);
      p.owner = "lower";
    }
  }

// read the drop statement
  public void readDrops(String s) {
    String[] strings = s.split(" ");
    String dropPiece = strings[1];
    int toRow = strings[2].charAt(1) - '1';
    int toCol = strings[2].charAt(0) - 'a';
    if (ifValidDrop(dropPiece, toRow, toCol)) {
      List<String> temp = new ArrayList<>(currentPlayer.capturedPieces);
      drop(dropPiece, toRow, toCol);
      currentPlayer = currentPlayer.opponent;
      if (dropPiece.toLowerCase().equals("p") && isCheck(toRow, toCol)) {
        undo(dropPiece, toRow, toCol, temp);
        legal = false;
      }
      else if (isCheck(toRow, toCol)) {
        inCheck = true;
      }
    }else {
      legal = false;
    }
  }

  // read the move statement
  public void readMoves(String s) {
    String[] strings = s.split(" ");
    int row = strings[1].charAt(1) - '1';
    int col = strings[1].charAt(0) - 'a';
    int toRow = strings[2].charAt(1) - '1';
    int toCol = strings[2].charAt(0) - 'a';
    boolean promotion = false;
    if (strings.length == 4) {
      promotion = true;
    }
    if (moveInToCheck(row, col, toRow, toCol)) {
      legal = false;
      return;
    }
    if (isValidMove(row, col, toRow, toCol, promotion)) {
      move(row, col, toRow, toCol, promotion);
      currentPlayer = currentPlayer.opponent;
      if (isCheck(toRow, toCol)) {
        inCheck = true;
      }
    }else {
      legal = false;
    }
  }

  public boolean isValidMove(int row, int col, int toRow, int toCol, boolean promotion) {
    if (!isInBoard(row, col) || !isInBoard(toRow, toCol)) return false;
    if (row == toRow && col == toCol) return true;
    if (board[row][col].length() == 0) return false;
    Piece piece = list.get(indexes.get(board[row][col].charAt(board[row][col].length()-1)));
    if (promotion == true) {
      if (piece.pieceName.toLowerCase().equals("k") || piece.pieceName.toLowerCase().equals("g")) return false;
      if (piece.promotion == true) return false;
    }

    if (piece.owner != currentPlayer.name) return false;
    if (board[toRow][toCol].length() != 0) {
      Piece secondPiece = list.get(indexes.get(board[toRow][toCol].charAt(board[toRow][toCol].length()-1)));
      if (secondPiece.owner == currentPlayer.name) return false;
    }
    return piece.isPieceValidMove(toRow, toCol);
  }

  public void makePiecePromote(int row, int col) {
    list.get(indexes.get(board[row][col].charAt(board[row][col].length()-1))).promotion = true;
    if (board[row][col].charAt(0) != '+') {
      String s = "+" + board[row][col];
      board[row][col] = s;
    }
  }

  public boolean ifForcePawnPromote(int row, int col) {
    Piece p = list.get(indexes.get(board[row][col].charAt(board[row][col].length()-1)));
    if (p.pieceName.toLowerCase().equals("p")) {
      if (p.owner.equals("UPPER")) {
        if (row == 0) return true;
      }else {
        if (row == 4) return true;
      }
    }
    return false;
  }

  public void move(int row, int col, int toRow, int toCol, boolean promotion) {
    Piece piece = list.get(indexes.get(board[row][col].charAt(board[row][col].length()-1)));
    if (board[toRow][toCol].length() != 0) {
      capture(toRow, toCol);
    }
    piece.row = toRow;
    piece.col = toCol;
    board[toRow][toCol] = board[row][col];
    board[row][col] = "";
    if (ifForcePawnPromote(toRow, toCol)) {
      makePiecePromote(toRow, toCol);
      return;
    }
    if (promotion) makePiecePromote(toRow, toCol);
  }

  public void capture(int row, int col) {
    Piece p = list.get(indexes.get(board[row][col].charAt(board[row][col].length()-1)));
    List<String> cp = new ArrayList<String>(currentPlayer.capturedPieces);
    if (currentPlayer.name.equals("UPPER")) upper.capturedPieces.add(p.pieceName);
    if (currentPlayer.name.equals("lower")) lower.capturedPieces.add(p.pieceName);
    p.owner = currentPlayer.name;
  }

  public boolean ifValidDrop(String pieceName, int toRow, int toCol) {
    if (board[toRow][toCol].length() != 0) return false;
    if (pieceName.equals("p") && doublePawnDrop(toRow, toCol)) return false;
    if (pieceName.equals("p") && pawnDropInProZone(toRow, toCol)) return false;
    if (currentPlayer == upper) {
      return upper.capturedPieces.contains(pieceName) || upper.capturedPieces.contains(pieceName.toUpperCase());
    }else {
      return lower.capturedPieces.contains(pieceName) || lower.capturedPieces.contains(pieceName.toUpperCase());
    }
  }

  public boolean pawnDropInProZone(int toRow, int toCol) {
    if (currentPlayer == upper) {
      if (toRow == 0) return true;
    }else {
      if (toRow == 4) return true;
    }
    return false;
  }

  public boolean doublePawnDrop(int toRow, int toCol) {
    for (int i = 0; i < 5; i++) {
      if (board[i][toCol].length() != 0) {
        Piece p = list.get(indexes.get(board[i][toCol].charAt(board[i][toCol].length()-1)));
        if (p.owner.equals(currentPlayer.name) && p.pieceName.toLowerCase().equals("p")) return true;
      }
      if (board[toRow][i].length() != 0) {
        Piece p = list.get(indexes.get(board[toRow][i].charAt(board[toRow][i].length()-1)));
        if (p.owner.equals(currentPlayer.name) && p.pieceName.toLowerCase().equals("p")) return true;
      }
    }
    return false;
  }

  public void drop(String pieceName, int toRow, int toCol) {
    if (currentPlayer == lower) {
      if (!lower.capturedPieces.contains(pieceName)) {
        pieceName = pieceName.toUpperCase();
      }
      board[toRow][toCol] = pieceName;
      Piece piece = getPiece(pieceName.charAt(0));
      piece.row = toRow;
      piece.col = toCol;
      piece.owner = "lower";
      lower.capturedPieces.remove(pieceName);
      boardList.add(pieceName.charAt(0));
    }else {
      if (!upper.capturedPieces.contains(pieceName)) {
        pieceName = pieceName.toUpperCase();
      }
      board[toRow][toCol] = pieceName;
      Piece piece = getPiece(pieceName.charAt(0));
      piece.row = toRow;
      piece.col = toCol;
      piece.owner = "UPPER";
      upper.capturedPieces.remove(pieceName);
      boardList.add(pieceName.charAt(0));
    }
  }

// undo the last move
  public void undo(String pieceName, int row, int col, List<String> temp) {
    board[row][col] = "";
    currentPlayer = currentPlayer.opponent;
    if (currentPlayer == lower) {
      if (lower.capturedPieces.contains(pieceName)) {
        pieceName = pieceName.toUpperCase();
      }
      Piece piece = getPiece(pieceName.charAt(0));
    }else {
      if (upper.capturedPieces.contains(pieceName)) {
        pieceName = pieceName.toUpperCase();
      }
      Piece piece = getPiece(pieceName.charAt(0));
    }
    currentPlayer.capturedPieces = temp;
  }

//move the king piece to see if the king can get out of the attack
  public List<String> moveK(Piece k) {
    List<String> availableM = new ArrayList<>();
    int[] directionR = {0, 0, 1, 1, 1, -1, -1, -1};
    int[] directionC = {-1, 1, 1, 0, -1, 1, 0, -1};
    Player tempOpponent = currentPlayer;
    for (int i = 0; i < 8; i++) {
      currentPlayer = tempOpponent;
      boolean flag = true;
      if (isValidMove(k.row, k.col, k.row+directionR[i], k.col+directionC[i], false)) {
        currentPlayer = tempOpponent.opponent;
        String temp = board[k.row+directionR[i]][k.col+directionC[i]];
        board[k.row+directionR[i]][k.col+directionC[i]] = "";
        for (char c : boardList) {
          Piece piece = getPiece(c);
          if (!piece.owner.equals(currentPlayer.name) || (piece.row == k.row+directionR[i] && piece.col == k.col+directionC[i])) continue;
          if (piece.row ==  k.row+directionR[i] && piece.col == k.col+directionC[i]) continue;
          if (isValidMove(piece.row, piece.col, k.row+directionR[i], k.col+directionC[i], false)) {
            flag = false;
          }
        }if (flag) {
          String s = "move " + getPosition(k.row, k.col) + " " + getPosition(k.row+directionR[i], k.col+directionC[i]);
          availableM.add(s);
        }
        board[k.row+directionR[i]][k.col+directionC[i]] = temp;
      }
    }
    currentPlayer = tempOpponent;
    return availableM;
  }

  public boolean isCheck(int row, int col) {
    Piece k;
    if (currentPlayer == upper) {
      k = getPiece('K');
    }else {
      k = getPiece('k');
    }
    Piece piece = getPiece(board[row][col].charAt(board[row][col].length()-1));
    if (piece.owner == k.owner) return false;
    if (piece.isPieceValidMove(k.row, k.col)) {
      if (!ifPieceBetween(k.row, k.col, row, col)) {
        this.availableMoves.addAll(blockOutOfCheckMove(k.row, k.col, row, col));
        this.availableMoves.addAll(dropOutOfCheck(k.row, k.col, row, col));
        this.availableMoves.addAll(moveK(k));
        return true;
      }
    }
    return false;
  }

  public boolean ifPieceBetween(int row, int col, int toRow, int toCol) {
    if (row == toRow) {
      if (Math.abs(col-toCol) == 1) return false;
      for (int i = Math.min(col, toCol)+1; i < Math.max(col, toCol); i++) {
        if (board[row][i].length() != 0) return true;
      }
    }else if (col == toCol) {
      if (Math.abs(row-toRow) == 1) return false;
      for (int i = Math.min(row, toRow)+1; i < Math.max(row, toRow); i++) {
        if (board[i][col].length() != 0) return true;
      }
    }else if (Math.abs(row + col) == Math.abs(toRow + toCol)) {
      int sum = row+col;
      if (Math.abs(row-toRow) == 1) return false;
      for (int i = Math.min(row, toRow)+1; i < Math.max(row, toRow); i++) {
        if (board[i][sum-i].length() != 0) return true;
      }
    }else if (Math.abs(row-col) == Math.abs(toRow-toCol)) {
      int diff = row - col;
      if (Math.abs(row-toRow) == 1) return false;
      for (int i = Math.min(row, toRow)+1; i < Math.max(row, toRow); i++) {
        if (board[i][i-diff].length() != 0) return true;
      }
    }
    return false;
  }

  public List<String> blockOutOfCheckMove(int row, int col, int toRow, int toCol) {
    List<String> list = new ArrayList<>();
    for (char c : boardList) {
      Piece piece = getPiece(c);
      if (!piece.owner.equals(currentPlayer.name) || piece.pieceName.equals(getPiece(board[toRow][toCol].charAt(board[toRow][toCol].length()-1)).pieceName)
          || piece.pieceName.equals(getPiece(board[row][col].charAt(board[row][col].length()-1)).pieceName)) {
        continue;
      }
      if (isValidMove(piece.row, piece.col, toRow, toCol, false)) {
        list.add("move "+getPosition(piece.row, piece.col) + " " + getPosition(toRow, toCol));
      }
      if (row == toRow) {
        for (int i = Math.min(col, toCol)+1; i < Math.max(col, toCol); i++) {
          if (isValidMove(piece.row, piece.col, row, i, false)) {
            list.add("move "+getPosition(piece.row, piece.col) + " " + getPosition(row, i));
          }
        }
      }else if (col == toCol) {
        for (int i = Math.min(row, toRow)+1; i < Math.max(row, toRow); i++) {
          if (isValidMove(piece.row, piece.col, i, col, false)) {
            list.add("move "+getPosition(piece.row, piece.col) + " " + getPosition(i, col));
          }
        }
      }else if (Math.abs(row + col) == Math.abs(toRow + toCol)) {
        int sum = row+col;
        for (int i = Math.min(row, toRow)+1; i < Math.max(row, toRow); i++) {
          if (isValidMove(piece.row, piece.col, i, sum-i, false)) {
            list.add("move "+getPosition(piece.row, piece.col) + " " + getPosition(i, sum-i));
          }
        }
      }else if (Math.abs(row-col) == Math.abs(toRow-toCol)) {
        int diff = row - col;
        for (int i = Math.min(row, toRow)+1; i < Math.max(row, toRow); i++) {
          if (isValidMove(piece.row, piece.col, i, i-diff, false)) {
            list.add("move "+getPosition(piece.row, piece.col) + " " + getPosition(i, i-diff));
          }
        }
      }
    }
    return list;
  }

  public List<String> dropOutOfCheck(int row, int col, int toRow, int toCol) {
    List<String> list = new ArrayList<>();
    for (String s : currentPlayer.capturedPieces) {
      char c = s.charAt(0);
      Piece piece = getPiece(s.charAt(0));
      if (row == toRow) {
        for (int i = Math.min(col, toCol)+1; i < Math.max(col, toCol); i++) {
          if (currentPlayer == lower) {
            list.add("drop "+Character.toLowerCase(c) + " " + getPosition(row, i));
          }else {
            list.add("drop "+ Character.toLowerCase(c) + " " + getPosition(row, i));
          }
        }
      }else if (col == toCol) {
        for (int i = Math.min(row, toRow)+1; i < Math.max(row, toRow); i++) {
          if (currentPlayer == lower) {
            list.add("drop "+Character.toLowerCase(c) + " " + getPosition(i, col));
          }else {
            list.add("drop "+ Character.toLowerCase(c) + " " + getPosition(i, col));
          }
        }
      }else if (Math.abs(row + col) == Math.abs(toRow + toCol)) {
        int sum = row+col;
        for (int i = Math.min(row, toRow)+1; i < Math.max(row, toRow); i++) {
          if (currentPlayer == lower) {
            list.add("drop "+Character.toLowerCase(c) + " " + getPosition(i, sum-i));
          }else {
            list.add("drop "+ Character.toLowerCase(c) + " " + getPosition(i, sum-i));
          }
        }
      }else if (Math.abs(row-col) == Math.abs(toRow-toCol)) {
        int diff = row - col;
        for (int i = Math.min(row, toRow)+1; i < Math.max(row, toRow); i++) {
          if (currentPlayer == lower) {
            list.add("drop "+Character.toLowerCase(c) + " " + getPosition(i, i-diff));
          }else {
            list.add("drop "+ Character.toLowerCase(c) + " " + getPosition(i, i-diff));
          }
        }
      }
    }
    return list;
  }

  public boolean moveInToCheck(int row, int col, int toRow, int toCol) {
    Piece k;
    if (currentPlayer == upper) {
      k = getPiece('K');
    }else {
      k = getPiece('k');
    }
    currentPlayer = currentPlayer.opponent;
    Piece moveP = getPiece(board[row][col].charAt(board[row][col].length()-1));
    moveP.row = toRow;
    moveP.col = toCol;
    String temp = board[toRow][toCol];
    board[toRow][toCol] = board[row][col];
    board[row][col] = "";
    for (char c : boardList) {
      Piece piece = getPiece(c);
      if (!piece.owner.equals(currentPlayer.name)) continue;
      if (temp.length() != 0 && piece.pieceName.charAt(0) == (temp.charAt(temp.length()-1))) continue;
      if (isValidMove(piece.row, piece.col, k.row, k.col, false) && !ifPieceBetween(piece.row, piece.col, k.row, k.col)) {
        currentPlayer = currentPlayer.opponent;
        board[row][col] = board[toRow][toCol];
        board[toRow][toCol] = temp;
        moveP.row = row;
        moveP.col = col;
        return true;
      }
    }
    moveP.row = row;
    moveP.col = col;
    board[row][col] = board[toRow][toCol];
    board[toRow][toCol] = temp;
    currentPlayer = currentPlayer.opponent;
    return false;
  }

//get the object piece
  public Piece getPiece(char c) {
    return list.get(indexes.get(c));
  }

  public void getLastMove() {
    String move = moves.get(currentMoveIndex-1);
    String[] strings = move.split(" ");
    int row = strings[1].charAt(1) - '1';
    int col = strings[1].charAt(0) - 'a';
    int toRow = strings[2].charAt(1) - '1';
    int toCol = strings[2].charAt(0) - 'a';
    currentPlayer = currentPlayer.opponent;
  }

  public boolean isInBoard(int row, int col) {
    if (row < 0 || row >= 5 || col < 0 || col >= 5) return false;
    return true;
  }

  public String getPosition(int row, int col) {
    StringBuilder sb = new StringBuilder();
    sb.append((char)('a'+col));
    sb.append((char)('1'+row));
    return sb.toString();
  }
}
