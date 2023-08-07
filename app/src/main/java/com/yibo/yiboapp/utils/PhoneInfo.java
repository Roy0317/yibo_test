package com.yibo.yiboapp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

public class PhoneInfo {

    private static final String TAG = "PhoneInfo";

    public static final int VERSION_CODES_ECLAIR_MR1 = 7;      // Android 2.1
    public static final int VERSION_CODES_FROYO = 8;          // Android 2.2
    public static final int VERSION_CODES_GINGERBREAD = 9;      // Android 2.3
    public static final int VERSION_CODES_GINGERBREAD_MR1 = 10;    // Android 2.3.3
    public static final int VERSION_CODES_HONEYCOMB = 11;      // Android 3.0
    public static final int VERSION_CODES_HONEYCOMB_MR1 = 12;    // Android 3.1
    public static final int VERSION_CODES_HONEYCOMB_MR2 = 13;    // Android 3.2
    public static final int VERSION_CODES_ICE_CREAM_SANDWICH = 14;  // Android 4.0
    public static final int VERSION_CODES_KITKAT = 19;        // Android 4.4
    public static final int VERSION_MALLOW = 23;        // Android 6.0

    public static final String SAMSUNG_NAME = "samsung";

    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getReleaseVersion() {
        return Build.VERSION.RELEASE;
    }

    public static boolean isSumsung() {
        if (SAMSUNG_NAME.equalsIgnoreCase(Build.MANUFACTURER)) {
            return true;
        }
        return false;
    }

    public static boolean isHtcD816t() {
        if ("HTC".equalsIgnoreCase(getManufacturer())) {
            if ("HTC D816t".equalsIgnoreCase(getPhoneModel())) {
                return true;
            }
        }
        return false;
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static boolean isCommaSpecialHandle() {
        boolean isSpecialHandle = false;

        if ("YuLong".equalsIgnoreCase(getManufacturer())) {
            if ("9120".equalsIgnoreCase(getPhoneModel()) || "5855".equalsIgnoreCase(getPhoneModel())) {
                isSpecialHandle = true;
            }
        }

        return isSpecialHandle;
    }

    public static boolean isSepicalPhone() {
        boolean isSpecialHandle = false;

        if ("HUAWEI".equalsIgnoreCase(getManufacturer())) {
            if ("MT7-TL00".equalsIgnoreCase(getPhoneModel())) {
                isSpecialHandle = true;
            }
        }

        if ("HUAWEI".equalsIgnoreCase(getManufacturer())) {
            if ("Note 2".equalsIgnoreCase(getPhoneModel())) {
                isSpecialHandle = true;
            }
        }

        return isSpecialHandle;
    }

    public static boolean isSpecialHandle(Context context) {
        boolean isSpecialHandle = false;

        String manufacturer = getManufacturer();

        if ("alps".equalsIgnoreCase(manufacturer)) {
            isSpecialHandle = true;
        } else if ("HUAWEI".equalsIgnoreCase(manufacturer)) {
            isSpecialHandle = true;
        } else if ("GiONEE".equalsIgnoreCase(manufacturer)) {
            isSpecialHandle = true;
        }

        return isSpecialHandle;
    }

    // IMEI
    public static String getPhoneDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    // IMSI
    public static String getSubscriberId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

    public static boolean isIMSIValid(String imsi) {
        boolean isValid = false;
        if (!Utils.isEmptyString(imsi)) {
            if (imsi.startsWith("4600") && imsi.length() == 15) {
                isValid = true;
            }
        }

        return isValid;
    }

    public static String getMacAddress(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wm.getConnectionInfo().getMacAddress();
    }

    public static String getSimOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String strSimType = "";
        int simState = tm.getSimState();
        if (TelephonyManager.SIM_STATE_READY == simState) {
            strSimType = tm.getSimOperatorName();
        }

        return strSimType;
    }

    public static String getNetworkOperator(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tm.getNetworkOperator();
        Utils.LOG(TAG, "networkOperator = " + networkOperator);

        return networkOperator;
    }

    public static String getSimOperator(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simOperator = "";
        int simState = tm.getSimState();
        if (TelephonyManager.SIM_STATE_READY == simState) {
            simOperator = tm.getSimOperator();
        }
        Utils.LOG(TAG, "simOperator = " + simOperator);

        return simOperator;
    }

    public static boolean isChinaTelecomSim(Context context) {
        boolean isChinaTelecomSim = false;
        String networkOperator = getNetworkOperator(context);
        if ("46003".equals(networkOperator)) {
            isChinaTelecomSim = true;
        }
        Utils.LOG(TAG, "isChinaTelecomSim = " + isChinaTelecomSim);

        return isChinaTelecomSim;
    }

    public static boolean isEmulator(Context context) {
        boolean isEmulator = false;
        String deviceId = getPhoneDeviceId(context);
        if (null == deviceId || "000000000000000".equals(deviceId)) {
            String model = getPhoneModel();
            String manufacturer = getManufacturer();
            if (("sdk".equals(model)) && ("unknown".equals(manufacturer))) {
                isEmulator = true;
            }
        }

        return isEmulator;
    }

    public static boolean isSupportRecording() {
        boolean isSupport = false;
        if (getSdkVersion() <= VERSION_CODES_ECLAIR_MR1) {
            isSupport = false;
        } else {
            isSupport = true;
        }
        return isSupport;
    }

    public static boolean isSupportContactSortKey() {
        boolean isSupport = false;
        if (getSdkVersion() >= VERSION_CODES_FROYO) {
            isSupport = true;
        }
        return isSupport;
    }

    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tm.getLine1Number();
        //		Utility.writeLogToFile(TAG, "phoneNumber = " + phoneNumber);
        return phoneNumber;
    }

    public static String getBaseBandVersion() {
        String strBaseBandVersion = null;

        try {
            @SuppressWarnings("rawtypes") Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[] {String.class, String.class});
            Object result = m.invoke(invoker, new Object[] {"gsm.version.baseband", "unknown"});
            strBaseBandVersion = (String) result;
        } catch (Exception e) {
        }

        return strBaseBandVersion;
    }

    public static String getKernelVersion() {
        String kernerVersion = null;
        Process process = null;

        try {
            process = Runtime.getRuntime().exec("cat /proc/version");
        } catch (IOException e) {

        }
        // get the output line
        InputStream outs = process.getInputStream();
        InputStreamReader isrout = new InputStreamReader(outs);
        BufferedReader brout = new BufferedReader(isrout, 8 * 1024);
        String result = "";
        String line;
        // get the whole standard output string
        try {
            while ((line = brout.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {

        }

        if (result != "") {
            String Keyword = "version ";
            int index = result.indexOf(Keyword);
            line = result.substring(index + Keyword.length());
            //			index = line.indexOf(" ");
            //			kernerVersion = line.substring(0, index);
            kernerVersion = line;
        }

        return kernerVersion;
    }

    public static String getInternalVersion() {
        String internalVersion = Build.VERSION.INCREMENTAL;
        return internalVersion;
    }

    public static boolean isKitkat(){
        if(Build.VERSION.SDK_INT >= 19){
            return true;
        }
        return false;
    }
}
