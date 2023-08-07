package com.yibo.yiboapp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.simon.utils.LogUtil;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.DatabaseUtils;
import com.yibo.yiboapp.data.RequestEventTrack;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BallListItemInfo;
import com.yibo.yiboapp.entify.SysConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import crazy_wrapper.Crazy.CrazyConstant;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;


/**
 * help util tool class for logic control
 *
 * @author johnson
 */
public class Utils {

    public static final boolean DEBUG = BuildConfig.DEBUG;
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static String YIBO_DIR = Environment.getExternalStorageDirectory() + "/ybandroid";

    public enum DIR_CATEGORY {
        IMAGE, TEMP, THUMBNAIL, LOG, AUDIO, CACHE, VIDEO, JSON, RECOGNIZE
    }

    public static void LOG(String TAG, String txt) {
        if (DEBUG) {
            Log.d(TAG, txt);
        }
    }

    public static void logAll(String TAG, String message) {
        if (DEBUG) {
            if (message.length() > 3000) {
                int chunkCount = message.length() / 3000;     // integer division
                for (int i = 0; i <= chunkCount; i++) {
                    int min = 3000 * i;
                    int max = min + 3000;
                    if (min < message.length()) {
                        if (max >= message.length()) {
                            Log.d(TAG, "chunk " + i + ":*** " + message.substring(3000 * i));
                        } else {
                            Log.d(TAG, "chunk " + i + ":*** " + message.substring(3000 * i, max));
                        }
                    }
                }
            } else {
                Log.d(TAG, message);
            }
        }
    }

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static boolean isEmptyString(String s) {
        if (s == null || s.equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }


    /**
     * 一串字符是否全为字母
     *
     * @param fstrData
     * @return
     */
    public static boolean isChar(String fstrData) {

        for (int i = 0; i < fstrData.length(); i++) {
            char c = fstrData.charAt(i);
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                return false;
            }
        }
        return true;
    }

