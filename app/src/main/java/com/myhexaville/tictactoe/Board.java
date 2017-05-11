package com.myhexaville.tictactoe;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.myhexaville.tictactoe.model.Point;
import com.myhexaville.tictactoe.model.Shape;
import com.myhexaville.tictactoe.model.Victory;

import static android.graphics.Paint.Style.STROKE;
import static com.myhexaville.tictactoe.Constants.CIRCLE;
import static com.myhexaville.tictactoe.Constants.DIAGONAL_RISING;
import static com.myhexaville.tictactoe.Constants.HORIZONTAL;
import static com.myhexaville.tictactoe.Constants.NONE;
import static com.myhexaville.tictactoe.Constants.VERTICAL;

public class Board extends View {
    private static final String TAG = "CanvasBrushDrawing";
    public static final int BRUSH_SIZE_IN_DP = 24;
    public static final int SHAPE_SIZE_IN_DP = 200;
    public static final int LINE_SIZE_IN_DP = 16;
    public static final int HORIZONTAL_LINE_MARGIN_IN_DP = 16;
    public static final int DRAW_ANIMATION_SPEED = 500;
    public static final int AI_TURN_DELAY = 300;
    private int horizontalLineMargin;
    private int shapeSizeInPixels;

    private int MY_SHAPE = CIRCLE;
    private int AI_SHAPE = Constants.X;

    private Shape shape;
    private float v;

    private int width, height;
    private int topLeft;
    private int lineSize;
    private Paint line;
    private Point currentPoint = new Point();

    private int currentAnimationShape;

    private int[][] field = new int[3][3];

    private Rect zeroZero, zeroOne, zeroTwo, oneZero, oneOne, oneTwo, twoZero, twoOne, twoTwo;

    private Victory victory;
    private boolean isDrawing;
    private int victoryLineSize;
    private Paint victiryLine;
    private AI ai;
    private boolean aiTurn;

    public Board(Context context) {
        super(context);
    }

    public Board(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        lineSize = (int) (getResources().getDisplayMetrics().density * LINE_SIZE_IN_DP);
        horizontalLineMargin = (int) (getResources().getDisplayMetrics().density * HORIZONTAL_LINE_MARGIN_IN_DP);
        shape = new Shape(context);

        victiryLine = new Paint();
        victiryLine.setColor(Color.parseColor("#E53935"));
        victiryLine.setStyle(STROKE);
        victiryLine.setStrokeCap(Paint.Cap.ROUND);
        victoryLineSize = (int) (getResources().getDisplayMetrics().density * 20);
        victiryLine.setStrokeWidth(victoryLineSize);

        ai = new AI(MY_SHAPE, AI_SHAPE, field);
    }

