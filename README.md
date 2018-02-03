# Oniatus Small Games

This module for Terasology aims to provide a lobby where players can select small games to play. 
The module should create an instance of the game and let one player play against ai or multiple players play
against each other.

Therefore the goals from a game perspective are:
1. Provide an ingame lobby
2. Multiplayer capable
3. Singleplayer capable (ai for all games)
4. Every release must be playable

Besides that the module will be developed as techdemo and development example for terasology.
Goals from the developer guide example are:
1. Maintain release notes
2. Document line of thought and architecture decisions in a devlog
3. Document features for possible extraction and re-use

All textual documentation is maintained in this single document.

* [Release Notes](#Release Notes)
* [Features](#Features)
* [Devlog](#Devlog)

# Release Notes

# Features

# Devlog

## 0.0.1

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