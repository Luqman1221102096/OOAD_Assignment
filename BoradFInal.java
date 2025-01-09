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

        // Initialize pieces in HashMap with owner information
        for (int col = 0; col < COLUMNS; col++) {
            // Red pieces in the first row (player one)
            if (col == 0) {
                pieceMap.put("0," + col, new Piece(redTor, true));
            } else if (col == 1) {
                pieceMap.put("0," + col, new Piece(redBiz, true));
            } else if (col == 2) {
                pieceMap.put("0," + col, new Piece(redSau, true));
            } else if (col == 3) {
                pieceMap.put("0," + col, new Piece(redBiz, true));
            } else if (col == 4) {
                pieceMap.put("0," + col, new Piece(redXor, true));
            }

            // Red Rams in the second row (player one)
            pieceMap.put("1," + col, new Piece(redRam, true));

            // Blue pieces in the last row (player two)
            if (col == 0) {
                pieceMap.put((ROWS - 1) + "," + col, new Piece(blueXor, false));
            } else if (col == 1) {
                pieceMap.put((ROWS - 1) + "," + col, new Piece(blueBiz, false));
            } else if (col == 2) {
                pieceMap.put((ROWS - 1) + "," + col, new Piece(blueSau, false));
            } else if (col == 3) {
                pieceMap.put((ROWS - 1) + "," + col, new Piece(blueBiz, false));
            } else if (col == 4) {
                pieceMap.put((ROWS - 1) + "," + col, new Piece(blueTor, false));
            }

            // Blue Rams in the second-to-last row (player two)
            pieceMap.put((ROWS - 2) + "," + col, new Piece(blueRam, false));
        }

        
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                JButton cell = new JButton();
                cell.setBackground((row + col) % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
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
        String[] oldPos = oldKey.split(",");
        String[] newPos = newKey.split(",");

        int oldRow = Integer.parseInt(oldPos[0]);
        int oldCol = Integer.parseInt(oldPos[1]);
        int newRow = Integer.parseInt(newPos[0]);
        int newCol = Integer.parseInt(newPos[1]);

        boardButtons[oldRow][oldCol].setIcon(null);
        boardButtons[newRow][newCol].setIcon(pieceMap.get(newKey).icon);
    }

    private void switchPlayer() {
        isPlayerOneTurn = !isPlayerOneTurn;
        statusLabel.setText(isPlayerOneTurn ? "Player 1's turn" : "Player 2's turn");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Board(8, 5));
    }

    // Define Piece class
    private class Piece {
        ImageIcon icon;
        boolean isPlayerOne;

        public Piece(ImageIcon icon, boolean isPlayerOne) {
            this.icon = icon;
            this.isPlayerOne = isPlayerOne;
        }
    }
}
