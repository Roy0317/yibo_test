package com.yibo.yiboapp.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.yibo.yiboapp.utils.Utils;

/**
 * save some light-level data in app
 * cache
 *
 * @author johnson
 */
public class YiboPreference {
    static YiboPreference pref;
    Context context;
    SharedPreferences mySharedPreferences;
    private final String TOKEN = "token";
    private final String LOGIN_STATE = "login_state";
    private final String AUTO_LOGIN = "auto_login";
    private final String REMEMBER_PWD = "remember_pwd";
    private final String CP_VERSION = "cp_version";
    private final String VERSION_TYPE = "version_type";
    private final String NATIVE_STYLE = "native_style";
    private final String ACCOUNT_ID = "account_id";
    private final String ACCOUNT_MODE = "account_mode";
    private final String sport_bet_show_been = "has_show";
    private final String BUTTON_SOUND_ALLOW = "buttonSoundAllow";
    private final String KAIJIANG_SOUND_ALLOW = "kaiJiangSoundAllow";
    private final String AUTO_CHECK_UPDATE = "autoCheckUpdate";
    private final String CHECK_UPDATE_VERSION = "checkUpdateVersion";
    private final String CHECK_UPDATE_FIRST = "checkUpdatefirst";
    private final String TOUZHU_WARM_REMIND = "warm_remind";
    private final String START_UP_COUNT_DOWN = "start_up_count_down";
    private final String START_NETWORK_SERVICE = "start_network_service";
    private final String VIBRATE_ALLOW = "vibrate_allow";
    private final String SHOW_NOTICES_POP = "show_notices";
    private final String LOGIN_USERNAME = "username";
    private final String TOUZHU_ASK = "touzhu_ask";
    private final String SYSTEM_TIME = "system_time";
    private final String PASSWORD = "password";
    private final String SYS_CONFIG = "sys_config";
    private final String LAUNCHER_IMG = "launcher_img";
    private final String YJF_MODE = "yjf_mode";
    private final String NOT_TOAST_WHEN_QIHAO_TOUZHU_ENDLINE = "ntwqte";
    private final String SHOW_GESUTRE_SHAKE = "show_shake_gesture";
    private final String SHOW_SLIDE_SHAKE = "show_slide_gesture";
    private final String LOTTERYS = "lotterys";
    private final String SHOW_PICTURES = "show_pictures";
    private static final String CURRENT_DUMP_TIME = "current_dump_time";
    private static final String CONFIRM_TIME = "confirm_time";
    private static final String USER_YU_E = "user_yu_e";
    private static final String USER_HEADER = "user_header";

    private static final String MAIN_POPUP_TIME = "main_popup_time_new"; //首页弹窗时间设定

    private static final String SPACE_MAIN_POPUP_TIME = "space_main_popup_time"; //首页弹窗间隔时间
    private static final String LAST_TIME_SHOW_MAIN_POPUP = "last_time_show_main_popup"; //上次首页弹窗时间

    private final String USER_IS_OPEN_GESTURE = "isUserChecked";
    private final String USER_IS_SET_GESTURE = "isSetGesture";
    private final String SP_TIME_LOCK = "sp_timelock";

    private final String IS_FIRST_INSTALL = "is_first_install";//是否是首次安装
    private final String SIX_MARK = "six_mark";
    private final String LOGIN_UUID = "login_uuid";
    private final String ROUTE_URLS = "route_urls";//线路列表
    private final String CHOOSE_ROUTE = "choose_route";//当前选择的线路
    private final String CHOOSE_HOST_URL = "choose_host_url";//当前选择的hostUrl
    private final String CHOOSE_ROUTE_TYPE = "choose_route_type";//当前选择的routeType
    private final String CHOOSE_ROUTE_NAME = "choose_route_name";//当前选择的routeName
    private final String CHOOSE_ROUTE_ID = "choose_route_id";//当前选择的线路id
    private final String AUTO_ROUTE = "auto_route";//自动线路
    private final String PACKAGE_DOMAIN = "package_domain";//打包域名
    private final String PACKAGE_HOST_URL = "package_host_url";//打包hostUrl
    private final String FIX_URLS = "fix_url";
    private final String UPDATE_PASSWORD_TIME = "update_password_timestamp";
    private final String DEVICE_ID = "device_id";//装置唯一码
    private final String VERIFY_TOKEN = "verify_token";//验证码token

