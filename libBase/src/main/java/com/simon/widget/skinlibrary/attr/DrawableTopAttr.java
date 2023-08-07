package com.simon.widget.skinlibrary.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.simon.view.SkinTextView;
import com.simon.widget.skinlibrary.attr.base.SkinAttr;
import com.simon.widget.skinlibrary.utils.SkinResourcesUtils;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:21:46
 */
public class DrawableTopAttr extends SkinAttr {

    @Override
    protected void applySkin(View view) {
        if (view instanceof SkinTextView) {
            SkinTextView tv = (SkinTextView) view;
            if (isDrawable()) {
                Drawable bg = SkinResourcesUtils.getDrawable(attrValueRefId);
                bg.setBounds(0, 0, bg.getMinimumWidth(), bg.getMinimumHeight());
                tv.setCompoundDrawables(null, bg, null, null);
            } else if (isColor()) {
                tv.setTextColor(SkinResourcesUtils.getColorStateList(attrValueRefId));
            }
        }
    }

    @Override
    protected void applyExtendMode(View view) {
        if (view instanceof SkinTextView) {
            SkinTextView tv = (SkinTextView) view;
            if (isDrawable()) {
                Drawable bg = SkinResourcesUtils.getDrawable(attrValueRefId);
                bg.setBounds(0, 0, bg.getMinimumWidth(), bg.getMinimumHeight());
                tv.setCompoundDrawables(null, bg, null, null);
            } else if (isColor()) {
                tv.setTextColor(SkinResourcesUtils.getColorStateList(attrValueRefId));
            }
        }
    }
}
