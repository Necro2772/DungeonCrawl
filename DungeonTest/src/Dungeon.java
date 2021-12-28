import org.json.simple.JSONObject;

public class Dungeon {
    public static Map[] maps;
    public static int currentFloor = 1;

    public static void loadMap(int floor) {
        maps[floor] = new Map(50, 50);
        maps[floor].divideMap();
        maps[floor].randConnectRooms();
    }

    public static JSONObject getCurrentMap() {
        JSONObject obj = new JSONObject();
        obj.put("map", maps[currentFloor - 1].map);
        return obj;
    }
}
