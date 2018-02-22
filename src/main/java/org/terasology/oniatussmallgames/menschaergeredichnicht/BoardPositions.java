package org.terasology.oniatussmallgames.menschaergeredichnicht;

import java.util.EnumMap;
import java.util.Map;

//see doc/board.jpg for a graphical representation of the board to index mapping
class BoardPositions {


    public static final int FIRST_FIELD_OFFSET = 1;

    public static final int OFFSET_SPAWN_GREEN = 1;
    public static final int OFFSET_SPAWN_YELLOW = 5;
    public static final int OFFSET_SPAWN_RED = 9;
    public static final int OFFSET_SPAWN_BLUE = 13;

    public static final int HOUSE_ENTRY_GREEN = 56;
    public static final int HOUSE_ENTRY_YELLOW = 26;
    public static final int HOUSE_ENTRY_RED = 46;
    public static final int HOUSE_ENTRY_BLUE = 36;

    public static final int FIRST_HOUSE_GREEN = 57;
    public static final int FIRST_HOUSE_YELLOW = 61;
    public static final int FIRST_HOUSE_RED = 69;
    public static final int FIRST_HOUSE_BLUE = 68;

    public static final int SPAWN_POSITION_GREEN = 17;
    public static final int SPAWN_POSITION_YELLOW = 27;
    public static final int SPAWN_POSITION_RED = 47;
    public static final int SPAWN_POSITION_BLUE = 37;

    private static EnumMap<PlayerColor, Integer> SPAWN_OFFSETS = new EnumMap<>(PlayerColor.class);

    static {
        SPAWN_OFFSETS.put(PlayerColor.GREEN, OFFSET_SPAWN_GREEN);
        SPAWN_OFFSETS.put(PlayerColor.YELLOW, OFFSET_SPAWN_YELLOW);
        SPAWN_OFFSETS.put(PlayerColor.RED, OFFSET_SPAWN_RED);
        SPAWN_OFFSETS.put(PlayerColor.BLUE, OFFSET_SPAWN_BLUE);
    }

    private static EnumMap<PlayerColor, Integer> SPAWN_POSITIONS = new EnumMap<>(PlayerColor.class);

    static {
        SPAWN_POSITIONS.put(PlayerColor.GREEN, SPAWN_POSITION_GREEN);
        SPAWN_POSITIONS.put(PlayerColor.YELLOW, SPAWN_POSITION_YELLOW);
        SPAWN_POSITIONS.put(PlayerColor.RED, SPAWN_POSITION_RED);
        SPAWN_POSITIONS.put(PlayerColor.BLUE, SPAWN_POSITION_BLUE);
    }

    private static final Map<PlayerColor, Integer> FIRST_HOUSE_POSITIONS = new EnumMap<>(PlayerColor.class);

    static {
        FIRST_HOUSE_POSITIONS.put(PlayerColor.GREEN, FIRST_HOUSE_GREEN);
        FIRST_HOUSE_POSITIONS.put(PlayerColor.YELLOW, FIRST_HOUSE_YELLOW);
        FIRST_HOUSE_POSITIONS.put(PlayerColor.RED, FIRST_HOUSE_RED);
        FIRST_HOUSE_POSITIONS.put(PlayerColor.BLUE, FIRST_HOUSE_BLUE);
    }

    private static final Map<PlayerColor, Integer> HOUSE_ENTRIES = new EnumMap<>(PlayerColor.class);

    static {
        HOUSE_ENTRIES.put(PlayerColor.GREEN, HOUSE_ENTRY_GREEN);
        HOUSE_ENTRIES.put(PlayerColor.YELLOW, HOUSE_ENTRY_YELLOW);
        HOUSE_ENTRIES.put(PlayerColor.RED, HOUSE_ENTRY_RED);
        HOUSE_ENTRIES.put(PlayerColor.BLUE, HOUSE_ENTRY_BLUE);
    }

    static int findFirstHousePosition(PlayerColor playerColor) {
        return FIRST_HOUSE_POSITIONS.get(playerColor);
    }

    static int findHouseEntry(PlayerColor playerColor) {
        return HOUSE_ENTRIES.get(playerColor);
    }

    static boolean isHouseEntry(int position) {
        return HOUSE_ENTRIES.values().contains(position);
    }

    static int findSpawnPosition(PlayerColor playerColor) {
        return SPAWN_POSITIONS.get(playerColor);
    }

    static int findSpawnOffset(PlayerColor playerColor) {
        return SPAWN_OFFSETS.get(playerColor);
    }

    public static boolean isAnyHousePosition(int toPosition) {
        for (int houseEntry : HOUSE_ENTRIES.values()) {
            for (int offset = 0; offset < 4; offset++) {
                if (toPosition == houseEntry + offset) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int findLastHousePosition(PlayerColor playerColor) {
        return findFirstHousePosition(playerColor) + 3;
    }
}
