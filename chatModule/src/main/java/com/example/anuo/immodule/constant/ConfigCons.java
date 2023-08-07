package com.example.anuo.immodule.constant;

import com.example.anuo.immodule.BuildConfig;

import crazy_wrapper.Crazy.BasicCrazyDispatcher;

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
 * Date  :2019/6/4
 * Desc  :com.example.anuo.immodule.constant
 */
public class ConfigCons {
    public static final String MSG_RECEIVER_ACTION = "com.anuo.immodule";

    public static final String ws = "ws://echo.websocket.org";//websocket测试地址
    //websocket服务器地址
//    public static final String ws ="ws://10.0.2.2:8880/ffpush/websocket/push.do";
    // socket连接地址

    public static String CHAT_SERVER_URL = "https://testapi.yibochat.com";//socket连接地址
//    public static  String CHAT_SERVER_URL = "http://192.168.9.37:6705";//socket连接地址--本地测试用


    public static final String CHAT_BASE_URL = CHAT_SERVER_URL + "/app";//APP聊天室域名
//    public static final String CHAT_BASE_URL = "http://192.168.9.37:8070/app";//APP聊天室域名--本地测试用

//    public static final String CHAT_BASE_URL = CHAT_SERVER_URL + "/app";//APP聊天室域名--本地测试用
    //    public static final String CHAT_BASE_URL = "http://a07.com:9999/play_core_war/app";//APP聊天室域名

    //    public static final String CHAT_FILE_BASE_URL = "https://testapi.yibochat.com/chatFile";//文件系统域名
    public static String CHAT_FILE_BASE_URL = "http://testfile.yibochat.com/";//文件系统域名
    public static String YUNJI_BASE_URL = BuildConfig.domain_url;//云迹主域名(可能是动态获取的)

    public static String YUNJI_BASE_HOST_URL = BuildConfig.host_domain;//云迹主域名对应的host域名
    public static String YUNJI_NATIVE_FLAG_URL = "1";//动态域名请求时携带的原生标识
    public static String YUNJI_NATIVE_DOMAIN_URL = BuildConfig.domain_url;//云迹主域名(可能是动态获取的)
    public static String YUNJI_ROUTE_TYPE_URL = "1";//系统线路来源方式 1--打包时固定 2--动态获取的IP或域名(IP和域名只有一个存在) 3--动态获取的IP和host域名(同时存在)

    public static final String PORT = "";//app端口
    public static final String PORT_CHAT = "";//聊天室端口
    public static final String SOURCE = "app";
    public static final String CHANNEL_ID = "chat";
    public static final String CHANNEL_NAME = "聊天消息";
    // Socket事件
    public static final String LOGIN = "LOGIN";// 登录（发送）
    public static final String USER_R = "USER_R";// 发消息（发送）
    public static final String USER_JOIN_ROOM = "USER_JOIN_ROOM";// 换房间（发送）
    public static final String USER_JOIN_GROUP = "USER_JOIN_GROUP";// 加入组（发送）
    public static final String RECONNECT = "RECONNECT";//重连（发送）
    public static final String login = "login";// 登录（接收）
    public static final String user_r = "user_r";// 发消息（接收）
    public static final String user_join_room = "user_join_room";// 换房间（接收）
    public static final String user_join_group = "user_join_group";// 加入组（接收）
    public static final String reconnect_r = "reconnect_r";//重连（接收）
    public static final String push_p = "push_p";//推送（接收）

