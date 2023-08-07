package com.simon.widget.skinlibrary;

import android.view.View;
import android.widget.TextView;

import com.simon.widget.skinlibrary.attr.base.DynamicAttr;

import java.util.List;


public interface IDynamicNewView {
    void dynamicAddView(View view, List<DynamicAttr> pDAttrs);

    void dynamicAddView(View view, String attrName, int attrValueResId);

    /**
     * add the textview for font switch
     *
     * @param textView textview
     */
    void dynamicAddFontView(TextView textView);
}
