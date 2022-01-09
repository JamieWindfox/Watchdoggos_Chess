package com.example.chess_game;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends Application implements Initializable {

    Game game;

    @FXML private FlowPane flowpane_cemetary1;
    @FXML private FlowPane flowpane_cemetary2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game = new Game();
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
        if(ButtonBar.ButtonData.YES.equals(result.getButtonData())) game = new Game();
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

        List<String> moves = game.getMoves();
        TextFlow textFlow = new TextFlow();
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