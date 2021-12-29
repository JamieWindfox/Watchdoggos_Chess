package com.example.chess_game;

import javafx.scene.image.Image;

import java.util.Set;

public abstract class Piece {
    public static final int WHITE = 0, BLACK = 1;
    private int color;
    private Image image;

    public Piece(int paraColor) //TODO: Image hinzufügen
    {
        this.color = paraColor;
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

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public abstract Set<Field> getValidMoves(Field[][] fields, Field currentField);
}
