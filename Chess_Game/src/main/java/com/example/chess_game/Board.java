package com.example.chess_game;

public class Board {

    private Field[][] fields;
    private static final int ASCII_OFFSET = 97;

    public Board() {
        initFields();
    }

    private void initFields() {
        this.fields = new Field[8][8];
        // Starting with field a8 top-left
        for (int rowNum = 7; rowNum >= 0; rowNum--) {
            for (int colAlphabet = 0; colAlphabet < 8; colAlphabet++) {
                this.fields[7 - rowNum][colAlphabet]
                        = new Field((char) (colAlphabet + ASCII_OFFSET) + Integer.toString(rowNum + 1), colAlphabet, rowNum);
            }
        }
    }

    public Field[][] getFields() {
        return this.fields;
    }
}
