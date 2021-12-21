import java.util.Hashtable;
import java.util.LinkedList;

public class Map {
    int[][] map;
    LinkedList<Room> rooms;
    LinkedList<Integer> pathTemp;
    Hashtable<Integer, Character> key;

    private static final int OPEN = 2;
    private static final int DOOR = 3;
    private static final int WALL = 0;

    private class Room {
        public Coord start;
        public int width;
        public int height;
        public Room inner;
        public LinkedList<Coord> corridors;

        public Room(Coord start, int width, int height) {
            this.start = start;
            this.width = width;
            this.height = height;
            inner = null;
            corridors = new LinkedList<>();
        }

        public int disOnSide(int dir) {
            switch (dir) {
                case 0:
                    return inner.start.y - start.y + 1;
                case 1:
                    return start.x + 1 + width - inner.start.x -  inner.width;
                case 2:
                    return start.y + 1 + height - inner.start.y -  inner.height;
                case 3:
                    return inner.start.x - start.x + 1;
                default:
                    break;
            }
            return 0;
        }

        public LinkedList<Coord> createDoorList(int wall) {
            LinkedList<Coord> validSpaces = new LinkedList<>();
            Coord start;
            int addX = 0;
            int addY = 0;
            switch (wall) {
                case 0:
                    start = new Coord(this.start);
                    addX = 1;
                    break;
                case 1:
                    start = new Coord(this.start.x + width, this.start.y);
                    addY = 1;
                    break;
                case 2:
                    start = new Coord(this.start.x, this.start.y + height);
                    addX = 1;
                    break;
                case 3:
                    start = new Coord(this.start);
                    addY = 1;
                    break;
                default:
                    System.err.println("Invalid input to createDoorList!");
                    return null;
            }
            for (int i = 1; i < Math.max(addX * width, addY * height); i++) {
                validSpaces.add(new Coord(start.x + i * addX, start.y + i * addY));
            }
            return validSpaces;
        }

        public void addInnerRoom(Coord start, int width, int height) {
            inner = new Room(start, width, height);
        }

        public boolean hasInnerRoom() {
            return inner != null;
        }

        public boolean equals(Object o) {
            Room oRoom = (Room)o;
            return start == oRoom.start && width == oRoom.width && height == oRoom.height;
        }

        public String toString() {
            if (hasInnerRoom()) {
                return String.format("%s, %d x %d (%s)", start.toString(), height, width, inner.toString());
            } else {
                return String.format("%s, %d x %d", start.toString(), height, width);
            }
        }
    }

    public Map(int width, int height) {
        map = new int[height][width];
        rooms = new LinkedList<>();
        key = new Hashtable<>();
        key.put(0, '#');
        key.put(1, '-');
        key.put(2, ' ');
        key.put(3, '|');
    }

    public void setMap(int[][] map2) {
        for (int i = 0; i < map2.length; i++) {
            System.arraycopy(map2[i], 0, map[i], 0, map[0].length);
        }
    }

    public void divideMap() {
        recDivideMap(1, map[0].length - 2, 1, map.length - 2);
    }

    private void recDivideMap(int x1, int x2, int y1, int y2) {
        final int MIN = 5;
        if ((y2 - y1) * (x2 - x1) < 225) {
            rooms.add(new Room(new Coord(x1, y1), x2 - x1 + 1, y2 - y1 + 1));
            drawRandBox(x1 - 1, x2 + 1, y1 - 1, y2 + 1, rooms.getLast());
            return;
        }
        //displayMap();

        if ((Math.random() > 0.50 || y2 - y1 < MIN * 2 + 1) && x2 - x1 >= MIN * 2 + 1) {
            int divide = (int)(Math.random() * (x2 - x1 - MIN * 2)) + x1 + MIN;
            for (int y = y1; y <= y2; y++) {
                //map[y][divide] = 1;
            }
            recDivideMap(x1, divide - 1, y1, y2);
            recDivideMap(divide + 1, x2, y1, y2);
        }
        else {
            int divide = (int) (Math.random() * (y2 - y1 - MIN * 2)) + y1 + MIN;
            for (int x = x1; x <= x2; x++) {
                //map[divide][x] = 1;
            }
            recDivideMap(x1, x2, y1, divide - 1);
            recDivideMap(x1, x2, divide + 1, y2);
        }
    }

