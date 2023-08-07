package com.yibo.yiboapp.webview.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.yibo.yiboapp.application.YiboApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author dx
 * @date 2018/12/14
 * SharedPreference数据持久化工具类
 */

public class SPUtils {
    private static final Map<String, SPUtils> SP_UTILS_MAP = new ConcurrentHashMap<>(1);
    private static final String DEFAULT_NAME = "sp_data";
    private SharedPreferences mSP;

    private static Context getContext() {
        return YiboApplication.getInstance();
    }

    private SPUtils(@NonNull String name) {
        mSP = getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * 获取默认的SPUtils实例,数据将会存储在data/data/<applicationId>/sp_data.xml
     *
     * @return 默认的SPUtils实例
     */
    public static SPUtils getInstance() {
        return obtainSPUtils(DEFAULT_NAME);
    }

    /**
     * 获取指定的的SPUtils实例,数据将会存储在data/data/<applicationId>/<name>.xml
     *
     * @return 指定的SPUtils实例, name的值不能为null或者"",否则将会抛出IllegalArgumentException
     */
    public static SPUtils getInstance(@NonNull String name) {
        return obtainSPUtils(name);
    }

    @NonNull
    private static SPUtils obtainSPUtils(String name) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name is empty");
        }
        SPUtils utils = SP_UTILS_MAP.get(name);
        if (utils == null) {
            utils = new SPUtils(name);
            SP_UTILS_MAP.put(name, utils);
        }
        return utils;
    }

    /**
     * 如果SPUtils提供的公有方法不能满足需求,这里提供SharedPreferences,来满足其它的需求
     *
     * @return SPUtils对应的SharedPreferences对象
     */
    public SharedPreferences getSP() {
        return mSP;
    }

    /**
     * 使用SharedPreferences的apply方法存储键值对(不需要关心是否存储成功的时候使用)
     *
     * @param data 需要存储的所有键值对,不能为null,否则将会抛出IllegalArgumentException
     */
    public void putApply(Map<String, String> data) {
        putData(data, false);
    }

    /**
     * 使用SharedPreferences的commit方法存储键值对(需要关心是否存储成功时使用)
     *
     * @param data 需要存储的所有键值对,不能为null,否则将会抛出IllegalArgumentException
     * @return 是否存储成功
     */
    public boolean putCommit(Map<String, String> data) {
        return putData(data, true);
    }

    /**
     * 使用SharedPreferences的apply方法存储键值对(不需要关心是否存储成功的时候使用)
     *
     * @param key   SharedPreferences中的key值,不能为null,否则将会抛出IllegalArgumentException
     * @param value SharedPreferences中的value值
     */
    public void putApply(String key, String value) {
        putData(key, value, false);
    }

    /**
     * 使用SharedPreferences的commit方法存储键值对(需要关心是否存储成功时使用)
     *
     * @param key   SharedPreferences中的key值,不能为null,否则将会抛出IllegalArgumentException
     * @param value SharedPreferences中的value值
     * @return 是否存储成功
     */
    public boolean putCommit(String key, String value) {
        return putData(key, value, true);
    }

    private boolean putData(Map<String, String> data, boolean commit) {
        if (data == null) {
            throw new IllegalArgumentException("data is null");
        }
        if (data.size() <= 0) {
            return true;
        }
        SharedPreferences.Editor editor = mSP.edit();
        for (String key : data.keySet()) {
            String value = data.get(key);
            if (TextUtils.isEmpty(key)) {
                throw new IllegalArgumentException("map contains empty key.");
            }
            editor.putString(key, value);
        }
        if (commit) {
            return editor.commit();
        } else {
            editor.apply();
            return false;
        }
    }

    private boolean putData(String key, String value, boolean commit) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key is empty.");
        }

        SharedPreferences.Editor editor = mSP.edit();
        editor.putString(key, value);

        if (commit) {
            return editor.commit();
        } else {
            editor.apply();
            return false;
        }
    }

    /**
     * 获取SharedPreferences中指定的String值
     *
     * @param key          SharedPreferences中的key值,不能为null或者空,否则将会抛出IllegalArgumentException
     * @param defaultValue 如果没在SharedPreferences中找到,就返回defaultValue
     * @return 如果没找到, 将会返回defaultValue
     */
    public String get(String key, String defaultValue) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key is empty.");
        }
        return mSP.getString(key, defaultValue);
    }

    /**
     * 获取SharedPreferences中指定的String值
     *
     * @param key SharedPreferences中的key值,不能为null或者空,否则将会抛出IllegalArgumentException
     * @return 如果没找到, 将会返回null
     */
    public String get(String key) {
        return get(key, null);
    }

    /**
     * 获取SharedPreferences中所有的键值对
     *
     * @return 不会返回null, 结果会被强转为Map<String , String>
     */
    @NonNull
    public Map<String, String> getAll() {
        Map<String, String> result = (Map<String, String>) mSP.getAll();
        if (result == null) {
            result = new HashMap<>(0);
        }
        return result;
    }
}
