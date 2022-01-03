package com.example.chess_game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class King extends Piece {

    public King(int paraColor) {
        super(paraColor);
    }

    @Override
    public Set<Field> getValidMoves(Field[][] fields, Field currentField, List<String> pastMoves) {
        validMoves = new HashSet<>();
        for (int rowDiff = -1; rowDiff <= 1; rowDiff++) {
            for (int colDiff = -1; colDiff <= 1; colDiff++) {
                if (currentField.getRow() + rowDiff < 0 || currentField.getRow() + rowDiff > 7 ||
                        currentField.getColumn() + colDiff < 0 || currentField.getColumn() + colDiff > 7 ||
                        (rowDiff == 0 && colDiff == 0)) continue;
                Field field = fields[currentField.getRow() + rowDiff][currentField.getColumn() + colDiff];
                if (field.getPiece() == null) {
                    evaluateField(field, true);
                } else if (field.getPiece() != null && !isPieceProtected(fields, field)) {
                    evaluateField(field, false);
                }
                // todo Castling
                // todo check on every piece if "valid moves" lead to self-check
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

    private boolean isPieceProtected(Field[][] fields, Field field) {
        int oldColor = field.getPiece().getColor();
        field.getPiece().setColor(this.getColor());
        boolean isProtected = false;
        for (Field[] column : fields) {
            for (Field boardField : column) {
                Piece possiblePiece = boardField.getPiece();
                if (possiblePiece != null && possiblePiece.getColor() != this.getColor() && boardField != field) {
                    if (possiblePiece instanceof King) {
                        isProtected = Math.abs(boardField.getRow() - field.getRow()) == 1 ||
                                Math.abs(boardField.getColumn() - field.getColumn()) == 1;
                    } else {
                        isProtected = possiblePiece.getValidMoves(fields, boardField, new ArrayList<>())
                                .stream()
                                .anyMatch(validFields -> validFields.getFieldName().equals(field.getFieldName()));
                    }
                    if (isProtected) break;
                }
            }
            if (isProtected) break;
        }
        field.getPiece().setColor(oldColor);
        return isProtected;
    }
}