    public void drawRandBox(int x1, int x2, int y1, int y2, Room outerRoom) {
        int width = (int)((x2 - x1 - 5) * Math.random()) + 6;
        int height = (int)((y2 - y1 - 5) * Math.random()) + 6;
//        int width = x2 - x1;
//        int height = y2 - y1;
        if (x2 - x1 <= 6) {
            width = x2 - x1;
        }
        if (y2 - y1 <= 6) {
            height = y2 - y1;
        }
        Coord start = new Coord(x1 + (int)((x2 - x1 - width + 1) * Math.random()),
                y1 + (int)((y2 - y1 - height + 1) * Math.random()));
        outerRoom.addInnerRoom(start, width, height);
        for (int x = start.x; x <= start.x + width; x++) {
            for (int y = start.y; y <= start.y + height; y++) {
                if (x == start.x || x == start.x + width || y == start.y || y == start.y + height ) {
                    map[y][x] = 0;
                } else {
                    map[y][x] = OPEN;
                }
            }
        }
    }

    public void randConnectRooms() {
        LinkedList<LinkedList<Room>> merge = new LinkedList<>();
        for (Room room : rooms) {
            LinkedList<Room> temp = new LinkedList<>();
            temp.add(room);
            merge.add(temp);
        }
        while (merge.size() > 1) {
            int index = (int)(merge.size() * Math.random());
            randMerge(merge, index);
        }
    }

    private void randMerge(LinkedList<LinkedList<Room>> merge, int index) {
        LinkedList<Room> adjacent = new LinkedList<>();
        for (Room current : merge.get(index)) {
            for (Room item : getAdjacentRooms(current)) {
                if (!adjacent.contains(item) && !merge.get(index).contains(item)) {
                    adjacent.add(item);
                }
            }
        }
        Room connect = adjacent.get((int)(Math.random() * adjacent.size()));
        for (Room item : merge.get(index)) {
            if (roomsAreAdjacent(connect, item)) {
                connectRooms(connect, item);
                break;
            }
        }
        for (LinkedList<Room> rooms : merge) {
            if (rooms.contains(connect)) {
                for (Room item : rooms) {
                    merge.get(index).add(item);
                }
                merge.remove(rooms);
                break;
            }
        }
    }

    public LinkedList<Room> getAdjacentRooms(Room room) {
        LinkedList<Room> adjacent = new LinkedList<>();
        for (Room item : rooms) {
            if (roomsAreAdjacent(item, room)) {
                adjacent.add(item);
            }
        }
        return adjacent;
    }

    public boolean roomsAreAdjacent(Room room1, Room room2) {
        if (room1.start.x + room1.width - 1 == room2.start.x - 2 || room1.start.x == room2.start.x + room2.width + 1) {
            return room1.start.y >= room2.start.y && room1.start.y < room2.start.y + room2.height
                    || room2.start.y >= room1.start.y && room2.start.y < room1.start.y + room1.height;
        } else if (room1.start.y + room1.height - 1 == room2.start.y - 2 || room1.start.y == room2.start.y + room2.height + 1) {
            return room1.start.x >= room2.start.x && room1.start.x < room2.start.x + room2.width
                    || room2.start.x >= room1.start.x && room2.start.x < room1.start.x + room1.width;
        }
        return false;
    }

    public int wallOfAdjacentRooms(Room start, Room other) {
        if (start.start.x + start.width - 1 == other.start.x - 2) {
            return 1;
        }
        if (start.start.x == other.start.x + other.width + 1) {
            return 3;
        }
        if (start.start.y + start.height - 1 == other.start.y - 2) {
            return 2;
        }
        if (start.start.y == other.start.y + other.height + 1) {
            return 0;
        }
        return -1;
    }

