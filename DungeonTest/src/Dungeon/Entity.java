package Dungeon;

public class Entity {
    Coord loc;
    int type;

    public Entity(Coord loc, int type) {
        this.loc = loc;
        this.type = type;
    }

    public void move(Coord newLoc) {
        loc = newLoc;
    }
}
