package com.simon.widget.skinlibrary.attr;

import android.view.View;

import com.simon.view.SkinChartViewColor;
import com.simon.widget.skinlibrary.attr.base.SkinAttr;
import com.simon.widget.skinlibrary.utils.SkinResourcesUtils;


public class ChartViewColorAttr extends SkinAttr {

    @Override
    protected void applySkin(View view) {
        if (view instanceof SkinChartViewColor) {
            SkinChartViewColor underlinePage = (SkinChartViewColor) view;
            if (isColor()) {
                underlinePage.setThemeColor(SkinResourcesUtils.getColor(attrValueRefId));
            }
        }
    }

    @Override
    protected void applyExtendMode(View view) {
        if (view instanceof SkinChartViewColor) {
            SkinChartViewColor underlinePage = (SkinChartViewColor) view;
            if (isColor()) {
                underlinePage.setThemeColor(SkinResourcesUtils.getColor(attrValueRefId));
            }
        }
    }
}
