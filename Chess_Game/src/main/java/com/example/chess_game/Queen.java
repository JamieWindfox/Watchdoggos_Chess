package com.example.chess_game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Queen extends Piece {

    public Queen(int paraColor) {
        super(paraColor);
    }

    @Override
    public Set<Field> getValidMoves(Field[][] fields, Field currentField, List<String> pastMoves) {
        validMoves = new HashSet<>();
        for (int rowDiff = -(currentField.getRow()); rowDiff <= (7 - currentField.getRow()); rowDiff++) {
            for (int colDiff = -(currentField.getColumn()); colDiff <= (7 - currentField.getColumn()); colDiff++) {
                if (rowDiff == 0 && colDiff == 0) continue; // Field is piece itself

                // Moves on same row or column or diagonally
                if (rowDiff == 0 || colDiff == 0 || Math.abs(colDiff) == Math.abs(rowDiff)) {
                    Field field = fields[currentField.getRow() + rowDiff][currentField.getColumn() + colDiff];
                    if (!isFieldEmpty(field) && field.getPiece().getColor() == this.getColor()) continue;
                    if (isPieceBetweenFields(fields, currentField, field)) continue;

                    evaluateField(field, true);
                    evaluateField(field, false);
                }
            }
        }
        return validMoves;
    }

    @Override
    public String getMoveAnnotation(Field oldField, Field newField) {
        return "Q" + newField.getFieldName();
    }
}
