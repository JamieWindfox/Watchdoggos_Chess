package com.example.chess_game;

public class Field {
    private final String fieldName;
    private int x;
    private int y;
    // Todo
    // UI-Coordinates
    // Piece

    public Field(String fieldName, int x, int y) {
        this.fieldName = fieldName;
        this.x = x;
        this.y = y;
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
