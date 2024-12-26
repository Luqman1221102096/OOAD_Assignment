import java.util.ArrayList;
import java.util.List;

public class sau extends Piece {
    sau(String pieceID, String side, int x, int y) {
        super(pieceID, side, x, y);
    }

    // Returns a list of possible moves for the sau
    public List<int[]> getMoves() {
        List<int[]> moves = new ArrayList<>();

        // Add all possible moves within one grid
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // Skip the current position (no movement)
                if (dx == 0 && dy == 0) {
                    continue;
                }

                int newX = this.x + dx;
                int newY = this.y + dy;

                // Ensure the move is within bounds (e.g., 0 <= x, y < boardSize)
                if (isWithinBounds(newX, newY)) {
                    moves.add(new int[]{newX, newY});
                }
            }
        }

        return moves;
    }

    // Checks if a given position is within board bounds (assume 8x8 board for chess)
    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    // Checks if the sau is captured
    public boolean isCaptured(int opponentX, int opponentY) {
        return this.x == opponentX && this.y == opponentY;
    }

    public static void main(String[] args) {
        sau sau = new sau("sau1", "White", 4, 4);

        // Test getMoves method
        List<int[]> moves = sau.getMoves();
        System.out.println("Possible moves:");
        for (int[] move : moves) {
            System.out.println("(" + move[0] + ", " + move[1] + ")");
        }

        // Test isCaptured method
        boolean captured = sau.isCaptured(4, 4); // Simulating an opponent at the sau's position
        System.out.println("Is sau captured? " + captured);
    }
}

