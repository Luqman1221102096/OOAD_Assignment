import java.util.List;

//displaying the board and messages to the user
class KwazamChessView {
    public void showBoard(List<Piece> pieces) {
        String[][] board = new String[8][5]; //create empty 8x5 board

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

        //print the board row by row (terminal output for testing)
        for (String[] row : board) {
            for (String cell : row) {
                System.out.print("|" + cell);
            }
            System.out.println("|");
        }
    }

    //show a message to the user (alsotesting text output)
    public void showMessage(String message) {
        System.out.println(message);
    }
}