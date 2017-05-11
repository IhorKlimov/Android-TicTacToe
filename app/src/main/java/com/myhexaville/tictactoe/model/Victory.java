package com.myhexaville.tictactoe.model;

public class Victory {
    public int row, col, lineType;
    public boolean isMe;

    public Victory(int row, int col, int lineType, boolean isMe) {
        this.row = row;
        this.col = col;
        this.lineType = lineType;
        this.isMe = isMe;
    }

    @Override
    public String toString() {
        return "Victory{" +
                "row=" + row +
                ", col=" + col +
                ", lineType=" + lineType +
                ", isMe=" + isMe +
                '}';
    }
}
