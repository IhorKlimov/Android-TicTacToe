package com.myhexaville.tictactoe;

import static com.myhexaville.tictactoe.Constants.NONE;

public class AI {
    private int[] opponentsChoice = new int[2];
    private int playersChar;
    private int opponentChar;
    private int[][] field;

    public AI(int playersShape, int aiShape, int field[][]) {
        this.playersChar = playersShape;
        this.opponentChar = aiShape;
        this.field = field;
    }

    public void makeDecision() {
        if (isCenterEmpty()) return;
        if (aiIsCloseToWin()) return;
        if (playerIsCloseToWin()) return;
        if (opponentHasOneChar()) return;
        chooseRandom();
    }

    private boolean isCenterEmpty() {
        if (field[1][1] == NONE) {
            opponentsChoice[0] = 1;
            opponentsChoice[1] = 1;
            return true;
        }
        return false;
    }

    private boolean playerIsCloseToWin() {
        return hasTwoCharsInLine(0, 0, 0, 1, 0, 2, playersChar)
                || hasTwoCharsInLine(1, 0, 1, 1, 1, 2, playersChar)
                || hasTwoCharsInLine(2, 0, 2, 1, 2, 2, playersChar)
                || hasTwoCharsInLine(0, 0, 1, 0, 2, 0, playersChar)
                || hasTwoCharsInLine(0, 1, 1, 1, 2, 1, playersChar)
                || hasTwoCharsInLine(0, 2, 1, 2, 2, 2, playersChar)
                || hasTwoCharsInLine(0, 0, 1, 1, 2, 2, playersChar)
                || hasTwoCharsInLine(0, 2, 1, 1, 2, 0, playersChar);
    }

    private boolean aiIsCloseToWin() {
        return hasTwoCharsInLine(0, 0, 0, 1, 0, 2, opponentChar)
                || hasTwoCharsInLine(1, 0, 1, 1, 1, 2, opponentChar)
                || hasTwoCharsInLine(2, 0, 2, 1, 2, 2, opponentChar)
                || hasTwoCharsInLine(0, 0, 1, 0, 2, 0, opponentChar)
                || hasTwoCharsInLine(0, 1, 1, 1, 2, 1, opponentChar)
                || hasTwoCharsInLine(0, 2, 1, 2, 2, 2, opponentChar)
                || hasTwoCharsInLine(0, 0, 1, 1, 2, 2, opponentChar)
                || hasTwoCharsInLine(0, 2, 1, 1, 2, 0, opponentChar);
    }

    private boolean hasTwoCharsInLine(int r1, int c1, int r2, int c2, int r3, int c3, int side) {
        if (field[r1][c1] == side && field[r2][c2] == side && field[r3][c3] == NONE) {
            opponentsChoice[0] = r3;
            opponentsChoice[1] = c3;
            return true;
        }
        if (field[r1][c1] == side && field[r3][c3] == side && field[r2][c2] == NONE) {
            opponentsChoice[0] = r2;
            opponentsChoice[1] = c2;
            return true;
        }
        if (field[r2][c2] == side && field[r3][c3] == side && field[r1][c1] == NONE) {
            opponentsChoice[0] = r1;
            opponentsChoice[1] = c1;
            return true;
        }
        return false;
    }

    private boolean opponentHasOneChar() {
        return opponentHasOneChar(0, 0, 0, 1, 0, 2)
                || opponentHasOneChar(1, 0, 1, 1, 1, 2)
                || opponentHasOneChar(2, 0, 2, 1, 2, 2)
                || opponentHasOneChar(0, 0, 1, 0, 2, 0)
                || opponentHasOneChar(0, 1, 1, 1, 2, 1)
                || opponentHasOneChar(0, 2, 1, 2, 2, 2)
                || opponentHasOneChar(0, 0, 1, 1, 2, 2)
                || opponentHasOneChar(0, 2, 1, 1, 2, 0);
    }

    @SuppressWarnings("WrongConstant")
    private boolean opponentHasOneChar(int r1, int c1, int r2, int c2, int r3, int c3) {
        if (field[r1][c1] == opponentChar && field[r2][c2] == NONE && field[r3][c3] == NONE) {
            opponentsChoice[0] = r3;
            opponentsChoice[1] = c3;
            return true;
        }
        if (field[r2][c2] == opponentChar && field[r1][c1] == NONE && field[r3][c3]==NONE) {
            opponentsChoice[0] = r1;
            opponentsChoice[1] = c1;
            return true;
        }
        if (field[r3][c3] == opponentChar && field[r1][c1] == NONE&& field[r2][c2] == NONE) {
            opponentsChoice[0] = r1;
            opponentsChoice[1] = c1;
            return true;
        }
        return false;
    }

    private void chooseRandom() {
        while (true) {
            int r = (int) (Math.random() * 3);
            int c = (int) (Math.random() * 3);
            if (field[r][c] == NONE) {
                opponentsChoice[0] = r;
                opponentsChoice[1] = c;
                break;
            }
        }
    }

    public int getRow() {
        return opponentsChoice[0];
    }

    public int getCol() {
        return opponentsChoice[1];
    }

}