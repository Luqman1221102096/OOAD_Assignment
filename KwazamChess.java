//main class to run kwzam
public class KwazamChess {
    public static void main(String[] args) {
        KwazamChessModel model = new KwazamChessModel();
        KwazamChessView view = new KwazamChessView(8, 5);
        KwazamChessController controller = new KwazamChessController(model, view);

        //controller.startGame(); //start the game
        //controller.loadGame(); // Testing loading safeFile
    }
}