package com.simon.widget.skinlibrary;

import android.content.Context;

import com.simon.widget.skinlibrary.attr.base.AttrFactory;
import com.simon.widget.skinlibrary.attr.base.SkinAttr;
import com.simon.widget.skinlibrary.utils.SkinPreferencesUtils;


public class SkinConfig {
    public static final String NAMESPACE = "http://schemas.android.com/android/skin";
    public static final String PREF_CUSTOM_SKIN_PATH = "skin_custom_path";
    public static final String PREF_FONT_PATH = "skin_font_path";
    public static final String DEFAULT_SKIN = "skin_default";
    public static final String ATTR_SKIN_ENABLE = "enable";

    //切换主题风格支持的属性
    public static final String BACKGROUND = "background";
    public static final String TEXTCOLOR = "textColor";
    public static final String SRC = "src";
    public static final String DRAWABLETOP = "drawableTop";
    public static final String DRAWABLE_LEFT = "drawableLeft";
    public static final String DRAWABLE_RIGHT = "drawableRight";
    public static final String CHRATVIEWCOLOR = "chratViewColor";
    public static final String UNDERLINE = "underline";

    //网络下载的主题风格
    public static final String PREF_EXTEND_MODE = "extend_mode";

    public static final String FONT_DIR_NAME = "fonts";
    private static boolean isCanChangeStatusColor = false;
    private static boolean isCanChangeFont = false;
    private static boolean isDebug = false;
    private static boolean isGlobalSkinApply = false;

    /**
     * get path of last skin package path
     *
     * @param context
     * @return path of skin package
     */
    public static String getCustomSkinPath(Context context) {
        return SkinPreferencesUtils.getString(context, PREF_CUSTOM_SKIN_PATH, DEFAULT_SKIN);
    }

    /**
     * save the skin's path
     *
     * @param context
     * @param path
     */
    public static void saveSkinPath(Context context, String path) {
        SkinPreferencesUtils.putString(context, PREF_CUSTOM_SKIN_PATH, path);
    }

    public static void saveFontPath(Context context, String path) {
        SkinPreferencesUtils.putString(context, PREF_FONT_PATH, path);
    }

    public static boolean isDefaultSkin(Context context) {
        return DEFAULT_SKIN.equals(getCustomSkinPath(context));
    }

    public static void setExtendMode(Context context, boolean isEnableExtendMode) {
        SkinPreferencesUtils.putBoolean(context, PREF_EXTEND_MODE, isEnableExtendMode);
    }

    public static boolean isInExtendMode(Context context) {
        return SkinPreferencesUtils.getBoolean(context, PREF_EXTEND_MODE, false);
    }

    public static void setCanChangeStatusColor(boolean isCan) {
        isCanChangeStatusColor = isCan;
    }

    public static boolean isCanChangeStatusColor() {
        return isCanChangeStatusColor;
    }

    public static void setCanChangeFont(boolean isCan) {
        isCanChangeFont = isCan;
    }

    public static boolean isCanChangeFont() {
        return isCanChangeFont;
    }

    public static void setDebug(boolean enable) {
        isDebug = enable;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    /**
     * add custom skin attribute support
     *
     * @param attrName attribute name
     * @param skinAttr skin attribute
     */
    public static void addSupportAttr(String attrName, SkinAttr skinAttr) {
        AttrFactory.addSupportAttr(attrName, skinAttr);
    }

    public static boolean isGlobalSkinApply() {
        return isGlobalSkinApply;
    }

    /**
     * apply skin for global and you don't to set  skin:enable="true"  in layout
     */
    public static void enableGlobalSkinApply() {
        isGlobalSkinApply = true;
    }

}
