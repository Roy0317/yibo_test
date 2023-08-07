package com.example.anuo.immodule.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.ChatMainActivity;
import com.example.anuo.immodule.bean.AudioMsgBody;
import com.example.anuo.immodule.bean.BetSlipMsgBody;
import com.example.anuo.immodule.bean.ChatMessage;
import com.example.anuo.immodule.bean.ImageMsgBody;
import com.example.anuo.immodule.bean.MsgSendStatus;
import com.example.anuo.immodule.bean.MsgType;
import com.example.anuo.immodule.bean.PersonDataBean;
import com.example.anuo.immodule.bean.PlanMsgBody;
import com.example.anuo.immodule.bean.RedPackageMsgBody;
import com.example.anuo.immodule.bean.SysTextMsgBody;
import com.example.anuo.immodule.bean.TextMsgBody;
import com.example.anuo.immodule.bean.VideoMsgBody;
import com.example.anuo.immodule.bean.base.BetInfo;
import com.example.anuo.immodule.bean.base.FileMsgBody;
import com.example.anuo.immodule.bean.base.MsgBody;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.ChatSysConfig;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.utils.GlideUtils;
import com.example.anuo.immodule.utils.LogUtils;
import com.google.gson.Gson;
import com.simon.utils.DisplayUtil;
import com.simon.view.webview.BridgeWebView;
import com.simon.view.webview.WebUtil;

import java.io.File;
import java.util.List;

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
 * Date  :2019/6/6
 * Desc  :com.example.anuo.immodule.adapter
 */
public class ChatAdapter extends BaseQuickAdapter<ChatMessage, BaseViewHolder> {
    private static final int TYPE_SEND_TEXT = 1;
    private static final int TYPE_RECEIVE_TEXT = 2;
    private static final int TYPE_SEND_IMAGE = 3;
    private static final int TYPE_RECEIVE_IMAGE = 4;
    private static final int TYPE_SEND_VIDEO = 5;
    private static final int TYPE_RECEIVE_VIDEO = 6;
    private static final int TYPE_SEND_FILE = 7;
    private static final int TYPE_RECEIVE_FILE = 8;
    private static final int TYPE_SEND_AUDIO = 9;
    private static final int TYPE_RECEIVE_AUDIO = 10;
    private static final int TYPE_SEND_SYS_PLAN = 11;
    private static final int TYPE_RECEIVE_SYS_PLAN = 12;
    private static final int TYPE_SEND_BET_SLIP = 13;
    private static final int TYPE_RECEIVE_BET_SLIP = 14;
    private static final int TYPE_SEND_SYS_TEXT = 15;
    private static final int TYPE_RECEIVE_SYS_TEXT = 16;
    private static final int TYPE_SEND_RED_PACKAGE = 17;
    private static final int TYPE_RECEIVE_RED_PACKAGE = 18;
    private static final int TYPE_SEND_HTML = 19;
    private static final int TYPE_RECEIVE_HTML = 20;
    private static final int TYPE_TIME = 99;
    private static final int TYPE_SEND_SHARE_DATA = 21;
    private static final int TYPE_RECEIVE_SHARE_DATA = 22;
    private static final int TYPE_SEND_BET_SLIP_LIST = 23;
    private static final int TYPE_RECEIVE_BET_SLIP_LIST = 24;

    private static final int SEND_TEXT = R.layout.item_text_send;
    private static final int RECEIVE_TEXT = R.layout.item_text_receive;
    private static final int SEND_IMAGE = R.layout.item_image_send;
    private static final int RECEIVE_IMAGE = R.layout.item_image_receive;
    private static final int SEND_VIDEO = R.layout.item_video_send;
    private static final int RECEIVE_VIDEO = R.layout.item_video_receive;
    private static final int SEND_FILE = R.layout.item_file_send;
    private static final int RECEIVE_FILE = R.layout.item_file_receive;
    private static final int SEND_AUDIO = R.layout.item_audio_send;
    private static final int RECEIVE_AUDIO = R.layout.item_audio_receive;
    private static final int SEND_SYS_PLAN = R.layout.item_sys_plan_send;
    private static final int RECEIVE_SYS_PLAN = R.layout.item_sys_plan_receive;
    private static final int SEND_BET_SLIP = R.layout.item_bet_slip_send;
    private static final int RECEIVE_BET_SLIP = R.layout.item_bet_slip_receive;
    private static final int SEND_BET_SLIP_LIST = R.layout.item_bet_slip_list_send;
    private static final int RECEIVE_BET_SLIP_LIST = R.layout.item_bet_slip_list_receive;
    //    private static final int SEND_LOCATION = R.layout.item_location_send;
//    private static final int RECEIVE_LOCATION = R.layout.item_location_receive;
    private static final int SEND_RED_PACKAGE = R.layout.item_red_package_send;
    private static final int RECEIVE_RED_PACKAGE = R.layout.item_red_package_receive;
    public static final int SEND_HTML = R.layout.item_html_send;
    public static final int REVEIVE_HTML = R.layout.item_html_receive;
    public static final int TIME = R.layout.item_msg_time;
    public static final int SEND_PERSON_DATA = R.layout.item_person_data_send;
    public static final int RECEIVE_PERSON_DATA = R.layout.item_person_data_receive;

