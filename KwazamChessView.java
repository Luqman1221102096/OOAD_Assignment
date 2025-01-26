import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class KwazamChessView {
    private JFrame frame;
    private JPanel boardPanel;
    private JLabel statusLabel;
    //private KwazamChessController controller;
    
    
    private final int ROWS;
    private final int COLUMNS;
    private int turnCount = 0;

    private HashMap<String, ImagePiece> pieceMap; 
    private JButton[][] boardButtons; 
    private List<JMenuItem> menuItems;
    // Author Balfaqih
    public KwazamChessView(int rows, int columns) {
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.pieceMap = new HashMap<>();
        this.boardButtons = new JButton[ROWS][COLUMNS];
        this.menuItems = new ArrayList<JMenuItem>(); 
        initializeGUI();
    }

    // Author Balfaqih, Luqman, Arif
    public void initializeGUI() {
        frame = new JFrame("Kwazam Chess");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout());

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        menuItems.add(exitItem);

        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        JMenuItem saveGame = new JMenuItem("Save");
        menuItems.add(saveGame);
        fileMenu.add(saveGame);

        JMenuItem loadGame = new JMenuItem("Load");
        menuItems.add(loadGame);
        fileMenu.add(loadGame);

        JMenuItem undoGame = new JMenuItem("undo");
        menuItems.add(undoGame);
        fileMenu.add(undoGame);

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
    // Authors Balfaqih
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
                pieceMap.put("0," + col, new ImagePiece(redTor, false, "Tor"));
            } else if (col == 1) {
                pieceMap.put("0," + col, new ImagePiece(redBiz, false, "Biz"));
            } else if (col == 2) {
                pieceMap.put("0," + col, new ImagePiece(redSau, false, "Sau"));
            } else if (col == 3) {
                pieceMap.put("0," + col, new ImagePiece(redBiz, false, "Biz"));
            } else if (col == 4) {
                pieceMap.put("0," + col, new ImagePiece(redXor, false, "Xor"));
            }
    
            // Red Rams in the second row (player one)
            pieceMap.put("1," + col, new ImagePiece(redRam, false, "Ram"));
    
            // Blue pieces in the last row (player two)
            if (col == 0) {
                pieceMap.put((ROWS - 1) + "," + col, new ImagePiece(blueXor, true, "Xor"));
            } else if (col == 1) {
                pieceMap.put((ROWS - 1) + "," + col, new ImagePiece(blueBiz, true, "Biz"));
            } else if (col == 2) {
                pieceMap.put((ROWS - 1) + "," + col, new ImagePiece(blueSau, true, "Sau"));
            } else if (col == 3) {
                pieceMap.put((ROWS - 1) + "," + col, new ImagePiece(blueBiz, true, "Biz"));
            } else if (col == 4) {
                pieceMap.put((ROWS - 1) + "," + col, new ImagePiece(blueTor, true, "Tor"));
            }
    
            // Blue Rams in the second-to-last row (player two)
            pieceMap.put((ROWS - 2) + "," + col, new ImagePiece(blueRam, true, "Ram"));
        }

        int whiteBlack = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                JButton cell = new JButton();
                String key = row + "," + col;
    
                // Set initial icon
                if (pieceMap.containsKey(key)) {
                    cell.setIcon(pieceMap.get(key).icon);
                }

                // Colors the board in checker patterns
                if(whiteBlack % 2 == 0){
                    cell.setBackground(Color.WHITE);
                }else{
                    cell.setBackground(Color.BLACK);
                }
                whiteBlack++;
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                boardPanel.add(cell);
                boardButtons[row][col] = cell;
            }
        }
    }
    // Author Balfaqih, Luqman
    private ImageIcon preScaledIcon(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            // Blue's turn
            if(turnCount % 2 == 0){
                Image image = icon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            }else{
                //It's red's turn and flip the image icons
                Image image = icon.getImage();
                BufferedImage bufferedImage = new BufferedImage(55, 55, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                // Apply scaling transformation to fit the image within the BufferedImage
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                // Apply flipping and scaling transformation
                AffineTransform transform = new AffineTransform();
                // Flip vertically: scale y by -1, translate height to maintain position
                transform.scale(1, -1);
                transform.translate(0, -55); // Negative of the width for horizontal flip
                transform.scale(55.0 / image.getWidth(null), 55.0 / image.getHeight(null)); // Scale to fit

                // Apply the transformation while drawing the scaled image
                g2d.drawImage(image, transform, null);
                g2d.dispose();
                return new ImageIcon(bufferedImage);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            return null;
        }
    }
    //Author Balfaqih
    public class ImagePiece {
        ImageIcon icon;
        boolean isBlue;
        String type;
    
        public ImagePiece(ImageIcon icon, boolean isBlue, String type) {
            this.icon = icon;
            this.isBlue = isBlue;
            this.type = type;
        }
        public boolean getIsBlue(){
            return this.isBlue;
        }
        public String getType(){
            return this.type;
        }
    }
    // Author Luqman
    public HashMap<String, ImagePiece> getPieceMap(){
        return pieceMap;
    }
    // Author Luqman
    public JButton[][] getBoardButtons(){
        return boardButtons;
    }
    // Author Luqman
    public List<JMenuItem> getMenuItems(){
        return menuItems;
    }
    // Author Luqman
    public void updateStatusLabel(String label){
        statusLabel.setText(label);
    }
    // Author Balfaqih, Hao
    public void updateBoard(String oldKey, String newKey) {
        ImagePiece movingPiece = pieceMap.get(oldKey);

        if (oldKey != null && newKey != null) {
            pieceMap.remove(oldKey);
            pieceMap.put(newKey, movingPiece);
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
    // Author Bafalqih
    private void TorAndXor() {
        if (turnCount % 2 == 0) { // Only execute every two turns
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLUMNS; col++) {
                    ImagePiece piece = pieceMap.get(row + "," + col);
                    if (piece != null) {
                        if (piece.type.equals("Tor")) {
                            if (!piece.isBlue) {
                                pieceMap.put(row + "," + col, new ImagePiece(preScaledIcon("r-Xor.png"), false, "Xor"));
                            } else {
                                pieceMap.put(row + "," + col, new ImagePiece(preScaledIcon("b-Xor.png"), true, "Xor"));
                            }
                        } else if (piece.type.equals("Xor")) {
                            if (!piece.isBlue) {
                                pieceMap.put(row + "," + col, new ImagePiece(preScaledIcon("r-Tor.png"), false, "Tor"));
                            } else {
                                pieceMap.put(row + "," + col, new ImagePiece(preScaledIcon("b-Tor.png"), true, "Tor"));
                            }
                        }
                    }
                }
            }
            updateBoard(null, null); // Refresh the board to display changes
        }
    }
    // Author Hao
    public void switchPlayerAndFlip() {
        turnCount++;
        TorAndXor();
        HashMap<String, ImagePiece> newPieceMap = new HashMap<>();
        //System.out.println(pieceMap.keySet());
        for (String key : pieceMap.keySet()) {
            ImagePiece piece = pieceMap.get(key);
           
            String[] coordinates = key.split(",");
            int oldRow = Integer.parseInt(coordinates[0]);
            int oldCol = Integer.parseInt(coordinates[1]);
    
            // Flip the row for position
            int newRow = ROWS - 1 - oldRow;
            String newKey = newRow + "," + oldCol;
            //System.out.println(newKey);
            // Update piece icon based on flipped ownership
            updatePieceIcon(piece);
    
            // Place the piece in the new piece map with the updated ownership and position
            newPieceMap.put(newKey, piece);
        }
        
        // Update the piece map and refresh the board
        pieceMap = newPieceMap;
        updateBoard(null, null);
    
        // Switch player turn
        //statusLabel.setText(isPlayerOneTurn ? "Player 1's turn" : "Player 2's turn");
    }
    // Author Hao
    private void updatePieceIcon(ImagePiece piece) {
        // Based on the flipped ownership, update the piece's icon
        if (!piece.isBlue) {
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
    
    // Load the board from the model when loading a safefile or when undoing a move
    // Author Luqman
    public void loadBoard(List<Piece> pieces, int turnNumber){
        turnCount = turnNumber; 
        HashMap<String, ImagePiece> newPieceMap = new HashMap<String, ImagePiece>();
        for(Piece piece : pieces){
            if(piece instanceof Ram){
                ImageIcon newImageIcon = piece.getSide().equals("Blue") ? preScaledIcon("b-Ram.png") : preScaledIcon("r-Ram.png");
                ImagePiece newImagePiece = new ImagePiece(newImageIcon, (piece.getSide().equals("Blue") ? true : false), "Ram");
                newPieceMap.put(piece.getCoordinateY() + "," + piece.getCoordinateX(), newImagePiece);
            }
            else if(piece instanceof Sau){
                ImageIcon newImageIcon = piece.getSide().equals("Blue") ? preScaledIcon("b-Sau.png") : preScaledIcon("r-Sau.png");
                ImagePiece newImagePiece = new ImagePiece(newImageIcon, (piece.getSide().equals("Blue") ? true : false), "Sau");
                newPieceMap.put(piece.getCoordinateY() + "," + piece.getCoordinateX(), newImagePiece);
            }
            else if(piece instanceof Biz){
                ImageIcon newImageIcon = piece.getSide().equals("Blue") ? preScaledIcon("b-Biz.png") : preScaledIcon("r-Biz.png");
                ImagePiece newImagePiece = new ImagePiece(newImageIcon, (piece.getSide().equals("Blue") ? true : false), "Biz");
                newPieceMap.put(piece.getCoordinateY() + "," + piece.getCoordinateX(), newImagePiece);
            }
            else if(piece instanceof TorXor){
                ImagePiece newImagePiece;
                if(((TorXor) piece).getState() == "Tor"){
                    ImageIcon newImageIcon = piece.getSide().equals("Blue") ? preScaledIcon("b-Tor.png") : preScaledIcon("r-Tor.png");
                    newImagePiece = new ImagePiece(newImageIcon, (piece.getSide().equals("Blue") ? true : false), "Tor");
                }else{
                    ImageIcon newImageIcon = piece.getSide().equals("Blue") ? preScaledIcon("b-Xor.png") : preScaledIcon("r-Xor.png");
                    newImagePiece = new ImagePiece(newImageIcon, (piece.getSide().equals("Blue") ? true : false), "Xor");
                }
                newPieceMap.put(piece.getCoordinateY() + "," + piece.getCoordinateX(), newImagePiece);
            }
        }
        pieceMap = newPieceMap;
        updateBoard(null, null);
    }
    // Author Luqman
    public void GameOver(String whoWon){
        JFrame gameOver = new JFrame();
        JOptionPane.showMessageDialog(gameOver, whoWon, "WINNER!", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
}

