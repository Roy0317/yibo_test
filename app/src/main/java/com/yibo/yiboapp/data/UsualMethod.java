package com.yibo.yiboapp.data;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.example.anuo.immodule.bean.AuthorityBean;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.manager.SocketManager;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.utils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.GameListActivity;
import com.yibo.yiboapp.activity.LoginActivity;
import com.yibo.yiboapp.activity.LoginAndRegisterActivity;
import com.yibo.yiboapp.activity.VerifyActivity;
import com.yibo.yiboapp.application.YiboApplication;
import com.yibo.yiboapp.entify.ActiveBadgeWrapper;
import com.yibo.yiboapp.entify.BallListItemInfo;
import com.yibo.yiboapp.entify.BetToken;
import com.yibo.yiboapp.entify.CheckUpdateWraper;
import com.yibo.yiboapp.entify.LocCountDownWraper;
import com.yibo.yiboapp.entify.LocPlaysWraper;
import com.yibo.yiboapp.entify.LoginOutWraper;
import com.yibo.yiboapp.entify.LoginUUIDBean;
import com.yibo.yiboapp.entify.MainPopupTime;
import com.yibo.yiboapp.entify.MemberHeaderWraper;
import com.yibo.yiboapp.entify.Meminfo;
import com.yibo.yiboapp.entify.PaySysMethodWraper;
import com.yibo.yiboapp.entify.PeilvWebResult;
import com.yibo.yiboapp.entify.PlayEnify;
import com.yibo.yiboapp.entify.RealDomainWraper;
import com.yibo.yiboapp.entify.RegisterResultWrapper;
import com.yibo.yiboapp.entify.SavedGameData;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.entify.SysConfigWraper;
import com.yibo.yiboapp.entify.UnreadMsgCountWraper;
import com.yibo.yiboapp.interfaces.FileResultCallback;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.ui.BallonView;
import com.yibo.yiboapp.utils.DateUtils;
import com.yibo.yiboapp.utils.FileUploadCallback;
import com.yibo.yiboapp.utils.LoadImageUtil;
import com.yibo.yiboapp.utils.Lunbar;
import com.yibo.yiboapp.utils.OnResultListener;
import com.yibo.yiboapp.utils.PhoneInfo;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.webview.Live800ChattingActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.RequestQueue;
import crazy_wrapper.Crazy.dialog.CustomConfirmDialog;
import crazy_wrapper.Crazy.dialog.CustomDialogManager;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.anuo.immodule.utils.CommonUtils.getLocImageUrl;
import static java.lang.String.valueOf;

/**
 * A simple {@link } subclass.
 * 常用接口
 *
 * @author johnson
 */
public class UsualMethod {

    public static final int UPLOAD_TIMEOUT = 90000;
    public static final String TAG = "usualmehtod";

    /**
     * 将随机彩票号码添加到号码开奖组件中
     *
     * @param context
     * @param wrapper
     * @param numList
     * @param viewType 填充的是那种类型的球：位数，球，功能
     */
    public static void fillBallons(Context context, LinearLayout wrapper, List<String> numList,
                                   int viewType, boolean clickable) {
        if (numList == null || numList.isEmpty()) {
            return;
        }
        if (wrapper == null) {
            return;
        }
        wrapper.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //球类状态下，每个子球之间有一定的间隔
        if (viewType == Constant.BALLON_VIEW) {
            params.rightMargin = 8;
            params.leftMargin = 8;
        }
        for (int i = 0; i < numList.size(); i++) {
            int bgDrawable = 0;
            int txtColor = R.color.color_red;
            if (viewType == Constant.LETTER_VIEW || viewType == Constant.WEISHU_VIEW) {
                txtColor = R.color.lightgrey;
                if (i == 0) {
                    bgDrawable = R.drawable.light_gred_border_left_selector;
                } else if (i == numList.size() - 1) {
                    bgDrawable = R.drawable.light_gred_border_right_selector;
                } else {
                    bgDrawable = R.drawable.light_gred_rect_selector;
                    params.leftMargin = (int) context.getResources().getDimension(R.dimen.mistake_margin);
                }
            } else if (viewType == Constant.BALLON_VIEW) {
                txtColor = R.color.color_red;
                bgDrawable = R.drawable.red_border_normal;
            }
            String result = numList.get(i);
            BallonView ballonView = new BallonView(context);
            ballonView.setText(result);
            ballonView.setClickable(clickable);
            ballonView.setGravity(Gravity.CENTER);
            ballonView.setBackgroundResource(bgDrawable);
            ballonView.setTextColor(context.getResources().getColor(txtColor));
            wrapper.addView(ballonView, params);
        }
    }

    public static void fillBallons(Context context, LinearLayout wraper, List<String> strs) {
        fillBallons(context, wraper, strs, Constant.BALLON_VIEW, false);
    }

    public static List<PlayEnify> loadJsonFromDisk(Context context, String file) {
        Type listType = new TypeToken<List<PlayEnify>>() {
        }.getType();
        List<PlayEnify> result = new Gson().fromJson(Utils.readAssetJson(context, file), listType);
        return result;
    }

    public interface ChannelListener {
        void onCaiPiaoItemClick(String cpCode);

        void onKaiJianItemClick(String name, String cpCode, String cpType);

        void onGoucaiItemClick(String name, String cpCode);
    }

    public static List<BallListItemInfo> getCaipiaoListIconByCode(String cpName) {
        String iconStr = "彩";
        if (!Utils.isEmptyString(cpName) && cpName.length() > 1) {
            iconStr = cpName.substring(0, 1);
        }
        BallListItemInfo info = new BallListItemInfo();
        info.setNum(iconStr);
        List<BallListItemInfo> list = new ArrayList<BallListItemInfo>();
        list.add(info);
        return list;
    }

    /**
     * 获取投注记录页面记录分类数组
     *
     * @param context
     * @param status
     * @return
     */
    public static String[] getRecordsArrays(Context context, int status) {
        switch (status) {
            case Constant.CAIPIAO_RECORD_STATUS:
                return context.getResources().getStringArray(R.array.caipiao_touzhu_categories);
            case Constant.LHC_RECORD_STATUS:
                return context.getResources().getStringArray(R.array.caipiao_touzhu_categories);
            case Constant.SPORTS_RECORD_STATUS:
                return context.getResources().getStringArray(R.array.sports_touzhu_categories);
            case Constant.REAL_PERSON_RECORD_STATUS:
                return context.getResources().getStringArray(R.array.touzhu_categories);
            case Constant.ELECTRIC_GAME_RECORD_STATUS:
                return context.getResources().getStringArray(R.array.touzhu_categories);
        }
        return null;
    }

    public static void loginWhenSessionInvalid(Context context) {
        //退出登录时，清掉帐号相当的任何状态
        YiboPreference.instance(context).setLoginState(false);
        YiboPreference.instance(context).setToken("");
        YiboPreference.instance(context).setUserHeader("");
        SysConfig sc = UsualMethod.getConfigFromJson(context);
        if (sc.getNewmainpage_switch().equals("on")) {
            LoginAndRegisterActivity.createIntent(context, YiboPreference.instance(context).getUsername(), YiboPreference.instance(context).getPwd(), 0);
        } else {
            LoginActivity.createIntent(context, YiboPreference.instance(context).getUsername(), YiboPreference.instance(context).getPwd());
        }
        //退出登录时断开Socket
        ChatSpUtils.instance(context).setCURRENT_ROOM_ID("");
        SocketManager.instance(context).disconnectSocket();
        //退出登录清除当前账户的缓存数据
        RequestQueue.getInstance().getCache().evictAll();
    }


    public static boolean checkIsLogin(Context context) {
        if (!YiboPreference.instance(context).isLogin()) {
            ToastUtils.showShort("请先登录");
            SysConfig sc = UsualMethod.getConfigFromJson(context);
            if (sc != null && sc.getNewmainpage_switch().equals("on")) {
                LoginAndRegisterActivity.createIntent(context, YiboPreference.instance(context).getUsername(), "", 0);
            } else {
                LoginActivity.createIntent(context, YiboPreference.instance(context).getUsername(), "");
            }
            return false;
        }
        return true;
    }

    public static void authorization(Context ctx) {
        ApiParams params = new ApiParams();
//        params.put("newSystem", 1);
        HttpUtil.startRequest(ctx, ConfigCons.AUTHORITY_URL, params, "", false, false, 0,
                CrazyRequest.Priority.HIGH.ordinal(),
                CrazyRequest.ExecuteMethod.GET.ordinal(),
                CrazyRequest.Protocol.HTTP.ordinal(), false, result -> {
                    if (!result.isSuccess()) {
                        String errorString = CommonUtils.parseResponseResult(result.getMsg());
                        ToastUtils.showShort(TextUtils.isEmpty(errorString) ? ctx.getString(com.example.anuo.immodule.R.string.authority_fail) : errorString);
                        return;
                    }
                    AuthorityBean.ContentBean contentBean = new Gson().fromJson(result.getContent(), AuthorityBean.ContentBean.class);
                    if (contentBean == null)
                        return;
                    // 授权成功后修改聊天室域名、文件系统域名
                    if (!TextUtils.isEmpty(contentBean.getAppChatDomain())) {
                        ConfigCons.CHAT_SERVER_URL = contentBean.getAppChatDomain();
                    }
                    if (!TextUtils.isEmpty(contentBean.getFileChatDomain())) {
                        ConfigCons.CHAT_FILE_BASE_URL = contentBean.getFileChatDomain();
                    }
                    //保存聊天室登陆需要的参数
                    ChatSpUtils.instance(ctx).setENCRYPTED(contentBean.getEncrypted());
                    ChatSpUtils.instance(ctx).setCLUSTER_ID(contentBean.getClusterId());
                    ChatSpUtils.instance(ctx).setSIGN(contentBean.getSign());
                    SocketManager socketManager = SocketManager.instance(ctx);
                    socketManager.setConnectListener(args -> {
                        LogUtils.e("UsualMethod", "on connect success:" + Arrays.toString(args));
                    });

                    socketManager.setDisconnectListener(args -> {
                        LogUtils.e("UsualMethod", "on disConnect :" + Arrays.toString(args));
                        if (socketManager != null)
                            socketManager.reconnectSocket();
                    });

                    socketManager.setConnectErrorListener(args -> {
                        LogUtils.e("UsualMethod", "on connectError :" + Arrays.toString(args));
                        if (socketManager != null)
                            socketManager.reconnectSocket();
                    });

                    socketManager.setConnectTimeoutListener(args -> {
                        LogUtils.e("UsualMethod", "on connectTimeout :" + Arrays.toString(args));
                        if (socketManager != null)
                            socketManager.reconnectSocket();
                    });
                    socketManager.connectSocket();
                }, "", false, CommonUtils.getChatHeader(ctx));
    }


    //var moneyType = {1:"人工加款",2:"人工扣款",130:"彩票投注",3:"在线提款失败",131:"彩票派奖",
    // 4:"在线提款",132:"彩票撤单",5:"在线支付",133:"彩票派奖回滚",6:"快速入款",134:"参与彩票合买",
    // 7:"一般入款",135:"彩票合买满员",136:"彩票合买失效",9:"反水加钱",201:"体育投注",137:"彩票合买撤单",
    // 10:"反水回滚",202:"体育派奖",138:"彩票合买截止",11:"代理反点加钱",203:"体育撤单",139:"彩票合买派奖",
    // 12:"代理反点回滚",204:"体育派奖回滚",140:"六合彩投注",13:"多级代理反点加钱",141:"六合彩派奖",
    // 14:"多级代理反点回滚",142:"六合彩派奖回滚",15:"三方额度转入系统额度",79:"注册赠送",143:"六合彩撤单",
    // 16:"系统额度转入三方额度",80:"存款赠送",17:"三方转账失败",18:"活动中奖",19:"现金兑换积分",20:"积分兑换现金",23:"系统接口加款"};
    private static String getType(int code) {
        String name = "未知类型";
        switch (code) {
            case 1:
                name = "人工加款";
                break;
            case 2:
                name = "人工扣款";
                break;
            case 3:
                name = "在线提款失败";
                break;
            case 4:
                name = "在线提款";
                break;
            case 5:
                name = "在线支付";
                break;
            case 6:
                name = "快速入款";
                break;
            case 7:
                name = "一般入款";
                break;
            case 8:
                name = "体育投注";
                break;
            case 9:
                name = "反水加钱";
                break;
            case 10:
                name = "反水扣钱";
                break;
            case 11:
                name = "反点加钱";
                break;
            case 12:
                name = "反点扣钱";
                break;
            case 13:
                name = "反点加钱";
                break;
            case 14:
                name = "反点扣钱";
                break;
            case 15:
                name = "三方额度转入系统额度";
                break;
            case 16:
                name = "系统额度转入三方额度";
                break;
            case 17:
                name = "三方转账失败";
                break;
            case 18:
                name = "活动中奖";
                break;
            case 19:
                name = "现金兑换积分";
                break;
            case 20:
                name = "积分兑换现金";
                break;
            case 23:
                name = "系统接口加款";
                break;
            case 2444:
                name = "签到活动彩金";
                break;
            case 79:
                name = "注册赠送";
                break;
            case 80:
                name = "存款赠送";
                break;
            case 130:
            case 131:
                name = "彩票投注";
                break;
            case 132:
                name = "彩票撤单";
                break;
            case 133:
                name = "彩票派奖回滚";
                break;
            case 140:
                name = "六合彩投注";
                break;
            case 141:
                name = "六合彩派奖";
                break;
            case 142:
                name = "六合彩派奖回滚";
                break;
            case 201:
                name = "体育投注";
                break;
            case 202:
                name = "体育派奖";
                break;
            case 203:
                name = "体育撤单";
                break;
            case 204:
                name = "体育派奖回滚";
                break;
            case 310:
                name = "提款回滚";
                break;
            case 110:
                name = "每日加奖";
                break;
            case 111:
                name = "每周亏损奖励";
                break;
            case 81:
                name = "赠送彩金";
                break;
            case 24:
                name = "红包活动中奖";
                break;
            case 145:
                name = "余额生金发送奖励";
                break;
            case 1000:
                name = "等级权限升级赠送";
                break;
            case 777:
                name = "代金券充值";
                break;
            case 77:
                name = "充值卡入款";
                break;
            case 254:
                name = "粽子兑换现金";
                break;
        }
        return name;
    }

