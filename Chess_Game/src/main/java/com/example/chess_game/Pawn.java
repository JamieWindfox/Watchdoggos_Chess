package com.example.chess_game;

import java.util.*;

public class Pawn extends Piece {

    public Pawn(int color) {
        super(color);
    }

    @Override
    public Set<Field> getValidMoves(Board board, Field currentField) {
        // todo promotion
        validMoves = new HashSet<>();
        Field[][] fields = board.getFields();
        int pieceColor = this.getColor();
        boolean atStart = (pieceColor == WHITE && currentField.getRow() == 1)
                || (pieceColor == BLACK && currentField.getRow() == 6);

        int moveCounter = pieceColor == WHITE ? 1 : -1;
        if (pieceColor == WHITE ? currentField.getColumn() > 0 : currentField.getColumn() < 7) {
            Field takePieceLeft = fields[currentField.getRow() + moveCounter][currentField.getColumn() - moveCounter];
            validateAndAddMove(takePieceLeft, true);
        }
        if (pieceColor == WHITE ? currentField.getColumn() < 7 : currentField.getColumn() > 0) {
            Field takePieceRight = fields[currentField.getRow() + moveCounter][currentField.getColumn() + moveCounter];
            validateAndAddMove(takePieceRight, true);
        }

        Field oneFieldForward = fields[currentField.getRow() + moveCounter][currentField.getColumn()];
        validateAndAddMove(oneFieldForward, false);

        if (oneFieldForward.getPiece() == null && atStart) {
            moveCounter = pieceColor == WHITE ? moveCounter + 1 : moveCounter - 1;
            oneFieldForward = fields[currentField.getRow() + moveCounter][currentField.getColumn()];
            validateAndAddMove(oneFieldForward, false);
        }

        // En passant rule
        List<String> pastMoves = board.getMoves();
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

    // Jamie, 05.01.22: Could be implemented in superclass
    @Override
    public String getMoveAnnotation(Field oldField, Field newField) {
        return newField.getPiece() != null || (newField.getPiece() == null && oldField.getColumn() != newField.getColumn())
                ? oldField.getFieldName().charAt(0) + "x" + newField.getFieldName() : newField.getFieldName();
    }
}


