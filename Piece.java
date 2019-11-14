package boxtakehome;
import java.io.*;
import java.util.*;

abstract public class Piece {
  public String pieceName;
  public int row;
  public int col;
  public boolean promotion = false;
  public String owner;

  public Piece (String name, int r, int c, String owner) {
      this.pieceName = name;
      this.row = r;
      this.col = c;
      this.owner = owner;
  }

   abstract public boolean isPieceValidMove(int toRow, int toCol);
   abstract public boolean isValidMove(int toRow, int toCol);
   abstract public boolean isPromotedValidMove(int toRow, int toCol);
}
