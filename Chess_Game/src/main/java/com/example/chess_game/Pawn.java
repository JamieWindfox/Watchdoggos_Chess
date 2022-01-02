package com.example.chess_game;

import java.util.HashSet;
import java.util.Set;

public class Pawn extends Piece {

    private Set<Field> validMoves;

    public Pawn(int color) {
        super(color);
    }

    @Override
    public Set<Field> getValidMoves(Field[][] fields, Field currentField) {
        // TODO check boundaries
        validMoves = new HashSet<>();
        int pieceColor = this.getColor();
        boolean atStart = (pieceColor == WHITE && currentField.getRow() == 1)
                || (pieceColor == BLACK && currentField.getRow() == 6);

        int moveCounter = pieceColor == WHITE ? 1 : -1;
        Field oneFieldForward = fields[currentField.getRow() + moveCounter][currentField.getColumn()];
        Field takePieceLeft = fields[currentField.getRow() + moveCounter][currentField.getColumn() - moveCounter];
        Field takePieceRight = fields[currentField.getRow() + moveCounter][currentField.getColumn() + moveCounter];

        evaluateField(oneFieldForward, true);
        evaluateField(takePieceLeft, false);
        evaluateField(takePieceRight, false);


        if (isFieldEmpty(oneFieldForward) && atStart) {
            moveCounter = pieceColor == WHITE ? moveCounter + 1 : moveCounter - 1;
            oneFieldForward = fields[currentField.getRow() + moveCounter][currentField.getColumn()];
            evaluateField(oneFieldForward, true);
        }

        return validMoves;
    }

    @Override
    public String getMoveAnnotation(Field oldField, Field newField, boolean isCapture) {
        return isCapture ? oldField.getFieldName().charAt(0) + "x" + newField.getFieldName() : newField.getFieldName();
    }

    private boolean isFieldEmpty(Field f) {
        return f.getPiece() == null;
    }

    private void evaluateField(Field field, boolean shouldFieldBeEmpty) {
        if (shouldFieldBeEmpty && isFieldEmpty(field)) {
            validMoves.add(field);
        } else if (!shouldFieldBeEmpty && !isFieldEmpty(field) && this.getColor() != field.getPiece().getColor()) {
            validMoves.add(field);
        }
    }
}