    public void connectRooms(Room room1, Room room2) {
        // TODO: Remove display
        setValue(room1.inner.start, 5);
        setValue(room2.inner.start, 5);
        //displayMap();
        setValue(room1.inner.start, WALL);
        setValue(room2.inner.start, WALL);
        //System.out.println("Connecting " + room1 + " to " + room2);
        // Pick adjacent wall, connect points on that wall
        int dir1 = wallOfAdjacentRooms(room1, room2);
        int dir2 = (dir1 + 2) % 4;
        LinkedList<Coord> spaces1 = room1.inner.createDoorList(dir1);
        LinkedList<Coord> spaces2 = room2.inner.createDoorList((dir1 + 2) % 4);
        if (spaces1 == null || spaces2 == null) {
            System.err.println("null door list in connectRooms!");
            return;
        }

        if (dir1 == 1 || dir1 == 3) {
            spaces1.removeIf(space -> space.y < Math.max(room1.start.y, room2.start.y)
                            || space.y > Math.min(room1.start.y + room1.height - 1, room2.start.y + room2.height - 1));
            spaces2.removeIf(space -> space.y < Math.max(room1.start.y, room2.start.y)
                    || space.y > Math.min(room1.start.y + room1.height - 1, room2.start.y + room2.height - 1));
        } else {
            spaces1.removeIf(space -> space.x < Math.max(room1.start.x, room2.start.x)
                    || space.x > Math.min(room1.start.x + room1.width - 1, room2.start.x + room2.width - 1));
            spaces2.removeIf(space -> space.x < Math.max(room1.start.x, room2.start.x)
                    || space.x > Math.min(room1.start.x + room1.width - 1, room2.start.x + room2.width - 1));
        }

        boolean corner = false;
        int index1 = (int)(Math.random() * spaces1.size());
        int index2 = (int)(Math.random() * spaces2.size());

        if (spaces1.isEmpty() || spaces2.isEmpty()) {
            corner = true;
        } else {
            Coord door1 = spaces1.get(index1);
            if (room1.disOnSide(dir1) + room2.disOnSide(dir2) < 3) {
                door1.moveInDir(dir1);
                if (getValue(door1) == OPEN) {
                    door1.moveInDir(dir2);
                    setValue(door1, DOOR);
                    return;
                }
                door1.moveInDir(dir1);
                if (getValue(door1) == OPEN) {
                    door1.moveInDir(dir2);
                    setValue(door1, DOOR);
                    door1.moveInDir(dir2);
                    setValue(door1, DOOR);
                    return;
                }
                corner = true;
            }
        }

        // Corner connection if necessary
        if (corner) {
            // TODO: add corner connection and checks
            // Clear out space for one entrance on wall, then find a point in that direction and make two straight paths
            return;
        }

        // Direct connection
        Coord door1 = spaces1.get(index1);
        Coord door2 = spaces2.get(index2);

        setValue(door1, DOOR);
        setValue(door2, DOOR);
        door1.moveInDir(dir1);
        door2.moveInDir(dir2);
        connectPoints(door1, door2);
        setValue(door2, OPEN);
    }

    public void connectPoints(Coord pos1, Coord pos2) {
        LinkedList<Integer> path = new LinkedList<>();
        for (int i = 0; i < Math.abs(pos1.x - pos2.x); i++) {
            if (pos1.x < pos2.x) {
                path.add(1);
            } else if (pos2.x < pos1.x) {
                path.add(3);
            }
        }
        for (int i = 0; i < Math.abs(pos1.y - pos2.y); i++) {
            if (pos1.y < pos2.y) {
                path.add(2);
            } else if (pos2.y < pos1.y) {
                path.add(0);
            }
        }
        drawLine(path, pos1);
    }

//    public LinkedList<Room> getRooms() {
//        LinkedList<Room> rooms = new LinkedList<>();
//        for (int x = 0; x < map[0].length; x++) {
//            for (int y = 0; y < map.length; y++) {
//                Coord current = new Coord(x, y);
//                if (getValue(current) == OPEN) {
//                    LinkedList<Coord> adjacent = getAdjacent(current);
//                    int aOpen = 0;
//                    for (Coord item : adjacent) {
//                        if (getValue(item) == OPEN) {
//                            aOpen++;
//                        }
//                    }
//                    if (aOpen == 2) {
//                        if (rooms.isEmpty()) {
//                            rooms.add(current);
//                        } else {
//                            for (Coord room : rooms) {
//                                if (pathExists(current, room)) {
//                                    break;
//                                }
//                                if (rooms.indexOf(room) == rooms.size() - 1) {
//                                    rooms.add(current);
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return rooms;
//    }

