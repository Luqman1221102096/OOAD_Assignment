import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;

public class Board {
    private JFrame frame;
    private JPanel boardPanel;
    private JLabel statusLabel;

    private final int ROWS;
    private final int COLUMNS;
    private int turnCount = 0;

    private HashMap<String, Piece> pieceMap; 
    private JButton[][] boardButtons; 

    private int selectedPieceRow = -1;
    private int selectedPieceCol = -1;
    private boolean isPlayerOneTurn = false;

    public Board(int rows, int columns) {
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.pieceMap = new HashMap<>();
        this.boardButtons = new JButton[ROWS][COLUMNS];
        initializeGUI();
    }

    private void initializeGUI() {
    frame = new JFrame("Kwazam Chess");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setLayout(new BorderLayout());

    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem exitItem = new JMenuItem("Exit");
    exitItem.addActionListener(e -> System.exit(0));
    fileMenu.add(exitItem);
    menuBar.add(fileMenu);
    frame.setJMenuBar(menuBar);

    boardPanel = new JPanel();
    boardPanel.setLayout(new GridLayout(ROWS, COLUMNS));
    initializeBoard();

    JPanel statusPanel = new JPanel();
    statusLabel = new JLabel("Player 1's turn");
    statusPanel.add(statusLabel);

    frame.add(boardPanel, BorderLayout.CENTER);
    frame.add(statusPanel, BorderLayout.SOUTH);

    frame.setVisible(true);
}

private void initializeBoard() {
    // Load icons for red (player one) pieces
    ImageIcon redRam = preScaledIcon("r-Ram.png");
    ImageIcon redBiz = preScaledIcon("r-Biz.png");
    ImageIcon redSau = preScaledIcon("r-Sau.png");
    ImageIcon redTor = preScaledIcon("r-Tor.png");
    ImageIcon redXor = preScaledIcon("r-Xor.png");

    // Load icons for blue (player two) pieces
    ImageIcon blueRam = preScaledIcon("b-Ram.png");
    ImageIcon blueBiz = preScaledIcon("b-Biz.png");
    ImageIcon blueSau = preScaledIcon("b-Sau.png");
    ImageIcon blueTor = preScaledIcon("b-Tor.png");
    ImageIcon blueXor = preScaledIcon("b-Xor.png");

    // Initialize pieces in HashMap with owner and type information
    for (int col = 0; col < COLUMNS; col++) {
        // Red pieces in the first row (player one)
        if (col == 0) {
            pieceMap.put("0," + col, new Piece(redTor, true, "Tor"));
        } else if (col == 1) {
            pieceMap.put("0," + col, new Piece(redBiz, true, "Biz"));
        } else if (col == 2) {
            pieceMap.put("0," + col, new Piece(redSau, true, "Sau"));
        } else if (col == 3) {
            pieceMap.put("0," + col, new Piece(redBiz, true, "Biz"));
        } else if (col == 4) {
            pieceMap.put("0," + col, new Piece(redXor, true, "Xor"));
        }

        // Red Rams in the second row (player one)
        pieceMap.put("1," + col, new Piece(redRam, true, "Ram"));

        // Blue pieces in the last row (player two)
        if (col == 0) {
            pieceMap.put((ROWS - 1) + "," + col, new Piece(blueXor, false, "Xor"));
        } else if (col == 1) {
            pieceMap.put((ROWS - 1) + "," + col, new Piece(blueBiz, false, "Biz"));
        } else if (col == 2) {
            pieceMap.put((ROWS - 1) + "," + col, new Piece(blueSau, false, "Sau"));
        } else if (col == 3) {
            pieceMap.put((ROWS - 1) + "," + col, new Piece(blueBiz, false, "Biz"));
        } else if (col == 4) {
            pieceMap.put((ROWS - 1) + "," + col, new Piece(blueTor, false, "Tor"));
        }

        // Blue Rams in the second-to-last row (player two)
        pieceMap.put((ROWS - 2) + "," + col, new Piece(blueRam, false, "Ram"));
    }

    for (int row = 0; row < ROWS; row++) {
        for (int col = 0; col < COLUMNS; col++) {
            JButton cell = new JButton();
            String key = row + "," + col;

            // Set initial icon
            if (pieceMap.containsKey(key)) {
                cell.setIcon(pieceMap.get(key).icon);
            }

            cell.addActionListener(new CellClickListener(row, col));
            boardPanel.add(cell);
            boardButtons[row][col] = cell;
        }
    }
}

