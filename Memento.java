// This class implements the memento design patter and allows the user to undo moves
import java.util.ArrayList;
import java.util.List;

public class Memento {
    // Informatin from the model that the momento needs to restore its state later.
    private List<Piece> pieces;
    private boolean blueTurn;
    private int turnNumber;
    // Stores state of model
    Memento(List<Piece> pieces, boolean blueTurn, int turnNumber){
        this.pieces = new ArrayList<Piece>(); 
        deepCopy(pieces); // Creates a deep copy of the pieces
        this.blueTurn = blueTurn;
        this.turnNumber = turnNumber;
    }  
    public List<Piece> getPieces(){
        return pieces;
    }  
    public boolean getBlueTurn(){
        return blueTurn;
    } 
    public int getTurnNumber(){
        return turnNumber;
    } 
    // Creates a new list with deep copies of the pieces
    private void deepCopy(List<Piece> pieces){
        for(Piece piece : pieces){
            if(piece instanceof Ram){
                this.pieces.add(new Ram(piece));
            }else if (piece instanceof Biz){
                this.pieces.add(new Biz(piece));
            }else if (piece instanceof Sau){
                this.pieces.add(new Sau(piece));
            }
            else if (piece instanceof TorXor){
                this.pieces.add(new TorXor(piece));
            }
        }
    }
}
