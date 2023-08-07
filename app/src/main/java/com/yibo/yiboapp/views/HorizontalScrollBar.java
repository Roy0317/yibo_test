package com.yibo.yiboapp.views;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.yibo.yiboapp.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HorizontalScrollBar extends View {

    private int mHorizontalThumbHeight;
    private int mHorizontalThumbWidth;
    private int mHorizontalTrackWidth;
    private int mHorizontalThumbStart;
    private Drawable mThumbDrawable;
    private Drawable mTrackDrawable;
    private ObjectAnimator animator;
    private Runnable dismissRunnable;

    public HorizontalScrollBar(@Nullable Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 两个drawable文件可以自己更换
        mThumbDrawable = ContextCompat.getDrawable(getContext(), R.drawable.new_page_btn_normal);
        mTrackDrawable = ContextCompat.getDrawable(getContext(), R.drawable.new_page_btn_normal_gray);
        mHorizontalTrackWidth = 90;
        mHorizontalThumbHeight = 10;
        dismissRunnable = new Runnable() {
            @Override
            public void run() {
                if (isShown()) {
                    animator = ObjectAnimator.ofFloat(this, "alpha",
                            new float[]{getAlpha(), 0.0F}).setDuration(1000);
                    animator.start();
                }
            }
        };
    }

    protected void onDraw(@Nullable Canvas canvas) {
        super.onDraw(canvas);
        if (canvas != null) {
            int start = mHorizontalThumbStart;
            int end = mHorizontalThumbStart + mHorizontalThumbWidth;
            mTrackDrawable.setBounds(0, 0, mHorizontalTrackWidth, mHorizontalThumbHeight);
            mTrackDrawable.draw(canvas);
            mThumbDrawable.setBounds(start, 0, end, mHorizontalThumbHeight);
            mThumbDrawable.draw(canvas);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    public final void attachScrollView(@NotNull final HorizontalScrollView nestedScrollView) {
        setVisibility(VISIBLE);
        // 这里判断只有滑动的时候予以显示
//        nestedScrollView.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int eventAction = event.getAction();
//                switch (eventAction) {
//                    case MotionEvent.ACTION_MOVE:
//                        setVisibility(VISIBLE);
//                        showNow();
//                        invalidate();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        setVisibility(INVISIBLE);
//                        showNow();
//                        invalidate();
//                        break;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });
        nestedScrollView.setOnScrollChangeListener(new OnScrollChangeListener() {
            public final void onScrollChange(View view, int x, int y, int lastX, int lastY) {
                calculate(nestedScrollView);
            }
        });
        post(new Runnable() {
            @Override
            public void run() {
                calculate(nestedScrollView);
            }
        });
    }

    private void calculate(HorizontalScrollView nestedScrollView) {
        int visibleWidth = nestedScrollView.getMeasuredWidth();
        int contentWidth = nestedScrollView.getChildAt(0).getWidth();
        if (contentWidth > visibleWidth) {
            int scrollX = nestedScrollView.getScrollX();
            mHorizontalThumbWidth = getMeasuredWidth() * visibleWidth / contentWidth;
            mHorizontalThumbStart = (getMeasuredWidth() - mHorizontalThumbWidth) * scrollX / (contentWidth - visibleWidth);
            showNow();
            invalidate();
        }
    }

    private void showNow() {
        if (animator != null) {
            animator.end();
            animator.cancel();
        }
        setAlpha(1.0F);
        postDelayDismissRunnable();
    }

    private void postDelayDismissRunnable() {
        removeCallbacks(dismissRunnable);
        postDelayed(dismissRunnable, 1000);
    }
}
