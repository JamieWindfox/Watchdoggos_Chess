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
        Set<Field> validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece));
        System.out.println("Valid pawn moves for white Turn 1: " + validMoves.size());
        board.update(moveToField(validMoves, "d4"), piece);
        board.printField();

        // Black's turn
        piece = this.board.getFields()[6][4].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece));
        System.out.println("Valid pawn moves for black Turn 1: " + validMoves.size());
        board.update(moveToField(validMoves, "e5"), piece);
        board.printField();

        // White's turn
        piece = this.board.getFields()[3][3].getPiece();
        validMoves = piece.getValidMoves(board.getFields(), board.getPieceLocation(piece));
        System.out.println("Valid pawn moves for white Turn 2: " + validMoves.size());
        board.update(moveToField(validMoves, "e5"), piece);
        board.printField();

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
