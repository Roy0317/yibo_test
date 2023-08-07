package com.simon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.simon.R;
import com.simon.utils.DisplayUtil;


/**
 * Created by Simon
 * 日期：on 2016/4/25.
 */
public class RootView extends FrameLayout {

    private Context ctx;
    private LayoutInflater mInflater;
    private FrameLayout rootFl;  //根布局
    private FrameLayout contentLayout;  //内容布局

    public RootView(Context context) {
        this(context, null);
    }


    public RootView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RootView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * @param ctx
     */
    private void init(Context ctx) {
        this.ctx = ctx;
        mInflater = LayoutInflater.from(ctx);
        mInflater.inflate(R.layout.activity_base, this, true);
        rootFl = findViewById(R.id.rootFl);
        contentLayout = findViewById(R.id.contentLayout);
    }


    /**
     * 添加导航栏布局
     *
     * @param titleView
     */
    public void setTitleView(View titleView) {
        if (titleView != null) {
            rootFl.addView(titleView);
            coverContentLayout(false);
        }
    }

    /**
     * @param view
     */
    public void removeView(View view) {
        if (view == null) return;

        contentLayout.removeView(view);
    }

    /**
     * 设置主要显示视图
     *
     * @param child
     */
    public View setContentView(View child) {
        if (child != null) {
            contentLayout.addView(child);
        }

        return child;
    }


    /**
     * 设置主要显示视图
     *
     * @param resoucreId
     */
    public View setContentView(int resoucreId) {
        View child = null;
        if (resoucreId != 0) {
            child = mInflater.inflate(resoucreId, null);
            setContentView(child);
        }

        return child;
    }


    /**
     * 设置内容布局被headview盖住
     */
    public void coverContentLayout(boolean isCover) {
        FrameLayout.LayoutParams params = (LayoutParams) contentLayout.getLayoutParams();
        if (!isCover) {
            params.setMargins(0, ctx.getResources().getDimensionPixelSize(R.dimen.top_view_height), 0, 0);
        } else {
            params.setMargins(0, 0, 0, 0);
        }
        contentLayout.requestLayout();

    }


}
