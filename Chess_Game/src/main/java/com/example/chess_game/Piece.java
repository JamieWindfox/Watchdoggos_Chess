package com.example.chess_game;

import javafx.scene.image.Image;

import java.util.HashSet;
import java.util.Set;

public abstract class Piece {
    private Color color;
    private Image image;
    public Set<Field> validMoves;

    public Piece(Color color) //TODO: Image hinzufügen
    {
        this.color = color;
        this.validMoves = new HashSet<>();
        try {
            this.image = new Image("graphics/" + (color == Color.WHITE ? "white_" : "black_") + getClass().getSimpleName().toLowerCase() + ".png");
        } catch (Exception e) {

        }
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * A method to validate moves depending on whether they are moves with or without captures
     * and once validated those moves will be added to the set of current valid moves
     *
     * @param moveToField The field that the piece is moving to
     */
    public void validateAndAddMove(Field moveToField) {
        if (moveToField.getPiece() == null) {
            validMoves.add(moveToField);
        } else if (moveToField.getPiece() != null && this.color != moveToField.getPiece().getColor()) {
            validMoves.add(moveToField);
        }
    }

    /**
     * Method to check the boundaries of a field
     *
     * @param row    The given row coordinate
     * @param column The given column coordinate
     * @return true if row and column are within boundary - otherwise false
     */
    public static boolean areCoordinatesValid(int row, int column) {
        return row >= 0 && row <= 7 && column >= 0 && column <= 7;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Gets the valid moves of a piece with the addition of checking the king's safety
     *
     * @param board        The playing board
     * @param currentField The current field of the piece
     * @return The set of fields the piece is allowed to move to - if the king is in check
     */
    public Set<Field> getLegalMoves(Board board, Field currentField) {
        getValidMoves(board, currentField);
        if (board.isEnemyKingInCheck(color == Color.WHITE ? Color.BLACK : Color.WHITE) && !(this instanceof King)) {
            Set<Field> blockOrCaptureFields = board.getDefensiveBlocksOrCaptures(color);
            if (!blockOrCaptureFields.isEmpty()) {
                validMoves.removeIf(field ->
                        blockOrCaptureFields.stream()
                                .noneMatch(fieldToBlockOrCapture -> fieldToBlockOrCapture.getFieldName().equals(field.getFieldName()))
                );
            }
        }
        board.removeMoveIfSelfCheck(validMoves, this);
        return validMoves;
    }

    /**
     * Gets the valid moves of a piece without checking the king's safety
     *
     * @param board        The playing board
     * @param currentField The current field of the piece
     * @return The set of fields the piece is allowed to move to
     */
    public abstract Set<Field> getValidMoves(Board board, Field currentField);

    public abstract String getMoveAnnotation(Field oldField, Field newField);

    public abstract Set<Field> getInBetweenFields(Field startField, Field endField, Field[][] fields);

    public Image getImage() {
        return image;
    }
}