    public static String getBuildModel() {
        return Build.BRAND + "/" + Build.MODEL;
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null)
            return null;

        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap bytes2Bimap(byte[] b) {
        try {
            if (b.length > 0) {
                return BitmapFactory.decodeByteArray(b, 0, b.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sync cookie share when use webview
     * but there exist safety question relative to http.
     *
     * @param context
     * @param url
     */
    public static void syncCookie(Context context, String url) {
        if (Utils.isEmptyString(url)) {
            return;
        }
        CookieSyncManager csm = CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.removeSessionCookie();
        cm.setCookie(url, "SESSION=" + YiboPreference.instance(context).getToken());
        csm.sync();
    }

    public static void clearSyncCookie(Context context) {
        //CookieSyncManager csm = CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.removeSessionCookie();
    }

    /**
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void writeLogToFile(String tag, String log) {
        if (DEBUG) {
            Log.d(tag, log);
        }
        long time = System.currentTimeMillis();
        String content = time + "    " + tag + " " + log + "\r\n";
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            byte[] buffer = content.getBytes();
            appendFileContent(createFilepath(DIR_CATEGORY.LOG, formatLogTime(System.currentTimeMillis()) + "_log.txt"), buffer);
        }
    }

    public static String formatLogTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    private static boolean appendFileContent(String fileName, byte[] buffer) {

        try {
            FileOutputStream fos = new FileOutputStream(fileName, true);
            fos.write(buffer);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static final boolean dirExist() {
        boolean ok = true;
        String dir = YIBO_DIR;
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        File destDir = new File(dir);
        if (!destDir.exists()) {
            ok = destDir.mkdirs();
        }
        return ok;
    }

    public static String dirName(DIR_CATEGORY dir) {
        if (dir == DIR_CATEGORY.IMAGE) {
            return "Image/";
        } else if (dir == DIR_CATEGORY.TEMP) {
            return "Temp/";
        } else if (dir == DIR_CATEGORY.THUMBNAIL) {
            return "THumbnail/";
        } else if (dir == DIR_CATEGORY.LOG) {
            return "Log/";
        } else if (dir == DIR_CATEGORY.AUDIO) {
            return "Audio/";
        } else if (dir == DIR_CATEGORY.CACHE) {
            return "Cache/";
        } else if (dir == DIR_CATEGORY.VIDEO) {
            return "Video/";
        } else if (dir == DIR_CATEGORY.JSON) {
            return "Json/";
        } else if (dir == DIR_CATEGORY.RECOGNIZE) {
            return "Recognize/";
        }
        return "";
    }

    public static String createFilepath(DIR_CATEGORY dir, String filename) {

        if (isEmptyString(filename)) {
            return null;
        }
        boolean ok = dirExist();
        if (!ok)
            return null;
        String filePath = YIBO_DIR + "/" + dirName(dir);
        File f = new File(filePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        filePath += filename;
        return filePath;
    }

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

    public static void shakeView(Context context, View view) {
        if (view != null) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.shake_x);
            view.startAnimation(animation);
        }
    }

    public static List<String> splitString(String s, String format) {
        List<String> list = new ArrayList<String>();
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

    public static String formatTime(long time) {

        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
        Date date = new Date(time);
        String LgTime = sdformat.format(date);
        return LgTime;
    }

    public static String formatBeijingTime(long time) {

        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
        sdformat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(time);
        String LgTime = sdformat.format(date);
        return LgTime;
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

    public static String formatTicketTime(long time) {

        SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");// 24小时制
        Date date = new Date(time);
        String LgTime = sdformat.format(date);
        return LgTime;
    }

    public static String formatTime(long time, String format) {
        SimpleDateFormat sdformat = new SimpleDateFormat(format);// 24小时制
        Date date = new Date(time);
        String LgTime = sdformat.format(date);
        return LgTime;
    }

    public static String int2Time(long timenumber) {
        StringBuffer sb = new StringBuffer();
        timenumber = timenumber / 1000;
        int offset = 1;
        //if time duration large than one day
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (timenumber >= 24 * 60 * 60) {
            int day = (int) (timenumber / (24 * 3600));
            sb.append(day).append("天");
            offset = 24 * 3600;
            hour = (int) (timenumber % offset / 3600);
            minute = (int) (timenumber % offset % 3600 / 60);
            second = (int) (timenumber % offset % 3600 % 60);
        } else {
            hour = (int) (timenumber / 3600);
            minute = (int) (timenumber % 3600 / 60);
            second = (int) (timenumber % 3600 % 60);
        }

        if (hour > 9) {
            sb.append(hour).append(":");
        } else {
            sb.append(0).append(hour).append(":");
        }
        if (minute > 9) {
            sb.append(minute).append(":");
        } else {
            sb.append(0).append(minute).append(":");
        }
        if (second > 9) {
            sb.append(second);
        } else {
            sb.append(0).append(second);
        }
        return sb.toString();
    }

    public static void setListViewHeightBasedOnChildren(GridView gridView, int columnNum) {
        setListViewHeightBasedOnChildren(gridView, columnNum, 0);
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
     * random generate lottery ballon numbers
     *
     * @param allowRepeat 是否允许重复号码
     * @param numCount    选择几个号码
     * @param format      号码字符串分隔格式
     * @param numStr      被随机选择的字符串
     * @return
     */
    public static List<String> randomNumbers(String numStr, boolean allowRepeat, int numCount, String format) {

        List<String> results = Utils.splitString(numStr, format);
        if (results == null || results.isEmpty()) {
            return null;
        }
        int maxNum = results.size();
        int i;
        int count = 0;
        List<String> numbers = new ArrayList<String>();
        Random r = new Random();
        while (count < numCount) {
            i = Math.abs(r.nextInt(maxNum));
            if (!allowRepeat) {
                if (i == maxNum) {
                    i--;
                }
                if (numbers.contains(results.get(i))) {
                    continue;
                }
            }
            numbers.add(results.get(i));
            count++;
        }
        results.clear();
        return numbers;
    }

    public static DisplayMetrics screenInfo(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }

    /**
     * 读取Area json
     *
     * @param fileName
     * @return
     */
    public static String readAssetJson(Context context, String fileName) {
        if (Utils.isEmptyString(fileName)) {
            return null;
        }
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale * dipValue + 0.5f);
    }

    public static void setListViewHeight(ListView listview) {
        ListAdapter listAdapter = listview.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int count = listAdapter.getCount();
        for (int i = 0, len = count; i < len; i++) {
            View listItem = listAdapter.getView(i, null, listview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + listview.getDividerHeight() * (count - 1);
        }
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        params.height = totalHeight;
        listview.setLayoutParams(params);
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static String getMD5(String val) throws NoSuchAlgorithmException {
        String s = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(val.getBytes());
        byte tmp[] = md5.digest();
        char str[] = new char[16 * 2];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte byte0 = tmp[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        s = new String(str);
        return s;
    }

    public static boolean limitAccount(String account) {
        String pwdRegx = "^[a-zA-Z0-9]{5,11}$";
        Pattern pattern = Pattern.compile(pwdRegx);
        Matcher isNum = pattern.matcher(account);
        if (!isNum.matches()) {
            return false;
        }
        if (account.length() < 5 || account.length() > 11) {
            return false;
        }
        String SPECIAL = "`~!@#$%^&*()+=|{};:'\"<>,.";
        char[] arrays = SPECIAL.toCharArray();
        for (int i = 0; i < arrays.length; i++) {
            char s = arrays[i];
            if (account.contains(String.valueOf(s))) {
                return false;
            }
        }
        return true;
    }

    public static boolean limitPwd(String password) {
        String pwdRegx = ".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]";
        Pattern pattern = Pattern.compile(pwdRegx);
        Matcher isNum = pattern.matcher(password);
        if (!isNum.matches()) {
            return false;
        }
        if (password.length() < 6 || password.length() > 16) {
            return false;
        }
        String SPECIAL = "`~!@#$%^&*()+=|{};:'\"<>,.";
        char[] arrays = SPECIAL.toCharArray();
        for (int i = 0; i < arrays.length; i++) {
            char s = arrays[i];
            if (password.contains(String.valueOf(s))) {
                return false;
            }
        }
        return true;
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
     * 隐藏字符串
     *
     * @param str               原字符串
     * @param showBackCharCount 保留后几位字符
     * @return
     */
    public static String hideChar(String str, int showBackCharCount) {
        if (isEmptyString(str)) {
            return "";
        }
        if (str.length() < showBackCharCount + 1) {
            return str;
        }
        String aaa = "";
        for (int i = 0; i < str.length() - showBackCharCount; i++) {
            aaa += "*";
        }
        return aaa + str.substring(str.length() - showBackCharCount, str.length());
    }

    /**
     * 隐藏字符串
     *
     * @param str       原字符串
     * @param showCount 显示前面几位
     * @return
     */
    public static String hideTailChar(String str, int showCount) {
        if (isEmptyString(str)) {
            return "";
        }
        if (showCount >= str.length()) {
            return str;
        }
        String aaa = "";
        for (int i = showCount; i < str.length(); i++) {
            aaa += "*";
        }
        return str.substring(0, showCount) + aaa;
    }

    public static boolean isSDCardExisted() {
        boolean isSDCardExisted = false;
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            isSDCardExisted = true;
        }
        return isSDCardExisted;
    }

    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }


    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static double change(double a) {
        return a * Math.PI / 180;
    }

    public static double changeAngle(double a) {
        return a * 180 / Math.PI;
    }

    public static String keepMoneyChange(String money) { //保留金额的两位小数
        String mon = money;
        String[] arr = money.split("\\.");
        if (arr.length > 1) {
            if (arr[1].length() > 2) {
                int num = Integer.parseInt(arr[1].substring(0, 2));
                int temp = Integer.parseInt(arr[1].substring(2, 3));
                if (temp >= 5) {
                    num++;
                }
                mon = arr[0] + "." + num;
            }
        }
        return mon;
    }


    public static String bigNum(float d) {
        NumberFormat nf = NumberFormat.getInstance();
        // 是否以逗号隔开, 默认true以逗号隔开,如[123,456,789.128]
        nf.setGroupingUsed(false);
        // 结果未做任何处理
        return nf.format(d);
    }

    public static boolean judgeDate(String fData, String sData) { //第二个时间是否大于第一个时间,可相等  2019-03-15  2018-01-01
        boolean isOk = false;
        String[] fArr = fData.split("-");
        String[] sArr = sData.split("-");
        if (Integer.parseInt(fArr[0]) < Integer.parseInt(sArr[0])) {
            isOk = true;
        } else if (Integer.parseInt(fArr[0]) == Integer.parseInt(sArr[0])) {
            if (Integer.parseInt(fArr[1]) < Integer.parseInt(sArr[1])) {
                isOk = true;
            } else if (Integer.parseInt(fArr[1]) == Integer.parseInt(sArr[1])) {
                if (Integer.parseInt(fArr[2]) <= Integer.parseInt(sArr[2])) {
                    isOk = true;
                }

            }
        }
        return isOk;

    }

    public static boolean judgeTime(String fData, String sData) { //第二个时间是否大于第一个时间,可相等  2019-03-15 24:00  2018-01-01 04:10
        boolean isOk = false;
        String[] fArr = fData.substring(0, 10).split("-");
        String[] sArr = sData.substring(0, 10).split("-");
        if (Integer.parseInt(fArr[0]) < Integer.parseInt(sArr[0])) {
            isOk = true;
        } else if (Integer.parseInt(fArr[0]) == Integer.parseInt(sArr[0])) {
            if (Integer.parseInt(fArr[1]) < Integer.parseInt(sArr[1])) {
                isOk = true;
            } else if (Integer.parseInt(fArr[1]) == Integer.parseInt(sArr[1])) {
                if (sArr.length > 2) {
                    if (Integer.parseInt(fArr[2]) < Integer.parseInt(sArr[2])) {
                        isOk = true;
                    } else if (Integer.parseInt(fArr[2]) == Integer.parseInt(sArr[2])) {
                        String[] fTimeArr = fData.substring(11, fData.length()).split(":");
                        String[] sTimeArr = sData.substring(11, fData.length()).split(":");
                        if (Integer.parseInt(fTimeArr[0]) < Integer.parseInt(sTimeArr[0])) {
                            isOk = true;
                        } else if (Integer.parseInt(fTimeArr[0]) == Integer.parseInt(sTimeArr[0])) {
                            if (Integer.parseInt(fTimeArr[1]) < Integer.parseInt(sTimeArr[1])) {
                                isOk = true;
                            }
                        }
                    }
                }
            }
        }
        return isOk;

    }

    /**
     * 判断时间间隔是否大于31秒天
     *
     * @param sData
     * @return
     */
    public static boolean timeoffset(String sData, String eData) {
        boolean isOk = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date d1 = dateFormat.parse(sData);
            Date d2 = dateFormat.parse(eData);
            long day = (d2.getTime() - d1.getTime()) / 1000 / 60 / 60 / 24;
            if (day >= 31) {
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isOk;

    }

    public static String Utf8Text(String txt) {
        String value = txt.replaceAll("/", "%2f").replaceAll("%252f", "%2f")
                .replaceAll("\\+", "%2b").replaceAll("=", "%3d");
        return value;
    }

    /**
     * 判断一个字符串是否全为数字
     *
     * @param strNum
     * @return
     */
    public static boolean isDigit2(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) strNum);
        return matcher.matches();
    }

    public static String keepTwoDecimal(float f) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(f);
    }


    /**
     * 判断指定的activity不是在栈顶
     */
    public static boolean isActivityOnTop(Activity activity) {
        return ActivityUtils.getTopActivity().getLocalClassName().equals(activity.getLocalClassName());
    }


    /**
     * 判断某个界面是否在前台
     *
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }


    /**
     * double转String,保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    public static String getDateFormat(long currentTime, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(currentTime);
    }

    public static String getMoney(String money) {
        return getMoney(money, false);
    }

    public static String getMoney(String money, boolean isNeedsymbol) {
        return getMoney(money, isNeedsymbol, 2);
    }

    /**
     * @param money
     * @param isNeedsymbol 是否需要单位  元
     * @param newScale     保留的小数位
     * @return
     */
    public static String getMoney(String money, boolean isNeedsymbol, int newScale) {
        if (TextUtils.isEmpty(money)) {
            if (isNeedsymbol) {
                return "0 元";
            } else {
                return "0";
            }
        }

        double price = parseDouble(money);

        String temp = String.valueOf(new BigDecimal(price).setScale(newScale, BigDecimal.ROUND_HALF_UP));
        int last = Integer.parseInt(temp.substring(temp.lastIndexOf(".") + 1));
        if (last == 0) {
            temp = temp.substring(0, temp.lastIndexOf("."));
        } else if (temp.substring(temp.length() - 1).equals("0")) {
            temp = temp.substring(0, temp.length() - 1);
        }
        if (isNeedsymbol) {
            return temp + " 元";
        } else {
            return temp;
        }
    }

    public static double parseDouble(String s) {
        double douNum = 0.00;
        try {
            douNum = Double.parseDouble(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return douNum;
    }

    /**
     * 获得当前手机的手机高度
     */
    public static int getDensityHeight(Context ctx) {
        int height;
        DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
        boolean hasNav = checkDeviceHasNavigationBar(ctx);
        if (hasNav) {
            height = dm.heightPixels - getNavigationBarHeight(ctx);
        } else {
            height = dm.heightPixels;
        }

        return height;

    }

    /**
     * 检测是否有虚拟按键
     *
     * @param ctx
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context ctx) {
        boolean hasNavigationBar = false;
        Resources rs = ctx.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }

        return hasNavigationBar;

    }

    /**
     * 获取虚拟按键的高度
     *
     * @param ctx
     * @return
     */
    public static int getNavigationBarHeight(Context ctx) {
        int navigationBarHeight = 0;
        Resources rs = ctx.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(ctx)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    public static boolean isTestPlay(Context context) { //是否是试玩
        if (YiboPreference.instance(context).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
            Toast.makeText(context, "操作权限不足，请联系客服！", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    public static boolean isTestPlay(Context context, boolean showToast) { //是否是试玩
        if (YiboPreference.instance(context).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
            if (showToast) {
                Toast.makeText(context, "操作权限不足，请联系客服！", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return false;
        }
    }

    public static String cleanHtml(String text) { //去除体育详情里面的html代码
        if (text.contains("<font")) {
            text = text.replaceAll("</font>", "");
            int s = text.indexOf("<font");
            int e = text.indexOf(">", s);
            String temp = text.substring(0, s) + text.substring(e + 1);
            return temp;
        } else {
            return text;
        }
    }

    /**
     * 开启或者关闭原生聊天室
     *
     * @param ctx
     * @return
     */
    public static boolean onOrOffOriginalChat(Context ctx) {
        SysConfig config = UsualMethod.getConfigFromJson(ctx);
        if (config != null && !TextUtils.isEmpty(config.getNative_chat_room()) && "on".equals(config.getNative_chat_room())) {
            return true;
        }
        return false;
    }


    public static int bankCardIcon(String name) {
        if (name.contains("建设")) {
            return R.drawable.js_bank_card_icon;
        } else if (name.equals("中国银行")) {
            return R.drawable.china_bank_card_icon;
        } else if (name.contains("工商")) {
            return R.drawable.gs_bank_card_icon;
        } else if (name.contains("农业")) {
            return R.drawable.ny_bank_card_icon;
        } else {
            return R.drawable.default_pay_icon;
        }
    }

    public static String getIpAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 3/4g网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
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

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  wifi网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                return ipAddress;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // 有限网络
                return getLocalIp();
            }
        }
        return null;
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    // 获取有限网IP
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface

                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "0.0.0.0";

    }

    public static String getIp() {
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(CrazyConstant.GET_IP_URL);
            URLConnection URLconnection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                LogUtil.e("ip获取成功");
                InputStream in = httpConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader bufr = new BufferedReader(isr);
                String str;
                while ((str = bufr.readLine()) != null) {
                    builder.append(str);
                }
                bufr.close();
                return builder.toString();
            } else {
                LogUtil.e("ip获取失败");
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断是否后台手动添加的试玩帐号
     *
     * @param ctx
     * @return
     */
    public static boolean shiwanFromMobile(Context ctx) {
        if (YiboPreference.instance(ctx).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
            String username = YiboPreference.instance(ctx).getUsername();
            if (username.startsWith("guest")) {
                return true;
            }
        }
        return false;
    }

    public static Single<File> createClickEventLog(Context context) {
        return Single.create((SingleOnSubscribe<File>) emitter -> {
            StringBuilder sb = new StringBuilder();
            List<RequestEventTrack> events = DatabaseUtils.getInstance(context).getEventTracks();

            for(RequestEventTrack event: events){
                sb.append(event.getEvent()).append("\n")
                        .append(event.getTimestamp()).append("\n")
                        .append(event.getUrl()).append("\n\n")
                        .append(event.getHeadersString()).append("\n")
                        .append("status code = ").append(event.getStatusCode()).append("\n")
                        .append("response = ").append(event.getResponse()).append("\n\n")
                        .append("-------------------------------------------------------------\n");
            }
            String content = sb.toString();

            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + String.format("/%s_log/", context.getString(R.string.app_name)));
            if (!dir.exists())
                dir.mkdir();

            String fileName = new SimpleDateFormat("yyyy-MM-dd_HHmm").format(System.currentTimeMillis()) + ".txt";
            File file = new File(dir, fileName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
                emitter.onSuccess(file);
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }
}