    //Growing Tree Algorithm, picks exposed squares at random
    public void genMaze() {
        LinkedList<Coord> exposed = new LinkedList<>();
        map[0][0] = 2;
        exposed.add(new Coord(1, 0));
        exposed.add(new Coord(0, 1));
        while(!exposed.isEmpty()) {
            cut(exposed, (int)(Math.random() * exposed.size()));
        }
    }

    public void cut(LinkedList<Coord> exposed, int current) {
        int num = 0;
        for (Coord adjacent : getAdjacent(exposed.get(current))) {
            if (getValue(adjacent) == 2) {
                num++;
            }
        }
        if (num <= 1) {
            setValue(exposed.get(current), 2);
            for (Coord adjacent: getAdjacent(exposed.get(current))) {
                if (getValue(adjacent) == 0) {
                    exposed.add(adjacent);
                }
            }
            exposed.remove(current);
        } else {
            setValue((exposed.get(current)), 1);
            exposed.remove(current);
        }
    }

    public LinkedList<Coord> getAdjacent(Coord current) {
        LinkedList<Coord> list = new LinkedList<>();
        for (int x : new int[]{-1, 1}) {
            Coord temp = new Coord(current.x + x, current.y);
            if (inBounds(temp)) {
                list.add(temp);
            }
        }
        for (int y : new int[]{-1, 1}) {
            Coord temp = new Coord(current.x, current.y + y);
            if (inBounds(temp)) {
                list.add(temp);
            }
        }
        return list;
    }

