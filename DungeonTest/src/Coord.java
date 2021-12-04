public class Coord {
    int x;
    int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object o) {
        Coord other = (Coord) o;
        return other.x == x && other.y == y;
    }
}
