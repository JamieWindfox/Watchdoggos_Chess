package com.example.chess_game;

import java.util.ArrayList;
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

    public void testGame() {
        board.printField();
        Piece whitePiece = this.board.getFields()[1][0].getPiece();
        Set<Field> validMoves = whitePiece.getValidMoves(board.getFields(), board.getPieceLocation(whitePiece));
        System.out.println("Valid pawn moves for white Turn 1: " + validMoves.size());
        board.update(new ArrayList<>(validMoves).get(1), whitePiece);
        board.printField();

        Piece blackPiece = this.board.getFields()[6][0].getPiece();
        validMoves = blackPiece.getValidMoves(board.getFields(), board.getPieceLocation(blackPiece));
        System.out.println("Valid pawn moves for black Turn 1: " + validMoves.size());
        board.update(new ArrayList<>(validMoves).get(1), blackPiece);
        board.printField();

        Piece whitePieceTurn2 = this.board.getFields()[3][0].getPiece();
        validMoves = whitePieceTurn2.getValidMoves(board.getFields(), board.getPieceLocation(whitePieceTurn2));
        System.out.println("Valid pawn moves for white Turn 2: " + validMoves.size());

        Piece blackPieceTurn2 = this.board.getFields()[3][0].getPiece();
        validMoves = blackPieceTurn2.getValidMoves(board.getFields(), board.getPieceLocation(blackPieceTurn2));
        System.out.println("Valid pawn moves for black Turn 2: " + validMoves.size());
    }


}
