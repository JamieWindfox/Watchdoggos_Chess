package com.example.chess_game;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    public static final int WHITE = 0, BLACK = 1;
    private int color;
    private Image image;
    private Field currentField;

    public Piece( int paraColor, Field paraField ) //TODO: Image hinzufügen
    {
        this.color = paraColor;
        this.currentField = paraField;
        //this.image = paraImage;
    }

    public Field getCurrentField() {
        return currentField;
    }

    public void setCurrentField(Field currentField) {
        this.currentField = currentField;
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

    public abstract List<Field> getMovements(Board b);
}
