package com.yibo.yiboapp.ui;

import android.content.Context;
import android.util.AttributeSet;

/*
 * 文字滚动
 * */
public class ScrollTextView extends androidx.appcompat.widget.AppCompatTextView {
    public ScrollTextView(Context context) {
        super(context);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {//必须重写，且返回值是true，表示始终获取焦点
        return true;
    }
}
