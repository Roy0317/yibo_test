package com.example.anuo.immodule.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.ChatRoomListActivity;
import com.example.anuo.immodule.bean.BaseChatReceiveMsg;
import com.example.anuo.immodule.bean.ChatRoomListBean;
import com.example.anuo.immodule.bean.JoinChatRoomBean;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.interfaces.iview.IChatRoomListView;
import com.example.anuo.immodule.jsonmodel.ChangeRoomModel;
import com.example.anuo.immodule.jsonmodel.ChatRoomListJsonModel;
import com.example.anuo.immodule.jsonmodel.JoinChatRoomJsonModel;
import com.example.anuo.immodule.manager.SocketManager;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.AESUtils;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.utils.LogUtils;
import com.example.anuo.immodule.utils.ToastUtils;
import com.example.anuo.immodule.view.PayPsdInputView;
import com.example.anuo.immodule.view.PsdDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;
import io.socket.client.Ack;
import io.socket.emitter.Emitter;

public class ChatRoomListPresenter extends ChatBasePresenter implements SessionResponse.Listener<CrazyResult<Object>> {

    private final SocketManager socketManager;
    private ChatRoomListActivity activity;
    private IChatRoomListView iView;

    private PsdDialog psdDialog;
    private String oldRoomId;


    public ChatRoomListPresenter(ChatRoomListActivity context, IChatRoomListView iView) {
        super(context, iView);
        this.activity = context;
        this.iView = iView;
        oldRoomId = context.getIntent().getStringExtra("oldRoomId");
        socketManager = SocketManager.instance(activity);
    }

