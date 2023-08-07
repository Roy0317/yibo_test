package com.example.anuo.immodule.presenter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.PersonDataActivity;
import com.example.anuo.immodule.bean.BaseChatReceiveMsg;
import com.example.anuo.immodule.bean.ChatModifyPersonDataBean;
import com.example.anuo.immodule.bean.ChatPersonDataBean;
import com.example.anuo.immodule.bean.ChatPersonPhotoListBean;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.interfaces.iview.IChatPersonDataView;
import com.example.anuo.immodule.jsonmodel.ModifyPersonDataModel;
import com.example.anuo.immodule.jsonmodel.PersonDataModel;
import com.example.anuo.immodule.jsonmodel.PhotoListModel;
import com.example.anuo.immodule.jsonmodel.UploadAvatarModel;
import com.example.anuo.immodule.jsonmodel.UploadAvatarResponse;
import com.example.anuo.immodule.jsonmodel.UploadAvatarToDBResponse;
import com.example.anuo.immodule.manager.SocketManager;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.AESUtils;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.utils.LogUtils;
import com.example.anuo.immodule.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

public class ChatPersonDataPresenter extends ChatBasePresenter implements SessionResponse.Listener<CrazyResult<Object>> {

    private final int GET_PHOTO_LIST = 0x10;
    private final int GET_PERSON_DATA = 0x11; //获取用户资料
    private final int MODIFY_PERSON_DATA = 0x12; //修改用户资料
    private final int UPDATE_PERSON_BET_DATA = 0x13; //刷新用户数据
    private final int UPLOAD_PHOTO = 0x14; //上傳自定義頭像
    private final int UPLOAD_PHOTO_TO_DB = 0x15; //上傳自定義頭像

    private final SocketManager socketManager;
    private PersonDataActivity activity;
    private IChatPersonDataView iView;

    private boolean isRefresh = false; //用户是否在刷新数据

    public ChatPersonDataPresenter(PersonDataActivity activity, IChatPersonDataView iView) {
        super(activity, iView);
        this.activity = activity;
        this.iView = iView;
        socketManager = SocketManager.instance(activity);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            switch (msg.what) {
                case GET_PHOTO_LIST:
                    ChatPersonPhotoListBean chatPersonPhotoListBean = new Gson().fromJson(json, ChatPersonPhotoListBean.class);
                    if (!chatPersonPhotoListBean.isSuccess()) {
                        return;
                    }
                    if (chatPersonPhotoListBean.getSource() != null && chatPersonPhotoListBean.getSource().getItems().size() > 0) {
                        iView.getPhotoList(chatPersonPhotoListBean);
                    }
                    break;
                case UPLOAD_PHOTO_TO_DB:
                    Utils.logd("ChatPersonData", "json = " + json);
                    UploadAvatarToDBResponse response = new Gson().fromJson(json, UploadAvatarToDBResponse.class);
                    if(!response.isSuccess()){
                        iView.onPhotoUploaded(false, "");
                    }else {
                        String url = "";
                        if(response.getSource() != null && !Utils.isEmptyString(response.getSource().getAvatar())){
                            url = response.getSource().getAvatar();
                        }

                        iView.onPhotoUploaded(!Utils.isEmptyString(url), url);
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
                case UPDATE_PERSON_BET_DATA:

                    break;
            }
        }
    };

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
        sendSocket(ConfigCons.USER_R, bodyStr, true);
        /*------------------------------*/
//        CrazyRequest<CrazyResult<ChatPersonPhotoListBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(GET_PHOTO_LIST)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(bodyStr)
//                .contentType("application/json;utf-8")
//                .placeholderText(activity.getString(R.string.loading))
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<ChatPersonPhotoListBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }

    public void uploadAvatarPhoto(String photo, String photoType){
        String fileType;
        String type;
        switch (photoType){
            case "image/gif":
                fileType = ".gif";
                type = "0";
                break;
            case "image/jpeg":
                fileType = ".jpeg";
                type = "1";
                break;
            case "image/png":
                fileType = ".png";
                type = "2";
                break;
            default:
                fileType = ".jpg";
                type = "3";
                break;
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(ConfigCons.CHAT_FILE_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.UPLOAD_AVATAR)
                    .append("?fileType=").append(URLEncoder.encode(fileType, "utf-8"))
                    .append("&type=").append(URLEncoder.encode(type, "utf-8"))
                    .append("&fileString=").append(URLEncoder.encode(photo, "utf-8"));

            CrazyRequest<CrazyResult<UploadAvatarResponse>> request = new AbstractCrazyRequest.Builder()
                    .url(sb.toString())
                    .seqnumber(UPLOAD_PHOTO)
                    .headers(CommonUtils.getChatHeader(activity))
                    .shouldCache(false)
                    .placeholderText(activity.getString(R.string.loading))
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<UploadAvatarResponse>() {}.getType()))
                    .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                    .create();
            RequestManager.getInstance().startRequest(activity, request, this);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    private void uploadPhotoToDB(String fileCode){
        String userID = ChatSpUtils.instance(context).getUserId();
        String stationID = ChatSpUtils.instance(context).getStationId();
        UploadAvatarModel jsonModel = new UploadAvatarModel(ConfigCons.UPLOAD_PHOTO_TO_DB,
                ConfigCons.SOURCE, stationID, userID, fileCode);
        String bodyStr = new Gson().toJson(jsonModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, true);
    }

    /**
     * 获取用户资料
     */
    public void getPersonData(String userId, String roomId, boolean isRefresh) {
        this.isRefresh = isRefresh;
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
        //PhotoListModel jsonModel = new PhotoListModel(ConfigCons.PERSON_DATA, ConfigCons.SOURCE, stationId, userId);
        PersonDataModel personDataModel = new PersonDataModel(ConfigCons.PERSON_DATA, ConfigCons.SOURCE, userId, roomId);
        String bodyStr = new Gson().toJson(personDataModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, true);
        /*------------------------------*/
//        CrazyRequest<CrazyResult<ChatPersonDataBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(GET_PERSON_DATA)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(bodyStr)
//                .contentType("application/json;utf-8")
//                .placeholderText(activity.getString(R.string.loading))
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<ChatPersonDataBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }


    /**
     * 修改用户资料
     */
    public void modifyPersonData(ModifyPersonDataModel modifyPersonDataModel) {
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
        String bodyStr = new Gson().toJson(modifyPersonDataModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, true);
        /*------------------------------*/
//        CrazyRequest<CrazyResult<ChatModifyPersonDataBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(MODIFY_PERSON_DATA)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(bodyStr)
//                .contentType("application/json;utf-8")
//                .placeholderText("修改资料中")
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<ChatModifyPersonDataBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }


    /**
     * 刷新用户数据
     */
    public void updateBetData(String userId, String roomId) {
        StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.LOGIN_CHAT_URL);
        //PhotoListModel jsonModel = new PhotoListModel(ConfigCons.PERSON_DATA, ConfigCons.SOURCE, stationId, userId);
        PersonDataModel personDataModel = new PersonDataModel(ConfigCons.UPDATE_PERSON_BET_DATA, ConfigCons.SOURCE, userId, roomId);
        String bodyStr = new Gson().toJson(personDataModel);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, bodyStr, true);
        /*------------------------------*/
