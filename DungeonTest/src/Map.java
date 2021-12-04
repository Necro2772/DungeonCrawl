import java.util.LinkedList;

public class Map {
    int[][] map;


    public Map(int width, int height) {
        map = new int[height][width];
    }

    public void setValue(Coord coord, int value) {
        map[coord.y][coord.x] = value;
    }

    public int getValue(Coord coord) {
        return map[coord.y][coord.x];
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

    //Idea: breadth search, recursively get path, divide into rectangles, each one normally with current path/checking
    public void createPathR(Coord p1, Coord p2) {
        if (p1.equals(p2)) {
            System.out.println("Done!");
            return;
        }
        Coord temp;
        LinkedList<Coord> coords = new LinkedList<>();
        for (int x : new int[] {-1, 1}) {
            temp = new Coord(p1.x+x, p1.y);
            if (inBounds(temp) && pathExists(temp, p2)) {
                coords.add(temp);
            }
        }
        for (int y: new int[] {-1, 1}) {
            temp = new Coord(p1.x, p1.y+y);
            if (inBounds(temp) && pathExists(temp, p2)) {
                coords.add(temp);
            }
        }
        if(coords.isEmpty()) {
            return;
        }
        Coord next = coords.get((int)(coords.size() * Math.random()));
        for (int x : new int[] {-1, 1}) {
            temp = new Coord(p1.x+x, p1.y);
            if (inBounds(temp) && !new Coord(p1.x+x, next.y).equals(next)) {
                if(getValue(temp) == 0) {
                    setValue(new Coord(p1.x+x, p1.y), 1);
                }
            }
        }
        for (int y: new int[] {-1, 1}) {
            temp = new Coord(p1.x, p1.y+y);
            if (inBounds(temp) && !new Coord(p1.x, next.y+y).equals(next)) {
                if (getValue(temp) == 0) {
                    setValue(new Coord(p1.x, p1.y+y), 1);
                }
            }
        }
        setValue(next, 2);
        displayMap();
        createPathR(next, p2);
    }

    public boolean pathExists(Coord p1, Coord p2) {
        if (getValue(p1) != 0) {
            return false;
        }
        LinkedList<Coord> items = new LinkedList<>();
        LinkedList<Coord> found = new LinkedList<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == 0) {
                    items.add(new Coord(x, y));
                }
            }
        }
        found.add(p1);
        pathExistsR(p1, items, found);
        return found.contains(p2);
    }

    public void pathExistsR(Coord value, LinkedList<Coord> items, LinkedList<Coord> found) {
        if (items.contains(new Coord(value.x-1, value.y)) && !found.contains(new Coord(value.x-1, value.y))) {
            found.add(new Coord(value.x-1, value.y));
            items.remove(new Coord(value.x-1, value.y));
            pathExistsR(new Coord(value.x-1, value.y), items, found);
        }
        if (items.contains(new Coord(value.x+1, value.y)) && !found.contains(new Coord(value.x+1, value.y))) {
            found.add(new Coord(value.x+1, value.y));
            items.remove(new Coord(value.x+1, value.y));
            pathExistsR(new Coord(value.x+1, value.y), items, found);
        }
        if (items.contains(new Coord(value.x, value.y-1)) && !found.contains(new Coord(value.x, value.y-1))) {
            found.add(new Coord(value.x, value.y-1));
            items.remove(new Coord(value.x, value.y-1));
            pathExistsR(new Coord(value.x, value.y-1), items, found);
        }
        if (items.contains(new Coord(value.x, value.y+1)) && !found.contains(new Coord(value.x, value.y+1))) {
            found.add(new Coord(value.x, value.y+1));
            items.remove(new Coord(value.x, value.y+1));
            pathExistsR(new Coord(value.x, value.y+1), items, found);
        }
    }

    public void createPath(int x1, int y1, int x2, int y2) {
        LinkedList<Integer> directions = new LinkedList<>();
        if (x1 < x2) {
            for (int i = 0; i < x2-x1; i++) {
                directions.add(1);
            }
        }
        else {
            for (int i = 0; i < x2-x1; i++) {
                directions.add(3);
            }
        }
        if (y1 < y2) {
            for (int i = 0; i < y2-y1; i++) {
                directions.add(2);
            }
        }
        else {
            for (int i = 0; i < y2-y1; i++) {
                directions.add(0);
            }
        }
        int posX = x1;
        int posY = y1;
        while (!directions.isEmpty()) {
            int index = (int)(directions.size() * Math.random());
            switch (directions.get(index)) {
                case 0: posY--;
                    break;
                case 1: posX++;
                    break;
                case 2: posY++;
                    break;
                case 3: posX--;
                    break;
                default:
                    break;
            }
            map[posY][posX] = 4;
            directions.remove(index);
        }
    }

    public void displayMap() {
        for (int[] i : map){
            for (int j : i){
                System.out.print(j);
            }
            System.out.println();
        }
        System.out.println();
    }
}
