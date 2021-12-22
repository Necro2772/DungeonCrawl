function generateMap() {
    const test = 'http://localhost:3000/testMap.json'

    fetch(test)
    .then(data => data.json())
    .then(res => drawMap(res.map));
}

function drawMap(m) {
    let map = '<div id="map">';

    for (let i = 0; i < m.length; i++) {
        map = map.concat('<div class="row">');

        for (let j = 0; j < m[0].length; j++) {
            if (m[i][j] === 0) {
                map = map.concat('<div class="cell wall"></div>');
            }
            else if (m[i][j] === 3) {
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