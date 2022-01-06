package com.example.chess_game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rook extends Piece {

    public Rook(Color paraColor) {
        super(paraColor);
    }

    @Override
    public Set<Field> getValidMoves(Board board, Field currentField) {
        validMoves = new HashSet<>();
        for (int rowDiff = -(currentField.getRow()); rowDiff <= (7 - currentField.getRow()); rowDiff++) {
            for (int colDiff = -(currentField.getColumn()); colDiff <= (7 - currentField.getColumn()); colDiff++) {
                if (rowDiff == 0 && colDiff == 0) continue; // Field is piece itself

                // Moves on same row or column
                if (rowDiff == 0 || colDiff == 0) {
                    Field field = board.getFields()[currentField.getRow() + rowDiff][currentField.getColumn() + colDiff];
                    if (field.getPiece() != null && field.getPiece().getColor() == this.getColor()) continue;
                    if (board.isPieceBetweenFields(board.getFields(), currentField, field)) continue;

                    validateAndAddMove(field);
                }
            }
        }
        return validMoves;
    }

    @Override
    public String getMoveAnnotation(Field oldField, Field newField) {
        return "R" + newField.getFieldName();
    }
}
