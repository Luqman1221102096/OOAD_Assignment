// Xor piece class
// Xor moves diagonally in any direction until it reaches the edge of the board
// Author: Luqman 
import java.util.ArrayList;
import java.util.List;

public class Xor extends Piece{
    // When turn is two, which to Tor
    int turn = 0;
    Xor(String pieceID, String side, int x, int y){
        super(pieceID, side, x, y);
    }
    // Gets the list of legal moves of the Tor piece
    // The algorithm loops through all the x coordinates and adjusts the y coordinates with yCoordinatePlus
    // Since the y coordinate increases/decrease by 1 for every 1 square we move away from current x we can calculate the y coordinate at the left edge of the board as current x coordinte - 1 or this.x-1.
    // yCoordinatePlus is decreased as it approches the current x coordinate. Once it passes it, the yCoordinatePlus is increase as we go in the opposite direction.
    public List<int[]> getMoves(){
        List<int[]> moves = new ArrayList<int[]>();
        int yCoordinatePlus = this.x - 1;// Set yCoordinatePlus
        for (int i = 0; i < 5; i++){
            // Skips the current square and adds 1 to yCoordinatePlus so it doesn't add 0 on the next loop
            if(i+1 == this.x){
                yCoordinatePlus++;
                continue;
            }
            // Adding upper diagonal moves with bound checking
            if(this.y+yCoordinatePlus <= 8){
                moves.add(new int[]{i+1,this.y+yCoordinatePlus});
            }
            // Adding lower diagonal moves with bound checking
            if(this.y-yCoordinatePlus >= 1){
                moves.add(new int[]{i+1,this.y-yCoordinatePlus});
            }
            // Begin adding to yCoordinatePlus once it has passed the current square
            if(this.x > i+1){
                yCoordinatePlus--;
            }
            else{
                yCoordinatePlus++;
            }
        }
        return moves;
    }
    // Validates if the given coordinates is a legal move.
    public void validateMove(int x, int y){
        List<int[]> moves = getMoves();// Gets list of legal moves
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
        Xor x1 = new Xor("Xor", "Red", 3, 5);
        List<int[]> list = x1.getMoves();
        // Printing moves
        for(int i = 0; i < list.size(); i++){
            System.out.print(list.get(i)[0] + " ");
            System.out.println(list.get(i)[1]);
        }
        x1.validateMove(2, 6);
    }
}
