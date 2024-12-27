import java.util.ArrayList;
import java.util.List;

//Sau piece moves like a king in chess
class Sau extends Piece {
    public Sau(String pieceID, String side, int x, int y) {
        super(pieceID, side, x, y);
    }

    @Override
    public List<int[]> getMoves(List<Piece> pieces) {
        List<int[]> moves = new ArrayList<>();
        //single-step moves in any direction
        int[][] deltas = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}, //horizontal and vertical moves
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1} //diagonal moves
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
