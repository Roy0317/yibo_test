package com.example.anuo.immodule.view;

import android.content.Context;
import android.util.AttributeSet;

import com.example.anuo.immodule.R;

/*
* 获取焦点后光标在最后面
* */
public class LastInputEditText extends androidx.appcompat.widget.AppCompatEditText {

    public LastInputEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSelection(getText().length());
    }

    public LastInputEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public LastInputEditText(Context context) {
        this(context, null);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //光标首次获取焦点是在最后面，之后操作就是按照点击的位置移动光标
        if (isEnabled() && hasFocus() && hasFocusable()) {
            setSelection(selEnd);
        } else {
            setSelection(getText().length());
        }

    }
}
