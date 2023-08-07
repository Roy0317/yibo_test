package com.simon.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Vibrator;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 与电话相关功能的工具类
 *
 * @author canyinghao
 */
public class PhoneHelper {
    private Context ctx;
    private static PhoneHelper util;


    synchronized public static PhoneHelper getInstance(Context ctx) {
        if (util == null) {
            util = new PhoneHelper(ctx);
        }
        return util;

    }

    private PhoneHelper(Context ctx) {
        super();
        this.ctx = ctx;
    }

    /**
     * 生产商家
     *
     * @return
     */
    public String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获得固件版本
     *
     * @return
     */
    public String getRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获得手机型号
     *
     * @return
     */
    public String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获得手机品牌
     *
     * @return
     */
    public String getBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机运营商
     */
    public String getSimOperatorName() {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        return tm.getSimOperatorName();
    }

    /**
     * 得到本机手机号码,未安装SIM卡或者SIM卡中未写入手机号，都会获取不到
     *
     * @return
     */
    public String getThisPhoneNumber() {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            String number = tm.getLine1Number();
            return number;
        }

        return "";
    }

    /**
     * 是否是电话号码
     *
     * @param phonenumber
     * @return
     */
    public boolean isPhoneNumber(String phonenumber) {
        Pattern pa = Pattern.compile("^[1][3,4,5,8,7][0-9]{9}$");
        Matcher ma = pa.matcher(phonenumber);
        return ma.matches();
    }


    /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return "";
        }
    }


    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }


    /**
     * 发短信
     *
     * @param phone
     * @param content
     * @param content
     */
    public void doSMS(String phone, String content) {
        Uri uri = null;
        if (!TextUtils.isEmpty(phone))
            uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        ctx.startActivity(intent);
    }

    /**
     * 得到屏幕信息 getScreenDisplayMetrics().heightPixels 屏幕高
     * getScreenDisplayMetrics().widthPixels 屏幕宽
     *
     * @return
     */
    public DisplayMetrics getScreenDisplayMetrics() {
        WindowManager manager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = manager.getDefaultDisplay();
        display.getMetrics(displayMetrics);

        return displayMetrics;

    }


    /**
     * 检测网络是否可用
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }


    /**
     * 调用浏览器打开
     *
     * @param url
     */
    public void openWeb(String url) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }


    /**
     * 是否有外存卡
     *
     * @return
     */
    public boolean isExistExternalStore() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到sd卡路径
     *
     * @return
     */
    public String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 得到网络类型，0是未知或未连上网络，1为WIFI，2为2g，3为3g，4为4g
     *
     * @return
     */
    public int getNetType() {
        ConnectivityManager connectMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        int type = 0;
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return type;
        }

        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                type = 1;
                break;
            case ConnectivityManager.TYPE_MOBILE:
                type = getNetworkClass(info.getSubtype());
                break;

            default:
                type = 0;
                break;
        }
        return type;
    }


    /**
     * 判断数据连接的类型
     *
     * @param networkType
     * @return
     */
    public int getNetworkClass(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:

                return 2;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return 3;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return 4;
            default:
                return 0;
        }
    }

    /**
     * 开始震动
     *
     * @param context
     * @param repeat  0重复 -1不重复
     * @param pattern
     */
    @SuppressLint("NewApi")
    public synchronized void doVibrate(Context context, int repeat, long... pattern) {

        if (pattern == null) {
            pattern = new long[]{1000, 1000, 1000};
        }
        Vibrator mVibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        if (mVibrator.hasVibrator()) {
            mVibrator.vibrate(pattern, repeat);
        }

    }


    /**
     * 开始震动
     *
     * @param pattern 震动时间
     */
    @SuppressLint("NewApi")
    public synchronized void doVibrate(long pattern) {
        Vibrator mVibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
        mVibrator.vibrate(pattern);
    }


}
