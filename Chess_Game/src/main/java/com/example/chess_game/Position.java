package com.example.chess_game;

import com.example.chess_game.pieces.Piece;

import java.util.Map;

public record Position(Map<Piece, Field> pieceLocation, ChessColor playerToMove) {
}
