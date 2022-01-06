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
                switch (rowNum) {
                    case 0 -> initPieces(colAlphabet, Piece.WHITE, currentField, white);
                    case 1 -> setPieceOnBoard(Pawn.class, Piece.WHITE, currentField, white);
                    case 6 -> setPieceOnBoard(Pawn.class, Piece.BLACK, currentField, black);
                    case 7 -> initPieces(colAlphabet, Piece.BLACK, currentField, black);
                }
            }
        }
    }

    private void initPieces(int col, int color, Field fieldToPlace, Player player) {
        switch (col) {
            case 0, 7 -> setPieceOnBoard(Rook.class, color, fieldToPlace, player);
            case 1, 6 -> setPieceOnBoard(Knight.class, color, fieldToPlace, player);
            case 2, 5 -> setPieceOnBoard(Bishop.class, color, fieldToPlace, player);
            case 3 -> setPieceOnBoard(Queen.class, color, fieldToPlace, player);
            case 4 -> setPieceOnBoard(King.class, color, fieldToPlace, player);
        }
    }

    private void setPieceOnBoard(Class<? extends Piece> pieceClass, int color, Field fieldToPlace, Player player) {
        Piece p = null;
        if (pieceClass == Pawn.class) {
            p = new Pawn(color);
        } else if (pieceClass == King.class) {
            p = new King(color);
        } else if (pieceClass == Queen.class) {
            p = new Queen(color);
        } else if (pieceClass == Rook.class) {
            p = new Rook(color);
        } else if (pieceClass == Knight.class) {
            p = new Knight(color);
        } else if (pieceClass == Bishop.class) {
            p = new Bishop(color);
        }

        fieldToPlace.setPiece(p);
        pieceLocation.put(p, fieldToPlace);
        player.addPiece(p);
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

        // Check if move was an En Passant
        if (piece instanceof Pawn && newField.getPiece() == null
                && oldField.getColumn() != newField.getColumn()) {
            Field capturedPawnField = fields[newField.getRow() + (piece.getColor() == Piece.BLACK ? 1 : -1)][newField.getColumn()];
            if (capturedPawnField.getPiece() instanceof Pawn) {
                pieceLocation.remove(capturedPawnField.getPiece());
                fields[capturedPawnField.getRow()][capturedPawnField.getColumn()].setPiece(null);
            }
        }

        moves.add(piece.getMoveAnnotation(oldField, newField));

        fields[oldField.getRow()][oldField.getColumn()].setPiece(null);
        fields[newField.getRow()][newField.getColumn()].setPiece(piece);
    }
}
