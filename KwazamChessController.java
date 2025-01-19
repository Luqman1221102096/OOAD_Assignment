//KwazamChessController.java
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

class KwazamChessController {
    private KwazamChessModel model; //logic and state from KCModel.java
    private KwazamChessView view;   //display of the game from KCView.java
    private MementoHistory history; // stores the mementos

    //constructor for controller
    public KwazamChessController(KwazamChessModel model, KwazamChessView view) {
        this.model = model;
        this.view = view;
        this.history = new MementoHistory();

    }

    //start the game and play turn by turn
    public void startGame() {
        Scanner scanner = new Scanner(System.in);

        while (true) { //users turn logic
            history.addMemento(new Memento(model.getPieces(), model.isBlueTurn(), model.getTurn()));// Stores previous state

            view.showBoard(model.getPieces());
            System.out.println("Turn: " + model.getTurn());
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

            view.showBoard(model.getPieces()); // Shows that the move actuall changes the board before undo.
            // This is just to test memento
            if(model.getTurn() != 1){
                System.out.println("Undo move? y/n");
                String undo = scanner.nextLine();
                if(undo.equals("y") || undo.equals("Y")){
                    System.out.println("Restoring game state");
                    model.restoreState(history.getMemento());
                }
            }
            model.safeGame();
        }
    }

    // Load game and start playing from that position
    public void loadGame() {
        model.loadGameState();
        startGame();
    }
}