package com.example.chess_game;

import javafx.scene.image.Image;

import java.util.HashSet;
import java.util.Set;

public abstract class Piece {
    private Color color;
    private Image image;
    public Set<Field> validMoves;

    public Piece(Color color) //TODO: Image hinzufügen
    {
        this.color = color;
        this.validMoves = new HashSet<>();
        this.image = new Image("graphics/" + (color == Color.WHITE ? "white_" : "black_") + getClass().getSimpleName().toLowerCase() + ".png");
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    /*public Image getImage()
    {
        if ( this.color == WHITE )
        {
            //TODO: Image rausholen mit 'white_'
        }
        else
        {
            //TODO: Image rausholen mit 'black_'
        }

        return image;
    }*/

    /**
     * A method to validate moves depending on whether they are moves with or without captures
     * and once validated those moves will be added to the set of current valid moves
     *
     * @param moveToField The field that the piece is moving to
     */
    public void validateAndAddMove(Field moveToField) {
        if (moveToField.getPiece() == null) {
            validMoves.add(moveToField);
        } else if (moveToField.getPiece() != null && this.color != moveToField.getPiece().getColor()) {
            validMoves.add(moveToField);
        }
    }

    /**
     * Method to check the boundaries of a field
     *
     * @param row    The given row coordinate
     * @param column The given column coordinate
     * @return true if row and column are within boundary - otherwise false
     */
    public static boolean areCoordinatesValid(int row, int column) {
        return row >= 0 && row <= 7 && column >= 0 && column <= 7;
    }

    public boolean isPieceBetweenFields(Field[][] fields, Field field1, Field field2) {
        int rowDiff = Math.max(field1.getRow(), field2.getRow()) - Math.min(field1.getRow(), field2.getRow());
        int colDiff = Math.max(field1.getColumn(), field2.getColumn()) - Math.min(field1.getColumn(), field2.getColumn());
        if (rowDiff == 0) { // Pieces are in the same row
            for (int i = Math.min(field1.getColumn(), field2.getColumn()) + 1; i < Math.max(field1.getColumn(), field2.getColumn()); i++)
                if(!isFieldEmpty(fields[field1.getRow()][i])) return true;
        } else if (colDiff == 0) { // Pieces are in the same column
            for (int i = Math.min(field1.getRow(), field2.getRow()) + 1; i < Math.max(field1.getRow(), field2.getRow()); i++)
                if(!isFieldEmpty(fields[i][field1.getColumn()])) return true;
        } else if (rowDiff == colDiff) { // Pieces are diagonal to each other
            if (field1.getRow() < field2.getRow()) {
                if (field1.getColumn() < field2.getColumn()) {
                    for(int i = 1; (i + field1.getColumn()) < field2.getColumn(); i++)
                        if(!isFieldEmpty(fields[field1.getRow() + i][field1.getColumn() + i])) return true;
                } else {
                    for(int i = 1; (field1.getColumn() - i) > field2.getColumn(); i++)
                        if(!isFieldEmpty(fields[field1.getRow() + i][field1.getColumn() - i])) return true;
                }
            } else return isPieceBetweenFields(fields, field2, field1);
        }
        return false;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public abstract Set<Field> getValidMoves(Board board, Field currentField);

    public abstract String getMoveAnnotation(Field oldField, Field newField);

    public Image getImage() {
        return image;
    }
}
