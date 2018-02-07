package org.terasology.oniatussmallgames.menschaergeredichnicht;

import java.util.Objects;

class Piece {
    private final PlayerColor playerColor;
    private final int index;

    public Piece(PlayerColor playerColor, int index) {
        this.playerColor = playerColor;
        this.index = index;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return index == piece.index &&
                playerColor == piece.playerColor;
    }

    @Override
    public int hashCode() {

        return Objects.hash(playerColor, index);
    }
}