    public void genCave() {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (Math.random() < 0.40) {
                    setValue(new Coord(x, y), 1);
                } else {
                    setValue(new Coord(x, y), 0);
                }
            }
        }

        int[][] tempMap = new int[map.length][map[0].length];
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                tempMap[y][x] = 1;
            }
        }

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (y == 0 || y == map.length - 1 || x == 0 || x == map[0].length - 1) {
                    setValue(new Coord(x, y), 1);
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            displayMap();
            for (int y = 1; y < map.length - 1; y++) {
                for (int x = 1; x < map[0].length - 1; x++) {
                    if (search2(new Coord(x, y), 1, 1) >= 5
                            || search2(new Coord(x, y), 2, 1) <= 2) {
                        tempMap[y][x] = 1;
                    } else {
                        tempMap[y][x] = 0;
                    }
                }
            }
            setMap(tempMap);
        }

        for (int i = 0; i < 3; i++) {
            displayMap();
            for (int y = 1; y < map.length - 1; y++) {
                for (int x = 1; x < map[0].length - 1; x++) {
                    if (search2(new Coord(x, y), 1, 1) >= 5) {
                        tempMap[y][x] = 1;
                    } else {
                        tempMap[y][x] = 0;
                    }
                }
            }
            setMap(tempMap);
        }

    }

    public int search2(Coord center, int dist, int search) {
        int count = 0;
        for (int y = center.y - dist; y < center.y + dist + 1; y++) {
            for (int x = center.x - dist; x < center.x + dist + 1; x++) {
                if (inBounds(new Coord(x, y)) && getValue(new Coord(x, y)) == search
                        && !(Math.abs(y - center.y) == 2 && Math.abs(x - center.x) == 2)) {
                    count++;
                }
            }
        }
        return count;
    }

    public int searchArea(Coord start, Coord end, int search) {
        int count = 0;
        for (int y = start.y; y < end.y + 1; y++) {
            for (int x = start.x; x < end.x + 1; x++) {
                if (inBounds(new Coord(x, y)) && getValue(new Coord(x, y)) == search) {
                    count++;
                }
            }
        }
        return count;
    }

    public void setValue(Coord coord, int value) {
        map[coord.y][coord.x] = value;
    }

    public int getValue(Coord coord) {
        return map[coord.y][coord.x];
    }

    public int getValue(int x, int y) {
        return map[y][x];
    }

    public boolean inBounds(Coord coord) {
        return coord.x >= 0 && coord.x < map[0].length && coord.y >= 0 && coord.y < map.length;
    }

    public int addRoom(int minH, int incH, int minW, int incW) {
        int height = minH + (int)Math.round(incH * Math.random()) * 2;
        int width = minW + (int)Math.round(incW * Math.random()) * 2;
        int locX = (int)(Math.random() * (map[0].length - width - 2) / 2) * 2 + 1;
        int locY = (int)(Math.random() * (map.length - height - 2) / 2) * 2 + 1;
        for (int y = locY; y < locY + height; y++) {
            for (int x = locX; x < locX + width; x++) {
                if(map[y][x] != 0) {
                    return 0;
                }
            }
        }
        for (int y = locY - 1; y < locY + height + 1; y++) {
            for (int x = locX - 1; x < locX + width + 1; x++) {
                map[y][x] = 1;
                if (y == locY - 1 || y == locY + height || x == locX - 1 || x == locX + width) {
                    map[y][x] = 2;
                }
            }
        }
        int numDoors = (int)(Math.random() * (width * height / 4) + 1);
        for (int i = 0; i < numDoors; i++) {
            switch ((int)(Math.random() * 4)) {
                case 0: map[locY-1][(int)(Math.random()*width)+locX] = 3;
                    break;
                case 1: map[locY+height][(int)(Math.random()*width)+locX] = 3;
                    break;
                case 2: map[(int)(Math.random()*height)+locY][locX-1] = 3;
                    break;
                case 3: map[(int)(Math.random()*height)+locY][locX+width] = 3;
                    break;
                default: break;
            }
        }
        return 1;
    }

    public int forceRoom(int minH, int incH, int minW, int incW) {
        int height = minH + (int)Math.round(incH * Math.random()) * 2;
        int width = minW + (int)Math.round(incW * Math.random()) * 2;
        int locX = (int)(Math.random() * (map[0].length - width - 2) / 2) * 2 + 1;
        int locY = (int)(Math.random() * (map.length - height - 2) / 2) * 2 + 1;
//        for (int y = locY; y < locY + height; y++) {
//            for (int x = locX; x < locX + width; x++) {
//                if(map[y][x] != 0) {
//                    return 0;
//                }
//            }
//        }
        for (int y = locY - 1; y < locY + height + 1; y++) {
            for (int x = locX - 1; x < locX + width + 1; x++) {
                map[y][x] = 2;
                if (y == locY - 1 || y == locY + height || x == locX - 1 || x == locX + width) {
                    map[y][x] = 1;
                }
            }
        }
        int numDoors = (int)(Math.random() * (width * height / 4) + 1);
        for (int i = 0; i < numDoors; i++) {
            switch ((int)(Math.random() * 4)) {
                case 0: map[locY-1][(int)(Math.random()*width)+locX] = 2;
                    break;
                case 1: map[locY+height][(int)(Math.random()*width)+locX] = 2;
                    break;
                case 2: map[(int)(Math.random()*height)+locY][locX-1] = 2;
                    break;
                case 3: map[(int)(Math.random()*height)+locY][locX+width] = 2;
                    break;
                default: break;
            }
        }
        return 1;
    }

    //Idea: breadth search, recursively get path, divide into rectangles, each one normally with current path/checking
