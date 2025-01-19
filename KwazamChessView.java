import java.util.List;

//displaying the board and messages to the user
class KwazamChessView {
    public void showBoard(List<Piece> pieces) {
        String[][] board = new String[8][5]; //create an empty 8x5 board

        //fill the board with blank spaces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = "   ";
            }
        }

        //place pieces on the board
        for (Piece piece : pieces) {
            board[piece.getCoordinateY()][piece.getCoordinateX()] = piece.getPieceID();
        }

        //X-axis labels
        System.out.println("     X");//label X
        System.out.print("    ");
        for (int x = 0; x < 5; x++) {
            System.out.printf(" %2d ", x);
        }
        System.out.println();

        //Y-axis labels
        for (int y = 0; y < 8; y++) {
            System.out.printf(" %2d ", y);
            for (int x = 0; x < 5; x++) {
                System.out.print("|" + board[y][x]);
            }
            System.out.println("|");
        }

        //label Y
        System.out.println("   Y");
    }

    //sshow a message to the user (also testing text output)
    public void showMessage(String message) {
        System.out.println(message);
    }
}
