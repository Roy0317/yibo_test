package com.example.anuo.immodule.view;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.adapter.ChatRoomListGridAdapter;
import com.example.anuo.immodule.bean.ChatRoomListBean;

import java.util.List;

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
 * Date  :26/06/2019
 * Desc  :com.example.anuo.immodule.view
 */
public class ChatRoomListWindow extends PopupWindow implements ChatRoomListGridAdapter.onItemCheckedListener {

    private Context context;
    private List<ChatRoomListBean.SourceBean.DataBean> dataList;
    private GridView gridView;
    private ChatRoomListGridAdapter adapter;

    private String currentRoom = "房间ID";


    public ChatRoomListWindow(Context context, List<ChatRoomListBean.SourceBean.DataBean> dataBeanList) {
        super(context);
        this.context = context;
        this.dataList = dataBeanList;
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(null);
        initView();
    }

    private void initView() {
        View view = View.inflate(context, R.layout.window_chat_room, null);
        gridView = ((GridView) view.findViewById(R.id.gv_chat_room));
        adapter = new ChatRoomListGridAdapter(context, dataList);
        //不需要默认直接选中第一间房，要在成功进入房间之后才给选中状态
//        if (dataList != null && dataList.size() > 0) {
//            currentRoom = dataList.get(0).getId();
//            adapter.setCurrentRoom(currentRoom);
//        }
        adapter.setListener(this);
        gridView.setAdapter(adapter);
        setContentView(view);
    }

    @Override
    public void onItemChecked(int position, boolean isLast) {
        if (listener != null) {
            if (isLast) {
                // 进入更多房间页面
                listener.onMoreChatRoom();
            } else {
                if (!currentRoom.equals(dataList.get(position).getId())) {
                    // 进入聊天室
                    listener.onJoinChatRoom(dataList.get(position), position);
                }
            }
        }
    }

    public void updataClickRoom(String roomId) {
        adapter.setCurrentRoom(roomId);
        adapter.notifyDataSetChanged();
        currentRoom = roomId;
    }

    public void setListener(onSelectListener listener) {
        this.listener = listener;
    }

    private onSelectListener listener;


    public interface onSelectListener {
        void onMoreChatRoom();

        void onJoinChatRoom(ChatRoomListBean.SourceBean.DataBean dataBean, int pos);
    }
}
