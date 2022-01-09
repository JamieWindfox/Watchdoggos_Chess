package com.example.chess_game;

import java.util.*;

public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color);
    }

    @Override
    public Set<Field> getValidMoves(Board board, Field currentField) {
        validMoves = new HashSet<>();
        Field[][] fields = board.getFields();
        Color pieceColor = this.getColor();
        boolean atStart = (pieceColor == Color.WHITE && currentField.getRow() == 1)
                || (pieceColor == Color.BLACK && currentField.getRow() == 6);

        int moveCounter = pieceColor == Color.WHITE ? 1 : -1;
        if (pieceColor == Color.WHITE ? currentField.getColumn() > 0 : currentField.getColumn() < 7) {
            Field takePieceLeft = fields[currentField.getRow() + moveCounter][currentField.getColumn() - moveCounter];
            if (takePieceLeft.getPiece() != null) validateAndAddMove(takePieceLeft);
        }
        if (pieceColor == Color.WHITE ? currentField.getColumn() < 7 : currentField.getColumn() > 0) {
            Field takePieceRight = fields[currentField.getRow() + moveCounter][currentField.getColumn() + moveCounter];
            if (takePieceRight.getPiece() != null) validateAndAddMove(takePieceRight);
        }

        Field oneFieldForward = fields[currentField.getRow() + moveCounter][currentField.getColumn()];
        if (oneFieldForward.getPiece() == null) validateAndAddMove(oneFieldForward);

        if (oneFieldForward.getPiece() == null && atStart) {
            moveCounter = pieceColor == Color.WHITE ? moveCounter + 1 : moveCounter - 1;
            oneFieldForward = fields[currentField.getRow() + moveCounter][currentField.getColumn()];
            if (oneFieldForward.getPiece() == null) validateAndAddMove(oneFieldForward);
        }

        // En passant rule
        List<String> pastMoves = board.getMoves();
        if ((currentField.getRow() == 3 && pieceColor == Color.BLACK || currentField.getRow() == 4 && pieceColor == Color.WHITE)
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

    // Jamie, 05.01.22: Could be implemented in superclass
    @Override
    public String getMoveAnnotation(Field oldField, Field newField) {
        return newField.getPiece() != null || oldField.getColumn() != newField.getColumn()
                ? oldField.getFieldName().charAt(0) + "x" + newField.getFieldName() : newField.getFieldName();
    }

    @Override
    public Set<Field> getInBetweenFields(Field startField, Field endField, Field[][] fields) {
        return new HashSet<>();
    }
}


