package com.example.chess_game;

import javafx.scene.control.Label;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private final Color color;
    private Timer timer;
    private final Set<Piece> pieces;
    private final String name;

    public Player(Color color, String name) {
        this.pieces = new HashSet<>();
        this.color = color;
        this.name = name;
        //this.timer = new Timer(timerLabel);
    }

    public void addPiece(Piece piece) {
        this.pieces.add(piece);
    }
}
