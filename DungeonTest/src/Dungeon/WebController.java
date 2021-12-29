package Dungeon;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class WebController {

    @GetMapping("/loadMap")
    public int[][] loadMap() {
        return Dungeon.getCurrentMap();
    }

    @GetMapping("/genMap")
    public int[][] genMap() {
        Dungeon.loadMap(0);
        return Dungeon.getCurrentMap();
    }
}
