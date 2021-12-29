const root = 'http://localhost:8080/';

// FOR DRAWING FULL MAP
const mapDisplay = document.getElementById("map-display");
const mapContext = mapDisplay.getContext("2d");
const grid = 15;

let map;

function generateMap() {
	const url = `${root}/genMap`;

	fetch(url)
	.then(data => data.json())
	.then(res => {
		map = res;
		drawMap(map, grid)
	});
}

function drawMap(m, g) {
	// drawGrid(m);

	mapDisplay.width = (m[0].length + 2) * g;
	mapDisplay.height = (m.length + 2) * g;

	mapContext.strokeStyle = "#253652";

	for (let x = 0; x < m[0].length; x++) {
		for (let y = 0; y < m.length; y++) {
			mapContext.beginPath();
			mapContext.rect(g * (x + 1), g * (y + 1), g, g);
			switch (m[y][x]) {
				case 2: 	// ROOM
					mapContext.fillStyle = 'purple';
					break;
				
				case 3: 	// CORRIDOR
					mapContext.fillStyle = 'orange';
					break;
				
				case 4: 	// DOOR
					mapContext.fillStyle = 'yellow';
					break;

				case 5: 	// PLAYER
					mapContext.fillStyle = 'red';
					break;
				
				case 6:		// STAIRS
					mapContext.fillStyle = 'grey';
					break;
				
				default: 	// WALL
					mapContext.fillStyle = 'black';
			}
			
			mapContext.fill();
			mapContext.stroke();
		}
	}
}

// function drawGrid(m) {
// 	mapDisplay.width = (m[0].length + 2) * grid;
// 	mapDisplay.height = (m.length + 2) * grid;

// 	mapContext.beginPath();

// 	for (let x = grid; x <= mapDisplay.width - grid; x += grid) {
// 		mapContext.moveTo(x, grid);
// 		mapContext.lineTo(x, mapDisplay.height - grid);
// 	}

// 	for (let y = grid; y <= mapDisplay.height - grid; y += grid) {
// 		mapContext.moveTo(grid, y);
// 		mapContext.lineTo(mapDisplay.width - grid, y);
// 	}

// 	mapContext.strokeStyle = "#253652";
// 	mapContext.stroke();
// }

// FOR DRAWING GAME DISPLAY

// const canvas = document.getElementById("game-display");
// const context = canvas.getContext("2d");

// function drawGame() {
// 	canvas.width = window.innerWidth * 0.8;
// 	canvas.height = window.innerHeight * 0.8;

// 	context.strokeStyle = "#253652";

// 	context.beginPath();
// 	context.rect(0, 0, canvas.width, canvas.height);
// 	context.stroke();

// 	context.beginPath();
// 	context.rect(0, 0, canvas.width * 0.65, canvas.height);
// 	context.stroke();

// 	drawMiniMap(canvas.width * 0.66, canvas.height * ((1 - 0.45) / 2), canvas.width * 0.33, canvas.height * 0.45);
// }

// function drawMiniMap(x, y, w, h) {
// 	context.beginPath();
// 	context.rect(x, y, w, h);
// 	context.stroke();

// 	drawMap(map, 5);
// }