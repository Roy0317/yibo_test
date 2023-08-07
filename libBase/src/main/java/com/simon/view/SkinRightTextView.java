package com.simon.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 切换主题风格时，设置drawableRight，Skin不支持该属性，所以自定义TextView做出区分
 */
public class SkinRightTextView extends androidx.appcompat.widget.AppCompatTextView {

    public SkinRightTextView(Context context) {
        super(context);
    }

    public SkinRightTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SkinRightTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
