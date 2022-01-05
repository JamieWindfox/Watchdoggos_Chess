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

    // Why do we give the colour as an integer, it would be more intuitive to use if we just use an enum
    public Piece(int paraColor) //TODO: Image hinzufügen
    {
        this.color = paraColor;
        this.validMoves = new HashSet<>();
        this.image = new Image("graphics/" + (color == 0 ? "white_" : "black_") + getClass().getSimpleName().toLowerCase() + ".png");
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
            //TODO: Image rausholen mit 'white_'
        }
        else
        {
            //TODO: Image rausholen mit 'black_'
        }

        return image;
    }*/

    // Jamie, 05.01.22: Why is this method in the Piece class and not in Board? f.getPiece could also be called from
    // evaluate field directly, in my optinion it makes no sense to just call one method from another
    public boolean isFieldEmpty(Field f) {
        return f.getPiece() == null;
    }

    // Jamie, 05.01.22: Why is this method in the Piece class and not in Board?
    // Besides that I would rename the method to make clear that if the evaluation succeeds, the field is added
    // to validMoves. E.g. "evaluateAndAddField(...)"
    public void evaluateField(Field field, boolean shouldFieldBeEmpty) {
        if (shouldFieldBeEmpty && isFieldEmpty(field)) {
            validMoves.add(field);
        } else if (!shouldFieldBeEmpty && !isFieldEmpty(field) && this.getColor() != field.getPiece().getColor()) {
            validMoves.add(field);
        }
    }

    // Jamie, 05.01.22: To add to the preceeding two methods, I put this here
    // Checks if the given coordinates are in the board
    public static boolean areCoordinatesValid(int row, int column) {
        return row >= 0 && row <= 7 && column >= 0 && column <= 7;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public abstract Set<Field> getValidMoves(Field[][] fields, Field currentField, List<String> pastMoves);

    public abstract String getMoveAnnotation(Field oldField, Field newField);

    public Image getImage() {
        return image;
    }
}
