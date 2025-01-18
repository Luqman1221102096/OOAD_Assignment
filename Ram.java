import java.util.ArrayList;
import java.util.List;

//rams move forward and turns back when they reach the edge of the board
class Ram extends Piece {
    private boolean movingForward = true; //track direction of movement
    public Ram(String pieceID, String side, int x, int y) {
        super(pieceID, side, x, y);
    }

    @Override
    public List<int[]> getMoves(List<Piece> pieces) {
        List<int[]> moves = new ArrayList<>();

        if (side.equals("Blue")) {
            int newX = x;
            int newY = movingForward ? y - 1 : y + 1; //move up if movingForward = true, move down if movingForward = false

            //make sure the move stays on the board (8x5 grid) (i also added the boundary checker in KCModel.java)
            if (newX >= 0 && newX < 5 && newY >= 0 && newY < 8) {
                moves.add(new int[]{newX, newY});
            } else {
                //start moving downwards (change movingForward = true to false)
                movingForward = !movingForward;
            }
        } else if (side.equals("Red")) {
            int newX = x;
            int newY = movingForward ? y + 1 : y - 1; //move down if movingForward = true, move up if movingForward = false

            //make sure the move stays on the board (8x5 grid) (i also added the boundary checker in KCModel.java)
            if (newX >= 0 && newX < 5 && newY >= 0 && newY < 8) {
                moves.add(new int[]{newX, newY});
            } else {
                //start moving updwards (change movingForward = true to false)
                movingForward = !movingForward;
            }
        }

        return moves;
    }
}
