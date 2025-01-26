//KwazamChessModel.java

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

class KwazamChessModel {
    private List<Piece> pieces; //list of all the pieces on the board
    private boolean blueTurn;      //logic for first turn (dont change this, only change in line 18)
    private int turnNumber;        //turn number
    private List<String> blueCaptured; //;ist of pieces captured by blue (//not working, prolly remove later...too eepy to fix)
    private List<String> redCaptured;  //list of pieces captured by red (//not working, prolly remove later...too eepy to fix)
    private boolean gameOver = false; // To let the controller and view know when a game is over.

    public KwazamChessModel() {
        pieces = new ArrayList<>();
        this.blueTurn = true; //blue = true / red = false (change this for first turn)
        this.turnNumber = 0;  //start turn number
        this.blueCaptured = new ArrayList<>();
        this.redCaptured = new ArrayList<>();
        setupBoard(); //place the pieces on the board
    }

    //set up all the pieces
    private void setupBoard() {
        //add Rams for both teams
        for (int i = 0; i < 5; i++) {
            pieces.add(new Ram("BR" + i, "Blue", i, 6)); // Blue Rams on row 6
            pieces.add(new Ram("RR" + i, "Red", i, 1));  // Red Rams on row 1
        }
        //add Sau and Biz pieces
        pieces.add(new Sau("BS ", "Blue", 2, 7));
        pieces.add(new Sau("RS ", "Red", 2, 0));
        pieces.add(new Biz("BB1", "Blue", 1, 7));
        pieces.add(new Biz("BB2", "Blue", 3, 7));
        pieces.add(new Biz("RB1", "Red", 1, 0));
        pieces.add(new Biz("RB2", "Red", 3, 0));
        // add Tor and Xor pieces
        pieces.add(new TorXor("BX1", "Blue", 0, 7, "Xor"));
        pieces.add(new TorXor("BT2", "Blue", 4, 7, "Tor"));
        pieces.add(new TorXor("RX1", "Red", 4, 0, "Xor"));
        pieces.add(new TorXor("RT2", "Red", 0, 0, "Tor"));
    }

    public boolean movePiece(String pieceID, int x, int y) {
        Piece piece = findPiece(pieceID);
        if (piece == null) {
            System.out.println("Piece not found.");
            return false;
        }

        if ((blueTurn && !piece.side.equals("Blue")) || (!blueTurn && !piece.side.equals("Red"))) {
            System.out.println("It's not this piece's turn.");
            return false;
        }

        if (isWithinBounds(x, y) &&
            (pieceAt(x, y) == null || !pieceAt(x, y).side.equals(piece.side)) &&
            piece.getMoves(pieces).stream().anyMatch(move -> move[0] == x && move[1] == y)) {

            executeMove(pieceID, x, y);
            return true;
        }

        return false;
    }

    public void endTurn() {
        blueTurn = !blueTurn; //switch turn
        turnNumber++;//+turnnumber
        for (Piece piece : pieces) { // Update Tor/Xor turn
            if (piece instanceof TorXor) {
                ((TorXor) piece).updateTurn(turnNumber);
            }
        }
    }

    public boolean isBlueTurn() {
        return blueTurn;
    }

    //get a list of all the pieces
    public List<Piece> getPieces() {
        return pieces;
    }

    //get a list of all the pieces
    public int getTurn() {
        return turnNumber;
    }

    public boolean getGameOver(){
        return gameOver;
    }

    //find piece by its ID
    public Piece findPiece(String pieceID) {
        for (Piece piece : pieces) {
            if (piece.getPieceID().equals(pieceID)) {
                return piece;
            }
        }
        return null; //if no piece matches the ID, return null
    }

    //remove a piece from the board when captured
    public void removePiece(Piece piece) {
        pieces.remove(piece);
        if (piece instanceof Sau) { //if Sau gets captured then game over
            gameOver = true;
        }
    }

    //check if there is a piece at the given coords
    public Piece pieceAt(int x, int y) {
        for (Piece piece : pieces) {
            if (piece.getCoordinateX() == x && piece.getCoordinateY() == y) {
                return piece;
            }
        }
        return null; //no piece is at this location
    }

