public class Coord {
    int x;
    int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord(Coord other) {
        this.x = other.x;
        this.y = other.y;
    }

    public void moveInDir(int dir) {
        switch (dir) {
            case 0:
                y--;
                break;
            case 1:
                x++;
                break;
            case 2:
                y++;
                break;
            case 3:
                x--;
                break;
            default:
                System.err.println("Cannot move coord!");
                break;
        }
    }

    public boolean equals(Object o) {
        Coord other = (Coord) o;
        return other.x == x && other.y == y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
