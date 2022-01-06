package com.example.chess_game;

import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class Knight extends Piece {

    public Knight(Color color) {
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
        String newFieldName = newField.getFieldName();
        return newField.getPiece() != null ? "Nx" + newFieldName : "N" + newFieldName;
    }
}