    public static String getAccountByCode(String type) {
        String code = "1";
        switch (type) {
            case "人工加款":
                code = "1";
                break;
            case "人工扣款":
                code = "2";
                break;
            case "在线提款失败":
                code = "3";
                break;
            case "在线提款":
                code = "4";
                break;
            case "在线支付":
                code = "5";
                break;
            case "快速入款":
                code = "6";
                break;
            case "一般入款":
                code = "7";
                break;
            case "体育投注":
                code = "8";
                break;
            case "反水加钱":
                code = "9";
                break;
            case "反水扣钱":
                code = "10";
                break;
            case "反点加钱":
                code = "11";
                break;
            case "反点扣钱":
                code = "14";
                break;
            case "三方额度转入系统额度":
                code = "15";
                break;
            case "系统额度转入三方额度":
                code = "16";
                break;
            case "三方转账失败":
                code = "17";
                break;
            case "活动中奖":
                code = "18";
                break;
            case "现金兑换积分":
                code = "19";
                break;
            case "积分兑换现金":
                code = "20";
                break;
            case "系统接口加款":
                code = "23";
                break;
            case "注册赠送":
                code = "79";
                break;
            case "存款赠送":
                code = "80";
                break;
            case "彩票投注":
                code = "130";
                break;
            case "彩票撤单":
                code = "132";
                break;
            case "彩票派奖回滚":
                code = "133";
                break;
            case "六合彩投注":
                code = "140";
                break;
            case "六合彩派奖":
                code = "141";
                break;
            case "六合彩派奖回滚":
                code = "142";
                break;
            case "体育派奖":
                code = "202";
                break;
            case "体育撤单":
                code = "203";
                break;
            case "体育派奖回滚":
                code = "204";
                break;
            case "每日加奖":
                code = "110";
                break;
            case "每周亏损奖励":
                code = "111";
                break;
            case "赠送彩金":
                code = "81";
                break;
            case "红包活动中奖":
                code = "24";
                break;
            case "余额生金发送奖励":
                code = "145";
                break;
            case "等级权限升级赠送":
                code = "1000";
                break;
            case "代金券充值":
                code = "777";
                break;
            case "充值卡入款":
                code = "77";
                break;
        }
        return code;
    }


    /**
     * 转换帐变类型为对应的文字
     *
     * @param type
     * @return
     */
    public static String convertAccountChangeTypeToString(int type) {
        return getType(type);
    }

    /**
     * 元角分模式转换
     *
     * @param mode
     * @return
     */
    public static String convertMode(int mode) {
        if (mode == Constant.YUAN_MODE) {
            return "元";
        } else if (mode == Constant.JIAO_MODE) {
            return "角";
        } else if (mode == Constant.FEN_MODE) {
            return "分";
        }
        return "元";
    }

    /**
     * 1:滚球   2:今日  3:早盘
     */
    public static String convertSportTimeCategory(long mode) {
        if (mode == 1) {
            return "滚球";
        } else if (mode == 2) {
            return "今日";
        } else if (mode == 3) {
            return "早盘";
        }
        return "";
    }

    public static String convertSportCommitStatus(long mode) {
        if (mode == 1) {
            return "待确认";
        } else if (mode == 2) {
            return "已确认";
        } else if (mode == 3) {
            return "已取消";
        } else if (mode == 4) {
            return "系统取消";
        }
        return "待确认";
    }

    public static String convertSportBalanceStatus(long mode) {
        if (mode == 1) {
            return "未结算";
        } else if (mode == 2) {
            return "已结算";
        }
        return "未结算";
    }

    public static int convertPostMode(int mode) {
        if (mode == Constant.YUAN_MODE) {
            return 1;
        } else if (mode == Constant.JIAO_MODE) {
            return 10;
        } else if (mode == Constant.FEN_MODE) {
            return 100;
        }
        return 1;
    }


    public static String convertMoneyRecordStatus(long status) {
        if (status == Constant.STATUS_UNTREATED) {
            return "处理中";
        } else if (status == Constant.STATUS_SUCCESS) {
            return "处理成功";
        } else if (status == Constant.STATUS_FAILED) {
            return "处理失败";
        } else if (status == Constant.STATUS_CANCELED) {
            return "已取消";
        } else if (status == Constant.STATUS_EXPIRED) {
            return "已过期";
        } else if (status == Constant.ROOL_BACK || status == Constant.ROOL_BACK_RECHARGE) {
            return "已回滚";
        }
        return "处理中";
    }

    public static int convertEgameValue(String gameType) {
        Utils.LOG("aa", "platform === " + gameType);
        if (gameType.equalsIgnoreCase("MG电子游艺")) {
            return Constant.MG_INT;
        } else if (gameType.equalsIgnoreCase("AG电子游艺")) {
            return Constant.AG_INT;
        } else if (gameType.equalsIgnoreCase("bbin电子游艺")) {
            return Constant.BBIN_INT;
        } else if (gameType.equalsIgnoreCase("QT电子游艺")) {
            return Constant.QT_INT;
        } else if (gameType.equalsIgnoreCase("PT电子游艺")) {
            return Constant.PT_INT;
        } else if (gameType.equalsIgnoreCase("PG电子游艺")) {
            return Constant.PG_INT;
        } else if (gameType.equalsIgnoreCase("CQ9电子游艺")) {
            return Constant.CQ9_INT;
        } else if (gameType.equalsIgnoreCase("BG电子游艺")) {
            return Constant.BG_INT;
        }
        return 0;

    }

    public static int convertRealMainPlatformValue(String platform) {
        Utils.LOG("aa", "platform === " + platform);
        if (platform.equalsIgnoreCase("AG")) {
            return Constant.AG_INT;
        } else if (platform.equalsIgnoreCase("MG")) {
            return Constant.MG_INT;
        } else if (platform.equalsIgnoreCase("QT")) {
            return Constant.QT_INT;
        } else if (platform.equalsIgnoreCase("ALLBET")) {
            return Constant.ALLBET_INT;
        } else if (platform.equalsIgnoreCase("PT")) {
            return Constant.PT_INT;
        } else if (platform.equalsIgnoreCase("BBIN")) {
            return Constant.BBIN_INT;
        } else if (platform.equalsIgnoreCase("OG")) {
            return Constant.OG_INT;
        } else if (platform.equalsIgnoreCase("DS")) {
            return Constant.DS_INT;
        } else if (platform.equalsIgnoreCase("BG")) {
            return Constant.BG_INT;
        } else if (platform.equalsIgnoreCase("DG")) {
            return Constant.DG_INT;
        } else if (platform.equalsIgnoreCase("皇家真人(RG)")) {
            return Constant.RG_INT;
        } else if (platform.equalsIgnoreCase("性感赌场(NT)")) {
            return Constant.NT_INT;
        } else if (platform.equalsIgnoreCase("EBET真人")) {
            return Constant.EBET_INT;
        } else if (platform.equals("利博真人")) {
            return Constant.LB_INT;
        }
        return 0;
    }

    public static int convertChessMainPlatformValue(String platform) {
        Utils.LOG("aa", "platform === " + platform);
        if (platform.equalsIgnoreCase("NB")) {
            return Constant.NB_TYPE;
        } else if (platform.equalsIgnoreCase("KY")) {
            return Constant.KY_TYPE;
        } else if (platform.equalsIgnoreCase("YG")) {
            return Constant.YG_TYPE;
        } else if (platform.equalsIgnoreCase("LEG")) {
            return Constant.LEG_TYPE;
        } else if (platform.equalsIgnoreCase("YB")) {
            return Constant.YB_TYPE;
        } else if (platform.equalsIgnoreCase("BS")) {
            return Constant.BS_TYPE;
        }
        return 0;
    }

    public static final String convertSportPan(String plate) {
        if (plate.equalsIgnoreCase("H")) {
            return "亚洲盘";
        }
        return "亚洲盘";
    }

    public static void combination(String[] ia, int n, List<String> results) {
        combination("", ia, n, results, "|");
    }

    public static void combination(String[] ia, int n, List<String> results, String format) {
        combination("", ia, n, results, format);
    }


    /**
     * 计算组合结果
     *
     * @param s
     * @param ia      源数据
     * @param n       组合次数
     * @param format  号码分隔符格式
     * @param results
     */
    public static void combination(String s, String[] ia, int n, List<String> results, String format) {
        if (n == 1) {
            for (int i = 0; i < ia.length; i++) {
//                System.out.println(s + ia[i]);
                results.add(s + ia[i]);
            }
        } else {
            for (int i = 0; i < ia.length - (n - 1); i++) {
                String ss = "";
                ss = s + ia[i] + format;
                // 建立从i开始的子数组
                String[] ii = new String[ia.length - i - 1];
                for (int j = 0; j < ia.length - i - 1; j++) {
                    ii[j] = ia[i + j + 1];
                }
                combination(ss, ii, n - 1, results, format);
            }
        }
    }

