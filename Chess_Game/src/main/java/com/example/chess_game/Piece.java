package com.example.chess_game;

import javafx.scene.image.Image;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Piece {
    public static final int WHITE = 0, BLACK = 1;
    private int color;
    private Image image;
    public Set<Field> validMoves;

    public Piece(int paraColor) //TODO: Image hinzufügen
    {
        this.color = paraColor;
        this.validMoves = new HashSet<>();
        //this.image = paraImage;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    /*public Image getImage()
    {
        if ( this.color == WHITE )
        {
            //TODO: Image rausholen mit '_white'
        }
        else
        {
            //TODO: Image rausholen mit '_white'
        }

        return image;
    }*/

    public boolean isFieldEmpty(Field f) {
        return f.getPiece() == null;
    }

    public void evaluateField(Field field, boolean shouldFieldBeEmpty) {
        if (shouldFieldBeEmpty && isFieldEmpty(field)) {
            validMoves.add(field);
        } else if (!shouldFieldBeEmpty && !isFieldEmpty(field) && this.getColor() != field.getPiece().getColor()) {
            validMoves.add(field);
        }
        // TODO invalid move if king can capture but it's protected by another piece
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

    public abstract Set<Field> getValidMoves(Field[][] fields, Field currentField, List<String> pastMoves);

    public abstract String getMoveAnnotation(Field oldField, Field newField);
}
