package Dungeon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        Dungeon.loadMap(0);
        SpringApplication.run(Main.class, args);
    }
}
