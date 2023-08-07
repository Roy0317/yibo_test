package com.yibo.yiboapp.views;

import android.content.Context;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Author: Ray
 * created on 2018/10/27
 * description : 为了解决和ViewPager的滑动冲突
 */
public class VerticalSwipeRefreshLayout extends SwipeRefreshLayout {

    private float startX;//开始触摸的x坐标
    private float startY;//开始触摸的y坐标
    private int mTouchSlop = 0;//触摸的最小距离

    public VerticalSwipeRefreshLayout(Context context) {
        super(context);
    }

    public VerticalSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = ev.getX();
                float endY = ev.getY();
                int distanceX = (int) Math.abs(endX - startX);
                int distanceY = (int) Math.abs(endY - startY);
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    return false;//
                }
                break;
        }

        //其他情况交给父类去处理
        return super.onInterceptTouchEvent(ev);
    }
}
