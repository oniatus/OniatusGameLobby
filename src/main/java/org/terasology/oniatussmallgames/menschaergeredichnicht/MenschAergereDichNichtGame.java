package org.terasology.oniatussmallgames.menschaergeredichnicht;

import java.util.*;
import java.util.stream.Collectors;

public class MenschAergereDichNichtGame {

    public static final int OFFSET_SPAWN_GREEN = 1;
    public static final int OFFSET_SPAWN_YELLOW = 5;
    public static final int OFFSET_SPAWN_RED = 9;
    public static final int OFFSET_SPAWN_BLUE = 13;
    public static final int FIRST_FIELD_OFFSET = 1;


    private PiecePositionManager piecePositionManager = new PiecePositionManager();

    public MenschAergereDichNichtGame() {
        initializePiecesOnSpawn();
    }

    private void initializePiecesOnSpawn() {
        PlayerColor[] playerColorsInSpawnOrder = new PlayerColor[]{PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLUE};
        int boardPosition = FIRST_FIELD_OFFSET;
        for (int pieceColorIndex = 0; pieceColorIndex < 4; pieceColorIndex++) {
            for (int pieceOffset = 0; pieceOffset < 4; pieceOffset++) {
                piecePositionManager.addPiece(boardPosition++, new Piece(playerColorsInSpawnOrder[pieceColorIndex], pieceOffset));
            }
        }
    }

    private PlayerColor playerOnTurn = PlayerColor.GREEN;
    private int numberOfAttemptsToLeaveSpawn = 1;


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
        boolean isEndOfPly = diceResult != 6;
        List<GameAction> possibleActions = piecePositionManager.streamPiecesOnBoard(playerOnTurn)
                .map(piece -> new GameAction(piecePositionManager.getPiecePosition(piece), findToPosition(diceResult, piece), isEndOfPly))
                .collect(Collectors.toList());

        filterActionsTargetingOwnPieces(possibleActions);
        return possibleActions;
    }

    private int findToPosition(int diceResult, Piece piece) {
        int targetPosition = piecePositionManager.getPiecePosition(piece);
        for(int i = 0; i < diceResult; i++){
            targetPosition = findNextPosition(targetPosition,piece.getPlayerColor());
        }
        return targetPosition;
    }

    private int findNextPosition(int fromPosition, PlayerColor playerColor) {
        if(fromPosition == 56){
            if(playerColor == PlayerColor.GREEN){
                return 57;
            }else{
                return 17;
            }
        }else if(fromPosition == 26){
            if(playerColor == PlayerColor.YELLOW){
                return 61;
            }else{
                return 27;
            }
        }else if(fromPosition == 36){
            if(playerColor == PlayerColor.BLUE){
                return 68;
            }else{
                return 37;
            }
        }
        return ++fromPosition;
    }

    private void filterActionsTargetingOwnPieces(List<GameAction> possibleActions) {
        Set<GameAction> actionsToRemove = new HashSet<>();
        for(GameAction action : possibleActions){
            Piece pieceOnPosition = piecePositionManager.findPieceOnPosition(action.getToPosition());
            if(pieceOnPosition != null && pieceOnPosition.getPlayerColor() == playerOnTurn){
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
        if(pieceOnPosition != null){
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
        teleportPiece(piecePositionManager.getPiecePosition(piece),nextFreeSpawnAreaPosition);
    }

    private int findNextFreeSpawnAreaPosition(int pieceColorSpawnOffset) {
        for(int i = 0; i < 4; i++){
            int spawnAreaPosition = pieceColorSpawnOffset + i;
            if(piecePositionManager.findPieceOnPosition(spawnAreaPosition) == null){
                return spawnAreaPosition;
            }
        }
        throw new RuntimeException("Spawn area occupied");
    }

    private int findSpawnOffset(PlayerColor playerColor) {
        switch (playerColor) {
            case GREEN:
                return OFFSET_SPAWN_GREEN;
            case YELLOW:
                return OFFSET_SPAWN_YELLOW;
            case RED:
                return OFFSET_SPAWN_RED;
            case BLUE:
                return OFFSET_SPAWN_BLUE;
        }
        throw new RuntimeException();
    }

    public int getPiecePosition(PlayerColor playerColor, int pieceIndex) {
        return piecePositionManager.getPiecePosition(playerColor, pieceIndex);
    }

    public Piece teleportPiece(int fromPosition, int toPosition) {
        return piecePositionManager.movePiece(fromPosition, toPosition);
    }

    public int getPiecePosition(Piece piece) {
        return getPiecePosition(piece.getPlayerColor(),piece.getIndex());
    }
}
