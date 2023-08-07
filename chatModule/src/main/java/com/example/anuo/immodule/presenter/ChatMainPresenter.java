package com.example.anuo.immodule.presenter;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.ChatMainActivity;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;
import com.example.anuo.immodule.bean.AuthorityBean;
import com.example.anuo.immodule.bean.BaseChatReceiveMsg;
import com.example.anuo.immodule.bean.ChatCollectionImagesBean;
import com.example.anuo.immodule.bean.ChatHistoryMessageBean;
import com.example.anuo.immodule.bean.ChatLongDragonBean;
import com.example.anuo.immodule.bean.ChatLotteryBean;
import com.example.anuo.immodule.bean.ChatMessage;
import com.example.anuo.immodule.bean.ChatModifyPersonDataBean;
import com.example.anuo.immodule.bean.ChatMuteAllBean;
import com.example.anuo.immodule.bean.ChatMuteBean;
import com.example.anuo.immodule.bean.ChatPersonDataBean;
import com.example.anuo.immodule.bean.ChatPersonPhotoListBean;
import com.example.anuo.immodule.bean.ChatPrivateConversationBean;
import com.example.anuo.immodule.bean.ChatPrivateMessageBean;
import com.example.anuo.immodule.bean.ChatPrivateReceiveMsgBean;
import com.example.anuo.immodule.bean.ChatQuickMessageBean;
import com.example.anuo.immodule.bean.ChatRecallBean;
import com.example.anuo.immodule.bean.ChatRemarkBean;
import com.example.anuo.immodule.bean.ChatRoomListBean;
import com.example.anuo.immodule.bean.ChatRoomNoticeBean;
import com.example.anuo.immodule.bean.ChatSendMsg;
import com.example.anuo.immodule.bean.ChatShareDataBean;
import com.example.anuo.immodule.bean.ChatSignDataBean;
import com.example.anuo.immodule.bean.ChatSysConfigBean;
import com.example.anuo.immodule.bean.ChatToolPermissionBean;
import com.example.anuo.immodule.bean.ChatUserListBean;
import com.example.anuo.immodule.bean.ChatViolateWordsBean;
import com.example.anuo.immodule.bean.GetAuditListBean;
import com.example.anuo.immodule.bean.GrabRedPackageBean;
import com.example.anuo.immodule.bean.ImageMsgBody;
import com.example.anuo.immodule.bean.JoinChatRoomBean;
import com.example.anuo.immodule.bean.LoginChatBean;
import com.example.anuo.immodule.bean.LotteryDownBean;
import com.example.anuo.immodule.bean.MsgType;
import com.example.anuo.immodule.bean.RedPackageDetailBean;
import com.example.anuo.immodule.bean.RedPackageMsgBody;
import com.example.anuo.immodule.bean.SendMsgBean;
import com.example.anuo.immodule.bean.TextMsgBody;
import com.example.anuo.immodule.bean.TodayProfitResponse;
import com.example.anuo.immodule.bean.UpLoadFileBean;
import com.example.anuo.immodule.bean.base.FileMsgBody;
import com.example.anuo.immodule.bean.base.MsgBody;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.constant.UserToolConstant;
import com.example.anuo.immodule.interfaces.iview.IIChatMainView;
import com.example.anuo.immodule.interfaces.iview.base.IChatBaseView;
import com.example.anuo.immodule.jsonmodel.AllLotteryInfoResponse;
import com.example.anuo.immodule.jsonmodel.ApplyBanSpeakModel;
import com.example.anuo.immodule.jsonmodel.AuditListModel;
import com.example.anuo.immodule.jsonmodel.AuditRequestModel;
import com.example.anuo.immodule.jsonmodel.ChangeRoomModel;
import com.example.anuo.immodule.jsonmodel.ChatBaseModel;
import com.example.anuo.immodule.jsonmodel.ChatCollectionImagesModel;
import com.example.anuo.immodule.jsonmodel.ChatJoinPrivateRoomMsg;
import com.example.anuo.immodule.jsonmodel.ChatLongDragonJsonModel;
import com.example.anuo.immodule.jsonmodel.ChatMessageHistoryModel;
import com.example.anuo.immodule.jsonmodel.ChatMuteAllJsonModel;
import com.example.anuo.immodule.jsonmodel.ChatMuteJsonModel;
import com.example.anuo.immodule.jsonmodel.ChatPrivateHistoryJsonModel;
import com.example.anuo.immodule.jsonmodel.ChatPrivateJsonModel;
import com.example.anuo.immodule.jsonmodel.ChatQuickMessageJsonModel;
import com.example.anuo.immodule.jsonmodel.ChatRecallJsonModel;
import com.example.anuo.immodule.jsonmodel.ChatRoomListJsonModel;
import com.example.anuo.immodule.jsonmodel.ChatRoomNoticeModel;
import com.example.anuo.immodule.jsonmodel.ChatSysConfigModel;
import com.example.anuo.immodule.jsonmodel.ChatToolPermissionJsonModel;
import com.example.anuo.immodule.jsonmodel.ChatViolateWordsModel;
import com.example.anuo.immodule.jsonmodel.FollowBetJsonModel;
import com.example.anuo.immodule.jsonmodel.GetOnlineJsonModel;
import com.example.anuo.immodule.jsonmodel.GrabRedPackageJsonModel;
import com.example.anuo.immodule.jsonmodel.JoinChatRoomJsonModel;
import com.example.anuo.immodule.jsonmodel.JoinPrivateRoomJsonModel;
import com.example.anuo.immodule.jsonmodel.LoginJsonModel;
import com.example.anuo.immodule.jsonmodel.LotteryHistoryResultResponse;
import com.example.anuo.immodule.jsonmodel.LotteryResultModel;
import com.example.anuo.immodule.jsonmodel.MasterPlanModel;
import com.example.anuo.immodule.jsonmodel.PersonDataModel;
import com.example.anuo.immodule.jsonmodel.PhotoListModel;
import com.example.anuo.immodule.jsonmodel.RedPackageDetailJsonModel;
import com.example.anuo.immodule.manager.SocketManager;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.AESUtils;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.ChatSysConfig;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.utils.LogUtils;
import com.example.anuo.immodule.utils.ToastUtils;
import com.example.anuo.immodule.utils.YiboPreferenceUtils;
import com.example.anuo.immodule.view.LogingDialog;
import com.example.anuo.immodule.view.PayPsdInputView;
import com.example.anuo.immodule.view.PsdDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.simon.widget.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.network.FileHandler;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;
import io.socket.client.Ack;
import io.socket.emitter.Emitter;

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
 * Date  :20/06/2019
 * Desc  :com.example.anuo.immodule.presenter
 */
public class ChatMainPresenter extends ChatBasePresenter implements SessionResponse.Listener<CrazyResult<Object>> {
    private static int NOTIFY_ID = 100;
    private final ChatMainActivity activity;
    private final IIChatMainView iView;
    private final ChatSysConfig chatSysConfig;
    public final SocketManager socketManager;
    private String permissionInfo;
    public static final String mSenderId = "right";
    public static final String mTargetId = "left";
    public static final int CHAT_ROOM_LIST_REQUEST = 0x10;
    public static final int JOIN_CHAT_ROOM_REQUEST = 0x11;
    public static final int JOIN_CHAT_ROOM_SUCCESS = 0x26;
    public static final int JOIN_PRIVATE_CHAT_ROOM_SUCCESS = 0x260;

    private final int SEND_TEXT_MSG_REQUEST = 0x12;
    private final int UPLOAD_FILE_IMAGE = 0x13;
    private final int UPLOAD_FILE_AUDIO = 0x27;
    private final int GRAB_RED_PACKAGE = 0x14;
    private final int RED_PACKAGE_DETAIL = 0x15;
    private final int GET_LOTTERY_LIST = 0x16;
    private final int GET_NOTICE_REQUEST = 0x17;
    private final int GET_LOTTERY_RESULT = 0x18;
    private final int GET_HISTORY_MESSAGE = 0x19;
    private final int FOLLOW_BET = 0x20;
    private final int GET_VIOLATE_WORDS = 0x21;

    private final int AUTHORIZATION_REQUEST = 0x23;
    private final int LOGIN_CHATROOM_REQUEST = 0X24;
    private final int GET_SYS_CONFIG = 0X25;
    private final int GET_PERSON_DATA = 0x28; //获取用户资料
    private final int MODIFY_PERSON_DATA = 0x29; //修改用户资料

    private final int GET_ONLINE_USER = 0x30;//获取在线用户
    private final int GET_ONLINE_MANAGER = 0x31;//获取在线管理
    private final int SHARE_DATA = 0x32;//分享今日盈亏
    private final int GET_CHARGE = 0x33;//获取用户余额
    private final int GET_QUICK_MESSAGES = 0x34;//获取快捷消息
    private final int SAVE_IMAGES = 0x35;//收藏图片
    private final int SIGN = 0x36;//签到
    private final int MASTER_PLAN = 0x37;//导师计划
    private final int GET_PHOTO_LIST = 0x38;//头像列表
    private final int GET_PRIVATE_CONVERSATION = 0x39;//初始化私人列表
    private final int BAN_SPEAK_USER = 0x40;//单独禁言
    private final int BAN_SPEAK_ROOM = 0x41;//全体禁言
    private final int RETRACT_MSG = 0x42;//撤回
    private final int GET_TOOL_PERMISSION = 0x43;//工具箱
    private final int GET_PRIVATE_GROUP_HISTORY = 0x44;
    private final int GET_PRIVATE_SEND_MSG = 0x45;
    private final int APPLY_FOR_BAN_SPEAK = 0x46;//申请解除禁言
    private final int GET_LONG_DRAGON = 0x47;//长龙

    private final int GET_ALL_LOTTERY_INFO = 0x48;//取得所有彩票的基本资讯
    private final int GET_LOTTERY_HISTORY_RESULT = 0x49;//取得彩种的历史开奖纪录
    private final int GET_AUDIT_LIST = 0x50;
    private final int POST_CHAT_REMARK = 0x51;
    private final int GET_TODAY_PROFIT = 0x52;


    private ArrayList<String> imageList = new ArrayList<>();
    private HashMap<Integer, Integer> imagePosition = new HashMap<Integer, Integer>();
    private PsdDialog psdDialog;
    public static final String SECURITY_KEY = "fad.23IIkfd24￥@";
    private static String msgUUID = "";
    private SoundPool soundPool;

    private LogingDialog logingDialog;

    private LoginChatBean loginChatBean;
    private AuthorityBean authorityBean;
    private static String oldRoomId = "";

    //与开奖历史纪录相关的2项变数
    private String cpCodeOfClickedKaiJianResult;
    private List<AllLotteryInfoResponse.LotteryInfo> lotteryInfos;
    private String currentManagerID = "";
    private JSONObject chatRemarkObj;


    public void setOldRoomId(String oldRoomId) {
        this.oldRoomId = oldRoomId;
    }

