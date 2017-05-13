package com.myhexaville.tictactoe.model;

public class Victory {
    public int row, col, lineType;
    public int winner;

    public Victory(int row, int col, int lineType, int winner) {
        this.row = row;
        this.col = col;
        this.lineType = lineType;
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "Victory{" +
                "row=" + row +
                ", col=" + col +
                ", lineType=" + lineType +
                ", winner=" + winner +
                '}';
    }
}
