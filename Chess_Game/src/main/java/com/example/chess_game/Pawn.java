package com.example.chess_game;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(int color, Field position) {
        super(color,position);
    }

    @Override
    public List<Field> getMovements(Board b) {
        List<Field> movementList = new ArrayList<>();

        Field[][] board = b.getFields();
        int pieceColor = this.getColor();
        int x = this.getCurrentField().getX();
        int y = this.getCurrentField().getY();

        int newYPosition = 0;
        if ( pieceColor == WHITE )
        {
            newYPosition = y - 1;

            //TODO: Überprüfen ob es schon mal bewegt wurde, wenn nicht y+2

            /*if ( y + 2 < 8 )
            {
                movementList.add( board[y+2][x] );
            }*/

        }
        else
        {
            newYPosition = y - 1;

            //TODO: Überprüfen ob es schon mal bewegt wurde, wenn nicht y-2

            /*if ( y - 2 < 8 )
            {
                movementList.add( board[y+2][x] );
            }*/
        }

        if ( newYPosition >= 0 && newYPosition < 8 )
        {
            movementList.add( board[newYPosition][x] );
        }

        if ( x + 1 < 8 && newYPosition >= 0 && newYPosition < 8 )
        {
            movementList.add( board[newYPosition][x+1] );
        }

        if ( x - 1 >= 0 && newYPosition >= 0 && newYPosition < 8 )
        {
            movementList.add( board[newYPosition][x-1] );
        }
        
        return movementList;
    }
}


