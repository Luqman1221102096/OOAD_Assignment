import java.util.List;

//base class for all pieces
//Author: Luqman, Arif 
abstract class Piece {
    String pieceID; //ID for the piece (like "BR0" for Blue Ram 0)
    String side;    //red or blue
    int x, y;       //position of the piece on the board

    public Piece(String pieceID, String side, int x, int y) {
        this.pieceID = pieceID;
        this.side = side;
        this.x = x;
        this.y = y;
    }

    // Copy constructor.
    public Piece(Piece piece) {
        this.pieceID = piece.getPieceID();
        this.side = piece.getSide();
        this.x = piece.getCoordinateX();
        this.y = piece.getCoordinateY();
    }

    //get the current x-coords of the piece
    public int getCoordinateX() {
        return x;
    }

    //get the current y-coords of the piece
    public int getCoordinateY() {
        return y;
    }

    //update the position of the piece to new coords
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //get the unique ID of the piece
    public String getPieceID() {
        return pieceID;
    }
    
    public void setPieceID(String newPieceID) {
        this.pieceID = newPieceID;
    }

    //get the side (team) of the piece
    public String getSide() {
        return side;
    }

    //for diff piece, diff moves
    public abstract List<int[]> getMoves(List<Piece> pieces);// I add in the pieces so that Tor and Xor can immediately return moves that don't contain blockoff squares
}

