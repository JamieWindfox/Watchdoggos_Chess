package com.example.chess_game;

import java.util.HashSet;
import java.util.Set;

public class Pawn extends Piece {

    public Pawn(int color) {
        super(color);
    }

    @Override
    public Set<Field> getValidMoves(Field[][] fields, Field currentField) {
        Set<Field> validMoves = new HashSet<>();
        int pieceColor = this.getColor();
        boolean atStart = (pieceColor == WHITE && currentField.getRow() == 1)
                || (pieceColor == BLACK && currentField.getRow() == 6);

        int moveCounter = pieceColor == WHITE ? 1 : -1;
        Field field = fields[currentField.getRow() + moveCounter][currentField.getColumn()];
        if (isFieldEmpty(field)) {
            validMoves.add(field);

            if (atStart) {
                moveCounter = pieceColor == WHITE ? moveCounter + 1 : moveCounter - 1;
                field = fields[currentField.getRow() + moveCounter][currentField.getColumn()];
                if (isFieldEmpty(field)) validMoves.add(field);
            }
        }

        return validMoves;
    }

    private boolean isFieldEmpty(Field f) {
        return f.getPiece() == null;
    }
}


