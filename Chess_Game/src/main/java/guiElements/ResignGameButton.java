package guiElements;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class ResignGameButton extends Button {

    private boolean resignGame = false;

    public ResignGameButton() {
        setText("Resign");
        setWrapText(true);
        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("INFO: Player clicked on 'Resign'");
                // if there is a game running, ask if the player really wants to resign
                buildResignGameDialog();

            }
        });
    }

    public  boolean buildResignGameDialog() {
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
        resignGame = ButtonBar.ButtonData.YES.equals(result.getButtonData());
        return resignGame;
    }
}
