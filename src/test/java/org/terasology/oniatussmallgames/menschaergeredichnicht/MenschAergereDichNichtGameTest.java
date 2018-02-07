package org.terasology.oniatussmallgames.menschaergeredichnicht;


import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.terasology.oniatussmallgames.menschaergeredichnicht.MenschAergereDichNichtGame.*;

public class MenschAergereDichNichtGameTest {

    private MenschAergereDichNichtGame game;

    @Before
    public void setUp() throws Exception {
        game = new MenschAergereDichNichtGame();
    }

    @Test
    public void shouldInitializeStartingGameState() throws Exception {
        verifySpawnPosition(game, OFFSET_SPAWN_GREEN, PlayerColor.GREEN);
        verifySpawnPosition(game, OFFSET_SPAWN_YELLOW, PlayerColor.YELLOW);
        verifySpawnPosition(game, OFFSET_SPAWN_RED, PlayerColor.RED);
        verifySpawnPosition(game, OFFSET_SPAWN_BLUE, PlayerColor.BLUE);
        assertEquals(PlayerColor.GREEN, game.getPlayerColorOnTurn());
    }

    @Test
    public void shouldAllowToMovePieceFromGreenSpawnOnSix() throws Exception {
        List<GameAction> possibleActions = game.findPossibleActions(6);

        GameAction pieceOneAction = possibleActions.get(0);
        verifyGameActionFromTo(pieceOneAction, 1, 17);
        GameAction pieceTwoAction = possibleActions.get(1);
        verifyGameActionFromTo(pieceTwoAction, 2, 17);
        GameAction pieceThreeAction = possibleActions.get(2);
        verifyGameActionFromTo(pieceThreeAction, 3, 17);
        ;
        GameAction pieceFourAction = possibleActions.get(3);
        verifyGameActionFromTo(pieceFourAction, 4, 17);
    }

    @Test
    public void shouldForbidToLeaveSpawnOnOneToFive() throws Exception {
        for (int diceResult = 1; diceResult <= 5; diceResult++) {
            game = new MenschAergereDichNichtGame();
            assertTrue(game.findPossibleActions(diceResult).isEmpty());
        }
    }

    @Test
    public void shouldChangePlayerAfterThreeAttemptsToLeaveSpawn() throws Exception {
        game.findPossibleActions(1);
        assertEquals(PlayerColor.GREEN, game.getPlayerColorOnTurn());
        game.findPossibleActions(2);
        assertEquals(PlayerColor.GREEN, game.getPlayerColorOnTurn());
        game.findPossibleActions(3);
        assertEquals(PlayerColor.YELLOW, game.getPlayerColorOnTurn());
    }

    @Test
    public void shouldCycleThroughPlayers() throws Exception {
        failToLeaveSpawnThreeTimes();
        assertEquals(PlayerColor.YELLOW, game.getPlayerColorOnTurn());
        failToLeaveSpawnThreeTimes();
        assertEquals(PlayerColor.BLUE, game.getPlayerColorOnTurn());
        failToLeaveSpawnThreeTimes();
        assertEquals(PlayerColor.RED, game.getPlayerColorOnTurn());
        failToLeaveSpawnThreeTimes();
        assertEquals(PlayerColor.GREEN, game.getPlayerColorOnTurn());
    }

    @Test
    public void shouldSpawnYellowPiecesOnSix() throws Exception {
        game.setPlayerOnTurn(PlayerColor.YELLOW);
        List<GameAction> possibleActions = game.findPossibleActions(6);
        verifyGameActionFromTo(possibleActions.get(0), 5, 27);
        verifyGameActionFromTo(possibleActions.get(1), 6, 27);
        verifyGameActionFromTo(possibleActions.get(2), 7, 27);
        verifyGameActionFromTo(possibleActions.get(3), 8, 27);
    }

    @Test
    public void shouldSpawnBluePiecesOnSix() throws Exception {
        game.setPlayerOnTurn(PlayerColor.BLUE);
        List<GameAction> possibleActions = game.findPossibleActions(6);
        verifyGameActionFromTo(possibleActions.get(0), 13, 37);
        verifyGameActionFromTo(possibleActions.get(1), 14, 37);
        verifyGameActionFromTo(possibleActions.get(2), 15, 37);
        verifyGameActionFromTo(possibleActions.get(3), 16, 37);
    }

    @Test
    public void shouldSpawnRedPiecesOnSix() throws Exception {
        game.setPlayerOnTurn(PlayerColor.RED);
        List<GameAction> possibleActions = game.findPossibleActions(6);
        verifyGameActionFromTo(possibleActions.get(0), 9, 47);
        verifyGameActionFromTo(possibleActions.get(1), 10, 47);
        verifyGameActionFromTo(possibleActions.get(2), 11, 47);
        verifyGameActionFromTo(possibleActions.get(3), 12, 47);
    }

    @Test
    public void shouldMovePieceToSpawn() throws Exception {
        List<GameAction> possibleActions = game.findPossibleActions(6);
        game.execute(possibleActions.get(0));
        assertEquals(17,game.getPiecePosition(PlayerColor.GREEN,0));
    }

    private void failToLeaveSpawnThreeTimes() {
        for (int i = 0; i < 3; i++) {
            game.findPossibleActions(1);
        }
    }

    private void verifyGameActionFromTo(GameAction gameAction, int expectedFrom, int expectedTo) {
        assertEquals(expectedFrom, gameAction.getFromPosition());
        assertEquals(expectedTo, gameAction.getToPosition());
    }

    private void verifySpawnPosition(MenschAergereDichNichtGame game, int startIndex, PlayerColor colorToCheck) {
        assertEquals(startIndex, game.getPiecePosition(colorToCheck, 0));
        assertEquals(startIndex + 1, game.getPiecePosition(colorToCheck, 1));
        assertEquals(startIndex + 2, game.getPiecePosition(colorToCheck, 2));
        assertEquals(startIndex + 3, game.getPiecePosition(colorToCheck, 3));
    }
}
