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
                .map(piece -> new GameAction(piecePositionManager.getPiecePosition(piece), piecePositionManager.getPiecePosition(piece) + diceResult, isEndOfPly))
                .collect(Collectors.toList());

        filterActionsTargetingOwnPieces(possibleActions);
        return possibleActions;
    }

    private void filterActionsTargetingOwnPieces(List<GameAction> possibleActions) {
        Set<GameAction> actionsToRemove = new HashSet<>();
        for(GameAction action : possibleActions){
            for(GameAction action2 : possibleActions){
                if(action.getToPosition() == action2.getFromPosition()){
                    actionsToRemove.add(action);
                }
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
        piecePositionManager.movePiece(fromPosition, toPosition);
        if (gameAction.isEndOfPly()) {
            nextPlayer();
        }
    }

    public int getPiecePosition(PlayerColor playerColor, int pieceIndex) {
        return piecePositionManager.getPiecePosition(playerColor, pieceIndex);
    }

    public Piece teleportPiece(int fromPosition, int toPosition) {
        return piecePositionManager.movePiece(fromPosition, toPosition);
    }
}
