package com.example.chess_game;

public class Field {
    private final String fieldName;
    private final int row;
    private final int column;
    // Todo
    // UI-Coordinates
    private Piece piece;

    public Field(String fieldName, int row, int column) {
        this.fieldName = fieldName;
        this.row = row;
        this.column = column;
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
