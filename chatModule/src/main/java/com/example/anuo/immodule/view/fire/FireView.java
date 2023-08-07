package com.example.anuo.immodule.view.fire;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.LinkedList;

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

public class FireView extends View {

    private final String TAG = this.getClass().getSimpleName();
    private LinkedList<FireManager> fireworks = new LinkedList<FireManager>();
    private int windSpeed;
    private TextWatcher mTextWatcher;
    Context context;
    //    GameSoundPool sounds;
    private float srceenWidth;
    private float screenHeight;


    public FireView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    public FireView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        srceenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
    }

    public FireView(Context context) {
        super(context);
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        srceenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
    }

    //    public FireView(Context context, GameSoundPool sounds) {
//        super(context);
//        this.context = context;
//        this.sounds = sounds;
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        srceenWidth = wm.getDefaultDisplay().getWidth();
//        screenHeight = wm.getDefaultDisplay().getHeight();
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void lunchFireWork(float x, float y, int direction, int mode, int color) {
        final FireManager firework = new FireManager(new FireManager.Location(x, y), direction, mode, context, color, srceenWidth, screenHeight);
        firework.addAnimationEndListener(new FireManager.AnimationEndListener() {
            @Override

            public void onAnimationEnd(FireManager mFirework) {
                fireworks.remove(mFirework);
                mFirework.release();
                postInvalidate();
            }
        });
        fireworks.add(firework);
        firework.fire();
        invalidate();
    }

    public void removeAllFireWork() {
        if (fireworks != null) {
            for (FireManager obj : fireworks) {
                fireworks.remove(obj);
                obj.release();
            }
        }
    }

    @Override

    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < fireworks.size(); i++) {
            fireworks.get(i).draw(canvas);
        }
        if (fireworks.size() > 0)
            invalidate();
    }
}