    public LoginChatBean getLoginChatBean() { return loginChatBean; }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            switch (msg.what) {
                case LOGIN_CHATROOM_REQUEST:
                    loginChatBean = new Gson().fromJson(json, LoginChatBean.class);
                    if (!loginChatBean.isSuccess()) {
                        dialogDis();
                        showToast(TextUtils.isEmpty(loginChatBean.getMsg()) ? activity.getString(R.string.login_chat_fail) : loginChatBean.getMsg());
                        onLoginFail();
                        return;
                    }
                    if (!TextUtils.isEmpty(loginChatBean.getDataKey())) {
                        //登陆之后获取AES加密的KEY
                        ConfigCons.DATA_KEY = loginChatBean.getDataKey();
                    }
                    if (loginChatBean.getSource() != null) {
                        LoginChatBean.SourceBean userTmp = loginChatBean.getSource();
                        LoginChatBean.SourceBean.UserConfigRoomBean agentRoom = userTmp.getAgentRoom();
                        ChatRoomListBean.SourceBean.DataBean userConfigRoom = userTmp.getUserConfigRoom();
                        if (agentRoom != null) {
                            ChatSpUtils.instance(activity).setAGENT_USER_CODE(agentRoom.getAgentUserCode());
                            ChatSpUtils.instance(activity).setAGENT_USER("1");
                            String agentEnterPublicRoom = agentRoom.getSwitchAgentEnterPublicRoom();
                            if (!TextUtils.isEmpty(agentEnterPublicRoom) && agentEnterPublicRoom.equals("1")) {
                                agentEnterPublicRoom = "1";
                            } else {
                                agentEnterPublicRoom = "0";
                            }
                            iView.onAgentRoom(userTmp.getRoomEntify());
                            ChatSpUtils.instance(activity).setSWITCH_AGENT_PERMISSION(agentEnterPublicRoom);
                        } else {
                            ChatSpUtils.instance(activity).setAGENT_USER("0");
                            ChatSpUtils.instance(activity).setSWITCH_AGENT_PERMISSION("0");
                        }
                        if (userConfigRoom != null) {
                            iView.onConfigRoom(userConfigRoom);
                        }
                        ChatSpUtils.instance(activity).setUSER_TYPE(userTmp.getUserType() + "");
                        getSysConfig(userTmp.getSelfUserId(), userTmp.getStationId(), userTmp.getUserType());
                    } else {
                        dialogDis();
                        showToast(TextUtils.isEmpty(loginChatBean.getMsg()) ? activity.getString(R.string.login_chat_fail) : loginChatBean.getMsg());
                        onLoginFail();
                        return;
                    }
                    break;
                case GET_SYS_CONFIG:
                    ChatSysConfigBean chatSysConfigBean = new Gson().fromJson(json, ChatSysConfigBean.class);
                    if (chatSysConfigBean.isSuccess()) {
                        String config = new Gson().toJson(chatSysConfigBean.getSource());
                        ChatSpUtils.instance(activity).setChatSysConfig(config);
                        iView.onLoginSuc(loginChatBean);
                    } else {
                        dialogDis();
                        showToast(TextUtils.isEmpty(chatSysConfigBean.getMsg()) ? activity.getString(R.string.get_sys_config_fail) : chatSysConfigBean.getMsg());
                        onLoginFail();
                    }
                    break;
                case CHAT_ROOM_LIST_REQUEST:
                    ChatRoomListBean chatRoomListBean = new Gson().fromJson(json, ChatRoomListBean.class);
                    dialogDis();
                    if (!chatRoomListBean.isSuccess()) {
                        showToast(Utils.isEmptyString(chatRoomListBean.getMsg()) ? activity.getString(R.string.get_chat_room_list_fail)
                                : chatRoomListBean.getMsg());
                        return;
                    }
//                    if (chatRoomListBean.getSource().getData().isEmpty()) {
//                        dialogDis();
//                        showToast(R.string.novalid_room);
//                        return;
//                    }
                    iView.onGetChatRoomList(chatRoomListBean);
                    break;
                case JOIN_PRIVATE_CHAT_ROOM_SUCCESS:
                    dialogDis();
                    ChatJoinPrivateRoomMsg privateRoomMsg = new Gson().fromJson(json, ChatJoinPrivateRoomMsg.class);
                    if (privateRoomMsg.isSuccess()) {
                        iView.onJoinPrivateRoomSuccess(privateRoomMsg);
                    } else {
                        showToast(Utils.isEmptyString(privateRoomMsg.getMsg()) ? activity.getString(R.string.join_private_chat_fail)
                                : privateRoomMsg.getMsg());
                    }
                    break;
                case JOIN_CHAT_ROOM_REQUEST:
                    if (psdDialog != null && psdDialog.isShowing()) {
                        psdDialog.dismiss();
                        psdDialog = null;
                    }
                    joinChatRoomBean = new Gson().fromJson(json, JoinChatRoomBean.class);
                    if (!joinChatRoomBean.isSuccess()) {
                        dialogDis();
                        showToast(Utils.isEmptyString(joinChatRoomBean.getMsg()) ? activity.getString(R.string.join_chat_room_fail)
                                : joinChatRoomBean.getMsg());
                        return;
                    }
                    changeRoom(oldRoomId, joinChatRoomBean.getSource().getRoomId(),
                            joinChatRoomBean.getSource().getNickName(), joinChatRoomBean.getSource().getAccount());
                    break;
                case JOIN_CHAT_ROOM_SUCCESS:
                    dialogDis();
                    BaseChatReceiveMsg baseChatReceiveMsg = new Gson().fromJson(json, BaseChatReceiveMsg.class);
                    if (!baseChatReceiveMsg.isSuccess()) {
                        showToast(Utils.isEmptyString(baseChatReceiveMsg.getMsg()) ? activity.getString(R.string.join_chat_room_fail)
                                : baseChatReceiveMsg.getMsg());
                        return;
                    }
                    oldRoomId = baseChatReceiveMsg.getRoomId();
                    iView.onJoinChatRoom(joinChatRoomBean);
                    break;
                case SEND_TEXT_MSG_REQUEST:
                    SendMsgBean msgBean = new Gson().fromJson(json, SendMsgBean.class);
                    if (msgBean.isSuccess() && "9".equals(msgBean.getSource().getMsgType())) {
                        // 分享今日盈亏
                        ChatShareDataBean shareDataBean = new Gson().fromJson(json, ChatShareDataBean.class);
                        if (shareDataBean.isSuccess()) {
                            playSound(R.raw.send_msg, ConfigCons.SEND_MSG_SOUND);
                            iView.onShareData(shareDataBean);
                        } else {
                            showToast(TextUtils.isEmpty(shareDataBean.getMsg()) ? "今日盈亏分享失败！" : shareDataBean.getMsg());
                            iView.onShareFail();
                        }
                        return;
                    }
                    if (!msgBean.isSuccess()) {
                        if (msgBean.getCode() != null && msgBean.getCode().equals(ConfigCons.SEND_RED_PACKAGE)) {

                        } else {
                            iView.onSendFail();
                        }
                        String errorMsg = msgBean.getMsg();
                        showToast(TextUtils.isEmpty(errorMsg) ? activity.getResources().getString(R.string.request_fail) : errorMsg);
                        return;
                    }
                    playSound(R.raw.send_msg, ConfigCons.SEND_MSG_SOUND);
                    if (msgBean.getCode().equals(ConfigCons.SEND_RED_PACKAGE)) {
                        iView.onSendRedPackage(msgBean);
                    } else {
                        iView.onSendSuccess(msgBean.getSource().getMsgUUID());
                    }
                    break;
                case GRAB_RED_PACKAGE:
                    try {
                        JSONObject object = new JSONObject(json);
                        GrabRedPackageBean packageBean = new Gson().fromJson(json, GrabRedPackageBean.class);
                        if (!packageBean.isSuccess()) {
                            if ("se34".equals(object.getString("status"))) {
                                // 已领取过
                                iView.onGrabed(object.getString("payId"));
                            } else
                                showToast(TextUtils.isEmpty(packageBean.getMsg()) ? activity.getString(R.string.request_fail) : packageBean.getMsg());
                            return;
                        }
                        if ("se34".equals(packageBean.getSource().getStatus())) {
                            // 已领取过
                            iView.onGrabed(packageBean.getSource().getPayId());
                        } else if ("se23".equals(packageBean.getSource().getStatus())) {
                            // 红包已经抢光了
                            iView.onGrabOut(packageBean.getSource().getPayId());
                        } else {
                            iView.onGrabRedPackage(packageBean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case RED_PACKAGE_DETAIL:
                    RedPackageDetailBean detailBean = new Gson().fromJson(json, RedPackageDetailBean.class);
                    if (!detailBean.isSuccess()) {
                        showToast(TextUtils.isEmpty(detailBean.getMsg()) ? activity.getString(R.string.request_fail) : detailBean.getMsg());
                        return;
                    }
                    iView.onRedPackageDetail(detailBean);
                    break;
                case GET_LOTTERY_LIST:
                    ChatLotteryBean chatLotteryBean = new Gson().fromJson(json, ChatLotteryBean.class);
                    if (!chatLotteryBean.isSuccess()) {
                        showToast(TextUtils.isEmpty(chatLotteryBean.getMsg()) ? activity.getString(R.string.request_fail) : chatLotteryBean.getMsg());
                        return;
                    }
                    iView.onGetLotteryList(chatLotteryBean);
                    break;
                case GET_NOTICE_REQUEST:
                    ChatRoomNoticeBean chatRoomNoticeBean = new Gson().fromJson(json, ChatRoomNoticeBean.class);
                    if (!chatRoomNoticeBean.isSuccess()) {
                        showToast(TextUtils.isEmpty(chatRoomNoticeBean.getMsg()) ? activity.getString(R.string.request_fail) : chatRoomNoticeBean.getMsg());
                        return;
                    }
                    iView.onGetNotice(chatRoomNoticeBean);
                    break;
                case GET_LOTTERY_RESULT:
                    LotteryDownBean chatLotteryDetailBean = new Gson().fromJson(json, LotteryDownBean.class);
                    iView.onGetLotteryDetail(chatLotteryDetailBean);
                    break;
                case GET_HISTORY_MESSAGE:
                    ChatHistoryMessageBean chatHistoryMessageBean = new Gson().fromJson(json, ChatHistoryMessageBean.class);
                    if (!chatHistoryMessageBean.isSuccess()) {
                        showToast(TextUtils.isEmpty(chatHistoryMessageBean.getMsg()) ? activity.getString(R.string.request_fail) : chatHistoryMessageBean.getMsg());
                        iView.onGetHistoryFail();
                    } else if (chatHistoryMessageBean.getSource() == null) {
                        iView.onGetHistoryFail();
                    }else {
                        iView.onGetHistoryMessage(chatHistoryMessageBean);
                    }
                    break;
                case FOLLOW_BET:
                    SendMsgBean sendMsgBean = new Gson().fromJson(json, SendMsgBean.class);
                    if (!sendMsgBean.isSuccess()) {
                        showToast(TextUtils.isEmpty(sendMsgBean.getMsg()) ? activity.getString(R.string.request_fail) : sendMsgBean.getMsg());
                        return;
                    }
                    if (sendMsgBean.getSource() != null && !sendMsgBean.getSource().isSuccess()) {
                        showToast(TextUtils.isEmpty(sendMsgBean.getSource().getMsg()) ? activity.getString(R.string.request_fail) : sendMsgBean.getSource().getMsg());
                        return;
                    }
                    showToast("跟单成功");
                    break;
                case GET_VIOLATE_WORDS:
                    ChatViolateWordsBean chatViolateWordsBean = new Gson().fromJson(json, ChatViolateWordsBean.class);
                    if (chatViolateWordsBean.isSuccess() && chatViolateWordsBean.getSource() != null) {
                        List<String> arr = new ArrayList<>();
                        for (int i = 0; i < chatViolateWordsBean.getSource().size(); i++) {
                            if (chatViolateWordsBean.getSource().get(i).getEnable() == 1) { //禁言词开启
                                arr.add(chatViolateWordsBean.getSource().get(i).getWord());
                            }
                        }
                        iView.onGetViolateWords(arr);
                    }
                    break;
                case GET_PERSON_DATA:
                    ChatPersonDataBean chatPersonDataBean = new Gson().fromJson(json, ChatPersonDataBean.class);
                    if (chatPersonDataBean != null && chatPersonDataBean.getSource() != null) {
                        if (chatPersonDataBean.isSuccess()) {
                            //LogUtils.e("获取用户数据", new Gson().toJson(chatPersonDataBean).toString());
                            iView.getPersonData(chatPersonDataBean);
                        } else {
                            showToast(TextUtils.isEmpty(chatPersonDataBean.getMsg()) ? getMsg() : chatPersonDataBean.getMsg());
                            return;
                        }
                    } else {
                        showToast(getMsg());
                    }
                    break;
                case MODIFY_PERSON_DATA:
                    ChatModifyPersonDataBean chatModifyPersonDataBean = new Gson().fromJson(json, ChatModifyPersonDataBean.class);
                    if (chatModifyPersonDataBean.isSuccess()) {
                        iView.ModifyPersonData(true);
                    } else {
                        showToast(TextUtils.isEmpty(chatModifyPersonDataBean.getMsg()) ? "修改当前用户资料失败" : chatModifyPersonDataBean.getMsg());
                        return;
                    }
                    break;
                case GET_PHOTO_LIST:
                    ChatPersonPhotoListBean chatPersonPhotoListBean = new Gson().fromJson(json, ChatPersonPhotoListBean.class);
                    if (!chatPersonPhotoListBean.isSuccess()) {
                        return;
                    }
                    if (chatPersonPhotoListBean.getSource() != null && chatPersonPhotoListBean.getSource().getItems().size() > 0) {
                        iView.getPhotoList(chatPersonPhotoListBean);
                    }
                    break;
                case GET_QUICK_MESSAGES:
                    ChatQuickMessageBean chatQuickMessageBean = new Gson().fromJson(json, ChatQuickMessageBean.class);
                    if (!chatQuickMessageBean.isSuccess()) {
                        showToast(TextUtils.isEmpty(chatQuickMessageBean.getMsg()) ? activity.getString(R.string.request_fail) : chatQuickMessageBean.getMsg());
                        return;
                    }
                    if (chatQuickMessageBean.getSource() == null) {
                        return;
                    }
                    iView.onGetQuickMessages(chatQuickMessageBean);
                    break;
                case SAVE_IMAGES:
                    ChatCollectionImagesBean chatCollectionImagesBean = new Gson().fromJson(json, ChatCollectionImagesBean.class);
                    if (!chatCollectionImagesBean.isSuccess()) {
                        showToast(TextUtils.isEmpty(chatCollectionImagesBean.getMsg()) ? activity.getString(R.string.request_fail) : chatCollectionImagesBean.getMsg());
                        return;
                    }
                    if (chatCollectionImagesBean.getSource() == null) {
                        return;
                    }
                    iView.onCollectionsImages(chatCollectionImagesBean);

                    break;
                case GET_ONLINE_USER:
                    ChatUserListBean chatUserListBean = new Gson().fromJson(json, ChatUserListBean.class);
                    if (!chatUserListBean.isSuccess()) {
                        showToast(TextUtils.isEmpty(chatUserListBean.getMsg()) ? "获取在线人员列表失败" : chatUserListBean.getMsg());
                        return;
                    }
                    iView.onGetOnlineUser(chatUserListBean);
                    break;
                case GET_ONLINE_MANAGER:
                    ChatUserListBean managerListBean = new Gson().fromJson(json, ChatUserListBean.class);
                    if (!managerListBean.isSuccess()) {
                        showToast(TextUtils.isEmpty(managerListBean.getMsg()) ? "获取管理员列表失败" : managerListBean.getMsg());
                        return;
                    }
                    iView.onGetManagerList(managerListBean);
                    break;
                case SHARE_DATA:
                    ChatShareDataBean chatShareDataBean = new Gson().fromJson(json, ChatShareDataBean.class);
                    if (chatShareDataBean.isSuccess()) {
                        iView.onShareData(chatShareDataBean);
                    } else {
                        showToast(TextUtils.isEmpty(chatShareDataBean.getMsg()) ? "今日盈亏分享失败！" : chatShareDataBean.getMsg());
                        iView.onShareFail();
                    }
                    break;
                case GET_CHARGE:
                    ChatShareDataBean bean = new Gson().fromJson(json, ChatShareDataBean.class);
                    iView.onStopRefresh();
                    if (bean.isSuccess()) {
                        iView.onGetCharge(CommonUtils.getDoubleToString(Double.parseDouble(bean.getSource().getMoney()), 2));
                    } else {
                        showToast(TextUtils.isEmpty(bean.getMsg()) ? "余额获取失败" : bean.getMsg());
                    }
                    break;
                case SIGN:
                    ChatSignDataBean signBean = new Gson().fromJson(json, ChatSignDataBean.class);
                    if (signBean.isSuccess()) {
                        iView.onSign(signBean);
                    } else {
                        showToast(TextUtils.isEmpty(signBean.getMsg()) ? "签到失败" : signBean.getMsg());
                    }
                    break;
                case MASTER_PLAN:
                    ChatHistoryMessageBean masterPlan = new Gson().fromJson(json, ChatHistoryMessageBean.class);
                    if (!masterPlan.isSuccess()) {
                        showToast(TextUtils.isEmpty(masterPlan.getMsg()) ? activity.getString(R.string.request_fail) : masterPlan.getMsg());
                        return;
                    }
                    if (masterPlan.getSource() == null || masterPlan.getSource().isEmpty()) {
                        showToast("暂无导师计划消息！");
                        return;
                    }
                    iView.onGetMasterPlan(masterPlan);
                    break;

                case GET_PRIVATE_CONVERSATION:
                    ChatPrivateConversationBean conversationBean = new Gson().fromJson(json, ChatPrivateConversationBean.class);
                    if (conversationBean.isSuccess()) {
                        iView.initPrivateConversation(conversationBean);
                    } else {
//                        showToast(TextUtils.isEmpty(conversationBean.getMsg()) ? "请求异常" : conversationBean.getMsg());
                        LogUtils.e(this, "私聊接口请求异常");
                    }
                    break;
                case POST_CHAT_REMARK:
                    ChatRemarkBean chatRemarkBean = new Gson().fromJson(json, ChatRemarkBean.class);

                    iView.onRemarkResult(chatRemarkBean,
                            chatRemarkObj.optString("passiveUserId"),
                            chatRemarkObj.optString("remarks"));
                    break;
                case GET_PRIVATE_GROUP_HISTORY:
                    ChatPrivateMessageBean chatPrivateMessageBean = new Gson().fromJson(json, ChatPrivateMessageBean.class);
                    if (chatPrivateMessageBean.isSuccess()) {
                        iView.onGetPrivateChatHistoryMessagesSuccess(chatPrivateMessageBean);
                    } else {
                        LogUtils.e(this, "获取私聊消息失败");
                    }
                    break;
                case GET_PRIVATE_SEND_MSG:
                    ChatPrivateMessageBean chatBean = new Gson().fromJson(json, ChatPrivateMessageBean.class);
                    if (chatBean.isSuccess()) {
                        iView.onGetPrivateChatHistoryMessagesSuccess(chatBean);
                    } else {
                        ToastUtil.showToast(context, "發送私聊消息失敗");
                        if(chatBean.getSource() != null && !TextUtils.isEmpty(chatBean.getSource().getUserMessageId())){
                            iView.onSendFailMsg(chatBean.getSource().getUserMessageId());
                        }else {
                            iView.onSendFail();
                        }
                    }
                    break;
                case BAN_SPEAK_USER:
                    ChatMuteBean chatMuteBean = new Gson().fromJson(json, ChatMuteBean.class);
                    if (!chatMuteBean.isSuccess()) {
                        ToastUtil.showToast(context, "操作失败");
                    } else {
                        //禁言成功 (将item重新赋值)
                        boolean isDeleteMessage = chatSysConfig.getSwitch_ban_speak_del_msg().equals("1");
                        iView.onGetBanSuccessful(chatMuteBean.getSource().getSpeakingClose(), chatMuteBean.getSource().getUserId(), isDeleteMessage);
                    }
                    break;
                case BAN_SPEAK_ROOM:
                    ChatMuteAllBean chatMuteAllBean = new Gson().fromJson(json, ChatMuteAllBean.class);
                    if (chatMuteAllBean.isSuccess()) {
                        String msg1 = TextUtils.isEmpty(chatMuteAllBean.getMsg()) ? "操作成功" : chatMuteAllBean.getMsg();
                        String msg2 = TextUtils.isEmpty(chatMuteAllBean.getSource().getMsg()) ? "当前房间已禁言" : chatMuteAllBean.getSource().getMsg();
                        ToastUtil.showToast(context, msg2);
                    } else {
                        ToastUtils.showToast(context, "操作失败");
                    }
                    break;

                case RETRACT_MSG:
                    ChatRecallBean chatRecallBean = new Gson().fromJson(json, ChatRecallBean.class);
                    if (chatRecallBean.isSuccess()) {
                        System.out.println("消息撤回成功");
                    } else {
                        ToastUtil.showToast(context, "操作失败");
                    }
                    break;

                case GET_TOOL_PERMISSION:
                    ChatToolPermissionBean chatToolPermissionBean = new Gson().fromJson(json, ChatToolPermissionBean.class);

                    if (chatToolPermissionBean.isSuccess()) {
                        iView.onGetToolPermissionSuccess(chatToolPermissionBean.getSource().getToolPermission());
                    }
                    break;
                case APPLY_FOR_BAN_SPEAK:
                    try {
                        JSONObject object = new JSONObject(json);
                        String optString;
                        if (object.getBoolean("success")) {
                            optString = object.getJSONObject("source").optString("msg");
                        } else {
                            optString = object.optString("msg");
                        }
                        if (!TextUtils.isEmpty(optString))
                            showToast(optString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case GET_LONG_DRAGON:
                    ChatLongDragonBean longDragonBean = new Gson().fromJson(json, ChatLongDragonBean.class);
                    if (longDragonBean.isSuccess()) {
                        iView.onGetLongDragon(longDragonBean.getSource().getLongDragon());
                    } else {
                        showToast(TextUtils.isEmpty(longDragonBean.getMsg()) ? "长龙数据获取失败!" : longDragonBean.getMsg());
                    }
                    break;
                case GET_AUDIT_LIST:
                    try{
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject msgObject = jsonObject.optJSONObject("msg");
                        if(msgObject != null && msgObject.optInt("type") == 2){
                            if(jsonObject.optBoolean("success")){
                                getAuditList();
                            }else {
                                showToast("审核失败");
                            }
                        }else {
                            GetAuditListBean auditListBean = new Gson().fromJson(json, GetAuditListBean.class);
                            if(auditListBean.isSuccess()){
                                if (auditListBean.getMsg().getListUserAudit() != null && !auditListBean.getMsg().getListUserAudit().isEmpty()){
                                    iView.onGetAuditList(auditListBean.getMsg().getListUserAudit());
                                }else {
                                    iView.onGetAuditListNull();
                                }
                            }else {
                                showToast("审核名单获取失败!");
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    break;
                case GET_TODAY_PROFIT:
                    TodayProfitResponse response = new Gson().fromJson(json, TodayProfitResponse.class);
                    if(response.isSuccess() && ConfigCons.GET_WINNING_LIST.equals(response.getCode()) &&
                        response.getMsg() != null && "4".equals(response.getMsg().getPrizeType())){
                        iView.onGetTodayProfitList(response.getMsg().getWinningList());
                    }
                    break;
            }
        }
    };

    private boolean isRefresh = false; //用户是否在刷新数据

    public String getMsg() {
        if (isRefresh) {
            return "刷新数据失败";
        }
        return "获取当前用户资料失败";
    }

    private JoinChatRoomBean joinChatRoomBean;


    public ChatMainPresenter(ChatBaseActivity activity, IChatBaseView view) {
        super(activity, view);
        this.activity = (ChatMainActivity) activity;
        this.iView = (IIChatMainView) view;
        chatSysConfig = ChatSpUtils.instance(activity).getChatSysConfig();
        logingDialog = new LogingDialog(activity);
        logingDialog.setCanceledOnTouchOutside(false);
        socketManager = SocketManager.instance(activity);
        initSocket();
    }

    private void initSocket() {
        socketManager.setConnectErrorListener(connectErrorListener);
        socketManager.setConnectListener(connectListener);
        socketManager.setConnectTimeoutListener(connectTimeoutListener);
        socketManager.setDisconnectListener(disconnectListener);
        socketManager.setMsgListner(msgListner);
        socketManager.setPingListener(pingListener);
        socketManager.setPongListner(pongListner);
        socketManager.setPushListener(pushListener);
        socketManager.setReconnectListener(reconnectListener);
        socketManager.setLoginListener(loginListener);
        socketManager.setSendListener(sendListener);
        socketManager.setJoinRoomListener(joinRoomListener);
        socketManager.setJoinGroupListener(joinGroupListener);
    }

    public void initData() {
        // 模拟数据
//        loadChatMsg();
        getChatRoomList(ChatSpUtils.instance(activity).getUserId(), ChatSpUtils.instance(activity).getStationId());
    }

    /**
     * 聊天室授权
     */
    public void authorization() {
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.YUNJI_BASE_URL).append(ConfigCons.PORT).append(ConfigCons.AUTHORITY_URL);
        if (!activity.isFinishing())
            logingDialog.updateTitle(activity.getString(R.string.authority_ongoing));
        CrazyRequest<CrazyResult<AuthorityBean>> request = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(AUTHORIZATION_REQUEST)
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .headers(CommonUtils.getChatHeader(activity))
                .placeholderText(activity.getString(R.string.authority_ongoing))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<AuthorityBean>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(activity, request, this);
    }


    /**
     * 聊天室登陆
     *
     * @param clusterId
     * @param encrypted
     * @param sign
     */
    public void loginChatRoom(String clusterId, String encrypted, String sign) {
//        StringBuilder urls = new StringBuilder();
//        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
        String token = "clusterId=" + clusterId + "&encrypted=" + encrypted + "&sign=" + sign;
        LogUtils.e("loginChatRoom", "token:" + token);
        LoginJsonModel jsonModel = new LoginJsonModel(ConfigCons.LOGIN_AUTHORITY, ConfigCons.SOURCE, token);
        String bodyStr = new Gson().toJson(jsonModel);
        if (!activity.isFinishing())
            logingDialog.updateTitle(activity.getString(R.string.login_chat_ongoing));
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.LOGIN, bodyStr, false, true, "");
        /*------------------------------*/

        //Http登录
//        CrazyRequest<CrazyResult<LoginChatBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(LOGIN_CHATROOM_REQUEST)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(bodyStr)
//                .contentType("application/json;utf-8")
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<LoginChatBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }

    //获取聊天室配置
    public void getSysConfig(String userId, String stationId, int userType) {
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
        ChatSysConfigModel chatSysConfig = new ChatSysConfigModel(ConfigCons.GET_SYS_CONFIG, userId, stationId, userType, ConfigCons.SOURCE, UUID.randomUUID().toString());
        String bodyStr = new Gson().toJson(chatSysConfig);
        if (!activity.isFinishing())
            logingDialog.updateTitle("获取系统配置中...");
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
        /*------------------------------*/
//        CrazyRequest<CrazyResult<ChatSysConfigBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(GET_SYS_CONFIG)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(bodyStr)
//                .contentType("application/json;utf-8")
//                .placeholderText("获取系统配置中...")
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<ChatSysConfigBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }


    /**
     * 1、获取聊天室列表
     *
     * @param userId
     * @param stationId
     */
    public void getChatRoomList(String userId, String stationId) {
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
        ChatRoomListJsonModel jsonModel = new ChatRoomListJsonModel(ConfigCons.CHAT_ROOM_LIST, ConfigCons.SOURCE, userId,
                stationId, ChatSpUtils.instance(activity).getAGENT_USER(), ChatSpUtils.instance(activity).getSWITCH_AGENT_PERMISSION());
        String bodyStr = new Gson().toJson(jsonModel);
        if (!activity.isFinishing())
            logingDialog.updateTitle(activity.getString(R.string.get_chat_room_list_ongoing));
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
        /*------------------------------*/
//        CrazyRequest<CrazyResult<ChatRoomListBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(CHAT_ROOM_LIST_REQUEST)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(bodyStr)
//                .contentType("application/json;utf-8")
//                .placeholderText(activity.getString(R.string.get_chat_room_list_ongoing))
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<ChatRoomListBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }

    public void getQuickMessage(String roomId, String stationId) {
        ChatQuickMessageJsonModel chatQuickMessageJsonModel = new ChatQuickMessageJsonModel();
        chatQuickMessageJsonModel.setSource(ConfigCons.SOURCE);
        chatQuickMessageJsonModel.setCode(ConfigCons.GET_QUICK_MESSAGES);
        chatQuickMessageJsonModel.setStationId(stationId);
        chatQuickMessageJsonModel.setRoomId(roomId);
        String bodyStr = new Gson().toJson(chatQuickMessageJsonModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }

    /**
     * 2、进入聊天室
     *
     * @param id
     * @param roomKey isShowDia :是否显示loading ,首次登录的时候不显示loading
     */
    public void joinChatRoom(final String id, final String roomKey, String roomName, boolean isShowDia) {
        if (TextUtils.isEmpty(roomKey)) {
            joinRoom(id, roomKey, isShowDia);
        } else {
            final String userId = ChatSpUtils.instance(activity).getUserId();
            String roomPasswordData = ChatSpUtils.instance(activity).getRoomPasswordData(userId + "," + id);
            //如果之前保存了密码直接进入房间
            if (!TextUtils.isEmpty(roomPasswordData) && roomPasswordData.equalsIgnoreCase(roomKey)) {
                joinRoom(id, roomKey, false);
                return;
            }

            dialogDis();
            psdDialog = new PsdDialog(activity);
            psdDialog.setMaxcount(roomKey.length());
            psdDialog.setRoomName(roomName);
            psdDialog.psdInputView.setComparePassword(roomKey, new PayPsdInputView.onPasswordListener() {
                @Override
                public void onDifference(String oldPsd, String newPsd) {
                    showToast("密码不正确");
                    psdDialog.psdInputView.cleanPsd();
                }

                @Override
                public void onEqual(String psd) {
                    joinRoom(id, roomKey, false);
                    //保存当前用户当前房间的密码
                    ChatSpUtils.instance(context).setRoomPasswordData(userId + "," + id, roomKey);
                }

                @Override
                public void inputFinished(String inputPsd) {

                }
            });
            psdDialog.show();

        }
    }

    /**
     * 进入房间
     *
     * @param id
     * @param roomKey
     */
    private void joinRoom(String id, String roomKey, boolean isShowDia) {
        String userId = ChatSpUtils.instance(activity).getUserId();
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
        JoinChatRoomJsonModel jsonModel = new JoinChatRoomJsonModel(ConfigCons.JOIN_CHAT_ROOM, ConfigCons.SOURCE, userId, id, roomKey);
//        if (ChatSpUtils.instance(activity).getACCOUNT_TYPE() == 2 || ChatSpUtils.instance(activity).getACCOUNT_TYPE() == 3) {
        jsonModel.setAgentUserCode(ChatSpUtils.instance(activity).gettAGENT_USER_CODE());
//        }
        String bodyStr = new Gson().toJson(jsonModel);
        if (!activity.isFinishing())
            logingDialog.updateTitle(activity.getString(R.string.join_chat_room_ongoing));
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
        /*------------------------------*/
//        CrazyRequest<CrazyResult<JoinChatRoomBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(JOIN_CHAT_ROOM_REQUEST)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(bodyStr)
//                .contentType("application/json;utf-8")
//                .placeholderText(activity.getString(R.string.join_chat_room_ongoing))
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<JoinChatRoomBean>() {
//                }.getType()))
//                .loadMethod(isShowDia ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() : CrazyRequest.LOAD_METHOD.NONE.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }

    private void changeRoom(String oldRoomId, String newRoomId, String nickName, String account) {
        socketManager.setJoinRoomListener(joinRoomListener);
        ChangeRoomModel changeRoomModel = new ChangeRoomModel(oldRoomId, newRoomId,
                ChatSpUtils.instance(activity).getStationId(), nickName, ConfigCons.SOURCE, account);
        String bodyStr = new Gson().toJson(changeRoomModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_JOIN_ROOM, bodyStr, false, true, "");
        /*------------------------------*/
    }

    public void joinPrivateRoom(String roomIdX, String type, String userId, String idX) {
        socketManager.setJoinRoomListener(joinGroupListener);
        JoinPrivateRoomJsonModel joinPrivateRoomJsonModel = new JoinPrivateRoomJsonModel();
        joinPrivateRoomJsonModel.setRoomId(roomIdX);
        joinPrivateRoomJsonModel.setPassiveUserId(idX);
        joinPrivateRoomJsonModel.setCode(ConfigCons.GET_PRIVATE_GROUP_HISTORY);
        joinPrivateRoomJsonModel.setSource(ConfigCons.SOURCE);
        joinPrivateRoomJsonModel.setUserId(userId);
        joinPrivateRoomJsonModel.setType(type);
        joinPrivateRoomJsonModel.setStationId(ChatSpUtils.instance(context).getStationId());

        String bodyStr = new Gson().toJson(joinPrivateRoomJsonModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_JOIN_GROUP, bodyStr, false, true, "");
        /*------------------------------*/
    }

    /**
     * 用户申请解除禁言
     *
     * @param applyBanSpeakModel
     */
    public void applyBanTalk(ApplyBanSpeakModel applyBanSpeakModel) {
        String content = new Gson().toJson(applyBanSpeakModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, content, false, true, "");
        /*------------------------------*/
    }


    /**
     * 获取在线用户
     *
     * @param jsonModel
     */
    public void getOnlineUser(GetOnlineJsonModel jsonModel) {
        String content = new Gson().toJson(jsonModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, content, false, true, "");
        /*------------------------------*/
    }

    /**
     * 抢红包
     *
     * @param jsonModel
     */
    public void grabRedPackage(GrabRedPackageJsonModel jsonModel) {
        logingDialog.updateTitle("红包领取中...");
        String content = new Gson().toJson(jsonModel);
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, content, false, true, "");
        /*------------------------------*/
//        CrazyRequest<CrazyResult<GrabRedPackageBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(content)
//                .contentType("application/json;utf-8")
//                .placeholderText(activity.getString(R.string.pick_ongoing))
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<GrabRedPackageBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }

    /**
     * 获取红包详情
     *
     * @param payId
     */
    public void getRedPackageDetail(String payId) {
        RedPackageDetailJsonModel jsonModel = new RedPackageDetailJsonModel();
        jsonModel.setPayId(payId);
        jsonModel.setCode(ConfigCons.RED_PACKAGE_DETAIL);
        jsonModel.setUserId(ChatSpUtils.instance(activity).getUserId());
        jsonModel.setSource(ConfigCons.SOURCE);
        String json = new Gson().toJson(jsonModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, json, false, true, "");
        /*------------------------------*/
//        StringBuilder urls = new StringBuilder();
//        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
//        CrazyRequest<CrazyResult<RedPackageDetailBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(RED_PACKAGE_DETAIL)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(json)
//                .contentType("application/json;utf-8")
//                .placeholderText(activity.getString(R.string.loading))
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<RedPackageDetailBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }

    /**
     * 发送消息
     *
     * @param chatMessage
     * @param roomBeanMsg
     */
    public void sentMsg(final ChatMessage chatMessage, JoinChatRoomBean.SourceBean roomBeanMsg) {
//        ChatSendMsg message = new ChatSendMsg();
//            SYSTEM_MSG_TYPE = 0;//系统消息
//            TEXT_MSG_TYPE = 1;//文本消息
//            IMAGE_MSG_TYPE = 2;//图片消息
//            REDPACKET_MSG_TYPE = 3;//发红包
//            PICK_REDPACKET_MSG_TYPE = 4;//领红包
//            PLAN_MSG_TYPE = 6;//计划消息
//            FOLLOW_BET_MSG_TYPE = 8;//跟注消息
//            SHARE_BET_MSG_TYPE = 5;//分享注单消息
//        使用Socket发送消息
        if (chatMessage.getBody().getCode().equals(ConfigCons.GET_PRIVATE_GROUP_HISTORY)) {
            chatMessage.getBody().setCode(ConfigCons.GET_PRIVATE_SEND_MSG);
        }
        String content = new Gson().toJson(chatMessage.getBody());
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, content, true, true, chatMessage.getUuid());
        /*------------------------------*/
        //使用http接口发送消息
//        StringBuilder urls = new StringBuilder();
//        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
//        CrazyRequest<CrazyResult<SendMsgBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(SEND_TEXT_MSG_REQUEST)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(content)
//                .contentType("application/json;utf-8")
//                .placeholderText(activity.getString(R.string.join_chat_room_ongoing))
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<SendMsgBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }

    /**
     * 使用Socket发送消息
     *
     * @param socketKey Socket事件Key
     * @param content   发送的内容
     */
    public void sendSocket(String socketKey, String content, final boolean isSendMsg, final boolean showToast, final String msgUUID) {
        socketManager.setSendListener(sendListener);
        final boolean[] isSuccess = {false};
        String str = "";
        if (socketKey.equals(ConfigCons.USER_JOIN_ROOM) || socketKey.equals(ConfigCons.LOGIN)) {
            str = AESUtils.encrypt(content, ConfigCons.DEFAULT_KEY, ConfigCons.DEFAULT_IV);
        } else if (socketKey.equals(ConfigCons.USER_JOIN_GROUP)) {
            str = AESUtils.encrypt(content, ConfigCons.DEFAULT_IV, ConfigCons.DEFAULT_KEY);
        } else {
            str = AESUtils.encrypt(content, ConfigCons.DATA_KEY, ConfigCons.DEFAULT_IV);
        }

        Ack ack = new Ack() {
            @Override
            public void call(Object... args) {
                LogUtils.e("a", "服务器确认收到我的消息=" + Arrays.toString(args));
                isSuccess[0] = true;
//                if (isSendMsg) {
//                    iView.onSendSuccess(msgUUID);
//                }
            }
        };
        socketManager.sendMsg(socketKey, str, ack);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 延迟十秒钟，如果还没有发送成功，就提示消息发送失败
                if (!isSuccess[0]) {
                    if (isSendMsg) {
                        iView.onSendFailMsg(msgUUID);
                    }
                    if (logingDialog != null && logingDialog.isShowing()) {
                        logingDialog.dismiss();
                    }
                    if (showToast) {
                        ToastUtils.showToast(activity, R.string.time_out);
                    }
                }
            }
        }, 15000);
    }


    /**
     * 上传文件
     *
     * @param chatMessage
     * @param type
     */
    public void upLoadFile(final ChatMessage chatMessage, String type, FileHandler.FileHandlerListener fileHandlerListener) {
        int UPLOAD_FILE = 0;
        switch (type) {
            case "jpeg":
                UPLOAD_FILE = UPLOAD_FILE_IMAGE;
                break;
            case "amr":
                UPLOAD_FILE = UPLOAD_FILE_AUDIO;
                break;
        }
        String stationId = ChatSpUtils.instance(activity).getStationId();
        String localPath = ((FileMsgBody) chatMessage.getBody()).getLocalPath();
        String userId = ChatSpUtils.instance(activity).getUserId();
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_FILE_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.UPLOAD_FILE);
        try {
            urls.append("?fileType=").append(URLEncoder.encode(type, "utf-8"));
            urls.append("&name=").append(URLEncoder.encode(System.currentTimeMillis() + "." + type, "utf-8"));
            urls.append("&stationId=").append(URLEncoder.encode(stationId, "utf-8"));
            urls.append("&fileString=").append(URLEncoder.encode(localPath, "utf-8"));
            urls.append("&key=").append(URLEncoder.encode("", "utf-8"));
            urls.append("&userId=").append(URLEncoder.encode(userId, "utf-8"));
//            FileBean fileBean = new FileBean(type, System.currentTimeMillis() + "." + type, stationId, localPath, "");
//        String body = new Gson().toJson(fileBean);
            CrazyRequest<CrazyResult<UpLoadFileBean>> request = new AbstractCrazyRequest.Builder().
                    url(urls.toString())
                    .seqnumber(UPLOAD_FILE)
//                .body(body)
                    .path(localPath)
                    .headers(CommonUtils.getChatHeader(activity))
                    .placeholderText(activity.getString(R.string.up_loading_file))
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.UPLOAD.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<UpLoadFileBean>() {
                    }.getType()))
                    .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                    .fileHandlerListener(fileHandlerListener)
                    .create();
            RequestManager.getInstance().startRequest(activity, request, this);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取彩种列表
     *
     * @param lotteryResultModel
     */
    public void getLotteryResult(final LotteryResultModel lotteryResultModel) {
        String content = new Gson().toJson(lotteryResultModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, content, false, false, "");
        /*------------------------------*/
//        StringBuilder urls = new StringBuilder();
//        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
//        CrazyRequest<CrazyResult<ChatLotteryBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(GET_LOTTERY_LIST)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(content)
//                .contentType("application/json;utf-8")
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<ChatLotteryBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }

    /**
     * 获取公告
     *
     * @param noticeModel
     */
    public void getNotice(final ChatRoomNoticeModel noticeModel) {
        String content = new Gson().toJson(noticeModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, content, false, false, "");
        /*------------------------------*/
//        StringBuilder urls = new StringBuilder();
//        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
//        CrazyRequest<CrazyResult<ChatRoomNoticeBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(GET_NOTICE_REQUEST)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(content)
//                .contentType("application/json;utf-8")
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<ChatRoomNoticeBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }

    /**
     * 取得所有彩票的基本资讯
     */
    public void getAllLotteryInfo(){
        String url = ChatSpUtils.instance(activity).getMainAppBaseUrl() + ConfigCons.GET_ALL_LOTTERY_INFO;
        CrazyRequest<CrazyResult<AllLotteryInfoResponse>> request = new AbstractCrazyRequest.Builder()
                .url(url)
                .seqnumber(GET_ALL_LOTTERY_INFO)
                .headers(CommonUtils.getChatHeader(activity))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .contentType("application/json;utf-8")
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<AllLotteryInfoResponse>() {}.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(activity, request, this);
    }

    /**
     * 获取开奖结果
     *
     * @param bianHao 彩种编码
     */
    public void getKaiJianResult(String bianHao) {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatSpUtils.instance(activity).getMainAppBaseUrl()).append(ConfigCons.LOTTERY_LAST_RESULT_URL);
        sb.append("?lotCode=" + bianHao).append("&version=" + YiboPreferenceUtils.instance(activity).getGameVersion());
        CrazyRequest<CrazyResult<LotteryDownBean>> request = new AbstractCrazyRequest.Builder().
                url(sb.toString())
                .seqnumber(GET_LOTTERY_RESULT)
                .headers(CommonUtils.getChatHeader(activity))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .contentType("application/json;utf-8")
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LotteryDownBean>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(activity, request, this);
    }

