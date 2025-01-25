// Main class to run kwazam
// Author Luqman, Arif
public class KwazamChess {
    public static void main(String[] args) {
        KwazamChessModel model = new KwazamChessModel();
        KwazamChessView view = new KwazamChessView(8, 5);
        KwazamChessController controller = new KwazamChessController(model, view);
    }
}