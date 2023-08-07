package com.simon.widget.skinlibrary.utils;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.simon.widget.skinlibrary.loader.SkinManager;


/**
 * Created by _SOLID
 * Date:2016/7/9
 * Time:13:56
 */
public class SkinResourcesUtils {

    public static int getColor(int resId) {
        return SkinManager.getInstance().getColor(resId);
    }

    public static ColorStateList getExtendColorStateList(int resId) {
        return SkinManager.getInstance().getExtendColorStateList(resId);
    }

    public static int getExtendColor(int resId) {
        return SkinManager.getInstance().getExtendColor(resId);
    }

    public static Drawable getDrawable(int resId) {
        return SkinManager.getInstance().getDrawable(resId);
    }

    public static Drawable getExtendDrawable(String resName) {
        return SkinManager.getInstance().getExtendDrawable(resName);
    }

    /**
     * get drawable from specific directory
     *
     * @param resId res id
     * @param dir   res directory
     * @return drawable
     */
    public static Drawable getDrawable(int resId, String dir) {
        return SkinManager.getInstance().getDrawable(resId, dir);
    }

    public static ColorStateList getColorStateList(int resId) {
        return SkinManager.getInstance().getColorStateList(resId);
    }

    public static int getColorPrimaryDark() {
        if (!isExtendMode()) {
            Resources resources = SkinManager.getInstance().getResources();
            if (resources != null) {
                int identify = resources.getIdentifier(
                        "colorPrimaryDark",
                        "color",
                        SkinManager.getInstance().getCurSkinPackageName());
                if (identify > 0) {
                    return resources.getColor(identify);
                }
            }
        } else {
            Resources resources = SkinManager.getInstance().getResources();
            if (resources != null) {
                int identify = resources.getIdentifier(
                        "colorPrimaryDark_night",
                        "color",
                        SkinManager.getInstance().getCurSkinPackageName());
                if (identify > 0) {
                    return SkinManager.getInstance().getColor(identify);
                }
            }
        }
        return -1;
    }

    public static boolean isExtendMode() {
        return SkinManager.getInstance().isExtendMode();
    }
}
