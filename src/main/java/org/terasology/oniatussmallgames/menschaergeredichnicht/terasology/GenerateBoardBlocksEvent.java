package org.terasology.oniatussmallgames.menschaergeredichnicht.terasology;

import org.terasology.entitySystem.event.Event;
import org.terasology.math.geom.Vector3i;

/**
 * Tiggers the block generation for a new board. Existing blocks will be replaced.
 */
public class GenerateBoardBlocksEvent implements Event {
    private final Vector3i upperLeftCorner;

    /**
     * Creates a new event to generate the board blocks at the given position.
     * @param upperLeftCorner The upper left corner next to the green house. The board will be generated in x+ and z+ direction.
     *
     */
    public GenerateBoardBlocksEvent(Vector3i upperLeftCorner) {
        this.upperLeftCorner = upperLeftCorner;
    }

    public Vector3i getUpperLeftCorner() {
        return upperLeftCorner;
    }
}
