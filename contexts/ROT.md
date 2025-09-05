# rot.js

This document provides a summary of the `rot.js` library features, focusing on procedural generation capabilities like noise, random numbers, and map generation. It is intended as a reference for developers and LLMs.

## Random Number Generator (RNG)

`rot.js` provides a seedable random number generator, `ROT.RNG`, which is an alternative to `Math.random()`. It uses the Alea algorithm.

### Generating random values

- `ROT.RNG.getUniform()`: Returns a random number [0..1) with uniform distribution.
- `ROT.RNG.getNormal(mean, stddev)`: Returns a random number with a normal distribution.
- `ROT.RNG.getPercentage()`: Returns a random integer from 1 to 100.

```javascript
ROT.RNG.getUniform();
// 0.09970397083088756
ROT.RNG.getNormal(0, 10);
// -18.330120558740685
ROT.RNG.getPercentage();
// 72
```

Example of generating a histogram with `getNormal`:
```javascript
var canvas = document.createElement("canvas");
canvas.width = 500;
canvas.height = 200;

var ctx = canvas.getContext("2d");
ctx.fillStyle = "#fff";
ctx.fillRect(0, 0, canvas.width, canvas.height);
ctx.fillStyle = "#f00";

var data = [];
for (var i=0;i<40000;i++) {       /* generate histogram */
    var num = Math.round(ROT.RNG.getNormal(250, 100));
    data[num] = (data[num] || 0) + 1;
}

for (var i=0;i<data.length;i++) { /* plot histogram */
    ctx.fillRect(i, canvas.height-data[i], 1, data[i]);
}
```

### Working with arrays

- `ROT.RNG.getItem(array)`: Picks a random item from an array.
- `ROT.RNG.shuffle(array)`: Shuffles an array in place and returns it.

```javascript
ROT.RNG.getItem(["apples", "oranges", "zombies"]);
// "oranges"
ROT.RNG.shuffle(["apples", "oranges", "zombies"]);
// ["apples","oranges","zombies"] (shuffled)
```

### Picking a weighted value

`ROT.RNG.getWeightedValue(object)` picks a key from an object where values are numeric weights.

```javascript
var monsters = {
    "orc": 3,
    "ogre": 1,
    "rat": 5
}

for (var i=0; i<5; i++) {
    console.log(ROT.RNG.getWeightedValue(monsters));
}
// Example output:
// rat
// orc
// rat
// rat
// orc
```

### Working with RNG state

The RNG's internal state can be managed to produce deterministic sequences.

- `ROT.RNG.getState()`: Returns the current state.
- `ROT.RNG.setState(state)`: Sets the RNG to a specific state.

```javascript
var state = ROT.RNG.getState();
ROT.RNG.getUniform(); // 0.6650914123747498

ROT.RNG.setState(state);
ROT.RNG.getUniform(); // 0.6650914123747498
```

- `ROT.RNG.setSeed(seed)`: Initializes the RNG with a seed.
- `ROT.RNG.getSeed()`: Returns the seed used for initialization.

```javascript
ROT.RNG.setSeed(12345);
console.log(ROT.RNG.getUniform(), ROT.RNG.getUniform());
// 0.01198604702949524 0.8647531978785992

ROT.RNG.setSeed(12345);
console.log(ROT.RNG.getUniform(), ROT.RNG.getUniform());
// 0.01198604702949524 0.8647531978785992
```

### Cloning a RNG

`ROT.RNG.clone()` creates an independent copy of the RNG, pre-set to its parent's state.

```javascript
var clone = ROT.RNG.clone();

console.log(ROT.RNG.getUniform()); // 0.6391114671714604
console.log(clone.getUniform()); // 0.6391114671714604

ROT.RNG.setSeed(123);
console.log(ROT.RNG.getUniform()); // 0.0599007117561996
console.log(clone.getUniform()); // 0.4838599886279553 (unaffected by parent's re-seeding)
```

## Noise Generation

`rot.js` can generate noise using `ROT.Noise.Simplex`.

The `get(x, y)` method returns a noise value for the given coordinates.

### Simplex Noise Example

```javascript
var w = 256;
var h = 100;
var noise = new ROT.Noise.Simplex();

var display = new ROT.Display({width:w, height:h, fontSize:3});

for (var j=0;j<h;j++) {
    for (var i=0;i<w;i++) {
        var val = noise.get(i/20, j/20) * 255;

        var r = ~~(val>0 ? val : 0);
        var g = ~~(val<0 ? -val : 0);
        display.draw(i, j, "", "", "rgb("+r+","+g+",0)");
    }
}
```

### Hexagonal Simplex Noise Example

```javascript
var w = 120;
var h = 50;
var noise = new ROT.Noise.Simplex();

var display = new ROT.Display({width:w, height:h, fontSize:12, layout:"hex"});

for (var j=0;j<h;j++) {
    for (var i=j%2;i<w;i+=2) {
        var val = noise.get(i/60, j/60) * 255;

        var r = ~~(val>0 ? val : 0);
        var g = ~~(val<0 ? -val : 0);
        display.draw(i, j, "", "", "rgb("+r+","+g+",0)");
    }
}
```

