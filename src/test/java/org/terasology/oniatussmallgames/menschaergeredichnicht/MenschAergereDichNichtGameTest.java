package org.terasology.oniatussmallgames.menschaergeredichnicht;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.terasology.oniatussmallgames.menschaergeredichnicht.MenschAergereDichNichtGame.*;

public class MenschAergereDichNichtGameTest {

    public static final int DICE_RESULT = 6;
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
        assertEquals(17, game.getPiecePosition(PlayerColor.GREEN, 0));
    }

    @Test
    public void shouldAllowOnlyPiecesOnBoardToMoveWithLessThanSix() throws Exception {
        game.teleportPiece(1, 17);
        List<GameAction> possibleActions = game.findPossibleActions(1);
        assertEquals(1, possibleActions.size());
        assertEquals(17, possibleActions.get(0).getFromPosition());
        assertEquals(18, possibleActions.get(0).getToPosition());
    }

    @Test
    public void shouldGiveAnExtraMoveAfterLeavingSpawn() throws Exception {
        PlayerColor firstPlayerOnTurn = game.getPlayerColorOnTurn();
        leaveSpawn();
        PlayerColor playerOnTurnAfterLeavingSpawn = game.getPlayerColorOnTurn();
        assertEquals(firstPlayerOnTurn, playerOnTurnAfterLeavingSpawn);
    }

    private void leaveSpawn() {
        List<GameAction> possibleActions = game.findPossibleActions(6);
        game.execute(possibleActions.get(0));
    }

    @Test
    public void shouldMoveToNextPlayerAfterReqularMove() throws Exception {
        PlayerColor firstPlayerOnTurn = game.getPlayerColorOnTurn();
        leaveSpawn();
        List<GameAction> possibleActions = game.findPossibleActions(1);
        game.execute(possibleActions.get(0));
        PlayerColor nextPlayerOnTurn = game.getPlayerColorOnTurn();
        assertNotEquals(firstPlayerOnTurn, nextPlayerOnTurn);
    }

    @Test
    public void shouldHaveExtraMovesOnSix() throws Exception {
        Piece piece = game.teleportPiece(1, 18);
        executeActionForPiece(piece, game.findPossibleActions(6));
        executeActionForPiece(piece, game.findPossibleActions(6));
        assertEquals(18 + 6 + 6, game.getPiecePosition(piece.getPlayerColor(), piece.getIndex()));
        assertEquals(piece.getPlayerColor(), game.getPlayerColorOnTurn());
    }

    @Test
    public void shouldNotAllowMoveOnSamePosition() throws Exception {
        game.teleportPiece(9, 50);
        game.teleportPiece(10, 51);
        game.setPlayerOnTurn(PlayerColor.RED);
        List<GameAction> possibleActions = game.findPossibleActions(1);
        assertEquals(1, possibleActions.size());
        GameAction gameAction = possibleActions.get(0);
        assertEquals(51, gameAction.getFromPosition());
    }

    @Test
    public void shouldCaptureEnemyPiecesOnSamePosition() throws Exception {
        Piece yellowPiece = game.teleportPiece(5, 17);
        leaveSpawn();
        assertEquals(5,game.getPiecePosition(yellowPiece.getPlayerColor(),yellowPiece.getIndex()));
        assertEquals(17,game.getPiecePosition(PlayerColor.GREEN,0));
    }

    @Test
    public void shouldMoveGreenInHouse() throws Exception {
        Piece greenPiece = game.teleportPiece(1, 56);
        game.execute(game.findPossibleActions(1).get(0));
        assertEquals(57,game.getPiecePosition(greenPiece.getPlayerColor(),greenPiece.getIndex()));
    }

    @Test
    public void shouldForbidGreenHouseForOtherColor() throws Exception {
        Piece redPiece = game.teleportPiece(9,56);
        game.setPlayerOnTurn(PlayerColor.RED);
        game.execute(game.findPossibleActions(1).get(0));
        assertEquals(17,game.getPiecePosition(redPiece.getPlayerColor(),redPiece.getIndex()));
    }

    @Test
    public void shouldMoveYellowInHouse() throws Exception {
        Piece yellowPiece = game.teleportPiece(5, 26);
        game.setPlayerOnTurn(PlayerColor.YELLOW);
        executeActionForPiece(yellowPiece,game.findPossibleActions(1));
        assertEquals(61,game.getPiecePosition(yellowPiece));
    }

    @Test
    public void shouldForbidYellowHouseForOtherColor() throws Exception {
        Piece bluePiece = game.teleportPiece(13, 26);
        game.setPlayerOnTurn(PlayerColor.BLUE);
        executeActionForPiece(bluePiece,game.findPossibleActions(1));
        assertEquals(27,game.getPiecePosition(bluePiece));
    }

    @Test
    public void shouldMoveBlueInHouse() throws Exception {
        Piece bluePiece = game.teleportPiece(13, 36);
        game.setPlayerOnTurn(PlayerColor.BLUE);
        executeActionForPiece(bluePiece,game.findPossibleActions(1));
        assertEquals(68,game.getPiecePosition(bluePiece));
    }

    @Test
    public void shouldForbidBlueHouseForOtherColor() throws Exception {
        Piece greenPiece = game.teleportPiece(1,36);
        executeActionForPiece(greenPiece,game.findPossibleActions(1));
        assertEquals(37,game.getPiecePosition(greenPiece));
    }

    @Test
    public void shouldMoveRedInHouse() throws Exception {
        Piece redPiece = game.teleportPiece(9,46);
        game.setPlayerOnTurn(PlayerColor.RED);
        executeActionForPiece(redPiece, game.findPossibleActions(1));
        assertEquals(72,game.getPiecePosition(redPiece));
    }

    @Test
    public void shouldForbidRedHouseForOtherColor() throws Exception {
        Piece greenPiece = game.teleportPiece(1,46);
        executeActionForPiece(greenPiece,game.findPossibleActions(1));
        assertEquals(47,game.getPiecePosition(greenPiece));
    }

    private void executeActionForPiece(Piece piece, List<GameAction> possibleActions) {
        assertEquals(game.getPlayerColorOnTurn(), piece.getPlayerColor());
        int piecePosition = game.getPiecePosition(piece.getPlayerColor(), piece.getIndex());
        for (GameAction action : possibleActions) {
            if (action.getFromPosition() == piecePosition) {
                game.execute(action);
                return;
            }
        }
        Assert.fail("No action for piece");
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
