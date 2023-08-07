package com.simon.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.widget.TextView;

/**
 * Created by simon.
 */
public class DrawableUtils {

    /**
     * 设置图片
     */
    public static void drawableLeft(Context context, TextView myTextview, int res) {
        Drawable drawable = ContextCompat.getDrawable(context, res);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        myTextview.setCompoundDrawables(drawable, null, null, null);

    }


    public static <T extends TextView> void drawableTop(Context context, T myTextview, int res) {
        Drawable drawable = ContextCompat.getDrawable(context, res);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        myTextview.setCompoundDrawables(null, drawable, null, null);
    }

    public static <T extends TextView> void drawableTop(Context context, T myTextview, int res, int size) {
        Drawable drawable = ContextCompat.getDrawable(context, res);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, size, size);
        myTextview.setCompoundDrawables(null, drawable, null, null);
    }

    public static <T extends TextView> void drawableLeft(Context context, T myTextview, int res, int size) {
        Drawable drawable = ContextCompat.getDrawable(context, res);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, size, size);
        myTextview.setCompoundDrawables(drawable, null, null, null);
    }


    public static <T extends TextView> void drawableRight(Context context, T myTextview, int res) {
        Drawable drawable = ContextCompat.getDrawable(context, res);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        myTextview.setCompoundDrawables(null, null, drawable, null);
    }

    public static <T extends TextView> void drawableRight(Context context, T myTextview, int res, int size) {
        Drawable drawable = ContextCompat.getDrawable(context, res);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, size, size);
        myTextview.setCompoundDrawables(null, null, drawable, null);
    }


    public static <T extends TextView> void drawableNone(Context context, T myTextview) {
        myTextview.setCompoundDrawables(null, null, null, null);

    }

}
