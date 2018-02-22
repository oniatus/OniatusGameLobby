package org.terasology.oniatussmallgames.menschaergeredichnicht;

import java.util.*;
import java.util.stream.Collectors;

import static org.terasology.oniatussmallgames.menschaergeredichnicht.BoardPositions.*;

public class MenschAergereDichNichtGame {


    private PiecePositionManager piecePositionManager = new PiecePositionManager();
    private PlayerColor playerOnTurn = PlayerColor.GREEN;
    private int numberOfAttemptsToLeaveSpawn = 1;

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
        return playerOnTurn;
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
        return piecePositionManager.streamPiecesOnSpawn(playerOnTurn)
                .map(piece -> new GameAction(piecePositionManager.getPiecePosition(piece), findSpawnPosition(playerOnTurn), false))
                .collect(Collectors.toList());
    }


    private Collection<? extends GameAction> findPossibleActionsForPiecesOnBoard(int diceResult) {
        boolean isEndOfPly = diceResult != 6;
        List<GameAction> possibleActions = piecePositionManager.streamPiecesOnBoard(playerOnTurn)
                .map(piece -> new GameAction(piecePositionManager.getPiecePosition(piece), findToPosition(diceResult, piece), isEndOfPly))
                .collect(Collectors.toList());

        filterActionsTargetingOwnPieces(possibleActions);
        return possibleActions;
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
            if (pieceOnPosition != null && pieceOnPosition.getPlayerColor() == playerOnTurn) {
                actionsToRemove.add(action);
            }
        }
        possibleActions.removeAll(actionsToRemove);
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
        Piece pieceOnPosition = piecePositionManager.findPieceOnPosition(toPosition);
        if (pieceOnPosition != null) {
            movePieceBackToSpawn(pieceOnPosition);
        }
        piecePositionManager.movePiece(fromPosition, toPosition);
        if (gameAction.isEndOfPly()) {
            nextPlayer();
        }
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
}
