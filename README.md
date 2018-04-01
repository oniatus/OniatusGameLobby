# Oniatus Small Games

This module for Terasology aims to provide a lobby where players can select small games to play. 
The module should create an instance of the game and let one player play against ai or multiple players play
against each other.

Therefore the goals from a game perspective are:
1. Provide an ingame lobby
2. Multiplayer capable
3. Singleplayer capable (ai for all games)
4. Every release must be playable

Besides that the module will be developed as techdemo and development example for Terasology.
Goals from the developer guide example are:
1. Maintain release notes
2. Document line of thought and architecture decisions in a devlog
3. Document features for possible extraction and re-use

All textual documentation is maintained in this single document.

* [Release Notes](#Release Notes)
* [Features](#Features)
* [Devlog](#Devlog)

# Release Notes
- none yet ;)

# Features

- Gameplay Module
- Generates a glass lobby on startup and empty chunks elsewhere
- Debug commands:
  - Spawn a game board: `spawnBoard <x> <y> <z>`

# Devlog

## 0.0.1

### Spawning the game board ingame
To transfer the game coordinates to Terasology coordinates and then spawn blocks for the
game positions, we should first have a look at the Terasology coordinate system.
![Terasology Coordinates](doc/terasology_coordinates.jpg "coordinates")
Standing on the x-axis, the z-axis will decreases instead of increasing like our own y-axis.
So our board x-axis can directly be transferred to Terasology but the y-axis has to be multiplied by -1.
To have more control over the board spawning, the logic will be triggered by an event which can then be used in a 
debug command or later on in the actual logic. To test the correct wiring from the event to a spawn of blocks,
the module tests will be used.
A first try of such an integration test would look like this:
```
    @Test
    public void shouldGenerateBoardOnEvent() {
        //board is 13x13, generate chunks first, otherwise everything will be unloaded
        forceAndWaitForGeneration(new Vector3i(0,10,0));
        forceAndWaitForGeneration(new Vector3i(13,10,13));

        WorldProvider worldProvider = getHostContext().get(WorldProvider.class);
        EntityRef worldEntity = worldProvider.getWorldEntity();
        worldEntity.send(new GenerateBoardBlocksEvent(new Vector3i(0, 10, 0)));
        Block upperLeftCorner = worldProvider.getBlock(0, 10, 0);
        assertEquals(MODULE_NAME + ":boardDefault", upperLeftCorner.getURI().toString());
        Block lowerRightCorner = worldProvider.getBlock(12, 10, 12);
        assertEquals(MODULE_NAME + ":boardDefault", lowerRightCorner.getURI().toString());
        Block field51 = worldProvider.getBlock(5, 10, 8);
        assertEquals(MODULE_NAME + ":boardSW", field51.getURI().toString());
    }
```
Testing only some major blocks will be enough as the whole generation logic will be a simple lookup class 
and very easy to validate once ingame. 
As this kind of mapping logic is almost certain to never change a unit test for each field is not needed.

Adding a debug command is very simple:
```
@RegisterSystem
public class ManualDebugCommands extends BaseComponentSystem {

    @Command
    public void spawnBoard(@Sender EntityRef sender, @CommandParam("x") int x, @CommandParam("y") int y, @CommandParam("z") int z) {
        sender.send(new GenerateBoardBlocksEvent(new Vector3i(x, y, z)));
    }
}
```

The handling logic takes place in another system:
```
    @In
    private WorldProvider worldProvider;
    @In
    private BlockManager blockManager;

    @ReceiveEvent
    public void onGenerateBoardBlocks(GenerateBoardBlocksEvent event, EntityRef entity){
        Vector3i delta = event.getUpperLeftCorner();
        for (BoardWorldLookup.BoardBlock boardBlock : BoardWorldLookup.getAllBoardBlocks()) {
            Block block = blockManager.getBlock(boardBlock.getBlockUri());
            Vector3i position = new Vector3i(delta).addX(boardBlock.getDx()).addZ(boardBlock.getDz());
            worldProvider.setBlock(position, block);
        }

    }
```

The `BoardWorldLookup` contains a hardcoded tileset of the board:
```
    private static final String[][] BOARD = new String[][]{
            {"D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D"},
            {"D", "GH", "GH", "D", "D", "SE", "EW", "YS", "D", "D", "YH", "YH", "D"},
            {"D", "GH", "GH", "D", "D", "NS", "YH", "NS", "D", "D", "YH", "YH", "D"},
            {"D", "D", "D", "D", "D", "NS", "YH", "NS", "D", "D", "D", "D", "D"},
            {"D", "D", "D", "D", "D", "NS", "YH", "NS", "D", "D", "D", "D", "D"},
            {"D", "GS", "EW", "EW", "EW", "NW", "YH", "NE", "EW", "EW", "EW", "SW", "D"},
            {"D", "NS", "GH", "GH", "GH", "GH", "D", "BH", "BH", "BH", "BH", "NS", "D"},
            {"D", "NE", "EW", "EW", "EW", "SW", "RH", "SE", "EW", "EW", "EW", "BS", "D"},
            {"D", "D", "D", "D", "D", "NS", "RH", "NS", "D", "D", "D", "D", "D"},
            {"D", "D", "D", "D", "D", "NS", "RH", "NS", "D", "D", "D", "D", "D"},
            {"D", "RH", "RH", "D", "D", "NS", "RH", "NS", "D", "D", "BH", "BH", "D"},
            {"D", "RH", "RH", "D", "D", "RS", "EW", "NW", "D", "D", "BH", "BH", "D"},
            {"D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D"}
    };
```
These values are only used inside the class, the outside view is coordinate offsets and block uris.
The conversion from the previous field indices to x/z-offsets will also go in this lookup class.

Final result ingame:
![Game Board Ingame 1](doc/board1.jpg "Board Ingame 1")

### First backend game logic
The game logic of "Mensch ärgere dich nicht" is an interesting programming exercise because of the different pathing
for each color at their house and because of the special move logic, e.g. pieces are not allowed
to move on positions with a piece of the same color but may capture pieces of different colors.
To model the positions, a single position was used for each field to decouple the game from 2d or 3d representations.
This logic can be wrapped in a 2d-facade at a second step.
The initial concept of the board looks like this
![Game Board](doc/board.jpg "Board")
Green numbers represent the indices, purple numbers the 2d-coordinates.
The logic is also a good exercise for test driven development.
A video of the implementation for the indices and game logic is [available at YouTube](https://youtu.be/iNGY-hZ_aR4) 
in case someone is interested of the process between commits.

### World generation and module testing
The world generator has to create a world which fits the purpose of a game lobby.
The area should not be too big an visible from all places. The first attempt will be a glass
floor with a border on the sides. Terasology has a glass block in core but the module should not include Core as
dependency. Therefore a copy of the glass block will be included in this module.

The first game in this module will be the german board game [Mensch ärgere Dich nicht](https://en.wikipedia.org/wiki/Mensch_%C3%A4rgere_dich_nicht).
As the lobby should be able to handle multiple games and players should be able to view running games through a glass floor, 
the initial size of the lobby should be able to handle 2x2 games at once.
One game board has a size in tiles of 11 by 11, taking 2 extra tiles as reserve means the lobby should have a side-length
of around 26. We round this up to 30 and start with borders at the +-30 axis.