    /**
     * 上传文件加参数 接口
     *
     * @param url
     * @param map  参数map
     * @param file
     */
    public static void postFile(Context context, final String url,
                                final Map<String, Object> map, File file,
                                final FileResultCallback callback) {
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            requestBody.addFormDataPart("headImage", file.getName(), body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Connection", "close").post(requestBody.build()).tag(context).build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(90000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("file", "上传文件失败");
                if (callback != null) {
                    callback.fileResult(false, "");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if (response.isSuccessful()) {
                    if (callback != null) {
                        callback.fileResult(true, str);
                    }
                    Log.i("file", response.message() + " , body " + str);
                } else {
                    if (callback != null) {
                        callback.fileResult(false, str);
                    }
                    Log.i("file", response.message() + " error : body " + response.body().string());
                }
            }
        });
    }

    /**
     * 1 待确认
     * 2：已确认
     * 3：已取消 (滚球系统自动取消)
     * 4: 手动取消
     */
    public static String convertSportRecordStatus(long status, float betResult) {
        if (status == Constant.BALANCE_UNDO) {
            return "等待开奖";
        }
        if (status == Constant.BALANCE_CUT_GAME) {
            return "赛事取消";
        }

        if (status == Constant.BALANCE_DONE || status == Constant.BALANCE_AGENT_HAND_DONE
                || status == Constant.BALANCE_BFW_DONE) {
            if (betResult > 0) {
                return "派彩:" + betResult + "元";
            }
            return "未中奖";
        }
        return "等待开奖";
    }

    /**
     * 1 待确认
     * 2：已确认
     * 3：已取消 (滚球系统自动取消)
     * 4: 手动取消
     */
    public static String convertSportBettingStatus(long status) {
        if (status == 1) {
            return "待确认";
        } else if (status == 2) {
            return "已确认";
        } else if (status == 3) {
            return "已取消";
        } else if (status == 4) {
            return "系统取消";
        }
        return "等待开奖";
    }

    public static String convertResultStatus(int status) {
        if (status == Constant.BALANCE_ALL_LOST) {
            return "全输";
        } else if (status == Constant.BALANCE_HALF_LOST) {
            return "输一半";
        } else if (status == Constant.BALANCE_DRAW) {
            return "平局";
        } else if (status == Constant.BALANCE_HALF_WIN) {
            return "赢一半";
        } else if (status == Constant.BALANCE_ALL_WIN) {
            return "全赢";
        }
        return "等待开奖";
    }

    public static String convertResultStatusAccordingBalance(int status, int balance, String remark, float winMoney, String statusRemark) {

        if (balance == 1) {
            return "等待开奖";
        } else if (balance == 4) {
            if (status == 3 || status == 4) {
                return !Utils.isEmptyString(remark) ? remark : "暂无备注";
            } else if (status == 6)
                return statusRemark;
            else {
                return "赛事腰斩";
            }
        } else if (balance == 3) {
            return "订单取消";
        } else if (balance == 2) {
            switch (status) {
                case 1:
                    return "全输";
                case 2:
                    return "输一半(派彩:" + winMoney + ")";
                case 3:
                    return "平(退回本金" + winMoney + ")";
                case 4:
                    return "赢一半(派彩:" + winMoney + ")";
                case 5:
                    return "全赢(派彩:" + winMoney + ")";
                case 6:
                    return "撤单(退回本金:" + winMoney + ")";
                default:
                    return "已结算(派彩:" + winMoney + ")";
            }
        }
        return "";
    }

    public static String convertSportBallon(int ballType) {
        if (ballType == Constant.ALL_BALL_CONSTANT) {
            return "全部";
        } else if (ballType == Constant.FOOTBALL_CONSTANT) {
            return "足球";
        } else if (ballType == Constant.BASCKETBALL_CONSTANT) {
            return "篮球";
        }
        return "全部";
    }

    public static CrazyRequest buildBetsTokenRequest(Context context, int requestCode) {
        StringBuilder qiHaoUrl = new StringBuilder();
        qiHaoUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.TOKEN_BETS_URL);
        CrazyRequest<CrazyResult<BetToken>> request = new AbstractCrazyRequest.Builder().
                url(qiHaoUrl.toString())
                .seqnumber(requestCode)
                .headers(Urls.getHeader(context))
                .placeholderText(context.getString(R.string.vertify_bet_tokening))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<BetToken>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        return request;
    }

    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 打开微信并跳入到二维码扫描页面
     *
     * @param act
     */
    public static void toWeChatScan(Activity act) {
        try {
            Intent intent = act.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            act.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            showToastLong(act, "没有检测到微信客户端");
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param info 显示的内容
     */
    public static Toast showToastLong(Context ctx, String info) {
        if (ctx == null) return null;

        Toast toast = Toast.makeText(ctx, info, Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }

    // 支付宝包名
    private static final String ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone";

    /**
     * 检查支付宝是否安装
     *
     * @param context
     * @return
     */
    public static boolean hasInstalledAlipayClient(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(ALIPAY_PACKAGE_NAME, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 调用此方法跳转到支付宝
     *
     * @param activity
     * @param urlCode
     * @return
     */
    public static boolean startAlipayClient(Activity activity, String urlCode) {
        return startIntentUrl(activity, doFormUri(urlCode));
    }


    private static String doFormUri(String urlCode) {
        try {
            urlCode = URLEncoder.encode(urlCode, "utf-8");
        } catch (Exception e) {
        }
        final String alipayqr = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + urlCode;
        String openUri = alipayqr + "%3F_s%3Dweb-other&_t=" + System.currentTimeMillis();
        return openUri;
    }


    /**
     * 跳转到支付宝
     *
     * @param activity
     * @param intentFullUrl
     * @return
     */
    private static boolean startIntentUrl(Activity activity, String intentFullUrl) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentFullUrl));
            activity.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 保存图片到手机指定目录
    public static boolean savaBitmap(String imgName, byte[] bytes) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = null;
            FileOutputStream fos = null;
            try {
                filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                File imgDir = new File(filePath);
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }
                imgName = filePath + "/" + imgName;
                fos = new FileOutputStream(imgName);
                fos.write(bytes);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 上传文件加参数 接口
     *
     * @param url
     * @param map  参数map
     * @param file
     */
    public static void postFile(Context context, final String url,
                                final Map<String, Object> map, File file,
                                final FileUploadCallback callback) {
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            requestBody.addFormDataPart("headImage", file.getName(), body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request.Builder builder = new Request.Builder().url(url).post(requestBody.build()).tag(context);
        Map<String, String> headers = Urls.getHeader(context);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = builder.build();
        client.newBuilder().readTimeout(UPLOAD_TIMEOUT, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("file", "上传文件失败");
                if (callback != null) {
                    callback.uploadResult(false, "");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if (response.isSuccessful()) {
                    if (callback != null) {
                        callback.uploadResult(true, str);
                    }
                    Log.i("file", response.message() + " , body " + str);
                } else {
                    if (callback != null) {
                        callback.uploadResult(false, str);
                    }
//                    Log.i("file", response.message() + " error : body " + response.body().string());
                }
            }
        });
    }


    /**
     * 上传本地文件
     *
     * @param context
     * @param params   请求参数集
     * @param filePath 文件路径
     */
    public static void uploadLocalFile(Context context, Map<String, Object> params,
                                       String filePath, String url,
                                       FileUploadCallback callback) {

        if (Utils.isEmptyString(filePath)) {
            return;
        }
        final File file = new File(filePath);
        if (!file.exists()) {
            Utils.LOG("a", "file unexit ,path = " + filePath);
            return;
        }
        UsualMethod.postFile(context, url, params, file, callback);
    }

    /**
     * 开始获取当前期号离结束投注倒计时
     *
     * @param bianHao 彩种编号
     */
    public static void getCountDownByCpcode(Context context, String bianHao, int requestCode) {
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOTTERY_COUNTDOWNV2_URL);
        configUrl.append("?lotCode=").append(bianHao);
        Log.e("whw", configUrl.toString());
        CrazyRequest<CrazyResult<LocCountDownWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(requestCode)
                .headers(Urls.getHeader(context))
                .shouldCache(false)
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LocCountDownWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
    }

    public static boolean viewService(Context context) {
        String linkUrl = "";
        String sysConfig = YiboPreference.instance(context).getSysConfig();
        if (!Utils.isEmptyString(sysConfig)) {
            SysConfig sc = new Gson().fromJson(sysConfig, SysConfig.class);
            if (sc != null) {
                linkUrl = sc.getCustomerServiceUrlLink();
            }
        }
        if (Utils.isEmptyString(linkUrl)) {
            return false;
        }
        String url = linkUrl.trim();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean viewService(Context context, String version) {
        String linkUrl = "";

        String sysConfig = YiboPreference.instance(context).getSysConfig();
        String live700Secret = "";
        if (!Utils.isEmptyString(sysConfig)) {
            SysConfig sc = new Gson().fromJson(sysConfig, SysConfig.class);
            if (sc != null) {
                linkUrl = sc.getCustomerServiceUrlLink();
                live700Secret = sc.getLive700_secret();
            }
        }
        if (Utils.isEmptyString(linkUrl)) {
            return false;
        }
        //固定移除 userID userName，根据有无登入状态带入 userID userName。
        String url;
        if(!linkUrl.contains("http")){
            linkUrl=Urls.BASE_URL+linkUrl;
        }
        if (YiboPreference.instance(context).isLogin()) {
            url = removeParam(linkUrl.trim(), "userID", "userName", "timestamp", "sign");
            if (url.contains("?")) {
                url += "&userID=" + YiboPreference.instance(context).getAccountId() + "&userName=" + YiboPreference.instance(context).getUsername();
            } else {
                url += "?userID=" + YiboPreference.instance(context).getAccountId() + "&userName=" + YiboPreference.instance(context).getUsername();
            }

            if(!TextUtils.isEmpty(live700Secret)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String time = sdf.format(new Date());
                String sb = "userId=" + YiboPreference.instance(context).getAccountId() +
                        "&userName=" + YiboPreference.instance(context).getUsername() +
                        "&timestamp=" + time +
                        "&secret=" + live700Secret;
                String sign;

                try{
                    sign = Utils.getMD5(sb.toString().toLowerCase());
                }catch (Exception e){
                    e.printStackTrace();
                    sign = "";
                }

                url += "&timestamp=" + time;
                url += "&sign=" + sign;
            }
        }else {
            url = linkUrl.trim();
        }
        if ("v1".equals(version)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            // 跳转到对话页面
            Intent intent = new Intent(context, Live800ChattingActivity.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
            return true;
        }

    }

    public static void viewLink(Context context, String url) {
        try {
            url = replaceurl(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            //intent.setClassName("com.UCMobile","com.uc.browser.InnerUCMobile");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //多浏览器选择
    public static void viewLink(Context context, String url, String browser) {

        switch (browser) {
            case "0":
                viewLink(context, url);
                break;
            case "1":
                try {
                    if (isAppInstalled(context, "com.UCMobile")) {
                        url = replaceurl(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setClassName("com.UCMobile", "com.UCMobile.main.UCMobile");
                        context.startActivity(intent);
                    } else if (isAppInstalled(context, "com.uc.browser")) {
                        url = replaceurl(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setClassName("com.uc.browser", "com.uc.browser.ActivityUpdate");
                        context.startActivity(intent);
                    } else {
                        SharedPreferences sp = context.getSharedPreferences("browser", 0);
                        sp.edit().clear().commit();
                        Toast.makeText(context, "亲，您尚未安装UC浏览器.", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "2":
                try {
                    if (isAppInstalled(context, "com.tencent.mtt")) {
                        url = replaceurl(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setClassName("com.tencent.mtt", "com.tencent.mtt.MainActivity");
                        context.startActivity(intent);
                    } else {
                        SharedPreferences sp = context.getSharedPreferences("browser", 0);
                        sp.edit().clear().commit();
                        Toast.makeText(context, "亲，您尚未安装QQ浏览器.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "3":
                try {
                    if (isAppInstalled(context, "com.android.chrome")) {
                        url = replaceurl(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main");
                        context.startActivity(intent);
                    } else {
                        SharedPreferences sp = context.getSharedPreferences("browser", 0);
                        sp.edit().clear().commit();
                        Toast.makeText(context, "亲，您尚未安装谷歌浏览器.", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "4":
                try {
                    if (isAppInstalled(context, "org.mozilla.firefox")) {
                        url = replaceurl(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setClassName("org.mozilla.firefox", "org.mozilla.gecko.BrowserApp");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "亲，您尚未安装火狐浏览器.", Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = context.getSharedPreferences("browser", 0);
                        sp.edit().clear().commit();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }


    }

    static String replaceurl(String url) {
        if (url.startsWith("HTTPS")) {
            url = url.replace("HTTPS", "https");
        } else if (url.startsWith("HTTP")) {
            url = url.replace("HTTP", "http");
        } else if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return url;
    }


    static boolean isAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 同步后台最新支付方式
     *
     * @param context
     * @param requestCode
     */
    public static void syncPayMethod(Context context, int requestCode, boolean showDialog) {
        StringBuilder url = new StringBuilder();
        url.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.PAY_METHODS_URL);
        CrazyRequest<CrazyResult<PaySysMethodWraper>> request = new AbstractCrazyRequest.Builder().
                url(url.toString())
                .seqnumber(requestCode)
                .placeholderText(context.getString(R.string.sync_pay_methoding))
                .headers(Urls.getHeader(context))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<PaySysMethodWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
    }

    public static void registGuest(Context context, int requestCode) {
        StringBuilder urls = new StringBuilder();
        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.REG_GUEST_URL);
        CrazyRequest<CrazyResult<RegisterResultWrapper>> request = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(requestCode)
                //.listener()
                .headers(Urls.getHeader(context))
                .placeholderText(context.getString(R.string.free_guest_loading))
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<RegisterResultWrapper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
    }

    public static void updateLocImage(Context context, ImageView lotImageView, String lotCode) {
        //彩种的图地址是根据彩种编码号为姓名构成的
        String imgUrl = Urls.BASE_URL + Urls.PORT + "/native/resources/images/" + lotCode + ".png";
//        Utils.LOG("a", "the pic url = " + imgUrl);
        GlideUrl glideUrl = getGlide(context, imgUrl);
        RequestOptions options = new RequestOptions().placeholder(R.drawable.default_lottery)
                .error(R.drawable.default_lottery);
        Glide.with(context).load(glideUrl).
                apply(options)
                .into(lotImageView);
    }

    public static boolean isSscZuxuan(String playCode) {

        String[] codes = new String[]{PlayCodeConstants.zuxuansan_housan, PlayCodeConstants.zuxuansan_qiansan,
                PlayCodeConstants.zuxuansan_zhongsan, PlayCodeConstants.zuxuanliu_housan, PlayCodeConstants.zuxuanliu_zhongsan,
                PlayCodeConstants.zuxuansan_qiansan, PlayCodeConstants.zuxuan_san_peilv, PlayCodeConstants.zuxuan_liu_peilv
        };
        if (Arrays.asList(codes).contains(playCode)) {
            return true;
        }
        return false;
    }

    public static void updateLocImageWithUrl(Context context, ImageView lotImageView, String url) {
        //彩种的图地址是根据彩种编码号为姓名构成的
//        Utils.LOG("a", "the pic url = " + url);
        if (Utils.isEmptyString(url)) {
            lotImageView.setBackgroundResource(R.drawable.default_placeholder_picture);
            return;
        }
        GlideUrl glideUrl = getGlide(context, url);
        RequestOptions options = new RequestOptions().placeholder(R.drawable.default_lottery)
                .error(R.drawable.default_lottery);
        Glide.with(context).load(glideUrl).
                apply(options)
                .into(lotImageView);
    }

    public static void updateLocImageWithUrl(Context context, ImageView lotImageView, String url, int defaultIcon) {
        //彩种的图地址是根据彩种编码号为姓名构成的
        if (Utils.isEmptyString(url)) {
            lotImageView.setBackgroundResource(defaultIcon);
            return;
        }
//        Utils.LOG("a", "the pic url = " + url);
        GlideUrl glideUrl = getGlide(context, url);
        RequestOptions options = new RequestOptions().placeholder(defaultIcon)
                .error(defaultIcon);
        Glide.with(context).load(glideUrl).
                apply(options)
                .into(lotImageView);
    }

    public static String forwardGame(Context context, String playCode, int requestCode,
                                     SessionResponse.Listener<CrazyResult<Object>> listener) {
        return forwardGame(context, playCode, requestCode, listener, "");
    }

    //获取电子游戏转发数据
    public static String forwardGame(Context context, String playCode, int requestCode,
                                     SessionResponse.Listener<CrazyResult<Object>> listener,
                                     String relativeUrl) {
        StringBuilder url = new StringBuilder();
        url.append(Urls.BASE_URL).append(Urls.PORT).append(relativeUrl);
        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(url.toString())
                .seqnumber(requestCode)
                .listener(listener)
                .headers(Urls.getHeader(context))
                .placeholderText(context.getString(R.string.forward_jumping))
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(null)
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
        return "";
    }

    public static void actionViewGame(Context context, String url) {
        try {
            if (url.startsWith("HTTPS")) {
                url = url.replace("HTTPS", "https");
            } else if (url.startsWith("HTTP")) {
                url = url.replace("HTTP", "http");
            }
            if (url.startsWith("www")) {
                url = url.replace("www", "https://www");
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断网站是否在维护中
     *
     * @param result
     * @return
     */
    public static String isMaintenancing(String result) {
        try {

            if (!Utils.isEmptyString(result)) {
                String[] split = result.split("\\{");
                if (split.length > 1) {
                    String json = "{" + split[split.length - 1];
                    JSONObject rootObject = new JSONObject(json);
                    if (!rootObject.isNull("code") && rootObject.getString("code").equalsIgnoreCase("987200156")) {
                        if (rootObject.has("msg")) {
                            return rootObject.getString("msg");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String fixPayIconWithIconCss(String iconCss) {
        if (Utils.isEmptyString(iconCss)) {
            return "";
        }
        if (iconCss.equalsIgnoreCase("wechat")) {
            return "/native/resources/images/weixin.jpg";
        } else if (iconCss.equalsIgnoreCase("alipay")) {
            return "/native/resources/images/zhifubao.jpg";
        } else if (iconCss.equalsIgnoreCase("qq")) {
            return "/native/resources/images/qqpay.jpg";
        } else if (iconCss.equalsIgnoreCase("jd")) {
            return "/native/resources/images/jdpay.jpg";
        } else if (iconCss.equalsIgnoreCase("baidu")) {
            return "/native/resources/images/baidu.jpg";
        } else if (iconCss.equalsIgnoreCase("union")) {
            return "/native/resources/images/union.jpg";
        }
        return "";
    }

    //获取真人转发数据
    public static void forwardRealInCommon(Context context, int requestCode, String playCode,
                                           SessionResponse.Listener<CrazyResult<Object>> listener) {
        //获取列表数据
        StringBuilder url = new StringBuilder();
        Log.e("123123", "playCode=" + playCode);
        url.append(Urls.BASE_URL).append(Urls.PORT);
        if (playCode.equalsIgnoreCase("ag")) {//AG真人娱乐
            url.append(Urls.REAL_AG_URL);
            url.append("?h5=1&gameType=11");
        } else if (playCode.equalsIgnoreCase("mg")) {//MG真人娱乐
            url.append(Urls.REAL_MG_URL);
            url.append("?gameType=1&gameid=66936");
        } else if (playCode.equalsIgnoreCase("ebet")) {
            url.append(Urls.REAL_EBET_URL);
            url.append("?gameType=1&mobile=1");
        }
//        else if (playCode.equalsIgnoreCase("bbin")) {//BBIN真人娱乐
////            url.append(Urls.REAL_BBIN_URL);
////            url.append("?gameType=60&mobile=1");
//        }
        else if (playCode.equalsIgnoreCase("bbin")) {//BBIN真人娱乐
            url.append(Urls.REAL_BBIN_URL2);
            url.append("?gameType=60&mobile=1");
        } else if (playCode.equalsIgnoreCase("ab")) {//ab真人娱乐
            url.append(Urls.REAL_AB_URL);
            url.append("?mobile=1");
        } else if (playCode.equalsIgnoreCase("og")) {//OG真人娱乐
            url.append(Urls.REAL_OG_URL);
            url.append("?mobile=1");
        } else if (playCode.equalsIgnoreCase("ds")) {//ds真人娱乐
            url.append(Urls.REAL_DS_URL);
            url.append("?mobile=1");
        } else if (playCode.equalsIgnoreCase("bgzr")) {//bg真人娱乐
            url.append(Urls.GAME_BG_URL);
            url.append("?gameType=1&mobile=1");
        } else if (playCode.equalsIgnoreCase("dgzr")) {//dg真人娱乐
            url.append(Urls.GAME_DG_URL);
            url.append("?gameType=1&mobile=1");
        } else if (playCode.equalsIgnoreCase("rgzr")) {//rg真人娱乐
            url.append(Urls.GAME_RG_URL);
            url.append("?gameType=1&mobile=1");
        } else if (playCode.equalsIgnoreCase("nt")) {//rg真人娱乐
            url.append(Urls.GAME_NT_URL);
            url.append("?gameType=1&mobile=1");
        }


        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(url.toString())
                .seqnumber(requestCode)
                .listener(listener)
                .headers(Urls.getHeader(context))
                .placeholderText(context.getString(R.string.forward_jumping))
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(null)
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();

        RequestManager.getInstance().startRequest(context, request);
    }

    public static void actionOpenGameList(Context context, String gameCode) {
        String name = "";
        if (gameCode.equalsIgnoreCase("mg")) {
            name = "MG电子游戏";
        } else if (gameCode.equalsIgnoreCase("pt")) {
            name = "PT电子游戏";
        } else if (gameCode.equalsIgnoreCase("nb")) {
            name = "NB棋牌";
        } else if (gameCode.equalsIgnoreCase("qt")) {
            name = "QT电子游戏";
        } else if (gameCode.equalsIgnoreCase("kyqp")) {
            name = "开元棋牌";
        } else if (gameCode.equalsIgnoreCase("bgdz")) {
            name = "BG电子";
        } else if (gameCode.equalsIgnoreCase("cq9")) {
            name = "CQ9电子";
        } else if (gameCode.equalsIgnoreCase("yg")) {
            name = "王者棋牌";
        }
        GameListActivity.createIntent(context, name, gameCode);
    }

    //获取电子游戏跳转链接
    public static void forwardGameUseWebLink(Context context, int requestCode, String url) {
        //获取列表数据
        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(url.toString())
                .seqnumber(requestCode)
                .headers(Urls.getHeader(context))
                .placeholderText(context.getString(R.string.forward_jumping))
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(null)
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
    }


    ////在选择下注号码时不变更赔率显示项
    public static boolean dontShowPeilvWhenTouzhu(String playCode) {
        if (playCode.equalsIgnoreCase(PlayCodeConstants.weishulian) || playCode.equalsIgnoreCase(PlayCodeConstants.lianxiao) || playCode.equalsIgnoreCase(PlayCodeConstants.hexiao)) {
            return true;
        }
        return false;
    }

    /**
     * 弹框提示签到成功，获取积分内容
     */
    public static void showSignSuccessDialog(Context context, String content) {

        final CustomConfirmDialog ccd = new CustomConfirmDialog(context);
        ccd.setBtnNums(1);
        ccd.setContent(content);
        ccd.setTitle("签到成功");
        ccd.setMiddleBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }

    public static void actionLoginOut(Context context, int requestCode, boolean showDialog, SessionResponse.Listener<?> listener) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOGIN_OUT_URL);
        CrazyRequest<CrazyResult<LoginOutWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(requestCode)
                .headers(Urls.getHeader(context))
                .listener(listener)
                .shouldCache(false).placeholderText(context.getString(R.string.loginout_onging))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LoginOutWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
    }

    public static void syncHeader(Context context, int requestCode, boolean showDialog, SessionResponse.Listener<?> listener) {
        String userName = YiboPreference.instance(context).getUsername();
        String stationId = YiboPreference.instance(context).getPwd();
        String headerKey = "";
        try {
            headerKey = Utils.getMD5(stationId + userName + "project-yibo" + UsualMethod.getPackageName(context));//在外部项目上传时，暂时是将(站点代号+用户名+项目来源)的md5值当文件名
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.ACQUIRE_HEADER_URL);
        configUrl.append("?headerKey=").append(headerKey);
        CrazyRequest<CrazyResult<MemberHeaderWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(requestCode)
                .headers(Urls.getHeader(context))
                .listener(listener)
                .shouldCache(false).placeholderText(context.getString(R.string.acquire_award_ongoing))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<MemberHeaderWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
    }

    //根据彩票编码获取所应的玩法数据
    public static void syncLotteryPlaysByCode(Context context, String lotteryCode, int request, SessionResponse.Listener<?> listener) {
        syncLotteryPlaysByCode(context, lotteryCode, true, request, listener);
    }

    public static void syncLotteryPlaysByCode(Context context, String lotteryCode, boolean showLoading, int request, SessionResponse.Listener<?> listener) {
        if (context instanceof Activity) {
            boolean finish = ((Activity) context).isFinishing();
            if (finish) {
                return;
            }
        }
        StringBuilder urls = new StringBuilder();
        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GAME_PLAYS_URL);
        urls.append("?lotCode=").append(lotteryCode);
        CrazyRequest<CrazyResult<LocPlaysWraper>> playsRequest = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(request)
                .headers(Urls.getHeader(context))
                .shouldCache(false)
                .listener(listener)
                .placeholderText(context.getString(R.string.acquire_lottery_playing))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LocPlaysWraper>() {
                }.getType()))
                .loadMethod(showLoading ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() : CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, playsRequest);
    }

    public static CrazyRequest startAsyncConfig(Context context, int requestCode) {
//        loadingText.setText(getString(R.string.sync_sys_configing));
        //获取系统配置
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SYS_CONFIG_URL);
        CrazyRequest<CrazyResult<SysConfigWraper>> configRequest = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(requestCode)
                .headers(Urls.getHeader(context))
                .shouldCache(false)
                .cachePeroid(3 * 60)
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<SysConfigWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
//        RequestManager.getInstance().startRequest(context,configRequest);
        return configRequest;
    }

    public static CrazyRequest getActiveBadges(Context context, int requestCode, SessionResponse.Listener<?> listener) {
//        loadingText.setText(getString(R.string.sync_sys_configing));
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ACTIVE_BADGE_URL);
        CrazyRequest<CrazyResult<ActiveBadgeWrapper>> configRequest = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(requestCode)
                .headers(Urls.getHeader(context))
                .shouldCache(false)
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<ActiveBadgeWrapper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        if (listener != null) {
            configRequest.setListener(listener);
        }
        return configRequest;
    }

    public static void showUpdateContentDialog(
            final Context context, String lastVersion, String content, final String updateWebsite) {
        showUpdateContentDialog(context, lastVersion, content, updateWebsite, null);
    }

    public interface UpdateDialogEvent {
        void onDialogEvent();
    }

    public static void showUpdateContentDialog(
            final Context context, String lastVersion, String content, final String updateWebsite, final UpdateDialogEvent event) {

        final CustomConfirmDialog ccd = new CustomConfirmDialog(context);
        ccd.setTitle("发现新版本");
        ccd.setBtnNums(2);
        ccd.setLeftBtnText("取消");
        ccd.setRightBtnText("去更新");


        StringBuilder contents = new StringBuilder();
        contents.append(context.getString(R.string.app_name)).append(" ").append(lastVersion).append(":\n\n");

        String force_update_app = getConfigFromJson(context).getForce_update_app() == null ? "off" : getConfigFromJson(context).getForce_update_app();
        final boolean isForceUpdate = force_update_app.equalsIgnoreCase("on");

        if (!Utils.isEmptyString(content)) {
            String[] split = content.split(";");
            if (split.length > 0) {
                for (String item : split) {
                    contents.append(item).append("\n");
                }
            } else {
                contents.append(content);
            }
        }
        contents.append("\n");
        ccd.setContent(contents.toString());

        ccd.setRightBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
                showListDialog(context, updateWebsite, event, isForceUpdate);
                YiboPreference.instance(context).setCheckUpdateFiest(false);


            }
        });
        ccd.setLeftBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
                if (event != null) {
                    event.onDialogEvent();
                }
            }
        });
//        ccd.setTouchOutCancel(false);
        ccd.setCancelable(false);
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }

    public static void actionLogin(String username, String pwd, String validateCode, Context context, String vcode, boolean showDialog, HttpCallBack callback) {
        //登录请求不用提示，且改为异步；不用等登录结果返回再进主页 modify 2020-01-16
//        showLoginDialog();
//        if (logingDialog != null)
//            logingDialog.updateTitle(getString(R.string.auto_logining));
        //end modify 2020-01-16
        if (Utils.isEmptyString(username) || Utils.isEmptyString(pwd)) {
            YiboPreference.instance(context).setLoginState(false);
            return;
        }

        String uuidjson = YiboPreference.instance(context).getLoginUuid();
        String uuid = "";
        if (!TextUtils.isEmpty(uuidjson)) {
            List<LoginUUIDBean> list = new Gson().fromJson(uuidjson, new TypeToken<List<LoginUUIDBean>>() {
            }.getType());
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getUsername().equals(username)) {
                        uuid = list.get(i).getUuid();
                    }
                }
            }
        }
        SysConfig sc = null;
        String sysConfig = YiboPreference.instance(context).getSysConfig();
        if (!Utils.isEmptyString(sysConfig)) {
            sc = new Gson().fromJson(sysConfig, SysConfig.class);
        }

        String stationId = "";
        if (sc != null && !Utils.isEmptyString(sc.getStation_id())) {
            stationId = sc.getStation_id();
        }
        String preffix = stationId + ":" + "android/" + Utils.getVersionName(context) + ":" + "ANDROID" + PhoneInfo.getManufacturer() + "-" + PhoneInfo.getPhoneModel();
        preffix += ":" + BuildConfig.apk_code;
        String preffixKey = com.example.anuo.immodule.utils.AESUtils.encrypt(preffix, ConfigCons.DEFAULT_IV, ConfigCons.DEFAULT_KEY);
        String source = preffix + "---" + username + "---" + pwd + "---" + (!Utils.isEmptyString(vcode) ? vcode : "");
        String dataEncrypt = com.example.anuo.immodule.utils.AESUtils.encrypt(source, ConfigCons.DEFAULT_KEY, ConfigCons.DEFAULT_IV);

        ApiParams params = new ApiParams();
        params.put("data", dataEncrypt);
        params.put("key", preffixKey);
        params.put("uuid", uuid);
        if(TextUtils.isEmpty(validateCode)){
            params.put("apiVersion", 1);
        }else {
            params.put("apiVersion", 3);
            params.put("validateCode", validateCode);
        }
        HttpUtil.postForm(context, Urls.LOGIN_NEWV2_URL, params, showDialog, context.getString(R.string.login_ongoing), callback);
//        HttpUtil.postForm(context, "/native/vipLogin666.do", params, showDialog, context.getString(R.string.login_ongoing), callback);
    }

    public static CrazyRequest checkUpdate(Context context, String curVersion, String bundleId,
                                           boolean showDialog, int requestCode, int signVersion) {
        return checkUpdate(context, curVersion, bundleId, showDialog, requestCode, "android", signVersion);
    }

    public static CrazyRequest checkUpdate(Context context, String curVersion, String bundleId,
                                           boolean showDialog, int requestCode, String platform, int signVersion) {

        StringBuilder configUrl = new StringBuilder();
//        int signVersion = YiboPreference.instance(context).getCheckUpdateVersion();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.CHECK_UPDATE_URL);
        configUrl.append("?curVersion=").append(curVersion);
        configUrl.append("&appID=").append(bundleId);
        configUrl.append("&platform=").append(platform);
        configUrl.append("&superSign=").append(signVersion);
        CrazyRequest<CrazyResult<CheckUpdateWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(requestCode)
                .headers(Urls.getHeader(context))
                .shouldCache(false).placeholderText(context.getString(R.string.version_checking))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<CheckUpdateWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        return request;
//        RequestManager.getInstance().startRequest(this,request);
    }

    //根据选择的赔率数据，计算出要提交的投注号码
    public static String getPeilvPostNumbers(PeilvPlayData data) {
        if (data == null) {
            return "";
        }
        String postNumber = "";
        if (data.isAppendTag()) {
            if (data.isAppendTagToTail()) {
                postNumber = data.getNumber() + "--" + data.getItemName();
            } else {
                postNumber = data.getItemName() + "--" + data.getNumber();
            }
        } else {
            postNumber = data.getNumber();
        }
        return postNumber;
    }

    private static String getShenxiaoNumbers(int startIndex) {
        int maxValue = 49;
        int tmp = startIndex;
        StringBuilder sb = new StringBuilder();
        while (tmp <= maxValue) {
            sb.append(String.format("%02d", tmp)).append(",");
            tmp += 12;
        }
        if (sb.length() > 0) {
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 根据新历时间，获取对应年份的农历年下对应号码的生肖
     *
     * @param context
     * @param time
     * @return
     */
    public static String getShenxiaoFromDate(Context context, long time) {
        Calendar c = Calendar.getInstance();
        Date d = new Date(time);
        c.setTime(d);
        Lunbar lunbar = new Lunbar(c);
        String sx = lunbar.animalsYear();
        return sx;
    }

    public static String[] getNumbersFromShengXiaoFromDate(Context context, long date) {

        String shenxiao = getShenxiaoFromDate(context, date);
        String[] arr = context.getResources().getStringArray(R.array.shengxiao_str);
        String[] results = new String[arr.length];
        int bmnIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            String item = arr[i];
            if (item.equals(shenxiao)) {
                bmnIndex = i;
                break;
            }
        }
        Utils.LOG("a", "bmnindex = " + bmnIndex);
        for (int i = 0; i < arr.length; i++) {
            int startNum = 0;
            if (i <= bmnIndex) {
                startNum = (bmnIndex - i) + 1;
            } else {
                startNum = (i - bmnIndex) - 1;
                startNum = 12 - startNum;
            }
            String numResult = arr[i] + "|" + getShenxiaoNumbers(startNum);
//            Utils.LOG("aa", "the numresult = " + numResult);
            results[i] = numResult;

        }
        return results;
    }

    public static String[] getNumbersFromShengXiao(Context context) {
        String shenxiao = getShengXiaoFromYear();
        String[] arr = context.getResources().getStringArray(R.array.shengxiao_str);
        String[] results = new String[arr.length];
        int bmnIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            String item = arr[i];
            if (item.equals(shenxiao)) {
                bmnIndex = i;
                break;
            }
        }
        Utils.LOG("a", "bmnindex = " + bmnIndex);
        for (int i = 0; i < arr.length; i++) {
            int startNum = 0;
            if (i <= bmnIndex) {
                startNum = (bmnIndex - i) + 1;
            } else {
                startNum = (i - bmnIndex) - 1;
                startNum = 12 - startNum;
            }
            String numResult = arr[i] + "|" + getShenxiaoNumbers(startNum);
//            Utils.LOG("aa", "the numresult = " + numResult);
            results[i] = numResult;

        }
        return results;
    }

    public static String[] getNumbersFromShengXiaoBaseDate(Context context, long date) {
        String shenxiao = "";
        if (date > 0) {
            shenxiao = getShenxiaoFromDate(context, date);
        } else {
            shenxiao = getShengXiaoFromYear();
        }
        String[] arr = context.getResources().getStringArray(R.array.shengxiao_str);
        String[] results = new String[arr.length];
        int bmnIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            String item = arr[i];
            if (item.equals(shenxiao)) {
                bmnIndex = i;
                break;
            }
        }
        Utils.LOG("a", "bmnindex = " + bmnIndex);
        for (int i = 0; i < arr.length; i++) {
            int startNum = 0;
            if (i <= bmnIndex) {
                startNum = (bmnIndex - i) + 1;
            } else {
                startNum = (i - bmnIndex) - 1;
                startNum = 12 - startNum;
            }
            String numResult = arr[i] + "|" + getShenxiaoNumbers(startNum);
//            Utils.LOG("aa", "the numresult = " + numResult);
            results[i] = numResult;

        }
        return results;
    }

    /**
     * 根据生肖号码获取对应的生肖名
     *
     * @param context
     * @param sxNumber
     * @return
     */
    public static String getNumbersFromShengXiaoName(Context context, String sxNumber) {
        String[] numbers = getNumbersFromShengXiao(context);
        if (numbers != null) {
            for (String s : numbers) {
                if (s.contains(sxNumber)) {
                    String[] split = s.split("\\|");
                    if (split.length == 2) {
                        return split[0];
                    }
                }
            }
        }
        return "";
    }

    public static String getNumbersFromShengXiaoName(Context context, String sxNumber, long time) {
        String[] numbers = null;
        if (time > 0) {
            numbers = getNumbersFromShengXiaoFromDate(context, time);
        } else {
            numbers = getNumbersFromShengXiao(context);
        }
        if (numbers != null) {
            for (String s : numbers) {
                if (s.contains(sxNumber)) {
                    String[] split = s.split("\\|");
                    if (split.length == 2) {
                        return split[0];
                    }
                }
            }
        }
        return "";
    }

    private static final String[] zodiacArray = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};

    /**
     * 回传该农历年代表的生肖
     */
    private static String getZodiacForLunarYear(int lunarYear) {
        int startYear = 1804;
        while (lunarYear < startYear) {    // 如果年份小于起始的甲子年(startYear = 1804),则起始甲子年往前偏移
            startYear -= 60;
        }

        return zodiacArray[(lunarYear - startYear) % 12];
    }

    public static String getShengXiaoForNumber(int lunarYear, String number) {
        int num;
        try {
            num = Integer.parseInt(number);
        } catch (Exception e) {
            return "无";
        }

        return getZodiacForLunarYear(lunarYear - num + 1);
    }

    public static boolean explosedKeys(String key) {
        String[] arr = new String[]{"sign", "order_time"};
        for (String s : arr) {
            if (s.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }

    //计算某年所属生肖
    public static String getShengXiaoFromYear() {
        Lunbar lunbar = new Lunbar(Calendar.getInstance());
        String sx = lunbar.animalsYear();
        return sx;
    }

    public static String getShengXiaoFromYear(int year) {
        Lunbar lunbar = new Lunbar(Calendar.getInstance());
        String sx = lunbar.animalsYear(year);
        return sx;
    }

    public static void getUnreadMsg(Context context, int requestCode, SessionResponse.Listener<?> listener) {
        //未读消息数
        StringBuilder msgUrl = new StringBuilder();
        msgUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.UNREAD_MSG_COUNT_URL);
        CrazyRequest<CrazyResult<UnreadMsgCountWraper>> unreadRequest = new AbstractCrazyRequest.Builder().
                url(msgUrl.toString())
                .seqnumber(requestCode)
                .headers(Urls.getHeader(context))
                .listener(listener)
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<UnreadMsgCountWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, unreadRequest);
    }

    //是否六合彩彩种
    public static boolean isSixMark(Context context, String lotCode) {
        if (Utils.isEmptyString(lotCode)) {
            return false;
        }

        try {
            String str = YiboPreference.instance(context).getSIX_MARK();
            List<String> list = new Gson().fromJson(str, new TypeToken<List<String>>() {
            }.getType());
            for (int i = 0; i < list.size(); i++) {
                if (lotCode.equals(list.get(i))) {
                    return true;
                }
            }
            if (TextUtils.isEmpty(str)) {
                return lotCode.contains("LHC");
            } else {


            }
        } catch (Exception ex) {
            return lotCode.contains("LHC");
        }
        return lotCode.contains("LHC");
    }

    public static boolean isXGSixMark(String lotCode) {
        if (Utils.isEmptyString(lotCode)) {
            return false;
        }
        return lotCode.contains("LHC");
    }

    public static PeilvWebResult getPeilvData(Context context, String lotCode, String playCode, int selectCount, PeilvPlayData data) {
        if (isSixMark(context, lotCode)) {
            return getRightDataWhenSixMark(playCode, data);
        } else {
            return getRightData(selectCount, data.getAllDatas());
        }
    }

    public static boolean showConvertFee(Context context) {
        SysConfig sc = UsualMethod.getConfigFromJson(context);
        if (sc == null) {
            return false;
        }
        //更新额度转换按钮显示与否
        boolean zr = !Utils.isEmptyString(sc.getOnoff_zhen_ren_yu_le()) && sc.getOnoff_zhen_ren_yu_le().equals("on");
        boolean dz = !Utils.isEmptyString(sc.getOnoff_dian_zi_you_yi()) && sc.getOnoff_dian_zi_you_yi().equals("on");
        boolean sb = !Utils.isEmptyString(sc.getOnoff_shaba_sports_game()) && sc.getOnoff_shaba_sports_game().equals("on");
        boolean nb = !Utils.isEmptyString(sc.getOnoff_nb_game()) && sc.getOnoff_nb_game().equals("on");
        boolean ky = !Utils.isEmptyString(sc.getOnoff_ky_game()) && sc.getOnoff_ky_game().equals("on");
        boolean tt = !Utils.isEmptyString(sc.getOnoff_tt_lottery_game()) && sc.getOnoff_tt_lottery_game().equals("on");

        if (zr || dz || sb || nb || ky || tt) {
            return true;
        }
        return false;
    }

    /**
     * 多选下注的情况下，从所有赔率数据中选择用户选择项数对应的那个赔率数据
     *
     * @param selectCount
     * @param allDatas
     * @return
     */
    public static PeilvWebResult getRightData(int selectCount, List<PeilvWebResult> allDatas) {
        if (allDatas == null || allDatas.isEmpty()) {
            return null;
        }
        if (allDatas.size() == 1) {
            return allDatas.get(0);
        }
        PeilvWebResult selectData = null;
        for (PeilvWebResult data : allDatas) {
            if (data.getName().contains(String.valueOf(selectCount)) || data.getPlayCode().equals(PlayCodeConstants.syx5_renxuan)) {
                selectData = data;
                break;
            }
        }
        return selectData;
    }

    /**
     * 六合彩下注，且多选号码时从赔率数据列表中选择合适的赔率数据
     *
     * @param playCode 大玩法代号
     * @param data     用户选择的号码数据集中的第一个号码数据
     * @return
     */
    public static PeilvWebResult getRightDataWhenSixMark(String playCode, PeilvPlayData data) {
        if (data == null) {
            return null;
        }
        List<PeilvWebResult> allDatas = data.getAllDatas();
        if (allDatas == null || allDatas.isEmpty()) {
            return null;
        }
        for (PeilvWebResult result : allDatas) {
            if (playCode.equals(PlayCodeConstants.hexiao) || playCode.equals(PlayCodeConstants.lianxiao)) {
                if (result.getIsNowYear() != 1) {
                    return result;
                }
            } else if (playCode.equals(PlayCodeConstants.weishulian)) {
                //尾数连时，不管选择的号码包不包含"0尾"，都选择"0"尾的赔率id;这里所谓的非0尾以赔率数据中isNowYear来区分；
                //如果isNowYear为1，代表是"0尾"赔率项
                if (result.getIsNowYear() == 0) {
                    return result;
                }
            } else {
                return result;
            }
        }
        return null;
    }

    public static boolean isPeilvVersionMethod(Context context) {
        String currentVersion = YiboPreference.instance(context).getGameVersion();
        if (Utils.isEmptyString(currentVersion)) {
            return false;
        }
        int version = Integer.parseInt(currentVersion);
        if (version == Constant.lottery_identify_V2 || version == Constant.lottery_identify_V4 ||
                version == Constant.lottery_identify_V5) {
            return true;
        }
        return false;
    }

    public static void localeGameData(Context context, SavedGameData data) {
//        List<SavedGameData> datas = getUsualGame(context);
//        if (datas != null && datas.size() < 5) {
        DatabaseUtils.getInstance(context).saveUsualGame(data);
//        }
    }

    public static List<SavedGameData> getUsualGame(Context context) {
        List<SavedGameData> usualGames = DatabaseUtils.getInstance(context).getUsualGames(context, -1);
        return usualGames;
    }

    public static List<SavedGameData> getUsualCaiPiaoGame(Context context) {
        List<SavedGameData> usualGames = DatabaseUtils.getInstance(context).
                getUsualCaipiaoGames(context);
        return usualGames;
    }

    private static String configString;
    public static SysConfig getConfigFromJson(Context context) {
        if(configString == null || configString.isEmpty()){
            configString = YiboPreference.instance(context).getSysConfig();
        }

        if (Utils.isEmptyString(configString)) {
            return new SysConfig();
        }

        SysConfig sc = new Gson().fromJson(configString, SysConfig.class);
        if (sc == null) {
            return new SysConfig();
        }
        return sc;
    }

    public static String getBetStatus(String ticketStatus) {
        if (ticketStatus.equalsIgnoreCase("DRAW")) {
            return "和局";
        } else if (ticketStatus.equalsIgnoreCase("running") || ticketStatus.equalsIgnoreCase("waiting")) {
            return "未结算";
        } else if (ticketStatus.equalsIgnoreCase("Half WON") ||
                ticketStatus.equalsIgnoreCase("Half LOSE") ||
                ticketStatus.equalsIgnoreCase("WON") ||
                ticketStatus.equalsIgnoreCase("LOSE") ||
                ticketStatus.equalsIgnoreCase("VOID") ||
                ticketStatus.equalsIgnoreCase("Reject") ||
                ticketStatus.equalsIgnoreCase("Refund")
        ) {
            return "已结算";
        } else {
            return "结算失败";
        }
    }


    public static String getOrderStatus(String ticketStatus) {
        if (ticketStatus.equalsIgnoreCase("waiting")) {
            return "待确认";
        } else if (ticketStatus.equalsIgnoreCase("VOID")) {
            return "系统作废(退款)";
        } else if (ticketStatus.equalsIgnoreCase("Reject")) {
            return "系统取消(退款)";
        } else if (ticketStatus.equalsIgnoreCase("Refund")) {
            return "系统退款";
        } else {
            return "已确认";
        }
    }

    /**
     * 快三
     *
     * @param num
     * @param ball
     * @param context
     */
    private static void setKuai3NumImage(String num, TextView ball, int index, Context context) {
        String stationCode = UsualMethod.getConfigFromJson(context).getStationCode();
        if (index < 3) {
            if (num.equals("1")) {
                ball.setBackgroundResource(R.drawable.kuai3_bg_one);
                ball.setText("");
            } else if (num.equals("2")) {
                ball.setBackgroundResource(R.drawable.kuai3_bg_two);
                ball.setText("");
            } else if (num.equals("3")) {
                ball.setBackgroundResource(R.drawable.kuai3_bg_three);
                ball.setText("");
            } else if (num.equals("4")) {
                ball.setBackgroundResource(R.drawable.kuai3_bg_four);
                ball.setText("");
            } else if (num.equals("5")) {
                ball.setBackgroundResource(R.drawable.kuai3_bg_five);
                ball.setText("");
            } else if (num.equals("6")) {
                ball.setBackgroundResource(R.drawable.kuai3_bg_six);
                ball.setText("");
            } else {
                ball.setBackgroundResource(R.drawable.kuai3_bg_empty);
                ball.setText("");
            }
        } else {
//            if("a118".equalsIgnoreCase(stationCode)){
//                ball.setBackgroundResource(R.drawable.kuai3_bg_empty_bai);
//                ball.setTextColor(Color.BLACK);
//            }else {
            if ("小".equals(num) || "单".equals(num)) {
                ball.setBackgroundResource(R.drawable.icon_blue_bg);
                ball.setTextColor(Color.WHITE);
            } else if ("大".equals(num) || "双".equals(num)) {
                ball.setBackgroundResource(R.drawable.icon_red_bg);
                ball.setTextColor(Color.WHITE);
            } else {
                ball.setBackgroundResource(R.drawable.kuai3_bg_empty_bai);
                ball.setTextColor(Color.BLACK);
            }
//            }
            ball.setText(num);
        }
    }

    /**
     * 快三
     *
     * @param num
     */
    private static List<ScroolTouZhuModle> setKuai3NumImageScrool(String num, int index) {
        List<ScroolTouZhuModle> data = new LinkedList<ScroolTouZhuModle>();
        if (index < 3) {
            for (int i = 1; i < 6; i++) {
                if ((i + "").equals(num)) continue;
                if ((i + "").equals("1")) {
                    data.add(new ScroolTouZhuModle("", R.drawable.kuai3_bg_one));
                } else if ((i + "").equals("2")) {
                    data.add(new ScroolTouZhuModle("", R.drawable.kuai3_bg_two));
                } else if ((i + "").equals("3")) {
                    data.add(new ScroolTouZhuModle("", R.drawable.kuai3_bg_three));
                } else if ((i + "").equals("4")) {
                    data.add(new ScroolTouZhuModle("", R.drawable.kuai3_bg_four));
                } else if ((i + "").equals("5")) {
                    data.add(new ScroolTouZhuModle("", R.drawable.kuai3_bg_five));
                } else if ((i + "").equals("6")) {
                    data.add(new ScroolTouZhuModle("", R.drawable.kuai3_bg_six));
                }

            }
            Collections.shuffle(data);
            if ((num).equals("1")) {
                ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.kuai3_bg_one));
            } else if (num.equals("2")) {
                ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.kuai3_bg_two));
            } else if (num.equals("3")) {
                ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.kuai3_bg_three));
            } else if (num.equals("4")) {
                ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.kuai3_bg_four));
            } else if (num.equals("5")) {
                ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.kuai3_bg_five));
            } else if (num.equals("6")) {
                ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.kuai3_bg_six));
            }

            return data;
        } else {
            if ((num).equals("大") || (num).equals("双")) {
                ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle(num, R.drawable.icon_red_bg));
            } else if ((num).equals("小") || (num).equals("单")) {
                ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle(num, R.drawable.icon_blue_bg));
            } else {
                ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle(num, R.drawable.kuai3_bg_empty_bai));
            }
            return data;
        }

    }


    /**
     * PC蛋蛋
     *
     * @param num
     * @param ball
     */
    private static void figurePceggImage(String num, TextView ball) {
        int[] images = new int[]{
                R.drawable.ffc_yellow_bg, R.drawable.ffc_red_bg, R.drawable.ffc_green_bg, R.drawable.ffc_blue_bg, R.drawable.ffc_light_blue_bg};
        Random r = new Random();
        int index = Math.abs(r.nextInt(images.length));
        if (index == images.length) {
            index--;
        }
        ball.setBackgroundResource(images[index]);
        ball.setText(num);
    }

    /**
     * PC蛋蛋和加拿大28背景
     *
     * @param num
     * @param ball
     */
    private static void figurePcDDImageOrder(String num, TextView ball) {

        int drawableRes;
        int number;
        if (Utils.isNumeric(num)) {
            number = Integer.parseInt(num);
        } else {
            number = 0;
        }

        switch (number) {
            case 3:
            case 6:
            case 9:
            case 12:
            case 15:
            case 18:
            case 21:
            case 24:
                drawableRes = R.drawable.ffc_red_bg;
                break;
            case 1:
            case 4:
            case 7:
            case 10:
            case 16:
            case 19:
            case 22:
            case 25:
                drawableRes = R.drawable.ffc_green_bg;
                break;
            case 2:
            case 5:
            case 8:
            case 11:
            case 17:
            case 20:
            case 23:
            case 26:
                drawableRes = R.drawable.ffc_blue_bg;
                break;
            default:
                drawableRes = R.drawable.ffc_yellow_bg;
                break;
        }
        ball.setBackgroundResource(drawableRes);
        ball.setText(num);
    }

    /**
     * PC蛋蛋和加拿大28背景
     *
     * @param num
     */
    private static List<ScroolTouZhuModle> figurePcDDImageOrderScrool(String num) {
        final List<ScroolTouZhuModle> data = new LinkedList<ScroolTouZhuModle>();
        for (int i = 0; i < 10; i++) {
            if ((i + "").equals(num)) continue;
            switch (i) {
                case 3:
                case 6:
                case 9:
                case 12:
                case 15:
                case 18:
                case 21:
                case 24:
                    data.add(new ScroolTouZhuModle(i + "", R.drawable.ffc_red_bg));
                    break;
                case 1:
                case 4:
                case 7:
                case 10:
                case 16:
                case 19:
                case 22:
                case 25:
                    data.add(new ScroolTouZhuModle(i + "", R.drawable.ffc_green_bg));
                    break;
                case 2:
                case 5:
                case 8:
                case 11:
                case 17:
                case 20:
                case 23:
                case 26:
                    data.add(new ScroolTouZhuModle(i + "", R.drawable.ffc_blue_bg));
                    break;
                default:
                    data.add(new ScroolTouZhuModle(i + "", R.drawable.ffc_yellow_bg));
                    break;
            }


        }
        Collections.shuffle(data);
        int number;
        if (Utils.isNumeric(num)) {
            number = Integer.parseInt(num);
        } else {
            number = 0;
        }
        switch (number) {
            case 3:
            case 6:
            case 9:
            case 12:
            case 15:
            case 18:
            case 21:
            case 24:
                ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle(num, R.drawable.ffc_red_bg));
                break;
            case 1:
            case 4:
            case 7:
            case 10:
            case 16:
            case 19:
            case 22:
            case 25:
                ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle(num, R.drawable.ffc_green_bg));
                break;
            case 2:
            case 5:
            case 8:
            case 11:
            case 17:
            case 20:
            case 23:
            case 26:
                ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle(num, R.drawable.ffc_blue_bg));
                break;
            default:
                ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle(num, R.drawable.ffc_yellow_bg));
                break;
        }
        return data;

    }


    /**
     * 幸运农场
     *
     * @param num
     * @param ball
     */
    private static void figureXYNCImage(String num, TextView ball) {
        if (num.equals("?")) {
            ball.setBackgroundResource(R.drawable.ffc_red_bg);
            ball.setText(num);
        } else {
            if (num.equals("01")) {
                ball.setBackgroundResource(R.drawable.xync_01);
            } else if (num.equals("02")) {
                ball.setBackgroundResource(R.drawable.xync_02);
            } else if (num.equals("03")) {
                ball.setBackgroundResource(R.drawable.xync_03);
            } else if (num.equals("04")) {
                ball.setBackgroundResource(R.drawable.xync_04);
            } else if (num.equals("05")) {
                ball.setBackgroundResource(R.drawable.xync_05);
            } else if (num.equals("06")) {
                ball.setBackgroundResource(R.drawable.xync_06);
            } else if (num.equals("07")) {
                ball.setBackgroundResource(R.drawable.xync_07);
            } else if (num.equals("08")) {
                ball.setBackgroundResource(R.drawable.xync_08);
            } else if (num.equals("09")) {
                ball.setBackgroundResource(R.drawable.xync_09);
            } else if (num.equals("10")) {
                ball.setBackgroundResource(R.drawable.xync_10);
            } else if (num.equals("11")) {
                ball.setBackgroundResource(R.drawable.xync_11);
            } else if (num.equals("12")) {
                ball.setBackgroundResource(R.drawable.xync_12);
            } else if (num.equals("13")) {
                ball.setBackgroundResource(R.drawable.xync_13);
            } else if (num.equals("14")) {
                ball.setBackgroundResource(R.drawable.xync_14);
            } else if (num.equals("15")) {
                ball.setBackgroundResource(R.drawable.xync_15);
            } else if (num.equals("16")) {
                ball.setBackgroundResource(R.drawable.xync_16);
            } else if (num.equals("17")) {
                ball.setBackgroundResource(R.drawable.xync_17);
            } else if (num.equals("18")) {
                ball.setBackgroundResource(R.drawable.xync_18);
            } else if (num.equals("19")) {
                ball.setBackgroundResource(R.drawable.xync_19);
            } else if (num.equals("20")) {
                ball.setBackgroundResource(R.drawable.xync_20);
            }
            ball.setText("");
        }

    }

    /**
     * 赛车
     *
     * @param num
     * @param ball
     */
    private static void figureSaiCheImage(String num, TextView ball) {
        if (num.equals("?")) {
            ball.setBackgroundResource(R.drawable.ffc_red_bg);
            ball.setText(num);
        } else {
            if (!num.startsWith("0") && num.length() == 1) {
                num = "0" + num;
            }
            ball.setText("");
            if (num.equals("01")) {
                ball.setBackgroundResource(R.drawable.sc_01_title);
            } else if (num.equals("02")) {
                ball.setBackgroundResource(R.drawable.sc_02_title);
            } else if (num.equals("03")) {
                ball.setBackgroundResource(R.drawable.sc_03_title);
            } else if (num.equals("04")) {
                ball.setBackgroundResource(R.drawable.sc_04_title);
            } else if (num.equals("05")) {
                ball.setBackgroundResource(R.drawable.sc_05_title);
            } else if (num.equals("06")) {
                ball.setBackgroundResource(R.drawable.sc_06_title);
            } else if (num.equals("07")) {
                ball.setBackgroundResource(R.drawable.sc_07_title);
            } else if (num.equals("08")) {
                ball.setBackgroundResource(R.drawable.sc_08_title);
            } else if (num.equals("09")) {
                ball.setBackgroundResource(R.drawable.sc_09_title);
            } else if (num.equals("10")) {
                ball.setBackgroundResource(R.drawable.sc_10_title);
            } else {
                ball.setText(num);
                ball.setBackground(null);
            }
        }
    }


    /**
     * 赛车
     *
     * @param num
     */
    private static List<ScroolTouZhuModle> figureSaiCheImageScrool(String num) {
        List<ScroolTouZhuModle> data = new LinkedList<ScroolTouZhuModle>();
        if (num.equals("?")) {
            data.add(new ScroolTouZhuModle(num, R.drawable.ffc_red_bg));
            return data;
        } else {

//            int n = 0;
            for (int i = 0; i < 10; i++) {
                if (("" + i).equals(num)) {
                    continue;
                }
                if (i == 1) {
                    data.add(new ScroolTouZhuModle("", R.drawable.sc_01_title));
                } else if (i == 2) {
                    data.add(new ScroolTouZhuModle("", R.drawable.sc_02_title));
                } else if (i == 3) {
                    data.add(new ScroolTouZhuModle("", R.drawable.sc_03_title));
                } else if (i == 4) {
                    data.add(new ScroolTouZhuModle("", R.drawable.sc_04_title));
                } else if (i == 5) {
                    data.add(new ScroolTouZhuModle("", R.drawable.sc_05_title));
                } else if (i == 6) {
                    data.add(new ScroolTouZhuModle("", R.drawable.sc_06_title));
                } else if (i == 7) {
                    data.add(new ScroolTouZhuModle("", R.drawable.sc_07_title));
                } else if (i == 8) {
                    data.add(new ScroolTouZhuModle("", R.drawable.sc_08_title));
                } else if (i == 9) {
                    data.add(new ScroolTouZhuModle("", R.drawable.sc_09_title));
                } else if (i == 10) {
                    data.add(new ScroolTouZhuModle("", R.drawable.sc_10_title));
                }
            }

        }
        Collections.shuffle(data);
        if (!num.startsWith("0") && num.length() == 1) {
            num = "0" + num;
        }
        if (num.equals("01")) {
            ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.sc_01_title));

        } else if (num.equals("02")) {
            ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.sc_02_title));

        } else if (num.equals("03")) {
            ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.sc_03_title));

        } else if (num.equals("04")) {
            ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.sc_04_title));

        } else if (num.equals("05")) {
            ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.sc_05_title));

        } else if (num.equals("06")) {
            ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.sc_06_title));

        } else if (num.equals("07")) {
            ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.sc_07_title));

        } else if (num.equals("08")) {
            ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.sc_08_title));

        } else if (num.equals("09")) {
            ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.sc_09_title));

        } else if (num.equals("10")) {
            ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle("", R.drawable.sc_10_title));

        } else {
            ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle(num, R.color.background));
            return data;
        }
        return data;
    }

    private static void figureLhcImage(String num, TextView ball, Context context) {
        String[] redBO = new String[]{"01", "02", "07", "08", "12", "13", "18", "19", "23", "24", "29", "30", "34", "35", "40", "45", "46"};
        String[] blueBO = new String[]{"03", "04", "09", "10", "14", "15", "20", "25", "26", "31", "36", "37", "41", "42", "47", "48"};
        String[] greenBO = new String[]{"05", "06", "11", "16", "17", "21", "22", "27", "28", "32", "33", "38", "39", "43", "44", "49"};

        if (!num.startsWith("0") && num.length() == 1) {
            num = "0" + num;
        }
        if (Arrays.asList(redBO).contains(num)) {
            ball.setBackgroundResource(R.drawable.lhc_red_bg);
        } else if (Arrays.asList(blueBO).contains(num)) {
            ball.setBackgroundResource(R.drawable.lhc_blue_bg);
        } else if (Arrays.asList(greenBO).contains(num)) {
            ball.setBackgroundResource(R.drawable.lhc_green_bg);
        } else {
            ball.setBackgroundResource(R.drawable.lhc_blue_bg);
        }
        ball.setText(num);
    }

    private static List<ScroolTouZhuModle> figureLhcImageScroll(String num) {
        String[] redBO = new String[]{"01", "02", "07", "08", "12", "13", "18", "19", "23", "24", "29", "30", "34", "35", "40", "45", "46"};
        String[] blueBO = new String[]{"03", "04", "09", "10", "14", "15", "20", "25", "26", "31", "36", "37", "41", "42", "47", "48"};
        String[] greenBO = new String[]{"05", "06", "11", "16", "17", "21", "22", "27", "28", "32", "33", "38", "39", "43", "44", "49"};
        LinkedList<ScroolTouZhuModle> data = new LinkedList<ScroolTouZhuModle>();
        for (int i = 1; i < 10; i++) {
            if ((i + "").equals(num)) continue;
            if (Arrays.asList(redBO).contains(i + "")) {
                data.add(new ScroolTouZhuModle(i + "", R.drawable.lhc_red_bg));
            } else if (Arrays.asList(blueBO).contains(i + "")) {
                data.add(new ScroolTouZhuModle(i + "", R.drawable.lhc_blue_bg));
            } else if (Arrays.asList(greenBO).contains(i + "")) {
                data.add(new ScroolTouZhuModle(i + "", R.drawable.lhc_green_bg));
            } else {
                data.add(new ScroolTouZhuModle(i + "", R.drawable.lhc_blue_bg));
            }

        }
        Collections.shuffle(data);

        if (Arrays.asList(redBO).contains(num)) {
            data.addFirst(new ScroolTouZhuModle(num, R.drawable.lhc_red_bg));
        } else if (Arrays.asList(blueBO).contains(num)) {
            data.addFirst(new ScroolTouZhuModle(num, R.drawable.lhc_blue_bg));
        } else if (Arrays.asList(greenBO).contains(num)) {
            data.addFirst(new ScroolTouZhuModle(num, R.drawable.lhc_green_bg));
        } else {
            data.addFirst(new ScroolTouZhuModle(num, R.drawable.lhc_blue_bg));
        }

        return data;
    }


    public static void figureImgeByCpCode(String cpCode, String num, String cpVersion, TextView ball, int position, Context context) {

        if (cpVersion.equals(String.valueOf(Constant.lottery_identify_V2)) ||
                cpVersion.equals(String.valueOf(Constant.lottery_identify_V4)) ||
                cpVersion.equals(String.valueOf(Constant.lottery_identify_V5))) {
            if (cpCode.equals("9")) {//时时彩
                ball.setBackgroundResource(R.drawable.ffc_yellow_bg);
                ball.setText(num);
            } else if (cpCode.equals("10")) {//快三
                setKuai3NumImage(num, ball, position, context);
            } else if (cpCode.equals("11")) {//pc蛋蛋，加拿大28
//                figurePceggImage(num, ball);
                figurePcDDImageOrder(num, ball);
            } else if (cpCode.equals("12")) {////重庆幸运农场,湖南快乐十分,广东快乐十分
//                figureXYNCImage(num, bsall);
                ball.setBackgroundResource(R.drawable.ffc_blue_bg);
                ball.setText(num);
            } else if (cpCode.equals("8")) {//极速赛车，北京赛车，幸运飞艇
                figureSaiCheImage(num, ball);
            } else if (cpCode.equals("14")) {//11选5
                ball.setBackgroundResource(R.drawable.ffc_yellow_bg);
                ball.setText(num);
            } else if (cpCode.equals("15")) {//福彩3D，排列三
                ball.setBackgroundResource(R.drawable.ffc_red_bg);
                ball.setText(num);
            } else if (cpCode.equals("6") || cpCode.equals("66")) {//六合彩，十分六合彩
                figureLhcImage(num, ball, context);
            }
        } else if (cpVersion.equals(String.valueOf(Constant.lottery_identify_V1))) {
            if (cpCode.equals("1") || cpCode.equals("2")) {//时时彩
                ball.setBackgroundResource(R.drawable.ffc_yellow_bg);
                ball.setText(num);
            } else if (cpCode.equals("100")) {//快三
                setKuai3NumImage(num, ball, position, context);
            } else if (cpCode.equals("7")) {//pc蛋蛋，加拿大28
//                figurePceggImage(num, ball);
                figurePcDDImageOrder(num, ball);
            } else if (cpCode.equals("3")) {//极速赛车，北京赛车，幸运飞艇
                figureSaiCheImage(num, ball);
            } else if (cpCode.equals("5")) {//11选5
                ball.setBackgroundResource(R.drawable.ffc_yellow_bg);
                ball.setText(num);
            } else if (cpCode.equals("4")) {//福彩3D，排列三
                ball.setBackgroundResource(R.drawable.ffc_red_bg);
                ball.setText(num);
            } else if (cpCode.equals("6") || cpCode.equals("66")) {//六合彩，十分六合彩
                figureLhcImage(num, ball, context);
            }
        } else if (cpVersion.equals(String.valueOf(Constant.lottery_identify_V3))) {
            if (cpCode.equals("51") || cpCode.equals("52")) {//时时彩
                ball.setBackgroundResource(R.drawable.ffc_yellow_bg);
                ball.setText(num);
            } else if (cpCode.equals("58")) {//快三
                setKuai3NumImage(num, ball, position, context);
            } else if (cpCode.equals("57")) {//pc蛋蛋，加拿大28
//                figurePceggImage(num, ball);
                figurePcDDImageOrder(num, ball);
            } else if (cpCode.equals("53")) {//极速赛车，北京赛车，幸运飞艇
                figureSaiCheImage(num, ball);
            } else if (cpCode.equals("55")) {//11选5
                ball.setBackgroundResource(R.drawable.ffc_yellow_bg);
                ball.setText(num);
            } else if (cpCode.equals("54")) {//福彩3D，排列三
                ball.setBackgroundResource(R.drawable.ffc_red_bg);
                ball.setText(num);
            } else if (cpCode.equals("6") || cpCode.equals("66")) {//六合彩，十分六合彩
                figureLhcImage(num, ball, context);
            }
        }

    }

    public static List<ScroolTouZhuModle> figureImgeByCpCodescrool(String cpCode, String num, String cpVersion, int position) {

        if (cpVersion.equals(String.valueOf(Constant.lottery_identify_V2)) ||
                cpVersion.equals(String.valueOf(Constant.lottery_identify_V4)) ||
                cpVersion.equals(String.valueOf(Constant.lottery_identify_V5))) {
            if (cpCode.equals("9")) {//时时彩
                return figureShishicaiImageScrool(num);
            } else if (cpCode.equals("10")) {//快三
                return setKuai3NumImageScrool(num, position);
            } else if (cpCode.equals("11")) {//pc蛋蛋，加拿大28
                return figurePcDDImageOrderScrool(num);
            } else if (cpCode.equals("12")) {////重庆幸运农场,湖南快乐十分,广东快乐十分
                return figurekuaileshifenImageScrool(num);
            } else if (cpCode.equals("8")) {//极速赛车，北京赛车，幸运飞艇
                return figureSaiCheImageScrool(num);
            } else if (cpCode.equals("14")) {//11选5
                return figure11xuan5mageScrool(num);
            } else if (cpCode.equals("15")) {//福彩3D，排列三
                return figurefucai3dImageScrool(num);
            } else if (cpCode.equals("6") || cpCode.equals("66")) {//六合彩，十分六合彩
                return figureLhcImageScroll(num);
            }
        } else if (cpVersion.equals(String.valueOf(Constant.lottery_identify_V1))) {
            if (cpCode.equals("1") || cpCode.equals("2")) {//时时彩
                return figureShishicaiImageScrool(num);
            } else if (cpCode.equals("100")) {//快三
                return setKuai3NumImageScrool(num, position);
            } else if (cpCode.equals("7")) {//pc蛋蛋，加拿大28
//                figurePceggImage(num, ball);
                return figurePcDDImageOrderScrool(num);
            } else if (cpCode.equals("3")) {//极速赛车，北京赛车，幸运飞艇
                return figureSaiCheImageScrool(num);
            } else if (cpCode.equals("5")) {//11选5
                return figure11xuan5mageScrool(num);
            } else if (cpCode.equals("4")) {//福彩3D，排列三
                return figurefucai3dImageScrool(num);
            } else if (cpCode.equals("6") || cpCode.equals("66")) {//六合彩，十分六合彩
                return figureLhcImageScroll(num);
            }
        } else if (cpVersion.equals(String.valueOf(Constant.lottery_identify_V3))) {
            if (cpCode.equals("51") || cpCode.equals("52")) {//时时彩
                return figureShishicaiImageScrool(num);
            } else if (cpCode.equals("58")) {//快三
                return setKuai3NumImageScrool(num, position);
            } else if (cpCode.equals("57")) {//pc蛋蛋，加拿大28
//                figurePceggImage(num, ball);
                return figurePcDDImageOrderScrool(num);
            } else if (cpCode.equals("53")) {//极速赛车，北京赛车，幸运飞艇
                return figureSaiCheImageScrool(num);
            } else if (cpCode.equals("55")) {//11选5
                return figure11xuan5mageScrool(num);
            } else if (cpCode.equals("54")) {//福彩3D，排列三
                return figurefucai3dImageScrool(num);

            } else if (cpCode.equals("6") || cpCode.equals("66")) {//六合彩，十分六合彩
                return figureLhcImageScroll(num);
            }
        }
        return null;
    }

    private static List<ScroolTouZhuModle> figureShishicaiImageScrool(String num) {
        List<ScroolTouZhuModle> data = new LinkedList<ScroolTouZhuModle>();
        for (int i = 1; i < 10; i++) {
            if ((i + "").equals(num)) continue;
            data.add(new ScroolTouZhuModle(i + "", R.drawable.ffc_yellow_bg));
        }
        Collections.shuffle(data);
        ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle(num, R.drawable.ffc_yellow_bg));
        return data;
    }

    private static List<ScroolTouZhuModle> figure11xuan5mageScrool(String num) {
        List<ScroolTouZhuModle> data = new LinkedList<ScroolTouZhuModle>();
        for (int i = 1; i < 10; i++) {
            if ((i + "").equals(num)) continue;
            data.add(new ScroolTouZhuModle(i + "", R.drawable.ffc_yellow_bg));
        }
        Collections.shuffle(data);
        ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle(num, R.drawable.ffc_yellow_bg));
        return data;
    }

    private static List<ScroolTouZhuModle> figurekuaileshifenImageScrool(String num) {
        List<ScroolTouZhuModle> data = new LinkedList<ScroolTouZhuModle>();
        for (int i = 1; i < 10; i++) {
            if ((i + "").equals(num)) continue;
            data.add(new ScroolTouZhuModle(i + "", R.drawable.ffc_blue_bg));
        }
        Collections.shuffle(data);
        ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle(num, R.drawable.ffc_blue_bg));
        return data;
    }

    private static List<ScroolTouZhuModle> figurefucai3dImageScrool(String num) {
        final List<ScroolTouZhuModle> data = new LinkedList<ScroolTouZhuModle>();
        for (int i = 1; i < 8; i++) {
            if ((i + "").equals(num)) continue;
            data.add(new ScroolTouZhuModle(i + "", R.drawable.ffc_red_bg));
        }
        Collections.shuffle(data);
        ((LinkedList<ScroolTouZhuModle>) data).addFirst(new ScroolTouZhuModle(num, R.drawable.ffc_red_bg));
        return data;
    }

    /**
     * 返回彩种显示的球的个数
     *
     * @param cpCode
     * @param cpVersion
     * @return
     */
    public static int ballCount(String cpCode, String cpVersion) {

        if (cpVersion.equals(String.valueOf(Constant.lottery_identify_V2)) ||
                cpVersion.equals(String.valueOf(Constant.lottery_identify_V4)) ||
                cpVersion.equals(String.valueOf(Constant.lottery_identify_V5))) {
            if (cpCode.equals("9")) {//时时彩
                return 5;
            } else if (cpCode.equals("10")) {//快三
                return 6;
            } else if (cpCode.equals("11")) {//pc蛋蛋，加拿大28
                return 7;
            } else if (cpCode.equals("12")) {////重庆幸运农场,湖南快乐十分,广东快乐十分
                return 8;
            } else if (cpCode.equals("8")) {//极速赛车，北京赛车，幸运飞艇
                return 10;
            } else if (cpCode.equals("14")) {//11选5
                return 5;
            } else if (cpCode.equals("15")) {//福彩3D，排列三
                return 3;
            } else if (cpCode.equals("6") || cpCode.equals("66")) {//六合彩，十分六合彩
                return 7;
            }
        } else if (cpVersion.equals(String.valueOf(Constant.lottery_identify_V1))) {
            if (cpCode.equals("1") || cpCode.equals("2")) {//时时彩
                return 5;
            } else if (cpCode.equals("100")) {//快三
                return 6;
            } else if (cpCode.equals("7")) {//pc蛋蛋，加拿大28
                return 7;
            } else if (cpCode.equals("3")) {//极速赛车，北京赛车，幸运飞艇
                return 10;
            } else if (cpCode.equals("5")) {//11选5
                return 5;
            } else if (cpCode.equals("4")) {//福彩3D，排列三
                return 3;
            } else if (cpCode.equals("6") || cpCode.equals("66")) {//六合彩，十分六合彩
                return 7;
            }
        } else if (cpVersion.equals(String.valueOf(Constant.lottery_identify_V3))) {
            if (cpCode.equals("51") || cpCode.equals("52")) {//时时彩
                return 5;
            } else if (cpCode.equals("58")) {//快三
                return 6;
            } else if (cpCode.equals("57")) {//pc蛋蛋，加拿大28
                return 7;
            } else if (cpCode.equals("53")) {//极速赛车，北京赛车，幸运飞艇
                return 10;
            } else if (cpCode.equals("55")) {//11选5
                return 5;
            } else if (cpCode.equals("54")) {//福彩3D，排列三
                return 3;
            } else if (cpCode.equals("6") || cpCode.equals("66")) {//六合彩，十分六合彩
                return 7;
            }
        }
        return 0;
    }

    public static long dateToStamp(String s, String formate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formate);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();

        return ts;
    }

    /**
     * 获取用户信息
     *
     * @param ctx
     * @param showDialog
     * @param tips
     * @param tostMsg          提示错误信息
     * @param onResultListener
     */
    public static void getUserInfo(final Context ctx, boolean showDialog, String tips, final boolean tostMsg, final OnResultListener<Meminfo> onResultListener) {
        HttpUtil.get(ctx, Urls.MEMINFO_URL, null, showDialog, tips, 0, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    YiboPreference.instance(ctx).setToken(result.getAccessToken());
                    Meminfo meminfo = new Gson().fromJson(result.getContent(), Meminfo.class);
                    if (onResultListener != null)
                        onResultListener.onResultListener(meminfo);

                } else {
                    if (tostMsg)
                        ;
                }
            }
        });
    }

