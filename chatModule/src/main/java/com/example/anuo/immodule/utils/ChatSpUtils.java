package com.example.anuo.immodule.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.anuo.immodule.constant.ConfigCons;
import com.google.gson.Gson;

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
 * Date  :22/06/2019
 * Desc  :com.example.anuo.immodule.utils
 */
public class ChatSpUtils {
    static ChatSpUtils spUtils;
    Context context;
    SharedPreferences mySharedPreferences;
    private final String TOKEN = "token";
    private final String LOGIN_STATE = "login_state";
    private final String AUTO_LOGIN = "auto_login";
    private final String REMEMBER_PWD = "remember_pwd";
    private final String CP_VERSION = "cp_version";
    private final String NATIVE_STYLE = "native_style";
    private final String ACCOUNT_MODE = "account_mode";
    private final String sport_bet_show_been = "has_show";
    private final String BUTTON_SOUND_ALLOW = "buttonSoundAllow";
    private final String KAIJIANG_SOUND_ALLOW = "kaiJiangSoundAllow";
    private final String AUTO_CHECK_UPDATE = "autoCheckUpdate";
    private final String TOUZHU_WARM_REMIND = "warm_remind";
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
    private static final String MAIN_APP_BASE_URL = "main_app_base_url"; //主工程的基础url
    private static final String LOTTERY_VERSION_1 = "lottery_version_1"; //彩票官方版本
    private static final String LOTTERY_VERSION_2 = "lottery_version_2"; //彩票信用版本
    private static final String CHAT_SYS_CONFIG = "chat_sys_config";//聊天室系统开关

    private static final String NOTICE_SEND_VOICE = "notice_send_voice"; //消息发送提示音
    private static final String NOTICE_RECIEVE_VOICE = "notice_recieve_voice"; //消息接收提示音
    private static final String NOTICE_ROOM = "notice_room"; //进房提示
    private static final String NOTICE_MESSAGE = "notice_message";//消息通知

    private static final String SEND_BETTING = "send_betting";
    private static final String SEND_EXPRESSION = "send_expression";
    private static final String ENTER_ROOM = "enter_room";
    private static final String SEND_TEXT = "send_text";
    private static final String RECEIVE_RED_PACKET = "receive_red_package";
    private static final String SEND_RED_PACKET = "send_red_package";
    private static final String SEND_IMAGE = "send_image";
    private static final String SEND_AUDIO = "send_audio";
    private static final String FAST_TALK = "fast_text";
    private static final String AGENT_ROOM_HOST = "agentRoomHost";
    private final String ENCRYPTED = "encrypted";
    private final String SIGN = "sign";
    private final String CLUSTER_ID = "clusterId";
    private final String USER_TYPE = "userType";
    private final String CURRENT_ROOM_ID = "current_room_id";

    private final String BET_IP = "betIp";

    //聊天室数据
    private final String USER_ID = "userId";
    private final String STATION_ID = "stationId";

    private final String ACCOUNT_TYPE = "account_type"; //业务系统的类型：1会员，2代理，3总代理，4普通试玩账号
    private final String AGENT_USER_CODE = "agent_user_code"; //用户为代理的时候（ACCOUNT_TYPE=2），进代理房需要传这个参数

    private final String AGENT_USER = "agent_user";
    private final String SWITCH_AGENT_PERMISSION = "switch_agent_permission";

    public void setUserId(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public String getUserId() {
        return mySharedPreferences.getString(USER_ID, "");
    }

    public void setStationId(String stationId) {
        if (TextUtils.isEmpty(stationId)) {
            return;
        }
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(STATION_ID, stationId);
        editor.commit();
    }

    public String getStationId() {
        return mySharedPreferences.getString(STATION_ID, "");
    }

    public String getUserHeader() {
        return mySharedPreferences.getString(USER_HEADER, "");
    }

    public void setUserHeader(String header) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(USER_HEADER, header);
        editor.commit();
    }

    public static ChatSpUtils instance(Context context) {
        if (spUtils == null) {
            spUtils = new ChatSpUtils(context.getApplicationContext());
        }
        return spUtils;
    }

    public ChatSpUtils(Context context) {
        this.context = context;
        mySharedPreferences = context.getSharedPreferences("yibo_pref", Activity.MODE_PRIVATE);
    }

    public void setToken(String token) {
        if (TextUtils.isEmpty(token)) {
            return;
        }
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return mySharedPreferences.getString(TOKEN, "");
    }

