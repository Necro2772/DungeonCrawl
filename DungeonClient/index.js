const canvas = document.getElementById("map-canvas");
const context = canvas.getContext("2d");
const grid = 20;

function generateMap() {
	const test = 'http://localhost:3001/testMap.json'

	fetch(test)
	.then(data => data.json())
	.then(res => drawMap(res.map));
}

function drawMap(m) {
	drawGrid(m);

	for (let x = 0; x < m[0].length; x++) {
		for (let y = 0; y < m.length; y++) {
			context.beginPath();
			context.rect(grid * (x + 1), grid * (y + 1), grid, grid);
			if (m[y][x] === 0) {
				context.fillStyle = 'black';
			}
			else if (m[y][x] === 3) {
				context.fillStyle = 'yellow';
			}
			else {
				context.fillStyle = 'purple';
			}
			context.fill();
			context.stroke();
		}
	}
}

function drawGrid(m) {
	canvas.width = (m[0].length + 2) * 20;
	canvas.height = (m.length + 2) * 20;

	context.beginPath();

	for (let x = grid; x <= canvas.width - grid; x += grid) {
		context.moveTo(x, grid);
		context.lineTo(x, canvas.height - grid);
	}

	for (let y = grid; y <= canvas.height - grid; y += grid) {
		context.moveTo(grid, y);
		context.lineTo(canvas.width - grid, y);
	}

	context.strokeStyle = "#253652";
	context.stroke();
}