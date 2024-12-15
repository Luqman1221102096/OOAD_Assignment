
// Just Testing board display
import javax.swing.*;
import java.awt.Color;
import java.awt.BasicStroke;
public class Model{
    Model(){
        //System.out.println("Testing VS Code java");
        JFrame frame = new JFrame("Kwazam Chess");
        Box[] boxes = new Box[13];
        JLayeredPane panel = new JLayeredPane();
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        for (int j = 0; j < 8; j++){
            for (int i = 0; i < 5; i++) {
                boxes[i] = new Box(BoxLayout.PAGE_AXIS);
                boxes[i].setLocation(i * 80, j * 80);
                boxes[i].setSize(80, 80);
                //boxes[i].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(3.0f)));
                boxes[i].setBorder(BorderFactory.createLineBorder(Color.black));
                panel.add(boxes[i], 0, -1);
            }
        }
        //Test Piece
        JLabel piece = new JLabel("Piece");
        piece.setLocation(80,  80);
        piece.setSize(80, 80);
        //panel.add(piece, 1, -1);

        Box piece2 = new Box(BoxLayout.PAGE_AXIS);
        piece2.setLocation(80,  80);
        piece2.setSize(80, 80);
        panel.add(piece2, 1, -1);
        piece2.add(piece);
        panel.add(piece2, 1, -1);

        frame.add(panel);
        frame.setSize(440, 680);
        panel.setSize(600, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main (String[] args){
        new Model();
    }


}