    private void getHistoryResultsByCpCode(String cpCode){
        StringBuilder sb = new StringBuilder();
        sb.append(ChatSpUtils.instance(activity).getMainAppBaseUrl()).append(ConfigCons.GET_HISTORY_RESULT)
                .append("?lotCode=").append(cpCode).append("&page=1").append("&rows=").append(40);
        CrazyRequest<CrazyResult<LotteryHistoryResultResponse>> request = new AbstractCrazyRequest.Builder()
                .url(sb.toString())
                .seqnumber(GET_LOTTERY_HISTORY_RESULT)
                .headers(CommonUtils.getChatHeader(activity))
                .placeholderText(activity.getString(R.string.loading))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LotteryHistoryResultResponse>() {}.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();

        RequestManager.getInstance().startRequest(activity, request, this);
    }

    public void onKaiJianResultClicked(String cpCode){
        this.cpCodeOfClickedKaiJianResult = cpCode;
        getHistoryResultsByCpCode(cpCode);
    }

    /**
     * 获取历史消息
     *
     * @param chatMessageHistoryModel
     */
    public void getHistoryMessage(final ChatMessageHistoryModel chatMessageHistoryModel) {
        String content = new Gson().toJson(chatMessageHistoryModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, content, false, false, "");
        /*------------------------------*/
//        StringBuilder urls = new StringBuilder();
//        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
//        CrazyRequest<CrazyResult<ChatHistoryMessageBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(GET_HISTORY_MESSAGE)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(content)
//                .contentType("application/json;utf-8")
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<ChatHistoryMessageBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }

