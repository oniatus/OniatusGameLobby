package org.terasology.oniatussmallgames.menschaergeredichnicht;

import java.util.*;
import java.util.stream.Collectors;

import static org.terasology.oniatussmallgames.menschaergeredichnicht.BoardPositions.*;

public class MenschAergereDichNichtGame {


    private PiecePositionManager piecePositionManager = new PiecePositionManager();
    private PlayerColor playerOnTurnColor = PlayerColor.GREEN;
    private int numberOfAttemptsToLeaveSpawn = 1;
    private List<MenschAergereDichNichtGameListener> listeners = new ArrayList<>();

    public MenschAergereDichNichtGame() {
        initializePiecesOnSpawn();
    }

    private void initializePiecesOnSpawn() {
        PlayerColor[] playerColorsInSpawnOrder = new PlayerColor[]{PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLUE};
        int boardPosition = BoardPositions.FIRST_FIELD_OFFSET;
        for (int pieceColorIndex = 0; pieceColorIndex < 4; pieceColorIndex++) {
            for (int pieceOffset = 0; pieceOffset < 4; pieceOffset++) {
                piecePositionManager.addPiece(boardPosition++, new Piece(playerColorsInSpawnOrder[pieceColorIndex], pieceOffset));
            }
        }
    }

    public PlayerColor getPlayerColorOnTurn() {
        return playerOnTurnColor;
    }

    public List<GameAction> findPossibleActions(int diceResult) {
        ArrayList<GameAction> possibleActions = new ArrayList<>();
        if (diceResult == 6) {
            possibleActions.addAll(findPossibleActionsForPiecesOnSpawn());
        }
        possibleActions.addAll(findPossibleActionsForPiecesOnBoard(diceResult));
        if (numberOfAttemptsToLeaveSpawn++ == 3) {
            nextPlayer();
        }
        return possibleActions;
    }

    private Collection<? extends GameAction> findPossibleActionsForPiecesOnSpawn() {
        return piecePositionManager.streamPiecesOnSpawn(playerOnTurnColor)
                .map(piece -> new GameAction(piecePositionManager.getPiecePosition(piece), findSpawnPosition(playerOnTurnColor), false))
                .collect(Collectors.toList());
    }


    private Collection<? extends GameAction> findPossibleActionsForPiecesOnBoard(int diceResult) {
        boolean isEndOfPly = diceResult != 6;
        List<GameAction> possibleActions = findDefaultMovementActions(diceResult, isEndOfPly);
        filterActionsJumpingOverOwnPiecesInHouse(possibleActions);
        filterActionsLeavingTheBoard(possibleActions);
        filterActionsTargetingOwnPieces(possibleActions);
        return possibleActions;
    }

    private void filterActionsLeavingTheBoard(List<GameAction> possibleActions) {
        Set<GameAction> actionsToRemove = new HashSet<>();
        for (GameAction gameAction : possibleActions) {
            int lastHousePosition = findLastHousePosition(playerOnTurnColor);
            if (gameAction.getToPosition() > lastHousePosition) {
                actionsToRemove.add(gameAction);
            }
        }
        possibleActions.removeAll(actionsToRemove);
    }

    private void filterActionsJumpingOverOwnPiecesInHouse(List<GameAction> possibleActions) {
        Set<GameAction> actionsToRemove = new HashSet<>();
        for (GameAction gameAction : possibleActions) {
            if (areOwnPiecesInHouseOnPath(gameAction)) {
                actionsToRemove.add(gameAction);
            }
        }
        possibleActions.removeAll(actionsToRemove);
    }

    private boolean areOwnPiecesInHouseOnPath(GameAction gameAction) {
        int currentPosition = findNextPosition(gameAction.getFromPosition(), playerOnTurnColor);
        while (currentPosition != gameAction.getToPosition()) {
            Piece pieceOnPosition = piecePositionManager.findPieceOnPosition(currentPosition);
            if (pieceOnPosition != null && isAnyHousePosition(currentPosition)) {
                return true;
            }
            currentPosition = findNextPosition(currentPosition, playerOnTurnColor);
        }
        return false;
    }

    private List<GameAction> findDefaultMovementActions(int diceResult, boolean isEndOfPly) {
        return piecePositionManager.streamPiecesOnBoard(playerOnTurnColor)
                .map(piece -> new GameAction(piecePositionManager.getPiecePosition(piece), findToPosition(diceResult, piece), isEndOfPly))
                .collect(Collectors.toList());
    }

