package com.example.anuo.immodule.interfaces.iview;

import com.example.anuo.immodule.bean.ChatPersonPhotoListBean;
import com.example.anuo.immodule.bean.ChatWinningListBean;
import com.example.anuo.immodule.interfaces.iview.base.IChatBaseView;

public interface IChatWinningListView extends IChatBaseView {

    void onGetWinningList(ChatWinningListBean bean);

    void onGetPhotoList(ChatPersonPhotoListBean chatPersonPhotoListBean);
}
