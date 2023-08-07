package com.example.anuo.immodule.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;
import com.example.anuo.immodule.adapter.ChatAdapter;
import com.example.anuo.immodule.bean.ChatHistoryMessageBean;
import com.example.anuo.immodule.bean.ChatMessage;
import com.example.anuo.immodule.bean.MsgSendStatus;
import com.example.anuo.immodule.bean.MsgType;
import com.example.anuo.immodule.bean.TextMsgBody;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.ChatTimeUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.anuo.immodule.activity.ChatMainActivity.mSenderId;
import static com.example.anuo.immodule.activity.ChatMainActivity.mTargetId;

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
 * Date  :2019-12-21
 * Desc  :com.example.anuo.immodule.activity
 */
public class MasterPlanListActivity extends ChatBaseActivity {

    RecyclerView rcy_master_plan;
    private List<ChatMessage> chatMsgs = new ArrayList<>();
    private ChatAdapter mAdapter;


    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected int onCreate_(Bundle savedInstanceState) {
        return R.layout.activity_master_plan;
    }

    @Override
    protected void initView() {
        super.initView();
        rcy_master_plan = findViewById(R.id.rcy_master_plan);
        tvMiddleTitle.setText("导师计划");
        mAdapter = new ChatAdapter(this, chatMsgs);
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);
        rcy_master_plan.setLayoutManager(mLinearLayout);
        rcy_master_plan.setAdapter(mAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        ChatHistoryMessageBean master_plan = getIntent().getParcelableExtra("master_plan");
        List<ChatHistoryMessageBean.SourceBean> source = master_plan.getSource();
        if (source.isEmpty())
            return;
        for (int i = 0; i < source.size(); i++) {
            handleHistoryMsg(source.get(i));
        }
        //mLinearLayout.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, Integer.MIN_VALUE);
        if (mAdapter.getItemCount() > 0) {
            rcy_master_plan.scrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    @Override
    protected void initListener() {

    }

    private void handleHistoryMsg(ChatHistoryMessageBean.SourceBean msgBean) {
        ChatMessage mMessgae = new ChatMessage();
        if (msgBean.getSender().equals(ChatSpUtils.instance(this).getUserId())) {
            mMessgae.setSenderId(mSenderId);
            mMessgae.setTargetId(mTargetId);
            mMessgae.setSentStatus(MsgSendStatus.SENT);
        } else {
            mMessgae.setSenderId(mTargetId);
            mMessgae.setTargetId(mSenderId);
        }
        mMessgae.setPlanUser(msgBean.getIsPlanUser());
        mMessgae.setUuid(msgBean.getMsgId());
        mMessgae.setAccount_level(msgBean.getLevelIcon());
        mMessgae.setAccount_level_name(msgBean.getLevelName());
        mMessgae.setMsgId(msgBean.getMsgId());
        mMessgae.setUserType(msgBean.getUserType());
        if (msgBean.getUserType() == 2) {
            // 系统管理员
//            mMessgae.setAccount_name(msgBean.getNativeAccount());
            mMessgae.setAccount_pic(msgBean.getNativeAvatar());
        } else {
            mMessgae.setAccount_pic(msgBean.getAvatar());
        }
        String nickName = msgBean.getNickName();
        if (msgBean.getSender().equals(ChatSpUtils.instance(this).getUserId())) {
            mMessgae.setAccount_name(TextUtils.isEmpty(nickName) ? msgBean.getAccount() : nickName);
        } else {
            mMessgae.setAccount_name(TextUtils.isEmpty(nickName) ? msgBean.getNativeAccount() : nickName);
        }
//        if (msgBean.getUserType() != 3) {
        switch (msgBean.getMsgType()) {
            case 1:
                //文本消息
                TextMsgBody mTextMsgBody = new TextMsgBody();
                mTextMsgBody.setRecord(msgBean.getContext());
                mTextMsgBody.setType(msgBean.getUserType());
                mMessgae.setMsgType(MsgType.TEXT);
                mMessgae.setBody(mTextMsgBody);
                break;
        }
        if (mMessgae.getBody() != null) {
            if (mAdapter.getData().size() == 0) {
                long date = ChatTimeUtil.getStringToDate(msgBean.getTime(), "yyyy-MM-dd HH:mm:ss");
                TextMsgBody txtTimeBody = new TextMsgBody();
                txtTimeBody.setRecord(msgBean.getContext());
                txtTimeBody.setType(msgBean.getUserType());
                txtTimeBody.setRemark(ChatTimeUtil.getNewChatTime(date));
                mMessgae.setMsgType(MsgType.MSG_TIME);
                mMessgae.setBody(txtTimeBody);
                mMessgae.getBody().setSentTime(msgBean.getTime());
                mMessgae.getBody().setSource(ConfigCons.SOURCE);
                mMessgae.getBody().setUserId(msgBean.getSender());
                mAdapter.addData(mMessgae);
                handleHistoryMsg(msgBean);
                return;
            } else {
                String time = mAdapter.getData().get(mAdapter.getData().size() - 1).getBody().getSentTime();
                long date = ChatTimeUtil.getStringToDate(msgBean.getTime(), "yyyy-MM-dd");
                long proDate = ChatTimeUtil.getStringToDate(time, "yyyy-MM-dd");
                if (date != proDate) {
                    long newdate = ChatTimeUtil.getStringToDate(msgBean.getTime(), "yyyy-MM-dd HH:mm:ss");
                    TextMsgBody txtTimeBody = new TextMsgBody();
                    txtTimeBody.setRecord(msgBean.getContext());
                    txtTimeBody.setType(msgBean.getUserType());
                    txtTimeBody.setRemark(ChatTimeUtil.getNewChatTime(newdate));
                    mMessgae.setMsgType(MsgType.MSG_TIME);
                    mMessgae.setBody(txtTimeBody);
                    mMessgae.getBody().setSentTime(msgBean.getTime());
                    mMessgae.getBody().setSource(ConfigCons.SOURCE);
                    mMessgae.getBody().setUserId(msgBean.getSender());
                    mAdapter.addData(mMessgae);
                    handleHistoryMsg(msgBean);
                    return;
                }
            }
            mMessgae.getBody().setSentTime(msgBean.getTime());
            mMessgae.getBody().setMsgUUID(msgBean.getMsgId());
            mMessgae.getBody().setMsgId(msgBean.getMsgId());
            mMessgae.getBody().setSource(ConfigCons.SOURCE);
            mMessgae.getBody().setUserId(msgBean.getSender());
            mAdapter.addData(mMessgae);
        }
    }

}