    public void setAccountMode(int mode) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(ACCOUNT_MODE, mode);
        editor.commit();
    }

    public int getAccountMode() {
        return mySharedPreferences.getInt(ACCOUNT_MODE, ConfigCons.ACCOUNT_PLATFORM_MEMBER);
    }

    public void setUserYuE(String money) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(USER_YU_E, money);
        editor.commit();
    }

    public String getUserYuE() {
        return mySharedPreferences.getString(USER_YU_E, "");
    }


    public void setSportBetShow(boolean hasShow) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(sport_bet_show_been, hasShow);
        editor.commit();
    }

    public boolean getSportBetShow() {
        return mySharedPreferences.getBoolean(sport_bet_show_been, false);
    }

    public void setConfirmtime(long confirmtime) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putLong(CONFIRM_TIME, confirmtime);
        editor.commit();
    }

    public long getConfirmtime() {
        return mySharedPreferences.getLong(CONFIRM_TIME, 0);
    }

    public void setLoginState(boolean isLogin) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(LOGIN_STATE, isLogin);
        editor.commit();
    }

    public boolean isLogin() {
        return mySharedPreferences.getBoolean(LOGIN_STATE, false);
    }

    public void setButtonSoundAllow(boolean allow) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(BUTTON_SOUND_ALLOW, allow);
        editor.commit();
    }

    public void setKaiJiangSoundAllow(boolean allow) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
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
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(AUTO_CHECK_UPDATE, update);
        editor.commit();
    }


    //投注页面温馨提示
    public boolean isWarmRemind() {
        return mySharedPreferences.getBoolean(TOUZHU_WARM_REMIND, true);
    }

    //投注页面温馨提示
    public void setWarmRemind(boolean isRemind) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(TOUZHU_WARM_REMIND, isRemind);
        editor.commit();
    }


    public void setVibrateAllow(boolean allow) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(VIBRATE_ALLOW, allow);
        editor.commit();
    }

    public boolean isVirateAllow() {
        return mySharedPreferences.getBoolean(VIBRATE_ALLOW, true);
    }

    public void setPopNotices(boolean allow) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(SHOW_NOTICES_POP, allow);
        editor.commit();
    }

    public boolean isPopNotices() {
        return mySharedPreferences.getBoolean(SHOW_NOTICES_POP, false);
    }


    public void setTouzhuAsk(boolean hasAsk) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(TOUZHU_ASK, hasAsk);
        editor.commit();
    }

    public boolean hasTouzhuAsk() {
        return mySharedPreferences.getBoolean(TOUZHU_ASK, false);
    }

    public void saveUsername(String username) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
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
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(PASSWORD, username);
        editor.commit();
    }

    public String getPwd() {
        return mySharedPreferences.getString(PASSWORD, "");
    }

    public void saveSysTime(long sysTime) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putLong(SYSTEM_TIME, sysTime);
        editor.commit();
    }

    public long getSysTime() {
        return mySharedPreferences.getLong(SYSTEM_TIME, 0);
    }

    public void setAutoLogin(boolean auto) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(AUTO_LOGIN, auto);
        editor.commit();
    }

    public boolean isAutoLogin() {
        return mySharedPreferences.getBoolean(AUTO_LOGIN, true) == true;
    }

    public void setRememberPwd(boolean remember) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(REMEMBER_PWD, remember);
        editor.commit();
    }

    public boolean isRememberPwd() {
        return mySharedPreferences.getBoolean(REMEMBER_PWD, true) == true;
    }

    public void setHasShowPicture(boolean hasShow) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(SHOW_PICTURES, hasShow);
        editor.commit();
    }

    public boolean getHasShowPicture() {
        return mySharedPreferences.getBoolean(SHOW_PICTURES, false);
    }

    public void saveGameVersion(String version) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(CP_VERSION, version);
        editor.commit();
    }

    public String getGameVersion() {
        return mySharedPreferences.getString(CP_VERSION,
                String.valueOf(ConfigCons.lottery_identify_V1));
    }

    public void saveNativeStyle(String code) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(NATIVE_STYLE, code);
        editor.commit();
    }

    public String getNativeStyle() {
        return mySharedPreferences.getString(CP_VERSION,
                String.valueOf(ConfigCons.OLD_CLASSIC_STYLE));
    }

    public void setLauncherImg(String config) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(LAUNCHER_IMG, config);
        editor.commit();
    }

    public String getLauncherImg() {
        return mySharedPreferences.getString(LAUNCHER_IMG, "");
    }

    public void saveConfig(String config) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(SYS_CONFIG, config);
        editor.commit();
    }

    public String getSysConfig() {
        return mySharedPreferences.getString(SYS_CONFIG, "");
    }

    public void saveYjfMode(String mode) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(YJF_MODE, mode);
        editor.commit();
    }

    public String getYjfMode() {
        return mySharedPreferences.getString(YJF_MODE, String.valueOf(ConfigCons.YUAN_MODE));
    }

    public void saveNotToastWhenTouzhuEnd(boolean remember) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(NOT_TOAST_WHEN_QIHAO_TOUZHU_ENDLINE, remember);
        editor.commit();
    }

    public boolean getNotToastWhenTouzhuEnd() {
        return mySharedPreferences.getBoolean(NOT_TOAST_WHEN_QIHAO_TOUZHU_ENDLINE, false);
    }


    public void setShowShakeGesture(boolean remember) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(SHOW_GESUTRE_SHAKE, remember);
        editor.commit();
    }

    public boolean hasShowShakeGesture() {
        return mySharedPreferences.getBoolean(SHOW_GESUTRE_SHAKE, false);
    }

    public void setShowSildeGesture(boolean remember) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(SHOW_SLIDE_SHAKE, remember);
        editor.commit();
    }

    public boolean getShowSildeGesture() {
        return mySharedPreferences.getBoolean(SHOW_SLIDE_SHAKE, false);
    }

    public void saveLotterys(String lotterys) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
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
        SharedPreferences.Editor editor = mySharedPreferences.edit();
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
        SharedPreferences.Editor editor = mySharedPreferences.edit();
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

    public String getLotterysTypeJson() {
        return mySharedPreferences.getString("lotterysTypeJson", "");
    }


    public void setMainAppBaseUrl(String url) {
        mySharedPreferences.edit().putString(MAIN_APP_BASE_URL, url).apply();
    }


    public String getMainAppBaseUrl() {
        return mySharedPreferences.getString(MAIN_APP_BASE_URL, "");
    }

    public void setLotteryVersion(int lotteryVersion1, int lotteryVersion2) {
        mySharedPreferences.edit().putInt(LOTTERY_VERSION_1, lotteryVersion1).apply();
        mySharedPreferences.edit().putInt(LOTTERY_VERSION_2, lotteryVersion2).apply();
    }


    public int getLotteryVersion_1() {
        return mySharedPreferences.getInt(LOTTERY_VERSION_1, 1);
    }

    public int getLotteryVersion_2() {
        return mySharedPreferences.getInt(LOTTERY_VERSION_2, 2);
    }

    public void setChatSysConfig(String config) {
        mySharedPreferences.edit().putString(CHAT_SYS_CONFIG, config).apply();
    }

    public ChatSysConfig getChatSysConfig() { //获取聊天室开关
        String con = mySharedPreferences.getString(CHAT_SYS_CONFIG, "");
        if (!TextUtils.isEmpty(con)) {
            ChatSysConfig chatSysConfig = new Gson().fromJson(con, ChatSysConfig.class);
            return chatSysConfig;
        } else {
            return new ChatSysConfig();
        }
    }

    public void setNoticeSendVoice(String str) {
        mySharedPreferences.edit().putString(NOTICE_SEND_VOICE, str).apply();
    }

    public String getNoticeSendVoice() {
        return mySharedPreferences.getString(NOTICE_SEND_VOICE, "");
    }

    public void setNoticeRecieveVoice(String str) {
        mySharedPreferences.edit().putString(NOTICE_RECIEVE_VOICE, str).apply();
    }

    public String getNoticeRecieveVoice() {
        return mySharedPreferences.getString(NOTICE_RECIEVE_VOICE, "");
    }

    public void setNoticeRoom(String str) {
        mySharedPreferences.edit().putString(NOTICE_ROOM, str).apply();
    }

    public String getNoticeRoom() {
        return mySharedPreferences.getString(NOTICE_ROOM, "");
    }

    public void setNoticeMessage(String str) {
        mySharedPreferences.edit().putString(NOTICE_MESSAGE, str).apply();
    }

    public String getNoticeMessage() {
        return mySharedPreferences.getString(NOTICE_MESSAGE, "");
    }

    public void setSendBetting(String str) {
        mySharedPreferences.edit().putString(SEND_BETTING, str).apply();
    }

    public String getSendBetting() {
        return mySharedPreferences.getString(SEND_BETTING, "");
    }

    public void setSendExpression(String str) {
        mySharedPreferences.edit().putString(SEND_EXPRESSION, str).apply();
    }

    public String getSendExpression() {
        return mySharedPreferences.getString(SEND_EXPRESSION, "");
    }

    public void setSendText(String str) {
        mySharedPreferences.edit().putString(SEND_TEXT, str).apply();
    }

    public String getSendText() {
        return mySharedPreferences.getString(SEND_TEXT, "");
    }

    public void setSendImage(String str) {
        mySharedPreferences.edit().putString(SEND_IMAGE, str).apply();
    }

    public String getSendImage() {
        return mySharedPreferences.getString(SEND_IMAGE, "");
    }

    public void setSendAudio(String str) {
        mySharedPreferences.edit().putString(SEND_AUDIO, str).apply();
    }

    public String getSendAudio() {
        return mySharedPreferences.getString(SEND_AUDIO, "");
    }

    public void setSendRedPacket(String str) {
        mySharedPreferences.edit().putString(SEND_RED_PACKET, str).apply();
    }

    public String getSendRedPacket() {
        return mySharedPreferences.getString(SEND_RED_PACKET, "");
    }

    public void setEnterRoom(String str) {
        mySharedPreferences.edit().putString(ENTER_ROOM, str).apply();
    }

    public String getEnterRoom() {
        return mySharedPreferences.getString(ENTER_ROOM, "");
    }

    public void setReceiveRedPacket(String str) {
        mySharedPreferences.edit().putString(RECEIVE_RED_PACKET, str).apply();
    }

    public String getReceiveRedPacket() {
        return mySharedPreferences.getString(RECEIVE_RED_PACKET, "");
    }

    public void setFastTalk(String str) {
        mySharedPreferences.edit().putString(FAST_TALK, str).apply();
    }

    public String getFastTalk() {
        return mySharedPreferences.getString(FAST_TALK, "");
    }

    public void setACCOUNT_TYPE(int type) {
        mySharedPreferences.edit().putInt(ACCOUNT_TYPE, type).apply();
    }

    public int getACCOUNT_TYPE() {
        return mySharedPreferences.getInt(ACCOUNT_TYPE, 1);
    }

    public void setAGENT_USER_CODE(String code) {
        mySharedPreferences.edit().putString(AGENT_USER_CODE, code).apply();
    }

    public String gettAGENT_USER_CODE() {
        return mySharedPreferences.getString(AGENT_USER_CODE, "");
    }

    public void setAgentRoomHost(String code) {
        mySharedPreferences.edit().putString(AGENT_ROOM_HOST, code).apply();
    }

    public String getAgentRoomHost() {
        return mySharedPreferences.getString(AGENT_ROOM_HOST, "");
    }

    public void setENCRYPTED(String code) {
        mySharedPreferences.edit().putString(ENCRYPTED, code).apply();
    }

    public String getENCRYPTED() {
        return mySharedPreferences.getString(ENCRYPTED, "");
    }

    public void setCLUSTER_ID(String code) {
        mySharedPreferences.edit().putString(CLUSTER_ID, code).apply();
    }

    public String getCLUSTER_ID() {
        return mySharedPreferences.getString(CLUSTER_ID, "");
    }

    public void setSIGN(String code) {
        mySharedPreferences.edit().putString(SIGN, code).apply();
    }

    public String getSIGN() {
        return mySharedPreferences.getString(SIGN, "");
    }

    public void setUSER_TYPE(String code) {
        mySharedPreferences.edit().putString(USER_TYPE, code).apply();
    }

    public String getUSER_TYPE() {
        return mySharedPreferences.getString(USER_TYPE, "");
    }

    public void setCURRENT_ROOM_ID(String code) {
        mySharedPreferences.edit().putString(CURRENT_ROOM_ID, code).apply();
    }

    public String getCURRENT_ROOM_ID() {
        return mySharedPreferences.getString(CURRENT_ROOM_ID, "");
    }

    public void setAGENT_USER(String code) {
        mySharedPreferences.edit().putString(AGENT_USER, code).apply();
    }

    public String getAGENT_USER() {
        return mySharedPreferences.getString(AGENT_USER, "");
    }

    public void setSWITCH_AGENT_PERMISSION(String code) {
        mySharedPreferences.edit().putString(SWITCH_AGENT_PERMISSION, code).apply();
    }

    public String getSWITCH_AGENT_PERMISSION() {
        return mySharedPreferences.getString(SWITCH_AGENT_PERMISSION, "");
    }

    //以用户id+","+roomId 为键
    //以房间key为值
    public void setRoomPasswordData(String userId, String data) {
        mySharedPreferences.edit().putString(userId, data).apply();
    }

    public String getRoomPasswordData(String userId) {
        return mySharedPreferences.getString(userId, "");
    }

    public static final String DOMAIN_URL = "domain_url";

    public void setDomainUrl(String data) {
        mySharedPreferences.edit().putString(DOMAIN_URL, data).apply();
    }

    public String getDomainUrl() {
        return mySharedPreferences.getString(DOMAIN_URL, "");
    }

    public void setBET_IP(String data) {
        mySharedPreferences.edit().putString(BET_IP, data).apply();
    }

    public String getBET_IP() {
        return mySharedPreferences.getString(BET_IP, "");
    }

}
