package com.example.anuo.immodule.presenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;
import com.example.anuo.immodule.bean.ChatPlanNewsBean;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.interfaces.iview.IChatPlanNewsView;
import com.example.anuo.immodule.interfaces.iview.base.IChatBaseView;
import com.example.anuo.immodule.jsonmodel.ChatPlanNewsJsonModel;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.google.gson.Gson;


public class ChatPlanNewsPresenter extends ChatBasePresenter implements Handler.Callback{

    public static final int GET_PLAN_NEWS = 0x11;
    private IChatPlanNewsView iView;


    public ChatPlanNewsPresenter(ChatBaseActivity activity, IChatBaseView view) {
        super(activity, view);
        iView = (IChatPlanNewsView) view;

    }


    @Override
    public boolean handleMessage(Message msg) {
        String json = (String) msg.obj;
        if (msg.what == GET_PLAN_NEWS) {
            ChatPlanNewsBean chatPlanNewsBean = new Gson().fromJson(json, ChatPlanNewsBean.class);
            if (logingDialog != null && logingDialog.isShowing()) {
                logingDialog.dismiss();
            }
            if (!chatPlanNewsBean.isSuccess()) {
                showToast(TextUtils.isEmpty(chatPlanNewsBean.getMsg()) ? context.getString(R.string.request_fail) : chatPlanNewsBean.getMsg());
                return false;
            }
            if (chatPlanNewsBean.getSource() == null) {
                return false;
            }
            iView.onGetPlanNews(chatPlanNewsBean);
        }
        return false;
    }


    /**
     * 获取彩票计划消息
     *
     * @param chatPlanNewsJsonModel
     */
    public void initData(ChatPlanNewsJsonModel chatPlanNewsJsonModel) {
        chatPlanNewsJsonModel.setStationId(ChatSpUtils.instance(context).getStationId());
        chatPlanNewsJsonModel.setSource(ConfigCons.SOURCE);
        chatPlanNewsJsonModel.setCode(ConfigCons.GET_PLAN_NEWS);
        String json = new Gson().toJson(chatPlanNewsJsonModel);
        /*---------Socket发送------------*/
        if (context instanceof Activity){
            if (!((Activity)context).isFinishing()){
                logingDialog.updateTitle("获取彩票计划消息...");
            }
        }

        sendSocket(ConfigCons.USER_R, json, true);
    }

}
