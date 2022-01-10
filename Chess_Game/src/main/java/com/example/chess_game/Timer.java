package com.example.chess_game;

// Source 07.01.22, inspired by: https://github.com/grzegorz103/chess-clock/blob/master/src/main/java/app/models/Timer.java

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Timer implements Runnable {

    // Each Player stars with 15 minutes left
    private int timeLeft;
    private final Label timer;

    public Timer(Label timerLabel, int totalMinutes) {
        timer = timerLabel;
        timeLeft = totalMinutes * 60;
        updateTimer();
    }

    private void updateTimer() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timer.setText(String.format("Time left: %d:%02d", minutes, seconds));
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            synchronized (this) {
                timeLeft --;

                Platform.runLater(this::updateTimer);
            }
            try {
                // wait exactly one second
                Thread.sleep(1000);
            }
            catch (InterruptedException ex) {
                System.err.println("An error happened during waiting for timer: " + ex.getMessage());
            }
            if(timeLeft <= 0) {
                running = false;
            }
        }
        //Run function for next player turn
        //Platform.runLater();
    }


}
