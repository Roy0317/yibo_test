package com.simon.widget.skinlibrary.attr;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.simon.view.SkinRightTextView;
import com.simon.widget.skinlibrary.attr.base.SkinAttr;
import com.simon.widget.skinlibrary.utils.SkinResourcesUtils;


public class DrawableRightAttr extends SkinAttr {

    @Override
    protected void applySkin(View view) {
        if (view instanceof SkinRightTextView) {
            SkinRightTextView tv = (SkinRightTextView) view;
            if (isDrawable()) {
                setDrawableLeft(tv);
            }
        }
    }

    @Override
    protected void applyExtendMode(View view) {
        if (view instanceof SkinRightTextView) {
            SkinRightTextView tv = (SkinRightTextView) view;
            if (isDrawable()) {
                setDrawableLeft(tv);
            }
        }
    }


    private void setDrawableLeft(TextView textView) {
        Drawable bg = SkinResourcesUtils.getDrawable(attrValueRefId);
        bg.setBounds(0, 0, bg.getMinimumWidth(), bg.getMinimumHeight());
        textView.setCompoundDrawables(null, null, bg, null);
    }

}
