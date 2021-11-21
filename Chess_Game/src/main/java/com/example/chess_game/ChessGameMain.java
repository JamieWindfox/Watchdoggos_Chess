package com.example.chess_game;

import guiElements.NewGameButton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChessGameMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        StackPane pane = new StackPane();
        NewGameButton newGameButton = new NewGameButton();
        pane.getChildren().add(newGameButton);

        Scene scene = new Scene(pane, 320, 240);
        stage.setTitle("Watchdoggos Chess!");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}