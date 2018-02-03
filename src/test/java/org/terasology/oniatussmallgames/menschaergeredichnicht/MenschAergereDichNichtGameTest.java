package org.terasology.oniatussmallgames.menschaergeredichnicht;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.terasology.oniatussmallgames.menschaergeredichnicht.MenschAergereDichNichtGame.*;

public class MenschAergereDichNichtGameTest {

    @Test
    public void shouldInitializeStartingGameState() throws Exception {
        MenschAergereDichNichtGame menschAergereDichNichtGame = new MenschAergereDichNichtGame();

        verifySpawnPosition(menschAergereDichNichtGame, OFFSET_SPAWN_GREEN, PlayerColor.GREEN);
        verifySpawnPosition(menschAergereDichNichtGame, OFFSET_SPAWN_YELLOW, PlayerColor.YELLOW);
        verifySpawnPosition(menschAergereDichNichtGame, OFFSET_SPAWN_RED, PlayerColor.RED);
        verifySpawnPosition(menschAergereDichNichtGame, OFFSET_SPAWN_BLUE, PlayerColor.BLUE);

        assertEquals(PlayerColor.GREEN,menschAergereDichNichtGame.getPlayerColorOnTurn());
    }

    private void verifySpawnPosition(MenschAergereDichNichtGame game, int startIndex, PlayerColor colorToCheck) {
        assertEquals(startIndex,game.getPiecePosition(colorToCheck,0));
        assertEquals(startIndex+1,game.getPiecePosition(colorToCheck,1));
        assertEquals(startIndex+2,game.getPiecePosition(colorToCheck,2));
        assertEquals(startIndex+3,game.getPiecePosition(colorToCheck,3));
    }
}
