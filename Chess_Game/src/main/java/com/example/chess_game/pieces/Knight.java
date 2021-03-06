package com.example.chess_game.pieces;

import com.example.chess_game.Board;
import com.example.chess_game.ChessColor;
import com.example.chess_game.Field;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class Knight extends Piece {

    public final String ANNOTATION_LETTER = "N";

    public Knight(ChessColor color) {
        super(color);
    }

    @Override
    public Set<Field> getValidMoves(Board board, Field currentField) {
        validMoves = new HashSet<>();
        Field[][] fields = board.getFields();

        Set<Pair<Integer, Integer>> possibleCoordinates = new HashSet<>();
        possibleCoordinates.add(new Pair<>(currentField.getRow() - 2, currentField.getColumn() - 1));
        possibleCoordinates.add(new Pair<>(currentField.getRow() - 2, currentField.getColumn() + 1));
        possibleCoordinates.add(new Pair<>(currentField.getRow() + 2, currentField.getColumn() - 1));
        possibleCoordinates.add(new Pair<>(currentField.getRow() + 2, currentField.getColumn() + 1));
        possibleCoordinates.add(new Pair<>(currentField.getRow() - 1, currentField.getColumn() - 2));
        possibleCoordinates.add(new Pair<>(currentField.getRow() + 1, currentField.getColumn() - 2));
        possibleCoordinates.add(new Pair<>(currentField.getRow() - 1, currentField.getColumn() + 2));
        possibleCoordinates.add(new Pair<>(currentField.getRow() + 1, currentField.getColumn() + 2));

        for (Pair<Integer, Integer> coordinate : possibleCoordinates) {
            if (areCoordinatesValid(coordinate.getKey(), coordinate.getValue())) {
                validateAndAddMove(fields[coordinate.getKey()][coordinate.getValue()]);
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
        return new HashSet<>();
    }
}