    /**
     * 跟单
     *
     * @param flFollowBetJsonModel
     */
    public void followBet(FollowBetJsonModel flFollowBetJsonModel) {
        String content = new Gson().toJson(flFollowBetJsonModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, content, false, true, "");
        /*------------------------------*/
//        StringBuilder urls = new StringBuilder();
//        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
//        CrazyRequest<CrazyResult<SendMsgBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(FOLLOW_BET)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(content)
//                .contentType("application/json;utf-8")
//                .placeholderText(activity.getString(R.string.join_chat_room_ongoing))
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<SendMsgBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }


    /**
     * 获取违禁词
     */
    public void getViolateWords() {
        ChatViolateWordsModel chatViolateWordsModel = new ChatViolateWordsModel(ConfigCons.GET_VIOLATE_WORDS, ChatSpUtils.instance(activity).getStationId(), ConfigCons.SOURCE);
        String content = new Gson().toJson(chatViolateWordsModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, content, false, false, "");
        /*------------------------------*/
//        StringBuilder urls = new StringBuilder();
//        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
//        CrazyRequest<CrazyResult<ChatViolateWordsBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(GET_VIOLATE_WORDS)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(content)
//                .contentType("application/json;utf-8")
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<ChatViolateWordsBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }


    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (activity.isFinishing() || response == null) {
            dialogDis();
            return;
        }
        CrazyResult<Object> result = response.result;
        switch (response.action) {
            case AUTHORIZATION_REQUEST:
                CrazyResult<Object> authorityResult = response.result;
                if (authorityResult == null) {
                    dialogDis();
                    showToast(R.string.authority_fail);
                    onLoginFail();
                    return;
                }
                if (!authorityResult.crazySuccess) {
                    dialogDis();
                    String errorString = CommonUtils.parseResponseResult(authorityResult.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.authority_fail) : errorString);
                    onLoginFail();
                    return;
                }
                authorityBean = (AuthorityBean) authorityResult.result;
                if (!authorityBean.isSuccess()) {
                    dialogDis();
                    showToast(R.string.authority_fail);
                    onLoginFail();
                    return;
                }
                // 授权成功后修改聊天室域名、文件系统域名
                if (!TextUtils.isEmpty(authorityBean.getContent().getAppChatDomain())) {
                    ConfigCons.CHAT_SERVER_URL = authorityBean.getContent().getAppChatDomain();
                }
                if (!TextUtils.isEmpty(authorityBean.getContent().getFileChatDomain())) {
                    ConfigCons.CHAT_FILE_BASE_URL = authorityBean.getContent().getFileChatDomain();
                }
                if (!activity.isFinishing())
                    logingDialog.updateTitle(activity.getString(R.string.chat_room_connecting));
                connectSocket();
//                loginChatRoom(authorityBean.getContent().getClusterId(), authorityBean.getContent().getEncrypted(), authorityBean.getContent().getSign());
                break;
            case LOGIN_CHATROOM_REQUEST:
                CrazyResult<Object> loginResult = response.result;
                if (loginResult == null) {
                    dialogDis();
                    showToast(R.string.login_chat_fail);
                    onLoginFail();
                    return;
                }
                if (!loginResult.crazySuccess) {
                    dialogDis();
                    String errorString = CommonUtils.parseResponseResult(loginResult.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.login_chat_fail) : errorString);
                    onLoginFail();
                    return;
                }
                loginChatBean = (LoginChatBean) loginResult.result;
                if (!loginChatBean.isSuccess()) {
                    dialogDis();
                    showToast(R.string.login_chat_fail);
                    onLoginFail();
                    return;
                }
                if (loginChatBean.getSource() != null) {
                    LoginChatBean.SourceBean userTmp = loginChatBean.getSource();
                    LoginChatBean.SourceBean.UserConfigRoomBean agentRoom = userTmp.getAgentRoom();
                    if (agentRoom != null) {
                        ChatSpUtils.instance(activity).setAGENT_USER("1");
                        ChatSpUtils.instance(activity).setSWITCH_AGENT_PERMISSION(agentRoom.getSwitchAgentEnterPublicRoom());
                    } else {
                        ChatSpUtils.instance(activity).setAGENT_USER("0");
                    }
                    getSysConfig(userTmp.getSelfUserId(), userTmp.getStationId(), userTmp.getUserType());
                } else {
                    dialogDis();
                    showToast(R.string.login_chat_fail);
                    onLoginFail();
                    return;
                }
                break;
            case GET_SYS_CONFIG:
                CrazyResult<Object> sysResult = response.result;
                if (sysResult == null) {
                    dialogDis();
                    showToast(R.string.get_sys_config_fail);
                    onLoginFail();
                    return;
                }
                if (!sysResult.crazySuccess) {
                    dialogDis();
                    String errorString = CommonUtils.parseResponseResult(sysResult.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.get_sys_config_fail) : errorString);
                    onLoginFail();
                    return;
                }
                ChatSysConfigBean chatSysConfigBean = (ChatSysConfigBean) sysResult.result;
                if (chatSysConfigBean.isSuccess()) {
                    String config = new Gson().toJson(chatSysConfigBean.getSource());
                    ChatSpUtils.instance(activity).setChatSysConfig(config);
                    iView.onLoginSuc(loginChatBean);
                } else {
                    dialogDis();
                    showToast(TextUtils.isEmpty(chatSysConfigBean.getMsg()) ? activity.getString(R.string.get_sys_config_fail) : chatSysConfigBean.getMsg());
                    onLoginFail();
                }
                break;
            case CHAT_ROOM_LIST_REQUEST:
                if (result == null) {
                    dialogDis();
                    showToast(R.string.get_chat_room_list_fail);
                    return;
                }
                if (!result.crazySuccess) {
                    dialogDis();
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.get_chat_room_list_fail) : errorString);
                    return;
                }

                ChatRoomListBean chatRoomListBean = (ChatRoomListBean) result.result;
                if (!chatRoomListBean.isSuccess()) {
                    dialogDis();
                    showToast(Utils.isEmptyString(chatRoomListBean.getMsg()) ? activity.getString(R.string.get_chat_room_list_fail)
                            : chatRoomListBean.getMsg());
                    return;
                }
                if (chatRoomListBean.getSource().getData().isEmpty()) {
                    dialogDis();
                    showToast(R.string.novalid_room);
                    return;
                }
                iView.onGetChatRoomList(chatRoomListBean);
                break;
            case JOIN_CHAT_ROOM_REQUEST:
                dialogDis();
                if (psdDialog != null && psdDialog.isShowing()) {
                    psdDialog.dismiss();
                    psdDialog = null;
                }
                if (result == null) {
                    showToast(R.string.join_chat_room_fail);
                    return;
                }
                if (!result.crazySuccess) {
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.join_chat_room_fail) : errorString);
                    return;
                }

                JoinChatRoomBean joinChatRoomBean = (JoinChatRoomBean) result.result;
                if (!joinChatRoomBean.isSuccess()) {
                    showToast(Utils.isEmptyString(joinChatRoomBean.getMsg()) ? activity.getString(R.string.join_chat_room_fail)
                            : joinChatRoomBean.getMsg());
                    return;
                }
                iView.onJoinChatRoom(joinChatRoomBean);
                break;
            case UPLOAD_FILE_IMAGE:
                if (result == null) {
                    iView.onUploadFail(MsgType.IMAGE);
                    showToast(R.string.up_load_fail);
                    return;
                }
                UpLoadFileBean upLoadFileBean = (UpLoadFileBean) result.result;
                if (!result.crazySuccess) {
                    iView.onUploadFail(MsgType.IMAGE);
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.up_load_fail) : errorString);
                    return;
                }
                if (!upLoadFileBean.isSuccess()) {
                    iView.onUploadFail(upLoadFileBean, MsgType.IMAGE);
                    showToast(R.string.up_load_fail);
                    return;
                }
                iView.onUploadSuccess(upLoadFileBean, MsgType.IMAGE);
                break;
            case UPLOAD_FILE_AUDIO:
                if (result == null) {
                    iView.onUploadFail(MsgType.AUDIO);
                    showToast(R.string.up_load_fail);
                    return;
                }
                UpLoadFileBean upLoadFileBean1 = (UpLoadFileBean) result.result;
                if (!result.crazySuccess) {
                    iView.onUploadFail(MsgType.AUDIO);
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.up_load_fail) : errorString);
                    return;
                }
                if (!upLoadFileBean1.isSuccess()) {
                    iView.onUploadFail(upLoadFileBean1, MsgType.AUDIO);
                    showToast(R.string.up_load_fail);
                    return;
                }
                iView.onUploadSuccess(upLoadFileBean1, MsgType.AUDIO);
                break;
            case SEND_TEXT_MSG_REQUEST:
                if (result == null) {
                    iView.onSendFail();
                    return;
                }
                SendMsgBean msgBean = (SendMsgBean) result.result;
                if (!result.crazySuccess) {
                    iView.onSendFail();
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.request_fail) : errorString);
                    return;
                }
                if (!msgBean.isSuccess()) {
                    if (msgBean.getCode() != null && msgBean.getCode().equals(ConfigCons.SEND_RED_PACKAGE)) {

                    } else {
                        iView.onSendFail();
                    }
                    String msg = msgBean.getMsg();
                    showToast(TextUtils.isEmpty(msg) ? activity.getResources().getString(R.string.request_fail) : msg);
                    return;
                }
                playSound(R.raw.send_msg, ConfigCons.SEND_MSG_SOUND);
                if (msgBean.getCode().equals(ConfigCons.SEND_RED_PACKAGE)) {
                    iView.onSendRedPackage(msgBean);
                } else {
                    iView.onSendSuccess(msgBean.getSource().getMsgUUID());
                }
                break;
            case GRAB_RED_PACKAGE:
                if (result == null) {
                    showToast(R.string.request_fail);
                    return;
                }
                GrabRedPackageBean packageBean = (GrabRedPackageBean) result.result;
                if (!result.crazySuccess) {
                    iView.onSendFail();
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.request_fail) : errorString);
                    return;
                }
                if (!packageBean.isSuccess()) {
                    showToast(TextUtils.isEmpty(packageBean.getMsg()) ? activity.getString(R.string.request_fail) : packageBean.getMsg());
                    return;
                }
                if ("se34".equals(packageBean.getSource().getStatus())) {
                    // 已领取过
                    iView.onGrabed(packageBean.getSource().getPayId());
                } else if ("se23".equals(packageBean.getSource().getStatus())) {
                    // 红包已经抢光了
                    iView.onGrabOut(packageBean.getSource().getPayId());
                } else {
                    iView.onGrabRedPackage(packageBean);
                }
                break;
            case RED_PACKAGE_DETAIL:
                if (result == null) {
                    showToast(R.string.request_fail);
                    return;
                }
                RedPackageDetailBean detailBean = (RedPackageDetailBean) result.result;
                if (!result.crazySuccess) {
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.request_fail) : errorString);
                    return;
                }
                if (!detailBean.isSuccess()) {
                    showToast(TextUtils.isEmpty(detailBean.getMsg()) ? activity.getString(R.string.request_fail) : detailBean.getMsg());
                    return;
                }
                iView.onRedPackageDetail(detailBean);
                break;
            case GET_LOTTERY_LIST:
                if (result == null) {
                    return;
                }
                ChatLotteryBean chatLotteryBean = (ChatLotteryBean) result.result;
                if (!result.crazySuccess) {
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.request_fail) : errorString);
                    return;
                }
                if (!chatLotteryBean.isSuccess()) {
                    showToast(TextUtils.isEmpty(chatLotteryBean.getMsg()) ? activity.getString(R.string.request_fail) : chatLotteryBean.getMsg());
                    return;
                }
                iView.onGetLotteryList(chatLotteryBean);
                break;
            case GET_NOTICE_REQUEST:
                if (result == null) {
                    return;
                }
                ChatRoomNoticeBean chatRoomNoticeBean = (ChatRoomNoticeBean) result.result;
                if (!result.crazySuccess) {
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.request_fail) : errorString);
                    return;
                }
                if (!chatRoomNoticeBean.isSuccess()) {
                    showToast(TextUtils.isEmpty(chatRoomNoticeBean.getMsg()) ? activity.getString(R.string.request_fail) : chatRoomNoticeBean.getMsg());
                    return;
                }
                iView.onGetNotice(chatRoomNoticeBean);
                break;
            case GET_ALL_LOTTERY_INFO:
                if(result != null){
                    AllLotteryInfoResponse response1 = (AllLotteryInfoResponse) result.result;
                    if(response1.isSuccess()){
                        lotteryInfos = response1.getContent();
                    }
                }
                break;
            case GET_LOTTERY_RESULT:
                if (result == null) {
                    return;
                }
                LotteryDownBean chatLotteryDetailBean = (LotteryDownBean) result.result;
                if (!result.crazySuccess) {
                    String errorString = CommonUtils.parseResponseResult(result.error);
//                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.request_fail) : errorString);
                    return;
                }
                iView.onGetLotteryDetail(chatLotteryDetailBean);
                break;
            case GET_LOTTERY_HISTORY_RESULT:
                if (result == null || lotteryInfos == null) {
                    showToast(R.string.lottery_history_result_failed);
                    return;
                }

                LotteryHistoryResultResponse response1 = (LotteryHistoryResultResponse) result.result;
                String cpType = "";
                for(AllLotteryInfoResponse.LotteryInfo info: lotteryInfos){
                    if(cpCodeOfClickedKaiJianResult.equals(info.getCode())){
                        cpType = info.getCzCode();
                        break;
                    }
                }

                if(!response1.isSuccess() || TextUtils.isEmpty(cpType)){
                    showToast(R.string.lottery_history_result_failed);
                    return;
                }

                iView.showLotteryHistoryResults(response1, cpCodeOfClickedKaiJianResult, cpType);
                break;
            case GET_HISTORY_MESSAGE:
                if (result == null) {
                    iView.onGetHistoryFail();
                    return;
                }
                ChatHistoryMessageBean chatHistoryMessageBean = (ChatHistoryMessageBean) result.result;
                if (!result.crazySuccess) {
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.request_fail) : errorString);
                    iView.onGetHistoryFail();
                    return;
                }
                if (!chatHistoryMessageBean.isSuccess()) {
                    showToast(TextUtils.isEmpty(chatHistoryMessageBean.getMsg()) ? activity.getString(R.string.request_fail) : chatHistoryMessageBean.getMsg());
                    return;
                }
                if (chatHistoryMessageBean.getSource() == null) {
                    return;
                }
                iView.onGetHistoryMessage(chatHistoryMessageBean);
                break;
            case FOLLOW_BET:
                if (result == null) {
                    iView.onSendFail();
                    return;
                }
                SendMsgBean sendMsgBean = (SendMsgBean) result.result;
                if (!result.crazySuccess) {
                    iView.onSendFail();
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.request_fail) : errorString);
                    return;
                }
                if (!sendMsgBean.isSuccess()) {
                    showToast(TextUtils.isEmpty(sendMsgBean.getMsg()) ? activity.getString(R.string.request_fail) : sendMsgBean.getMsg());
                    return;
                }
                if (sendMsgBean.getSource() != null && !sendMsgBean.getSource().isSuccess()) {
                    showToast(TextUtils.isEmpty(sendMsgBean.getSource().getMsg()) ? activity.getString(R.string.request_fail) : sendMsgBean.getSource().getMsg());
                    return;
                }
                showToast("跟单成功");
                break;
            case GET_VIOLATE_WORDS:
                if (result == null) {
                    return;
                }
                if (!result.crazySuccess) {
                    return;
                }
                ChatViolateWordsBean chatViolateWordsBean = (ChatViolateWordsBean) result.result;
                if (chatViolateWordsBean.isSuccess() && chatViolateWordsBean.getSource() != null) {
                    List<String> arr = new ArrayList<>();
                    for (int i = 0; i < chatViolateWordsBean.getSource().size(); i++) {
                        if (chatViolateWordsBean.getSource().get(i).getEnable() == 1) { //禁言词开启
                            arr.add(chatViolateWordsBean.getSource().get(i).getWord());
                        }
                    }
                    iView.onGetViolateWords(arr);
                }
                break;
        }
    }


    public void loadImageList(List<ChatMessage> chatMsgs) {
        imageList.clear();
        imagePosition.clear();
        int key = 0;
        int position = 0;
        for (ChatMessage chatMsg : chatMsgs) {
            if (chatMsg.getMsgType() == MsgType.IMAGE) {
                ImageMsgBody imageMsgBody = (ImageMsgBody) chatMsg.getBody();
                String record = imageMsgBody.getRecord();
                if (record == null) {
                    if (imageMsgBody.getPicMsg() != null) {
                        imageMsgBody = imageMsgBody.getPicMsg();
                    }
                    record = imageMsgBody.getRecord();
                }
                if (TextUtils.isEmpty(record)) {
                    imageList.add(imageMsgBody.getLocalPath() == null ? imageMsgBody.getThumbPath() : imageMsgBody.getLocalPath());
                } else {
                    StringBuilder urls = new StringBuilder();
                    if (record.startsWith("http")) {
                        urls.append(record);
                    } else {
                        urls.append(ConfigCons.CHAT_FILE_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.READ_FILE_WAP);
//                        urls.append("?fileId=" + record);
//                        urls.append("&contentType=image/jpeg");
                        urls.append("/" + record);
                    }
                    imageList.add(urls.toString());
                }
                imagePosition.put(key, position);
                position++;
            }
            key++;
        }
        iView.onGetChatImage(imageList, imagePosition);
    }

    /**
     * 连接Socket并注册监听
     */
    public void connectSocket() {
        if (socketManager != null) {
            socketManager.connectSocket();
        }
    }


    /**
     * 断开Socket连接并清除监听
     */
    public void disconnectSocket() {
        if (socketManager != null) {
            socketManager.disconnectSocket();
        }
    }

    private void onLoginFail() {
        activity.finish();
    }

    private void dialogDis() {
        if (logingDialog != null && logingDialog.isShowing()) {
            logingDialog.dismiss();
        }
    }


    //--------------------------------SocketIO监听-----------------------------------

    /**
     * Socket连接监听
     */
    private Emitter.Listener connectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.e("a", "on connect success:" + Arrays.toString(args));
            // 连接成功保存聊天室登陆需要的参数
            if (authorityBean != null) {
                ChatSpUtils.instance(activity).setENCRYPTED(authorityBean.getContent().getEncrypted());
                ChatSpUtils.instance(activity).setCLUSTER_ID(authorityBean.getContent().getClusterId());
                ChatSpUtils.instance(activity).setSIGN(authorityBean.getContent().getSign());
            }
            iView.onConnectSuccess(authorityBean);
        }
    };

    /**
     * Socket重连监听
     */
    private Emitter.Listener reconnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.e("a", "on reconnect success:" + Arrays.toString(args));
        }
    };

    /**
     * 聊天室登陆监听
     */
    private Emitter.Listener loginListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String json = AESUtils.decrypt(String.valueOf(args[0]), ConfigCons.DEFAULT_KEY, ConfigCons.DEFAULT_IV);
            LogUtils.e("a", "on login success:" + json);
