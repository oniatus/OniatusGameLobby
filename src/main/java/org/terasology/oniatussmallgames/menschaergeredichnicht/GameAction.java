package org.terasology.oniatussmallgames.menschaergeredichnicht;

public class GameAction {
    private int fromPosition;
    private int toPosition;
    private boolean isEndOfPly;

    public GameAction(int fromPosition, int toPosition, boolean isEndOfPly) {
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
        this.isEndOfPly = isEndOfPly;
    }

    public int getFromPosition() {
        return fromPosition;
    }

    public int getToPosition() {
        return toPosition;
    }

    public boolean isEndOfPly() {
        return isEndOfPly;
    }

    public void setEndOfPly(boolean endOfPly) {
        this.isEndOfPly = endOfPly;
    }
}
