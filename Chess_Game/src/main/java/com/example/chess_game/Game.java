package com.example.chess_game;

import javafx.scene.image.Image;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Game {
    private final Player black;
    private final Player white;
    private Player currentPlayer;

    /**
     * The one board instance that is currently running
     */
    private static Board board;

    public Game(Player playerBlack, Player playerWhite, boolean playerWhiteBegins) {
        this.black = playerBlack;
        this.white = playerWhite;
        currentPlayer = playerWhiteBegins ? this.white : this.black;
        board = new Board(white, black);
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
        /*board.printField();

        // White's turn
        Piece piece = board.getFields()[1][3].getPiece();
        Set<Field> validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid pawn moves for white Turn 1: " + validMoves.size());
        board.update(moveToField(validMoves, "d4"), piece);
        board.printField();

        // Black's turn
        piece = board.getFields()[6][4].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid pawn moves for black Turn 1: " + validMoves.size());
        board.update(moveToField(validMoves, "e5"), piece);
        board.printField();

        // White's turn
        piece = board.getFields()[3][3].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid pawn moves for white Turn 2: " + validMoves.size());
        board.update(moveToField(validMoves, "e5"), piece);
        board.printField();

        System.out.println("Move history: " + board.getMoves());

        // Black's turn
        piece = board.getFields()[6][5].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid pawn moves for black Turn 2: " + validMoves.size());
        board.update(moveToField(validMoves, "f5"), piece);
        board.printField();

        // White's turn
        piece = board.getFields()[4][4].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid pawn moves for white Turn 3: " + validMoves.size());
        board.update(moveToField(validMoves, "f6"), piece);
        board.printField();

        // Black's turn
        piece = board.getFields()[7][4].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid king moves for black Turn 3: " + validMoves.size());
        board.update(moveToField(validMoves, "f7"), piece);
        board.printField();

        // White's turn
        piece = board.getFields()[1][6].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid pawn moves for white Turn 3: " + validMoves.size());
        board.update(moveToField(validMoves, "g4"), piece);
        board.printField();

        // Black's turn
        piece = board.getFields()[6][5].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid king moves for black Turn 4: " + validMoves.size());
        board.update(moveToField(validMoves, "e6"), piece);
        board.printField();

        // White's turn
        piece = board.getFields()[3][6].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid pawn moves for white Turn 5: " + validMoves.size());
        board.update(moveToField(validMoves, "g5"), piece);
        board.printField();

        // Black's turn
        piece = board.getFields()[5][4].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid king moves for black Turn 5: " + validMoves.size());
        board.update(moveToField(validMoves, "f5"), piece);
        board.printField();

        // White's turn
        piece = board.getFields()[0][4].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid king moves for white Turn 6: " + validMoves.size());
        board.update(moveToField(validMoves, "d2"), piece);
        board.printField();

        // Black's turn
        piece = board.getFields()[4][5].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid king moves for black Turn 5: " + validMoves.size());
        board.update(moveToField(validMoves, "f4"), piece);
        board.printField();

        // White's turn
        piece = board.getFields()[0][5].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid bishop moves for white Turn 6: " + validMoves.size());
        board.update(moveToField(validMoves, "g2"), piece);
        board.printField();

        // Black's turn
        piece = board.getFields()[7][3].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid queen moves for black Turn 6: " + validMoves.size());
        board.update(moveToField(validMoves, "f6"), piece);

        // White's turn
        piece = board.getFields()[1][3].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid bishop moves for white Turn 7: " + validMoves.size());
        board.update(moveToField(validMoves, "e1"), piece);

        // Black's turn
        piece = board.getFields()[3][5].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid queen moves for black Turn 7: " + validMoves.size());
        board.update(moveToField(validMoves, "e5"), piece);

        // White's turn
        piece = board.getFields()[0][2].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));
        System.out.println("Valid bishop moves for white Turn 8: " + validMoves.size());
        board.update(moveToField(validMoves, "f4"), piece);
        board.printField();

        // Black's turn
        piece = board.getFields()[4][4].getPiece();
        validMoves = piece.getLegalMoves(board, board.getPieceLocation(piece));

        for (Field vm :
                validMoves) {
            System.out.println(vm.getFieldName());
        }

        System.out.println("Move history: " + board.getMoves());*/
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

    public void toggleCurrentPlayer() {
        currentPlayer = (currentPlayer == black) ? white : black;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static Board getBoard() {
        return board;
    }

    public List<String> getMoves() {
        return board.getMoves();
    }

    public boolean isDraw(Player playerToMove) {
        Set<Piece> playerToMovePieces = board.getPieces(playerToMove.getColor());
        Set<Piece> enemyPieces = board.getPieces(playerToMove.getColor() == Color.WHITE ? Color.BLACK : Color.WHITE);

        // Stalemate
        boolean stalemate = playerToMovePieces.stream()
                .noneMatch(piece -> piece.getLegalMoves(board, board.getPieceLocation(piece)).size() > 0);

        // Insufficient material
        boolean enemyInsufficientMaterial = enemyPieces.stream().filter(piece -> !(piece instanceof King))
                .allMatch(piece -> piece instanceof Bishop || piece instanceof Knight);
        boolean insufficient =
                playerToMovePieces.stream()
                        .filter(piece -> !(piece instanceof King))
                        .allMatch(piece -> piece instanceof Bishop || piece instanceof Knight) && enemyInsufficientMaterial;


        // Threefold repetition
        boolean repetition = false;
        List<Position> positions = board.getBoardPositions();
        for (Position p : positions) {
            int positionCount = Collections.frequency(positions, p);
            if (positionCount == 3) {
                repetition = true;
                break;
            }
        }

        // Mutual agreement
        // TODO

        // 50-Move rule
        List<String> moves = board.getMoves();
        boolean fiftyMoveRule = false;
        if (moves.size() > 50) {
            fiftyMoveRule = moves.subList(moves.size() - 51, moves.size())
                    .stream()
                    .noneMatch(move -> move.length() == 2 || move.contains("x"));
        }

        // Timeout-Draw - Player to move ran out of time and enemy has insufficient materials
        // TODO timer value
        boolean timeoutDraw = false;
        if (enemyInsufficientMaterial) {
            timeoutDraw = true;
        }

        return stalemate || insufficient || repetition || fiftyMoveRule || timeoutDraw;
    }

    public Field getField(int row, int column) {
        if (row < 0 || column < 0 || row > 7 || column > 7) {
            throw new IllegalArgumentException("Illegal Coordinate was given to Game::getField(row, column)");
        }
        return board.getFields()[column][row];
    }

    public Field[][] getFields() {
        return board.getFields();
    }

    @Override
    public String toString() {
        return "Current Game";
    }

    /**
     * Getter for the player of the given color
     * @param color The color of which the player should be returned
     * @return The player of the given color
     */
    public Player getPlayer(Color color) {
        if(color.equals(Color.WHITE)) {
            return white;
        }
        return black;
    }
}
