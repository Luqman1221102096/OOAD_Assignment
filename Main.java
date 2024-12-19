public class Main {
    public static void main (String[] args){
        Model model = new Model();
        View view = new View();
        Controller gameController = new Controller(view,model);
        gameController.view.createBoard();
    }
}