//    public static void LoadUserImage(Context ctx, ImageView view) {
//        String url = YiboPreference.instance(ctx).getUserHeader();
//        if (Utils.isEmptyString(url))
//            url = getConfigFromJson(ctx).getMember_center_logo_url();
//        if (!Utils.isEmptyString(url)) {
//            GlideUrl glideUrl = new GlideUrl(url.trim(), new LazyHeaders.Builder()
//                    .addHeader("Cookie", "SESSION=" + YiboPreference.instance(ctx).getToken())
//                    .build());
//            Glide.with(ctx.getApplicationContext()).load(glideUrl)
//                    .placeholder(R.drawable.member_headers)
//                    .error(R.drawable.member_headers)
//                    .fitCenter()
//                    .transform(new GlideCircleTransform(ctx))
//                    .into(view);
//        } else {
//            Glide.with(ctx).load(R.drawable.member_headers).asBitmap().into(view);
//            view.setBackground(ctx.getResources().getDrawable(R.drawable.member_headers));
//        }
//
//    }

    public static void LoadUserImage(Context ctx, SimpleDraweeView view) {
        String url = YiboPreference.instance(ctx).getUserHeader();
        SysConfig configFromJson = getConfigFromJson(ctx);
        if (Utils.isEmptyString(url) && !Utils.isEmptyString(configFromJson.getMember_center_logo_url()))
            url = configFromJson.getMember_center_logo_url();
        if (!Utils.isEmptyString(url)) {
            view.setImageURI(Uri.parse(url.trim()));
        } else {
            view.setImageURI(Uri.parse("res:///" + R.drawable.member_headers));
        }
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    private static void showListDialog(final Context context, final String updateWebsite, final UpdateDialogEvent event, final boolean forceUpdate) {
        String jump = "V1";
        if (getConfigFromJson(context) != null) {
            jump = TextUtils.isEmpty(getConfigFromJson(context).getGoto_downpage_when_update_version()) ? "V1" :
                    getConfigFromJson(context).getGoto_downpage_when_update_version();
        }
        //V1浏览器
        boolean isJump = jump.equalsIgnoreCase("V1");
        if (isJump) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateWebsite));
                context.startActivity(intent);

                if (forceUpdate) {
                    System.exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isSpaceMainPopup() { //判断是否显示首页弹窗
        String arr = YiboPreference.instance(YiboApplication.getInstance()).getMainPopupTime();
        if (Utils.isEmptyString(arr)) {
            MainPopupTime.MainPopupTimeDetail detail = new MainPopupTime.MainPopupTimeDetail(YiboPreference.instance(YiboApplication.getInstance()).getUsername(),
                    5000, DateUtils.getSysTime());
            UsualMethod.setMainPopupTime(detail);
            arr = YiboPreference.instance(YiboApplication.getInstance()).getMainPopupTime();
            return true;
        }
        boolean isOk = false;
        MainPopupTime list = new Gson().fromJson(arr, MainPopupTime.class);
        String userName = "";
        for (int i = 0; i < list.getData().size(); i++) {
            if (userName.equals(list.getData().get(i).getUserName())) {
                if (list.getData().get(i).getSpaceTime() > 0) {
                    long nowTime = DateUtils.getSysTime();
                    if (nowTime - list.getData().get(i).getLastTime() > list.getData().get(i).getSpaceTime()) {
                        isOk = true;
                        list.getData().get(i).setLastTime(DateUtils.getSysTime());
                        UsualMethod.setMainPopupTime(list.getData().get(i));
                        break;
                    }
                } else {
                    return false;
                }

            }
        }
        return isOk;
    }

    public static void setMainPopupTime(MainPopupTime.MainPopupTimeDetail detail) { //设置首页弹窗时间
        String arr = YiboPreference.instance(YiboApplication.getInstance()).getMainPopupTime();
        if (Utils.isEmptyString(arr)) {
            MainPopupTime list = new MainPopupTime();
            List<MainPopupTime.MainPopupTimeDetail> temp = new ArrayList<>();
            temp.add(detail);
            list.setData(temp);
            YiboPreference.instance(YiboApplication.getInstance()).setMainPopupTime(new Gson().toJson(list));
        } else {
            MainPopupTime list = new Gson().fromJson(arr, MainPopupTime.class);
            String userName = "";
            boolean isFind = false;
            for (int i = 0; i < list.getData().size(); i++) {
                if (userName.equals(list.getData().get(i).getUserName())) {
                    isFind = true;
                    list.getData().get(i).setLastTime(detail.getLastTime());
                    list.getData().get(i).setSpaceTime(detail.getSpaceTime());
                    break;
                }
            }
            if (!isFind) {
                list.getData().add(detail);
            }
            YiboPreference.instance(YiboApplication.getInstance()).setMainPopupTime(new Gson().toJson(list));
        }
    }

    public static void updateMainPopupTime() { //弹窗之后更新上次弹窗时间数据
        String arr = YiboPreference.instance(YiboApplication.getInstance()).getMainPopupTime();
        MainPopupTime list = new Gson().fromJson(arr, MainPopupTime.class);
        String userName = YiboPreference.instance(YiboApplication.getInstance()).getUsername();
        for (int i = 0; i < list.getData().size(); i++) {
            if (userName.equals(list.getData().get(i).getUserName())) {
                list.getData().get(i).setLastTime(DateUtils.getSysTime());
                break;
            }
        }
        YiboPreference.instance(YiboApplication.getInstance()).setMainPopupTime(new Gson().toJson(list));
    }


    public static long getMainPopupSpaceTime() { //获取当前账号的首页弹窗间隔时间
        long spaceTime = 0;
        String arr = YiboPreference.instance(YiboApplication.getInstance()).getMainPopupTime();
        if (Utils.isEmptyString(arr)) {
            return 0;
        }
        MainPopupTime list = new Gson().fromJson(arr, MainPopupTime.class);
        String userName = "";
        for (int i = 0; i < list.getData().size(); i++) {
            if (userName.equals(list.getData().get(i).getUserName())) {
                spaceTime = list.getData().get(i).getSpaceTime();
                break;
            }
        }
        return spaceTime;
    }

    public static String showMainPopupSpaceTime() { //获取当前账号的首页弹窗间隔时间
        long spaceTime = getMainPopupSpaceTime();
        if (spaceTime == 60000) {
            return "1分钟";
        } else if (spaceTime == 300000) {
            return "5分钟";
        } else if (spaceTime == 600000) {
            return "10分钟";
        } else if (spaceTime == 1800000) {
            return "30分钟";
        } else if (spaceTime == 3600000) {
            return "1小时";
        } else {
            return "默认";
        }
    }


    /**
     * 开始获取当前期号离结束投注倒计时
     *
     * @param bianHao 彩种编号
     */
    public static void getCountDownByCpcode(Context context, String bianHao, int requestCode, SessionResponse.Listener<CrazyResult<Object>> listener) {
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOTTERY_COUNTDOWNV2_URL);
        configUrl.append("?lotCode=").append(bianHao);
        Log.e("whw", configUrl.toString());
        CrazyRequest<CrazyResult<LocCountDownWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(requestCode)
                .headers(Urls.getHeader(context))
                .shouldCache(false)
                .listener(listener)
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LocCountDownWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
    }

    public static boolean isMulSelectMode(String playCode) {
        if (playCode.equals(PlayCodeConstants.zuxuan_san_peilv) ||
                playCode.equals(PlayCodeConstants.zuxuan_liu_peilv) ||
                playCode.equals(PlayCodeConstants.lianma_peilv_klsf) ||
                playCode.equals(PlayCodeConstants.lianma) ||
                playCode.equals(PlayCodeConstants.hexiao) ||
                playCode.equals(PlayCodeConstants.quanbuzhong) ||
                playCode.equals(PlayCodeConstants.weishulian) ||
                playCode.equals(PlayCodeConstants.syx5_renxuan) ||
                playCode.equals(PlayCodeConstants.lianxiao) ||
                playCode.equals(PlayCodeConstants.syx5_zuxuan) ||
                playCode.equals(PlayCodeConstants.syx5_zhixuan)
        ) {
            return true;
        }
        return false;
    }

    /**
     * 根据彩种编号加载图片
     *
     * @param context
     * @param lotImageView
     * @param lotCode
     * @param lotIcon
     */
    public static void updateLocImage(Context context, ImageView lotImageView, String lotCode, String lotIcon) {
        String url;
        if (TextUtils.isEmpty(lotIcon)) {
            url = getLocImageUrl(lotCode);
        } else
            url = lotIcon;

//彩种的图地址是根据彩种编码号为姓名构成的
        LoadImageUtil.loadPicImage(context, lotImageView, url, R.drawable.default_lottery);
    }

    //保存bitmap到手机指定目录
    public static boolean saveBitmapToSD(String imgName, Bitmap bt) {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + imgName;
        File file = new File(filePath);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bt.compress(Bitmap.CompressFormat.JPEG, 90, out);
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;


    }

    public static void syncCookie(Context ctx, String url) {
        CookieSyncManager.createInstance(ctx);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        YiboPreference.instance(ctx).getToken();
        cookieManager.setCookie(url, "SESSION=" + YiboPreference.instance(ctx).getToken());//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }

    public static GlideUrl getGlide(Context context, String url) {
        if (Utils.isEmptyString(url)) {
            return null;
        }
        LazyHeaders.Builder builder = new LazyHeaders.Builder()
                .addHeader("Cookie", "SESSION=" + YiboPreference.instance(context).getToken())
                .addHeader("User-Agent", "android/" + Utils.getVersionName(context) + "|" + YiboPreference.instance(context).getDeviceId())
                .addHeader("cc-token", YiboPreference.instance(context).getVerifyToken())
                .addHeader("Native-Flag", "1");
        if (!Utils.isEmptyString(Urls.BASE_HOST_URL) && url.contains(Urls.BASE_URL)) {//本域名的图片下载，如果有HOST，则需要带上host
            builder.addHeader("Host", Urls.BASE_HOST_URL);
        }
        GlideUrl gu = new GlideUrl(url.trim(), builder.build());
        return gu;
    }

    /**
     * 去除url指定参数
     *
     * @param url
     * @param name
     * @return
     */
    public static String removeParam(String url, String... name) {
        for (String s : name) {
            // 使用replaceAll正则替换,replace不支持正则
            url = url.replaceAll("&?" + s + "=[^&]*", "");
        }
        //刪除尾巴問號
        return url.endsWith("?") ? url.replace("?", "") : url;
    }

    public static void showVerifyActivity(Context context){
        context.startActivity(new Intent(context, VerifyActivity.class));
    }

    public static String getReleaseAndSdkVersion(){
        return "android_"+PhoneInfo.getReleaseVersion()+"_sdk"+PhoneInfo.getSdkVersion();
    }
}
