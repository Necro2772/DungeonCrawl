import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Map map = new Map(10, 5);
        
        JFrame frame = new JFrame("Dungeon Crawler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(200, 200);
        frame.setVisible(true);

        frame.getContentPane().add(new MapDisplay());

        //map.addRoom(5, 1, 5, 1);
        //addRoom(map, 3, 2, 3, 2);
        //addRoom(map, 3, 2, 3, 2);

        //map.createPathR(new Coord(0,0), new Coord(10,10));
        //map.createPath(0, 0, 10, 10);
//        if (map.pathExists(new Coord(0, 0), new Coord(25, 13))) {
//            map.makePath(map.pathTemp, new Coord(0, 0));
//        } else {
//            System.out.println("Could not create path.");
//        }
        //map.genCave();
        map.genMaze();
        map.displayMap();
    }
}
