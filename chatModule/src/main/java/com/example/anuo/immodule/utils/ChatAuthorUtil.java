package com.example.anuo.immodule.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.anuo.immodule.activity.ChatMainActivity;
import com.example.anuo.immodule.bean.LoginBean;
import com.example.anuo.immodule.bean.LoginChatBean;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.interfaces.iview.IChatLoginView;
import com.example.anuo.immodule.presenter.ChatLoginAuthorPresneter;
import com.example.anuo.immodule.presenter.ChatLoginPresenter;

/*
* 获取聊天室授权
* */
public class ChatAuthorUtil  implements IChatLoginView {

    private ChatMainActivity context;

    private ChatLoginAuthorPresneter presenter;

    int lotteryVersion1;
    int lotteryVersion2;
    String baseUrl;


    public ChatAuthorUtil(ChatMainActivity context, int lotteryVersion1, int lotteryVersion2, String baseUrl) {
        this.context = context;
        this.lotteryVersion1 = lotteryVersion1;
        this.lotteryVersion2 = lotteryVersion2;
        this.baseUrl = baseUrl;
        presenter = new ChatLoginAuthorPresneter(context, this);
        presenter.authorization();
    }

    public ChatAuthorUtil(ChatMainActivity context) {
        this.context = context;
        presenter = new ChatLoginAuthorPresneter(context, this);
        presenter.authorization();
    }


    /**
     * 用户登录app成功回调
     *
     * @param loginBean
     */
    @Override
    public void onLogin(LoginBean loginBean) {

    }

    private LoginChatBean loginChatBean;

    /**
     * 用户登录聊天室成功回调
     *
     * @param loginChatBean
     */
    @Override
    public void onLoginChat(LoginChatBean loginChatBean) {
        // 储存聊天信息
        this.loginChatBean = loginChatBean;
        LoginChatBean.SourceBean userTmp = loginChatBean.getSource();
        presenter.getSysConfig(userTmp.getSelfUserId(),userTmp.getStationId(), userTmp.getUserType());
    }

    @Override
    public void onLoginSuc() {
        LoginChatBean.SourceBean userTmp = loginChatBean.getSource();
        ChatSpUtils.instance(context).setUserId(userTmp.getSelfUserId());
        ChatSpUtils.instance(context).setStationId(userTmp.getStationId());
        ChatSpUtils.instance(context).setACCOUNT_TYPE(userTmp.getAccountType());
        if (userTmp.getAccountType()== 2 || userTmp.getAccountType() == 3) {
            if (userTmp.getAgentRoom() != null) {
                ChatSpUtils.instance(context).setAGENT_USER_CODE(userTmp.getAgentRoom().getAgentUserCode());
            }
        }
        ChatSpUtils.instance(context).setMainAppBaseUrl(baseUrl);
        ChatSpUtils.instance(context).setLotteryVersion(lotteryVersion1, lotteryVersion2);

//        Intent intent = new Intent(context, ChatMainActivity.class);
//        intent.putExtra(ConfigCons.LOGIN_CHAT_INFO, loginChatBean);
//        context.startActivity(intent);
    }

    @Override
    public void onLoginFail() {
        context.finish();
    }

    @Override
    public void gotoActivity(Class aClass) {

    }
}
