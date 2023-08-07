 package com.example.anuo.immodule.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.anuo.immodule.constant.Constant;

/**
 * @author: soxin
 * @version: 1
 * @project: trunk
 * @package: com.example.anuo.immodule.utils
 * @description:
 * @date: 2019-10-29
 * @time: 19:43
 */
public class YiboPreferenceUtils {

    static YiboPreferenceUtils pref;
    Context context;
    SharedPreferences mySharedPreferences;
    private final String CP_VERSION = "cp_version";
    private final String VERSION_TYPE = "version_type";

    public static YiboPreferenceUtils instance(Context context) {
        if (pref == null) {
            pref = new YiboPreferenceUtils(context.getApplicationContext());
        }
        return pref;
    }

    public YiboPreferenceUtils(Context context) {
        this.context = context;
        mySharedPreferences = context.getSharedPreferences("yibo_pref", Activity.MODE_PRIVATE);
    }

    public String getGameVersion() {
        return mySharedPreferences.getString(CP_VERSION,
                String.valueOf(Constant.lottery_identify_V1));
    }



    public boolean isPeilv(){
        return mySharedPreferences.getBoolean(VERSION_TYPE,true);
    }



}
