package com.example.anuo.immodule.interfaces.iview;

import com.example.anuo.immodule.bean.ChatPlanNewsBean;
import com.example.anuo.immodule.interfaces.iview.base.IChatBaseView;

public interface IChatPlanNewsView extends IChatBaseView {

    void onGetPlanNews(ChatPlanNewsBean sourceBean);
}
