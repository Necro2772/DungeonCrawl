import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/loadMap")
    public JSONObject loadMap(@RequestParam(value = "name", defaultValue = "World") String name) {
        return Dungeon.getCurrentMap();
    }
}
