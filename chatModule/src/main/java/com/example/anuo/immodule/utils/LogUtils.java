package com.example.anuo.immodule.utils;

import android.util.Log;

import com.example.anuo.immodule.BuildConfig;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :2019/6/6
 * Desc  :com.example.anuo.immodule.utils
 */
public class LogUtils {
    public static boolean showDebugLog = BuildConfig.DEBUG;

    public static void i(Object objTag, Object objMsg) {
        String tag = getTag(objTag);
        String msg = (objMsg == null || objMsg.toString() == null) ? "null" : objMsg.toString();
        Log.i(tag, getMsgFormat(msg));
    }

    public static void d(Object objTag, Object objMsg) {
        if (showDebugLog) {
            String tag = getTag(objTag);
            String msg = (objMsg == null || objMsg.toString() == null) ? "null" : objMsg.toString();
            Log.d(tag, getMsgFormat(msg));
        }
    }

    public static void w(Object objTag, Object objMsg) {
        if (showDebugLog) {
            String tag = getTag(objTag);
            String msg = (objMsg == null || objMsg.toString() == null) ? "null" : objMsg.toString();
            Log.w(tag, getMsgFormat(msg));
        }
    }

    public static void e(Object objTag, Object objMsg) {
        if (showDebugLog) {
            String tag = getTag(objTag);
            String msg = (objMsg == null || objMsg.toString() == null) ? "null" : objMsg.toString();
            Log.e(tag, getMsgFormat(msg));
        }
    }

    private static String getTag(Object objTag) {
        String tag;
        if (objTag instanceof String) {
            tag = (String) objTag;
        } else if (objTag instanceof Class) {
            tag = ((Class<?>) objTag).getSimpleName();
        } else {
            tag = objTag.getClass().getSimpleName();
        }
        return tag;
    }

    /**
     * 获取类名,方法名,行号
     *
     * @return Thread:main, at com.haier.ota.OTAApplication.onCreate(OTAApplication.java:35)
     */
    private static String getFunctionName() {
        try {
            StackTraceElement[] sts = Thread.currentThread().getStackTrace();
            if (sts != null) {
                for (StackTraceElement st : sts) {
                    if (st.isNativeMethod()) {
                        continue;
                    }
                    if (st.getClassName().equals(Thread.class.getName())) {
                        continue;
                    }
                    if (st.getClassName().equals(LogUtils.class.getName())) {
                        continue;
                    }
                    return "Thread:" + Thread.currentThread().getName() + ", at " + st.getClassName() + "." + st.getMethodName()
                            + "(" + st.getFileName() + ":" + st.getLineNumber() + ")";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getMsgFormat(String msg) {
        return msg + "----" + getFunctionName();
    }
}
