package com.example.chess_game;

import javafx.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Knight extends Piece {

    public Knight(int color) {
        super(color);
    }

    @Override
    public Set<Field> getValidMoves(Field[][] fields, Field currentField, List<String> pastMoves) {
        validMoves = new HashSet<>();

        Set<Pair<Integer, Integer>> possibleCoordinates = new HashSet<>();
        possibleCoordinates.add(new Pair<Integer, Integer>(currentField.getRow()-2, currentField.getColumn()-1));
        possibleCoordinates.add(new Pair<Integer, Integer>(currentField.getRow()-2, currentField.getColumn()+1));
        possibleCoordinates.add(new Pair<Integer, Integer>(currentField.getRow()+2, currentField.getColumn()-1));
        possibleCoordinates.add(new Pair<Integer, Integer>(currentField.getRow()+2, currentField.getColumn()+1));
        possibleCoordinates.add(new Pair<Integer, Integer>(currentField.getRow()-1, currentField.getColumn()-2));
        possibleCoordinates.add(new Pair<Integer, Integer>(currentField.getRow()+1, currentField.getColumn()-2));
        possibleCoordinates.add(new Pair<Integer, Integer>(currentField.getRow()-1, currentField.getColumn()+2));
        possibleCoordinates.add(new Pair<Integer, Integer>(currentField.getRow()+1, currentField.getColumn()+2));

        for (Pair<Integer, Integer> coordinate : possibleCoordinates) {
            if(areCoordinatesValid(coordinate.getKey(), coordinate.getValue())) {
                evaluateField(fields[coordinate.getKey()][coordinate.getValue()], false);
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
