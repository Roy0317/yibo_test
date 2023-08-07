package com.example.anuo.immodule.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;
import com.example.anuo.immodule.adapter.RoomListAdapter;
import com.example.anuo.immodule.bean.ChatRoomListBean;
import com.example.anuo.immodule.bean.JoinChatRoomBean;
import com.example.anuo.immodule.interfaces.iview.IChatRoomListView;
import com.example.anuo.immodule.presenter.ChatRoomListPresenter;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
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
 * Desc  :com.example.anuo.immodule.activity
 */
public class ChatRoomListActivity extends ChatBaseActivity implements IChatRoomListView {

    RecyclerView recyclerView;
    private ChatRoomListPresenter presenter;
    private RoomListAdapter roomListAdapter;
    private List<ChatRoomListBean.SourceBean.DataBean> roomList = new ArrayList<>();
    private ChatRoomListBean.SourceBean.DataBean agentRoomBean;

    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected int onCreate_(Bundle savedInstanceState) {
        return R.layout.activity_chat_room_list;
    }

    @Override
    protected void initView() {
        super.initView();
        recyclerView = findViewById(R.id.rcy_chat_room);
    }

    @Override
    protected ChatBasePresenter initPresenter() {
        presenter = new ChatRoomListPresenter(this, this);
        return presenter;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        agentRoomBean = this.getIntent().getParcelableExtra("agentRoom");
        tvMiddleTitle.setText("房间列表");
        roomListAdapter = new RoomListAdapter(this, roomList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(roomListAdapter);
        roomListAdapter.setRoomItemClick(new RoomListAdapter.RoomItemClick() {
            @Override
            public void onClick(int position) {
                presenter.joinChatRoom(roomList.get(position).getId(), roomList.get(position).getRoomKey(), roomList.get(position).getName());
            }
        });
        presenter.getChatRoomList(ChatSpUtils.instance(this).getUserId(), ChatSpUtils.instance(this).getStationId());
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onGetChatRoomList(ChatRoomListBean chatRoomListBean) {
        // 默认已经不包含代理房间，如果不包含，则添加进房间列表
        boolean containAgentRoom = false;
        //TODO 暂时屏蔽筛选条件，后期再加
        roomList.addAll(chatRoomListBean.getSource().getData());

        //判断代理房间是否应该存在，不存在的话就添加至最前面
        for (int i = 0; i < chatRoomListBean.getSource().getData().size(); i++) {
            ChatRoomListBean.SourceBean.DataBean dataBean = chatRoomListBean.getSource().getData().get(i);
            if (agentRoomBean != null && dataBean.getId().equals(agentRoomBean.getId())) {
                containAgentRoom = true;
            }
        }
        if (!containAgentRoom && agentRoomBean != null) {
            roomList.add(0, agentRoomBean);
        }
//        if (ChatSpUtils.instance(this).getACCOUNT_TYPE() == 2 || ChatSpUtils.instance(this).getACCOUNT_TYPE() == 3) {
//            roomList.addAll(chatRoomListBean.getSource().getData());
//        } else {
//            List<ChatRoomListBean.SourceBean.DataBean> temp = new ArrayList<>();
//            for (int i = 0; i < chatRoomListBean.getSource().getData().size(); i++) {
//                if (chatRoomListBean.getSource().getData().get(i).getType() != 3) { //普通会员不显示代理房
//                    temp.add(chatRoomListBean.getSource().getData().get(i));
//                }
//            }
//            roomList.addAll(temp);
//        }
        roomListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onJoinChatRoom(JoinChatRoomBean joinChatRoomBean, String oldRoomId) {
        Intent intent = new Intent();
        String room = new Gson().toJson(joinChatRoomBean);
        intent.putExtra("room", room);
        setResult(RESULT_OK, intent);
        finish();
    }
}
