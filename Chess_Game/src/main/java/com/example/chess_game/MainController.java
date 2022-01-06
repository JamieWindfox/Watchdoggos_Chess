package com.example.chess_game;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends Application implements Initializable {

    Game game;

    @FXML
    private ImageView imageview_board;

    @FXML
    private Button button_new_game;

    @FXML
    private Button button_resign;

    @FXML
    private Button button_previous_moves;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game = new Game();

        imageview_board.setImage(game.getBoardImage());

        button_new_game.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("INFO: Player clicked on 'New Game'");
                // ask if the player really wants to start a new game
                if (showDiscardGameDialog()) game = new Game();
            }
        });

        button_resign.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("INFO: Player clicked on 'Resign'");
                // if there is a game running, ask if the player really wants to resign
                showResignGameDialog();
            }
        });

        button_previous_moves.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("INFO: Player clicked on 'Show Previous Moves'");
                showPreviousMovesDialog();
            }
        });

    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Watchdoggos Chess!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    private boolean showDiscardGameDialog() {
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
        return ButtonBar.ButtonData.YES.equals(result.getButtonData());
    }

    public  boolean showResignGameDialog() {
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
        return ButtonBar.ButtonData.YES.equals(result.getButtonData());
    }

    private void showPreviousMovesDialog() {
        Alert resignGameDialog = new Alert(Alert.AlertType.NONE);
        resignGameDialog.setTitle("Previous Moves");
        resignGameDialog.setContentText("PLACEHOLDER\nFOR\nPREVIOUS\nMOVES");

        // Set the buttons for the dialog
        resignGameDialog.getButtonTypes().setAll(
                new ButtonType("Ok", ButtonBar.ButtonData.YES)
        );

        // gets the clicked result; if the window is closed without a button being clicked it counts as "Ok"
        ButtonType result = resignGameDialog.showAndWait().orElse(ButtonType.YES);
    }
}