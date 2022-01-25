package com.example.chess_game;

import com.example.chess_game.pieces.Pawn;
import com.example.chess_game.pieces.Piece;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController extends Application implements Initializable {

    final ImageView HIGHLIGHT_CLICKED = new ImageView("graphics/highlight_pink.png");
    final Image HIGHLIGHT_VALID_MOVES_IMAGE = new Image("graphics/highlight_orange2px.png");
    final Image HIGHLIGHT_KING_CHECK = new Image("graphics/highlight_red.png");
    final Map<Piece, ImageView> pieceImageViews = new HashMap<>();

    Set<ImageView> highlightImageViews = new HashSet<>();
    Set<String> highlightedFieldNames = new HashSet<>();
    List<ImageView> graveyard_white = new ArrayList<>();
    List<ImageView> graveyard_black = new ArrayList<>();
    Piece selected_piece = null;

    boolean gameRunning = false;

    @FXML private GridPane gridPane_board;
    @FXML private FlowPane flowPanel_graveyard_white;
    @FXML private FlowPane flowPanel_graveyard_black;
    @FXML private Button resign_btn;
    @FXML private Button prev_moves_btn;
    @FXML private Label label_player_white;
    @FXML private Label label_player_black;
    @FXML private Label label_timer_white;
    @FXML private Label label_timer_black;

    /**
     * Clear label texts so the user is not distracted and build board & buttons
     *
     * Method override from Initializable Interface - we do not use the params here, but they have to be here
     * in order to fit the overridden method
     *
     * @param location  relative path to root object
     * @param resources resources used to localize root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label_player_white.setText("");
        label_player_black.setText("");
        label_timer_white.setText("");
        label_timer_black.setText("");

        gridPane_board.setOnMouseClicked(this::handle);
        resign_btn.setVisible(false);
        prev_moves_btn.setVisible(false);
    }

    /**
     * Moves the last selected piece to the given field and checks if the other king was set checkmate with that move
     *
     * @param clickedField the field where the selected piece is moved
     */
    private void movePiece(Field clickedField) {
        if (clickedField.getPiece() != null) {
            ImageView capturedPieceImageView = new ImageView(clickedField.getPiece().getImage());
            if (clickedField.getPiece().getColor() == ChessColor.WHITE) {
                graveyard_white.add(capturedPieceImageView);
                flowPanel_graveyard_white.getChildren().add(capturedPieceImageView);
            } else { // black
                graveyard_black.add(capturedPieceImageView);
                flowPanel_graveyard_black.getChildren().add(capturedPieceImageView);
            }
            gridPane_board.getChildren().remove(pieceImageViews.get(clickedField.getPiece()));
            pieceImageViews.remove(clickedField.getPiece());
        }

        boolean checkmate = Game.getBoard().update(clickedField, selected_piece, gridPane_board, pieceImageViews);

        // Skip changing board if the move was a Promotion
        if (!(selected_piece instanceof Pawn && ((Pawn) selected_piece).isPromoted())) {
            gridPane_board.getChildren().remove(pieceImageViews.get(selected_piece));
            ImageView newImgView = new ImageView(selected_piece.getImage());
            gridPane_board.add(newImgView, clickedField.getColumn(), 7 - clickedField.getRow());
            pieceImageViews.put(selected_piece, newImgView);
        }

        if (checkmate) {
            openWinnerDialog(Game.getPlayer(selected_piece.getColor()), "Other King is checkmate");
            return;
        } else if (Game.isDraw()) {
            openDrawDialog();
            return;
        }

        selected_piece = null;
        Game.toggleCurrentPlayer();
    }

    /**
     * Opens a dialog to tell the players who won the game
     *
     * @param winner The player who won the game
     * @param causeOfWin The cause of the win (e.g. Other King is checkmate, other player has resigned, etc.)
     */
    private void openWinnerDialog(Player winner, String causeOfWin) {
        stopGame();
        // Build dialog
        Alert winnerDialog = new Alert(Alert.AlertType.NONE);
        winnerDialog.setTitle("Congratulations!");
        winnerDialog.setContentText(winner.name() + " has won the Game.\nCause of win: " + causeOfWin);

        winnerDialog.getButtonTypes().setAll(
                new ButtonType("Ok", ButtonBar.ButtonData.YES)
        );

        // gets the clicked result; if the window is closed without a button being clicked it counts as "Ok"
        winnerDialog.showAndWait();

        createNewGame();
    }

    /**
     * Opens a dialog to tell the players that the game concluded to a draw
     */
    private void openDrawDialog() {
        stopGame();
        // Build dialog
        Alert drawDialog = new Alert(Alert.AlertType.NONE);
        drawDialog.setTitle("DRAW!");
        drawDialog.setContentText("This game ends in a draw.");

        drawDialog.getButtonTypes().setAll(
                new ButtonType("Ok", ButtonBar.ButtonData.YES)
        );

        // gets the clicked result; if the window is closed without a button being clicked it counts as "Ok"
        drawDialog.showAndWait();

        createNewGame();
    }

    private void handle(MouseEvent mouseEvent) {
        if (!gameRunning) return;

        Field clickedField = getField((int) mouseEvent.getX() / 37, (int) mouseEvent.getY() / 37);
        highlightClickedField(clickedField);

        if (selected_piece != null && highlightedFieldNames.contains(clickedField.getFieldName())) {
            movePiece(clickedField);
        } else {
            Piece piece = clickedField.getPiece();
            if (piece == null || piece.getColor() != Game.getCurrentPlayer().color()) return;
            System.out.println("Piece on clicked field: " + piece);
            selected_piece = piece;
            Set<Field> legalMoves = piece.getLegalMoves(Game.getBoard(), clickedField);
            highlightValidMoves(legalMoves);
        }
        if (!gameRunning) return;

        if (Game.getCurrentPlayer().timer().hasRunOut()) {
            Player loser = Game.getCurrentPlayer();
            Player winner = (loser.color() == ChessColor.WHITE) ? Game.getPlayer(ChessColor.BLACK) : Game.getPlayer(ChessColor.WHITE);
            openWinnerDialog(winner, "Time of other Player has run out.");
        }
        highlightKingsInCheck();
    }

    /**
     * @param row    the row coordinate
     * @param column the column coordinate
     * @return the field in the given coordinates
     */
    private Field getField(int row, int column) {
        Field field = null;
        if (gameRunning) {
            field = Game.getField(row, 7 - column);
            System.out.println("    Clicked on field: " + field.getFieldName());
        }
        return field;
    }

    /**
     * Highlights the clicked field and removes the highlighting from the other fields
     */
    private void highlightClickedField(Field field) {
        gridPane_board.getChildren().removeAll(HIGHLIGHT_CLICKED);
        if (highlightImageViews != null && !highlightImageViews.isEmpty()) {
            gridPane_board.getChildren().removeAll(highlightImageViews);
        }

        for (int row = 0; row < 8; ++row) {
            for (int column = 0; column < 8; ++column) {
                if (field.equals(Game.getField(row, column))) {
                    gridPane_board.add(HIGHLIGHT_CLICKED, row, 7 - column);
                }
            }
        }
    }

    private void highlightValidMoves(Set<Field> validMoves) {
        highlightImageViews = new HashSet<>();
        highlightedFieldNames = new HashSet<>();
        for (int row = 0; row < 8; ++row) {
            for (int column = 0; column < 8; ++column) {
                Field field = Game.getField(row, column);
                if (validMoves.contains(field)) {
                    ImageView node = new ImageView(HIGHLIGHT_VALID_MOVES_IMAGE);
                    gridPane_board.add(node, row, 7 - column);
                    highlightedFieldNames.add(field.getFieldName());
                    highlightImageViews.add(node);
                }
            }
        }
    }

    private void setStartFormation() {
        pieceImageViews.clear();
        gridPane_board.getChildren().clear();
        graveyard_black.clear();
        graveyard_white.clear();
        flowPanel_graveyard_white.getChildren().clear();
        flowPanel_graveyard_black.getChildren().clear();

        for (int row = 0; row < 8; ++row) {
            for (int column = 0; column < 8; ++column) {
                Piece piece = Game.getField(row, column).getPiece();
                if (piece != null) {
                    ImageView pieceImage = new ImageView(piece.getImage());
                    pieceImageViews.put(piece, pieceImage);
                    gridPane_board.add(pieceImage, row, 7 - column);
                }
            }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Watchdoggos Chess!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Load Dialog for user input (player names and playing time) and creates game if "new game" was clicked in dialog
     */
    private void createNewGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newgame-dialog.fxml"));
            Parent root = loader.load();
            NewGameDialog dialog = loader.getController();
            dialog.showDialog(root); // Waits for the dialog to finish

            if (!dialog.getDialogResult()) return; // User clicked Cancel

            if (gameRunning) {
                Game.getPlayer(ChessColor.WHITE).timer().resetAndStop();
                Game.getPlayer(ChessColor.BLACK).timer().resetAndStop();
            }

            Game.initGame(
                    new Player(ChessColor.WHITE,
                            dialog.textfield_player_white.getText(),
                            new ChessTimer(label_timer_white,
                                    Integer.parseInt(dialog.textfield_minutes_per_player.getText()))
                    ),
                    new Player(ChessColor.BLACK,
                            dialog.textfield_player_black.getText(),
                            new ChessTimer(label_timer_black,
                                    Integer.parseInt(dialog.textfield_minutes_per_player.getText()))
                    )
            );

            label_player_black.setText(dialog.textfield_player_black.getText());
            label_player_white.setText(dialog.textfield_player_white.getText());

            Game.getPlayer(ChessColor.WHITE).timer().start();

            setStartFormation();
            resign_btn.setVisible(true);
            prev_moves_btn.setVisible(true);
            gameRunning = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnNewGameClicked() {
        System.out.println("INFO: Player clicked on 'New Game'");

        // Check if game is running
        if (gameRunning) {
            // ask if the player really wants to start a new game
            Alert saveGameDialog = new Alert(Alert.AlertType.NONE);
            saveGameDialog.setTitle("Discard current game");
            saveGameDialog.setContentText("Warning: The current game is still running.\nDo you want to resign in the current game and start a new one?");

            // Set the buttons for the dialog
            saveGameDialog.getButtonTypes().setAll(
                    new ButtonType("Yes", ButtonBar.ButtonData.YES),
                    new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)
            );

            // gets the clicked result; if the window is closed without a button being clicked it counts as "Cancel"
            ButtonType result = saveGameDialog.showAndWait().orElse(ButtonType.CANCEL);

            System.out.println("INFO: Player selection if they want to discard and resign game: " + result.getText());
            if (!ButtonBar.ButtonData.YES.equals(result.getButtonData())) return;
        }

        System.out.println("INFO: Game is being initialized");
        createNewGame();
    }

    @FXML
    public void btnResignClicked() {
        System.out.println("INFO: Player clicked on 'Resign'");
        // if there is a game running, ask if the player really wants to resign
        Alert resignGameDialog = new Alert(Alert.AlertType.NONE);
        resignGameDialog.setTitle("Resign current game");
        resignGameDialog.setContentText("Do you want to resign in the current game?");

        // Set the buttons for the dialog
        resignGameDialog.getButtonTypes().setAll(
                new ButtonType("Yes", ButtonBar.ButtonData.YES),
                new ButtonType("Cancel", ButtonBar.ButtonData.NO)
        );

        // gets the clicked result; if the window is closed without a button being clicked it counts as "No"
        ButtonType result = resignGameDialog.showAndWait().orElse(ButtonType.NO);

        System.out.println("INFO: Player selection if they want to resign game: " + result.getText());


        if (ButtonBar.ButtonData.YES.equals(result.getButtonData())) {
            Player activePlayer = new Player(ChessColor.BLACK, "Tom", new ChessTimer(new Label(), 1)); // Player who clicked on resign in their turn
            Player winner = (activePlayer.color() == ChessColor.WHITE ? Game.getPlayer(ChessColor.BLACK) : Game.getPlayer(ChessColor.WHITE));
            openWinnerDialog(winner, "Other player resigned.");
        }
    }

    @FXML
    public void btnPreviousMovesClicked() {
        System.out.println("INFO: Player clicked on 'Show Previous Moves'");

        // Show Dialog of previous moves
        Alert resignGameDialog = new Alert(Alert.AlertType.NONE);
        resignGameDialog.setTitle("Previous Moves");

        TextFlow textFlow = new TextFlow();
        if (!Game.getMoves().isEmpty()) {
            List<String> moves = Game.getMoves();
            Text text;
            for (int i = 0; i < moves.size(); i++) {
                // Show Number of round (one round means turn for both)
                if (i % 2 == 0) {
                    text = new Text(String.format("%d. ", i / 2 + 1));
                    text.setStyle("-fx-font-weight: bold");
                    textFlow.getChildren().add(text);
                }

                // Show move
                textFlow.getChildren().add(new Text(moves.get(i) + " "));

                // Create Line Breaks
                if (i > 0 && ((i + 1) % 10 == 0)) textFlow.getChildren().add(new Text(System.lineSeparator()));
            }
        } else {
            textFlow.getChildren().add(new Text("No previous moves available."));
        }

        resignGameDialog.getDialogPane().setContent(textFlow);

        // Set the buttons for the dialog
        resignGameDialog.getButtonTypes().setAll(
                new ButtonType("Ok", ButtonBar.ButtonData.YES)
        );

        resignGameDialog.showAndWait();
    }

    /**
     * Checks if there are any kings in check and, if yes, hightlight them
     */
    private void highlightKingsInCheck() {
        Set<Piece> kingsInCheck = Game.getBoard().getKingsInCheck();
        if(kingsInCheck == null || kingsInCheck.isEmpty()) {
            return;
        }
        for (int row = 0; row < 8; ++row) {
            for (int column = 0; column < 8; ++column) {
                Field field = Game.getField(row, column);
                if (kingsInCheck.contains(field.getPiece())) {
                    ImageView node = new ImageView(HIGHLIGHT_KING_CHECK);
                    gridPane_board.add(node, row, 7 - column);
                    highlightedFieldNames.add(field.getFieldName());
                    highlightImageViews.add(node);
                }
            }
        }
    }

    private void stopGame() {
        selected_piece = null;
        Game.getPlayer(ChessColor.WHITE).timer().resetAndStop();
        Game.getPlayer(ChessColor.BLACK).timer().resetAndStop();
        gameRunning = false;
        resign_btn.setVisible(false);
    }

    public static void main(String[] args) {
        launch();
    }

}
