package com.example.chess_game;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class King extends Piece {

    public final String ANNOTATION_LETTER = "K";

    public King(ChessColor color) {
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
            }
        }
        if (moveCounter == 0) {
            Set<Piece> rooks = board.getPieces(getColor())
                    .stream()
                    .filter(piece -> piece instanceof Rook && piece.moveCounter == 0)
                    .collect(Collectors.toSet());
            if (!rooks.isEmpty()) {
                Field kingsField = board.getPieceLocation(this);
                for (Piece rook : rooks) {
                    Field rooksField = board.getPieceLocation(rook);
                    Set<Field> inBetweenFields = rook.getInBetweenFields(rooksField, kingsField, board.getFields());
                    int inBetweenFieldsCount = inBetweenFields.size();
                    boolean areFieldsBetweenEmpty = inBetweenFields.stream()
                            .filter(inBetweenField -> inBetweenField.getPiece() == null)
                            .count() == inBetweenFieldsCount;
                    if (areFieldsBetweenEmpty && rooksField.getColumn() != kingsField.getColumn()) {
                        board.removeMoveIfSelfCheck(inBetweenFields, this);
                        if (rooksField.getColumn() > kingsField.getColumn()) {
                            // Short castle
                            if (inBetweenFields.size() == inBetweenFieldsCount) {
                                Field shortCastleField = fields[kingsField.getRow()][kingsField.getColumn() + 2];
                                validateAndAddMove(shortCastleField);
                            }
                        } else if (rooksField.getColumn() < kingsField.getColumn()) {
                            // Long castle
                            boolean checkAtFileB = inBetweenFields.stream()
                                    .noneMatch(inbtwFields -> inbtwFields.getFieldName().contains("b"));
                            if (inBetweenFields.size() == inBetweenFieldsCount ||
                                    (inBetweenFields.size() < inBetweenFieldsCount && checkAtFileB)) {
                                Field longCastleField = fields[kingsField.getRow()][kingsField.getColumn() - 2];
                                validateAndAddMove(longCastleField);
                            }
                        }
                    }


                }

            }
        }
        return validMoves;
    }

    @Override
    public String getMoveAnnotation(Field oldField, Field newField) {
        if (newField.getColumn() - oldField.getColumn() == 2) {
            return "O-O";
        } else if (newField.getColumn() - oldField.getColumn() == -2) {
            return "O-O-O";
        }
        return newField.getPiece() != null ? ANNOTATION_LETTER + "x" + newField.getFieldName() : ANNOTATION_LETTER + newField.getFieldName();
    }

    @Override
    public String getAnnotationLetter() {
        return ANNOTATION_LETTER;
    }

    @Override
    public Set<Field> getInBetweenFields(Field startField, Field endField, Field[][] fields) {
        return new HashSet<>();
    }

    private boolean isEnemyPieceProtected(Board board, Field field) {
        ChessColor enemyColor = field.getPiece().getColor();
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
