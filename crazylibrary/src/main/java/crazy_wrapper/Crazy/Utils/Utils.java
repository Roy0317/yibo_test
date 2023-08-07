package crazy_wrapper.Crazy.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import com.meiya.cunnar.crazy.crazylibrary.BuildConfig;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import crazy_wrapper.Crazy.CrazyConstant;

public class Utils {

    public static final String SDCARD_ROOT = Environment.getExternalStorageDirectory() + "/crazy_library";
    public static final boolean DEBUG = BuildConfig.DEBUG;
    public static final String CHAR_FORMAT = "utf-8";
    public static final String DEFAULT_SEPARATOR = ",";


    /**
     * tool useful interfaces associate to crazy framework
     *
     * @author zhangy
     */
    public enum DIR_CATEGORY {
        IMAGE, TEMP, THUMBNAIL, LOG, AUDIO, CACHE, VIDEO, JSON, RECOGNIZE
    }

    public static void LOG(String TAG, String txt) {
        if (DEBUG) {
            Log.d(TAG, txt);
        }
    }

    public static void logd(String TAG, String message) {
        if (DEBUG) {
            if (message.length() > 3000) {
                int chunkCount = message.length() / 3000;     // integer division
                for (int i = 0; i <= chunkCount; i++) {
                    int max = 3000 * (i + 1);
                    if (max >= message.length()) {
                        Log.d(TAG, "chunk " + i + " of " + chunkCount + ":*** " + message.substring(3000 * i));
                    } else {
                        Log.d(TAG, "chunk " + i + " of " + chunkCount + ":*** " + message.substring(3000 * i, max));
                    }
                }
            } else {
                Log.d(TAG, message);
            }
        }
    }

    private static Map<String, Object> getDomainIp(String _dormain) {
        Map<String, Object> map = new HashMap<String, Object>();
        long start = 0;
        long end = 0;
        String time = null;
        InetAddress[] remoteInet = null;
        try {
            start = System.currentTimeMillis();
            remoteInet = InetAddress.getAllByName(_dormain);
            if (remoteInet != null) {
                end = System.currentTimeMillis();
                time = (end - start) + "";
            }
        } catch (UnknownHostException e) {
            end = System.currentTimeMillis();
            time = (end - start) + "";
            remoteInet = null;
            e.printStackTrace();
        } finally {
            map.put("remoteInet", remoteInet);
            map.put("useTime", time);
        }
        return map;
    }

    public static String getIPFromDns(String domain) {
        Map<String, Object> map = getDomainIp(domain);
//        String useTime = (String) map.get("useTime");
        if (map == null) {
            return "";
        }
        if (!map.containsKey("remoteInet")) {
            return "";
        }
        InetAddress[] _remoteInet = (InetAddress[]) map.get("remoteInet");
//        String timeShow = null;
//        if (Integer.parseInt(useTime) > 5000) {// 如果大于1000ms，则换用s来显示
//            timeShow = " (" + Integer.parseInt(useTime) / 1000 + "s)";
//        } else {
//            timeShow = " (" + useTime + "ms)";
//        }
        List<String> _remoteIpList = new ArrayList<>();
        String ipString = "";
        if (_remoteInet != null) {// 解析正确
            long len = _remoteInet.length;
            for (int i = 0; i < len; i++) {
                if (_remoteInet[i] != null) {
                    _remoteIpList.add(_remoteInet[i].getHostAddress());
                    ipString += _remoteInet[i].getHostAddress() + ",";
                }
            }
            ipString = ipString.substring(0, ipString.length() - 1);
            return ipString;
        }
        return "";
    }

    public static String getDomainFromUrl(String url) {
        if (isEmptyString(url)) {
            return url;
        }
        String protocol = "";
        if (url.indexOf("http://") >= 0) {
            protocol = url.substring(0, url.indexOf("http://") + 7);
        } else if (url.indexOf("https://") >= 0) {
            protocol = url.substring(0, url.indexOf("https://") + 8);
        }
        String domain = "";
        if (!isEmptyString(protocol)) {
            String leftStr = url.substring(url.indexOf(protocol) + protocol.length());
            domain = leftStr.substring(0, leftStr.indexOf("/") > 0 ? leftStr.indexOf("/") : leftStr.length());
        }
        return protocol + domain;
    }

    public static String encrypt(String domain) {
        if (isEmptyString(domain)) {
            return domain;
        }
        //把字符串转为字节数组
        byte[] b = domain.getBytes();
        for (int i = 0; i < b.length; i++) {
            b[i] += 1;//在原有的基础上+1
        }
        return new String(b);
    }

    public static boolean isEmptyString(String s) {
        if (s == null || s.equals("")) {
            return true;
        }
        return false;
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH");
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
        String dir = SDCARD_ROOT;
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

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static String createFilepath(DIR_CATEGORY dir, String filename) {

        if (isEmptyString(filename)) {
            return null;
        }
        boolean ok = dirExist();
        if (!ok)
            return null;
        String filePath = SDCARD_ROOT + "/" + dirName(dir);
        File f = new File(filePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        filePath += filename;
        return filePath;
    }

    public static String stringToMD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static void writeExceptionToFile(String tag, Exception e) {
        StackTraceElement[] messages = e.getStackTrace();
        writeLogToFile(tag, "Exception: " + e.getMessage());
        for (int i = 0; i < messages.length; i++) {
            writeLogToFile(tag, messages[i].toString());
        }
    }

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

    public static String formatDate(long dateLong, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = new Date(dateLong);
        return sdf.format(date);
    }

    public final static String getIp() {
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(CrazyConstant.GET_IP_URL);
            URLConnection URLconnection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("成功");
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
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


}
