module com.example.chess_game {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;


    opens com.example.chess_game to javafx.fxml;
    exports com.example.chess_game;
    exports com.example.chess_game.pieces;
    opens com.example.chess_game.pieces to javafx.fxml;
}