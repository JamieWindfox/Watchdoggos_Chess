package com.example.chess_game;

import guiElements.NewGameButton;
import guiElements.ResignGameButton;
import guiElements.ShowPreviousMovesButton;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChessGameMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        GridPane pane = new GridPane();
        Button newGameButton = new NewGameButton();
        Button resignGameButton = new ResignGameButton();

        HBox bottomPanel = new HBox();
        Button prevMovesButton = new ShowPreviousMovesButton();

        pane.add(newGameButton, 0, 0);
        pane.add(resignGameButton, 1, 0);
        pane.add(prevMovesButton, 2, 2);

        Scene scene = new Scene(pane, 320, 240);
        stage.setTitle("Watchdoggos Chess!");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}