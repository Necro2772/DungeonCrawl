import java.awt.Color;
import java.awt.Graphics;
import javax.swing.*;

public class MapDisplay extends JPanel {
    private int squareX = 50;
    private int squareY = 50;
    private int squareW = 20;
    private int squareH = 20;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.red);
        g.fillRect(squareX, squareY, squareW, squareH);
    }

    // public MapDisplay(String string, Map map) {
    //     JFrame frame = new JFrame(string);
    //     JPanel panel = new JPanel();
        

    //     frame.getContentPane().add(panel);
        
    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     frame.setLayout(new BorderLayout());
    //     frame.setSize(800, 800);
    //     frame.setVisible(true);
    // }
}
