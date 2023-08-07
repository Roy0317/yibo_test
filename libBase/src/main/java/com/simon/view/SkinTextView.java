package com.simon.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 切换主题风格时，设置drawableTop，Skin不支持该属性，所以自定义TextView做出区分
 */
public class SkinTextView extends androidx.appcompat.widget.AppCompatTextView {

    public SkinTextView(Context context) {
        super(context);
    }

    public SkinTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SkinTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
