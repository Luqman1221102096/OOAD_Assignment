// Tor piece class
// Tor moves orthogonally up untill it reaches the edge of the board
// Author: Luqman
import java.util.ArrayList;
import java.util.List;
public class Tor extends Piece{
    // When turn is two, which to Xor
    int turn = 0;
    Tor(String pieceID, String side, int x, int y){
        super(pieceID, side, x, y);
    }
    // Gets the list of legal moves of the Tor piece
    public List<int[]> getMoves(){
        List<int[]> moves = new ArrayList<int[]>();
        // Horizontal: the y coordinate stay still as we loop through the x coordinates and add the Squares
        for (int i = 0; i < 5; i++){
            if(i+1 == this.x){continue;}// Skips current square
            moves.add(new int[]{i+1,this.y});
        }
        // Vertical the x coordinate stay still as we loop through the y coordinates and add the Squares
        for (int i = 0; i < 8; i++){
            if(i+1 == this.y){continue;}// Skips current square
            moves.add(new int[]{this.x,i+1});
        }
        return moves;
    }
    // Validates if the given coordinates is a legal move.
    public void validateMove(int x, int y){
        List<int[]> moves = getMoves(); // Gets list of legal moves
        for(int i = 0; i < moves.size(); i++){
            if(x == moves.get(i)[0] && y == moves.get(i)[1])
            {
                setCoordinates(x, y);
                System.out.println("Valid move");
                turn++;
                return;
            }
        }
        System.out.println("Invalid move");
    }
    public static void main(String[] args){
        Tor t1 = new Tor("Tor", "Red", 1, 2);
        List<int[]> list = t1.getMoves();
        // Printing moves
        for(int i = 0; i < list.size(); i++){
            System.out.print(list.get(i)[0] + " ");
            System.out.println(list.get(i)[1]);
        }
        t1.validateMove(2, 8);
    }
}