    public Board(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Board(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        shapeSizeInPixels = (width - lineSize * 2) / 3;
        topLeft = (int) (height / 2 - shapeSizeInPixels * 1.5f - lineSize);

        line = new Paint();
        line.setStyle(STROKE);
        line.setColor(Color.parseColor("#e5e5e5"));
        line.setStrokeWidth(lineSize);
        line.setStrokeCap(Paint.Cap.ROUND);

        zeroZero = new Rect(0, topLeft, shapeSizeInPixels, topLeft + shapeSizeInPixels);
        zeroOne = new Rect(shapeSizeInPixels + lineSize, topLeft, shapeSizeInPixels * 2 + lineSize, topLeft + shapeSizeInPixels);
        zeroTwo = new Rect(shapeSizeInPixels * 2 + lineSize * 2, topLeft, shapeSizeInPixels * 3 + lineSize * 2, topLeft + shapeSizeInPixels);

        oneZero = new Rect(0, topLeft + lineSize + shapeSizeInPixels, shapeSizeInPixels, topLeft + lineSize + shapeSizeInPixels * 2);
        oneOne = new Rect(shapeSizeInPixels + lineSize, topLeft + lineSize + shapeSizeInPixels, shapeSizeInPixels * 2 + lineSize, topLeft + lineSize + shapeSizeInPixels * 2);
        oneTwo = new Rect(shapeSizeInPixels * 2 + lineSize * 2, topLeft + lineSize + shapeSizeInPixels, shapeSizeInPixels * 3 + lineSize * 2, topLeft + lineSize + shapeSizeInPixels * 2);

        twoZero = new Rect(0, topLeft + lineSize * 2 + shapeSizeInPixels * 2, shapeSizeInPixels, topLeft + lineSize * 2 + shapeSizeInPixels * 3);
        twoOne = new Rect(shapeSizeInPixels + lineSize, topLeft + lineSize * 2 + shapeSizeInPixels * 2, shapeSizeInPixels * 2 + lineSize, topLeft + lineSize * 2 + shapeSizeInPixels * 3);
        twoTwo = new Rect(shapeSizeInPixels * 2 + lineSize * 2, topLeft + lineSize * 2 + shapeSizeInPixels * 2, shapeSizeInPixels * 3 + lineSize * 2, topLeft + lineSize * 2 + shapeSizeInPixels * 3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLines(canvas);

        // square size is 300 pixels (hardcode value just for now)
        Rect r = getRect(currentPoint.x, currentPoint.y);

        if (currentAnimationShape != NONE) {
            shape.drawShape(currentAnimationShape, 0, canvas, r, v);
        }

        drawPreviousShapes(canvas);

        checkVictory(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (aiTurn || victory != null) {
            return true;
        }

        int x = (int) event.getX();
        int y = (int) event.getY();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (zeroZero.contains(x, y)) {
                drawShape(0, 0, MY_SHAPE);
            }
            if (zeroOne.contains(x, y)) {
                drawShape(0, 1, MY_SHAPE);
            }
            if (zeroTwo.contains(x, y)) {
                drawShape(0, 2, MY_SHAPE);
            }
            if (oneZero.contains(x, y)) {
                drawShape(1, 0, MY_SHAPE);
            }
            if (oneOne.contains(x, y)) {
                drawShape(1, 1, MY_SHAPE);
            }
            if (oneTwo.contains(x, y)) {
                drawShape(1, 2, MY_SHAPE);
            }
            if (twoZero.contains(x, y)) {
                drawShape(2, 0, MY_SHAPE);
            }
            if (twoOne.contains(x, y)) {
                drawShape(2, 1, MY_SHAPE);
            }
            if (twoTwo.contains(x, y)) {
                drawShape(2, 2, MY_SHAPE);
            }
        }
        return true;
    }

    private void drawPreviousShapes(Canvas canvas) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (field[row][col] != NONE) {
                    shape.drawShape(
                            field[row][col] == CIRCLE ? CIRCLE : Constants.X,
                            0,
                            canvas,
                            getRect(row, col),
                            1f);
                }
            }
        }
    }

    private void drawLines(Canvas canvas) {
        canvas.drawLine(shapeSizeInPixels + lineSize / 2, topLeft, shapeSizeInPixels + lineSize / 2, topLeft + shapeSizeInPixels * 3 + lineSize * 2, line);
        canvas.drawLine(shapeSizeInPixels * 2 + lineSize * 1.5f, topLeft, shapeSizeInPixels * 2 + lineSize * 1.5f, topLeft + shapeSizeInPixels * 3 + lineSize * 2, line);
        canvas.drawLine(horizontalLineMargin, topLeft + shapeSizeInPixels + lineSize / 2, width - horizontalLineMargin, topLeft + shapeSizeInPixels + lineSize / 2, line);
        canvas.drawLine(horizontalLineMargin, topLeft + shapeSizeInPixels * 2 + lineSize * 1.5f, width - horizontalLineMargin, topLeft + shapeSizeInPixels * 2 + lineSize * 1.5f, line);
    }

    @NonNull
    private Rect getRect(int row, int col) {
        Rect r = null;
        if (row == 0) {
            if (col == 0) {
                r = zeroZero;
            } else if (col == 1) {
                r = zeroOne;
            } else {
                r = zeroTwo;
            }
        } else if (row == 1) {
            if (col == 0) {
                r = oneZero;
            } else if (col == 1) {
                r = oneOne;
            } else {
                r = oneTwo;
            }
        } else if (row == 2) {
            if (col == 0) {
                r = twoZero;
            } else if (col == 1) {
                r = twoOne;
            } else {
                r = twoTwo;
            }
        }
        return r;
    }

    private void drawShape(int row, int col, int shapeType) {
        if (field[row][col] != NONE || isDrawing) {
            return;
        }

        isDrawing = true;
        startAnimation(row, col, shapeType == CIRCLE);
        currentPoint.x = row;
        currentPoint.y = col;
    }

    private void startAnimation(int row, int col, boolean isCircle) {
        currentAnimationShape = isCircle ? CIRCLE : Constants.X;
        ValueAnimator animator = ObjectAnimator.ofFloat(0f, 1f).setDuration(DRAW_ANIMATION_SPEED);
        animator.addUpdateListener(animation -> {
            v = (float) animation.getAnimatedValue();

            invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                currentAnimationShape = NONE;
                field[row][col] = isCircle ? CIRCLE : Constants.X;
                victory = VictoryChecker.checkForVictory(field, MY_SHAPE);
                if (victory != null) {
                    Log.d(TAG, "onAnimationEnd: " + victory.toString());
                    invalidate();
                }
                isDrawing = false;

                aiTurn = !aiTurn;
                if (aiTurn && victory == null) {
                    ai.makeDecision();
                    new Handler().postDelayed(() -> {
                        drawShape(ai.getRow(), ai.getCol(), AI_SHAPE);
                    }, AI_TURN_DELAY);
                }
            }
        });
        animator.start();
    }

    //todo implement rest of lines
    private void checkVictory(Canvas canvas) {
        if (victory != null) {
            if (victory.lineType == HORIZONTAL) {
                if (victory.row == 0) {
                    canvas.drawLine(horizontalLineMargin, topLeft + shapeSizeInPixels / 2 - victoryLineSize / 2, width - horizontalLineMargin, topLeft + shapeSizeInPixels / 2 - victoryLineSize / 2, victiryLine);
                } else if (victory.row == 1) {
                    canvas.drawLine(horizontalLineMargin, topLeft + shapeSizeInPixels * 1.5f - victoryLineSize / 2 + lineSize, width - horizontalLineMargin, topLeft + shapeSizeInPixels * 1.5f - victoryLineSize / 2 + lineSize, victiryLine);
                } else {
                    canvas.drawLine(horizontalLineMargin, topLeft + shapeSizeInPixels * 2.5f - victoryLineSize / 2 + lineSize * 2, width - horizontalLineMargin, topLeft + shapeSizeInPixels * 2.5f - victoryLineSize / 2 + lineSize * 2, victiryLine);
                }
            } else if (victory.lineType == VERTICAL) {
                if (victory.col == 0) {

                } else if (victory.col == 1) {

                } else {

                }
            } else if (victory.lineType == DIAGONAL_RISING) {

            } else {

            }
        }
    }

}