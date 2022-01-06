package com.example.chess_game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Knight extends Piece {

    public Knight(int paraColor) {
        super(paraColor);
    }

    @Override
    public Set<Field> getValidMoves(Field[][] fields, Field currentField, List<String> pastMoves) {
        validMoves = new HashSet<>();
        // todo implement knight moves
        return validMoves;
    }

    @Override
    public String getMoveAnnotation(Field oldField, Field newField) {
        return "N" + newField.getFieldName();
    }
}
