package com.myhexaville.tictactoe;

import com.myhexaville.tictactoe.model.Vector2;

import java.util.ArrayList;

/*
*  x and y fraction of a square going from top left corner
*  Where x and y are center points of a brush
*
*  Made for scalability of design to match different square sizes
*  Still might get gaps in between if square is too big and brush is small
*  Due to input being build on 24 dp brush
* */
public class Constants {
    public static final int NONE = 0;
    public static final int CIRCLE = 1;
    public static final int X = 2;

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    public static final int DIAGONAL_RISING = 2;
    public static final int DIAGONAL_FALLING = 3;

    public static final int DRAFT = 10;
    public static final int ME = 11;
    public static final int ENEMY = 12;

    public static final ArrayList<Vector2> circleOnePoints;
    public static final ArrayList<Vector2> circleTwoPoints;
    public static final ArrayList<Vector2> xOnePoints;

    public static final String FIREBASE_CLOUD_FUNCTIONS_BASE = "https://us-central1-tictactoe-64902.cloudfunctions.net";


    static {
        circleOnePoints = new ArrayList<>();
        circleOnePoints.add(new Vector2(0.76f, 0.23f));
        circleOnePoints.add(new Vector2(0.75714284f, 0.22857143f));
        circleOnePoints.add(new Vector2(0.74909955f, 0.22506836f));
        circleOnePoints.add(new Vector2(0.73090047f, 0.21887155f));
        circleOnePoints.add(new Vector2(0.68163955f, 0.2015039f));
        circleOnePoints.add(new Vector2(0.6078078f, 0.17994943f));
        circleOnePoints.add(new Vector2(0.5574652f, 0.16920733f));
        circleOnePoints.add(new Vector2(0.49907827f, 0.16203378f));
        circleOnePoints.add(new Vector2(0.44352922f, 0.163449f));
        circleOnePoints.add(new Vector2(0.3884395f, 0.1731822f));
        circleOnePoints.add(new Vector2(0.32928205f, 0.19125158f));
        circleOnePoints.add(new Vector2(0.28226453f, 0.21646109f));
        circleOnePoints.add(new Vector2(0.23382595f, 0.26193935f));
        circleOnePoints.add(new Vector2(0.1970013f, 0.30399823f));
        circleOnePoints.add(new Vector2(0.17015198f, 0.3511246f));
        circleOnePoints.add(new Vector2(0.15563037f, 0.40694076f));
        circleOnePoints.add(new Vector2(0.14820495f, 0.4594764f));
        circleOnePoints.add(new Vector2(0.15134482f, 0.50958616f));
        circleOnePoints.add(new Vector2(0.16053165f, 0.560233f));
        circleOnePoints.add(new Vector2(0.18035309f, 0.6088799f));
        circleOnePoints.add(new Vector2(0.21620801f, 0.6475781f));
        circleOnePoints.add(new Vector2(0.25472325f, 0.6883984f));
        circleOnePoints.add(new Vector2(0.30988508f, 0.7294472f));
        circleOnePoints.add(new Vector2(0.3839087f, 0.7536398f));
        circleOnePoints.add(new Vector2(0.4630379f, 0.76152045f));
        circleOnePoints.add(new Vector2(0.52508664f, 0.7450537f));
        circleOnePoints.add(new Vector2(0.5853413f, 0.71736676f));
        circleOnePoints.add(new Vector2(0.6257978f, 0.68766725f));
        circleOnePoints.add(new Vector2(0.6605766f, 0.64960605f));
        circleOnePoints.add(new Vector2(0.691524f, 0.5972853f));
        circleOnePoints.add(new Vector2(0.7188885f, 0.5272496f));
        circleOnePoints.add(new Vector2(0.7427591f, 0.45059928f));
        circleOnePoints.add(new Vector2(0.75350296f, 0.3863529f));
        circleOnePoints.add(new Vector2(0.7557143f, 0.32399186f));
        circleOnePoints.add(new Vector2(0.74238f, 0.28102905f));
        circleOnePoints.add(new Vector2(0.7244155f, 0.23849007f));
        circleOnePoints.add(new Vector2(0.70857143f, 0.21571429f));

        circleTwoPoints = new ArrayList<>();
        circleTwoPoints.add(new Vector2(0.7828571f, 0.30857143f));
        circleTwoPoints.add(new Vector2(0.7828571f, 0.30714285f));
        circleTwoPoints.add(new Vector2(0.77837706f, 0.29675424f));
        circleTwoPoints.add(new Vector2(0.76658297f, 0.28125158f));
        circleTwoPoints.add(new Vector2(0.7499655f, 0.26246113f));
        circleTwoPoints.add(new Vector2(0.71342057f, 0.23467311f));
        circleTwoPoints.add(new Vector2(0.66100323f, 0.20521337f));
        circleTwoPoints.add(new Vector2(0.58368844f, 0.17748971f));
        circleTwoPoints.add(new Vector2(0.52092147f, 0.1678064f));
        circleTwoPoints.add(new Vector2(0.45099556f, 0.16714285f));
        circleTwoPoints.add(new Vector2(0.3931761f, 0.17736477f));
        circleTwoPoints.add(new Vector2(0.3313802f, 0.19463213f));
        circleTwoPoints.add(new Vector2(0.2803147f, 0.21841404f));
        circleTwoPoints.add(new Vector2(0.24280901f, 0.25164926f));
        circleTwoPoints.add(new Vector2(0.21044168f, 0.28905553f));
        circleTwoPoints.add(new Vector2(0.17924792f, 0.33485526f));
        circleTwoPoints.add(new Vector2(0.16023298f, 0.39692304f));
        circleTwoPoints.add(new Vector2(0.15154807f, 0.45886177f));
        circleTwoPoints.add(new Vector2(0.14851576f, 0.5273839f));
        circleTwoPoints.add(new Vector2(0.15495217f, 0.601694f));
        circleTwoPoints.add(new Vector2(0.17149946f, 0.6641678f));
        circleTwoPoints.add(new Vector2(0.21564832f, 0.7183482f));
        circleTwoPoints.add(new Vector2(0.25908065f, 0.75575733f));
        circleTwoPoints.add(new Vector2(0.32316512f, 0.7837598f));
        circleTwoPoints.add(new Vector2(0.40197292f, 0.78612673f));
        circleTwoPoints.add(new Vector2(0.4826503f, 0.7671946f));
        circleTwoPoints.add(new Vector2(0.55761176f, 0.7194329f));
        circleTwoPoints.add(new Vector2(0.62743896f, 0.65060633f));
        circleTwoPoints.add(new Vector2(0.6997263f, 0.5650504f));
        circleTwoPoints.add(new Vector2(0.7399121f, 0.48169357f));
        circleTwoPoints.add(new Vector2(0.76586384f, 0.41393432f));
        circleTwoPoints.add(new Vector2(0.7715918f, 0.3524087f));
        circleTwoPoints.add(new Vector2(0.7657143f, 0.3042857f));

        xOnePoints = new ArrayList<>();
        xOnePoints.add(new Vector2(0.19857143f, 0.17f));
        xOnePoints.add(new Vector2(0.25957274f, 0.21297903f));
        xOnePoints.add(new Vector2(0.28269762f, 0.23072955f));
        xOnePoints.add(new Vector2(0.37268737f, 0.30130512f));
        xOnePoints.add(new Vector2(0.45174858f, 0.37035698f));
        xOnePoints.add(new Vector2(0.51254255f, 0.42035034f));
        xOnePoints.add(new Vector2(0.56999946f, 0.471224f));
        xOnePoints.add(new Vector2(0.62240994f, 0.516218f));
        xOnePoints.add(new Vector2(0.6711032f, 0.5639603f));
        xOnePoints.add(new Vector2(0.71525615f, 0.6070848f));
        xOnePoints.add(new Vector2(0.7546386f, 0.6423281f));
        xOnePoints.add(new Vector2(0.7832172f, 0.66750294f));
        xOnePoints.add(new Vector2(0.803763f, 0.6873617f));
        xOnePoints.add(new Vector2(0.8172261f, 0.7032427f));
        xOnePoints.add(new Vector2(0.8224297f, 0.7122161f));
        xOnePoints.add(new Vector2(0.82499003f, 0.7149901f));
        xOnePoints.add(new Vector2(0.8264286f, 0.7164286f));
        xOnePoints.add(new Vector2(0.8242857f, 0.6873312f));
        xOnePoints.add(new Vector2(0.81706977f, 0.08484715f));
        xOnePoints.add(new Vector2(0.7370434f, 0.17164795f));
        xOnePoints.add(new Vector2(0.6555459f, 0.26448137f));
        xOnePoints.add(new Vector2(0.581813f, 0.35072267f));
        xOnePoints.add(new Vector2(0.49706596f, 0.46558306f));
        xOnePoints.add(new Vector2(0.43554512f, 0.5449426f));
        xOnePoints.add(new Vector2(0.38515905f, 0.6230974f));
        xOnePoints.add(new Vector2(0.344098f, 0.69031286f));
        xOnePoints.add(new Vector2(0.30661482f, 0.75029296f));
        xOnePoints.add(new Vector2(0.28354156f, 0.79434556f));
        xOnePoints.add(new Vector2(0.27110872f, 0.8237367f));
        xOnePoints.add(new Vector2(0.2675137f, 0.84169f));
        xOnePoints.add(new Vector2(0.27260855f, 0.8570391f));
        xOnePoints.add(new Vector2(0.28f, 0.86142856f));
    }
}
