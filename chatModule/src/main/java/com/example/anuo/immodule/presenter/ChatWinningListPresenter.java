package com.example.anuo.immodule.presenter;

import android.os.Message;
import android.text.TextUtils;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;
import com.example.anuo.immodule.bean.ChatPersonPhotoListBean;
import com.example.anuo.immodule.bean.ChatWinningListBean;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.interfaces.iview.IChatWinningListView;
import com.example.anuo.immodule.interfaces.iview.base.IChatBaseView;
import com.example.anuo.immodule.jsonmodel.ChatWinningListJsonModel;
import com.example.anuo.immodule.jsonmodel.PhotoListModel;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.google.gson.Gson;

public class ChatWinningListPresenter extends ChatBasePresenter {

    public static final int GET_WINNING_LIST = 0x01;
    public static final int GET_PHOTO_LIST = 0x02;
    private IChatWinningListView iView;

    public ChatWinningListPresenter(ChatBaseActivity activity, IChatBaseView view) {
        super(activity, view);
        iView = (IChatWinningListView) view;
    }


    @Override
    public boolean handleMessage(Message msg) {
        String json = (String) msg.obj;
        if (msg.what == GET_WINNING_LIST) {
            ChatWinningListBean chatWinningListBean = new Gson().fromJson(json, ChatWinningListBean.class);
            if (logingDialog != null && logingDialog.isShowing()) {
                logingDialog.dismiss();
            }
            if (!chatWinningListBean.isSuccess()) {
                showToast(TextUtils.isEmpty(chatWinningListBean.getMsg()) ? context.getString(R.string.request_fail) : chatWinningListBean.getMsg());
                return false;
            }
            if (chatWinningListBean.getSource() == null) {
                return false;
            }
            iView.onGetWinningList(chatWinningListBean);
        } else if (msg.what == GET_PHOTO_LIST) {
            ChatPersonPhotoListBean chatPersonPhotoListBean = new Gson().fromJson(json, ChatPersonPhotoListBean.class);
            if (!chatPersonPhotoListBean.isSuccess()) {
                return false;
            }
            if (chatPersonPhotoListBean.getSource() != null && chatPersonPhotoListBean.getSource().getItems().size() > 0) {
                iView.onGetPhotoList(chatPersonPhotoListBean);
            }
        }


        return super.handleMessage(msg);

    }


    /**
     * 中奖榜单
     *
     * @param chatWinningListJsonBean
     */
    public void initData(ChatWinningListJsonModel chatWinningListJsonBean) {
        chatWinningListJsonBean.setSource(ConfigCons.SOURCE);
        chatWinningListJsonBean.setStationId(ChatSpUtils.instance(context).getStationId());
        chatWinningListJsonBean.setCode(ConfigCons.GET_WINNING_LIST);
        String json = new Gson().toJson(chatWinningListJsonBean);
        /*---------Socket发送------------*/
        sendSocket(ConfigCons.USER_R, json, true);
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
        sendSocket(ConfigCons.USER_R, bodyStr, true);

    }
}
