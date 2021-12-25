package com.example.chess_game;

import guiElements.NewGameButton;
import guiElements.ResignGameButton;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChessGameMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        HBox hBox = new HBox();
        Button newGameButton = new NewGameButton();
        Button resignGameButton = new ResignGameButton();
        hBox.getChildren().addAll(newGameButton, resignGameButton);

        Scene scene = new Scene(hBox, 320, 240);
        stage.setTitle("Watchdoggos Chess!");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}