//        CrazyRequest<CrazyResult<ChatPersonDataBean>> request = new AbstractCrazyRequest.Builder().
//                url(urls.toString())
//                .seqnumber(UPDATE_PERSON_BET_DATA)
//                .headers(CommonUtils.getChatHeader(activity))
//                .body(bodyStr)
//                .contentType("application/json;utf-8")
//                .placeholderText(activity.getString(R.string.loading))
//                .execMethod(CrazyRequest.ExecuteMethod.BODY.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<ChatPersonDataBean>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(activity, request, this);
    }


    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (activity.isFinishing() || response == null) {
            return;
        }
        CrazyResult<Object> result = response.result;
        switch (response.action) {
            case GET_PHOTO_LIST:
                if (result == null) {
                    return;
                }
                if (!result.crazySuccess) {
                    return;
                }
                ChatPersonPhotoListBean chatPersonPhotoListBean = (ChatPersonPhotoListBean) result.result;
                if (chatPersonPhotoListBean.getSource() != null && chatPersonPhotoListBean.getSource().getItems().size() > 0) {
                    iView.getPhotoList(chatPersonPhotoListBean);
                }
                break;
            case UPLOAD_PHOTO:
                if (result == null || !result.crazySuccess) {
                    iView.onPhotoUploaded(false, "");
                    return;
                }
                UploadAvatarResponse avatarResponse = (UploadAvatarResponse) result.result;
                Utils.logd("ChatPersonData", "uploadAvatarPhoto, response = " + avatarResponse);
                if(response.isSuccess() && !Utils.isEmptyString(avatarResponse.getFileCode())){
                    uploadPhotoToDB(avatarResponse.getFileCode());
                }else {
                    iView.onPhotoUploaded(false, "");
                }
                break;
            case GET_PERSON_DATA:
                if (result == null) {
                    showToast(getMsg());
                    return;
                }
                if (!result.crazySuccess) {
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? getMsg() : errorString);
                    return;
                }
                ChatPersonDataBean chatPersonDataBean = (ChatPersonDataBean) result.result;
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
                if (result == null) {
                    showToast("修改当前用户资料失败");
                    return;
                }
                if (!result.crazySuccess) {
                    String errorString = CommonUtils.parseResponseResult(result.error);
                    showToast(TextUtils.isEmpty(errorString) ? "修改当前用户资料失败" : errorString);
                    return;
                }

                ChatModifyPersonDataBean chatModifyPersonDataBean = (ChatModifyPersonDataBean) result.result;
                if (chatModifyPersonDataBean.isSuccess()) {
                    iView.ModifyPersonData(true);
                } else {
                    showToast(TextUtils.isEmpty(chatModifyPersonDataBean.getMsg()) ? "修改当前用户资料失败" : chatModifyPersonDataBean.getMsg());
                    return;
                }
                break;
            case UPDATE_PERSON_BET_DATA:

                break;
        }
    }

    public String getMsg() {
        if (isRefresh) {
            return "刷新数据失败";
        }
        return "获取当前用户资料失败";
    }

    /**
     * 使用Socket发送消息
     *
     * @param socketKey Socket事件Key
     * @param content   发送的内容
     */
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
                        ToastUtils.showToast(activity, R.string.time_out);
                    }
                }
            }
        }, 15000);
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
                if (baseChatReceiveMsg == null || TextUtils.isEmpty(baseChatReceiveMsg.getCode())){
                    return;
                }
                switch (baseChatReceiveMsg.getCode()) {
                    case ConfigCons.PHOTO_LIST:
                        message.what = GET_PHOTO_LIST;
                        break;
                    case ConfigCons.UPLOAD_PHOTO_TO_DB:
                        message.what = UPLOAD_PHOTO_TO_DB;
                        break;
                    case ConfigCons.PERSON_DATA:
                        message.what = GET_PERSON_DATA;
                        break;
                    case ConfigCons.MODIFY_PERSON_DATA:
                        message.what = MODIFY_PERSON_DATA;
                        break;
                    case ConfigCons.UPDATE_PERSON_BET_DATA:
                        message.what = UPDATE_PERSON_BET_DATA;
                        break;
                }
                handler.sendMessage(message);
            }
        }
    };

}
