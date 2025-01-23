//KwazamChessController.java
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;

class KwazamChessController {
    private KwazamChessModel model; //logic and state from KCModel.java
    private KwazamChessView view;   //display of the game from KCView.java
    private MementoHistory history; // stores the mementos

    private int selectedPieceRow = -1;
    private int selectedPieceCol = -1;
    private final int ROWS;
    private final int COLUMNS;

    //constructor for controller
    public KwazamChessController(KwazamChessModel model, KwazamChessView view) {
        this.model = model;
        this.view = view;
        this.history = new MementoHistory();
        this.ROWS = 8;
        this.COLUMNS = 5; 
        cellListenerAdder();
    }

    //start the game and play turn by turn
    public void startGame() {
        Scanner scanner = new Scanner(System.in);

        while (true) { //users turn logic
            history.addMemento(new Memento(model.getPieces(), model.isBlueTurn(), model.getTurn()));// Stores previous state

            //view.showBoard(model.getPieces());
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

            //view.showBoard(model.getPieces()); // Shows that the move actuall changes the board before undo.
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
        view.loadBoard(model.getPieces(), model.getTurn());
        //startGame();
    }

    // Adds actionListeners to all cells
    public void cellListenerAdder(){
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                view.getBoardButtons()[row][col].addActionListener(new CellClickListener(row, col));
            }
        }
    }
    // Implementation of action listener
    public class CellClickListener implements ActionListener {
        private int row;
        private int col;
    
        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }
    
        public void actionPerformed(ActionEvent e) {
            String key = row + "," + col;

            if (selectedPieceRow == -1 && selectedPieceCol == -1) {
                // First click: Select a piece
                if (view.getPieceMap().containsKey(key)) {
                    boolean isBluePiece = view.getPieceMap().get(key).getIsBlue();
                    if (isBluePiece == model.isBlueTurn()) {
                        selectedPieceRow = row;
                        selectedPieceCol = col;
                        view.updateStatusLabel("Selected piece at (" + col + ", " + (model.isBlueTurn() ? row : ROWS - 1 - row ) + ")");
                    } else {
                        view.updateStatusLabel("Not your turn or not your piece.");
                    }
                }
            } else {
                // Second click: Move the piece
                String oldKey = selectedPieceRow + "," + selectedPieceCol;
                if (view.getPieceMap().get(oldKey) != null && view.getPieceMap().get(oldKey).getIsBlue() == model.isBlueTurn()) {
                    history.addMemento(new Memento(model.getPieces(), model.isBlueTurn(), model.getTurn()));
                    if (model.parseMove(selectedPieceRow, selectedPieceCol, row, col)) {
                        model.endTurn();
                        view.updateBoard(oldKey, key);
                        //view.updateStatusLabel("Moved piece to (" + col + ", " + row + ")");
                        view.switchPlayerAndFlip();
                        model.flipModel();
                        view.updateStatusLabel(model.isBlueTurn() ? "Player 1's turn" : "Player 2's turn"); 
                        model.safeGame();// Temporary just. Should be called in its own function.
                    } else {
                        view.updateStatusLabel("Invalid move. Try again.");
                    }
                } else {
                    view.updateStatusLabel("Not your turn or not your piece.");
                }
                selectedPieceRow = -1;
                selectedPieceCol = -1;
            }
        }
    }
}