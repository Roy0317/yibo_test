package com.yibo.yiboapp.views;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Author: Ray
 * created on 2018/12/8
 * description : 拦截内部事件的View
 */
public class InterceptionLinearLayout extends LinearLayout {
    public InterceptionLinearLayout(Context context) {
        super(context);
    }

    public InterceptionLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptionLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