    public String getUserHeader() {
        return mySharedPreferences.getString(USER_HEADER, "");
    }

    public void setUserHeader(String header) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(USER_HEADER, header);
        editor.commit();
    }

    public static YiboPreference instance(Context context) {
        if (pref == null) {
            pref = new YiboPreference(context.getApplicationContext());
        }
        return pref;
    }

    public YiboPreference(Context context) {
        this.context = context;
        mySharedPreferences = context.getSharedPreferences("yibo_pref", Activity.MODE_PRIVATE);
    }

    public SharedPreferences getSP(){
        return mySharedPreferences;
    }

    public void setToken(String token) {
        if (Utils.isEmptyString(token)) {
            return;
        }
        Editor editor = mySharedPreferences.edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return mySharedPreferences.getString(TOKEN, "");
    }

    public void setAccountId(int accountId) {
        if (accountId==0) {
            return;
        }
        Editor editor = mySharedPreferences.edit();
        editor.putInt(ACCOUNT_ID, accountId);
        editor.commit();
    }

    public int getAccountId() {
        return mySharedPreferences.getInt(ACCOUNT_ID, 0);
    }

    public void setAccountMode(int mode) {
        Editor editor = mySharedPreferences.edit();
        editor.putInt(ACCOUNT_MODE, mode);
        editor.commit();
    }

    public int getAccountMode() {
        return mySharedPreferences.getInt(ACCOUNT_MODE, Constant.ACCOUNT_PLATFORM_MEMBER);
    }

    public void setSportBetShow(boolean hasShow) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(sport_bet_show_been, hasShow);
        editor.commit();
    }

    public boolean getSportBetShow() {
        return mySharedPreferences.getBoolean(sport_bet_show_been, false);
    }

    public void setConfirmtime(long confirmtime) {
        Editor editor = mySharedPreferences.edit();
        editor.putLong(CONFIRM_TIME, confirmtime);
        editor.commit();
    }

    public long getConfirmtime() {
        return mySharedPreferences.getLong(CONFIRM_TIME, 0);
    }

    public void setLoginState(boolean isLogin) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(LOGIN_STATE, isLogin);
        editor.commit();
    }

    public boolean isLogin() {
        return mySharedPreferences.getBoolean(LOGIN_STATE, false);
    }

    public void setButtonSoundAllow(boolean allow) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(BUTTON_SOUND_ALLOW, allow);
        editor.commit();
    }

    public void setKaiJiangSoundAllow(boolean allow) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(KAIJIANG_SOUND_ALLOW, allow);
        editor.commit();
    }

    public boolean isKaiJiangSoundAllow() {
        return mySharedPreferences.getBoolean(KAIJIANG_SOUND_ALLOW, false);
    }

    public boolean isButtonSoundAllow() {
        return mySharedPreferences.getBoolean(BUTTON_SOUND_ALLOW, false);
    }


    //自动检测更新
    public boolean isAutoCheckUpdate() {
        return mySharedPreferences.getBoolean(AUTO_CHECK_UPDATE, true);
    }

    //设置是否检测自动更新
    public void setAutoCheckUpdate(boolean update) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(AUTO_CHECK_UPDATE, update);
        editor.commit();
    }

    //设置更新通道版本
    public void setCheckUpdateVersion(int version) {
        Editor editor = mySharedPreferences.edit();
        editor.putInt(CHECK_UPDATE_VERSION, version);
        editor.commit();
    }

    //获取更新通道版本
    public int getCheckUpdateVersion() {
        return mySharedPreferences.getInt(CHECK_UPDATE_VERSION, 0);
    }

    //设置更新通道版本
    public void setCheckUpdateFiest(boolean version) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(CHECK_UPDATE_FIRST, version);
        editor.commit();
    }

    //获取更新通道版本
    public boolean isCheckUpdateFiest() {
        return mySharedPreferences.getBoolean(CHECK_UPDATE_FIRST, true);
    }

    //投注页面温馨提示
    public boolean isWarmRemind() {
        return mySharedPreferences.getBoolean(TOUZHU_WARM_REMIND, true);
    }

    //投注页面温馨提示
    public void setWarmRemind(boolean isRemind) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(TOUZHU_WARM_REMIND, isRemind);
        editor.commit();
    }

    //启动页倒计时
    public boolean isShowCountDown() {
        return mySharedPreferences.getBoolean(START_UP_COUNT_DOWN, false);
    }

    public void setShowCountDown(boolean isShow) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(START_UP_COUNT_DOWN, isShow);
        editor.commit();
    }

    //网路监测服务
    public boolean isStartNetworkService() {
        return mySharedPreferences.getBoolean(START_NETWORK_SERVICE, true);
    }

    public void setStartNetworkService(boolean isShow) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(START_NETWORK_SERVICE, isShow);
        editor.apply();
    }


    public void setVibrateAllow(boolean allow) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(VIBRATE_ALLOW, allow);
        editor.commit();
    }

    public boolean isVirateAllow() {
        return mySharedPreferences.getBoolean(VIBRATE_ALLOW, false);
    }

    public void setPopNotices(boolean allow) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(SHOW_NOTICES_POP, allow);
        editor.commit();
    }

    public boolean isPopNotices() {
        return mySharedPreferences.getBoolean(SHOW_NOTICES_POP, false);
    }


    public void setTouzhuAsk(boolean hasAsk) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(TOUZHU_ASK, hasAsk);
        editor.commit();
    }

    public boolean hasTouzhuAsk() {
        return mySharedPreferences.getBoolean(TOUZHU_ASK, false);
    }

    public void saveUsername(String username) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(LOGIN_USERNAME, username);
        editor.commit();
    }

    public String getUsername() {
        return mySharedPreferences.getString(LOGIN_USERNAME, "");
    }

    public void setLastCrashTime(long time) {
        if (mySharedPreferences != null) {
            mySharedPreferences.edit().putLong(CURRENT_DUMP_TIME, time).commit();
        }
    }

    public long getLastCrashTime() {
        if (mySharedPreferences != null) {
            return mySharedPreferences.getLong(CURRENT_DUMP_TIME, System.currentTimeMillis());
        }
        return 0;
    }

    public void savePwd(String username) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(PASSWORD, username);
        editor.commit();
    }

    public String getPwd() {
        return mySharedPreferences.getString(PASSWORD, "");
    }

    public void saveSysTime(long sysTime) {
        Editor editor = mySharedPreferences.edit();
        editor.putLong(SYSTEM_TIME, sysTime);
        editor.commit();
    }

    public long getSysTime() {
        return mySharedPreferences.getLong(SYSTEM_TIME, 0);
    }

    public void setAutoLogin(boolean auto) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(AUTO_LOGIN, auto);
        editor.commit();
    }

    public boolean isAutoLogin() {
        return mySharedPreferences.getBoolean(AUTO_LOGIN, true) == true;
    }

    public void setRememberPwd(boolean remember) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(REMEMBER_PWD, remember);
        editor.commit();
    }

    public boolean isRememberPwd() {
        return mySharedPreferences.getBoolean(REMEMBER_PWD, true) == true;
    }

    public void setHasShowPicture(boolean hasShow) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(SHOW_PICTURES, hasShow);
        editor.commit();
    }

    public boolean getHasShowPicture() {
        return mySharedPreferences.getBoolean(SHOW_PICTURES, false);
    }

    public void saveGameVersion(String version) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(CP_VERSION, version);
        editor.commit();
    }

    public String getGameVersion() {
        return mySharedPreferences.getString(CP_VERSION,
                String.valueOf(Constant.lottery_identify_V1));
    }

    public void saveNativeStyle(String code) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(NATIVE_STYLE, code);
        editor.commit();
    }

    public String getNativeStyle() {
        return mySharedPreferences.getString(CP_VERSION,
                String.valueOf(Constant.OLD_CLASSIC_STYLE));
    }

    public void setLauncherImg(String config) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(LAUNCHER_IMG, config);
        editor.commit();
    }

    public String getLauncherImg() {
        return mySharedPreferences.getString(LAUNCHER_IMG, "");
    }

    public void saveConfig(String config) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(SYS_CONFIG, config);
        editor.commit();
    }

    public String getSysConfig() {
        return mySharedPreferences.getString(SYS_CONFIG, "");
    }

    public void saveYjfMode(String mode) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(YJF_MODE, mode);
        editor.commit();
    }

    public String getYjfMode() {
        return mySharedPreferences.getString(YJF_MODE, String.valueOf(Constant.YUAN_MODE));
    }

    public void saveNotToastWhenTouzhuEnd(boolean remember) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(NOT_TOAST_WHEN_QIHAO_TOUZHU_ENDLINE, remember);
        editor.commit();
    }

    public boolean getNotToastWhenTouzhuEnd() {
        return mySharedPreferences.getBoolean(NOT_TOAST_WHEN_QIHAO_TOUZHU_ENDLINE, false);
    }


    public void setShowShakeGesture(boolean remember) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(SHOW_GESUTRE_SHAKE, remember);
        editor.commit();
    }

    public boolean hasShowShakeGesture() {
        return mySharedPreferences.getBoolean(SHOW_GESUTRE_SHAKE, false);
    }

    public void setShowSildeGesture(boolean remember) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean(SHOW_SLIDE_SHAKE, remember);
        editor.commit();
    }

    public boolean getShowSildeGesture() {
        return mySharedPreferences.getBoolean(SHOW_SLIDE_SHAKE, false);
    }

    public void saveLotterys(String lotterys) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(LOTTERYS, lotterys);
        editor.commit();
    }

    public String getLotterys() {
        return mySharedPreferences.getString(LOTTERYS, "");
    }


    /**
     * @param isMessagePushCheck 是否消息推送
     */
    public void saveMessagePush(boolean isMessagePushCheck) {
        Editor editor = mySharedPreferences.edit();
        editor.putBoolean("isMessagePushCheck", isMessagePushCheck);
        editor.apply();
    }

    public boolean getMessagePush() {
        return mySharedPreferences.getBoolean("isMessagePushCheck", true);
    }

    /**
     * 保存站内信的id
     *
     * @param id
     */
    public void saveInboxMessageId(String id) {
        Editor editor = mySharedPreferences.edit();
        editor.putString("inBoxMessageId", id);
        editor.apply();
    }

    /**
     * 获取站内信的id
     */
    public String getInboxMessageId() {
        return mySharedPreferences.getString("inBoxMessageId", "");
    }

    /**
     * 保存优惠活动推送的id
     */
    public void saveDiscountMessageId(String id) {
        mySharedPreferences.edit().putString("discountMessageId", id).apply();
    }

    /**
     * 获取优惠活动推送id
     *
     * @param
     * @return
     */
    public String getDiscoutMessageId() {
        return mySharedPreferences.getString("discountMessageId", "");
    }

    /**
     * 保存活动公告的id
     */
    public void saveWebsiteNotice(String id) {
        mySharedPreferences.edit().putString("websiteNoticeId", id).apply();
    }

    /**
     * 获取活动公告的推送id
     */
    public String getWebsiteNoticeMessageId() {
        return mySharedPreferences.getString("websiteNoticeId", "");
    }


    public void setMainPopupTime(String data) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(MAIN_POPUP_TIME, data);
        editor.apply();
    }

    public String getMainPopupTime() {
        return mySharedPreferences.getString(MAIN_POPUP_TIME, "");
    }


    /**
     * 用户是否开关手势密码(预设关闭)
     **/
    public boolean userIsCheckedGesture() {
        return mySharedPreferences.getBoolean("isUserChecked", false);
    }


    public void setUserCheckedGesture(boolean flag) {
        mySharedPreferences.edit().putBoolean(USER_IS_OPEN_GESTURE, flag).apply();
    }

    /**
     * 用户是否设置手势密码
     */

    public boolean userIsSetGesture() {
        return mySharedPreferences.getBoolean(USER_IS_SET_GESTURE, false);
    }


    /**
     * 是否设置手势密码
     *
     * @param flag
     */
    public void setUserGesture(boolean flag) {
        mySharedPreferences.edit().putBoolean(USER_IS_SET_GESTURE, flag).apply();
    }

    /**
     * 获取锁屏时间
     *
     * @return
     */
    public String getSptimeLock() {
        return mySharedPreferences.getString(SP_TIME_LOCK, "10000");
    }

    /**
     * 设置锁屏时间
     *
     * @param time
     */
    public void setSpTimeLock(String time) {
        mySharedPreferences.edit().putString(SP_TIME_LOCK, time).apply();
    }

    /**
     * 是否是第一次安装
     */
    public boolean isFirstInstall() {
        return mySharedPreferences.getBoolean(IS_FIRST_INSTALL, true);
    }

    public void saveFirstInstall(boolean flag) {
        mySharedPreferences.edit().putBoolean(IS_FIRST_INSTALL, flag).apply();
    }


    public boolean isPeilv() {
        return mySharedPreferences.getBoolean(VERSION_TYPE, true);
    }

    public void setIsPeilv(boolean flag) {
        mySharedPreferences.edit().putBoolean(VERSION_TYPE, flag).apply();
    }

    public void saveSixMark(String content) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(SIX_MARK, content);
        editor.commit();
    }

    public String getSIX_MARK() {
        return mySharedPreferences.getString(SIX_MARK, "");
    }


    public void setLogin_uuid(String content) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(LOGIN_UUID, content);
        editor.commit();
    }

    public String getLoginUuid() {
        return mySharedPreferences.getString(LOGIN_UUID, "");
    }

    public void setROUTE_URLS(String content) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(ROUTE_URLS, content);
        editor.commit();
    }

    public String getROUTE_URLS() {
        return mySharedPreferences.getString(ROUTE_URLS, "");
    }

    public void setCHOOSE_ROUTE(String content) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(CHOOSE_ROUTE, content);
        editor.commit();
    }

    public String getCHOOSE_ROUTE() {
        return mySharedPreferences.getString(CHOOSE_ROUTE, "");
    }

    public void setCHOOSE_HOST_URL(String content) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(CHOOSE_HOST_URL, content);
        editor.commit();
    }

    public String getCHOOSE_HOST_URL() {
        return mySharedPreferences.getString(CHOOSE_HOST_URL, "");
    }

    public void setCHOOSE_ROUTE_TYPE(int content) {
        Editor editor = mySharedPreferences.edit();
        editor.putInt(CHOOSE_ROUTE_TYPE, content);
        editor.commit();
    }

    public int getCHOOSE_ROUTE_TYPE() {
        return mySharedPreferences.getInt(CHOOSE_ROUTE_TYPE,1);
    }

    public boolean isAutoRoute() {
        return mySharedPreferences.getBoolean(AUTO_ROUTE, false);
    }

    public void setAUTO_ROUTE(boolean flag) {
        mySharedPreferences.edit().putBoolean(AUTO_ROUTE, flag).apply();
    }

    public void setPACKAGE_DOMAIN(String content) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(PACKAGE_DOMAIN, content);
        editor.commit();
    }

    public String getPACKAGE_DOMAIN() {
        return mySharedPreferences.getString(PACKAGE_DOMAIN, "");
    }

    public void setPACKAGE_HOST_URL(String content) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(PACKAGE_HOST_URL, content);
        editor.commit();
    }

    public String getPACKAGE_HOST_URL() {
        return mySharedPreferences.getString(PACKAGE_HOST_URL, "");
    }

    public void setCHOOSE_ROUTE_NAME(String content) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(CHOOSE_ROUTE_NAME, content);
        editor.commit();
    }

    public String getCHOOSE_ROUTE_NAME() {
        return mySharedPreferences.getString(CHOOSE_ROUTE_NAME, "");
    }

    public void saveUrlForDomains(String content) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(FIX_URLS, content);
        editor.commit();
    }

    public String getUrlForDomains() {
        return mySharedPreferences.getString(FIX_URLS, Urls.FIX_NATIVE_BASE_URL_1);
    }

    public void setCHOOSE_ROUTE_ID(int id) {
        Editor editor = mySharedPreferences.edit();
        editor.putInt(CHOOSE_ROUTE_ID, id);
        editor.commit();
    }

    public int getCHOOSE_ROUTE_ID(){
        return mySharedPreferences.getInt(CHOOSE_ROUTE_ID,0);
    }

    public void setUpdatePasswordTimestamp(long ts){
        mySharedPreferences.edit()
                .putLong(UPDATE_PASSWORD_TIME, ts)
                .apply();
    }

    public long getUpdatePasswordTimestamp(){
        return mySharedPreferences.getLong(UPDATE_PASSWORD_TIME, 0);
    }

    //装置唯一码
    public String getDeviceId() {
        return mySharedPreferences.getString(DEVICE_ID,"");
    }

    public void setDeviceId(String token) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(DEVICE_ID, token);
        editor.commit();
    }

    //验证码token
    public String getVerifyToken() {
        return mySharedPreferences.getString(VERIFY_TOKEN,"");
    }

    public void setVerifyToken(String token) {
        Editor editor = mySharedPreferences.edit();
        editor.putString(VERIFY_TOKEN, token);
        editor.commit();
    }
}
