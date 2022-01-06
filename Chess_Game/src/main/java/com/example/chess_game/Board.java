package com.example.chess_game;

import javafx.scene.image.Image;

import java.util.*;
import java.util.stream.Collectors;

public class Board {

    private final Field[][] fields;
    private static final int ASCII_OFFSET = 97;
    private final Map<Piece, Field> pieceLocation;
    private final List<String> moves;
    private final Image boardImage;

    public Board(Player white, Player black) {
        this.fields = new Field[8][8];
        this.pieceLocation = new HashMap<>();
        this.moves = new ArrayList<>();
        this.boardImage = new Image("graphics/ChessBoard.png");
        initFields(white, black);
    }

    public void initFields(Player white, Player black) {
        for (int rowNum = 0; rowNum < 8; rowNum++) {
            for (int colAlphabet = 0; colAlphabet < 8; colAlphabet++) {

                Field currentField
                        = new Field((char) (colAlphabet + ASCII_OFFSET) + Integer.toString(rowNum + 1), rowNum, colAlphabet);

                this.fields[rowNum][colAlphabet] = currentField;
                switch (rowNum) {
                    case 0 -> initPieces(colAlphabet, Color.WHITE, currentField, white);
                    case 1 -> setPieceOnBoard(Pawn.class, Color.WHITE, currentField, white);
                    case 6 -> setPieceOnBoard(Pawn.class, Color.BLACK, currentField, black);
                    case 7 -> initPieces(colAlphabet, Color.BLACK, currentField, black);
                }
            }
        }
    }

    private void initPieces(int col, Color color, Field fieldToPlace, Player player) {
        switch (col) {
            case 0, 7 -> setPieceOnBoard(Rook.class, color, fieldToPlace, player);
            case 1, 6 -> setPieceOnBoard(Knight.class, color, fieldToPlace, player);
            case 2, 5 -> setPieceOnBoard(Bishop.class, color, fieldToPlace, player);
            case 3 -> setPieceOnBoard(Queen.class, color, fieldToPlace, player);
            case 4 -> setPieceOnBoard(King.class, color, fieldToPlace, player);
        }
    }

    private void setPieceOnBoard(Class<? extends Piece> pieceClass, Color color, Field fieldToPlace, Player player) {
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

    public Set<Piece> getPieces(Color color) {
        return pieceLocation.keySet()
                .stream()
                .filter(piece -> piece.getColor() == color)
                .collect(Collectors.toSet());
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
            Field capturedPawnField = fields[newField.getRow() + (piece.getColor() == Color.BLACK ? 1 : -1)][newField.getColumn()];
            if (capturedPawnField.getPiece() instanceof Pawn) {
                pieceLocation.remove(capturedPawnField.getPiece());
                fields[capturedPawnField.getRow()][capturedPawnField.getColumn()].setPiece(null);
            }
        }

        String moveAnnotation = piece.getMoveAnnotation(oldField, newField);
        fields[oldField.getRow()][oldField.getColumn()].setPiece(null);
        fields[newField.getRow()][newField.getColumn()].setPiece(piece);

        String checkAnnotation = "";
        if (isEnemyKingInCheck(piece.getColor())) {
            checkAnnotation = "+";
        }
        moves.add(moveAnnotation + checkAnnotation);
    }

    public Image getBoardImage() {
        return boardImage;
    }

    public King getKing(Color color) {
        return (King) pieceLocation.keySet()
                .stream()
                .filter(piece -> piece instanceof King && piece.getColor() == color)
                .findFirst()
                .get();
    }

    public boolean isEnemyKingInCheck(Color attackerColor) {
        Color enemyColor = attackerColor == Color.WHITE ? Color.BLACK : Color.WHITE;
        return getPieces(attackerColor)
                .stream()
                .filter(piece -> !(piece instanceof King))
                .anyMatch(piece ->
                        piece.getValidMoves(this, pieceLocation.get(piece))
                                .stream()
                                .anyMatch(validFields ->
                                        validFields.getFieldName().equals(pieceLocation.get(getKing(enemyColor)).getFieldName())));
    }

    /**
     * Determines if pieces are located between two given fields.
     *
     * @param fields The fields array of the game board
     * @param field1 Field 1 - Order does not matter
     * @param field2 Field 2 - Order does not matter
     * @return True if at least one piece is located between the fields. Pieces on each field are not evaluated.
     */
    public boolean isPieceBetweenFields(Field[][] fields, Field field1, Field field2) {
        int rowDiff = Math.max(field1.getRow(), field2.getRow()) - Math.min(field1.getRow(), field2.getRow());
        int colDiff = Math.max(field1.getColumn(), field2.getColumn()) - Math.min(field1.getColumn(), field2.getColumn());
        if (rowDiff == 0) { // Pieces are in the same row
            for (int i = Math.min(field1.getColumn(), field2.getColumn()) + 1; i < Math.max(field1.getColumn(), field2.getColumn()); i++)
                if(fields[field1.getRow()][i].getPiece() != null) return true;
        } else if (colDiff == 0) { // Pieces are in the same column
            for (int i = Math.min(field1.getRow(), field2.getRow()) + 1; i < Math.max(field1.getRow(), field2.getRow()); i++)
                if(fields[i][field1.getColumn()] != null) return true;
        } else if (rowDiff == colDiff) { // Pieces are diagonal to each other
            if (field1.getRow() < field2.getRow()) {
                if (field1.getColumn() < field2.getColumn()) {
                    for(int i = 1; (i + field1.getColumn()) < field2.getColumn(); i++)
                        if(fields[field1.getRow() + i][field1.getColumn() + i] != null) return true;
                } else {
                    for(int i = 1; (field1.getColumn() - i) > field2.getColumn(); i++)
                        if(fields[field1.getRow() + i][field1.getColumn() - i] != null) return true;
                }
            } else return isPieceBetweenFields(fields, field2, field1);
        }
        return false;
    }
}