    public static final String DEFAULT_IV = "0>2^4*6(~9!B#D$F";
    public static final String DEFAULT_KEY = "5Po)(%G&v3#M.{:;";
    public static String DATA_KEY = "";
    public static final String BACK_GROUND_IV = "a>b^4*6(~g!B#D$F";
    public static final String BACK_GROUND_KEY = "q!@Y&F^v(jA{oD;)";
    //验证码ID加密
    public static final String CAPTCHA_VERTIFY_KEY = "5P1)(@G&73#H.{:;";
    public static final String CAPTCHA_VERTIFY_IV = "0>2468B(~9!B#9!F";
    // 聊天室接口code
    public static final String LOGIN_AUTHORITY = "R70000";//登录并授权
    public static final String CHAT_ROOM_LIST = "R7023";//房间列表
    public static final String JOIN_CHAT_ROOM = "R7022";//进入房间
    public static final String SEND_MSG = "R7001";//发送文本消息
    public static final String SEND_IMAGE = "R7024";//发送图片消息
    public static final String SHARE_BET = "R7008";//分享投注
    public static final String FOLLOW_BET = "R7010";//跟单
    public static final String SEND_RED_PACKAGE = "R7009";//发红包
    public static final String SEND_RED_PACKAGE_WAP = "R0012";//WAP发红包
    public static final String SEND_RED_PACKAGE_PC = "A0012";//收到pc端发来红包
    public static final String GET_RED_PACKAGE = "R7027";//抢红包
    public static final String GET_RED_PACKAGE_V2 = "R0013";//抢红包2
    public static final String RED_PACKAGE_DETAIL = "R7032";//查看红包详情
    public static final String GET_FRONT_RED_PACKAGE = "R0015";//查看后台发送的红包详情
    public static final String LOTTERY_LIST = "R7028";//拉取彩种列表和拉取指定彩种列表
    public static final String CHAT_ROOM_NOTICE = "R7025";//房间公告
    public static final String HISTORY_MESSAGE = "R7029";//历史消息
    public static final String PHOTO_LIST = "R7035";//头像列表
    public static final String UPLOAD_PHOTO_TO_DB = "R7020";//头像列表
    public static final String MODIFY_PERSON_DATA = "R7034";//修改昵称和头像
    public static final String PERSON_DATA = "R7038";//获取个人数据
    public static final String GET_BET_HISTORY = "R7033";//获取历史投注信息
    public static final String GET_SYS_CONFIG = "R7031";//获取系统配置
    public static final String GET_VIOLATE_WORDS = "R7030";//获取禁言词
    public static final String UPDATE_PERSON_BET_DATA = "R7036";// 刷新并获取用户输赢
    public static final String GET_ONLINE_USER = "R0010";//获取在线用户
    public static final String GET_ONLINE_MANAGER = "R0041";//获取在线管理员
    public static final String SHARE_DATA = "R0001";//分享今日盈亏
    public static final String GET_CHARGE = "R0034";//获取用户余额
    public static final String GET_PLAN_NEWS = "R0028";//获取计划消息
    public static final String ONLINE_MANAGER_LIST = "R0041";//在线管理员列表
    public static final String GET_QUICK_MESSAGES = "R0011";//获取快捷发言
    public static final String GET_SAVE_PICTURES = "R0038";//收藏图片
    public static final String GET_WINNING_LIST = "R7039";//中奖榜单
    public static final String SIGN = "R7037";//签到
    public static final String MASTER_PLAN = "R0040";//导师计划
    public static final String GET_AUDIT_LIST = "R0047";//房间审核名单
    public static final String GET_PRIVATE_CONVERSATION = "R0500";//抓取私聊列表
    public static final String GET_PRIVATE_GROUP_HISTORY = "R0501";//私聊历史记录, 加入房间
    public static final String GET_PRIVATE_SEND_MSG = "R7051";//发消息
    public static final String GET_TOOL_PERMISSION = "R0008";//权限工具箱
    public static final String APPLY_FOR_BAN_SPEAK = "R70026";//申请解除禁言
    public static final String APPLY_FOR_BAN_SPEAK_2 = "R0026";//申请解除禁言
    public static final String GET_LONG_DRAGON = "R70043";//长龙



    //    public static final String LOTTERY_LAST_RESULT_URL = "/native/lastResult.do";//获取彩票最后一期的开奖结果
    public static final String LOTTERY_LAST_RESULT_URL = "/mobile/v3/lotteryDown.do";//获取彩票最后一期的开奖结果
    public static final String GET_ALL_LOTTERY_INFO = "/native/getLoctterys.do";
    public static final String GET_HISTORY_RESULT = "/native/open_result_detail.do";

    //云迹系统接口定义
    public static final String LOGIN_URL = "/native/login.do";

    //聊天室授权接口
    public static final String AUTHORITY_URL = "/native/nativeChatAddr.do ";

//    public static final String AUTHORITY_URL = "/native/native_chatroom_url.do";

    //聊天室系统接口定义
    public static final String LOGIN_CHAT_URL = "/nativeTestApi";
    public static final String UPLOAD_FILE = "/native_add_file";
    public static final String UPLOAD_AVATAR = "/add_file";
    public static final String READ_FILE = "/native_read_file";
    public static final String READ_FILE_WAP = "/read_file";
    public static final int YUAN_MODE = 100;//元

    /**
     * 彩票版本 1=第一版，2=第二版, 3=第三版,4=第四版,5=第五版，6=第六版
     */
    public static final int lottery_identify_V1 = 1;
    public static final int lottery_identify_V2 = 2;
    public static final int lottery_identify_V3 = 3;
    public static final int lottery_identify_V4 = 4;
    public static final int lottery_identify_V5 = 5;
    public static final int lottery_identify_V6 = 6;

    /**
     * 聊天室设置选项
     */
    //发消息的声音
    public static final String SEND_MSG_SOUND = "send_msg_sound";
    //收到消息通知时的声音
    public static final String NOTIFY_SOUND = "notify_sound";
    //消息通知
    public static final String MSG_NOTIFY = "msg_notify";
    //主界面模版风格id
    public static final int OLD_CLASSIC_STYLE = 1;

    /**
     * 平台试玩账号
     */
    public static final int ACCOUNT_PLATFORM_TEST_GUEST = 6;
    /**
     * 会员平台账号
     */
    public static final int ACCOUNT_PLATFORM_MEMBER = 1;

    public static final String LOTTERY_COUNTDOWN_URL = "/native/getCountDown.do";//获取彩票最后一期的倒计时数

    /**
     * 默认的快捷金额
     */
    public static final String DEFAULT_FASE_MONEY = "10,20,50,100,200,500,1000,2000,5000";

    public static final String LOGIN_CHAT_INFO = "login_chat_info";

    public static final int INTERVAL_REQUEST_OPENRESULT_DURATION = 5 * 60 * 1000;
}
