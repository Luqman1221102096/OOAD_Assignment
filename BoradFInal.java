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
        private boolean isPlayerOneTurn = true;
    
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
        Controller controller = new Controller(this);
    
            // Load icons for red (player one) pieces
            ImageIcon redRam = preScaledIcon("resources/r-Ram.png");
            ImageIcon redBiz = preScaledIcon("resources/r-Biz.png");
            ImageIcon redSau = preScaledIcon("resources/r-Sau.png");
            ImageIcon redTor = preScaledIcon("resources/r-Tor.png");
            ImageIcon redXor = preScaledIcon("resources/r-Xor.png");
        
            // Load icons for blue (player two) pieces
            ImageIcon blueRam = preScaledIcon("resources/b-Ram.png");
            ImageIcon blueBiz = preScaledIcon("resources/b-Biz.png");
            ImageIcon blueSau = preScaledIcon("resources/b-Sau.png");
            ImageIcon blueTor = preScaledIcon("resources/b-Tor.png");
            ImageIcon blueXor = preScaledIcon("resources/b-Xor.png");
        
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
                    cell.setBackground((row + col) % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                    String key = row + "," + col;
    
                    if (pieceMap.containsKey(key)) {
                        cell.setIcon(pieceMap.get(key).icon);
                    }
    
                    cell.addActionListener(new CellClickListener(row, col, controller));
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
        private class Controller {
            private Board board;
    
            public Controller(Board board) {
                this.board = board;
            }
    
            public void handleCellClick(int row, int col) {
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
                    // Allow the user to reselect another piece
                    if (pieceMap.containsKey(key) && pieceMap.get(key).isPlayerOne == isPlayerOneTurn) {
                        selectedPieceRow = row;
                        selectedPieceCol = col;
                        statusLabel.setText("Reselected piece at (" + row + ", " + col + ")");
                        return;
                    }
            
                    // Second click: Move the piece
                    String oldKey = selectedPieceRow + "," + selectedPieceCol;
                    Piece movingPiece = pieceMap.get(oldKey);
            
                    if (movingPiece != null && movingPiece.isPlayerOne == isPlayerOneTurn) {
                        if (isValidMove(movingPiece, selectedPieceRow, selectedPieceCol, row, col)) {
                            // Remove the opponent's piece if it's a capture move
                            String destinationKey = row + "," + col;
                            if (pieceMap.containsKey(destinationKey)) {
                                Piece opponentPiece = pieceMap.get(destinationKey);
                                if (opponentPiece.isPlayerOne != isPlayerOneTurn) {
                                    pieceMap.remove(destinationKey);
                                }
                            }
                            // Move the piece
                            pieceMap.remove(oldKey);
                            pieceMap.put(destinationKey, movingPiece);
                            updateBoard(oldKey, destinationKey);
                            statusLabel.setText("Moved piece to (" + row + ", " + col + ")");
                            selectedPieceRow = -1;
                            selectedPieceCol = -1;
                            switchPlayer();
                        } else {
                            statusLabel.setText("Invalid move. Try again.");
                        }
                    } else {
                        statusLabel.setText("Not your turn or not your piece.");
                    }
                }
            }
            
    
            private boolean isValidMove(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {
                if (toRow < 0 || toRow >= ROWS || toCol < 0 || toCol >= COLUMNS) {
                    return false;
                }
    
                String destinationKey = toRow + "," + toCol;
                Piece destinationPiece = pieceMap.get(destinationKey);
    
                if (destinationPiece != null && destinationPiece.isPlayerOne == piece.isPlayerOne) {
                    return false; // Cannot move to a cell occupied by a friendly piece
                }
    
                switch (piece.type) {
                    case "Tor":
                        return isTorMoveValid();
                    case "Biz":
                        return isBizMoveValid();
                    case "Sau":
                        return isSauMoveValid();
                    case "Ram":
                        return isRamMoveValid();
                    case "Xor":
                        return isXorMoveValid();
                    default:
                        return false;
                }
            }
    
            private boolean isTorMoveValid(){}
    
            private boolean isBizMoveValid(){}
    
            private boolean isSauMoveValid(){}
    
            private boolean isRamMoveValid(){}
    
            private boolean isXorMoveValid(){}
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
        
    
        private class CellClickListener implements ActionListener {
            private int row;
            private int col;
            private Controller controller;
    
            public CellClickListener(int row, int col, Controller controller) {
                this.row = row;
                this.col = col;
                this.controller = controller;
            }
    
            public void actionPerformed(ActionEvent e) {
                controller.handleCellClick(row, col);
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

    private void switchPlayer() {
        isPlayerOneTurn = !isPlayerOneTurn;
        turnCount++;
        TorAndXor();
        statusLabel.setText(isPlayerOneTurn ? "Player 1's turn" : "Player 2's turn");
    } 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Board(8, 5));
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
