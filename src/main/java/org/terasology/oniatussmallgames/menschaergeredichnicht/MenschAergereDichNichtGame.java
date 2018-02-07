package org.terasology.oniatussmallgames.menschaergeredichnicht;

import java.util.*;
import java.util.stream.Collectors;

public class MenschAergereDichNichtGame {

    public static final int OFFSET_SPAWN_GREEN = 1;
    public static final int OFFSET_SPAWN_YELLOW = 5;
    public static final int OFFSET_SPAWN_RED = 9;
    public static final int OFFSET_SPAWN_BLUE = 13;
    public static final int FIRST_FIELD_OFFSET = 1;
    public static final int BOARD_BEGIN_INDEX = 17;

    public void teleportPiece(int fromPosition, int toPosition) {
        movePiece(fromPosition, toPosition);
    }

    private static class Piece {
        private PlayerColor playerColor;
        private int index;

        public Piece(PlayerColor playerColor, int index) {
            this.playerColor = playerColor;
            this.index = index;
        }

        public PlayerColor getPlayerColor() {
            return playerColor;
        }

        public void setPlayerColor(PlayerColor playerColor) {
            this.playerColor = playerColor;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    private Map<Integer, Piece> piecePositions = new HashMap<>();

    public MenschAergereDichNichtGame() {
        PlayerColor[] playerColors = new PlayerColor[]{PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLUE};
        int position = FIRST_FIELD_OFFSET;
        for (int pieceIndex = 0; pieceIndex < 4; pieceIndex++) {
            for (int pieceOffset = 0; pieceOffset < 4; pieceOffset++) {
                piecePositions.put(position++, new Piece(playerColors[pieceIndex], pieceOffset));
            }
        }
    }

    private PlayerColor playerOnTurn = PlayerColor.GREEN;
    private int numberOfAttemptsToLeaveSpawn = 1;

    public int getPiecePosition(PlayerColor playerColor, int pieceNumber) {
        return piecePositions.entrySet().stream().filter(e -> e.getValue().getPlayerColor() == playerColor && e.getValue().getIndex() == pieceNumber).map(Map.Entry::getKey).findFirst().get();
    }

    public PlayerColor getPlayerColorOnTurn() {
        return playerOnTurn;
    }

    public List<GameAction> findPossibleActions(int diceResult) {
        ArrayList<GameAction> possibleActions = new ArrayList<>();
        if (diceResult == 6) {
            possibleActions.addAll(findPossibleActionsForPiecesOnSpawn());
        } else {
            possibleActions.addAll(findPossibleActionsForPiecesOnBoard(diceResult));
        }
        if (numberOfAttemptsToLeaveSpawn++ == 3) {
            nextPlayer();
        }
        return possibleActions;
    }

    private Collection<? extends GameAction> findPossibleActionsForPiecesOnSpawn() {
        return piecePositions.entrySet().stream()
                .filter(e -> e.getValue().getPlayerColor() == playerOnTurn)
                .filter(e -> e.getKey() < BOARD_BEGIN_INDEX)
                .map(e -> new GameAction(e.getKey(),findSpawnPosition(playerOnTurn)))
                .collect(Collectors.toList());
    }

    private int findSpawnPosition(PlayerColor playerOnTurn) {
        switch (playerOnTurn) {
            case GREEN:
                return 17;
            case YELLOW:
                return 27;
            case BLUE:
                return 37;
            case RED:
                return 47;
        }
        throw new RuntimeException();
    }

    private Collection<? extends GameAction> findPossibleActionsForPiecesOnBoard(int diceResult) {
        return piecePositions.entrySet().stream()
                .filter(e -> e.getValue().getPlayerColor() == playerOnTurn)
                .filter(e -> e.getKey() >= BOARD_BEGIN_INDEX)
                .map(e -> new GameAction(e.getKey(), e.getKey() + diceResult))
                .collect(Collectors.toList());
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

    public void execute(GameAction gameAction) {
        int fromPosition = gameAction.getFromPosition();
        int toPosition = gameAction.getToPosition();
        movePiece(fromPosition, toPosition);
    }

    private void movePiece(int fromPosition, int toPosition) {
        Piece pieceToMove = piecePositions.remove(fromPosition);
        piecePositions.put(toPosition, pieceToMove);
    }
}