    private int findToPosition(int diceResult, Piece piece) {
        int targetPosition = piecePositionManager.getPiecePosition(piece);
        for (int i = 0; i < diceResult; i++) {
            targetPosition = findNextPosition(targetPosition, piece.getPlayerColor());
        }
        return targetPosition;
    }

    private int findNextPosition(int fromPosition, PlayerColor playerColor) {
        if (isHouseEntry(fromPosition) && fromPosition == findHouseEntry(playerColor)) {
            return findFirstHousePosition(playerColor);
        }
        return nextPositionAroundTheBoard(fromPosition);
    }

    private int nextPositionAroundTheBoard(int position) {
        //board index wraps at the green house to green spawn
        if (position == findHouseEntry(PlayerColor.GREEN)) {
            return findSpawnPosition(PlayerColor.GREEN);
        }
        return position + 1;
    }

    private void filterActionsTargetingOwnPieces(List<GameAction> possibleActions) {
        Set<GameAction> actionsToRemove = new HashSet<>();
        for (GameAction action : possibleActions) {
            Piece pieceOnPosition = piecePositionManager.findPieceOnPosition(action.getToPosition());
            if (pieceOnPosition != null && pieceOnPosition.getPlayerColor() == playerOnTurnColor) {
                actionsToRemove.add(action);
            }
        }
        possibleActions.removeAll(actionsToRemove);
    }

    private void nextPlayer() {
        playerOnTurnColor = findNextPlayerColor();
        numberOfAttemptsToLeaveSpawn = 1;
    }

    private PlayerColor findNextPlayerColor() {
        switch (playerOnTurnColor) {
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

    public void setPlayerOnTurnColor(PlayerColor playerColor) {
        this.playerOnTurnColor = playerColor;
        numberOfAttemptsToLeaveSpawn = 1;
    }

    public void execute(GameAction gameAction) {
        int fromPosition = gameAction.getFromPosition();
        int toPosition = gameAction.getToPosition();
        Piece pieceOnPosition = piecePositionManager.findPieceOnPosition(toPosition);
        if (pieceOnPosition != null) {
            movePieceBackToSpawn(pieceOnPosition);
        }
        piecePositionManager.movePiece(fromPosition, toPosition);
        checkWinner();
        if (gameAction.isEndOfPly()) {
            nextPlayer();
        }
    }

    private void checkWinner() {
        if (isCurrentPlayerWinner()) {
            listeners.forEach(l -> l.onPlayerWon(playerOnTurnColor));
        }
    }

    private boolean isCurrentPlayerWinner() {
        for (int housePosition = findFirstHousePosition(playerOnTurnColor); housePosition <= findLastHousePosition(playerOnTurnColor); housePosition++) {
            if (piecePositionManager.findPieceOnPosition(housePosition) == null) {
                return false;
            }
        }
        return true;
    }

    private void movePieceBackToSpawn(Piece piece) {
        int pieceColorSpawnOffset = findSpawnOffset(piece.getPlayerColor());
        int nextFreeSpawnAreaPosition = findNextFreeSpawnAreaPosition(pieceColorSpawnOffset);
        teleportPiece(piecePositionManager.getPiecePosition(piece), nextFreeSpawnAreaPosition);
    }

    private int findNextFreeSpawnAreaPosition(int pieceColorSpawnOffset) {
        for (int i = 0; i < 4; i++) {
            int spawnAreaPosition = pieceColorSpawnOffset + i;
            if (piecePositionManager.findPieceOnPosition(spawnAreaPosition) == null) {
                return spawnAreaPosition;
            }
        }
        throw new RuntimeException("Spawn area occupied");
    }


    public int getPiecePosition(PlayerColor playerColor, int pieceIndex) {
        return piecePositionManager.getPiecePosition(playerColor, pieceIndex);
    }

    public Piece teleportPiece(int fromPosition, int toPosition) {
        return piecePositionManager.movePiece(fromPosition, toPosition);
    }

    public int getPiecePosition(Piece piece) {
        return getPiecePosition(piece.getPlayerColor(), piece.getIndex());
    }

    public void registerListener(MenschAergereDichNichtGameListener listener) {
        listeners.add(listener);
    }
}
