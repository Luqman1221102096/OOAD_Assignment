
import java.util.ArrayList;
import java.util.List;

class KwazamChessModel {
    private List<Piece> pieces; //list of all the pieces on the board

    public KwazamChessModel() {
        pieces = new ArrayList<>();
        setupBoard(); //place the pieces on the board
    }

    //set up all the pieces
    private void setupBoard() {
        //add Rams for both teams
        for (int i = 0; i < 5; i++) {
            pieces.add(new Ram("BR" + i, "Blue", i, 6)); // Blue Rams on row 6
            pieces.add(new Ram("RR" + i, "Red", i, 1));  // Red Rams on row 1
        }
        //add Sau and Biz pieces
        pieces.add(new Sau("BS ", "Blue", 2, 7));
        pieces.add(new Sau("RS ", "Red", 2, 0));
        pieces.add(new Biz("BB1", "Blue", 1, 7));
        pieces.add(new Biz("BB2", "Blue", 3, 7));
        pieces.add(new Biz("RB1", "Red", 1, 0));
        pieces.add(new Biz("RB2", "Red", 3, 0));
    }

    //get a list of all the pieces
    public List<Piece> getPieces() {
        return pieces;
    }

    //find piece by its ID
    public Piece findPiece(String pieceID) {
        for (Piece piece : pieces) {
            if (piece.getPieceID().equals(pieceID)) {
                return piece;
            }
        }
        return null; //if no piece matches the ID, return null
    }

  //remove a piece from the board when captured
  public void removePiece(Piece piece) {
      pieces.remove(piece);
      if (piece instanceof Sau) { //if Sau gets captured then game over
          System.out.println("Game Over! " + piece.getSide() + " Sau has been captured.");
          System.exit(0); //end the game
        }
    }


    //check if there is a piece at the given coords
    public Piece pieceAt(int x, int y) {
        for (Piece piece : pieces) {
            if (piece.getCoordinateX() == x && piece.getCoordinateY() == y) {
                return piece;
            }
        }
        return null; //no piece is at this location
    }

    //board boundaries
    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < 5 && y >= 0 && y < 8;
    }
}
