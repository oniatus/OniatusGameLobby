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
        } else {
            possibleActions.addAll(findPossibleActionsForPiecesOnBoard(diceResult));
        }
        if (numberOfAttemptsToLeaveSpawn++ == 3) {
            nextPlayer();
        }
        return possibleActions;
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
