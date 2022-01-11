package com.example.chess_game;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    final ImageView HIGHLIGHT_CLICKED = new ImageView("graphics/highlight.png");
    final Image HIGHLIGHT_VALID_MOVES_IMAGE = new Image("graphics/highlight.png");
    List<ImageView> highlighted_valid_moves = new ArrayList<>();
    Piece selected_piece = null;
    ImageView clicked_piece_image = null;

    Game game;

    @FXML
    private GridPane gridpane_board;
    @FXML
    private FlowPane flowpanel_cemetary1;
    @FXML
    private FlowPane flowpanel_cemetary2;
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

        // TODO Remove in production ;)
        /*flowpanel_cemetary1.getChildren().add(new ImageView(new Image("graphics/black_queen.png")));
        flowpanel_cemetary1.getChildren().add(new ImageView(new Image("graphics/black_pawn.png")));
        flowpanel_cemetary1.getChildren().add(new ImageView(new Image("graphics/black_pawn.png")));
        flowpanel_cemetary1.getChildren().add(new ImageView(new Image("graphics/black_bishop.png")));
        flowpanel_cemetary1.getChildren().add(new ImageView(new Image("graphics/black_rook.png")));

        flowpanel_cemetary2.getChildren().add(new ImageView(new Image("graphics/white_queen.png")));
        flowpanel_cemetary2.getChildren().add(new ImageView(new Image("graphics/white_pawn.png")));
        flowpanel_cemetary2.getChildren().add(new ImageView(new Image("graphics/white_pawn.png")));
        flowpanel_cemetary2.getChildren().add(new ImageView(new Image("graphics/white_bishop.png")));
        flowpanel_cemetary2.getChildren().add(new ImageView(new Image("graphics/white_rook.png")));*/


        gridpane_board.setOnMouseClicked(this::handle);
    }

    private void movePiece(Field clickedField, ImageView clickedNode) {
        game.getBoard().update(clickedField, selected_piece);
        game.getBoard().printField();
        ImageView newImage = new ImageView(clicked_piece_image.getImage());
        gridpane_board.getChildren().remove(clicked_piece_image);
        clickedNode.setImage(newImage.getImage());
        selected_piece = null;
    }

    private void handle(MouseEvent mouseEvent) {
        if (game == null) return;

        Field clickedField = getFieldFromCoordinates(mouseEvent.getX(), mouseEvent.getY());
        ImageView clickedNode = getImageFromCoordinates(clickedField.getRow(), clickedField.getColumn());

        if (selected_piece != null && highlighted_valid_moves.contains(clickedNode)) {
            movePiece(clickedField, clickedNode);
        } else {
            highlightClickedField(clickedField);

            Piece piece = clickedField.getPiece();
            System.out.println("Piece on clicked field: " + piece);
            if (piece != null) {
                clicked_piece_image = clickedNode;
                selected_piece = piece;
                Set<Field> validMoves = piece.getValidMoves(game.getBoard(), clickedField);
                highlightValidMoves(validMoves);
            } else {
                selected_piece = null;
            }
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
            field = game.getField(row, column);
            System.out.println("    Clicked on field: " + field.getFieldName());
        }
        return field;
    }

    /**
     * Highlights the clicked field and removes the highlight from all other fields
     */
    private void highlightClickedField(Field field) {
        gridpane_board.getChildren().removeAll(HIGHLIGHT_CLICKED);
        if (highlighted_valid_moves != null && !highlighted_valid_moves.isEmpty()) {
            gridpane_board.getChildren().removeAll(highlighted_valid_moves);
        }

        for (int row = 0; row < 8; ++row) {
            for (int column = 0; column < 8; ++column) {
                if (field.equals(game.getField(row, column))) {
                    gridpane_board.add(HIGHLIGHT_CLICKED, row, column);
                }
            }
        }
    }

    /*
        /**
         * Gets the ImageView in gridpane_board by row and column index
         * @param row
         * @param column
         * @return
         */
    private ImageView getImageFromCoordinates(int row, int column) {
        for (Node node : gridpane_board.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                if (node instanceof ImageView)
                    return (ImageView) node;
            }
        }
        return null;
    }

    private void highlightValidMoves(Set<Field> validMoves) {
        highlighted_valid_moves = new ArrayList<>();
        for (int row = 0; row < 8; ++row) {
            for (int column = 0; column < 8; ++column) {
                if (validMoves.contains(game.getField(row, column))) {
                    ImageView node = new ImageView(HIGHLIGHT_VALID_MOVES_IMAGE);
                    gridpane_board.add(node, row, column);
                    highlighted_valid_moves.add(node);
                }
            }
        }
    }

    private void setStartFormation() {
        for (int column = 0; column < 8; ++column) {
            for (int row = 0; row < 8; ++row) {
                Piece piece = game.getField(row, column).getPiece();
                if (piece != null) {
                    gridpane_board.add(new ImageView(piece.getImage()), row, column);
                }
            }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Watchdoggos Chess!");
        stage.setScene(scene);
        stage.show();
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

        // Ask players for their names
        String playerWhite = showPlayerNameDialog(Color.WHITE.name());
        if (playerWhite == null || playerWhite.isBlank()) return;
        String playerBlack = showPlayerNameDialog(Color.BLACK.name());
        if (playerBlack == null || playerBlack.isBlank()) return;

        game = new Game(
                // TODO Change to time from user input dialog
                new Player(Color.WHITE, playerWhite, new Timer(label_timer1, 15)),
                new Player(Color.BLACK, playerBlack, new Timer(label_timer2, 15))
        );
        label_player1.setText(playerWhite);
        label_player2.setText(playerBlack);

        setStartFormation();
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
        // TODO do something with dialog output
        // ButtonBar.ButtonData.YES.equals(result.getButtonData());
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

        // gets the clicked result; if the window is closed without a button being clicked it counts as "Ok"
        ButtonType result = resignGameDialog.showAndWait().orElse(ButtonType.YES);
    }

    public static void main(String[] args) {
        launch();
    }

}