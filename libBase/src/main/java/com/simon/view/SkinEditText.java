package com.simon.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 切换主题风格时，设置drawableLeft，Skin不支持该属性，所以自定义TextView做出区分
 */
public class SkinEditText extends androidx.appcompat.widget.AppCompatEditText {

    public SkinEditText(Context context) {
        super(context);
    }

    public SkinEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SkinEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
