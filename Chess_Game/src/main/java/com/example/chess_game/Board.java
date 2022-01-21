package com.example.chess_game;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private final List<Position> boardPositions;
    private Field lastClickedField = null;

    public Board(Player white, Player black) {
        this.boardImage = new Image("graphics/ChessBoard.png");
        this.fields = new Field[8][8];
        this.pieceLocation = new HashMap<>();
        this.moves = new ArrayList<>();
        this.playableBoard = new GridPane();
        this.white = white;
        this.black = black;
        this.boardPositions = new ArrayList<>();
        initFields();
    }

    /**
     * Initializes all cells/fields on the chess board and calls the methods to set the pieces in their start formation
     */
    public void initFields() {
        for (int rowNum = 0; rowNum < 8; rowNum++) {
            for (int colAlphabet = 0; colAlphabet < 8; colAlphabet++) {
                Field currentField
                        = new Field((char) (colAlphabet + ASCII_OFFSET) + Integer.toString(rowNum + 1), rowNum,
                        colAlphabet, (int) (boardImage.getHeight() / 8));

                this.fields[rowNum][colAlphabet] = currentField;
                switch (rowNum) {
                    case 0 -> initPieces(colAlphabet, ChessColor.WHITE, currentField, white);
                    case 1 -> setPieceOnBoard(Pawn.class, ChessColor.WHITE, currentField, white);
                    case 6 -> setPieceOnBoard(Pawn.class, ChessColor.BLACK, currentField, black);
                    case 7 -> initPieces(colAlphabet, ChessColor.BLACK, currentField, black);
                }
                playableBoard.add(currentField.getCell(), rowNum, colAlphabet, (int) boardImage.getHeight() / 8, (int) boardImage.getWidth() / 8);
            }
        }
    }

    /**
     * Sets the pieces to their start field
     *
     * @param column       The column for which the pieces should be set
     * @param color        The color of the team of which the piece is set
     * @param fieldToPlace The field where the piece should be set
     * @param player       A reference to the player of the given color
     */
    private void initPieces(int column, ChessColor color, Field fieldToPlace, Player player) {
        switch (column) {
            case 0, 7 -> setPieceOnBoard(Rook.class, color, fieldToPlace, player);
            case 1, 6 -> setPieceOnBoard(Knight.class, color, fieldToPlace, player);
            case 2, 5 -> setPieceOnBoard(Bishop.class, color, fieldToPlace, player);
            case 3 -> setPieceOnBoard(Queen.class, color, fieldToPlace, player);
            case 4 -> setPieceOnBoard(King.class, color, fieldToPlace, player);
        }
    }

    /**
     * Method to set a single piece on a given field
     *
     * @param pieceClass   The class of the piece that should be set to the given field
     * @param color        The color of the piece that should be set to the given field
     * @param fieldToPlace The field where the given piece should be set
     * @param player       The player of the given color
     */
    private void setPieceOnBoard(Class<? extends Piece> pieceClass, ChessColor color, Field fieldToPlace, Player player) {
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

    /**
     * Getter for fields
     *
     * @return all the fields on the board as a 2-dimensional array
     */
    public Field[][] getFields() {
        return this.fields;
    }

    /**
     * @param piece The piece for which the location should be returned
     * @return The location on the field for the given piece
     */
    public Field getPieceLocation(Piece piece) {
        return pieceLocation.get(piece);
    }

    /**
     * @param color The color of the pieces that should be returned
     * @return All pieces of the given color that are still on the board
     */
    public Set<Piece> getPieces(ChessColor color) {
        return pieceLocation.keySet()
                .stream()
                .filter(piece -> piece.getColor() == color)
                .collect(Collectors.toSet());
    }

    /**
     * Getter for moves
     *
     * @return a list of all previous moves as strings in chess notation
     */
    public List<String> getMoves() {
        return this.moves;
    }

    /**
     * Prints the fields of the board in the console; kept for debugging reasons
     */
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

    /**
     * Moves the given piece to the given field and updates the board accordingly
     *
     * @param newField       The new location of the given piece
     * @param piece          The piece that is set to the given field
     * @param gridpane_board
     * @return true if the king of the other player was set checkmate, false otherwise
     */
    public boolean update(Field newField, Piece piece, GridPane gridpane_board, Map<Piece, ImageView> pieceImageViews) {
        Field oldField = pieceLocation.remove(piece);
        pieceLocation.put(piece, newField);

        String promotionAnnotation = "";
        String moveAnnotation = piece.getMoveAnnotation(oldField, newField);

        // Check if move was capture
        if (newField.getPiece() != null) {
            pieceLocation.remove(newField.getPiece());
        }

        if (piece instanceof Pawn) {
            // Check if move was an En Passant
            if (newField.getPiece() == null && oldField.getColumn() != newField.getColumn()) {
                Field capturedPawnField = fields[newField.getRow() + (piece.getColor() == ChessColor.BLACK ? 1 : -1)][newField.getColumn()];
                if (capturedPawnField.getPiece() instanceof Pawn) {
                    Piece capturedPawn = capturedPawnField.getPiece();
                    pieceLocation.remove(capturedPawn);
                    fields[capturedPawnField.getRow()][capturedPawnField.getColumn()].setPiece(null);
                    gridpane_board.getChildren().remove(pieceImageViews.get(capturedPawn));
                    pieceImageViews.remove(capturedPawn);
                }
            }

            // Check if move was Promotion
            if (newField.getRow() == 0 || newField.getRow() == 7) {
                Piece promoPiece = showPromotionDialog(piece.getColor());
                if (piece.getColor() == ChessColor.WHITE) {
                    white.promotePiece((Pawn) piece, promoPiece);
                } else {
                    black.promotePiece((Pawn) piece, promoPiece);
                }
                pieceLocation.remove(piece);
                pieceLocation.put(promoPiece, newField);

                ((Pawn) piece).setPromoted(true);
                gridpane_board.getChildren().remove(pieceImageViews.get(piece));
                pieceImageViews.remove(piece);
                ImageView newImgView = new ImageView(promoPiece.getImage());

                gridpane_board.add(newImgView, newField.getColumn(), 7 - newField.getRow());
                pieceImageViews.put(promoPiece, newImgView);

                promotionAnnotation = "=" + promoPiece.getAnnotationLetter();
                piece = promoPiece;
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

            gridpane_board.getChildren().remove(pieceImageViews.get(rook));
            gridpane_board.add(pieceImageViews.get(rook), rookCastleField.getColumn(), 7 - rookCastleField.getRow());
        }

        fields[oldField.getRow()][oldField.getColumn()].setPiece(null);
        fields[newField.getRow()][newField.getColumn()].setPiece(piece);

        String checkAnnotation = "";
        if (isEnemyKingInCheck(piece.getColor())) {
            checkAnnotation = "+";
        }
        boolean checkmate = false;
        if (isCheckmate(piece.getColor())) {
            checkAnnotation = "#";
            checkmate = true;
        }
        moves.add(moveAnnotation + promotionAnnotation + checkAnnotation);
        piece.increaseMoveCounter();
        boardPositions.add(new Position(Map.copyOf(pieceLocation), piece.getColor() == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE));
        return checkmate;
    }

    /**
     * @param color The color which the returned king has
     * @return the king of the given color
     */
    public King getKing(ChessColor color) {
        return (King) pieceLocation.keySet()
                .stream()
                .filter(piece -> piece instanceof King && piece.getColor() == color)
                .findFirst()
                .get();
    }

    /**
     * Checks if the other king is in check
     *
     * @param attackerColor The color of the attacker
     * @return true if the other king is in check, false otherwise
     */
    public boolean isEnemyKingInCheck(ChessColor attackerColor) {
        ChessColor enemyColor = attackerColor == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;
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

    /**
     * Setter for lastClickedField
     *
     * @param row    the row of the field that was clicked last
     * @param column the column of the field that was clicked last
     */
    public void setLastClickedField(int row, int column) {
        lastClickedField = fields[row][column];
    }

    /**
     * Save field's current state
     * Move piece to supposedly valid field
     * Check if own King is in check
     * Restore board state
     * @param availableMoves
     * @param piece
     */

    public void removeMoveIfSelfCheck(Set<Field> availableMoves, Piece piece) {
        ChessColor enemyColor = piece.getColor() == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;
        Field originalPieceField = pieceLocation.get(piece);
        availableMoves.removeIf(field -> {
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

    public Set<Field> getDefensiveBlocksOrCaptures(ChessColor color) {
        King king = getKing(color);
        ChessColor enemyColor = color == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;
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

    /**
     * Checks if the king of the given color is checkmate
     *
     * @param color The color of the king for which the check is made
     * @return true if the king is checkmate, false otherwise
     */
    public boolean isCheckmate(ChessColor color) {
        ChessColor enemyColor = color == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;
        boolean checkmate = getPieces(enemyColor).stream()
                .allMatch(piece -> piece.getLegalMoves(this, pieceLocation.get(piece)).isEmpty());
        if (checkmate) {
            System.out.println("The " + color + " king is checkmate.");
        }
        return checkmate;
    }

    public List<Position> getBoardPositions() {
        return boardPositions;
    }

    public Piece showPromotionDialog(ChessColor requestPieceColor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("promotion-dialog.fxml"));
            Parent root = loader.load();
            PromotionDialog dialog = loader.getController();
            dialog.setPieceColor(requestPieceColor);
            dialog.showDialog(root);
            return dialog.getPiece();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
