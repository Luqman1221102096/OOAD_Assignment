// Tor and Xor piece class
// Tor moves orthogonally up untill it reaches the edge of the board
// Xor moves diagonally in any direction until it reaches the edge of the board
// Since Tor and Xor turn into one another every 2 turns they are practically the same entity.
// Its simple for them to be implemented in the same class.
// Author: Luqman
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
public class TorXor extends Piece {
    // When turn is two, which to Xor or Tor depending on previous state
    int turn = 0;
    String state;
    TorXor(String pieceID, String side, int x, int y, String state){
        super(pieceID, side, x, y);
        this.state = state;
    }
    // Copy Constructor
    public TorXor(Piece piece){
        super(piece);
        this.turn = ((TorXor) piece).turn;
        this.state = ((TorXor) piece).state;
    }
    // Gets the list of legal moves of the Tor and Xor pieces
    public List<int[]> getMoves(List<Piece> pieces){
        List<int[]> moves = new ArrayList<int[]>();
        List<int[]> moves2 = new ArrayList<int[]>();
        // Gets the list of legal moves of the Tor piece
        if(state == "Tor"){
            // Horizontal: the y coordinate stay still as we loop through the x coordinates and add the Squares
            for (int i = 0; i < 5; i++){
                if(i == this.x){continue;}// Skips current square
                if(pieceAt(i, this.y, pieces)){
                    if(this.x-i < 0){
                        break;
                    }
                    else{
                        moves.clear();
                    }
                }
                moves.add(new int[]{i,this.y});
            }
            // Vertical the x coordinate stay still as we loop through the y coordinates and add the Squares
            for (int i = 0; i < 8; i++){
                if(i == this.y){continue;}// Skips current square
                if(pieceAt(this.x, i, pieces)){
                    if(this.y-i < 0){
                        break;
                    }
                    else{
                        moves2.clear();
                    }
                }
                moves2.add(new int[]{this.x,i});
            List<int[]> combinedMoves = Stream.concat(moves.stream(), moves2.stream()).toList();
            return combinedMoves;
            }
        }
        else{
            // Gets the list of legal moves of the Xor piece
            // The algorithm loops through all the x coordinates and adjusts the y coordinates with yCoordinatePlus
            // Since the y coordinate increases/decrease by 1 for every 1 square we move away from current x we can calculate the y coordinate at the left edge of the board as current x coordinte - 1 or this.x-1.
            // yCoordinatePlus is decreased as it approches the current x coordinate. Once it passes it, the yCoordinatePlus is increase as we go in the opposite direction.
            int yCoordinatePlus = this.x;// Set yCoordinatePlus
            boolean stopBottomMoves = false;
            boolean stopTopMoves = false;

            for (int i = 0; i < 5; i++){
                // Skips the current square and adds 1 to yCoordinatePlus so it doesn't add 0 on the next loop
                if(i == this.x){
                    yCoordinatePlus++;
                    continue;
                }
                // Adding lower diagonal moves with bound checking
                if(this.y+yCoordinatePlus < 8 && !stopBottomMoves){
                    if(pieceAt(i, this.y+yCoordinatePlus, pieces)){
                        if(this.x-i < 0){
                            stopBottomMoves = true;
                        }
                        else{
                            moves.clear();
                        }
                    }
                    moves.add(new int[]{i,this.y+yCoordinatePlus});
                }
                // Adding upper diagonal moves with bound checking
                if(this.y-yCoordinatePlus >= 0 && !stopTopMoves){
                    if(pieceAt(i, this.y-yCoordinatePlus, pieces)){
                        if(this.x-i < 0){
                            stopTopMoves = true;
                        }
                        else{
                            moves2.clear();
                        }
                    }
                    moves2.add(new int[]{i,this.y-yCoordinatePlus});
                }
                // Begin adding to yCoordinatePlus once it has passed the current square
                if(this.x > i){
                    yCoordinatePlus--;
                }
                else{
                    yCoordinatePlus++;
                }
            }
            List<int[]> combinedMoves = Stream.concat(moves.stream(), moves2.stream()).toList();
            return combinedMoves;
        }
        return moves;
    }
    public void updateTurn(){
        turn++;
        if(turn == 2){
            turn = 0;
            if(state == "Tor"){
                state = "Xor";
                this.setPieceID(pieceID.substring(0,1) + "X" + pieceID.substring(2,3));
            }
            else{
                state = "Tor";
                this.setPieceID(pieceID.substring(0,1) + "T" + pieceID.substring(2,3));
            }
        }
    }
    public String getState(){
        return state;
    }
    public boolean pieceAt(int x, int y, List<Piece> pieces) {
        for (Piece piece : pieces) {
            if (piece.getCoordinateX() == x && piece.getCoordinateY() == y) {
                return true;
            }
        }
        return false; //no piece is at this location
    }
}

