//only for testing code if works or not
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

class KwazamChessController {
    private KwazamChessModel model; //logic and state from KCModel.java
    private KwazamChessView view;   //display of the game from KCView.java

    //constructor for controller
    public KwazamChessController(KwazamChessModel model, KwazamChessView view) {
        this.model = model;
        this.view = view;
    }

    //start the game and play randomly until no moves are left
    public void startGame() {
        view.showBoard(model.getPieces()); //show initial board
        while(model.getTurn() <= 50){
            if(model.gameLoop()){
                view.showBoard(model.getPieces());
            }
        }
        model.safeGame();
    }
    // Load game and start playing from that position
    public void loadGame(){
        model.loadGameState();
        while(model.getTurn() <= 50){
            if(model.gameLoop()){
                view.showBoard(model.getPieces());
            }
        }
        model.safeGame();
    }
}
