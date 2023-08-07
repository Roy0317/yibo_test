package com.example.anuo.immodule.view.fire;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.animation.AccelerateInterpolator;

import com.example.anuo.immodule.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :09/07/2019
 * Desc  :com.example.anuo.immodule.view.fire
 */
public class FireManager {

    private final String TAG = this.getClass().getSimpleName();
    final float screenWidthMeasure = 720;
    private final static int BIG_DEFAULT_FireElement_COUNT = 120;//大烟花爆炸数量
    private final static int MIDDLE_DEFAULT_FireElement_COUNT = 100;
    private int BIG_DEFAULT_DURATION = 3000;
    private final static float BIG_DEFAULT_LAUNCH_SPEED = 13;
    private final static float BIG_DEFAULT_FireElement_SIZE = 8;

    private final static int SMALL_DEFAULT_FireElement_COUNT = 8;//小星星爆炸数量
    private int SMALL_DEFAULT_DURATION = 1300;//烟花持续时间
    private final static float SMALL_DEFAULT_LAUNCH_SPEED = 18;//烟花分散速度
    private final static float SMALL_DEFAULT_FireElement_SIZE = 8;//烟花颗粒大小

    private final static float DEFAULT_WIND_SPEED = 6;
    private final static float DEFAULT_GRAVITY = 6;

    private Paint mPaint;
    private int count; // count of FireElement
    private int duration;
    private int[] colors;
    private int color;
    private float launchSpeed;
    private float windSpeed;
    private float gravity;
    private int windDirection; // 1 or -1
    private Location location;
    private float FireElementSize;
    //    GameSoundPool sounds;//烟花爆炸声音控制
    private ValueAnimator animator;
    private float animatorValue;

    private ArrayList<FireElement> FireElements = new ArrayList<FireElement>();
    private AnimationEndListener listener;
    Context context;
    private int mode = 0;
    private float srceenWidth;
    private float screenHeight;
    //大烟花颗粒随机图片
    private int bitmapColor[] = {R.drawable.caidai_1, R.drawable.caidai_2, R.drawable.caidai_3,
            R.drawable.caidai_4, R.drawable.caidai_5, R.drawable.caidai_6, R.drawable.caidai_7, R.drawable.caidai_8,
            R.drawable.caidai_9, R.drawable.caidai_10, R.drawable.caidai_11, R.drawable.caidai_12, R.drawable.caidai_13,
            R.drawable.caidai_14, R.drawable.caidai_15, R.drawable.caidai_16, R.drawable.caidai_17, R.drawable.caidai_18,
            R.drawable.caidai_19, R.drawable.caidai_20, R.drawable.caidai_21};

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setScreenSize(float width, float height) {
        srceenWidth = width;
        screenHeight = height;
    }

    public FireManager(Location location, int windDirection, int mode,
                       Context context, int color, float width,
                       float height) {
        srceenWidth = width;
        screenHeight = height;
        this.color = color;
        this.location = location;
//        this.sounds = sounds;
        this.windDirection = windDirection;
        this.context = context;
        this.mode = mode;
        colors = baseColors;
        gravity = DEFAULT_GRAVITY;
        windSpeed = DEFAULT_WIND_SPEED;
        if (srceenWidth > 0) {
            BIG_DEFAULT_DURATION = (int) (srceenWidth / screenWidthMeasure * BIG_DEFAULT_DURATION);
            SMALL_DEFAULT_DURATION = (int) (srceenWidth / screenWidthMeasure * SMALL_DEFAULT_DURATION);
        }
        if (mode == 0) {// 大烟花
            count = BIG_DEFAULT_FireElement_COUNT;
            duration = BIG_DEFAULT_DURATION;
            launchSpeed = BIG_DEFAULT_LAUNCH_SPEED;
            FireElementSize = BIG_DEFAULT_FireElement_SIZE;
        } else if (mode == 1) {//小星星爆炸
            count = SMALL_DEFAULT_FireElement_COUNT;
            duration = SMALL_DEFAULT_DURATION;
            launchSpeed = SMALL_DEFAULT_LAUNCH_SPEED;
            FireElementSize = SMALL_DEFAULT_FireElement_SIZE;
        } else {
            count = MIDDLE_DEFAULT_FireElement_COUNT;
            duration = BIG_DEFAULT_DURATION;
            launchSpeed = BIG_DEFAULT_LAUNCH_SPEED;
            FireElementSize = BIG_DEFAULT_FireElement_SIZE;
        }
        init();
    }

    private float starSize = 15;

