module com.example.chess_game {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.chess_game to javafx.fxml;
    exports com.example.chess_game;
}