//            if (!TextUtils.isEmpty(json)) {
//                LoginChatBean loginChatBean = new Gson().fromJson(json, LoginChatBean.class);
//                if (!loginChatBean.isSuccess()) {
//                    dialogDis();
//                    showToast(R.string.login_chat_fail);
//                    onLoginFail();
//                    return;
//                }
//                if (!TextUtils.isEmpty(loginChatBean.getDataKey())) {
//                    //登陆之后获取AES加密的KEY
//                    ConfigCons.DEFAULT_KEY = loginChatBean.getDataKey();
//                }
//                if (loginChatBean.getSource() != null) {
//                    LoginChatBean.SourceBean userTmp = loginChatBean.getSource();
//                    getSysConfig(userTmp.getSelfUserId(), userTmp.getStationId(), userTmp.getUserType());
//                } else {
//                    dialogDis();
//                    showToast(R.string.login_chat_fail);
//                    onLoginFail();
//                    return;
//                }
//            }
            Message message = Message.obtain();
            message.obj = json;
            message.what = LOGIN_CHATROOM_REQUEST;
            handler.sendMessage(message);
        }
    };

    /**
     * 发送消息监听
     */
    private Emitter.Listener sendListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            logingDialog.dismiss();
            String json = AESUtils.decrypt(String.valueOf(args[0]), ConfigCons.DATA_KEY, ConfigCons.DEFAULT_IV);
            Utils.logd("a", "on send success:" + json);
            if (!TextUtils.isEmpty(json) && !activity.isFinishing()) {
                BaseChatReceiveMsg baseChatReceiveMsg = null;
                try {
                    baseChatReceiveMsg = new Gson().fromJson(json, BaseChatReceiveMsg.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    try{
                        JSONObject jsonObject = new JSONObject(json);
                        if(ConfigCons.GET_AUDIT_LIST.equals(jsonObject.opt("code"))){
                            baseChatReceiveMsg = new BaseChatReceiveMsg();
                            baseChatReceiveMsg.setCode(ConfigCons.GET_AUDIT_LIST);
                        }else if(ConfigCons.GET_WINNING_LIST.equals(jsonObject.optString("code"))){
                            baseChatReceiveMsg = new BaseChatReceiveMsg();
                            baseChatReceiveMsg.setCode(ConfigCons.GET_WINNING_LIST);
                        }
                    }catch (JSONException exp){
                        exp.printStackTrace();
                    }
                }
                Message message = Message.obtain();
                message.obj = json;
                if (baseChatReceiveMsg == null || baseChatReceiveMsg.getCode() == null) {
                    return;
                }
                switch (baseChatReceiveMsg.getCode()) {
//                    public static final String LOGIN_AUTHORITY = "R70000";//登录并授权
//                    public static final String CHAT_ROOM_LIST = "R7023";//房间列表
//                    public static final String JOIN_CHAT_ROOM = "R7022";//进入房间
//                    public static final String SEND_MSG = "R7001";//发送文本消息
//                    public static final String SEND_IMAGE = "R7024";//发送图片消息
//                    public static final String SHARE_BET = "R7008";//分享投注
//                    public static final String FOLLOW_BET = "R7010";//跟单
//                    public static final String SEND_RED_PACKAGE = "R7009";//发红包
//                    public static final String GET_RED_PACKAGE = "R7027";//抢红包
//                    public static final String RED_PACKAGE_DETAIL = "R7032";//查看红包详情
//                    public static final String LOTTERY_LIST = "R7028";//拉取彩种列表和拉取指定彩种
//                    public static final String CHAT_ROOM_NOTICE = "R7025";//房间公告
//                    public static final String HISTORY_MESSAGE = "R7029";//历史消息
//                    public static final String PHOTO_LIST = "R7035";//头像列表
//                    public static final String MODIFY_PERSON_DATA = "R7034";//修改昵称和头像
//                    public static final String PERSON_DATA = "R7038";//修改昵称和头像
//                    public static final String GET_BET_HISTORY = "R7033";//获取历史投注信息
//                    public static final String GET_SYS_CONFIG = "R7031";//获取系统配置
//                    public static final String GET_VIOLATE_WORDS = "R7030";//获取禁言词
//                    public static final String UPDATE_PERSON_BET_DATA = "R7036";// 刷新并获取用户输赢
                    case ConfigCons.GET_SYS_CONFIG:
                        message.what = GET_SYS_CONFIG;
                        break;
                    case ConfigCons.CHAT_ROOM_LIST:
                        message.what = CHAT_ROOM_LIST_REQUEST;
                        break;
                    case ConfigCons.SEND_MSG:
                    case ConfigCons.SEND_IMAGE:
                    case ConfigCons.SHARE_BET:
                    case ConfigCons.SEND_RED_PACKAGE:
                        message.what = SEND_TEXT_MSG_REQUEST;
                        break;
                    case ConfigCons.GET_RED_PACKAGE:
                    case ConfigCons.GET_RED_PACKAGE_V2:
                        message.what = GRAB_RED_PACKAGE;
                        break;
                    case ConfigCons.RED_PACKAGE_DETAIL:
                        message.what = RED_PACKAGE_DETAIL;
                        break;
                    case ConfigCons.LOTTERY_LIST:
                        message.what = GET_LOTTERY_LIST;
                        break;
                    case ConfigCons.CHAT_ROOM_NOTICE:
                        message.what = GET_NOTICE_REQUEST;
                        break;
                    case ConfigCons.LOTTERY_LAST_RESULT_URL:
                        message.what = GET_LOTTERY_RESULT;
                        break;
                    case ConfigCons.HISTORY_MESSAGE:
                        message.what = GET_HISTORY_MESSAGE;
                        break;
                    case ConfigCons.FOLLOW_BET:
                        message.what = FOLLOW_BET;
                        break;
                    case ConfigCons.GET_VIOLATE_WORDS:
                        message.what = GET_VIOLATE_WORDS;
                        break;
                    case ConfigCons.JOIN_CHAT_ROOM:
                        message.what = JOIN_CHAT_ROOM_REQUEST;
                        break;
                    case ConfigCons.PERSON_DATA:
                        message.what = GET_PERSON_DATA;
                        break;
                    case ConfigCons.MODIFY_PERSON_DATA:
                        message.what = MODIFY_PERSON_DATA;
                        break;
                    case ConfigCons.PHOTO_LIST:
                        message.what = GET_PHOTO_LIST;
                        break;
                    case ConfigCons.GET_QUICK_MESSAGES:
                        message.what = GET_QUICK_MESSAGES;
                        break;
                    case ConfigCons.GET_SAVE_PICTURES:
                        message.what = SAVE_IMAGES;
                        break;
                    case ConfigCons.GET_ONLINE_USER:
                        message.what = GET_ONLINE_USER;
                        break;
                    case ConfigCons.GET_ONLINE_MANAGER:
                        message.what = GET_ONLINE_MANAGER;
                        break;
                    case ConfigCons.SHARE_DATA:
                        message.what = SHARE_DATA;
                        break;
                    case ConfigCons.GET_CHARGE:
                        message.what = GET_CHARGE;
                        break;
                    case ConfigCons.SIGN:
                        message.what = SIGN;
                        break;
                    case ConfigCons.GET_PRIVATE_CONVERSATION:
                        try{
                            JSONObject jsonObject = new JSONObject(json);
                            JSONObject source = jsonObject.getJSONObject("source");
                            if(source.getInt("type") == 4){
                                message.what = POST_CHAT_REMARK;
                            }else {
                                message.what = GET_PRIVATE_CONVERSATION;
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                    case ConfigCons.GET_PRIVATE_GROUP_HISTORY:
                        message.what = GET_PRIVATE_GROUP_HISTORY;
                        break;
                    case ConfigCons.GET_PRIVATE_SEND_MSG:
                        message.what = GET_PRIVATE_SEND_MSG;
                        break;
                    case ConfigCons.MASTER_PLAN:
                        message.what = MASTER_PLAN;
                        break;
                    case UserToolConstant.BAN_SPEAK_USER:
                        message.what = BAN_SPEAK_USER;
                        break;
                    case UserToolConstant.BAN_SPEAK_ROOM:
                        message.what = BAN_SPEAK_ROOM;
                        break;
                    case UserToolConstant.RETRACT_MSG:
                        message.what = RETRACT_MSG;
                        break;
                    case ConfigCons.GET_TOOL_PERMISSION:
                        message.what = GET_TOOL_PERMISSION;
                        break;
                    case ConfigCons.APPLY_FOR_BAN_SPEAK:
                    case ConfigCons.APPLY_FOR_BAN_SPEAK_2:
                        message.what = APPLY_FOR_BAN_SPEAK;
                        break;
                    case ConfigCons.GET_LONG_DRAGON:
                        message.what = GET_LONG_DRAGON;
                        break;
                    case ConfigCons.GET_AUDIT_LIST:
                        message.what = GET_AUDIT_LIST;
                        break;
                    case ConfigCons.GET_WINNING_LIST:
                        message.what = GET_TODAY_PROFIT;
                        break;
                }
                handler.sendMessage(message);
            } else {
                dialogDis();
            }
        }
    };

    /**
     * 切换和加入房间监听
     */
    private Emitter.Listener joinRoomListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (activity.isFinishing())
                return;
            String json = AESUtils.decrypt(String.valueOf(args[0]), ConfigCons.DEFAULT_KEY, ConfigCons.DEFAULT_IV);
            LogUtils.e("a", "on joinRoom success:" + json);
            Message message = Message.obtain();
            message.what = JOIN_CHAT_ROOM_SUCCESS;
            message.obj = json;
            handler.sendMessage(message);
        }
    };

    /**
     * 加入群组监听
     */
    private Emitter.Listener joinGroupListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.e("a", "on joinGroup success:" + Arrays.toString(args));
            if (activity.isFinishing()) {
                return;
            }
            String json = AESUtils.decrypt(String.valueOf(args[0]), ConfigCons.DEFAULT_KEY, ConfigCons.DEFAULT_IV);
            LogUtils.e("a", "on joinRoom success:" + json);
            Message message = Message.obtain();
            message.what = JOIN_PRIVATE_CHAT_ROOM_SUCCESS;
            message.obj = json;
            handler.sendMessage(message);

        }
    };
    private String passiveUserId;
    /**
     * Socket推送接收监听
     */
    private Emitter.Listener pushListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                if (!activity.isInRoom())
                    return;
                Gson gson = new Gson();
                String json = AESUtils.decrypt(String.valueOf(args[0]), ConfigCons.DEFAULT_KEY, ConfigCons.DEFAULT_IV);
                LogUtils.e("a", "接受到服务器房间广播的登录消息：" + json);
                JSONObject jsonObject = new JSONObject(json);
                ChatSendMsg chatSendMsg = gson.fromJson(jsonObject.getString("nativeMsg"), ChatSendMsg.class);
                String code = jsonObject.optString("code");
                String str = chatSendMsg.getMsgStr();
                boolean privateChat = false;
                boolean isPrivatePush = false;
                if (str != null) {
                    privateChat = new JSONObject(str).optBoolean("privateChat");
                    isPrivatePush = !TextUtils.isEmpty(code) && (code.equals(ConfigCons.GET_PRIVATE_SEND_MSG) || code.equals(ConfigCons.GET_PRIVATE_GROUP_HISTORY));
                    if (isPrivatePush || privateChat) {
                        //私聊发红包消息
                        if (code.equals(ConfigCons.SEND_RED_PACKAGE) || code.equals(ConfigCons.SEND_RED_PACKAGE_WAP) ||
                                code.equals(ConfigCons.SEND_RED_PACKAGE_PC)) {
                            JSONObject obj = new JSONObject(str);
                            String userId = obj.optString("inputUserId");
                            chatSendMsg.setUserId(userId);
                        } else {
                            //私聊非红包用的是fromUser里面的id
                            //私聊非红包消息
                            ChatPrivateReceiveMsgBean chatPrivateReceiveMsgBean = gson.fromJson(str, ChatPrivateReceiveMsgBean.class);
                            if (chatPrivateReceiveMsgBean.getFromUser() != null && chatPrivateReceiveMsgBean.getFromUser().getId() != null) {
                                chatSendMsg.setUserId(chatPrivateReceiveMsgBean.getFromUser().getId());
                            }
                        }
                    }
                }

                if (chatSendMsg.getMsgType() == 21) {
                    //房间禁言
                    JSONObject object = new JSONObject(str);
                    String isBanSpeak = object.optString("isBanSpeak");
                    iView.onBanRoomSpeak(isBanSpeak.equals("1"));
                }

                if (chatSendMsg.getMsgType() == 31) {
                    //撤回消息
                    JSONObject object1 = new JSONObject(str);
                    String msgId1 = object1.optString("msgId");
                    iView.onMessageRecall(msgId1);
                }

                if (chatSendMsg.getUserId() != null && chatSendMsg.getUserId().equals(ChatSpUtils.instance(activity).getUserId())) {
                    // 屏蔽自己发送的消息
                    switch (chatSendMsg.getMsgType()) {
                        case 33: {
                            //私聊自己发消息
                            JSONObject obj = new JSONObject(str);
                            String msgUUID = obj.optString("msgUUID");
                            String userMessageId = obj.optString("userMessageId");
                            iView.onMessageSendSelf(userMessageId, msgUUID);
                            break;
                        }
                        case 16: {
                            //踢出房间
                            iView.onForceOffline();
                            break;
                        }
                        case 17:
                            //解禁言事件
                            JSONObject obj = new JSONObject(str);
                            iView.onBanSpeak(obj.getString("speakingClose"), chatSendMsg.getUserId(),
                                    chatSysConfig.getSwitch_ban_speak_del_msg().equals("1"), chatSendMsg.getMsgType(), obj.optString("message"));
                            break;
                        case 22:
                            //申诉失败
                            String nativeMsg = jsonObject.optString("nativeMsg");
                            String wapMsgStr = new JSONObject(nativeMsg).optString("wapMsgStr");
                            JSONObject object = new JSONObject(wapMsgStr);
                            iView.applyFail(object.optString("msg"));
                            break;
                        case 2://图片
                        case 11://语音
                        case 1://文本
                        case 5://分享注单消息
                        case 8://跟注消息
                        case 9://分享今日盈亏
                            //自己发消息
                            JSONObject myChatObj = new JSONObject(str);
                            String msgId = myChatObj.optString("msgId");
                            String msgUUID = myChatObj.optString("msgUUID");
                            iView.onMessageSendSelf(msgId, msgUUID);
                            break;
                    }
                    return;
                } else if (chatSendMsg.getUserId() != null && !chatSendMsg.getUserId().equals(ChatSpUtils.instance(activity).getUserId())) {
                    switch (chatSendMsg.getMsgType()) {
                        case 35:
                            //禁言解禁言，删除其他人的消息
                            JSONObject obj = new JSONObject(str);
                            iView.onBanSpeak(obj.getString("speakingClose"), chatSendMsg.getUserId(), chatSysConfig.getSwitch_ban_speak_del_msg().equals("1"), chatSendMsg.getMsgType(), obj.optString("message"));
                            break;
                    }
                }

                if (chatSendMsg.getMsgType() == 23) {
                    // 后台配置更改
                    ChatSpUtils.instance(activity).setChatSysConfig(jsonObject.getString("hashMap"));
                    iView.onUpdateConfig();
                    return;
                }
                if (chatSendMsg.getMsgType() == 14 && chatSysConfig.getSwitch_winning_banner_show().equals("1")
                        && (CommonUtils.isForeground(activity, "ChatMainActivity")
                        || CommonUtils.isForeground(activity, "BetForChatActivity")
                        || CommonUtils.isForeground(activity, "ChatBetHistoryActivity"))) {
                    // 中奖消息
                    iView.onReceiverWinningMsg(chatSendMsg);
                    return;
                }
                if (jsonObject.optString("code").equals("Welcome")
                        && ChatSpUtils.instance(activity).getNoticeRoom().equals("1")
                        && (CommonUtils.isForeground(activity, "ChatMainActivity")
                        || CommonUtils.isForeground(activity, "BetForChatActivity")
                        || CommonUtils.isForeground(activity, "ChatBetHistoryActivity")
                )) {
                    // 进房通知
                    String nativeMsg = jsonObject.getString("nativeMsg");
                    JSONObject nativeObj = new JSONObject(nativeMsg);
                    String msgStr = nativeObj.getString("msgStr");
                    JSONObject msgObj = new JSONObject(msgStr);
                    String nickName = msgObj.getString("nickName");
                    iView.onJoinRoomNotice(nickName);
                    return;
                }
                String msgStr = str;
                if (!TextUtils.isEmpty(msgStr)) {
                    String uuid = new Gson().fromJson(msgStr, MsgBody.class).getMsgUUID();
                    if (!TextUtils.isEmpty(ChatMainPresenter.msgUUID) && ChatMainPresenter.msgUUID.equals(uuid)) {
                        return;
                    }
                    msgUUID = uuid;
                }
                if (chatSendMsg.getMsgType() == 3 &&
                        (TextUtils.isEmpty(ChatSpUtils.instance(activity).getReceiveRedPacket()) || !ChatSpUtils.instance(activity).getReceiveRedPacket().equals("1"))) {
                    // 禁止红包消息
                    return;
                }
                if (ChatSpUtils.instance(activity).getNoticeMessage().equals("1")
                        && !jsonObject.optString("code").equals("Welcome")
                        && !CommonUtils.isForeground(activity, "ChatMainActivity")
                        && !CommonUtils.isForeground(activity, "BetForChatActivity")
                        && !CommonUtils.isForeground(activity, "ChatBetHistoryActivity")
                        && !CommonUtils.isForeground(activity, "ChatWinningListDialogActivity")) {
                    // 接收通知的条件：1、聊天室页面退出或者处于后台  2、处于后台时前台页面不是BetForChatActivity
                    // 3、处于后台时前台页面不是ChatBetHistoryActivity 4、后台通知开关必须打开
                    // 5、聊天室通知设置开关必须打开

                    ChatSendMsg.MsgResultBean.UserEntityBean userEntity = chatSendMsg.getMsgResult().getUserEntity();
                    String roomName;
                    String title;
                    String content;
                    String roomId;
                    if (chatSendMsg.getMsgType() == 33) {
                        String fromUser = new JSONObject(str).optString("fromUser");
                        JSONObject object = new JSONObject(fromUser);
                        roomName = TextUtils.isEmpty(object.optString("nickName")) ? object.optString("account") : object.optString("nickName");
                        roomId = new JSONObject(str).optString("roomId").split("_")[0];
                    } else {
                        roomName = chatSendMsg.getRoomName();
                        roomId = chatSendMsg.getRoomId().split("_")[0];
                    }

                    switch (chatSendMsg.getMsgType()) {
                        case 33:
                            //私聊
                            passiveUserId = chatSendMsg.getUserId();
                            title = TextUtils.isEmpty(userEntity.getNickName()) ? userEntity.getAccount() : userEntity.getNickName();
                            content = "私聊消息";
                            sendNotification(roomName, title, content, roomId);
                            break;
                        case 0:
                            // 系统消息
                            break;
                        case 1:
                            // 文字消息
                            TextMsgBody msgBody = new Gson().fromJson(msgStr, TextMsgBody.class);
                            title = TextUtils.isEmpty(userEntity.getNickName()) ? userEntity.getAccount() : userEntity.getNickName();
                            content = msgBody.getRecord();
                            sendNotification(roomName, title, content, roomId);
                            break;
                        case 2:
                            // 图片消息
                            ImageMsgBody imageMsgBody = new Gson().fromJson(msgStr, ImageMsgBody.class);
                            title = TextUtils.isEmpty(userEntity.getNickName()) ? userEntity.getAccount() : userEntity.getNickName();
                            content = "图片消息";
                            sendNotification(roomName, title, content, roomId);
                            break;
                        case 3:
                            //红包消息
                            RedPackageMsgBody packageMsgBody = new Gson().fromJson(msgStr, RedPackageMsgBody.class);
                            title = TextUtils.isEmpty(userEntity.getNickName()) ? userEntity.getAccount() : userEntity.getNickName();
                            content = "红包消息";
                            sendNotification(roomName, title, content, roomId);
                            break;
                        case 5:
                        case 8:
                            //注单消息
//                            BetSlipMsgBody betSlipMsgBody = new Gson().fromJson(msgStr, BetSlipMsgBody.class);
                            title = TextUtils.isEmpty(userEntity.getNickName()) ? userEntity.getAccount() : userEntity.getNickName();
                            content = "注单消息";
                            sendNotification(roomName, title, content, roomId);
                            break;
                        case 6:
                            // 计划消息
                            title = TextUtils.isEmpty(userEntity.getNickName()) ? userEntity.getAccount() : userEntity.getNickName();
                            content = "计划消息";
                            sendNotification(roomName, title, content, roomId);
                            break;
                        case 7:
                        case 20:
                            // 机器人消息
                            TextMsgBody textMsgBody = new Gson().fromJson(msgStr, TextMsgBody.class);
                            title = TextUtils.isEmpty(userEntity.getNickName()) ? userEntity.getAccount() : userEntity.getNickName();
                            String record = textMsgBody.getRecord();
                            if (record.contains("<img") || record.contains("<p>")) {
                                content = "富文本消息！";
                            } else {
                                content = record;
                            }
                            sendNotification(roomName, title, content, roomId);
                            break;
                    }
                    if (activity.isFinishing())
                        return;
                }
                if (chatSendMsg.getMsgType() != 14 && chatSendMsg.getMsgType() != 13) {
                    iView.onReceiverMsg(chatSendMsg, jsonObject.getString("source"), isPrivatePush || privateChat);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public void sendNotification(String name, String title, String content, String roomId) {
        ++NOTIFY_ID;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, ConfigCons.CHANNEL_ID);
        builder.setContentTitle("房间:" + name);
        builder.setContentText(title + ":" + content);
        builder.setSmallIcon(R.drawable.icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.drawable.icon));
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setAutoCancel(true);
//        Intent notifyIntent = new Intent(activity, ChatMainActivity.class);
//        notifyIntent.putExtra("roomId", roomId);
//        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(activity, NOTIFY_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent notifyIntent = new Intent(activity, ChatMainActivity.class);
        if (content.equals("私聊消息")) {
            notifyIntent.putExtra("isPrivate", true);
            notifyIntent.putExtra("passiveUserId", passiveUserId);
        } else {
            notifyIntent.putExtra("isPrivate", false);
        }
        notifyIntent.putExtra("roomName", name);

        notifyIntent.putExtra("roomId", roomId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);
        stackBuilder.addParentStack(ChatMainActivity.class);
        stackBuilder.addNextIntent(notifyIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFY_ID, builder.build());
        playSound(R.raw.notify_msg, ConfigCons.NOTIFY_SOUND);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private Emitter.Listener connectErrorListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (socketManager != null) {
                socketManager.reconnectSocket();
            }
            LogUtils.e("a", "Socket.EVENT_CONNECT_ERROR");
        }
    };

    private Emitter.Listener connectTimeoutListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (socketManager != null) {
                socketManager.reconnectSocket();
            }
            LogUtils.e("a", "Socket.EVENT_CONNECT_TIMEOUT");
        }
    };

    private Emitter.Listener pingListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.e("a", "Socket.EVENT_PING");
            iView.onPingEvent();
        }
    };

    private Emitter.Listener pongListner = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.e("a", "Socket.EVENT_PONG");
        }
    };

    private Emitter.Listener msgListner = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.e("a", "-----------接受到消息啦--------" + Arrays.toString(args));
        }
    };

    private Emitter.Listener disconnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.e("a", "客户端断开连接啦。。。");
            if (socketManager != null) {
                socketManager.reconnectSocket();
            }
        }
    };

    /**
     * 播放声音
     */
    public void playSound(int soundRes, String type) {
        switch (type) {
            case ConfigCons.SEND_MSG_SOUND:
                if (TextUtils.isEmpty(ChatSpUtils.instance(activity).getNoticeSendVoice()) || !ChatSpUtils.instance(activity).getNoticeSendVoice().equals("1")) {
                    return;
                }
                break;
            case ConfigCons.NOTIFY_SOUND:
                if (TextUtils.isEmpty(ChatSpUtils.instance(activity).getNoticeRecieveVoice()) || !ChatSpUtils.instance(activity).getNoticeRecieveVoice().equals("1")) {
                    return;
                }
                break;
        }
        if (soundPool == null) {
            //第一个参数是可以支持的声音数量，第二个是声音类型，第三个是声音品质
            soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        }
        final int load = soundPool.load(activity, soundRes, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                //第一个参数id，第二个和第三个参数为左右声道，第四个参数为优先级，第五个是否循环播放，0不循环，-1循环
                //最后一个参数播放比率，范围0.5到2，通常为1表示正常播放
                soundPool.play(load, 1, 1, 0, 0, 1);
            }
        });
    }

    public void savePermissions(JoinChatRoomBean.SourceBean.PermissionObjBean permissionObj) {
        ChatSpUtils.instance(activity).setSendBetting(permissionObj.getSEND_BETTING());
        ChatSpUtils.instance(activity).setSendExpression(permissionObj.getSEND_EXPRESSION());
        ChatSpUtils.instance(activity).setSendImage(permissionObj.getSEND_IMAGE());
        ChatSpUtils.instance(activity).setSendAudio(permissionObj.getSEND_AUDIO());
        ChatSpUtils.instance(activity).setSendRedPacket(permissionObj.getSEND_RED_PACKET());
        ChatSpUtils.instance(activity).setSendText(permissionObj.getSEND_TEXT());
        ChatSpUtils.instance(activity).setReceiveRedPacket(permissionObj.getRECEIVE_RED_PACKET());
        ChatSpUtils.instance(activity).setEnterRoom(permissionObj.getENTER_ROOM());
        ChatSpUtils.instance(activity).setFastTalk(permissionObj.getFAST_TALK());
    }

    public void getPersonData(String roomId, boolean isRefresh) {
        this.isRefresh = isRefresh;
        PersonDataModel personDataModel = new PersonDataModel(ConfigCons.PERSON_DATA, ConfigCons.SOURCE, ChatSpUtils.instance(activity).getUserId(), roomId);
        String bodyStr = new Gson().toJson(personDataModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }

    /**
     * 1、获取头像列表
     *
     * @param stationId
     */
    public void getPhotoList(String stationId, String userId) {
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
        PhotoListModel jsonModel = new PhotoListModel(ConfigCons.PHOTO_LIST, ConfigCons.SOURCE, stationId, userId);
        String bodyStr = new Gson().toJson(jsonModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, false, "");
    }


    /**
     * 收藏图片
     *
     * @param chatCollectionImagesModel
     */
    public void saveImages(ChatCollectionImagesModel chatCollectionImagesModel) {
        chatCollectionImagesModel.setUserId(ChatSpUtils.instance(activity).getUserId());
        chatCollectionImagesModel.setCode(ConfigCons.GET_SAVE_PICTURES);
        chatCollectionImagesModel.setSource(ConfigCons.SOURCE);
        String bodyStr = new Gson().toJson(chatCollectionImagesModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }


    public void getCharge() {
        ChatBaseModel chatBaseModel = new ChatBaseModel();
        chatBaseModel.setCode(ConfigCons.GET_CHARGE);
        chatBaseModel.setUserId(ChatSpUtils.instance(activity).getUserId());
        String bodyStr = new Gson().toJson(chatBaseModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }

    public void onClickTodayProfit(){
        try{
            JSONObject obj = new JSONObject();
            obj.put("code", ConfigCons.GET_WINNING_LIST);
            obj.put("source", ConfigCons.SOURCE);
            obj.put("stationId", ChatSpUtils.instance(context).getStationId());
            obj.put("prizeType", "4");
            String bodyStr = obj.toString();
            /*---------Socket发送------------*/
            sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * 签到
     */
    public void sign() {
        ChatBaseModel chatBaseModel = new ChatBaseModel();
        chatBaseModel.setCode(ConfigCons.SIGN);
        chatBaseModel.setUserId(ChatSpUtils.instance(activity).getUserId());
        chatBaseModel.setRequestIp(ChatSpUtils.instance(context).getBET_IP());
        String bodyStr = new Gson().toJson(chatBaseModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }

    /**
     * 导师计划
     *
     * @param roomId
     */
    public void getMasterPlan(String roomId) {
        MasterPlanModel masterPlanModel = new MasterPlanModel();
        masterPlanModel.setCode(ConfigCons.MASTER_PLAN);
        masterPlanModel.setSource(ConfigCons.SOURCE);
        masterPlanModel.setRoomId(roomId);
        String bodyStr = new Gson().toJson(masterPlanModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }

    /**
     * 私聊 --2020年01月22日17:35:45 开发中
     *
     * @param userType
     * @param nickName
     * @param roomId
     * @param type
     */
    public void getPrivateConversation(String passiveUserId, int userType, @Nullable String nickName, @Nullable String roomId, String type) {
        ChatPrivateJsonModel model = new ChatPrivateJsonModel();
        model.setType(type);
        model.setPassiveUserId(passiveUserId);
        model.setUserId(ChatSpUtils.instance(context).getUserId());
        model.setRoomId(roomId);
        model.setCode(ConfigCons.GET_PRIVATE_CONVERSATION);
        model.setSource(ConfigCons.SOURCE);
        model.setStationId(ChatSpUtils.instance(context).getStationId());
        model.setUserType(userType);
        model.setpNickName(nickName);
        String bodyStr = new Gson().toJson(model);
        /*-----------发送----------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }

    public void postPrivateChatRemark(String userId, String pId, String remark, String stationId){
        JSONObject obj = new JSONObject();

        try{
            obj.put("code", ConfigCons.GET_PRIVATE_CONVERSATION);
            obj.put("source", "app");
            obj.put("type", 4);
            obj.put("userId", userId);
            obj.put("passiveUserId", pId);
            obj.put("remarks", remark);
            obj.put("stationId", stationId);
        }catch (JSONException e){
            e.printStackTrace();
        }

        chatRemarkObj = obj;
        sendSocket(ConfigCons.USER_R, obj.toString(), false, true, "");
    }


    /**
     * 禁言
     *
     * @param item
     * @param unMuteOrMute
     * @param roomId
     */
    public void mute(ChatMessage item, int unMuteOrMute, String roomId) {
        ChatMuteJsonModel jsonModel = new ChatMuteJsonModel();
        jsonModel.setCode(UserToolConstant.BAN_SPEAK_USER);
        jsonModel.setSource(ConfigCons.SOURCE);
        jsonModel.setOperatorAcount(item.getUserAccount());
        jsonModel.setSpeakingClose(unMuteOrMute);
        jsonModel.setSpeakingCloseId(item.getBody().getUserId());
        jsonModel.setRoomId(roomId);
        String bodyStr = new Gson().toJson(jsonModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }


    /**
     * 全体禁言
     */
    public void muteAll(int isBanSpeak, String roomId) {
        ChatMuteAllJsonModel jsonModel = new ChatMuteAllJsonModel();
        jsonModel.setCode(UserToolConstant.BAN_SPEAK_ROOM);
        jsonModel.setSource(ConfigCons.SOURCE);
        jsonModel.setIsBanSpeak(isBanSpeak);
        jsonModel.setRoomId(roomId);
        jsonModel.setStationId(ChatSpUtils.instance(context).getStationId());
        String bodyStr = new Gson().toJson(jsonModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }


    /**
     * 撤回
     */
    public void recall(ChatMessage item, String roomId) {
        ChatRecallJsonModel jsonModel = new ChatRecallJsonModel();
        jsonModel.setSource(ConfigCons.SOURCE);
        jsonModel.setCode(UserToolConstant.RETRACT_MSG);
        jsonModel.setChannelId("android");
        jsonModel.setMsgId(item.getBody().getMsgId() == null ? item.getMsgId() : item.getBody().getMsgId());
        jsonModel.setUserType(item.getUserType());
        jsonModel.setRoomId(roomId);
        jsonModel.setStationId(ChatSpUtils.instance(context).getStationId());
        String bodyStr = new Gson().toJson(jsonModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }

    /**
     * 获取权限工具箱
     */
    public void getToolPermission(String userId) {
        ChatToolPermissionJsonModel jsonModel = new ChatToolPermissionJsonModel();
        jsonModel.setSource(ConfigCons.SOURCE);
        jsonModel.setCode(ConfigCons.GET_TOOL_PERMISSION);
        jsonModel.setUserId(userId);
        String bodyStr = new Gson().toJson(jsonModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }


    /**
     * 1.查询私聊历史记录
     * 2.发送私聊消息
     */
    public void getHistoryMessages(String passiveUserId, String roomId, int type, String userId, String record, int msgType) {
        ChatPrivateHistoryJsonModel jsonModel = new ChatPrivateHistoryJsonModel();
        jsonModel.setSource(ConfigCons.SOURCE);
        jsonModel.setRecord(record);
        jsonModel.setCount(30);
        jsonModel.setStart(0);
        jsonModel.setCode(ConfigCons.GET_PRIVATE_GROUP_HISTORY);
        jsonModel.setPassiveUserId(passiveUserId);
        jsonModel.setUserId(userId);
        jsonModel.setRoomId(roomId);
        jsonModel.setType(type);
        jsonModel.setMsgType(msgType);
        jsonModel.setStationId(ChatSpUtils.instance(context).getStationId());
        String bodyStr = new Gson().toJson(jsonModel);
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");

    }


    /**
     * 获取长龙数据
     *
     * @param stationId
     */
    public void getLongDragon(String stationId) {
        logingDialog.updateTitle("加载中...");
        ChatLongDragonJsonModel jsonModel = new ChatLongDragonJsonModel(stationId);
        jsonModel.setCode(ConfigCons.GET_LONG_DRAGON);
        String bodyStr = new Gson().toJson(jsonModel);
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }

    /**
     * 使用者是前台管理员的话可以取得审核名单
     */
    public void getAuditList(){
        logingDialog.updateTitle("加载中...");
        AuditListModel model = new AuditListModel();
        model.setCode(ConfigCons.GET_AUDIT_LIST);
        model.setRoomId(oldRoomId);
        model.setUserId(loginChatBean.getSource().getSelfUserId());
        model.setType("1");
        String bodyStr = new Gson().toJson(model);
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }

    public void postAuditAction(String auditUserID, int action){
        AuditRequestModel model = new AuditRequestModel();
        model.setCode(ConfigCons.GET_AUDIT_LIST);
        model.setRoomId(oldRoomId);
        model.setAuditUserId(auditUserID);
        model.setType("2");
        model.setAuditStatus(action);
        String bodyStr = new Gson().toJson(model);
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
    }
}
