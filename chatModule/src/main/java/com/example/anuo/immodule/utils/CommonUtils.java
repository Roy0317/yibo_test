package com.example.anuo.immodule.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import androidx.core.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.constant.Constant;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import crazy_wrapper.Crazy.Utils.RequestUtils;
import crazy_wrapper.Crazy.Utils.Utils;

import static com.example.anuo.immodule.lottery.LotteryUtils.getShengXiaoFromYear;

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
 * Date  :2019/6/5
 * Desc  :com.example.anuo.immodule.utils
 */
public class CommonUtils {


    public static final boolean DEBUG = true;
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static String YIBO_DIR = Environment.getExternalStorageDirectory() + "/ybandroid";

    /**
     * 修改状态栏
     *
     * @param context
     */
    public static void useTransformBar(Activity context) {
        if (context.isFinishing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = context.getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再沉浸到状态栏下
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }

    /**
     * 获取应用程序名称
     *
     * @param context
     * @return
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DisplayMetrics screenInfo(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }

    //是否六合彩彩种
    public static boolean isSixMark(String lotCode) {
        if (Utils.isEmptyString(lotCode)) {
            return false;
        }
        return lotCode.contains("LHC");
    }

    public static boolean isPeilvVersionMethod(Context context) {
        String currentVersion = YiboPreferenceUtils.instance(context).getGameVersion();
        if (Utils.isEmptyString(currentVersion)) {
            return false;
        }
        int version = Integer.parseInt(currentVersion);
        if (version == Constant.lottery_identify_V2 || version == Constant.lottery_identify_V4 ||
                version == Constant.lottery_identify_V5) {
            return true;
        }
        return false;
    }

    /**
     * 根据彩票类型码筛选显示背景
     *
     * @param cpCode
     * @param num
     * @param cpVersion
     * @param ball
     * @param position
     */
    public static void figureImgeByCpCode(String cpCode, String num, String cpVersion, TextView ball, int position) {

        if (cpVersion.equals(String.valueOf(ConfigCons.lottery_identify_V2)) ||
                cpVersion.equals(String.valueOf(ConfigCons.lottery_identify_V4)) ||
                cpVersion.equals(String.valueOf(ConfigCons.lottery_identify_V5))) {
            if (cpCode.equals("9")) {//时时彩
                ball.setBackgroundResource(R.drawable.ffc_yellow_bg);
                ball.setText(num);
            } else if (cpCode.equals("10")) {//快三
                setKuai3NumImage(num, ball, position);
            } else if (cpCode.equals("11")) {//pc蛋蛋，加拿大28
//                figurePceggImage(num, ball);
                figurePcDDImageOrder(num, ball);
            } else if (cpCode.equals("12")) {////重庆幸运农场,湖南快乐十分,广东快乐十分
//                figureXYNCImage(num, ball);
                ball.setBackgroundResource(R.drawable.ffc_blue_bg);
                ball.setText(num);
            } else if (cpCode.equals("8")) {//极速赛车，北京赛车，幸运飞艇
                figureSaiCheImage(num, ball);
            } else if (cpCode.equals("14")) {//11选5
                ball.setBackgroundResource(R.drawable.ffc_yellow_bg);
                ball.setText(num);
            } else if (cpCode.equals("15")) {//福彩3D，排列三
                ball.setBackgroundResource(R.drawable.ffc_red_bg);
                ball.setText(num);
            } else if (cpCode.equals("6") || cpCode.equals("66")) {//六合彩，十分六合彩
                figureLhcImage(num, ball);
            }
        } else if (cpVersion.equals(String.valueOf(ConfigCons.lottery_identify_V1))) {
            if (cpCode.equals("1") || cpCode.equals("2")) {//时时彩
                ball.setBackgroundResource(R.drawable.ffc_yellow_bg);
                ball.setText(num);
            } else if (cpCode.equals("100")) {//快三
                setKuai3NumImage(num, ball, position);
            } else if (cpCode.equals("7")) {//pc蛋蛋，加拿大28
//                figurePceggImage(num, ball);
                figurePcDDImageOrder(num, ball);
            } else if (cpCode.equals("3")) {//极速赛车，北京赛车，幸运飞艇
                figureSaiCheImage(num, ball);
            } else if (cpCode.equals("5")) {//11选5
                ball.setBackgroundResource(R.drawable.ffc_yellow_bg);
                ball.setText(num);
            } else if (cpCode.equals("4")) {//福彩3D，排列三
                ball.setBackgroundResource(R.drawable.ffc_red_bg);
                ball.setText(num);
            } else if (cpCode.equals("6") || cpCode.equals("66")) {//六合彩，十分六合彩
                figureLhcImage(num, ball);
            }
        } else if (cpVersion.equals(String.valueOf(ConfigCons.lottery_identify_V3))) {
            if (cpCode.equals("51") || cpCode.equals("52")) {//时时彩
                ball.setBackgroundResource(R.drawable.ffc_yellow_bg);
                ball.setText(num);
            } else if (cpCode.equals("58")) {//快三
                setKuai3NumImage(num, ball, position);
            } else if (cpCode.equals("57")) {//pc蛋蛋，加拿大28
//                figurePceggImage(num, ball);
                figurePcDDImageOrder(num, ball);
            } else if (cpCode.equals("53")) {//极速赛车，北京赛车，幸运飞艇
                figureSaiCheImage(num, ball);
            } else if (cpCode.equals("55")) {//11选5
                ball.setBackgroundResource(R.drawable.ffc_yellow_bg);
                ball.setText(num);
            } else if (cpCode.equals("54")) {//福彩3D，排列三
                ball.setBackgroundResource(R.drawable.ffc_red_bg);
                ball.setText(num);
            } else if (cpCode.equals("6") || cpCode.equals("66")) {//六合彩，十分六合彩
                figureLhcImage(num, ball);
            }
        }

    }

    /**
     * 快三
     *
     * @param num
     * @param ball
     */
    private static void setKuai3NumImage(String num, TextView ball, int index) {
        if (index < 3) {
            if (num.equals("1")) {
                ball.setBackgroundResource(R.drawable.kuai3_bg_one);
                ball.setText("");
            } else if (num.equals("2")) {
                ball.setBackgroundResource(R.drawable.kuai3_bg_two);
                ball.setText("");
            } else if (num.equals("3")) {
                ball.setBackgroundResource(R.drawable.kuai3_bg_three);
                ball.setText("");
            } else if (num.equals("4")) {
                ball.setBackgroundResource(R.drawable.kuai3_bg_four);
                ball.setText("");
            } else if (num.equals("5")) {
                ball.setBackgroundResource(R.drawable.kuai3_bg_five);
                ball.setText("");
            } else if (num.equals("6")) {
                ball.setBackgroundResource(R.drawable.kuai3_bg_six);
                ball.setText("");
            } else {
                ball.setBackgroundResource(R.drawable.kuai3_bg_empty);
                ball.setText("");
            }
        } else {
            ball.setBackgroundResource(R.drawable.kuai3_bg_empty_bai);
            ball.setText(num);
        }
    }

    /**
     * PC蛋蛋和加拿大28背景
     *
     * @param num
     * @param ball
     */
    private static void figurePcDDImageOrder(String num, TextView ball) {

        int drawableRes;
        int number;
        if (isNumeric(num)) {
            number = Integer.parseInt(num);
        } else {
            number = 0;
        }

        switch (number) {
            case 3:
            case 6:
            case 9:
            case 12:
            case 15:
            case 18:
            case 21:
            case 24:
                drawableRes = R.drawable.ffc_red_bg;
                break;
            case 1:
            case 4:
            case 7:
            case 10:
            case 16:
            case 19:
            case 22:
            case 25:
                drawableRes = R.drawable.ffc_green_bg;
                break;
            case 2:
            case 5:
            case 8:
            case 11:
            case 17:
            case 20:
            case 23:
            case 26:
                drawableRes = R.drawable.ffc_blue_bg;
                break;
            default:
                drawableRes = R.drawable.ffc_yellow_bg;
                break;
        }
        ball.setBackgroundResource(drawableRes);
        ball.setText(num);
    }

    /**
     * 赛车
     *
     * @param num
     * @param ball
     */
    private static void figureSaiCheImage(String num, TextView ball) {
        if (num.equals("?")) {
            ball.setBackgroundResource(R.drawable.ffc_red_bg);
            ball.setText(num);
        } else {
            if (!num.startsWith("0") && num.length() == 1) {
                num = "0" + num;
            }
            ball.setText("");
            if (num.equals("01")) {
                ball.setBackgroundResource(R.drawable.sc_01_title);
            } else if (num.equals("02")) {
                ball.setBackgroundResource(R.drawable.sc_02_title);
            } else if (num.equals("03")) {
                ball.setBackgroundResource(R.drawable.sc_03_title);
            } else if (num.equals("04")) {
                ball.setBackgroundResource(R.drawable.sc_04_title);
            } else if (num.equals("05")) {
                ball.setBackgroundResource(R.drawable.sc_05_title);
            } else if (num.equals("06")) {
                ball.setBackgroundResource(R.drawable.sc_06_title);
            } else if (num.equals("07")) {
                ball.setBackgroundResource(R.drawable.sc_07_title);
            } else if (num.equals("08")) {
                ball.setBackgroundResource(R.drawable.sc_08_title);
            } else if (num.equals("09")) {
                ball.setBackgroundResource(R.drawable.sc_09_title);
            } else if (num.equals("10")) {
                ball.setBackgroundResource(R.drawable.sc_10_title);
            } else {
                ball.setText(num);
                ball.setBackground(null);
            }
        }
    }

    private static void figureLhcImage(String num, TextView ball) {
        String[] redBO = new String[]{"01", "02", "07", "08", "12", "13", "18", "19", "23", "24", "29", "30", "34", "35", "40", "45", "46"};
        String[] blueBO = new String[]{"03", "04", "09", "10", "14", "15", "20", "25", "26", "31", "36", "37", "41", "42", "47", "48"};
        String[] greenBO = new String[]{"05", "06", "11", "16", "17", "21", "22", "27", "28", "32", "33", "38", "39", "43", "44", "49"};

        if (!num.startsWith("0") && num.length() == 1) {
            num = "0" + num;
        }
        if (Arrays.asList(redBO).contains(num)) {
            ball.setBackgroundResource(R.drawable.lhc_red_bg);
        } else if (Arrays.asList(blueBO).contains(num)) {
            ball.setBackgroundResource(R.drawable.lhc_blue_bg);
        } else if (Arrays.asList(greenBO).contains(num)) {
            ball.setBackgroundResource(R.drawable.lhc_green_bg);
        } else {
            ball.setBackgroundResource(R.drawable.lhc_blue_bg);
        }
        ball.setText(num);
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 复制功能
     *
     * @param message
     */
    public static void copy(Context context, String message) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", message);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

    /**
     * 手机震动
     *
     * @param activity
     * @param milliseconds 震动时长（毫秒）
     */
    public static void vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    /**
     * @param activity
     * @param pattern  震动规则 （[静止时长，震动时长，静止时长，震动时长。。。]毫秒）
     * @param isRepeat 是否反复震动 false：只震一次
     */
    public static void vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }


    /**
     * 获取通知权限,监测是否开启了系统通知
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {

        boolean isOpened;
        try {
            isOpened = NotificationManagerCompat.from(context).areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            isOpened = false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = manager.getNotificationChannel("chat");
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                isOpened = false;
            }
        }
        return isOpened;
    }

    /**
     * 如果没有开启通知，跳转至设置界面
     *
     * @param context
     */
    public static void setNotification(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) {
            // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else {
            // 其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String strVersionName = null;

        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            if (null != info) {
                strVersionName = info.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return strVersionName;
    }

    /**
     * app接口请求头获取
     *
     * @param context
     * @return
     */
    public static Map<String, String> getHeader(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept-Charset", "utf-8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Accept-Language", "zh-CN,en,*");
        headers.put("Cookie", "SESSION=" + ChatSpUtils.instance(context).getToken());
        headers.put("X-Requested-With", "XMLHttpRequest");//仿AJAX访问
        headers.put("User-Agent", "android/" + getVersionName(context));

        if (!Utils.isEmptyString(ConfigCons.YUNJI_BASE_HOST_URL)) {
            headers.put(RequestUtils.NATIVE_HOST, ConfigCons.YUNJI_BASE_HOST_URL);
        }
        if (!Utils.isEmptyString(ConfigCons.YUNJI_NATIVE_FLAG_URL)) {
            headers.put(RequestUtils.NATIVE_FLAG, ConfigCons.YUNJI_NATIVE_FLAG_URL);
        }
        if (!Utils.isEmptyString(ConfigCons.YUNJI_NATIVE_DOMAIN_URL)) {
            headers.put(RequestUtils.NATIVE_DOMAIN, ConfigCons.YUNJI_NATIVE_DOMAIN_URL);
        }
        if (!Utils.isEmptyString(ConfigCons.YUNJI_ROUTE_TYPE_URL)) {
            headers.put(RequestUtils.ROUTE_TYPE, ConfigCons.YUNJI_ROUTE_TYPE_URL);
        }
        return headers;
    }

    /**
     * 聊天接口请求头获取
     *
     * @param context
     * @return
     */
    public static Map<String, String> getChatHeader(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept-Charset", "utf-8");
        headers.put("Accept", "application/json");
        headers.put("Connection", "Keep-Alive");
        headers.put("Accept-Language", "zh-CN,en,*");
        headers.put("Cookie", "SESSION=" + ChatSpUtils.instance(context).getToken());
        headers.put("X-Requested-With", "XMLHttpRequest");//仿AJAX访问
        headers.put("User-Agent", "android/" + getVersionName(context));

        if (!Utils.isEmptyString(ConfigCons.YUNJI_BASE_HOST_URL)) {
            headers.put(RequestUtils.NATIVE_HOST, ConfigCons.YUNJI_BASE_HOST_URL);
        }
        if (!Utils.isEmptyString(ConfigCons.YUNJI_NATIVE_FLAG_URL)) {
            headers.put(RequestUtils.NATIVE_FLAG, ConfigCons.YUNJI_NATIVE_FLAG_URL);
        }
        if (!Utils.isEmptyString(ConfigCons.YUNJI_NATIVE_DOMAIN_URL)) {
            headers.put(RequestUtils.NATIVE_DOMAIN, ConfigCons.YUNJI_NATIVE_DOMAIN_URL);
        }
        if (!Utils.isEmptyString(ConfigCons.YUNJI_ROUTE_TYPE_URL)) {
            headers.put(RequestUtils.ROUTE_TYPE, ConfigCons.YUNJI_ROUTE_TYPE_URL);
        }
        return headers;
    }

    public static String parseResponseResult(String result) {
        if (TextUtils.isEmpty(result)) {
            return "";
        }
        if (result.equals("100")) {
            return "请求超时啦，请检查您的网络是否稳定";
        }
        return result;
    }

    /**
     * 判断网站是否在维护中
     *
     * @param result
     * @return
     */
    public static String isMaintenancing(String result) {
        try {

            if (!Utils.isEmptyString(result)) {
                String[] split = result.split("\\{");
                if (split.length > 1) {
                    String json = "{" + split[split.length - 1];
                    JSONObject rootObject = new JSONObject(json);
                    if (!rootObject.isNull("code") && rootObject.getString("code").equalsIgnoreCase("987200156")) {
                        if (rootObject.has("msg")) {
                            return rootObject.getString("msg");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 隐藏软键盘
     *
     * @param context
     * @param editText
     */
    public static void hideSoftInput(Context context, EditText editText) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param editText
     */
    public static void showSoftInput(Context context, EditText editText) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.requestFocus();
        manager.showSoftInput(editText, 0);
    }

    public static SysConfig getConfigFromJson(Context context) {
        String sysConfig = ChatSpUtils.instance(context).getSysConfig();
        if (Utils.isEmptyString(sysConfig)) {
            return new SysConfig();
        }
        SysConfig sc = new Gson().fromJson(sysConfig, SysConfig.class);
        if (sc == null)
            return new SysConfig();

        return sc;
    }


    public static List<String> splitString(String s, String format) {
        List<String> list = new ArrayList<>();
        if (Utils.isEmptyString(s)) {
            return list;
        }
        String[] ss;
        ss = s.split(format);
        for (int i = 0; i < ss.length; i++) {
            list.add(ss[i]);
        }
        return list;
    }

    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    public static String CheckViolateWord(List<String> violateWordsList, String txt) { //检查是否包含违禁词
        for (int temp = 0; temp < violateWordsList.size(); temp++) {
            if (txt.toLowerCase().contains(violateWordsList.get(temp).toLowerCase())) {
                if (violateWordsList.get(temp).length() > 1) {
                    int size = violateWordsList.get(temp).length() - 1;
                    return "您输入的词汇中包含敏感信息（" + violateWordsList.get(temp).substring(0, 1) + getStar(size) + "),请重新编辑";
                } else {
                    return "您输入的词汇中包含敏感信息（" + violateWordsList.get(temp).substring(0, 1) + "),请重新编辑";
                }
            }
        }
        return "";
    }

    public static String getStar(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append("*");
        }
        return sb.toString();
    }


    /**
     * 判断某个Activity 界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     * @return
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            String name = cpn.getClassName();
            if (name.endsWith(className)) {
                return true;
            }
        }

        return false;

    }

    /**
     * 根据彩种编号加载图片
     *
     * @param context
     * @param lotImageView
     * @param lotCode
     * @param lotIcon
     */
    public static void updateLocImage(Context context, ImageView lotImageView, String lotCode, String lotIcon) {
        String url;
        if (TextUtils.isEmpty(lotIcon)) {
            url = getLocImageUrl(lotCode);
        } else {
            url = lotIcon;
        }

        //彩种的图地址是根据彩种编码号为姓名构成的
        GlideUtils.loadChatImage(context, lotImageView, url, R.drawable.default_lottery);
    }


    public static String getLocImageUrl(String lotCode) {
        return ConfigCons.YUNJI_BASE_URL + ConfigCons.PORT + "/native/resources/images/" + lotCode + ".png";
    }

    public static int getRandomNum() {
        int num = (int) (Math.random() * 100);
        if (num > 80) {
            num = 80;
        }
        return num;
    }

    public static long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getNumbersFromShengXiaoName(Context context, String sxNumber, long time) {
        String[] numbers = null;
        if (time > 0) {
            numbers = getNumbersFromShengXiaoFromDate(context, time);
        } else {
            numbers = getNumbersFromShengXiao(context);
        }
        if (numbers != null) {
            for (String s : numbers) {
                if (s.contains(sxNumber)) {
                    String[] split = s.split("\\|");
                    if (split.length == 2) {
                        return split[0];
                    }
                }
            }
        }
        return "";
    }

    public static String[] getNumbersFromShengXiaoFromDate(Context context, long date) {

        String shenxiao = getShenxiaoFromDate(context, date);
        String[] arr = context.getResources().getStringArray(R.array.shengxiao_str);
        String[] results = new String[arr.length];
        int bmnIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            String item = arr[i];
            if (item.equals(shenxiao)) {
                bmnIndex = i;
                break;
            }
        }
        Utils.LOG("a", "bmnindex = " + bmnIndex);
        for (int i = 0; i < arr.length; i++) {
            int startNum = 0;
            if (i <= bmnIndex) {
                startNum = (bmnIndex - i) + 1;
            } else {
                startNum = (i - bmnIndex) - 1;
                startNum = 12 - startNum;
            }
            String numResult = arr[i] + "|" + getShenxiaoNumbers(startNum);
//            Utils.LOG("aa", "the numresult = " + numResult);
            results[i] = numResult;

        }
        return results;
    }

    public static String[] getNumbersFromShengXiao(Context context) {
        String shenxiao = getShengXiaoFromYear();
        String[] arr = context.getResources().getStringArray(R.array.shengxiao_str);
        String[] results = new String[arr.length];
        int bmnIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            String item = arr[i];
            if (item.equals(shenxiao)) {
                bmnIndex = i;
                break;
            }
        }
        Utils.LOG("a", "bmnindex = " + bmnIndex);
        for (int i = 0; i < arr.length; i++) {
            int startNum = 0;
            if (i <= bmnIndex) {
                startNum = (bmnIndex - i) + 1;
            } else {
                startNum = (i - bmnIndex) - 1;
                startNum = 12 - startNum;
            }
            String numResult = arr[i] + "|" + getShenxiaoNumbers(startNum);
//            Utils.LOG("aa", "the numresult = " + numResult);
            results[i] = numResult;

        }
        return results;
    }

    /**
     * 根据新历时间，获取对应年份的农历年下对应号码的生肖
     *
     * @param context
     * @param time
     * @return
     */
    public static String getShenxiaoFromDate(Context context, long time) {
        Calendar c = Calendar.getInstance();
        Date d = new Date(time);
        c.setTime(d);
        Lunbar lunbar = new Lunbar(c);
        String sx = lunbar.animalsYear();
        return sx;
    }

    private static String getShenxiaoNumbers(int startIndex) {
        int maxValue = 49;
        int tmp = startIndex;
        StringBuilder sb = new StringBuilder();
        while (tmp <= maxValue) {
            sb.append(String.format("%02d", tmp)).append(",");
            tmp += 12;
        }
        if (sb.length() > 0) {
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


    public static void setListViewHeightBasedOnChildren(GridView gridView, int columnNum, int offset) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int count = listAdapter.getCount();
        if (count == 0) {
            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = 0;
            gridView.setLayoutParams(params);
            return;
        }
        int hightCount = count / columnNum;
        if (hightCount == 0) {
            hightCount++;
        } else {
            if (count % columnNum > 0) {
                hightCount++;
            }
        }
        for (int i = 0, len = hightCount; i < len; i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            // 4.4以下版本会报空指针异常（RelativeLayout布局）
            listItem.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + offset * hightCount;
        gridView.setLayoutParams(params);
    }

    /**
     * 将double转为取NUM小数的字段
     *
     * @param numDouble
     * @param size      小数点后几位
     */
    public static String getDoubleToString(Double numDouble, int size) {
        String result = "";
        BigDecimal bd = new BigDecimal(numDouble);
        BigDecimal bd1 = bd.setScale(size, BigDecimal.ROUND_HALF_UP);
        double d = Double.parseDouble(bd1.toString());
        result = String.valueOf(d);
        return result;
    }

    /**
     * 随机生成指定length长度的字符串
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        int i = new Random().nextInt(sb.length() - 1);
        sb.insert(i, "***");
        return sb.toString();
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

}