    private void init() {
        Random random = new Random(System.currentTimeMillis());
// color = colors[random.nextInt(colors.length)];
// 给每个火花设定一个随机的方向 0-360
        FireElements.clear();
        if (mode == 0) {
            for (int i = 0; i < count; i++) {
                color = bitmapColor[random.nextInt(bitmapColor.length)];
                InputStream is = context.getResources().openRawResource(color);
                Bitmap mBitmap = BitmapFactory.decodeStream(is);
                FireElements.add(new FireElement(color, Math.toRadians(random
                        .nextInt(360)), random.nextFloat() * launchSpeed,
                        mBitmap));
            }
        }
//        else {
//            float bitmapScale = 2;
//            if (srceenWidth > 0) {
//                bitmapScale = srceenWidth / screenWidthMeasure * bitmapScale;
//            }
//            for (int i = 0; i < count; i++) {
//                InputStream is = context.getResources().openRawResource(color);//小星星图片资源id
//                Bitmap mBitmap = BitmapFactory.decodeStream(is);
//                Bitmap shapeBitmap = Utils.drawShapeBitmap(mBitmap,
//                        (int) (srceenWidth / screenWidthMeasure * starSize),
//                        "star");
//
//
//                FireElements.add(new FireElement(color, Math.toRadians(random
//                        .nextInt(360)), random.nextFloat() * launchSpeed,
//                        shapeBitmap));
//            }
//
//
//        }
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        timeCount = 1;
        animatorValue = timeCount;
    }

    private float timeCount = 1;
    private final float dif = 0.00816f;

    float startTime;
    private boolean needRemove = false;

    public boolean getRemove() {
        return needRemove;
    }

    boolean isStart = false;

    /*
     * 开始烟花爆炸动画
     */
    public void fire() {
        animator = ValueAnimator.ofFloat(1, 0);
        animator.setDuration(duration);
//从头开始动画
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatorValue = (Float) valueAnimator.getAnimatedValue();
// 计算每个火花的位置
                isStart = true;
                for (FireElement FireElement : FireElements) {
                    FireElement.x = (float) (FireElement.x
                            + Math.cos(FireElement.direction) * FireElement.speed
                            * animatorValue + windSpeed * windDirection);
                    FireElement.y = (float) (FireElement.y
                            - Math.sin(FireElement.direction) * FireElement.speed
                            * animatorValue + gravity * (1 - animatorValue));
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd(FireManager.this);
                needRemove = true;
            }
        });
        animator.start();
//        if (mode == 0 && sounds != null) {
//            sounds.playSound(9, 0);
//        }

    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setColors(int colors[]) {
        this.colors = colors;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void addAnimationEndListener(AnimationEndListener listener) {
        this.listener = listener;
    }

    private final int maxTime = 38;
    private int n = 0;

    public void draw(Canvas canvas) {
        mPaint.setAlpha((int) (225 * animatorValue));
        /*
         * 有些情况小星星动画不能停止，强制结束
         */
        n++;
        if (n > maxTime) {
            listener.onAnimationEnd(FireManager.this);
        }
        for (FireElement FireElement : FireElements) {
            canvas.drawBitmap(FireElement.bitmap, location.x + FireElement.x,
                    location.y + FireElement.y, mPaint);
        }
        /*
         * 更新烟花位置
         */
        if (n > 2 && !isStart) {
            updateLocation();
        }
    }


    public void updateLocation() {
        animatorValue -= dif;
        if (animatorValue < 0) {
            listener.onAnimationEnd(FireManager.this);
        }
        for (FireElement FireElement : FireElements) {
            FireElement.x = (float) (FireElement.x
                    + Math.cos(FireElement.direction) * FireElement.speed
                    * animatorValue + windSpeed * windDirection);
            FireElement.y = (float) (FireElement.y
                    - Math.sin(FireElement.direction) * FireElement.speed
                    * animatorValue + gravity * (1 - animatorValue));
        }
    }

    public void release() {
        for (FireElement FireElement : FireElements) {
            if (FireElement.bitmap != null && FireElement.bitmap.isRecycled()) {
                FireElement.bitmap.recycle();
            }
        }
    }

    private static final int[] baseColors = {0xFFFF43, 0x00E500, 0x44CEF6,
            0xFF0040, 0xFF00FFB7, 0x008CFF, 0xFF5286, 0x562CFF, 0x2C9DFF,
            0x00FFFF, 0x00FF77, 0x11FF00, 0xFFB536, 0xFF4618, 0xFF334B,
            0x9CFA18};

    interface AnimationEndListener {
        void onAnimationEnd(FireManager mFirework);
    }

    static class Location {
        public float x;
        public float y;

        public Location(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
