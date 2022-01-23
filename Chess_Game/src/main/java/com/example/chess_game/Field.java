package com.example.chess_game;

import javafx.scene.image.ImageView;

public class Field {
    private final String fieldName;
    private final int row;
    private final int column;
    private final ImageView cell;
    private Piece piece;

    public Field(String fieldName, int row, int column, int size) {
        this.fieldName = fieldName;
        this.row = row;
        this.column = column;

        this.cell = new ImageView();
        cell.setFitHeight(size);
        cell.setFitWidth(size);
        cell.setPickOnBounds(true);
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

    public ImageView getCell() {
        return cell;
    }

    @Override
    public String toString() {
        return "Field[" + fieldName + "]";
    }
}
