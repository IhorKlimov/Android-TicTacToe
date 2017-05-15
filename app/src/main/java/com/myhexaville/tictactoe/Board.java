package com.myhexaville.tictactoe;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.myhexaville.tictactoe.model.Point;
import com.myhexaville.tictactoe.model.Shape;
import com.myhexaville.tictactoe.model.Victory;
import com.myhexaville.tictactoe.model.VictoryLine;

import static android.graphics.Paint.Style.STROKE;
import static com.myhexaville.tictactoe.Constants.CIRCLE;
import static com.myhexaville.tictactoe.Constants.DRAFT;
import static com.myhexaville.tictactoe.Constants.ENEMY;
import static com.myhexaville.tictactoe.Constants.ME;
import static com.myhexaville.tictactoe.Constants.NONE;
import static com.myhexaville.tictactoe.Constants.X;
import static com.myhexaville.tictactoe.Util.getCurrentUserId;

public class Board extends FrameLayout {
    private static final String TAG = "CanvasBrushDrawing";
    public static final int BRUSH_SIZE_IN_DP = 24;
    public static final int SHAPE_SIZE_IN_DP = 200;
    public static final int LINE_SIZE_IN_DP = 16;
    public static final int HORIZONTAL_LINE_MARGIN_IN_DP = 16;
    public static final int DRAW_ANIMATION_SPEED = 500;
    public static final int AI_TURN_DELAY = 300;
    private View restart;
    private TextView text;
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
    private VictoryLine victoryLine;
    private AI ai;
    private boolean isMyTurn = true;
    private ImageView background;
    private boolean done;
    private boolean isWIfi;
    private String otherUserId;
    private String gameId;

    public Board(Context context) {
        super(context);
    }

    public Board(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.board, this);
        background = (ImageView) findViewById(R.id.background);
        text = (TextView) findViewById(R.id.text);
        restart = findViewById(R.id.restart);
        restart.setOnClickListener(v1 -> {
            if (!isWIfi) {
                restart();
            } else {
                FirebaseDatabase.getInstance().getReference().child("games")
                        .child(gameId)
                        .setValue(null);

                FirebaseDatabase.getInstance().getReference().child("games")
                        .child(gameId)
                        .child("restart")
                        .setValue(System.currentTimeMillis());
            }
        });

        lineSize = (int) (getResources().getDisplayMetrics().density * LINE_SIZE_IN_DP);
        horizontalLineMargin = (int) (getResources().getDisplayMetrics().density * HORIZONTAL_LINE_MARGIN_IN_DP);
        shape = new Shape(context);

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

        victoryLine = new VictoryLine(getContext(), topLeft, horizontalLineMargin, shapeSizeInPixels, width, lineSize);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.d(TAG, "dispatchDraw: ");
        if (!done) {
            drawLines(canvas);

            // square size is 300 pixels (hardcode value just for now)
            Rect r = getRect(currentPoint.x, currentPoint.y);

            if (currentAnimationShape != NONE) {
                shape.drawShape(currentAnimationShape, 0, canvas, r, v);
            }

            drawPreviousShapes(canvas);

            checkVictory(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isMyTurn || victory != null) {
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

                isMyTurn = !isMyTurn;

                Log.d(TAG, "onAnimationEnd: "+ isWIfi);
                if (!isWIfi) {
                    if (!isMyTurn && victory == null) {
                        Log.d(TAG, "onAnimationEnd: ai");
                        ai.makeDecision();
                        new Handler().postDelayed(() -> {
                            drawShape(ai.getRow(), ai.getCol(), AI_SHAPE);
                        }, AI_TURN_DELAY);
                    }
                } else {
                    FirebaseDatabase.getInstance().getReference().child("games")
                            .child(gameId)
                            .child(row + "_" + col)
                            .setValue(MY_SHAPE);
                }
            }
        });
        animator.start();
    }

    private void checkVictory(Canvas canvas) {
        if (victory != null) {
            if (victory.winner != DRAFT) {
                victoryLine.draw(canvas, victory);
            }
            new Handler().postDelayed(() -> {
                if (!done) {
                    Log.d(TAG, "checkVictory: ");
                    //define this only once if blurring multiple times
                    RenderScript rs = RenderScript.create(getContext());

                    setDrawingCacheEnabled(true);
                    buildDrawingCache();
                    Bitmap board = getDrawingCache();

                    background.setDrawingCacheEnabled(true);
                    background.buildDrawingCache();
                    Bitmap back = background.getDrawingCache();
                    final Allocation input = Allocation.createFromBitmap(rs, board); //use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
                    final Allocation output = Allocation.createTyped(rs, input.getType());
                    final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                    script.setRadius(25f);
                    script.setInput(input);
                    script.forEach(output);
                    output.copyTo(board);

                    background.setImageBitmap(board);
                    background.setBackground(new BitmapDrawable(getResources(), back));
                    done = true;


                    if (victory.winner == ME) {
                        text.setTextColor(Color.parseColor("#4CAF50"));
                        text.setText("YOU WIN!");
                    } else if (victory.winner == ENEMY) {
                        text.setTextColor(Color.parseColor("#FF5722"));
                        text.setText("YOU LOOSE!");
                    } else if (victory.winner == DRAFT) {
                        text.setTextColor(Color.parseColor("#9E9E9E"));
                        text.setText("DRAFT");
                    }

                    text.animate().scaleY(1f).scaleX(1f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            restart.setVisibility(VISIBLE);
                        }
                    }).start();
                }
            }, 500);
        }
    }

    private void restart() {
        destroyDrawingCache();
        restart.setVisibility(GONE);
        text.setScaleX(0f);
        text.setScaleY(0f);
        currentAnimationShape = 0;
        v = 0f;
        victory = null;
        isDrawing = false;
        field = new int[3][3];
        if (!isWIfi) {
            isMyTurn = true;
        } else {
            isMyTurn = MY_SHAPE == Constants.X;
        }
        done = false;
        ai = new AI(MY_SHAPE, AI_SHAPE, field);
        background.setImageResource(R.drawable.chalkboard2);
        background.setBackground(null);
        invalidate();
    }

    public void setWifiWith(String withId) {
        isWIfi = true;
        otherUserId = withId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
        Log.d(TAG, "setGameId: " + gameId);
        FirebaseDatabase.getInstance().getReference().child("games")
                .child(gameId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getValue() == null) {
                            return;
                        }
                        String key = dataSnapshot.getKey();
                        if (!key.equals("restart")) {
                            int row = Integer.parseInt(key.substring(0, 1));
                            int col = Integer.parseInt(key.substring(2, 3));
                            Integer shape = dataSnapshot.getValue(Integer.class);

                            if (field[row][col] == NONE) {
                                drawShape(row, col, shape);
                            }
                        } else {
                            restart();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getValue() == null) {
                            return;
                        }
                        if (dataSnapshot.getKey().equals("restart")) {
                            restart();

                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void setMe(String me) {
        if (me.equals("x")) {
            isMyTurn = true;
            Toast.makeText(getContext(), "Your turn", Toast.LENGTH_SHORT);
            MY_SHAPE = Constants.X;
        } else {
            isMyTurn = false;
            Toast.makeText(getContext(), "Opponent's turn", Toast.LENGTH_SHORT);
            MY_SHAPE = Constants.CIRCLE;
            isMyTurn = false;
        }
    }
}