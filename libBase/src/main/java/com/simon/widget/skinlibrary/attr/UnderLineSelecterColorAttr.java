package com.simon.widget.skinlibrary.attr;

import android.view.View;

import com.simon.view.SkinUnderlinePage;
import com.simon.widget.skinlibrary.attr.base.SkinAttr;
import com.simon.widget.skinlibrary.utils.SkinResourcesUtils;


public class UnderLineSelecterColorAttr extends SkinAttr {

    @Override
    protected void applySkin(View view) {
        if (view instanceof SkinUnderlinePage) {
            SkinUnderlinePage underlinePage = (SkinUnderlinePage) view;
            if (isColor()) {
                underlinePage.setSelectedColor(SkinResourcesUtils.getColor(attrValueRefId));
            }
        }
    }

    @Override
    protected void applyExtendMode(View view) {
        if (view instanceof SkinUnderlinePage) {
            SkinUnderlinePage underlinePage = (SkinUnderlinePage) view;
            if (isColor()) {
                underlinePage.setSelectedColor(SkinResourcesUtils.getColor(attrValueRefId));
            }
        }
    }
}
