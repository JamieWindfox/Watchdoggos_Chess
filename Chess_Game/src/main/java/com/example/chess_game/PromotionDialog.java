package com.example.chess_game;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
    private Piece dialogResultPiece = null;
    private Stage stage;

    @FXML ImageView imageview_queen;
    @FXML ImageView imageview_knight;
    @FXML ImageView imageview_rook;
    @FXML ImageView imageview_bishop;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("PROMO - INITIALIZE");
    }

    public void showDialog(Parent root) {
        System.out.println("PROMO - SHOW DIALOG");

        stage = new Stage(StageStyle.TRANSPARENT);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();
    }

    public void setPieceColor(Color pieceColor) {
        System.out.println("PROMO - setPieceColor (" + pieceColor.name() + ")");
        this.pieceColor = pieceColor;

        String urlPrefix = "graphics/" + pieceColor.name().toLowerCase() +  "_";
        String urlSuffix = ".png";
        imageview_queen.setImage(new Image(urlPrefix + "queen" + urlSuffix));
        imageview_knight.setImage(new Image(urlPrefix + "knight" + urlSuffix));
        imageview_rook.setImage(new Image(urlPrefix + "rook" + urlSuffix));
        imageview_bishop.setImage(new Image(urlPrefix + "bishop" + urlSuffix));
    }

    @FXML public void queenClicked() {
        System.out.println("PROMO - Queen clicked");
        dialogResultPiece = new Queen(pieceColor);
        stage.close();
    }

    @FXML public void knightClicked() {
        System.out.println("PROMO - Knight clicked");
        dialogResultPiece = new Knight(pieceColor);
        stage.close();
    }

    @FXML public void rookClicked() {
        System.out.println("PROMO - Rook clicked");
        dialogResultPiece = new Rook(pieceColor);
        stage.close();
    }

    @FXML public void bishopClicked() {
        System.out.println("PROMO - Bishop clicked");
        dialogResultPiece = new Bishop(pieceColor);
        stage.close();
    }

    public Piece getPiece() {
        System.out.println("PROMO - Returning Piece");
        return dialogResultPiece;
    }
}
