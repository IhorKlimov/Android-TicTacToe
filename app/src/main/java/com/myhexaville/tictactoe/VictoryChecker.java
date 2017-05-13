package com.myhexaville.tictactoe;

import android.support.annotation.Nullable;
import android.util.Log;

import com.myhexaville.tictactoe.model.Victory;

import static com.myhexaville.tictactoe.Constants.DIAGONAL_FALLING;
import static com.myhexaville.tictactoe.Constants.DIAGONAL_RISING;
import static com.myhexaville.tictactoe.Constants.DRAFT;
import static com.myhexaville.tictactoe.Constants.ENEMY;
import static com.myhexaville.tictactoe.Constants.HORIZONTAL;
import static com.myhexaville.tictactoe.Constants.ME;
import static com.myhexaville.tictactoe.Constants.NONE;
import static com.myhexaville.tictactoe.Constants.VERTICAL;

public class VictoryChecker {
    private static final String TAG = "VictoryChecker";

    @Nullable
    public static Victory checkForVictory(int[][] field, int myShapeType) {
        Victory v = null;
        //check horizontal lines
        if (field[0][0] != Constants.NONE && field[0][0] == field[0][1] && field[0][0] == field[0][2]) {
            Log.d(TAG, "checkForVictory: ");
            v = new Victory(0, 0, HORIZONTAL, field[0][0] == myShapeType ? ME : ENEMY);
        } else if (field[1][0] != Constants.NONE && field[1][0] == field[1][1] && field[1][0] == field[1][2]) {
            v = new Victory(1, 0, HORIZONTAL, field[1][0] == myShapeType ? ME : ENEMY);
        } else if (field[2][0] != Constants.NONE && field[2][0] == field[2][1] && field[2][0] == field[2][2]) {
            v = new Victory(2, 0, HORIZONTAL, field[2][0] == myShapeType ? ME : ENEMY);
        }

        //check vertical lines
        else if (field[0][0] != Constants.NONE && field[0][0] == field[1][0] && field[0][0] == field[2][0]) {
            v = new Victory(0, 0, VERTICAL, field[0][0] == myShapeType ? ME : ENEMY);
        } else if (field[0][1] != Constants.NONE && field[0][1] == field[1][1] && field[0][1] == field[2][1]) {
            v = new Victory(0, 1, VERTICAL, field[0][1] == myShapeType ? ME : ENEMY);
        } else if (field[0][2] != Constants.NONE && field[0][2] == field[1][2] && field[0][2] == field[2][2]) {
            v = new Victory(0, 2, VERTICAL, field[0][2] == myShapeType ? ME : ENEMY);
        }

        //check diagonal
        else if (field[0][0] != Constants.NONE && field[0][0] == field[1][1] && field[0][0] == field[2][2]) {
            v = new Victory(0, 0, DIAGONAL_FALLING, field[0][0] == myShapeType ? ME : ENEMY);
        } else if (field[2][0] != Constants.NONE && field[2][0] == field[1][1] && field[2][0] == field[0][2]) {
            v = new Victory(2, 0, DIAGONAL_RISING, field[2][0] == myShapeType ? ME : ENEMY);
        } else if (field[0][0] != NONE
                && field[0][1] != NONE
                && field[0][2] != NONE
                && field[1][0] != NONE
                && field[1][1] != NONE
                && field[1][2] != NONE
                && field[2][0] != NONE
                && field[2][1] != NONE
                && field[2][2] != NONE) {
            v = new Victory(-1, -1, -1, DRAFT);
        }

            return v;
    }
}
