const test = {
    map: [
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 2, 2, 2, 0, 0, 0, 0, 0, 0],
        [0, 2, 2, 2, 0, 0, 0, 0, 0, 0],
        [0, 2, 2, 2, 0, 0, 0, 0, 0, 0],
        [0, 0, 3, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 2, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 3, 2, 2, 2, 2, 2, 0, 0],
        [0, 0, 0, 2, 2, 2, 2, 2, 0, 0],
        [0, 0, 0, 2, 2, 2, 2, 2, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    ],
}

function drawMap() {
    let map = '<div id="map">';

    for (let i = 0; i < test.map.length; i++) {
        map = map.concat('<div class="row">');

        for (let j = 0; j < test.map[0].length; j++) {
            if (test.map[i][j] === 0) {
                map = map.concat('<div class="cell wall"></div>');
            }
            else if (test.map[i][j] === 3) {
                map = map.concat('<div class="cell door"></div>');
            }
            else {
                map = map.concat('<div class="cell open"></div>');
            }
        }

        map = map.concat('</div>');
    }

    map = map.concat('</div>');

    document.getElementById("map-outer").innerHTML = map;
}