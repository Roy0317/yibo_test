package com.yibo.yiboapp.webview.util;

import android.app.Activity;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.ColorUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;



public class SBUtil {
    /**
     * 设置状态栏的颜色，并在Android6.0之后自动适配状态栏文字颜色
     * <p>
     * 注意：
     * Android6.0之前，Android没有提供设置状态栏文字颜色的方法，所以Android6.0之前不要将状态栏文字颜色设置得太浅
     *
     * @param activity 当前的Activity
     * @param color    状态栏的颜色,可以携带透明位，如：Color.parseColor("#a0000000")
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBackgroundColor(Activity activity, @ColorInt int color) {
        Window window = activity.getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏(否者设置的颜色不会生效)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusTextColor(activity, color);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void setStatusTextColor(Activity activity, @ColorInt int color) {
        if (isLightColor(color)) {
            //设置状态栏文字颜色及图标为深色
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            //设置状态栏文字颜色及图标为浅色
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void setStatusTextColorTest(Activity activity, @ColorInt int color) {
        Window window = activity.getWindow();
        View decor = window.getDecorView();
        int ui = decor.getSystemUiVisibility();
        if (isLightColor(color)) {
            ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        decor.setSystemUiVisibility(ui);
    }

    /**
     * 判断颜色是不是亮色
     *
     * @param color 颜色
     * @return 是否为浅色
     * @from https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     */
    private static boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }
}
