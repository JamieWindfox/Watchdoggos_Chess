package com.example.chess_game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ChessTimer {

    private final Label timerLabel;
    private Timeline timeline;
    private int totalSeconds;
    private int timeLeft;

    public ChessTimer(Label timerLabel, int totalMinutes) {
        this.timerLabel = timerLabel;
        this.totalSeconds = totalMinutes * 60;
        this.timeLeft = totalSeconds;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                event -> cowntdown()));
        timeline.setCycleCount(totalSeconds);
    }

    private void cowntdown() {
        timeLeft--;
        displayTimer();
    }

    private void displayTimer() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerLabel.setText(String.format("Time left: %d:%02d", minutes, seconds));
    }

    public void start() {
        timerLabel.setTextFill(Color.YELLOW);
        timeline.play();
    }

    public void stop() {
        timerLabel.setTextFill(Color.WHITE);
        timeline.stop();
    }






}
