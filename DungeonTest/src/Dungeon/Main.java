package Dungeon;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        Dungeon.loadDungeon(5);
        Dungeon.maps[0].displayMap();
        //SpringApplication.run(Main.class, args);
    }
}
