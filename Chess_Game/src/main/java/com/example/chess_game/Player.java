package com.example.chess_game;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private final ChessColor color;
    private ChessTimer timer;
    private final Set<Piece> pieces;
    private final String name;

    public Player(ChessColor color, String name, ChessTimer timer) {
        this.pieces = new HashSet<>();
        this.color = color;
        this.name = name;
        this.timer = timer;
        //this.timer = new Timer(timerLabel);
    }

    public void addPiece(Piece piece) {
        this.pieces.add(piece);
    }

    public void promotePiece(Pawn pawn, Piece promotedPiece) {
        this.pieces.remove(pawn);
        this.pieces.add(promotedPiece);
    }

    public ChessTimer getTimer() {
        return timer;
    }

    public ChessColor getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
