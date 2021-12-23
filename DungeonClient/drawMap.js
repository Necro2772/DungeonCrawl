const canvas = document.getElementById("map-canvas");
const context = canvas.getContext("2d");

function generateMap() {
    const test = 'http://localhost:3001/testMap.json'

    fetch(test)
    .then(data => data.json())
    .then(res => drawMap(res.map));
}

function drawMap(m) {
    canvas.width = (m[0].length + 2) * 20;
    canvas.height = (m.length + 2) * 20;

    let size = 20;
    let padding = size;

    context.beginPath();

    for (let x = padding; x <= canvas.width - padding; x += size) {
        context.moveTo(x, padding);
        context.lineTo(x, canvas.height - padding);
    }

    for (let y = padding; y <= canvas.height - padding; y += size) {
        context.moveTo(padding, y);
        context.lineTo(canvas.width - padding, y);
    }

    context.strokeStyle = "#253652";
    context.stroke();
}

// function drawMap(m) {
//     let map = '<div id="map">';

//     for (let i = 0; i < m.length; i++) {
//         map = map.concat('<div class="row">');

//         for (let j = 0; j < m[0].length; j++) {
//             if (m[i][j] === 0) {
//                 map = map.concat('<div class="cell wall"></div>');
//             }
//             else if (m[i][j] === 3) {
//                 map = map.concat('<div class="cell door"></div>');
//             }
//             else {
//                 map = map.concat('<div class="cell open"></div>');
//             }
//         }

//         map = map.concat('</div>');
//     }

//     map = map.concat('</div>');

//     document.getElementById("map-outer").innerHTML = map;
// }