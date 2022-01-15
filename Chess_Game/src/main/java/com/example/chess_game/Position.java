package com.example.chess_game;

import java.util.Map;

public record Position(Map<Piece, Field> pieceLocation,
                       ChessColor playerToMove) {
}
