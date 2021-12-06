import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        Map map = new Map(10, 5);
        int[][] m = map.getMap();
        
        JFrame frame = new JFrame("Dungeon Crawler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(200, 200);
        frame.setVisible(true);

        frame.getContentPane().add(new MapDisplay(m));

        map.genMaze();
        map.displayMap();
    }
}
