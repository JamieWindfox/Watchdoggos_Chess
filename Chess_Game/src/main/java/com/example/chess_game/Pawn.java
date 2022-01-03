package com.example.chess_game;

import java.util.*;

public class Pawn extends Piece {

    public Pawn(int color) {
        super(color);
    }

    @Override
    public Set<Field> getValidMoves(Field[][] fields, Field currentField, List<String> pastMoves) {
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

        // En passant rule
        if ((currentField.getRow() == 3 && pieceColor == BLACK || currentField.getRow() == 4 && pieceColor == WHITE)
                && pastMoves.size() > 1) {
            String pastMove = pastMoves.get(pastMoves.size() - 1);
            Optional<Field> enemyPawnField = Arrays.stream(fields[currentField.getRow()])
                    .filter(fieldColumns -> fieldColumns.getFieldName().equals(pastMove)
                            && (fieldColumns.getRow() == 3 || fieldColumns.getRow() == 4)
                            && fieldColumns.getFieldName().length() == 2)
                    .findFirst();

            if (enemyPawnField.isPresent() && (enemyPawnField.get().getColumn() > currentField.getColumn() || enemyPawnField.get().getColumn() < currentField.getColumn())) {
                Field enPassantField = fields[enemyPawnField.get().getRow() + moveCounter][enemyPawnField.get().getColumn()];
                if (enPassantField.getPiece() == null) {
                    validMoves.add(enPassantField);
                }
            }
        }

        return validMoves;
    }

    @Override
    public String getMoveAnnotation(Field oldField, Field newField) {
        return newField.getPiece() != null || (newField.getPiece() == null && oldField.getColumn() != newField.getColumn())
                ? oldField.getFieldName().charAt(0) + "x" + newField.getFieldName() : newField.getFieldName();
    }
}


