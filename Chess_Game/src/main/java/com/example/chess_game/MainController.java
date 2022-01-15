package com.example.chess_game;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

    final int PLAYER_NAME_MAX_LENGTH = 20;
    final ImageView HIGHLIGHT_CLICKED = new ImageView("graphics/highlight_pink.png");
    final Image HIGHLIGHT_VALID_MOVES_IMAGE = new Image("graphics/highlight_orange2px.png");
    final Map<Piece, ImageView> pieceImageViews = new HashMap<>();
    Set<ImageView> highlightImageViews = new HashSet<>();
    Set<String> highlightedFieldNames = new HashSet<>();
    List<ImageView> cemetary_white = new ArrayList<>();
    List<ImageView> cemetary_black = new ArrayList<>();
    Piece selected_piece = null;
    boolean whitePlayerBegins = true;

    Game game;

    @FXML
    private GridPane gridpane_board;
    @FXML
    private FlowPane flowpanel_cemetary_white;
    @FXML
    private FlowPane flowpanel_cemetary_black;
    @FXML
    private Button resign_btn;
    @FXML
    private Label label_player1;
    @FXML
    private Label label_player2;
    @FXML
    private Label label_timer1;
    @FXML
    private Label label_timer2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Clear label texts so the user is not distracted
        label_player1.setText("");
        label_player2.setText("");
        label_timer1.setText("");
        label_timer2.setText("");

        gridpane_board.setOnMouseClicked(this::handle);
        resign_btn.setVisible(false);
    }

    /**
     * Moves the last selected piece to the given field and checks if the other king was set checkmate with that move
     *
     * @param clickedField the field where the selected piece is moved
     */
    private void movePiece(Field clickedField) {
        if (clickedField.getPiece() != null) {
            ImageView capturedPieceImageView = new ImageView(clickedField.getPiece().getImage());
            if (clickedField.getPiece().getColor() == Color.WHITE) {
                cemetary_white.add(capturedPieceImageView);
                flowpanel_cemetary_white.getChildren().add(capturedPieceImageView);
            } else { // black
                cemetary_black.add(capturedPieceImageView);
                flowpanel_cemetary_black.getChildren().add(capturedPieceImageView);
            }
            gridpane_board.getChildren().remove(pieceImageViews.get(clickedField.getPiece()));
            pieceImageViews.remove(clickedField.getPiece());
        }
        boolean checkmate = Game.getBoard().update(clickedField, selected_piece, gridpane_board, pieceImageViews);
        //game.getBoard().printField(); -> for debugging

        // Skip changing board if the move was a Promotion
        if (!(selected_piece instanceof Pawn && ((Pawn) selected_piece).isPromoted())) {
            gridpane_board.getChildren().remove(pieceImageViews.get(selected_piece));
            ImageView newImgView = new ImageView(selected_piece.getImage());
            gridpane_board.add(newImgView, clickedField.getColumn(), 7 - clickedField.getRow());
            pieceImageViews.put(selected_piece, newImgView);
        }

        if (checkmate) {
            openWinnerDialog(game.getPlayer(selected_piece.getColor()), "Other King is checkmate");
        }

        selected_piece = null;
        game.toggleCurrentPlayer();
    }

    /**
     * Opens a dialog to tell the players who have won the game
     */
    //WIP
    private void openWinnerDialog(Player winner, String causeOfWin) {
        // Build dialog
        Alert winnerDialog = new Alert(Alert.AlertType.NONE);
        winnerDialog.setTitle("Congratulations!");
        winnerDialog.setContentText(winner.getName() + " has won the Game.\nCause of win: " + causeOfWin);

        winnerDialog.getButtonTypes().setAll(
                new ButtonType("Ok", ButtonBar.ButtonData.YES)
        );

        // gets the clicked result; if the window is closed without a button being clicked it counts as "Ok"
        ButtonType result = winnerDialog.showAndWait().orElse(ButtonType.YES);

        createNewGame();
    }

    private void handle(MouseEvent mouseEvent) {
        if (game == null) return;

        Field clickedField = getFieldFromCoordinates(mouseEvent.getX(), mouseEvent.getY());
        highlightClickedField(clickedField);

        if (selected_piece != null && highlightedFieldNames.contains(clickedField.getFieldName())) {
            movePiece(clickedField);
        } else {
            Piece piece = clickedField.getPiece();
            if (piece == null || piece.getColor() != game.getCurrentPlayer().getColor()) return;
            System.out.println("Piece on clicked field: " + piece);
            selected_piece = piece;
            Set<Field> legalMoves = piece.getLegalMoves(Game.getBoard(), clickedField);
            highlightValidMoves(legalMoves);
        }
    }

    private Field getFieldFromCoordinates(double x, double y) {
        int row = (int) (x / 37.0);
        int column = (int) (y / 37.0);

        return getField(row, column);

    }

    private Field getField(int row, int column) {
        Field field = null;
        if (game != null) {
            field = game.getField(row, 7 - column);
            System.out.println("    Clicked on field: " + field.getFieldName());
        }
        return field;
    }

    /**
     * Highlights the clicked field and removes the highlighting from the other fields
     */
    private void highlightClickedField(Field field) {
        gridpane_board.getChildren().removeAll(HIGHLIGHT_CLICKED);
        if (highlightImageViews != null && !highlightImageViews.isEmpty()) {
            gridpane_board.getChildren().removeAll(highlightImageViews);
        }

        for (int row = 0; row < 8; ++row) {
            for (int column = 0; column < 8; ++column) {
                if (field.equals(game.getField(row, column))) {
                    gridpane_board.add(HIGHLIGHT_CLICKED, row, 7 - column);
                }
            }
        }
    }

    private void highlightValidMoves(Set<Field> validMoves) {
        highlightImageViews = new HashSet<>();
        highlightedFieldNames = new HashSet<>();
        for (int row = 0; row < 8; ++row) {
            for (int column = 0; column < 8; ++column) {
                Field field = game.getField(row, column);
                if (validMoves.contains(field)) {
                    ImageView node = new ImageView(HIGHLIGHT_VALID_MOVES_IMAGE);
                    gridpane_board.add(node, row, 7 - column);
                    highlightedFieldNames.add(field.getFieldName());
                    highlightImageViews.add(node);
                }
            }
        }
    }

    private void setStartFormation() {
        pieceImageViews.clear();
        gridpane_board.getChildren().clear();
        cemetary_black.clear();
        cemetary_white.clear();
        flowpanel_cemetary_white.getChildren().clear();
        flowpanel_cemetary_black.getChildren().clear();

        for (int row = 0; row < 8; ++row) {
            for (int column = 0; column < 8; ++column) {
                Piece piece = game.getField(row, column).getPiece();
                if (piece != null) {
                    ImageView pieceImage = new ImageView(piece.getImage());
                    pieceImageViews.put(piece, pieceImage);
                    gridpane_board.add(pieceImage, row, 7 - column);
                }
            }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 420);
        stage.setTitle("Watchdoggos Chess!");
        stage.setScene(scene);
        stage.show();
    }

    // creates a new game
    private void createNewGame() {
        // Ask players for their names
        String playerWhite = showPlayerNameDialog(Color.WHITE.name());
        if (playerWhite == null || playerWhite.isBlank()) return;
        String playerBlack = showPlayerNameDialog(Color.BLACK.name());
        if (playerBlack == null || playerBlack.isBlank()) return;

        whitePlayerBegins = true;
        game = new Game(
                // TODO Change to time from user input dialog
                new Player(Color.BLACK, playerBlack, new Timer(label_timer2, 15)),
                new Player(Color.WHITE, playerWhite, new Timer(label_timer1, 15)));
        whitePlayerBegins = !whitePlayerBegins;
        label_player1.setText(playerWhite);
        label_player2.setText(playerBlack);

        setStartFormation();
        resign_btn.setVisible(true);
    }

    @FXML
    public void btnNewGameClicked() {
        System.out.println("INFO: Player clicked on 'New Game'");

        // Check if game is running
        if (game != null) {
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

    public String showPlayerNameDialog(String playerColor) {
        // Build dialog
        TextInputDialog tid = new TextInputDialog(PLAYER_NAME_MAX_LENGTH + " characters maximum");
        tid.getDialogPane().setGraphic(null);
        tid.setHeaderText("Enter Name of " + playerColor + " Player:");
        tid.setTitle("Enter Player Name");
        Optional<String> dialogResult = tid.showAndWait(); // Holds until the user takes an action

        // isEmpty is true, if the user clicked Cancel or Close
        if (dialogResult.isEmpty()) return null;

        String playerName = tid.getEditor().getText();
        // Verify Input / Repeat Dialog if user is empty or too long
        if (playerName.isBlank() || playerName.length() > PLAYER_NAME_MAX_LENGTH)
            return showPlayerNameDialog(playerColor);

        System.out.println(playerColor + "Player chose user name '" + playerName + "'");
        return playerName;
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
            Player activePlayer = new Player(Color.BLACK, "Tom", new Timer(new Label(), 1)); // Player who clicked on resign in their turn
            Player winner = (activePlayer.getColor() == Color.WHITE ? game.getPlayer(Color.BLACK) : game.getPlayer(Color.WHITE));
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
        Text text;
        if (game != null) {
            List<String> moves = game.getMoves();
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

    public static void main(String[] args) {
        launch();
    }

}