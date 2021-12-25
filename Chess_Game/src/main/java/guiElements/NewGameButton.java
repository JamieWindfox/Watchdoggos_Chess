package guiElements;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class NewGameButton extends Button {

    private boolean createNewGame = false;

    public NewGameButton() {
        setText("New Game");
        setWrapText(true);
        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("INFO: Player clicked on 'New Game'");
                // if there is a game running, ask if the players want to resign the current one
                buildDiscardGameDialog();
            }
        });
    }

    private boolean buildDiscardGameDialog() {
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
        createNewGame = ButtonBar.ButtonData.YES.equals(result.getButtonData());
        return createNewGame;
    }

    public boolean isCreateNewGame() {
        return createNewGame;
    }

}
