import java.util.ArrayList;
import java.util.List;

//Rams can only move forward
class Ram extends Piece {
    public Ram(String pieceID, String side, int x, int y) {
        super(pieceID, side, x, y);
    }

    @Override
    public List<int[]> getMoves(List<Piece> pieces) {
        List<int[]> moves = new ArrayList<>();
        //rams move forward, the direction depends on their side
        if (side.equals("Blue")) {
            int newX = x;
            int newY = y - 1; //blue moves up the board
            //make sure the move stays on the board (8x5 grid) (i also added the boundary checker in KCModel.java)
            if (newX >= 0 && newX < 5 && newY >= 0 && newY < 8) {
                moves.add(new int[]{newX, newY});
            }
        } else if (side.equals("Red")) {
            int newX = x;
            int newY = y + 1; //red moves down the board
            //make sure the move stays on the board (8x5 grid) (i also added the boundary checker in KCModel.java)
            if (newX >= 0 && newX < 5 && newY >= 0 && newY < 8) {
                moves.add(new int[]{newX, newY});
            }
        }
        return moves;
    }
}
