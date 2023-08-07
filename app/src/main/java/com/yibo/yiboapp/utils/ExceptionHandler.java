package com.yibo.yiboapp.utils;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import com.yibo.yiboapp.data.YiboPreference;

public class ExceptionHandler implements UncaughtExceptionHandler {

    public static final String TAG = "ExceptionHandler";

    private UncaughtExceptionHandler mDefaultHandler;
    private static ExceptionHandler INSTANCE = null;
    private Context mContext;
    private YiboPreference preference = null;
    private static Map<String, String> infos;

    private ExceptionHandler(Context context) {
        mContext = context;
        preference = YiboPreference.instance(context);
    }

    public static ExceptionHandler getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ExceptionHandler(context);
        }
        return INSTANCE;
    }

    /**
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        collectDeviceInfo(mContext);
        saveCrashInfo2File(ex);
        return false;
    }

    /**
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        infos = new HashMap<>();
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("VERSION_NAME", versionName);
                infos.put("VERSION_CODE", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        if (null != preference.getUsername()) {
            String userName = preference.getUsername();
            infos.put("USER_NAME", userName);
        }
        infos.put("ANDROID_VERSION", PhoneInfo.getReleaseVersion());
        infos.put("project", "yibo");
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    public boolean saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            //String time = Utility.getLogTime(timestamp);
            //String fileName = "Crash"+ time + ".txt";
            preference.setLastCrashTime(timestamp);
            if (Utils.isSDCardExisted()) {
                String path = Utils.createFilepath(Utils.DIR_CATEGORY.LOG, "crashDump.txt");
                //File file = new File(path);
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return false;
    }
}
