// Tor and Xor piece class
// Tor moves orthogonally up untill it reaches the edge of the board
// Xor moves diagonally in any direction until it reaches the edge of the board
// Since Tor and Xor turn into one another every 2 turns they are practically the same entity.
// Its simple for them to be implemented in the same class.
// Author: Luqman
import java.util.ArrayList;
import java.util.List;
public class TorXor extends Piece {
    // When turn is two, which to Xor or Tor depending on previous state
    int turn = 0;
    String state;
    TorXor(String pieceID, String side, int x, int y, String state){
        super(pieceID, side, x, y);
        this.state = state;
    }
    // Gets the list of legal moves of the Tor and Xor pieces
    public List<int[]> getMoves(){
        List<int[]> moves = new ArrayList<int[]>();
        // Gets the list of legal moves of the Tor piece
        if(state == "Tor"){
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
        else{
            // Gets the list of legal moves of the Xor piece
            // The algorithm loops through all the x coordinates and adjusts the y coordinates with yCoordinatePlus
            // Since the y coordinate increases/decrease by 1 for every 1 square we move away from current x we can calculate the y coordinate at the left edge of the board as current x coordinte - 1 or this.x-1.
            // yCoordinatePlus is decreased as it approches the current x coordinate. Once it passes it, the yCoordinatePlus is increase as we go in the opposite direction.
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
                if(turn == 2){
                    turn = 0;
                    if(state == "Tor"){
                        state = "Xor";
                    }
                    else{
                        state = "Tor";
                    }
                }
                return;
            }
        }
        System.out.println("Invalid move");
    }
    public String getState(){
        return state;
    }
}