## Map Generation

`rot.js` includes several algorithms for map generation.

### Cellular Automata

`ROT.Map.Cellular` generates cave-like systems using cellular automata.

`new ROT.Map.Cellular(width, height, options)`

The `options` object can contain:
- `born`: An array of neighbor counts for an empty cell to become alive.
- `survive`: An array of neighbor counts for a living cell to survive.
- `topology`: `four`, `six`, or `eight` for neighbor definition.

Map initialization:
- `randomize(probability)`: Sets cells to "alive" with a given probability.
- `set(x, y, value)`: Sets a specific cell's value.

Generation:
- `create(callback)`: Advances the simulation by one step. The callback receives `(x, y, value)`. `value` is 0 for dead, 1 for alive.

#### Basic Cellular Map

```javascript
var w = 80, h = 40;
var map = new ROT.Map.Cellular(w, h);

/* cells with 1/2 probability */
map.randomize(0.5);

/* generate and show four generations */
for (var i=0; i<4; i++) {
    var display = new ROT.Display({width:w, height:h, fontSize:4});
    // SHOW(display.getContainer()); // In a browser context
    map.create(display.DEBUG);
}
```

#### Custom Rules

```javascript
var w = 100, h = 60;
var display = new ROT.Display({width:w, height:h, fontSize:6});

/* custom born/survive rules */
var map = new ROT.Map.Cellular(w, h, {
    born: [4, 5, 6, 7, 8],
    survive: [2, 3, 4, 5]
});

map.randomize(0.9);

/* generate fifty iterations, show the last one */
for (var i=49; i>=0; i--) {
    map.create(i ? null : display.DEBUG);
}
```

#### Connecting Regions

`map.connect(callback, wallValue, connectionCallback)` can be used to ensure all areas of the map are accessible.

- `callback`: A function to draw the map, like `display.DEBUG`.
- `wallValue`: The value representing a wall (e.g., 1) or empty space (e.g., 0) to connect. Defaults to 0.
- `connectionCallback(from, to)`: A function called when a connection is made.

```javascript
var w = 80, h = 40;
var map = new ROT.Map.Cellular(w, h);
map.randomize(0.5);
for (var i=0; i<4; i++) map.create();

// To connect empty spaces (value 0)
map.connect(display.DEBUG);

// To connect walls (value 1)
map.connect(display.DEBUG, 1);

// With a connection callback
map.connect(display.DEBUG, 0, function(from, to) {
    console.log("Connection was made " + from + " to " + to);
});
```

### Dungeon Generators

These generators create maps with rooms and corridors.

After creation, you can use:
- `getRooms()`: Returns an array of room objects.
- `getCorridors()`: Returns an array of corridor objects.

Room objects have a `getDoors(callback)` method, where the callback receives `(x, y)` for each door.

```javascript
ROT.RNG.setSeed(1234);
var map = new ROT.Map.Digger();
var display = new ROT.Display({fontSize:8});
map.create(display.DEBUG);

var drawDoor = function(x, y) {
    display.draw(x, y, "", "", "red");
}

var rooms = map.getRooms();
for (var i=0; i<rooms.length; i++) {
    var room = rooms[i];
    console.log(ROT.Util.format("Room #%s: [%s, %s] => [%s, %s]",
        (i+1),
        room.getLeft(), room.getTop(),
        room.getRight(), room.getBottom()
    ));

    room.getDoors(drawDoor);
}
```

#### Digger

`new ROT.Map.Digger(width, height, options)` uses a human-like digging pattern.

Options:
- `roomWidth`: `[min, max]` room width.
- `roomHeight`: `[min, max]` room height.
- `corridorLength`: `[min, max]` corridor length.
- `dugPercentage`: Fraction of map to dig out (default: 0.2).
- `timeLimit`: Stop after this many milliseconds.

```javascript
var w = 40, h = 25;
var map = new ROT.Map.Digger(w, h);
var display = new ROT.Display({width:w, height:h, fontSize:6});
map.create(display.DEBUG);
```

#### Uniform

`new ROT.Map.Uniform(width, height, options)` generates rooms and then connects them.

Options:
- `roomWidth`: `[min, max]` room width.
- `roomHeight`: `[min,max]` room height.
- `roomDugPercentage`: Fraction of map to be filled with rooms (default: 0.1).
- `timeLimit`: Stop after this many milliseconds.

```javascript
var w = 40, h = 25;
var map = new ROT.Map.Uniform(w, h);
var display = new ROT.Display({width:w, height:h, fontSize:6});
map.create(display.DEBUG);
```

#### Rogue

`new ROT.Map.Rogue(width, height)` implements the original Rogue dungeon generation algorithm.

```javascript
var w = 80, h = 24;
var map = new ROT.Map.Rogue(w,h);
var display = new ROT.Display({width:w, height:h, fontSize:6});
map.create(display.DEBUG);
```
