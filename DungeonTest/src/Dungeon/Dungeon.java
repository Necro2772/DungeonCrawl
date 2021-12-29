package Dungeon;

import org.json.simple.JSONObject;

public class Dungeon {
    public static Map[] maps = new Map[10];
    public static int currentFloor = 1;

    public static void loadMap(int floor) {
        maps[floor] = new Map(50, 50);
        maps[floor].divideMap();
        maps[floor].randConnectRooms();
        maps[floor].addEntity(Map.PLAYER);
    }

    public static int[][] getCurrentMap() {
//        JSONObject obj = new JSONObject();
//        obj.put("map", maps[currentFloor - 1].map);
        return maps[currentFloor - 1].map;
    }
}
