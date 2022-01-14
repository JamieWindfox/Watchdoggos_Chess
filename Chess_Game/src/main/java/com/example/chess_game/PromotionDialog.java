package com.example.chess_game;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PromotionDialog implements Initializable {

    private Color pieceColor;

    @FXML ImageView imageview_queen;
    @FXML ImageView imageview_knight;
    @FXML ImageView imageview_rook;
    @FXML ImageView imageview_bishop;

    public void showDialog(AnchorPane pane) throws IOException {
        System.out.println("SHOW DIALOG");

        Stage dialogStage = new Stage(StageStyle.TRANSPARENT);
        dialogStage.setScene(new Scene(pane));
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        dialogStage.showAndWait();
    }

    public void setPieceColor(Color pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("INITIALIZE");

        String urlPrefix = "graphics/" + pieceColor.name().toLowerCase() +  "_";
        String urlSuffix = ".png";
        imageview_queen.setImage(new Image(urlPrefix + "queen" + urlSuffix));
        imageview_knight.setImage(new Image(urlPrefix + "knight" + urlSuffix));
        imageview_rook.setImage(new Image(urlPrefix + "rook" + urlSuffix));
        imageview_bishop.setImage(new Image(urlPrefix + "bishop" + urlSuffix));
    }
}
