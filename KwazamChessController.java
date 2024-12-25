//only for testing code if works or not
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

class KwazamChessController {
    private KwazamChessModel model; //logic and state from KCModel.java
    private KwazamChessView view;   //display of the game from KCView.java
    private boolean blueTurn;      //logic for first turn (dont change this, only change in line 18)
    private int turnNumber;        //turn number
    private List<String> blueCaptured; //;ist of pieces captured by blue (//not working, prolly remove later...too eepy to fix)
    private List<String> redCaptured;  //list of pieces captured by red (//not working, prolly remove later...too eepy to fix)

    //constructor for controller
    public KwazamChessController(KwazamChessModel model, KwazamChessView view) {
        this.model = model;
        this.view = view;
        this.blueTurn = true; //blue = true / red = false (change this for first turn)
        this.turnNumber = 1;  //start turn number
        this.blueCaptured = new ArrayList<>();
        this.redCaptured = new ArrayList<>();
    }

    //start the game and play randomly until no moves are left
    public void startGame() {
        view.showBoard(model.getPieces()); //show initial board

        while (true) { //loop the moves
            List<Piece> pieces = model.getPieces();//get all the pieces on the board
            boolean hasValidMove = false; //flag if got valid move
            Random random = new Random();
            
            //check if got valid move for current player
            for (Piece piece : pieces) {
                if ((blueTurn && piece.side.equals("Blue")) || (!blueTurn && piece.side.equals("Red"))) {
                    List<int[]> moves = piece.getMoves();
                    for (int[] move : moves) {
                        if (model.pieceAt(move[0], move[1]) == null || !model.pieceAt(move[0], move[1]).side.equals(piece.side)) {
                            hasValidMove = true;
                        }
                    }
                }
            }
            //not working, prolly remove later...too eepy to fix
            if (!hasValidMove) {
                view.showMessage((blueTurn ? "Blue" : "Red") + " has no valid moves. Game over!");
                view.showMessage("Blue captured: " + blueCaptured);
                view.showMessage("Red captured: " + redCaptured);
                return;
            }

            while (true) {
                Piece randomPiece = pieces.get(random.nextInt(pieces.size()));

                if ((blueTurn && randomPiece.side.equals("Blue")) || (!blueTurn && randomPiece.side.equals("Red"))) {
                    List<int[]> moves = randomPiece.getMoves();
                    for (int[] move : moves) {
                        //check if moves are withinbound (KCModel.java)
                        if (model.isWithinBounds(move[0], move[1]) && 
                        (model.pieceAt(move[0], move[1]) == null || 
                         !model.pieceAt(move[0], move[1]).side.equals(randomPiece.side))) {
                        logTurnDetails(randomPiece, move[0], move[1]);//log the move
                        executeMove(randomPiece.getPieceID(), move[0], move[1]);//move
                        view.showBoard(model.getPieces());//update the board view
                        blueTurn = !blueTurn; //switch turn
                        turnNumber++;//+turnnumber
                        break;
                        }
                    }
                }
                break;
            }
        }
    }
    //logs
    private void logTurnDetails(Piece piece, int newX, int newY) {
        String side = blueTurn ? "Blue" : "Red"; //determine which side is playing
        System.out.println("Turn " + turnNumber + ":");
        System.out.println("Side: " + side);
        System.out.println("Piece: " + piece.getPieceID());
        System.out.println("Move: From (" + piece.getCoordinateX() + ", " + piece.getCoordinateY() + ") to (" + newX + ", " + newY + ")");
    }
    //execution
    private void executeMove(String pieceID, int newX, int newY) {
        Piece piece = model.findPiece(pieceID);//find the piece by ID
        if (piece != null) {
            Piece target = model.pieceAt(newX, newY);//check if theres a piece at the target location
            if (target != null && !target.side.equals(piece.side)) {
                model.removePiece(target); // Capture an enemy piece if it's there
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
}
