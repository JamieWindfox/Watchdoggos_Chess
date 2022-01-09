package com.example.chess_game;

import java.util.HashSet;
import java.util.Set;

public class King extends Piece {

    public King(Color color) {
        super(color);
    }

    @Override
    public Set<Field> getValidMoves(Board board, Field currentField) {
        validMoves = new HashSet<>();
        Field[][] fields = board.getFields();
        for (int rowDiff = -1; rowDiff <= 1; rowDiff++) {
            for (int colDiff = -1; colDiff <= 1; colDiff++) {
                if (currentField.getRow() + rowDiff < 0 || currentField.getRow() + rowDiff > 7 ||
                        currentField.getColumn() + colDiff < 0 || currentField.getColumn() + colDiff > 7 ||
                        (rowDiff == 0 && colDiff == 0)) continue;
                Field field = fields[currentField.getRow() + rowDiff][currentField.getColumn() + colDiff];
                if (field.getPiece() == null || !isEnemyPieceProtected(board, field)) {
                    validateAndAddMove(field);
                }
                // todo Castling
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
        String newFieldName = newField.getFieldName();
        return newField.getPiece() != null ? "Kx" + newFieldName : "K" + newFieldName;
    }

    @Override
    public Set<Field> getInBetweenFields(Field startField, Field endField, Field[][] fields) {
        return new HashSet<>();
    }

    private boolean isEnemyPieceProtected(Board board, Field field) {
        Color enemyColor = field.getPiece().getColor();
        field.getPiece().setColor(this.getColor());
        boolean isProtected = false;
        for (Piece piece : board.getPieces(enemyColor)) {
            Field boardField = board.getPieceLocation(piece);
            if (piece instanceof King) {
                isProtected = Math.abs(boardField.getRow() - field.getRow()) == 1 &&
                        Math.abs(boardField.getColumn() - field.getColumn()) == 1;
            } else {
                isProtected = piece.getValidMoves(board, boardField)
                        .stream()
                        .anyMatch(validFields -> validFields.getFieldName().equals(field.getFieldName()));
            }
            if (isProtected) break;
        }

        field.getPiece().setColor(enemyColor);
        return isProtected;
    }
}
