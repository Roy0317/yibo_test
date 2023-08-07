package com.simon.widget.skinlibrary.attr;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.simon.view.SkinEditText;
import com.simon.view.SkinLeftTextView;
import com.simon.widget.skinlibrary.attr.base.SkinAttr;
import com.simon.widget.skinlibrary.utils.SkinResourcesUtils;


public class DrawableLeftAttr extends SkinAttr {

    @Override
    protected void applySkin(View view) {
        if (view instanceof SkinEditText) {
            SkinEditText tv = (SkinEditText) view;
            if (isDrawable()) {
                setDrawableLeft(tv);
            }
        } else if (view instanceof SkinLeftTextView) {
            SkinLeftTextView tv = (SkinLeftTextView) view;
            if (isDrawable()) {
                setDrawableLeft(tv);
            }
        }
    }

    @Override
    protected void applyExtendMode(View view) {
        if (view instanceof SkinEditText) {
            SkinEditText tv = (SkinEditText) view;
            if (isDrawable()) {
                setDrawableLeft(tv);
            }
        } else if (view instanceof SkinLeftTextView) {
            SkinLeftTextView tv = (SkinLeftTextView) view;
            if (isDrawable()) {
                setDrawableLeft(tv);
            }
        }
    }


    private void setDrawableLeft(TextView textView) {
        Drawable bg = SkinResourcesUtils.getDrawable(attrValueRefId);
        bg.setBounds(0, 0, bg.getMinimumWidth(), bg.getMinimumHeight());
        textView.setCompoundDrawables(bg, null, null, null);
    }

}
