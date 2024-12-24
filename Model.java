
// Just Testing board display
import javax.swing.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.awt.BasicStroke;
public class Model{
    int boardWidth = 5;
    int boardHeight = 8;
    Model(){
        //To do
    }
    // Testing list with pieces
    public static void main(String[] args){
        List<Piece> list = new ArrayList<Piece>();
        Ram r1 = new Ram("Ram1", "Red", 1, 2);
        Tor t1 = new Tor("Tor", "Red", 1, 1);
        Xor x1 = new Xor("Xor", "Red", 1, 5);
        TorXor tx1 = new TorXor("TorXor", "Red", 1, 1, "Xor");
        list.add(r1);
        list.add(t1);
        list.add(x1);
        list.add(tx1);
        // Testing Xor to Tor switch
        tx1.validateMove(2, 2);
        tx1.validateMove(3, 3);
        tx1.validateMove(3, 8);
        //x1.validateMove(2, 4);

    }

}