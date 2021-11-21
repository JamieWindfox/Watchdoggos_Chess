package guiElements;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class NewGameButton extends Button {

    public NewGameButton() {
        setText("New Game");
        setWrapText(true);
        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // if there is a game running, ask if the players want to save it before starting a new one
                boolean createNewGame = buildSaveGameDialog();


            }
        });
    }

    private boolean buildSaveGameDialog() {
        Alert saveGameDialog = new Alert(Alert.AlertType.NONE);
        saveGameDialog.setTitle("Save current game");
        saveGameDialog.setContentText("Warning: The current game is still running.\nDo you want to save it before starting a new one?");

        // Set the buttons for the dialog
        saveGameDialog.getButtonTypes().setAll(
                new ButtonType("Yes", ButtonBar.ButtonData.YES),
                new ButtonType("No", ButtonBar.ButtonData.NO),
                new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)
        );

        // gets the clicked result; if the window is closed without a button being clicked it counts as "NO"
        ButtonType result = saveGameDialog.showAndWait().orElse(ButtonType.NO);

        if(ButtonType.YES.equals(result)) {
            // open filechooser to let the player save the game
            return true;
        }
        else if(ButtonType.NO.equals(result)) {
            return true;
        }
        return false;
    }

}
