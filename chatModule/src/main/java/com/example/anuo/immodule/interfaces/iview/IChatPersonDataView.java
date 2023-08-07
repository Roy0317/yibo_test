package com.example.anuo.immodule.interfaces.iview;

import com.example.anuo.immodule.bean.ChatPersonPhotoListBean;
import com.example.anuo.immodule.bean.ChatPersonDataBean;
import com.example.anuo.immodule.interfaces.iview.base.IChatBaseView;

public interface IChatPersonDataView extends IChatBaseView {

    void getPhotoList(ChatPersonPhotoListBean chatPersonPhotoListBean);

    void onPhotoUploaded(boolean isSuccess, String photoUrl);

    void getPersonData(ChatPersonDataBean chatPersonDataBean);

    void ModifyPersonData(boolean isSuc);

}
