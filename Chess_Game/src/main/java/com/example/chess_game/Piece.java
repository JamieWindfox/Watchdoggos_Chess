package com.example.chess_game;

import javafx.scene.image.Image;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Piece {
    public static final int WHITE = 0, BLACK = 1;
    private final int color;
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

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public abstract Set<Field> getValidMoves(Field[][] fields, Field currentField, List<String> pastMoves);

    public abstract String getMoveAnnotation(Field oldField, Field newField);
}
