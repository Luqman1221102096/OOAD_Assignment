//KwazamChessController.java
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

class KwazamChessController {
    private KwazamChessModel model; //logic and state from KCModel.java
    private KwazamChessView view;   //display of the game from KCView.java

    //constructor for controller
    public KwazamChessController(KwazamChessModel model, KwazamChessView view) {
        this.model = model;
        this.view = view;
    }

    //start the game and play turn by turn
    public void startGame() {
        Scanner scanner = new Scanner(System.in);

        while (true) { //users turn logic
            view.showBoard(model.getPieces());
            String currentPlayer = model.isBlueTurn() ? "Blue" : "Red";
            System.out.println(currentPlayer + "'s turn.");

            System.out.print("Piece: ");
            String pieceID = scanner.nextLine();

            System.out.print("Move (x, y): ");
            String moveInput = scanner.nextLine();
            String[] moveParts = moveInput.split(",");

            try {
                int x = Integer.parseInt(moveParts[0].trim());
                int y = Integer.parseInt(moveParts[1].trim());

                if (model.movePiece(pieceID, x, y)) {
                    model.endTurn();
                } else {
                    System.out.println("Invalid move. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input format. Please enter in the format <x, y>.");
            }
        }
    }

    // Load game and start playing from that position
    public void loadGame() {
        model.loadGameState();
        startGame();
    }
}