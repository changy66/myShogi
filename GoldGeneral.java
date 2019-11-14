package boxtakehome;
import java.io.*;
import java.util.*;
import boxtakehome.Piece;

public class GoldGeneral extends Piece {
  public GoldGeneral (String name, int r, int c, String owner) {
      super(name, r, c, owner);
  }

  public boolean isPieceValidMove(int toRow, int toCol) {
    if (!this.promotion) {
      return isValidMove(toRow, toCol);
    }else {
      return isPromotedValidMove(toRow, toCol);
    }
  }

  public boolean isPromotedValidMove(int toRow, int toCol) {
    return false;
  }

  public boolean isValidMove(int toRow, int toCol) {
    if (this.owner.equals("lower")) {
      if (toRow - row == -1 && Math.abs(toCol - col) == 1) return false;
      if (Math.abs(toRow - row) <= 1 && Math.abs(toCol - col) <= 1) return true;
    }else {
      if (toRow - row == 1 && Math.abs(toCol - col) == 1) return false;
      if (Math.abs(toRow - row) <= 1 && Math.abs(toCol - col) <= 1) return true;
    }
    return false;
  }
}
