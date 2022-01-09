package com.example.chess_game;

import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Field {
    private final String fieldName;
    private final int row;
    private final int column;
    private final ImageView cell;
    // Todo
    // UI-Coordinates
    private Piece piece;

    public Field(String fieldName, int row, int column, int size) {
        this.fieldName = fieldName;
        this.row = row;
        this.column = column;

        this.cell = new ImageView();
        cell.setFitHeight(size);
        cell.setFitWidth(size);
        cell.setPickOnBounds(true);
        cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Game.getBoard().setLastClickedField(row, column);
                System.out.println("Field " + fieldName + " was clicked");
            }
        });
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

    public ImageView getCell() { return cell; }
}
