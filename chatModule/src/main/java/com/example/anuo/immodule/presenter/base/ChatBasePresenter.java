package com.example.anuo.immodule.presenter.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;
import com.example.anuo.immodule.bean.BaseChatReceiveMsg;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.interfaces.iview.base.IChatBaseView;
import com.example.anuo.immodule.manager.SocketManager;
import com.example.anuo.immodule.presenter.ChatPlanNewsPresenter;
import com.example.anuo.immodule.presenter.ChatWinningListPresenter;
import com.example.anuo.immodule.utils.AESUtils;
import com.example.anuo.immodule.utils.LogUtils;
import com.example.anuo.immodule.utils.ToastUtils;
import com.example.anuo.immodule.view.LogingDialog;
import com.google.gson.Gson;

import java.util.Arrays;

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
 * Date  :21/06/2019
 * Desc  :com.example.anuo.immodule.presenter
 */
public class ChatBasePresenter implements Handler.Callback {

    private IChatBaseView iView;
    protected Context context;
    private SocketManager socketManager;
    public Handler handler;
    protected LogingDialog logingDialog;

    public ChatBasePresenter(ChatBaseActivity activity, IChatBaseView view) {
        this.iView = view;
        this.context = activity;
        socketManager = SocketManager.instance(activity);
        handler = new Handler(this);
        logingDialog = new LogingDialog(activity);
    }

    public ChatBasePresenter(Context context, IChatBaseView view) {
        this.iView = view;
        this.context = context;
        socketManager = SocketManager.instance(context);
    }

    public void showToast(String toast) {
        ToastUtils.showToast(context, toast);
    }

    public void showToast(int str) {
        showToast(context.getString(str));
    }

    public void onResume() {

    }

    public void onStop() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    public void sendSocket(String socketKey, String content, final boolean showToast) {
        socketManager.setSendListener(sendListener);
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
        }, 20000);
    }


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
                if (baseChatReceiveMsg == null || TextUtils.isEmpty(baseChatReceiveMsg.getCode())){
                    return;
                }
                switch (baseChatReceiveMsg.getCode()) {
                    case ConfigCons.GET_PLAN_NEWS:
                        message.what = ChatPlanNewsPresenter.GET_PLAN_NEWS;
                        break;
                    case ConfigCons.GET_WINNING_LIST:
                        message.what = ChatWinningListPresenter.GET_WINNING_LIST;
                        break;
                    case ConfigCons.PHOTO_LIST:
                        message.what = ChatWinningListPresenter.GET_PHOTO_LIST;
                        break;
                }
                handler.sendMessage(message);
            }
        }
    };
}
