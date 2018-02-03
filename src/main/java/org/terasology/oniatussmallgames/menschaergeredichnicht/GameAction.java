package org.terasology.oniatussmallgames.menschaergeredichnicht;

public class GameAction {
    private int fromPosition;
    private int toPosition;

    public GameAction(int fromPosition, int toPosition) {
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
    }

    public int getFromPosition() {
        return fromPosition;
    }

    public int getToPosition() {
        return toPosition;
    }
}