//    public void createPathR(Coord p1, Coord p2) {
//        if (p1.equals(p2)) {
//            System.out.println("Done!");
//            return;
//        }
//        Coord temp;
//        LinkedList<Coord> coords = new LinkedList<>();
//        for (int x : new int[] {-1, 1}) {
//            temp = new Coord(p1.x+x, p1.y);
//            if (inBounds(temp) && pathExists(temp, p2)) {
//                coords.add(temp);
//            }
//        }
//        for (int y: new int[] {-1, 1}) {
//            temp = new Coord(p1.x, p1.y+y);
//            if (inBounds(temp) && pathExists(temp, p2)) {
//                coords.add(temp);
//            }
//        }
//        if(coords.isEmpty()) {
//            return;
//        }
//        Coord next = coords.get((int)(coords.size() * Math.random()));
//        for (int x : new int[] {-1, 1}) {
//            temp = new Coord(p1.x+x, p1.y);
//            if (inBounds(temp) && !new Coord(p1.x+x, next.y).equals(next)) {
//                if(getValue(temp) == 0) {
//                    setValue(new Coord(p1.x+x, p1.y), 1);
//                }
//            }
//        }
//        for (int y: new int[] {-1, 1}) {
//            temp = new Coord(p1.x, p1.y+y);
//            if (inBounds(temp) && !new Coord(p1.x, next.y+y).equals(next)) {
//                if (getValue(temp) == 0) {
//                    setValue(new Coord(p1.x, p1.y+y), 1);
//                }
//            }
//        }
//        setValue(next, 2);
//        displayMap();
//        createPathR(next, p2);
//    }

    public boolean validPoint(Coord location, int... invalid) {
        for (int item : invalid) {
            if (getValue(location) == item) {
                return false;
            }
        }
        return true;
    }

    public boolean pathExists(Coord p1, Coord p2, int... walls) {
        if (!validPoint(p1, walls)) {
            return false;
        }
        LinkedList<Coord> items = new LinkedList<>();
        LinkedList<Coord> found = new LinkedList<>();
        LinkedList<Integer> dir = new LinkedList<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (validPoint(new Coord(x, y), walls)) {
                    items.add(new Coord(x, y));
                }
            }
        }
        found.add(p1);
        dir.add(-1);
        pathExistsR(p1, items, found, dir, 0, walls);
        if (found.contains(p2)) {
            while (found.size() > found.indexOf(p2) + 1) {
                found.removeLast();
                dir.removeLast();
            }

            LinkedList<Integer> path = new LinkedList<>();
            findPathR(found, dir, path);
            pathTemp = path;

            return true;
        } else return false;
    }

    public void pathExistsR(Coord value, LinkedList<Coord> items, LinkedList<Coord> found, LinkedList<Integer> dir, int index, int... walls) {

        for (Coord next : new Coord[]{new Coord(value.x+1, value.y), new Coord(value.x-1, value.y),
                new Coord(value.x, value.y+1), new Coord(value.x, value.y-1)}) {
            if (inBounds(next) && validPoint(next, walls)) {
                if (items.contains(next) && !found.contains(next)) {
                    found.add(next);
                    items.remove(next);
                    if(next.x > value.x) {
                        dir.add(1);
                    } else if(next.x < value.x) {
                        dir.add(3);
                    } else if(next.y > value.y) {
                        dir.add(2);
                    } else if(next.y < value.y) {
                        dir.add(0);
                    }
                }
            }
        }
        index++;
        if (index < found.size()) {
            pathExistsR(found.get(index), items, found, dir, index);
        }
    }

    public void findPathR(LinkedList<Coord> coords, LinkedList<Integer> dir, LinkedList<Integer> path) {
        if (dir.getLast() != -1) {
            path.addFirst(dir.getLast());
        }
        Coord temp = coords.getLast();
        switch(dir.getLast()) {
            case -1:
                return;
            case 0: temp.y++;
                break;
            case 1: temp.x--;
                break;
            case 2: temp.y--;
                break;
            case 3: temp.x++;
                break;
            default:
                break;
        }
        if (!coords.contains(temp)) {
            System.out.println("ERROR! findPathR is broken apparently");
            return;
        }
        int index = coords.indexOf(temp);
        while (coords.size() > index + 1) {
            coords.removeLast();
            dir.removeLast();
        }
        findPathR(coords, dir, path);
    }

    //For making paths: put paths on odd-numbered cells! prevents excessive merging (simple paths)
    public void makePath(LinkedList<Integer> path, Coord start, int... walls) {
        int x = 0;
        int y = 0;
        Coord pos =  new Coord(start);
        LinkedList<Integer> segment = new LinkedList<>();
        while(!path.isEmpty()) {
            if (path.getFirst() % 2 == 0) {
                if (y != 0 && y != path.getFirst() - 1) {
                    makeSegment(segment, start, walls);
                    makePath(path, pos, walls);
                    return;
                }
                y = path.getFirst() - 1;
                pos.y += y;

            } else {
                if (x != 0 && x != path.getFirst() - 2) {
                    makeSegment(segment, start, walls);
                    makePath(segment, pos, walls);
                    return;
                }
                x = path.getFirst() - 2;
                pos.x -= x;
            }
            segment.add(path.getFirst());
            path.removeFirst();
        }
        makeSegment(segment, start, walls);
    }

    //Makes a straight line!
    public void makeSegment(LinkedList<Integer> path, Coord pos, int... walls) {
        while (!path.isEmpty()) {
            setValue(pos, 1);
            int index = (int)(path.size() * Math.random());
            Coord pos2 = new Coord(pos);
            for (Integer integer : path) {
                switch (integer) {
                    case 0:
                        pos2.y--;
                        break;
                    case 1:
                        pos2.x++;
                        break;
                    case 2:
                        pos2.y++;
                        break;
                    case 3:
                        pos2.x--;
                        break;
                    default:
                        break;
                }
            }
            switch (path.get(index)) {
                case 0:
                    if (pathExists(new Coord(pos.x, pos.y - 1), pos2, walls) && pathTemp.size() == path.size() - 1) {
                        pos.y--;
                        path.remove(index);
                    }
                    break;
                case 1:
                    if (pathExists(new Coord(pos.x + 1, pos.y), pos2, walls) && pathTemp.size() == path.size() - 1) {
                        pos.x++;
                        path.remove(index);
                    }
                    break;
                case 2:
                    if (pathExists(new Coord(pos.x, pos.y + 1), pos2, walls) && pathTemp.size() == path.size() - 1) {
                        pos.y++;
                        path.remove(index);
                    }
                    break;
                case 3:
                    if (pathExists(new Coord(pos.x - 1, pos.y), pos2, walls) && pathTemp.size() == path.size() - 1) {
                        pos.x--;
                        path.remove(index);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void drawLine(LinkedList<Integer> path, Coord pos) {
        Coord position = new Coord(pos);
        while (!path.isEmpty()) {
            setValue(position, OPEN);
            int index = (int)(path.size() * Math.random());
            switch (path.get(index)) {
                case 0:
                    position.y--;
                    path.remove(index);
                    break;
                case 1:
                    position.x++;
                    path.remove(index);
                    break;
                case 2:
                    position.y++;
                    path.remove(index);
                    break;
                case 3:
                    position.x--;
                    path.remove(index);
                    break;
                default:
                    break;
            }
        }
    }

//    public void createPath(int x1, int y1, int x2, int y2) {
//        LinkedList<Integer> directions = new LinkedList<>();
//        if (x1 < x2) {
//            for (int i = 0; i < x2-x1; i++) {
//                directions.add(1);
//            }
//        }
//        else {
//            for (int i = 0; i < x2-x1; i++) {
//                directions.add(3);
//            }
//        }
//        if (y1 < y2) {
//            for (int i = 0; i < y2-y1; i++) {
//                directions.add(2);
//            }
//        }
//        else {
//            for (int i = 0; i < y2-y1; i++) {
//                directions.add(0);
//            }
//        }
//        int posX = x1;
//        int posY = y1;
//        while (!directions.isEmpty()) {
//            int index = (int)(directions.size() * Math.random());
//            switch (directions.get(index)) {
//                case 0: posY--;
//                    break;
//                case 1: posX++;
//                    break;
//                case 2: posY++;
//                    break;
//                case 3: posX--;
//                    break;
//                default:
//                    break;
//            }
//            map[posY][posX] = 4;
//            directions.remove(index);
//        }
//    }

    public void displayMap() {
        for (int[] i : map){
            for (int j : i){
                if (key.containsKey(j)) {
                    System.out.print(key.get(j));
                    System.out.print(key.get(j));
                } else {
                    System.out.print(j);
                    System.out.print(j);
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
