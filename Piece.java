// Base class of all the pieces
// Authors : Luqman
public class Piece {
    String pieceID, side;
    int x,y;
    Piece(String pieceID, String side, int x, int y){
        this.pieceID = pieceID;
        this.side = side;
        this.x = x;
        this.y = y;
    }
    public int getCoordinateX(){
        return this.x;
    }
    public int getCoordinateY(){
        return this.y;
    }
    public void setCoordinates(int x, int y){
        this.x = x;
        this.y = y;
    }
    public String getPieceID(){
        return this.pieceID;
    }
    // Most likely will update some information
    public void captured(){
        //To do
    }
}

