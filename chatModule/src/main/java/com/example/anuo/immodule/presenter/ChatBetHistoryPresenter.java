package com.example.anuo.immodule.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.ChatBetHistoryActivity;
import com.example.anuo.immodule.bean.BaseChatReceiveMsg;
import com.example.anuo.immodule.bean.BetHistoryBean;
import com.example.anuo.immodule.bean.BetSlipMsgBody;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.interfaces.iview.IChatBetHistoryView;
import com.example.anuo.immodule.jsonmodel.ChatBetHistoryModel;
import com.example.anuo.immodule.manager.SocketManager;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.AESUtils;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.utils.LogUtils;
import com.example.anuo.immodule.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
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
 * Date  :09/10/2019
 * Desc  :com.example.anuo.immodule.presenter
 */
public class ChatBetHistoryPresenter extends ChatBasePresenter implements SessionResponse.Listener<CrazyResult<Object>> {
    private static final int GET_BET_HISTORY = 0x11;
    private final SocketManager socketManager;
    private ChatBetHistoryActivity context;
    private IChatBetHistoryView iView;

    public ChatBetHistoryPresenter(ChatBetHistoryActivity context, IChatBetHistoryView view) {
        super(context, view);
        this.context = context;
        this.iView = view;
        socketManager = SocketManager.instance(context);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            switch (msg.what) {
                case GET_BET_HISTORY:
                    BetHistoryBean betHistoryBean = new Gson().fromJson(json, BetHistoryBean.class);
                    if (!betHistoryBean.isSuccess()) {
                        showToast(TextUtils.isEmpty(betHistoryBean.getMsg()) ? context.getString(R.string.request_fail) : betHistoryBean.getMsg());
                        return;
                    }
                    if (betHistoryBean.getSource() == null) {
                        return;
                    }
                    iView.onGetBetHistory(betHistoryBean.getSource());
                    break;
            }
        }
    };

    /**
     * 获取历史注单列表
     *
     * @param chatBetHistoryModel
     */
    public void initData(ChatBetHistoryModel chatBetHistoryModel) {
        String json = new Gson().toJson(chatBetHistoryModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, json, true);
        /*------------------------------*/
//        StringBuilder urls = new StringBuilder();
//        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
//        CrazyRequest<CrazyResult<BetHistoryBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(GET_BET_HISTORY)
//                .headers(CommonUtils.getChatHeader(context))
//                .body(json)
//                .contentType("application/json;utf-8")
//                .placeholderText(context.getString(R.string.loading))
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<BetHistoryBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(context, request, this);
    }

    public void sendSocket(String socketKey, String content, final boolean showToast) {
        socketManager.setSendListener(sendListener);
//        socketManager.connectSocket();
        final boolean[] isSuccess = {false};
        String str = AESUtils.encrypt(content, ConfigCons.DATA_KEY, ConfigCons.DEFAULT_IV);
        Ack ack = new Ack() {
            @Override
            public void call(Object... args) {
                LogUtils.e("a", "服务器确认收到我的消息=" + Arrays.toString(args));
                isSuccess[0] = true;
            }
        };
        socketManager.sendMsg(socketKey, str, ack);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 延迟十秒钟，如果还没有发送成功，就提示消息发送失败
                if (!isSuccess[0]) {
                    if (showToast) {
                        ToastUtils.showToast(context, R.string.time_out);
                    }
                }
            }
        }, 15000);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (context.isFinishing() || response == null) {
            return;
        }
        CrazyResult<Object> result = response.result;
        switch (response.action) {
            case GET_BET_HISTORY:
                if (result == null) {
                    showToast(context.getString(R.string.request_fail));
                    return;
                }
                BetHistoryBean betHistoryBean = (BetHistoryBean) result.result;
                if (!result.crazySuccess) {
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? context.getString(R.string.request_fail) : errorString);
                    return;
                }
                if (!betHistoryBean.isSuccess()) {
                    showToast(TextUtils.isEmpty(betHistoryBean.getMsg()) ? context.getString(R.string.request_fail) : betHistoryBean.getMsg());
                    return;
                }
                if (betHistoryBean.getSource() == null) {
                    return;
                }
                iView.onGetBetHistory(betHistoryBean.getSource());
                break;
        }
    }

    //--------------------------------SocketIO监听-----------------------------------

    /**
     * 发送消息监听
     */
    private Emitter.Listener sendListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String json = AESUtils.decrypt(String.valueOf(args[0]), ConfigCons.DATA_KEY, ConfigCons.DEFAULT_IV);
            LogUtils.e("a", "on send success:" + json);
            if (!TextUtils.isEmpty(json)) {
                BaseChatReceiveMsg baseChatReceiveMsg = new Gson().fromJson(json, BaseChatReceiveMsg.class);
                Message message = Message.obtain();
                message.obj = json;
                switch (baseChatReceiveMsg.getCode()) {
                    case ConfigCons.GET_BET_HISTORY:
                        message.what = GET_BET_HISTORY;
                        break;

                }
                handler.sendMessage(message);
            }
        }
    };
}
