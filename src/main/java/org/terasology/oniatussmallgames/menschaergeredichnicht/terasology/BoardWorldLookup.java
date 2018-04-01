package org.terasology.oniatussmallgames.menschaergeredichnicht.terasology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BoardWorldLookup {

    private static final String[][] BOARD = new String[][]{
            {"D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D"},
            {"D", "GH", "GH", "D", "D", "SE", "EW", "YS", "D", "D", "YH", "YH", "D"},
            {"D", "GH", "GH", "D", "D", "NS", "YH", "NS", "D", "D", "YH", "YH", "D"},
            {"D", "D", "D", "D", "D", "NS", "YH", "NS", "D", "D", "D", "D", "D"},
            {"D", "D", "D", "D", "D", "NS", "YH", "NS", "D", "D", "D", "D", "D"},
            {"D", "GS", "EW", "EW", "EW", "NW", "YH", "NE", "EW", "EW", "EW", "SW", "D"},
            {"D", "NS", "GH", "GH", "GH", "GH", "D", "BH", "BH", "BH", "BH", "NS", "D"},
            {"D", "NE", "EW", "EW", "EW", "SW", "RH", "SE", "EW", "EW", "EW", "BS", "D"},
            {"D", "D", "D", "D", "D", "NS", "RH", "NS", "D", "D", "D", "D", "D"},
            {"D", "D", "D", "D", "D", "NS", "RH", "NS", "D", "D", "D", "D", "D"},
            {"D", "RH", "RH", "D", "D", "NS", "RH", "NS", "D", "D", "BH", "BH", "D"},
            {"D", "RH", "RH", "D", "D", "RS", "EW", "NW", "D", "D", "BH", "BH", "D"},
            {"D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D", "D"}
    };

    private static List<BoardBlock> allBoardBlocks = initBoardBlocks();

    private static List<BoardBlock> initBoardBlocks() {
        List<BoardBlock> result = new ArrayList<>();
        for (int z = 0; z < BOARD.length; z++) {
            for (int x = 0; x < BOARD[z].length; x++) {
                result.add(new BoardBlock(x, z, "OniatusSmallGames:"+toBlockUri(BOARD[z][x])));
            }
        }
        return Collections.unmodifiableList(result);
    }

    static List<BoardBlock> getAllBoardBlocks(){
        return allBoardBlocks;
    }

    private static String toBlockUri(String boardTile) {
        switch (boardTile) {
            case "D":
                return "boardDefault";
            case "EW":
                return "boardEW";
            case "NE":
                return "boardNE";
            case "NS":
                return "boardNS";
            case "NW":
                return "boardNW";
            case "SE":
                return "boardSE";
            case "SW":
                return "boardSW";
            case "BH":
                return "homeBlue";
            case "GH":
                return "homeGreen";
            case "RH":
                return "homeRed";
            case "YH":
                return "homeYellow";
            case "BS":
                return "spawnBlue";
            case "GS":
                return "spawnGreen";
            case "RS":
                return "spawnRed";
            case "YS":
                return "spawnYellow";
            default:
                throw new IllegalArgumentException(boardTile);
        }
    }


    static class BoardBlock {
        private final int dx;
        private final int dz;
        private final String blockUri;

        BoardBlock(int dx, int dz, String blockUri) {
            this.dx = dx;
            this.dz = dz;
            this.blockUri = blockUri;
        }

        public int getDx() {
            return dx;
        }

        public int getDz() {
            return dz;
        }

        public String getBlockUri() {
            return blockUri;
        }
    }
}
