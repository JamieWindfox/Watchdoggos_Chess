package com.example.chess_game;

import java.util.HashSet;
import java.util.Set;

public class Rook extends Piece {

    public final String ANNOTATION_LETTER = "R";

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
        return newField.getPiece() != null ? ANNOTATION_LETTER + "x" + newField.getFieldName() : ANNOTATION_LETTER + newField.getFieldName();
    }

    @Override
    public String getAnnotationLetter() {
        return ANNOTATION_LETTER;
    }

    @Override
    public Set<Field> getInBetweenFields(Field startField, Field endField, Field[][] fields) {
        Set<Field> betweenFields = new HashSet<>();
        int rowDiff = Math.max(startField.getRow(), endField.getRow()) - Math.min(startField.getRow(), endField.getRow());
        int colDiff = Math.max(startField.getColumn(), endField.getColumn()) - Math.min(startField.getColumn(), endField.getColumn());
        if (rowDiff == 0) { // Pieces are in the same row
            for (int i = Math.min(startField.getColumn(), endField.getColumn()) + 1; i < Math.max(startField.getColumn(), endField.getColumn()); i++)
                betweenFields.add(fields[startField.getRow()][i]);
        } else if (colDiff == 0) { // Pieces are in the same column
            for (int i = Math.min(startField.getRow(), endField.getRow()) + 1; i < Math.max(startField.getRow(), endField.getRow()); i++)
                betweenFields.add(fields[i][startField.getColumn()]);
        }
        return betweenFields;
    }
}
