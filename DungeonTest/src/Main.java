public class Main {
    public static void main(String[] args) {

        Map map = new Map(50, 50);

        map.divideMap();
        map.randConnectRooms();
        //map.genMaze();
        map.displayMap();
    }
}
