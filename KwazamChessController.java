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

    // Load game and update the GUI state
    public void loadGame() {
        model.loadGameState(); // Load the saved game state into the model
        view.loadBoard(model.getPieces(), model.getTurn()); // Update the GUI to reflect the loaded state
    }

    public void saveGame() {
        model.safeGame(); //save game state
    }

    /*// undo game and update the GUI state
    public void undoGame() {
        memento.restoreState(); // undo the game
        view.loadBoard(model.getPieces(), model.getTurn()); // Update the GUI to reflect the loaded state
    }*/

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