package com.example.anuo.immodule.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.AuthorityBean;
import com.example.anuo.immodule.bean.ChatSysConfigBean;
import com.example.anuo.immodule.bean.LoginBean;
import com.example.anuo.immodule.bean.LoginChatBean;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.interfaces.iview.IChatLoginView;
import com.example.anuo.immodule.interfaces.iview.base.IChatBaseView;
import com.example.anuo.immodule.jsonmodel.ChatSysConfigModel;
import com.example.anuo.immodule.jsonmodel.LoginJsonModel;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.view.LogingDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.UUID;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/*
 * 在首页登录，不需要activity
 * */
public class ChatLoginAuthorPresneter extends ChatBasePresenter implements SessionResponse.Listener<CrazyResult<Object>> {

    private Context context;

    private final IChatLoginView iView;
    private final int LOGIN_REQUEST = 0x01;
    private final int AUTHORIZATION_REQUEST = 0x02;
    private final int LOGIN_CHATROOM_REQUEST = 0X03;
    private final int GET_SYS_CONFIG = 0X04;
    private String permissionInfo;

    private LogingDialog logingDialog;

    public ChatLoginAuthorPresneter(Context activity, IChatBaseView iview) {
        super(activity, iview);
        this.context = activity;
        this.iView = (IChatLoginView) iview;
        logingDialog = new LogingDialog(context);
        logingDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 1、用户登录
     *
     * @param userName
     * @param userPwd
     */
    public void login(String userName, String userPwd) {
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT).append(ConfigCons.LOGIN_URL);
        urls.append("?username=").append(userName).append("&password=").append(userPwd);
        CrazyRequest<CrazyResult<LoginBean>> request = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(LOGIN_REQUEST)
                .headers(CommonUtils.getHeader(context))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .placeholderText(context.getString(R.string.login_ongoing))
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LoginBean>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request, this);
    }

    /**
     * 2、聊天室授权
     */
    public void authorization() {
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.YUNJI_BASE_URL).append(ConfigCons.PORT).append(ConfigCons.AUTHORITY_URL);
        logingDialog.show();
        if (!((Activity) context).isFinishing())
            logingDialog.updateTitle(context.getString(R.string.authority_ongoing));
        CrazyRequest<CrazyResult<AuthorityBean>> request = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(AUTHORIZATION_REQUEST)
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .headers(CommonUtils.getChatHeader(context))
                .placeholderText(context.getString(R.string.authority_ongoing))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<AuthorityBean>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request, this);
    }


    /**
     * 3、聊天室登陆
     *
     * @param clusterId
     * @param encrypted
     * @param sign
     */
    public void loginChatRoom(String clusterId, String encrypted, String sign) {
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
        String token = "clusterId=" + clusterId + "&encrypted=" + encrypted + "&sign=" + sign;
        LoginJsonModel jsonModel = new LoginJsonModel(ConfigCons.LOGIN_AUTHORITY, ConfigCons.SOURCE, token);
        String bodyStr = new Gson().toJson(jsonModel);
        if (!((Activity) context).isFinishing())
            logingDialog.updateTitle(context.getString(R.string.login_chat_ongoing));
        CrazyRequest<CrazyResult<LoginChatBean>> request = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(LOGIN_CHATROOM_REQUEST)
                .headers(CommonUtils.getChatHeader(context))
                .body(bodyStr)
                .contentType("application/json;utf-8")
                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LoginChatBean>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request, this);
    }

    //获取聊天室配置
    public void getSysConfig(String userId, String stationId, int userType) {
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
        ChatSysConfigModel chatSysConfig = new ChatSysConfigModel(ConfigCons.GET_SYS_CONFIG, userId, stationId, userType, ConfigCons.SOURCE, UUID.randomUUID().toString());
        String bodyStr = new Gson().toJson(chatSysConfig);
        if (!((Activity) context).isFinishing())
            logingDialog.updateTitle("获取系统配置中...");
        CrazyRequest<CrazyResult<ChatSysConfigBean>> request = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(GET_SYS_CONFIG)
                .headers(CommonUtils.getChatHeader(context))
                .body(bodyStr)
                .contentType("application/json;utf-8")
                .placeholderText("获取系统配置中...")
                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<ChatSysConfigBean>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request, this);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (response == null) {
            logingDialog.dismiss();
            return;
        }
        switch (response.action) {
            case LOGIN_REQUEST:
                CrazyResult<Object> result = response.result;
                if (result == null) {
                    logingDialog.dismiss();
                    showToast(R.string.login_fail);
                    return;
                }
                if (!result.crazySuccess) {
                    logingDialog.dismiss();
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? context.getString(R.string.login_fail) : errorString);
                    return;
                }

                LoginBean loginBean = (LoginBean) result.result;
                if (!loginBean.isSuccess()) {
                    logingDialog.dismiss();
                    showToast(R.string.login_fail);
                    return;
                }
                iView.onLogin(loginBean);
                break;
            case AUTHORIZATION_REQUEST:
                CrazyResult<Object> authorityResult = response.result;
                if (authorityResult == null) {
                    logingDialog.dismiss();
                    showToast(R.string.authority_fail);
                    iView.onLoginFail();
                    return;
                }
                if (!authorityResult.crazySuccess) {
                    logingDialog.dismiss();
                    String errorString = CommonUtils.parseResponseResult(authorityResult.error);
                    showToast(TextUtils.isEmpty(errorString) ? context.getString(R.string.authority_fail) : errorString);
                    iView.onLoginFail();
                    return;
                }
                AuthorityBean authorityBean = (AuthorityBean) authorityResult.result;
                if (!authorityBean.isSuccess()) {
                    logingDialog.dismiss();
                    showToast(R.string.authority_fail);
                    iView.onLoginFail();
                    return;
                }
                loginChatRoom(authorityBean.getContent().getClusterId(), authorityBean.getContent().getEncrypted(), authorityBean.getContent().getSign());
                break;
            case LOGIN_CHATROOM_REQUEST:
                CrazyResult<Object> loginResult = response.result;
                if (loginResult == null) {
                    logingDialog.dismiss();
                    showToast(R.string.login_chat_fail);
                    iView.onLoginFail();
                    return;
                }
                if (!loginResult.crazySuccess) {
                    logingDialog.dismiss();
                    String errorString = CommonUtils.parseResponseResult(loginResult.error);
                    showToast(TextUtils.isEmpty(errorString) ? context.getString(R.string.login_chat_fail) : errorString);
                    iView.onLoginFail();
                    return;
                }
                LoginChatBean loginChatBean = (LoginChatBean) loginResult.result;
                if (!loginChatBean.isSuccess()) {
                    logingDialog.dismiss();
                    showToast(R.string.login_chat_fail);
                    iView.onLoginFail();
                    return;
                }
                iView.onLoginChat(loginChatBean);
                break;
            case GET_SYS_CONFIG:
                CrazyResult<Object> sysResult = response.result;
                logingDialog.dismiss();
                if (sysResult == null) {
                    showToast(R.string.get_sys_config_fail);
                    iView.onLoginFail();
                    return;
                }
                if (!sysResult.crazySuccess) {
                    String errorString = CommonUtils.parseResponseResult(sysResult.error);
                    showToast(TextUtils.isEmpty(errorString) ? context.getString(R.string.get_sys_config_fail) : errorString);
                    iView.onLoginFail();
                    return;
                }
                ChatSysConfigBean chatSysConfigBean = (ChatSysConfigBean) sysResult.result;
                if (chatSysConfigBean.isSuccess()) {
                    String config = new Gson().toJson(chatSysConfigBean.getSource());
                    ChatSpUtils.instance(context).setChatSysConfig(config);
                    iView.onLoginSuc();
                } else {
                    showToast(TextUtils.isEmpty(chatSysConfigBean.getMsg()) ? context.getString(R.string.get_sys_config_fail) : chatSysConfigBean.getMsg());
                    iView.onLoginFail();
                }
                break;
        }
    }


}
