package com.example.chess_game;

import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.*;
import java.util.stream.Collectors;

public class Board {

    private final Field[][] fields;
    private static final int ASCII_OFFSET = 97;
    private final Map<Piece, Field> pieceLocation;
    private final List<String> moves;
    private final Image boardImage;
    private final GridPane playableBoard;
    private final Player white;
    private final Player black;
    private Field lastClickedField = null;

    public Board(Player white, Player black) {
        this.boardImage = new Image("graphics/ChessBoard.png");
        this.fields = new Field[8][8];
        this.pieceLocation = new HashMap<>();
        this.moves = new ArrayList<>();
        this.playableBoard = new GridPane();
        this.white = white;
        this.black = black;
        initFields();
    }

    public void initFields() {
        for (int rowNum = 0; rowNum < 8; rowNum++) {
            for (int colAlphabet = 0; colAlphabet < 8; colAlphabet++) {
                Field currentField
                        = new Field((char) (colAlphabet + ASCII_OFFSET) + Integer.toString(rowNum + 1), rowNum,
                        colAlphabet, (int) (boardImage.getHeight() / 8));

                this.fields[rowNum][colAlphabet] = currentField;
                switch (rowNum) {
                    case 0 -> initPieces(colAlphabet, Color.WHITE, currentField, white);
                    case 1 -> setPieceOnBoard(Pawn.class, Color.WHITE, currentField, white);
                    case 6 -> setPieceOnBoard(Pawn.class, Color.BLACK, currentField, black);
                    case 7 -> initPieces(colAlphabet, Color.BLACK, currentField, black);
                }
                playableBoard.add(currentField.getCell(), rowNum, colAlphabet, (int) boardImage.getHeight() / 8, (int) boardImage.getWidth() / 8);
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

        String promotionAnnotation = "";
        String moveAnnotation = piece.getMoveAnnotation(oldField, newField);

        if (piece instanceof Pawn) {
            // Check if move was an En Passant
            if (newField.getPiece() == null && oldField.getColumn() != newField.getColumn()) {
                Field capturedPawnField = fields[newField.getRow() + (piece.getColor() == Color.BLACK ? 1 : -1)][newField.getColumn()];
                if (capturedPawnField.getPiece() instanceof Pawn) {
                    pieceLocation.remove(capturedPawnField.getPiece());
                    fields[capturedPawnField.getRow()][capturedPawnField.getColumn()].setPiece(null);
                }
            }

            // Check if move was Promotion
            if (newField.getRow() == 0 || newField.getRow() == 7) {
                // TODO Get selection of chosen Promotion
                // Filler Queen promotion
                Queen k = new Queen(piece.getColor());
                if (piece.getColor() == Color.WHITE) {
                    white.promotePiece((Pawn) piece, k);
                } else {
                    black.promotePiece((Pawn) piece, k);
                }
                pieceLocation.remove(piece);
                pieceLocation.put(k, newField);

                promotionAnnotation = "=" + k.ANNOTATION_LETTER;
                piece = k;
            }
        }

        // Check if move was short or long castle
        if (moveAnnotation.equals("O-O") || moveAnnotation.equals("O-O-O")) {
            String rooksColumn;
            int colDiff;
            if (moveAnnotation.equals("O-O")) {
                rooksColumn = "h";
                colDiff = -1;
            } else {
                rooksColumn = "a";
                colDiff = 1;
            }

            Piece rook = getPieces(piece.getColor())
                    .stream()
                    .filter(playerPiece -> playerPiece instanceof Rook && pieceLocation.get(playerPiece).getFieldName().contains(rooksColumn))
                    .findFirst()
                    .get();
            Field rookCastleField = fields[newField.getRow()][newField.getColumn() + colDiff];
            pieceLocation.get(rook).setPiece(null);
            pieceLocation.put(rook, rookCastleField);
            rookCastleField.setPiece(rook);
        }

        fields[oldField.getRow()][oldField.getColumn()].setPiece(null);
        fields[newField.getRow()][newField.getColumn()].setPiece(piece);

        String checkAnnotation = "";
        if (isEnemyKingInCheck(piece.getColor())) {
            checkAnnotation = "+";
        }
        if (isCheckmate(piece.getColor())) {
            checkAnnotation = "#";
        }
        moves.add(moveAnnotation + promotionAnnotation + checkAnnotation);
        piece.increaseMoveCounter();
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
                if (fields[field1.getRow()][i].getPiece() != null) return true;
        } else if (colDiff == 0) { // Pieces are in the same column
            for (int i = Math.min(field1.getRow(), field2.getRow()) + 1; i < Math.max(field1.getRow(), field2.getRow()); i++)
                if (fields[i][field1.getColumn()].getPiece() != null) return true;
        } else if (rowDiff == colDiff) { // Pieces are diagonal to each other
            if (field1.getRow() < field2.getRow()) {
                if (field1.getColumn() < field2.getColumn()) {
                    for (int i = 1; (i + field1.getColumn()) < field2.getColumn(); i++)
                        if (fields[field1.getRow() + i][field1.getColumn() + i].getPiece() != null) return true;
                } else {
                    for (int i = 1; (field1.getColumn() - i) > field2.getColumn(); i++)
                        if (fields[field1.getRow() + i][field1.getColumn() - i].getPiece() != null) return true;
                }
            } else return isPieceBetweenFields(fields, field2, field1);
        }
        return false;
    }

    public void setLastClickedField(int row, int column) {
        lastClickedField = fields[row][column];
    }

    public void removeMoveIfSelfCheck(Set<Field> availableMoves, Piece piece) {
        Color enemyColor = piece.getColor() == Color.WHITE ? Color.BLACK : Color.WHITE;
        Field originalPieceField = pieceLocation.get(piece);
        availableMoves.removeIf(field -> {
            // Save field's current state
            // Move piece to supposedly valid field
            // Check if own King is in check
            // Restore board state

            Piece oldFieldsPiece = field.getPiece();
            pieceLocation.remove(oldFieldsPiece);
            pieceLocation.put(piece, field);
            fields[field.getRow()][field.getColumn()].setPiece(piece);
            fields[originalPieceField.getRow()][originalPieceField.getColumn()].setPiece(null);
            boolean isSelfCheck = isEnemyKingInCheck(enemyColor);

            if (oldFieldsPiece != null)
                pieceLocation.put(oldFieldsPiece, field);
            pieceLocation.put(piece, originalPieceField);
            fields[field.getRow()][field.getColumn()].setPiece(oldFieldsPiece);
            fields[originalPieceField.getRow()][originalPieceField.getColumn()].setPiece(piece);
            return isSelfCheck;
        });
    }

    public Set<Field> getDefensiveBlocksOrCaptures(Color color) {
        King king = getKing(color);
        Color enemyColor = color == Color.WHITE ? Color.BLACK : Color.WHITE;
        Set<Field> blockOrCaptureFields = new HashSet<>();

        Set<Piece> attackingPieces = getPieces(enemyColor)
                .stream()
                .filter(piece -> !(piece instanceof King))
                .filter(piece ->
                        piece.getValidMoves(this, pieceLocation.get(piece))
                                .stream()
                                .anyMatch(validFields ->
                                        validFields.getFieldName().equals(pieceLocation.get(king).getFieldName())))
                .collect(Collectors.toSet());

        if (attackingPieces.size() != 1) return blockOrCaptureFields;
        Piece attackingPiece = attackingPieces.stream().findFirst().get();
        Set<Field> fieldsToBlock = attackingPiece.getInBetweenFields(pieceLocation.get(attackingPiece), pieceLocation.get(king), fields);

        getPieces(color)
                .stream()
                .filter(piece -> !(piece instanceof King))
                .forEach(piece ->
                        piece.getValidMoves(this, pieceLocation.get(piece))
                                .forEach(validField -> {

                                    // Attacking piece can be captured
                                    if (validField.getFieldName().equals(pieceLocation.get(attackingPiece).getFieldName())) {
                                        blockOrCaptureFields.add(validField);
                                    }

                                    // Attacking piece can be blocked
                                    fieldsToBlock
                                            .forEach(field -> {
                                                if (field.getFieldName().equals(validField.getFieldName())) {
                                                    blockOrCaptureFields.add(field);
                                                }
                                            });
                                }));
        return blockOrCaptureFields;
    }

    public boolean isCheckmate(Color color) {
        Color enemyColor = color == Color.WHITE ? Color.BLACK : Color.WHITE;
        return getPieces(enemyColor)
                .stream()
                .allMatch(piece -> piece.getLegalMoves(this, pieceLocation.get(piece)).isEmpty());
    }
}
