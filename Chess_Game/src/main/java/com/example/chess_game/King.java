package com.example.chess_game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class King extends Piece {

    public King(int paraColor) {
        super(paraColor);
    }

    @Override
    public Set<Field> getValidMoves(Field[][] fields, Field currentField, List<String> pastMoves) {
        Set<Field> validMoves = new HashSet<>();
        /*

        [-1][-1] [-1][0] [-1][+1]
        [0][-1]   [0][0]  [0][+1]
        [+1][-1] [+1][0] [+1][+1]

         */
        for (int rowDiff = -1; rowDiff <= 1; rowDiff++) {
            for (int colDiff = -1; colDiff <= 1; colDiff++) {
                Field field = fields[currentField.getRow() + rowDiff][currentField.getColumn() + colDiff];
                // todo Add check if piece is protected by another piece
                // todo valid moves
            }
        }
        return validMoves;
    }

    @Override
    public String getMoveAnnotation(Field oldField, Field newField) {
        if (newField.getRow() - oldField.getRow() == 2) {
            return "O-O";
        } else if (newField.getRow() - oldField.getRow() == -2) {
            return "O-O-O";
        }
        return "K" + newField.getFieldName();
    }
}
