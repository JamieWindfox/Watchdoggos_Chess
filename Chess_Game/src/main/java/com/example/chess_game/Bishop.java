package com.example.chess_game;

import java.util.HashSet;
import java.util.Set;

public class Bishop extends Piece {

    public final String ANNOTATION_LETTER = "B";

    public Bishop(ChessColor paraColor) {
        super(paraColor);
    }

    @Override
    public Set<Field> getValidMoves(Board board, Field currentField) {
        validMoves = new HashSet<>();
        for (int rowDiff = -(currentField.getRow()); rowDiff <= (7 - currentField.getRow()); rowDiff++) {
            for (int colDiff = -(currentField.getColumn()); colDiff <= (7 - currentField.getColumn()); colDiff++) {
                // Moves on diagonally
                if (Math.abs(colDiff) == Math.abs(rowDiff) && rowDiff != 0) {
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

        if (rowDiff == colDiff) { // Pieces are diagonal to each other
            if (startField.getRow() < endField.getRow()) {
                if (startField.getColumn() < endField.getColumn()) {
                    for (int i = 1; (i + startField.getColumn()) < endField.getColumn(); i++)
                        betweenFields.add(fields[startField.getRow() + i][startField.getColumn() + i]);
                } else {
                    for (int i = 1; (startField.getColumn() - i) > endField.getColumn(); i++)
                        betweenFields.add(fields[startField.getRow() + i][startField.getColumn() - i]);
                }
            } else return getInBetweenFields(endField, startField, fields);
        }
        return betweenFields;
    }
}
