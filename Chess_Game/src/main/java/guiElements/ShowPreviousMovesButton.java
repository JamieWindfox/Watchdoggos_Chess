package guiElements;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class ShowPreviousMovesButton extends Button {

    public ShowPreviousMovesButton() {
        setText("Show Previous Moves");
        setWrapText(true);
        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("INFO: Player clicked on 'Show Previous Moves'");
                showPreviousMovesDialog();
            }
        });
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
