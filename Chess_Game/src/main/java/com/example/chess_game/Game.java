package com.example.chess_game;

import com.example.chess_game.pieces.Bishop;
import com.example.chess_game.pieces.King;
import com.example.chess_game.pieces.Knight;
import com.example.chess_game.pieces.Piece;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Game {
    private static Player black;
    private static Player white;
    private static Player currentPlayer;

    /**
     * The one board instance that is currently running
     */
    private static Board board;

    public static void initGame(Player playerWhite, Player playerBlack) {
        black = playerBlack;
        white = playerWhite;
        currentPlayer = white;
        board = new Board(white, black);
    }

    public static void toggleCurrentPlayer() {
        currentPlayer.timer().stop();
        currentPlayer = (currentPlayer == black) ? white : black;
        currentPlayer.timer().start();
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static Board getBoard() {
        return board;
    }

    public static List<String> getMoves() {
        return board.getMoves();
    }

    public static boolean isStalemate(Set<Piece> playerToMovePieces) {
        return playerToMovePieces.stream()
                .noneMatch(piece -> piece.getLegalMoves(board, board.getPieceLocation(piece)).size() > 0);
    }

    public static boolean hasPlayerInsufficientMaterial(Set<Piece> playerToMovePieces, Set<Piece> enemyPieces) {
        return playerToMovePieces.stream()
                .filter(piece -> !(piece instanceof King))
                .allMatch(piece -> piece instanceof Bishop || piece instanceof Knight) &&
                enemyPieces.stream().filter(piece -> !(piece instanceof King))
                        .allMatch(piece -> piece instanceof Bishop || piece instanceof Knight);
    }

    public static boolean isThreefoldRepetition() {
        boolean repetition = false;
        List<Position> positions = board.getBoardPositions();
        for (Position p : positions) {
            if (Collections.frequency(positions, p) == 3) {
                repetition = true;
                break;
            }
        }
        return repetition;
    }

    public static boolean isFiftyRuleEffective() {
        List<String> moves = board.getMoves();
        boolean fiftyMoveRule = false;
        if (moves.size() > 50) {
            fiftyMoveRule = moves.subList(moves.size() - 51, moves.size())
                    .stream()
                    .noneMatch(move -> move.length() == 2 || move.contains("x"));
        }
        return fiftyMoveRule;
    }

    public static boolean isDraw() {
        toggleCurrentPlayer();
        Set<Piece> playerToMovePieces = board.getPieces(currentPlayer.color());
        Set<Piece> enemyPieces = board.getPieces(currentPlayer.color() == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE);
        toggleCurrentPlayer();

        // Timeout-Draw - Player to move ran out of time and enemy has insufficient materials
        // TODO
        boolean timeoutDraw = false;

        return isStalemate(playerToMovePieces) || hasPlayerInsufficientMaterial(playerToMovePieces, enemyPieces) ||
                isThreefoldRepetition() || isFiftyRuleEffective() || timeoutDraw;
    }

    public static Field getField(int row, int column) {
        if (row < 0 || column < 0 || row > 7 || column > 7) {
            throw new IllegalArgumentException("Illegal Coordinate was given to Game::getField(row, column)");
        }
        return board.getFields()[column][row];
    }

    /**
     * Getter for the player of the given color
     *
     * @param color The color of which the player should be returned
     * @return The player of the given color
     */
    public static Player getPlayer(ChessColor color) {
        if (color.equals(ChessColor.WHITE)) {
            return white;
        }
        return black;
    }
}
