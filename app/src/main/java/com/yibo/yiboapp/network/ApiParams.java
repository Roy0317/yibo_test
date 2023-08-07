
package com.yibo.yiboapp.network;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 接口参数
 */
public class ApiParams extends HashMap<String, Object> {


    public ApiParams() {

    }


    /**
     * @return
     */
    private Iterator<Entry<String, Object>> getEntry() {
        return this.entrySet().iterator();
    }

    /**
     * 判断字符串中是否有中文
     *
     * @param str
     * @return
     */
    private boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * @return
     */
    public String getParams(boolean why) {
        StringBuffer sp = new StringBuffer(why ? "?" : "");

        Iterator<Entry<String, Object>> iter = getEntry();
        while (iter.hasNext()) {
            Entry<String, Object> entry = iter.next();
            String key = entry.getKey();
            Object val = entry.getValue();

            if (val != null && val instanceof String) {
                try {
                    val = URLEncoder.encode((String) val, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            sp.append(key).append("=").append(val).append("&");
        }
        // 删除最后一个&
        if (sp.length() > (why ? 2 : 1))
            sp.deleteCharAt(sp.length() - 1);

        return sp.toString();
    }


    /**
     * 获取文件后缀名
     */
    private String getSuffix(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        if (index != -1)
            return fileName.substring(index + 1);
        return "";
    }
}
