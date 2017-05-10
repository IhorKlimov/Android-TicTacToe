package com.myhexaville.tictactoe.model;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.myhexaville.tictactoe.R;
import com.myhexaville.tictactoe.model.Constants;
import com.myhexaville.tictactoe.model.Shape;
import com.myhexaville.tictactoe.model.Vector2;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Paint.Style.STROKE;
import static com.myhexaville.tictactoe.model.Constants.CIRCLE;

public class Board extends View {
    private static final String LOG_TAG = "CanvasBrushDrawing";
    public static final int BRUSH_SIZE_IN_DP = 24;

    private List<Vector2> inputPoints = new ArrayList<>(100);
    private Bitmap bitmapBrush;
    private Paint boundaries;
    private Rect src;
    private int brushSize;
    private Rect dest;
    private Shape shape;
    private float v;

    private boolean animateCircle, drawnCircle;
    private boolean animateX, drawnX;

    public Board(Context context) {
        super(context);
    }

    public Board(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmapBrush = BitmapFactory.decodeResource(context.getResources(), R.drawable.brush2);

        boundaries = new Paint();
        boundaries.setStyle(STROKE);
        boundaries.setStrokeWidth(getResources().getDisplayMetrics().density * 4);
        boundaries.setColor(Color.parseColor("#000000"));

        src = new Rect(0, 0, bitmapBrush.getWidth() - 1, bitmapBrush.getHeight() - 1);
        brushSize = (int) (getResources().getDisplayMetrics().density * BRUSH_SIZE_IN_DP);
        dest = new Rect();

        shape = new Shape(context);
    }

    public Board(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Board(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(100, 100, 800, 800, boundaries);

        for (Vector2 pos : inputPoints) {
            dest.set((int) pos.x - brushSize, (int) pos.y - brushSize, (int) pos.x + brushSize, (int) pos.y + brushSize);
            canvas.drawBitmap(bitmapBrush, src, dest, null);
        }


        // square size is 300 pixels (hardcode value just for now)
        if (animateCircle) {
            shape.drawShape(CIRCLE, 0, canvas, new Rect(100, 1200, 400, 1500), v);
        } else if (drawnCircle) {
            shape.drawShape(CIRCLE, 0, canvas, new Rect(100, 1200, 400, 1500), 1f);
        }

        if (animateX) {
            shape.drawShape(Constants.X, 0, canvas, new Rect(700, 1200, 1000, 1500), v);
        } else if (drawnX) {
            shape.drawShape(Constants.X, 0, canvas, new Rect(700, 1200, 1000, 1500), 1f);
        }
    }


    public void drawCircle() {
        animateCircle = true;
        startAnimation(true);
    }

    public void drawX() {
        animateX = true;
        startAnimation(false);
    }

    private void startAnimation(boolean isCircle) {
        ValueAnimator animator = ObjectAnimator.ofFloat(0f, 1f).setDuration(1000);
        animator.addUpdateListener(animation -> {
            v = (float) animation.getAnimatedValue();

            invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isCircle) {
                    animateCircle = false;
                    drawnCircle = true;
                } else {
                    animateX = false;
                    drawnX = true;
                }
            }
        });
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final float posX = event.getX();
                final float posY = event.getY();
                inputPoints.add(new Vector2(posX, posY));
                invalidate();
        }

        return true;
    }

    public void print() {
        for (int i = 0; i < inputPoints.size(); i++) {
            Vector2 v = inputPoints.get(i);
            Log.d(LOG_TAG, "print: " + i + " " + v.x + " " + v.y);
        }
    }
}