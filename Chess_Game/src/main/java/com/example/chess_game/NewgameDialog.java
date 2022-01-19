package com.example.chess_game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class NewgameDialog {

    private Stage stage;
    private boolean dialogResult = false;

    @FXML TextField textfield_player_white;
    @FXML TextField textfield_player_black;
    @FXML TextField textfield_minutes_per_player;

    public void showDialog(Parent root) {
        System.out.println("PROMO - SHOW DIALOG");

        stage = new Stage(StageStyle.TRANSPARENT);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);

        // Dirty Hack because textfield is not ready for focus yet
        // https://stackoverflow.com/a/38900429/1592803
        Platform.runLater(()->textfield_player_white.requestFocus());

        stage.showAndWait();
    }

    @FXML
    private void btnNewGameClicked() {
        // Validate Player Names
        String playerWhiteName = textfield_player_white.getText();
        if (playerWhiteName.isBlank() || playerWhiteName.length() > 20) {
            showWrongInputDialog("White Player Name must not be empty or longer than 20 characters.");
            return;
        }

        String playerBlackName = textfield_player_black.getText();
        if (playerBlackName.isBlank() || playerBlackName.length() > 20) {
            showWrongInputDialog("Black Player Name must not be empty or longer than 20 characters.");
            return;
        }

        // Validate Playing Time
        String sMinutesPerPlayer = textfield_minutes_per_player.getText();
        if (sMinutesPerPlayer.isBlank() || !sMinutesPerPlayer.chars().allMatch(Character::isDigit)) {
            showWrongInputDialog("Minutes Per Player must not be empty and has to be numeric.");
            return;
        }

        int minutesPerPlayer = Integer.parseInt(sMinutesPerPlayer);
        if (minutesPerPlayer < 1 || minutesPerPlayer > 120) {
            showWrongInputDialog("Minutes Per Player must be greater than 0 and cannot be greater than 120.");
            return;
        }

        dialogResult = true;
        stage.close();
    }

    @FXML
    private void btnCancelClick() {
        stage.close();
    }

    private void showWrongInputDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Wrong input");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public boolean getDialogResult() {
        return dialogResult;
    }
}