    private ImageIcon preScaledIcon(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image image = icon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            return null;
        }
    }

    private class CellClickListener implements ActionListener {
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
                if (pieceMap.containsKey(key)) {
                    Piece piece = pieceMap.get(key);
                    if (piece.isPlayerOne == isPlayerOneTurn) {
                        selectedPieceRow = row;
                        selectedPieceCol = col;
                        statusLabel.setText("Selected piece at (" + row + ", " + col + ")");
                    } else {
                        statusLabel.setText("Not your turn or not your piece.");
                    }
                }
            } else {
                // Second click: Move the piece
                String oldKey = selectedPieceRow + "," + selectedPieceCol;
                Piece movingPiece = pieceMap.get(oldKey);

                if (movingPiece != null && movingPiece.isPlayerOne == isPlayerOneTurn) {
                    if (isValidMove(selectedPieceRow, selectedPieceCol, row, col)) {
                        pieceMap.remove(oldKey);
                        pieceMap.put(key, movingPiece);
                        updateBoard(oldKey, key);
                        statusLabel.setText("Moved piece to (" + row + ", " + col + ")");
                        switchPlayerAndFlip(); 
                        selectedPieceRow = -1;
                        selectedPieceCol = -1;
                    } else {
                        statusLabel.setText("Invalid move. Try again.");
                    }
                } else {
                    statusLabel.setText("Not your turn or not your piece.");
                }
            }
        }
    
        private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
            if (toRow < 0 || toRow >= ROWS || toCol < 0 || toCol >= COLUMNS) {
                return false;
            }
    
            String destinationKey = toRow + "," + toCol;
            if (pieceMap.containsKey(destinationKey)) {
                return false;
            }
    
            // Add move validation logic here (e.g., piece movement rules)
    
            return true;
        }
    }

    private void updateBoard(String oldKey, String newKey) {
        if (oldKey != null && newKey != null) {
            int oldRow = Integer.parseInt(oldKey.split(",")[0]);
            int oldCol = Integer.parseInt(oldKey.split(",")[1]);
            int newRow = Integer.parseInt(newKey.split(",")[0]);
            int newCol = Integer.parseInt(newKey.split(",")[1]);
    
            boardButtons[oldRow][oldCol].setIcon(null);
            boardButtons[newRow][newCol].setIcon(pieceMap.get(newKey).icon);
        } else {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLUMNS; col++) {
                    String key = row + "," + col;
                    if (pieceMap.containsKey(key)) {
                        boardButtons[row][col].setIcon(pieceMap.get(key).icon);
                    } else {
                        boardButtons[row][col].setIcon(null);
                    }
                }
            }
        }
    }
    private void TorAndXor() {
        if (turnCount % 2 == 0) { // Only execute every two turns
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLUMNS; col++) {
                    Piece piece = pieceMap.get(row + "," + col);
                    if (piece != null) {
                        if (piece.type.equals("Tor")) {
                            if (piece.isPlayerOne) {
                                pieceMap.put(row + "," + col, new Piece(preScaledIcon("resources/r-Xor.png"), true, "Xor"));
                            } else {
                                pieceMap.put(row + "," + col, new Piece(preScaledIcon("resources/b-Xor.png"), false, "Xor"));
                            }
                        } else if (piece.type.equals("Xor")) {
                            if (piece.isPlayerOne) {
                                pieceMap.put(row + "," + col, new Piece(preScaledIcon("resources/r-Tor.png"), true, "Tor"));
                            } else {
                                pieceMap.put(row + "," + col, new Piece(preScaledIcon("resources/b-Tor.png"), false, "Tor"));
                            }
                        }
                    }
                }
            }
            updateBoard(null, null); // Refresh the board to display changes
        }
    }


    private void switchPlayerAndFlip() {
        turnCount++;
        TorAndXor();
        HashMap<String, Piece> newPieceMap = new HashMap<>();
        for (String key : pieceMap.keySet()) {
            Piece piece = pieceMap.get(key);
           
            String[] coordinates = key.split(",");
            int oldRow = Integer.parseInt(coordinates[0]);
            int oldCol = Integer.parseInt(coordinates[1]);
    
            // Flip the row for position
            int newRow = ROWS - 1 - oldRow;
            String newKey = newRow + "," + oldCol;
    
            // Update piece icon based on flipped ownership
            updatePieceIcon(piece);
    
            // Place the piece in the new piece map with the updated ownership and position
            newPieceMap.put(newKey, piece);
        }
        
        // Update the piece map and refresh the board
        pieceMap = newPieceMap;
        updateBoard(null, null);
    
        // Switch player turn
        isPlayerOneTurn = !isPlayerOneTurn;
        statusLabel.setText(isPlayerOneTurn ? "Player 1's turn" : "Player 2's turn");
    }

    private void updatePieceIcon(Piece piece) {
        // Based on the flipped ownership, update the piece's icon
        if (piece.isPlayerOne) {
            switch (piece.type) {
                case "Tor":
                    piece.icon = preScaledIcon("r-Tor.png");
                    break;
                case "Biz":
                    piece.icon = preScaledIcon("r-Biz.png");
                    break;
                case "Sau":
                    piece.icon = preScaledIcon("r-Sau.png");
                    break;
                case "Xor":
                    piece.icon = preScaledIcon("r-Xor.png");
                    break;
                case "Ram":
                    piece.icon = preScaledIcon("r-Ram.png");
                    break;
                default:
                    break;
            }
        } else {
            switch (piece.type) {
                case "Tor":
                    piece.icon = preScaledIcon("b-Tor.png");
                    break;
                case "Biz":
                    piece.icon = preScaledIcon("b-Biz.png");
                    break;
                case "Sau":
                    piece.icon = preScaledIcon("b-Sau.png");
                    break;
                case "Xor":
                    piece.icon = preScaledIcon("b-Xor.png");
                    break;
                case "Ram":
                    piece.icon = preScaledIcon("b-Ram.png");
                    break;
                default:
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Board(8, 5));
    }
    private class Move {
        Piece piece;
        int fromRow, fromCol, toRow, toCol;

        public Move(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {
            this.piece = piece;
            this.fromRow = fromRow;
            this.fromCol = fromCol;
            this.toRow = toRow;
            this.toCol = toCol;
        }
    }
    // Define Piece class
    private class Piece {
        ImageIcon icon;
        boolean isPlayerOne;
        String type;
    
        public Piece(ImageIcon icon, boolean isPlayerOne, String type) {
            this.icon = icon;
            this.isPlayerOne = isPlayerOne;
            this.type = type;
        }
    }
}
