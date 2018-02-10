package org.terasology.oniatussmallgames.menschaergeredichnicht;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Map;
import java.util.stream.Stream;

class PiecePositionManager {


    private static final int BOARD_BEGIN_INDEX = 17;

    private BiMap<Integer, Piece> piecePositions = HashBiMap.create();

    public void addPiece(int boardPosition, Piece piece) {
        piecePositions.put(boardPosition, piece);
    }

    public int getPiecePosition(PlayerColor playerColor, int pieceNumber) {
        return piecePositions.entrySet().stream()
                .filter(e -> e.getValue().getPlayerColor() == playerColor && e.getValue().getIndex() == pieceNumber)
                .map(Map.Entry::getKey).findFirst().get();
    }

    public int getPiecePosition(Piece piece) {
        return piecePositions.inverse().get(piece);
    }

    public Stream<Piece> streamPiecesOnBoard(PlayerColor playerOnTurnColor) {
        return piecePositions.entrySet().stream()
                .filter(e -> e.getValue().getPlayerColor() == playerOnTurnColor)
                .filter(e -> e.getKey() >= BOARD_BEGIN_INDEX).map(Map.Entry::getValue);
    }


    public Piece movePiece(int fromPosition, int toPosition) {
        Piece pieceToMove = piecePositions.remove(fromPosition);
        piecePositions.put(toPosition, pieceToMove);
        return pieceToMove;
    }

    public Stream<Piece> streamPiecesOnSpawn(PlayerColor playerOnTurnColor) {
        return piecePositions.entrySet().stream()
                .filter(e -> e.getValue().getPlayerColor() == playerOnTurnColor)
                .filter(e -> e.getKey() < BOARD_BEGIN_INDEX).map(Map.Entry::getValue);
    }
}
