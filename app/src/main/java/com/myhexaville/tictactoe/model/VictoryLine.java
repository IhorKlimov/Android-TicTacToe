package com.myhexaville.tictactoe.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static android.graphics.Paint.Style.STROKE;
import static com.myhexaville.tictactoe.Constants.DIAGONAL_RISING;
import static com.myhexaville.tictactoe.Constants.HORIZONTAL;
import static com.myhexaville.tictactoe.Constants.VERTICAL;

public class VictoryLine {
    private final int topLeft;
    private final int horizontalLineMargin;
    private final int shapeSizeInPixels;
    private final int width;
    private final int lineSize;
    private final Paint victoryLine;
    private int victoryLineSize;

    public VictoryLine(Context context, int topLeft, int horizontalLineMargin, int shapeSizeInPixels, int width, int lineSize) {
        this.topLeft = topLeft;
        this.horizontalLineMargin = horizontalLineMargin;
        this.shapeSizeInPixels = shapeSizeInPixels;
        this.width = width;
        this.lineSize = lineSize;

        victoryLine = new Paint();
        victoryLine.setColor(Color.parseColor("#E53935"));
        victoryLine.setStyle(STROKE);
        victoryLine.setStrokeCap(Paint.Cap.ROUND);
        victoryLineSize = (int) (context.getResources().getDisplayMetrics().density * 20);
        victoryLine.setStrokeWidth(victoryLineSize);
    }

    public void draw(Canvas canvas, Victory victory) {
        if (victory.lineType == HORIZONTAL) {
            if (victory.row == 0) {
                canvas.drawLine(horizontalLineMargin, topLeft + shapeSizeInPixels / 2 - victoryLineSize / 2, width - horizontalLineMargin, topLeft + shapeSizeInPixels / 2 - victoryLineSize / 2, victoryLine);
            } else if (victory.row == 1) {
                canvas.drawLine(horizontalLineMargin, topLeft + shapeSizeInPixels * 1.5f - victoryLineSize / 2 + lineSize, width - horizontalLineMargin, topLeft + shapeSizeInPixels * 1.5f - victoryLineSize / 2 + lineSize, victoryLine);
            } else {
                canvas.drawLine(horizontalLineMargin, topLeft + shapeSizeInPixels * 2.5f - victoryLineSize / 2 + lineSize * 2, width - horizontalLineMargin, topLeft + shapeSizeInPixels * 2.5f - victoryLineSize / 2 + lineSize * 2, victoryLine);
            }
        } else if (victory.lineType == VERTICAL) {
            if (victory.col == 0) {
                canvas.drawLine(shapeSizeInPixels / 2 + lineSize / 2, topLeft, shapeSizeInPixels / 2 + lineSize / 2, topLeft + shapeSizeInPixels * 3 + lineSize * 2, victoryLine);
            } else if (victory.col == 1) {
                canvas.drawLine(shapeSizeInPixels * 1.5f + lineSize, topLeft, shapeSizeInPixels * 1.5f + lineSize, topLeft + shapeSizeInPixels * 3 + lineSize * 2, victoryLine);
            } else {
                canvas.drawLine(shapeSizeInPixels * 2.5f + lineSize * 1.5f, topLeft, shapeSizeInPixels * 2.5f + lineSize * 1.5f, topLeft + shapeSizeInPixels * 3 + lineSize * 2, victoryLine);
            }
        } else if (victory.lineType == DIAGONAL_RISING) {
            canvas.drawLine(horizontalLineMargin, topLeft + shapeSizeInPixels * 3 + lineSize * 2 - lineSize/2, width-horizontalLineMargin, topLeft + lineSize/2, victoryLine);
        } else {
            canvas.drawLine(horizontalLineMargin, topLeft + lineSize/2, width-horizontalLineMargin, topLeft + shapeSizeInPixels * 3 + lineSize * 2 - lineSize/2, victoryLine);
        }
    }
}
