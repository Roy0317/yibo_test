package com.example.anuo.immodule.interfaces.iview;

import com.example.anuo.immodule.bean.ChatRoomListBean;
import com.example.anuo.immodule.bean.JoinChatRoomBean;
import com.example.anuo.immodule.interfaces.iview.base.IChatBaseView;

public interface IChatRoomListView extends IChatBaseView {

    void onGetChatRoomList(ChatRoomListBean chatRoomListBean);

    void onJoinChatRoom(JoinChatRoomBean joinChatRoomBean, String oldRoomId);
}
