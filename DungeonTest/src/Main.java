import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        Map map = new Map(30, 15);

        map.addRoom(5, 1, 5, 1);
        //addRoom(map, 3, 2, 3, 2);
        //addRoom(map, 3, 2, 3, 2);

        //map.createPathR(new Coord(0,0), new Coord(10,10));
        //map.createPath(0, 0, 10, 10);

        map.displayMap();
    }
}
