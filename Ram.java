// Hi luqman here This was just what i made quickly
import java.util.ArrayList;
import java.util.List;

public class Ram extends Piece {
    Ram(String pieceID, String side, int x, int y){
        super(pieceID, side, x, y);
    }
    public List<int[]> getMoves(){
        List<int[]> moves = new ArrayList<int[]>();

        moves.add(new int[]{this.x,this.y+1});
        return moves;
    }
    // We can probablt do something like this to test the object logic.
    public static void main(String[] args){
        Ram r1 = new Ram("Ram1", "Red", 1, 2);
        List<int[]> list = r1.getMoves();
        // Y coordinate
        System.out.println(list.get(0)[1]);
        System.out.println("Test");
    }
    //public updateMove
}
