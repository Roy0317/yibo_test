package com.simon.widget.skinlibrary;

import android.view.View;


import com.simon.widget.skinlibrary.attr.base.SkinAttr;
import com.simon.widget.skinlibrary.utils.SkinListUtils;

import java.util.ArrayList;
import java.util.List;


public class SkinItem {

    public View view;

    public List<SkinAttr> attrs;

    public SkinItem() {
        attrs = new ArrayList<>();
    }

    public void apply() {
        if (SkinListUtils.isEmpty(attrs)) {
            return;
        }
        for (SkinAttr at : attrs) {
            at.apply(view);
        }
    }

    public void clean() {
        if (!SkinListUtils.isEmpty(attrs)) {
            attrs.clear();
        }
    }

    @Override
    public String toString() {
        return "SkinItem [view=" + view.getClass().getSimpleName() + ", attrs=" + attrs + "]";
    }
}