    private Context context;

    private Animation loadingAnimation = null;
    private ChatSysConfig chatSysConfig;
    private ChatSpUtils chatSpUtils;

    private boolean isHistoryMsg = false; //是否是历史信息
    private String mentorName;

    public ChatAdapter(final Context context, List<ChatMessage> data) {
        super(data);
        this.context = context;
        chatSpUtils = ChatSpUtils.instance(context);
//        chatSysConfig = ChatSpUtils.instance(context).getChatSysConfig();
        setMultiTypeDelegate(new MultiTypeDelegate<ChatMessage>() {
            @Override
            protected int getItemType(ChatMessage entity) {
                boolean isSend = entity.getSenderId().equals(ChatMainActivity.mSenderId);
                if (MsgType.TEXT == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_TEXT : TYPE_RECEIVE_TEXT;
                } else if (MsgType.IMAGE == entity.getMsgType()) {
                    if (isSend) {
                        loadingAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_loading_img);
                    }
                    return isSend ? TYPE_SEND_IMAGE : TYPE_RECEIVE_IMAGE;
                } else if (MsgType.VIDEO == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_VIDEO : TYPE_RECEIVE_VIDEO;
                } else if (MsgType.FILE == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_FILE : TYPE_RECEIVE_FILE;
                } else if (MsgType.AUDIO == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_AUDIO : TYPE_RECEIVE_AUDIO;
                } else if (MsgType.SYS_PLAN == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_SYS_PLAN : TYPE_RECEIVE_SYS_PLAN;
                } else if (MsgType.BET_SLIP == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_BET_SLIP : TYPE_RECEIVE_BET_SLIP;
                } else if (MsgType.SYS_TEXT == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_SYS_TEXT : TYPE_RECEIVE_SYS_TEXT;
                } else if (MsgType.RED_PACKAGE == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_RED_PACKAGE : TYPE_RECEIVE_RED_PACKAGE;
                } else if (MsgType.HTML == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_HTML : TYPE_RECEIVE_HTML;
                } else if (MsgType.MSG_TIME == entity.getMsgType()) {
                    return TYPE_TIME;
                } else if (MsgType.SHARE_DATA == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_SHARE_DATA : TYPE_RECEIVE_SHARE_DATA;
                } else if (MsgType.BET_SLIP_LIST == entity.getMsgType()) {
                    return isSend ? TYPE_SEND_BET_SLIP_LIST : TYPE_RECEIVE_BET_SLIP_LIST;
                }
                return 0;
            }
        });
        getMultiTypeDelegate().registerItemType(TYPE_SEND_TEXT, SEND_TEXT)
                .registerItemType(TYPE_RECEIVE_TEXT, RECEIVE_TEXT)
                .registerItemType(TYPE_SEND_IMAGE, SEND_IMAGE)
                .registerItemType(TYPE_RECEIVE_IMAGE, RECEIVE_IMAGE)
                .registerItemType(TYPE_SEND_VIDEO, SEND_VIDEO)
                .registerItemType(TYPE_RECEIVE_VIDEO, RECEIVE_VIDEO)
                .registerItemType(TYPE_SEND_FILE, SEND_FILE)
                .registerItemType(TYPE_RECEIVE_FILE, RECEIVE_FILE)
                .registerItemType(TYPE_SEND_AUDIO, SEND_AUDIO)
                .registerItemType(TYPE_RECEIVE_AUDIO, RECEIVE_AUDIO)
                .registerItemType(TYPE_SEND_SYS_PLAN, SEND_SYS_PLAN)
                .registerItemType(TYPE_RECEIVE_SYS_PLAN, RECEIVE_SYS_PLAN)
                .registerItemType(TYPE_SEND_BET_SLIP, SEND_BET_SLIP)
                .registerItemType(TYPE_RECEIVE_BET_SLIP, RECEIVE_BET_SLIP)
                .registerItemType(TYPE_SEND_SYS_TEXT, SEND_TEXT)
                .registerItemType(TYPE_RECEIVE_SYS_TEXT, RECEIVE_TEXT)
                .registerItemType(TYPE_SEND_RED_PACKAGE, SEND_RED_PACKAGE)
                .registerItemType(TYPE_RECEIVE_RED_PACKAGE, RECEIVE_RED_PACKAGE)
                .registerItemType(TYPE_SEND_HTML, SEND_HTML)
                .registerItemType(TYPE_RECEIVE_HTML, REVEIVE_HTML)
                .registerItemType(TYPE_TIME, TIME)
                .registerItemType(TYPE_SEND_SHARE_DATA, SEND_PERSON_DATA)
                .registerItemType(TYPE_RECEIVE_SHARE_DATA, RECEIVE_PERSON_DATA)
                .registerItemType(TYPE_SEND_BET_SLIP_LIST, SEND_BET_SLIP_LIST)
                .registerItemType(TYPE_RECEIVE_BET_SLIP_LIST, RECEIVE_BET_SLIP_LIST);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ChatMessage item) {
        if (chatSysConfig == null) {
            chatSysConfig = ChatSpUtils.instance(context).getChatSysConfig();
        }
        setContent(helper, item);
        if (item.getMsgType() != MsgType.MSG_TIME) {
            setOnClick(helper, item);
            setStatus(helper, item);

            helper.getView(R.id.chat_item_header).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemAitClickListener != null) {
                        onItemAitClickListener.onItemAitClick(item, helper.getAdapterPosition());
                    }
                }
            });

            if (helper.getView(R.id.bivPic) != null) {
                //长按图片收藏
                helper.getView(R.id.bivPic).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (onImageLongClickListener != null) {
                            onImageLongClickListener.onItemLongClick(item, helper.getAdapterPosition());
                        }
                        return true;
                    }
                });
            }
            helper.addOnClickListener(R.id.ll_msg_title);
        }
    }

    public void setMentorName(String name){ this.mentorName = name; }

    private void setStatus(BaseViewHolder helper, final ChatMessage item) {
        //只需要设置自己发送的状态
        MsgSendStatus sentStatus = item.getSentStatus();
        boolean isSend = item.getSenderId().equals(ChatMainActivity.mSenderId);
        if (isSend) {
            if (sentStatus == MsgSendStatus.SENDING) {
                helper.setGone(R.id.chat_item_progress, true).setGone(R.id.chat_item_fail, false);
            } else if (sentStatus == MsgSendStatus.FAILED) {
                helper.setGone(R.id.chat_item_progress, false).setGone(R.id.chat_item_fail, true);
            } else if (sentStatus == MsgSendStatus.SENT) {
                helper.setGone(R.id.chat_item_progress, false).setGone(R.id.chat_item_fail, false);
            }
            helper.getView(R.id.chat_item_fail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRepeatSendListener != null) {
                        onRepeatSendListener.onRepeatSendListener(item);
                    }
                }
            });
        }
    }


    private void setContent(BaseViewHolder helper, ChatMessage item) {
        // 设置等级、昵称、时间
        setAccountPic(helper, item);
        // 根据消息类型设置消息内容
        if (item.getMsgType().equals(MsgType.TEXT)) {
            TextMsgBody msgBody = (TextMsgBody) item.getBody();
            helper.setText(R.id.chat_item_content_text, msgBody.getRecord());

            TextView textView = helper.getView(R.id.chat_item_content_text);
            if (!TextUtils.isEmpty(chatSysConfig.getName_word_color_info())) {
                try {
                    textView.setTextColor(Color.parseColor("#" + chatSysConfig.getName_word_color_info()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //设置消息字体大小
            if (!TextUtils.isEmpty(chatSysConfig.getSwitch_chat_word_size_show())) {
                textView.setTextSize(DisplayUtil.px2sp(context, Float.parseFloat(chatSysConfig.getSwitch_chat_word_size_show())));
                if (DisplayUtil.px2sp(context, Float.parseFloat(chatSysConfig.getSwitch_chat_word_size_show())) < 14) {
                    textView.setTextSize(14);
                } else {
                    textView.setTextSize(DisplayUtil.px2sp(context, Float.parseFloat(chatSysConfig.getSwitch_chat_word_size_show())));
                }
            } else {
                textView.setTextSize(14);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setTextBackgroundColor(helper.getView(R.id.chat_item_content_text), item.getAccount_level_name());
            }
        } else if (item.getMsgType().equals(MsgType.IMAGE)) {
            ImageMsgBody msgBody = (ImageMsgBody) item.getBody();
            if (msgBody.getPicMsg() != null) {
                msgBody = msgBody.getPicMsg();
            }
            if (!TextUtils.isEmpty(msgBody.getThumbPath()) && item.getSenderId().equals(ChatMainActivity.mSenderId)) {
                File file = new File(msgBody.getThumbPath());
                if (file.exists()) {
                    if (!msgBody.getThumbPath().equals(helper.getView(R.id.bivPic).getTag(R.id.bivPic))) {
                        GlideUtils.loadChatImage(mContext, msgBody.getThumbPath(), (ImageView) helper.getView(R.id.bivPic));
                        helper.getView(R.id.bivPic).setTag(R.id.bivPic, msgBody.getThumbPath());
                    }
                }
            } else {
                StringBuilder urls = new StringBuilder();
                String record = msgBody.getRecord();
                if (!TextUtils.isEmpty(record) && record.startsWith("http")) {
                    urls.append(record);
                } else {
                    if (record != null && record.contains("/")) {
                        String[] split = record.split("/");
                        record = split[split.length - 1];
                    }
                    urls.append(ConfigCons.CHAT_FILE_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.READ_FILE_WAP);
//                    urls.append("?fileId=" + record);
//                    urls.append("&contentType=image/jpeg");
                    urls.append("/" + record);
                }
                LogUtils.e("ImageUri:" + helper.getPosition(), urls.toString());
                if (!urls.toString().equals(helper.getView(R.id.bivPic).getTag(R.id.bivPic))) {
                    GlideUtils.loadChatImage(mContext, urls.toString(), (ImageView) helper.getView(R.id.bivPic));
                    helper.getView(R.id.bivPic).setTag(R.id.bivPic, urls.toString());
                }
            }
        } else if (item.getMsgType().equals(MsgType.VIDEO)) {
            VideoMsgBody msgBody = (VideoMsgBody) item.getBody();
            File file = new File(msgBody.getExtra());
            if (file.exists()) {
                GlideUtils.loadChatImage(mContext, msgBody.getExtra(), (ImageView) helper.getView(R.id.bivPic));
            } else {
                GlideUtils.loadChatImage(mContext, msgBody.getExtra(), (ImageView) helper.getView(R.id.bivPic));
            }
            helper.setText(R.id.tvVideoDuration, msgBody.getDuration());
        } else if (item.getMsgType().equals(MsgType.FILE)) {
            FileMsgBody msgBody = (FileMsgBody) item.getBody();
            helper.setText(R.id.msg_tv_file_name, msgBody.getDisplayName());
            helper.setText(R.id.msg_tv_file_size, msgBody.getSize() + "B");
        } else if (item.getMsgType().equals(MsgType.AUDIO)) {
            AudioMsgBody msgBody = (AudioMsgBody) item.getBody();
            if (msgBody.getAudioMsg() != null) {
                msgBody = msgBody.getAudioMsg();
            }
            helper.setText(R.id.tvDuration, msgBody.getDuration() + "\"");
            if (TextUtils.isEmpty(msgBody.getLocalPath())) {
                StringBuilder urls = new StringBuilder();
                urls.append(ConfigCons.CHAT_FILE_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.READ_FILE);
                urls.append("?fileId=" + msgBody.getRecord());
                urls.append("&contentType=audio/amr");
                msgBody.setLocalPath(urls.toString());
            }
            LogUtils.e("AudioUri:", msgBody.getLocalPath());
        } else if (item.getMsgType().equals(MsgType.SYS_PLAN)) {
            PlanMsgBody planMsgBody = (PlanMsgBody) item.getBody();
//            BetInfo planMsgBodyBetInfo = planMsgBody.getBetInfo();
//            helper.setText(R.id.tv_plan_content, planMsgBodyBetInfo.getPlan_content());
            helper.setText(R.id.tv_plan_name, planMsgBody.getLotteryName());
            (helper.getView(R.id.web_plan_content)).setBackgroundColor(0);
//            (helper.getView(R.id.web_plan_content)).getBackground().setAlpha(0);
            String content = planMsgBody.getText();
            if (TextUtils.isEmpty(content)) {
                return;
            }
            if (content.contains("<p")) {
                helper.setVisible(R.id.web_plan_content, true);
                helper.setGone(R.id.tv_plan_content, false);
                WebUtil.setVocalconcerDetail((BridgeWebView) helper.getView(R.id.web_plan_content), content);
            } else {
                helper.setGone(R.id.web_plan_content, false);
                helper.setVisible(R.id.tv_plan_content, true);
                helper.setText(R.id.tv_plan_content, content);
            }
        } else if (item.getMsgType().equals(MsgType.BET_SLIP)) {
            BetSlipMsgBody betSlipMsgBody = (BetSlipMsgBody) item.getBody();
            if (betSlipMsgBody == null || betSlipMsgBody.getBetInfos() == null) {
                return;
            }
            List<BetInfo> betInfos = betSlipMsgBody.getBetInfos();
            BetInfo betSlipMsgBodyBetInfo;
            if (betInfos != null && betInfos.size() != 0) {
                betSlipMsgBodyBetInfo = betInfos.get(0);
            } else {
                betSlipMsgBodyBetInfo = betSlipMsgBody.getBetInfo();
            }
            if (betSlipMsgBodyBetInfo == null)
                return;
            CommonUtils.updateLocImage(context, ((ImageView) helper.getView(R.id.iv_lottery_type)), betSlipMsgBodyBetInfo.getLotCode(), betSlipMsgBodyBetInfo.getLotIcon());
            if (betSlipMsgBodyBetInfo.getVersion() != null) {
//                if (betSlipMsgBodyBetInfo.getVersion().equals("1")) {
//                    helper.setText(R.id.tv_lottery_name, betSlipMsgBodyBetInfo.getLottery_type() + "[官]");
//                } else {
//                    helper.setText(R.id.tv_lottery_name, betSlipMsgBodyBetInfo.getLottery_type() + "[信]");
//                }
                helper.setText(R.id.tv_lottery_name, betSlipMsgBodyBetInfo.getLottery_type());
            }
            String lottery_qihao = betSlipMsgBodyBetInfo.getLottery_qihao();
            if (lottery_qihao != null) {
                int length = lottery_qihao.length();
                if (length > 6) {
                    lottery_qihao = lottery_qihao.substring(length - 6, length);
                }
            }
            helper.setText(R.id.tv_lottery_qihao, "期号:" + lottery_qihao + "期");
            helper.setText(R.id.tv_lottery_play, betSlipMsgBodyBetInfo.getLottery_play());
            helper.setText(R.id.tv_lottery_content, betSlipMsgBodyBetInfo.getLottery_content());
            String lottery_amount = betSlipMsgBodyBetInfo.getLottery_amount();
            if (TextUtils.isEmpty(lottery_amount) || "null".equalsIgnoreCase(lottery_amount)) {
                lottery_amount = "0";
            }
            float amount = Float.parseFloat(lottery_amount);
            helper.setText(R.id.tv_lottery_amount, (float) Math.round(amount * 100) / 100 + "");
        } else if (item.getMsgType().equals(MsgType.SYS_TEXT)) {
            SysTextMsgBody sysTextMsgBody = (SysTextMsgBody) item.getBody();
            helper.setText(R.id.chat_item_content_text, sysTextMsgBody.getMsg());

            TextView textView = helper.getView(R.id.chat_item_content_text);
            if (!TextUtils.isEmpty(chatSysConfig.getName_word_color_info())) {
                try {
                    textView.setTextColor(Color.parseColor("#" + chatSysConfig.getName_word_color_info()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //设置消息字体大小
            if (!TextUtils.isEmpty(chatSysConfig.getSwitch_chat_word_size_show())) {
                textView.setTextSize(DisplayUtil.px2sp(context, Float.parseFloat(chatSysConfig.getSwitch_chat_word_size_show())));
            } else {
                textView.setTextSize(14);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setTextBackgroundColor(helper.getView(R.id.chat_item_content_text), item.getAccount_level_name());
            }
        } else if (item.getMsgType().equals(MsgType.RED_PACKAGE)) {
            RedPackageMsgBody redPackageMsgBody = (RedPackageMsgBody) item.getBody();
            if (chatSysConfig.getSwitch_red_bag_remark_show().equals("0")) {
                if (TextUtils.isEmpty(chatSysConfig.getName_red_bag_remark_info())) {
                    helper.setGone(R.id.tv_hb_content, false);
                } else {
                    helper.setText(R.id.tv_hb_content, chatSysConfig.getName_red_bag_remark_info());
                }
            } else {
                helper.setGone(R.id.tv_hb_content, true);
                helper.setText(R.id.tv_hb_content, redPackageMsgBody.getRemark());
            }
        } else if (item.getMsgType().equals(MsgType.HTML)) {
            TextMsgBody msgBody = (TextMsgBody) item.getBody();
//            (helper.getView(R.id.chat_item_web_html)).getBackground().setAlpha(0);
            String record = msgBody.getRecord();
            (helper.getView(R.id.chat_item_web_html)).setBackgroundColor(0);
            if (record.contains("<img")) {
                helper.setVisible(R.id.chat_item_web_html, true);
                helper.setGone(R.id.tv_item_html, false);
                WebUtil.setVocalconcerDetail((BridgeWebView) helper.getView(R.id.chat_item_web_html), record);
            } else {
                helper.setGone(R.id.chat_item_web_html, false);
                helper.setVisible(R.id.tv_item_html, true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setTextBackgroundColor(helper.getView(R.id.fl_chat_item_html), item.getAccount_level_name());
                }
                helper.setText(R.id.tv_item_html, Html.fromHtml(record).toString());
                TextView tv_item_html = helper.getView(R.id.tv_item_html);
                tv_item_html.setText(tv_item_html.getText().toString().trim());
            }
        } else if (item.getMsgType().equals(MsgType.MSG_TIME)) {
            TextMsgBody textMsgBody = (TextMsgBody) item.getBody();
            helper.setText(R.id.tv_time, textMsgBody.getRemark());
        } else if (item.getMsgType().equals(MsgType.SHARE_DATA)) {
            TextMsgBody body = (TextMsgBody) item.getBody();
            PersonDataBean personDataBean = new Gson().fromJson(body.getRecord(), PersonDataBean.class);
            helper.setText(R.id.tv_zyl, CommonUtils.getDoubleToString(personDataBean.getYingli(), 2));
            helper.setText(R.id.tv_zyk, CommonUtils.getDoubleToString(personDataBean.getYingkui(), 2));
            helper.setText(R.id.tv_ztz, CommonUtils.getDoubleToString(personDataBean.getTouzhu(), 2));
        } else if (item.getMsgType().equals(MsgType.BET_SLIP_LIST)) {
            final BetSlipMsgBody betSlipMsgBody = (BetSlipMsgBody) item.getBody();
            if (betSlipMsgBody.getBetInfos() == null)
                return;
            BetSlipAdapter betSlipAdapter = new BetSlipAdapter(betSlipMsgBody.getBetInfos(), context);
            betSlipAdapter.setOnFollowBetListener(new BetSlipAdapter.OnSingleFollowBetListener() {
                @Override
                public void onSingleFollowBet(BetInfo betInfo, int position) {
                    if (singleFollowBetListener != null) {
                        singleFollowBetListener.onSingleFollow(betInfo, betSlipMsgBody.getOrders().get(position).getOrderId()
                                , betSlipMsgBody.getMsgUUID(), betSlipMsgBody.getRoomId());
                    }
                }
            });
            LinearLayoutManager mLinearLayout = new LinearLayoutManager(context);
            ((RecyclerView) helper.getView(R.id.rcy_bet_slip)).setLayoutManager(mLinearLayout);
            ((RecyclerView) helper.getView(R.id.rcy_bet_slip)).setAdapter(betSlipAdapter);
            ((RecyclerView) helper.getView(R.id.rcy_bet_slip)).addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                    recyclerView.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }

                @Override
                public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean b) {

                }
            });
            if (betSlipAdapter.getItemCount() > 2) {
                helper.setVisible(R.id.iv_scroll, true);
            } else {
                helper.setVisible(R.id.iv_scroll, false);
            }
        }
    }

    private void setAccountPic(BaseViewHolder helper, ChatMessage item) {
        if (item.getMsgType() == MsgType.MSG_TIME) {
            return;
        }
        String sentTime = item.getBody().getSentTime();
        if (sentTime != null && sentTime.length() > 8) {
            sentTime = sentTime.substring(sentTime.length() - 8, sentTime.length());
        }
        String account_name = item.getAccount_name();
        if (item.getMsgType() == MsgType.SYS_TEXT) {
            helper.setGone(R.id.item_iv_level, false);
            helper.setGone(R.id.item_tv_daoshi, false);
            helper.setGone(R.id.chat_item_admin, false);
            helper.setText(R.id.item_tv_nick_name, account_name);
            helper.setText(R.id.item_tv_time, sentTime);
            helper.setImageResource(R.id.chat_item_header, R.mipmap.icon_sys);
            helper.itemView.findViewById(R.id.ll_baolv).setVisibility(View.GONE);
        } else {
            String account_pic = item.getAccount_pic();
            helper.setGone(R.id.item_tv_daoshi, item.getPlanUser() == 1);
            helper.setText(R.id.item_tv_daoshi, mentorName);
            helper.setGone(R.id.chat_item_admin, item.getUserType() == 4 || item.getUserType() == 2);
            if (item.getMsgType() != MsgType.SYS_PLAN) {
                if (chatSysConfig.getSwitch_level_show().equals("1")) {
                    helper.setGone(R.id.item_tv_level_name, true);
                    //d891要求将"计划员"改成"导师"
//                    helper.setText(R.id.item_tv_level_name, item.getPlanUser() == 1 ? "导师" : (item.getUserType() == 2 || item.getUserType() == 4) ? "管理员" : item.getAccount_level_name());
                    helper.setText(R.id.item_tv_level_name, item.getAccount_level_name().equals("后台管理员") ? "" : item.getAccount_level_name());
                    //设置等级文字颜色
                    setLevelNameColor((TextView) helper.getView(R.id.item_tv_level_name), item.getAccount_level_name());
                } else {
                    helper.setGone(R.id.item_tv_level_name, false);
                }
                if (chatSysConfig.getSwitch_level_ico_show().equals("1")) {
                    helper.setGone(R.id.item_iv_level, true);
                    GlideUtils.loadLevelImage(context, (ImageView) helper.getView(R.id.item_iv_level), item.getAccount_level());
                } else {
                    helper.setGone(R.id.item_iv_level, false);
                }
                if (item.isShowAccount()) {
                    helper.setText(R.id.item_tv_nick_name, item.getUserAccount());
                    helper.setTextColor(R.id.item_tv_nick_name, context.getResources().getColor(R.color.red8));
                } else {
                    helper.setText(R.id.item_tv_nick_name, account_name);
                    helper.setTextColor(R.id.item_tv_nick_name, context.getResources().getColor(R.color.black));
                }
                helper.setText(R.id.item_tv_time, sentTime);
//                if (item.getBody().getUserId().equals(chatSpUtils.getUserId()) && !TextUtils.isEmpty(chatSysConfig.getName_new_members_default_photo())) {
//                    GlideUtils.loadHeaderPic(context, chatSysConfig.getName_new_members_default_photo(), ((ImageView) helper.getView(R.id.chat_item_header)));
//                } else {
//                    GlideUtils.loadHeaderPic(context, item.getAccount_pic(), ((ImageView) helper.getView(R.id.chat_item_header)));
//                }
                GlideUtils.loadHeaderPic(context, account_pic, ((ImageView) helper.getView(R.id.chat_item_header)));
                if (item.getMsgType() == MsgType.BET_SLIP || item.getMsgType() == MsgType.BET_SLIP_LIST) {
                    String name_user_win_tips_per = chatSysConfig.getName_user_win_tips_per();
                    float winPer = 0.0f;
                    if (!TextUtils.isEmpty(name_user_win_tips_per)) {
                        winPer = Float.parseFloat(name_user_win_tips_per);
                    }

                    if (item.getWinOrLost() == null || item.getWinOrLost().getWinPer() == 0.0 || winPer > item.getWinOrLost().getWinPer()) {
                        helper.itemView.findViewById(R.id.ll_baolv).setVisibility(View.GONE);
                        helper.itemView.findViewById(R.id.item_tv_time).setVisibility(View.VISIBLE);
                    } else {
                        helper.itemView.findViewById(R.id.ll_baolv).setVisibility(View.VISIBLE);
                        helper.setText(R.id.item_tv_baolv, item.getWinOrLost().getWinPer() + "%");
                        helper.itemView.findViewById(R.id.item_tv_time).setVisibility(View.GONE);
                    }

                } else {
                    helper.itemView.findViewById(R.id.ll_baolv).setVisibility(View.GONE);
                }
            } else {
                helper.setText(R.id.item_tv_nick_name, "计划消息");
                helper.setTextColor(R.id.item_tv_nick_name, context.getResources().getColor(R.color.plan_col));
//                if (!TextUtils.isEmpty(account_name)) {
//                    if (item.isShowAccount()) {
//                        helper.setText(R.id.item_tv_nick_name, item.getUserAccount());
//                        helper.setTextColor(R.id.item_tv_nick_name, context.getResources().getColor(R.color.red8));
//                    } else {
//                        helper.setText(R.id.item_tv_nick_name, account_name);
//                        helper.setTextColor(R.id.item_tv_nick_name, context.getResources().getColor(R.color.black));
//                    }
//                } else {
//                    helper.setText(R.id.item_tv_nick_name, "计划消息");
//                    helper.setTextColor(R.id.item_tv_nick_name, context.getResources().getColor(R.color.plan_col));
//                }
                helper.setText(R.id.item_tv_time, sentTime);
                helper.itemView.findViewById(R.id.ll_baolv).setVisibility(View.GONE);
                if (!TextUtils.isEmpty(account_pic)) {
                    GlideUtils.loadHeaderPic(context, account_pic, ((ImageView) helper.getView(R.id.chat_item_header)));
                }
            }
        }
    }


    /**
     * 设置.9图片背景颜色
     *
     * @param textView
     * @param nameLevel
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setTextBackgroundColor(View textView, String nameLevel) {
        String nameColor = "";
        List<ChatSysConfig.NameBackGroundColorInfoBean> name_backGround_color_info = chatSysConfig.getNative_name_backGround_color_info();
        if (name_backGround_color_info == null) {
            return;
        }
        for (ChatSysConfig.NameBackGroundColorInfoBean bean : name_backGround_color_info) {
            if (nameLevel.equals(bean.getLevelName()) || nameLevel.contains(bean.getLevelName())) {
                nameColor = bean.getColor();
                break;
            }
        }
        try {
            if (!TextUtils.isEmpty(nameColor)) {
                ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#" + nameColor));
                textView.setBackgroundTintList(colorStateList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 设置等级文字颜色
     *
     * @param textView
     * @param nameLevel
     */
    private void setLevelNameColor(TextView textView, String nameLevel) {
        String nameColor = "";
        List<ChatSysConfig.NameLevelTitleColorInfoBean> name_backGround_color_info = chatSysConfig.getNative_name_level_title_color_info();
        if (name_backGround_color_info == null) {
            return;
        }
        for (ChatSysConfig.NameLevelTitleColorInfoBean bean : name_backGround_color_info) {
            if (nameLevel.equals(bean.getLevelName()) || nameLevel.contains(bean.getLevelName())) {
                nameColor = bean.getColor();
                break;
            }
        }
        try {
            if (TextUtils.isEmpty(nameColor)) {
                textView.setTextColor(Color.BLACK);
            } else {
                textView.setTextColor(Color.parseColor("#" + nameColor));
            }
        } catch (Exception e) {
            textView.setTextColor(Color.BLACK);
        }

    }


    private void setOnClick(BaseViewHolder helper, ChatMessage item) {
        MsgBody msgContent = item.getBody();
        if (msgContent instanceof AudioMsgBody) {
            helper.addOnClickListener(R.id.rlAudio);
        } else if (msgContent instanceof ImageMsgBody) {
            helper.addOnClickListener(R.id.bivPic);
        } else if (msgContent instanceof VideoMsgBody) {
            helper.addOnClickListener(R.id.ivPlay);
        } else if (msgContent instanceof TextMsgBody || msgContent instanceof SysTextMsgBody) {
            helper.addOnLongClickListener(R.id.chat_item_content_text);
        } else if (msgContent instanceof FileMsgBody) {
            helper.addOnClickListener(R.id.rc_message);
        } else if (msgContent instanceof PlanMsgBody) {
            helper.addOnClickListener(R.id.rl_plan_content);
        } else if (msgContent instanceof BetSlipMsgBody) {
            helper.addOnClickListener(R.id.ll_bet_slip_content);
        } else if (msgContent instanceof RedPackageMsgBody) {
            helper.addOnClickListener(R.id.ll_red_package_content);
        }
    }

    public void setOnItemAitClickListener(ChatAdapter.OnItemAitClickListener onItemAitClickListener) {
        this.onItemAitClickListener = onItemAitClickListener;
    }

    private OnItemAitClickListener onItemAitClickListener;

    public void setOnRepeatSendListener(OnRepeatSendClickListener onRepeatSendListener) {
        this.onRepeatSendListener = onRepeatSendListener;
    }

    private OnRepeatSendClickListener onRepeatSendListener;


    public void setOnImageLongClickListener(OnImageLongClickListener onImageLongClickListener) {
        this.onImageLongClickListener = onImageLongClickListener;
    }

    private OnImageLongClickListener onImageLongClickListener;

    public interface OnImageLongClickListener {
        void onItemLongClick(ChatMessage item, int position);
    }

    public interface OnItemAitClickListener {
        void onItemAitClick(ChatMessage item, int position);
    }

    public interface OnRepeatSendClickListener {
        void onRepeatSendListener(ChatMessage item);
    }

    /**
     * 添加新数据，目前限定消息列表最大长度为500
     *
     * @param chatMessage
     */
    public void addNewMsg(ChatMessage chatMessage) {
        if (this.getItemCount() >= 500) {
            this.remove(0);
        }
        this.addData(chatMessage);
    }

    public void isHistoryMsg(boolean isHistoryMsg) {
        this.isHistoryMsg = isHistoryMsg;
    }

    public interface ChatAdapterScrollInterFace {
        public void Scroll(int pos);
    }

    private ChatAdapterScrollInterFace chatAdapterScrollInterFace = null;

    public void setChatAdapterScrollInterFace(ChatAdapterScrollInterFace chatAdapterScrollInterFace) {
        this.chatAdapterScrollInterFace = chatAdapterScrollInterFace;
    }

    private SingleFollowBetListener singleFollowBetListener;

    public void setSingleFollowBetListener(SingleFollowBetListener singleFollowBetListener) {
        this.singleFollowBetListener = singleFollowBetListener;
    }

    public interface SingleFollowBetListener {
        void onSingleFollow(BetInfo betInfo, String orderId, String uuid, String roomId);
    }
}
