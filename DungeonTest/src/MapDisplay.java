import java.awt.Color;
import java.awt.Graphics;
import javax.swing.*;

public class MapDisplay extends JPanel {
    int[][] map;
    int width;
    int height;

    MapDisplay(int[][] m) {
        map = m;
        width = map[0].length * 10;
        height = map.length * 10;
    }

    public void drawRoomSpace(Graphics g, int x, int y) {
        g.setColor(Color.white);
        g.drawRect(x, y, 10, 10);
    }

    public void drawWallSpace(Graphics g, int x, int y) {
        g.setColor(Color.black);
        g.drawRect(x, y, 10, 10);
    }

    // protected void paintComponent(Graphics g) {
    //     super.paintComponent(g);
    //     g.setColor(Color.red);
    // }

    // we're working on it
    public void drawMap() {
        for (int[] i : map){
            for (int j : i){
                // if (j == 2) {
                //     System.out.print(key.get(j));
                // } else {
                //     System.out.print(j);
                // }
            }
            // System.out.println();
        }
        // System.out.println();
    }
}
