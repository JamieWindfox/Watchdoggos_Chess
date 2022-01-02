package com.example.chess_game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private final Field[][] fields;
    private static final int ASCII_OFFSET = 97;
    private final Map<Piece, Field> pieceLocation;
    private final List<String> moves;

    public Board(Player white, Player black) {
        this.fields = new Field[8][8];
        this.pieceLocation = new HashMap<>();
        this.moves = new ArrayList<>();
        initFields(white, black);
    }

    public void initFields(Player white, Player black) {
        for (int rowNum = 0; rowNum < 8; rowNum++) {
            for (int colAlphabet = 0; colAlphabet < 8; colAlphabet++) {

                Field currentField
                        = new Field((char) (colAlphabet + ASCII_OFFSET) + Integer.toString(rowNum + 1), rowNum, colAlphabet);

                this.fields[rowNum][colAlphabet] = currentField;
                if (rowNum == 6) {
                    setPieceOnBoard(Pawn.class, Piece.BLACK, currentField, black);
                }
                if (rowNum == 1) {
                    setPieceOnBoard(Pawn.class, Piece.WHITE, currentField, white);
                }
            }
        }
    }

    private void setPieceOnBoard(Class<? extends Piece> piece, int color, Field fieldToPlace, Player player) {
        if (piece == Pawn.class) {
            Pawn p = new Pawn(color);
            fieldToPlace.setPiece(p);
            pieceLocation.put(p, fieldToPlace);
            player.addPiece(p);
        }
    }

    public Field[][] getFields() {
        return this.fields;
    }

    public Field getPieceLocation(Piece p) {
        return pieceLocation.get(p);
    }

    public List<String> getMoves() {
        return this.moves;
    }

    public void printField() {
        for (int rowNum = 0; rowNum < 8; rowNum++) {
            for (int colAlphabet = 0; colAlphabet < 8; colAlphabet++) {
                Field currentField = this.fields[7 - rowNum][colAlphabet];
                System.out.printf("[%s] ", currentField.getPiece() != null ?
                        currentField.getPiece().getName() + "_" + currentField.getPiece().getColor()
                        : currentField.getFieldName());
            }
            System.out.println();
        }
        System.out.println();
    }

    public void update(Field newField, Piece piece) {
        Field oldField = pieceLocation.remove(piece);
        pieceLocation.put(piece, newField);

        moves.add(piece.getMoveAnnotation(oldField, newField, newField.getPiece() != null));

        fields[oldField.getRow()][oldField.getColumn()].setPiece(null);
        fields[newField.getRow()][newField.getColumn()].setPiece(piece);
    }
}
