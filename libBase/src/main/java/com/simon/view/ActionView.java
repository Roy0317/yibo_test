package com.simon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.simon.R;
import com.simon.listener.DataErrorCallBack;


public class ActionView extends FrameLayout {

    private Context ctx;

    public ActionView(Context context) {
        this(context, null);
    }

    public ActionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctx = context;
    }

    /**
     * 添加有相应处理事件视图
     *
     * @param listener    操作回调
     * @param contentView 显示的视图
     * @param id          需要操作的资源id
     */
    public void addActionView(final DataErrorCallBack listener, View contentView, int... id) {
        addView(contentView);

        for (int index : id) {
            if (listener != null) {
                findViewById(index).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onRetry();
                    }
                });
            } else {
                findViewById(index).setVisibility(GONE);
            }
        }
    }


    /**
     * 添加覆盖页面的加载动画
     */
    public void addLoadingView() {

    }


    public View addEmptyView() {
        return LayoutInflater.from(ctx).inflate(R.layout.layout_empty, this, true);
    }


    /**
     * @param layout
     */
    public void addProgressView(int layout) {
        LayoutInflater.from(ctx).inflate(layout, this, true);
    }


    /**
     * @param view
     */
    public void addProgressView(View view) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        addView(view);
    }


}
