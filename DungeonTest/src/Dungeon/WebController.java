package Dungeon;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class WebController {

    @GetMapping("/loadMap")
    public int[][] loadMap() {
        return Dungeon.getCurrentMap();
    }

    @GetMapping("/genDungeon")
    public int[][] genMap() {
        Dungeon.loadDungeon(5);
        return Dungeon.getCurrentMap();
    }
}
