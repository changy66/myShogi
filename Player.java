package boxtakehome;
import java.io.*;
import java.util.*;
import boxtakehome.Piece;

public class Player {
    public List<String> capturedPieces = new ArrayList<>();
    public String name;
    public Player opponent;

    public Player(String name) {
      this.name = name;
    }
}
