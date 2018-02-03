package org.terasology.oniatussmallgames.menschaergeredichnicht;

public class MenschAergereDichNichtGame {

    public static final int OFFSET_SPAWN_GREEN = 1;
    public static final int OFFSET_SPAWN_YELLOW = 5;
    public static final int OFFSET_SPAWN_RED = 9;
    public static final int OFFSET_SPAWN_BLUE = 13;

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
        return PlayerColor.GREEN;
    }
}
