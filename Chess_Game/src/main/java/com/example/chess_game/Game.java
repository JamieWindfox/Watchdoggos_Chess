package com.example.chess_game;

import java.util.Set;

public class Game {
    private final Player black;
    private final Player white;
    private final Board board;

    public Game() {
        this.black = new Player(Piece.BLACK, "PlayerBlack");
        this.white = new Player(Piece.WHITE, "PlayerWhite");
        this.board = new Board(white, black);
        this.testGame();
    }

    /**
     * For testing purposes
     * See examples below to make further moves
     * - Get a piece from the board
     * - Get the valid moves from that piece
     * - Choose a valid move's field name e.g. "e4" that the piece should move to
     * - Update the board
     * - Print the board
     */
    public void testGame() {
        board.printField();

        // White's turn
        Piece piece = this.board.getFields()[1][3].getPiece();
        Set<Field> validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid pawn moves for white Turn 1: " + validMoves.size());
        board.update(moveToField(validMoves, "d4"), piece);
        board.printField();

        // Black's turn
        piece = this.board.getFields()[6][4].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid pawn moves for black Turn 1: " + validMoves.size());
        board.update(moveToField(validMoves, "e5"), piece);
        board.printField();

        // White's turn
        piece = this.board.getFields()[3][3].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid pawn moves for white Turn 2: " + validMoves.size());
        board.update(moveToField(validMoves, "e5"), piece);
        board.printField();

        System.out.println("Move history: " + this.board.getMoves());

        // Black's turn
        piece = this.board.getFields()[6][5].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid pawn moves for black Turn 2: " + validMoves.size());
        board.update(moveToField(validMoves, "f5"), piece);
        board.printField();

        // White's turn
        piece = this.board.getFields()[4][4].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid pawn moves for white Turn 3: " + validMoves.size());
        board.update(moveToField(validMoves, "f6"), piece);
        board.printField();

        // Black's turn
        piece = this.board.getFields()[7][4].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid king moves for black Turn 3: " + validMoves.size());
        board.update(moveToField(validMoves, "f7"), piece);
        board.printField();

        // White's turn
        piece = this.board.getFields()[1][6].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid pawn moves for white Turn 4: " + validMoves.size());
        board.update(moveToField(validMoves, "g4"), piece);
        board.printField();

        // Black's turn
        piece = this.board.getFields()[6][5].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid king moves for black Turn 4: " + validMoves.size());
        board.update(moveToField(validMoves, "e6"), piece);
        board.printField();

        // White's turn
        piece = this.board.getFields()[3][6].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid pawn moves for white Turn 5: " + validMoves.size());
        board.update(moveToField(validMoves, "g5"), piece);
        board.printField();

        // Black's turn
        piece = this.board.getFields()[5][4].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid king moves for black Turn 5: " + validMoves.size());
        board.update(moveToField(validMoves, "f5"), piece);
        board.printField();

        // White's turn
        piece = this.board.getFields()[0][4].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid king moves for white Turn 6: " + validMoves.size());
        board.update(moveToField(validMoves, "d2"), piece);
        board.printField();

        // Black's turn
        /*piece = this.board.getFields()[4][5].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid king moves for black Turn 6: " + validMoves.size());

        for (Field field : validMoves) {
            System.out.println("Valid moves: " + field.getFieldName());
        }*/

        // Black's turn
        piece = this.board.getFields()[7][3].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid Queen moves for black Turn 6: " + validMoves.size());
        board.update(moveToField(validMoves, "f6"), piece);
        board.printField();

        // White's turn
        piece = this.board.getFields()[1][0].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid Pawn moves for white Turn 7: " + validMoves.size());
        board.update(moveToField(validMoves, "a4"), piece);
        board.printField();

        // Black's turn
        piece = this.board.getFields()[7][5].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid Bishop moves for black Turn 7: " + validMoves.size());
        board.update(moveToField(validMoves, "a3"), piece);
        board.printField();

        // White's turn
        piece = this.board.getFields()[0][0].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece), board.getMoves());
        System.out.println("Valid Rook moves for white Turn 8: " + validMoves.size());
        board.update(moveToField(validMoves, "a3"), piece);
        board.printField();

        for (Field field : validMoves) {
            System.out.println("Valid moves: " + field.getFieldName());
        }

        System.out.println("Move history: " + this.board.getMoves());
    }

    /**
     * For testing purposes
     *
     * @param validMoves The available set of valid fields
     * @param name       The name of the field e.g. e4
     * @return Field the piece is being moved to
     */
    private Field moveToField(Set<Field> validMoves, String name) {
        return validMoves.stream()
                .filter(field -> field.getFieldName().equals(name))
                .findFirst()
                .orElseThrow();
    }
}
