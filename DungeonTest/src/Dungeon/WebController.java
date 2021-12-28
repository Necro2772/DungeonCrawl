package Dungeon;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/loadMap")
    public JSONObject loadMap() {
        return Dungeon.getCurrentMap();
    }
}