    //board boundaries
    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < 5 && y >= 0 && y < 8;
    }

    //execution
    private void executeMove(String pieceID, int newX, int newY) {
        Piece piece = findPiece(pieceID);//find the piece by ID
        if (piece != null) {
            Piece target = pieceAt(newX, newY);//check if theres a piece at the target location
            if (target != null && !target.side.equals(piece.side)) {
                removePiece(target); // Capture an enemy piece if it's there
                if (blueTurn) {
                    blueCaptured.add(target.getPieceID());//add to blues captured list(not working)
                } else {
                    redCaptured.add(target.getPieceID());//add to reds captured list(not working)
                }
                System.out.println(piece.getPieceID() + " captured " + target.getPieceID() + "!");
            }
            piece.setCoordinates(newX, newY); //update the pieces position
        }
    }

    // Safe the game into a text file
    public void safeGame() {
        // Creates safe file if one doesn't already exit
        try {
            File file = new File("safeFile.txt");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        // writes information of every piece on the board line by line
        try (FileWriter writer = new FileWriter("safeFile.txt")) {
            writer.write(turnNumber + "\n");
            writer.write(blueTurn + "\n");
            for (Piece piece : pieces) {
                writer.write(piece.getPieceID() + " " + piece.getCoordinateX() + " " + piece.getCoordinateY() + "\n");
            }
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Load game from safe file //hao zhe applied hotfix to empty save file
    public void loadGameState() {
        File file = new File("safeFile.txt");
        if (!file.exists()) {
            System.out.println("Save file not found. Loading initial board state.");// if no save file is present, initial board state will be loaded
            pieces.clear();
            setupBoard();
            turnNumber = 0;
            blueTurn = true;
            return;
        }
    
        pieces.clear();
        try (Scanner scanner = new Scanner(file)) {
            String line;
            line = scanner.nextLine();
            turnNumber = Integer.parseInt(line.substring(0, 1));
            line = scanner.nextLine();
            blueTurn = line.equalsIgnoreCase("true");
    
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String pieceID = line.substring(0, 3);
                String side;
                int x = Integer.parseInt(line.substring(4, 5));
                int y = Integer.parseInt(line.substring(6, 7));
                if (line.charAt(0) == 'B') {
                    side = "Blue";
                } else {
                    side = "Red";
                }
                if (line.charAt(1) == 'R') {
                    pieces.add(new Ram(pieceID, side, x, y));
                } else if (line.charAt(1) == 'B') {
                    pieces.add(new Biz(pieceID, side, x, y));
                } else if (line.charAt(1) == 'S') {
                    pieces.add(new Sau(pieceID, side, x, y));
                } else if (line.charAt(1) == 'T') {
                    pieces.add(new TorXor(pieceID, side, x, y, "Tor"));
                } else if (line.charAt(1) == 'X') {
                    pieces.add(new TorXor(pieceID, side, x, y, "Xor"));
                }
            }
            System.out.println("Game state loaded from file.");
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while loading the save file.");
            e.printStackTrace();
        }
    }


    // Undo a move
    public void restoreState(Memento memento){
        this.pieces.clear(); // Removes current pieces
        this.pieces = memento.getPieces();
        this.blueTurn = memento.getBlueTurn();
        this.turnNumber = memento.getTurnNumber();
    }
    // Gets move from the controller 
    public boolean parseMove(int fromRow, int fromCol, int toRow, int toCol){
        Piece piece = pieceAt(fromCol, fromRow);
        //System.out.println(piece.getPieceID());
        if(piece != null){
            if(movePiece(piece.getPieceID(), toCol, toRow)){
                return true;
            }
                return false;
        }
        return false;
    }
    // Flip the model along with the board
    public void flipModel(){
        for(Piece piece : pieces){
            int x = piece.getCoordinateX();
            int y = piece.getCoordinateY();
            piece.setCoordinates(x, 8 - 1 - y);
        }
    }
}
