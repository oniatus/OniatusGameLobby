package org.terasology.oniatussmallgames.menschaergeredichnicht;

import java.util.ArrayList;
import java.util.List;

public class MenschAergereDichNichtGame {

    public static final int OFFSET_SPAWN_GREEN = 1;
    public static final int OFFSET_SPAWN_YELLOW = 5;
    public static final int OFFSET_SPAWN_RED = 9;
    public static final int OFFSET_SPAWN_BLUE = 13;


    private PlayerColor playerOnTurn = PlayerColor.GREEN;
    private int numberOfAttemptsToLeaveSpawn = 1;

    public int getPiecePosition(PlayerColor playerColor, int pieceNumber) {
        switch (playerColor) {
            case GREEN:
                return pieceNumber + OFFSET_SPAWN_GREEN;
            case YELLOW:
                return pieceNumber + OFFSET_SPAWN_YELLOW;
            case RED:
                return pieceNumber + OFFSET_SPAWN_RED;
            case BLUE:
                return pieceNumber + OFFSET_SPAWN_BLUE;
        }
        throw new RuntimeException();
    }

    public PlayerColor getPlayerColorOnTurn() {
        return playerOnTurn;
    }

    public List<GameAction> findPossibleActions(int diceResult) {
        ArrayList<GameAction> possibleActions = new ArrayList<>();
        if (diceResult == 6) {
            if (playerOnTurn == PlayerColor.GREEN) {
                possibleActions.add(new GameAction(1, 17));
                possibleActions.add(new GameAction(2, 17));
                possibleActions.add(new GameAction(3, 17));
                possibleActions.add(new GameAction(4, 17));
            } else if (playerOnTurn == PlayerColor.YELLOW) {
                possibleActions.add(new GameAction(5, 27));
                possibleActions.add(new GameAction(6, 27));
                possibleActions.add(new GameAction(7, 27));
                possibleActions.add(new GameAction(8, 27));
            } else if (playerOnTurn == PlayerColor.BLUE) {
                possibleActions.add(new GameAction(13, 37));
                possibleActions.add(new GameAction(14, 37));
                possibleActions.add(new GameAction(15, 37));
                possibleActions.add(new GameAction(16, 37));
            } else if (playerOnTurn == PlayerColor.RED) {
                possibleActions.add(new GameAction(9, 47));
                possibleActions.add(new GameAction(10, 47));
                possibleActions.add(new GameAction(11, 47));
                possibleActions.add(new GameAction(12, 47));

            }
        }
        if (numberOfAttemptsToLeaveSpawn++ == 3) {
            nextPlayer();
        }
        return possibleActions;
    }

    private void nextPlayer() {
        playerOnTurn = findNextPlayerColor();
        numberOfAttemptsToLeaveSpawn = 1;
    }

    private PlayerColor findNextPlayerColor() {
        switch (playerOnTurn) {
            case GREEN:
                return PlayerColor.YELLOW;
            case YELLOW:
                return PlayerColor.BLUE;
            case BLUE:
                return PlayerColor.RED;
            case RED:
                return PlayerColor.GREEN;
            default:
                throw new RuntimeException();
        }
    }

    public void setPlayerOnTurn(PlayerColor playerColor) {
        this.playerOnTurn = playerColor;
        numberOfAttemptsToLeaveSpawn = 1;
    }
}
