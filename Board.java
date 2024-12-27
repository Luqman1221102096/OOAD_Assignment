
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board {
    private JFrame frame;
    private JPanel boardPanel;
    private JLabel statusLabel;

    // Board dimensions
    private final int ROWS = 8;
    private final int COLUMNS = 5;

    public Board() {
        initializeGUI();
    }

    private void initializeGUI() {
        // Create the main frame
        frame = new JFrame("Kwazam Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save Game");
        JMenuItem loadItem = new JMenuItem("Load Game");
        JMenuItem exitItem = new JMenuItem("Exit");

        saveItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Save functionality not implemented yet!"));
        loadItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Load functionality not implemented yet!"));
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        // Create the board panel
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(ROWS, COLUMNS));
        initializeBoard();

        // Create a status panel
        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel("Player 1's turn");
        statusPanel.add(statusLabel);

        // Add components to the frame
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(statusPanel, BorderLayout.SOUTH);

        // Make the frame visible
        frame.setVisible(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                JButton cell = new JButton();
                cell.setBackground((row + col) % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);

                cell.addActionListener(new CellClickListener(row, col));
                boardPanel.add(cell);
            }
        }
    }

    private class CellClickListener implements ActionListener {
        private int row;
        private int col;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            String message = "Clicked cell at row " + row + ", column " + col;
            statusLabel.setText(message);

            JOptionPane.showMessageDialog(frame, message);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Board::new);
    }
}