    private JoinChatRoomBean joinChatRoomBean;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            switch (msg.what) {
                case ChatMainPresenter.CHAT_ROOM_LIST_REQUEST:
                    ChatRoomListBean chatRoomListBean = new Gson().fromJson(json, ChatRoomListBean.class);
                    if (!chatRoomListBean.isSuccess()) {
                        showToast(Utils.isEmptyString(chatRoomListBean.getMsg()) ? activity.getString(R.string.get_chat_room_list_fail)
                                : chatRoomListBean.getMsg());
                        return;
                    }
                    if (chatRoomListBean.getSource().getData().isEmpty()) {
                        showToast(R.string.novalid_room);
                        return;
                    }
                    iView.onGetChatRoomList(chatRoomListBean);
                    break;
                case ChatMainPresenter.JOIN_CHAT_ROOM_REQUEST:
                    if (psdDialog != null && psdDialog.isShowing()) {
                        psdDialog.dismiss();
                        psdDialog = null;
                    }
                    joinChatRoomBean = new Gson().fromJson(json, JoinChatRoomBean.class);
                    if (!joinChatRoomBean.isSuccess()) {
                        showToast(Utils.isEmptyString(joinChatRoomBean.getMsg()) ? activity.getString(R.string.join_chat_room_fail)
                                : joinChatRoomBean.getMsg());
                        return;
                    }
                    changeRoom(oldRoomId, joinChatRoomBean.getSource().getRoomId(),
                            joinChatRoomBean.getSource().getNickName(), joinChatRoomBean.getSource().getAccount());
                    break;
                case ChatMainPresenter.JOIN_CHAT_ROOM_SUCCESS:
                    BaseChatReceiveMsg baseChatReceiveMsg = new Gson().fromJson(json, BaseChatReceiveMsg.class);
                    if (!baseChatReceiveMsg.isSuccess()) {
                        showToast(Utils.isEmptyString(baseChatReceiveMsg.getMsg()) ? activity.getString(R.string.join_chat_room_fail)
                                : baseChatReceiveMsg.getMsg());
                        return;
                    }
                    oldRoomId = baseChatReceiveMsg.getRoomId();
                    iView.onJoinChatRoom(joinChatRoomBean, oldRoomId);
                    break;
            }
        }
    };

    /**
     * 1、获取聊天室列表
     *
     * @param userId
     * @param stationId
     */
    public void getChatRoomList(String userId, String stationId) {
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
        ChatRoomListJsonModel jsonModel = new ChatRoomListJsonModel(ConfigCons.CHAT_ROOM_LIST, ConfigCons.SOURCE, userId, stationId
                , ChatSpUtils.instance(activity).getAGENT_USER(), ChatSpUtils.instance(activity).getSWITCH_AGENT_PERMISSION());
        String bodyStr = new Gson().toJson(jsonModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
        /*------------------------------*/
//        CrazyRequest<CrazyResult<ChatRoomListBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(ChatMainPresenter.CHAT_ROOM_LIST_REQUEST)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(bodyStr)
//                .contentType("application/json;utf-8")
//                .placeholderText(activity.getString(R.string.get_chat_room_list_ongoing))
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<ChatRoomListBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }


    /**
     * 2、进入聊天室
     *
     * @param id
     * @param roomKey
     */
    public void joinChatRoom(final String id, final String roomKey,String roomName) {
        if (TextUtils.isEmpty(roomKey)) {
            joinRoom(id, roomKey);
        } else {
            final String userId = ChatSpUtils.instance(activity).getUserId();
            String roomPasswordData = ChatSpUtils.instance(activity).getRoomPasswordData(userId + "," + id);
            //如果之前保存了密码直接进入房间
            if (!TextUtils.isEmpty(roomPasswordData) && roomPasswordData.equalsIgnoreCase(roomKey)) {
                joinRoom(id, roomKey);
                return;
            }
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
                    joinRoom(id, roomKey);
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

    private void joinRoom(String id, String roomKey) {
        String userId = ChatSpUtils.instance(activity).getUserId();
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
        JoinChatRoomJsonModel jsonModel = new JoinChatRoomJsonModel(ConfigCons.JOIN_CHAT_ROOM, ConfigCons.SOURCE, userId, id, roomKey);
//        if (ChatSpUtils.instance(activity).getACCOUNT_TYPE() == 2 || ChatSpUtils.instance(activity).getACCOUNT_TYPE() == 3) {
        jsonModel.setAgentUserCode(ChatSpUtils.instance(activity).gettAGENT_USER_CODE());
//        }
        String bodyStr = new Gson().toJson(jsonModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, false, true, "");
        /*------------------------------*/
//        CrazyRequest<CrazyResult<JoinChatRoomBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(ChatMainPresenter.JOIN_CHAT_ROOM_REQUEST)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(bodyStr)
//                .contentType("application/json;utf-8")
//                .placeholderText(activity.getString(R.string.join_chat_room_ongoing))
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<JoinChatRoomBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
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
        if (socketKey.equals(ConfigCons.USER_JOIN_ROOM) || socketKey.equals(ConfigCons.LOGIN)|| socketKey.equals(ConfigCons.USER_JOIN_GROUP)) {
            str = AESUtils.encrypt(content, ConfigCons.DEFAULT_KEY, ConfigCons.DEFAULT_IV);
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
                    if (showToast) {
                        ToastUtils.showToast(activity, R.string.time_out);
                    }
                }
            }
        }, 15000);
    }


    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (activity.isFinishing() || response == null) {
            return;
        }
        CrazyResult<Object> result = response.result;
        switch (response.action) {
            case ChatMainPresenter.CHAT_ROOM_LIST_REQUEST:
                if (result == null) {
                    showToast(R.string.get_chat_room_list_fail);
                    return;
                }
                if (!result.crazySuccess) {
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? activity.getString(R.string.get_chat_room_list_fail) : errorString);
                    return;
                }

                ChatRoomListBean chatRoomListBean = (ChatRoomListBean) result.result;
                if (!chatRoomListBean.isSuccess()) {
                    showToast(Utils.isEmptyString(chatRoomListBean.getMsg()) ? activity.getString(R.string.get_chat_room_list_fail)
                            : chatRoomListBean.getMsg());
                    return;
                }
                if (chatRoomListBean.getSource().getData().isEmpty()) {
                    showToast(R.string.room_list_empty);
                    return;
                }
                iView.onGetChatRoomList(chatRoomListBean);
                break;
            case ChatMainPresenter.JOIN_CHAT_ROOM_REQUEST: //加入房间
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
                iView.onJoinChatRoom(joinChatRoomBean, oldRoomId);
                break;
        }
    }

    //--------------------------------SocketIO监听-----------------------------------

    /**
     * 切换和加入房间监听
     */
    private Emitter.Listener joinRoomListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String json = AESUtils.decrypt(String.valueOf(args[0]), ConfigCons.DEFAULT_KEY, ConfigCons.DEFAULT_IV);
            LogUtils.e("a", "on joinRoom success:" + json);
            Message message = Message.obtain();
            message.what = ChatMainPresenter.JOIN_CHAT_ROOM_SUCCESS;
            message.obj = json;
            handler.sendMessage(message);
        }
    };

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
                    case ConfigCons.CHAT_ROOM_LIST:
                        message.what = ChatMainPresenter.CHAT_ROOM_LIST_REQUEST;
                        break;
                    case ConfigCons.JOIN_CHAT_ROOM:
                        message.what = ChatMainPresenter.JOIN_CHAT_ROOM_REQUEST;
                        break;
                }
                handler.sendMessage(message);
            }
        }
    };
}
