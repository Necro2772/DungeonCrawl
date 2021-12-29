package Dungeon;

import org.json.simple.JSONObject;

import java.util.Hashtable;
import java.util.LinkedList;

public class Dungeon {
    public static Map[] maps;
    public static Hashtable<Coord, Coord> stairsDown;
    public static Hashtable<Coord, Coord> stairsUp;
    public static int currentFloor = 1;
    public static int maxFloor;

    public static void loadDungeon(int numFloors) {
        maxFloor = numFloors;
        maps = new Map[maxFloor];
        for (int i = 0; i < maxFloor; i++) {
            loadMap(i);
        }
        stairsDown = new Hashtable<>();
        stairsUp = new Hashtable<>();
        for (int i = 0; i < maxFloor - 1; i++) {
            Coord down = maps[i].addEntity(Map.DOWNSTAIR);
            Coord up = maps[i + 1].addEntity(Map.UPSTAIR);
            stairsDown.put(up, down);
            stairsUp.put(down, up);
        }
        currentFloor = 1;
    }

    private static void loadMap(int floor) {
        maps[floor] = new Map(50, 50);
        maps[floor].createBasicMap();
        maps[floor].randomizePlayer();
    }

    public static void moveDown(Coord stair) {
        currentFloor++;
        maps[currentFloor].player = stairsDown.get(stair);
    }

    public static void moveUp(Coord stair) {
        currentFloor--;
        maps[currentFloor].player = stairsUp.get(stair);
    }

    public static int[][] getCurrentMap() {
//        JSONObject obj = new JSONObject();
//        obj.put("map", maps[currentFloor - 1].map);
        return maps[currentFloor - 1].getMap();
    }
}
