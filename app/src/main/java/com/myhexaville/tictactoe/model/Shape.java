package com.myhexaville.tictactoe.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.myhexaville.tictactoe.R;

import java.util.List;

import static android.graphics.Paint.Style.STROKE;
import static com.myhexaville.tictactoe.model.Constants.CIRCLE;
import static com.myhexaville.tictactoe.model.Constants.circleOnePoints;
import static com.myhexaville.tictactoe.model.Constants.circleTwoPoints;
import static com.myhexaville.tictactoe.model.Constants.xOnePoints;
import static java.lang.StrictMath.min;

public class Shape {
    private static final String LOG_TAG = "Shape";
    public static final int BRUSH_SIZE_IN_DP = 12;

    private final Bitmap bitmapBrush;
    private final int brushSize;
    private final Rect src;
    private final Rect dest;

    public Shape(Context context) {
        bitmapBrush = BitmapFactory.decodeResource(context.getResources(), R.drawable.brush2);

        Paint chalkPaint = new Paint();
        chalkPaint.setStyle(STROKE);
        chalkPaint.setStrokeWidth(100);
        Bitmap chalkShader = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.brush2)).getBitmap();
        chalkPaint.setShader(new BitmapShader(chalkShader, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));

        src = new Rect(0, 0, chalkShader.getWidth() - 1, chalkShader.getHeight() - 1);
        brushSize = (int) (context.getResources().getDisplayMetrics().density * BRUSH_SIZE_IN_DP);
        dest = new Rect();

        Paint boundaries = new Paint();
        boundaries.setStyle(STROKE);
        boundaries.setStrokeWidth(context.getResources().getDisplayMetrics().density * 4);
        boundaries.setColor(Color.parseColor("#000000"));
    }

    public void drawShape(int shapeType, int subType, Canvas canvas, Rect inside, float fraction) {
        List<Vector2> points;
        if (shapeType == CIRCLE) {
            if (subType == 0) {
                points = circleOnePoints;
            } else {
                points = circleTwoPoints;
            }
        } else {
            points = xOnePoints;
        }

        int point = (int) (points.size() * fraction);
        Log.d(LOG_TAG, "drawShape: " + point);

        for (int i = 0; i <= point; i++) {
            Vector2 fr = points.get(min(points.size() - 1, i));
            int x = (int) ((inside.right - inside.left) * fr.x + inside.left);
            int y = (int) ((inside.bottom - inside.top) * fr.y + inside.top);
            dest.set(
                    x - brushSize,
                    y - brushSize,
                    x + brushSize,
                    y + brushSize);
            canvas.drawBitmap(bitmapBrush, src, dest, null);
        }
    }

}
