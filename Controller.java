// Controller class facilitates communication between the View and Model class
public class Controller {
    View view;
    Model model;
    public Controller(View view, Model model){
        this.view = view;
        this.model = model;
    }    
}
