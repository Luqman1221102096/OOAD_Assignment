import java.util.ArrayList;
import java.util.List;

//Biz pieces move like knights in chess
// Author Arif
class Biz extends Piece {
    public Biz(String pieceID, String side, int x, int y) {
        super(pieceID, side, x, y);
    }
    // Copy Constructor
    public Biz(Piece piece){super(piece);}
    
    @Override
    public List<int[]> getMoves(List<Piece> pieces) {
        List<int[]> moves = new ArrayList<>();
        //all the possible knight-like moves (any direction "L")
        int[][] deltas = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] delta : deltas) {
            int newX = x + delta[0];
            int newY = y + delta[1];
            //make sure the move stays on the board (8x5 grid) (i also added the boundary checker in KCModel.java)
            if (newX >= 0 && newX < 5 && newY >= 0 && newY < 8) {
                moves.add(new int[]{newX, newY});
            }
        }

        return moves;
    }
}