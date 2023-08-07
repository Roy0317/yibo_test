package com.example.anuo.immodule.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anuo.immodule.R;
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
 * Desc  :com.example.anuo.immodule.adapter
 */
public class ChatRoomListGridAdapter extends BaseAdapter {
    private Context context;
    private List<ChatRoomListBean.SourceBean.DataBean> dataList;
    private int[] bgs = {R.drawable.selector_chat_room_bg_red, R.drawable.selector_chat_room_bg_pink, R.drawable.selector_chat_room_bg_blue,
            R.drawable.selector_chat_room_bg_purple, R.drawable.selector_chat_room_bg_orange};

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }

    private String currentRoom = "房间ID";

    public ChatRoomListGridAdapter(Context context, List<ChatRoomListBean.SourceBean.DataBean> dataBeanList) {
        this.context = context;
        this.dataList = dataBeanList;
    }

    @Override
    public int getCount() {
        return dataList.size() >= 7 ? 8 : dataList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_chat_room_list_grid, null);
            viewHolder.relativeLayout = convertView.findViewById(R.id.rl_chat_room);
            viewHolder.imageView = convertView.findViewById(R.id.iv_chat_room);
            viewHolder.textView = convertView.findViewById(R.id.tv_chat_room);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (dataList.size() == 0 || position == 7 || position == dataList.size()) {
            viewHolder.relativeLayout.setBackground(context.getResources().getDrawable(bgs[4]));
            viewHolder.imageView.setImageResource(R.mipmap.icon_more);
            viewHolder.textView.setText("更多房间");
            if (listener != null) {
                viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemChecked(position, true);
                    }
                });
            }
            viewHolder.relativeLayout.setEnabled(true);
        } else {
            if (position <= 3) {
                viewHolder.relativeLayout.setBackground(context.getResources().getDrawable(bgs[position]));
            } else {
                viewHolder.relativeLayout.setBackground(context.getResources().getDrawable(bgs[(position + 1) % 4 - 1]));
            }
            viewHolder.imageView.setImageResource(R.mipmap.icon_diamond);
            viewHolder.textView.setText(dataList.get(position).getName());
            if (listener != null) {
                viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemChecked(position, false);
                    }
                });
            }
            if (currentRoom.equals(dataList.get(position).getId())) {
                viewHolder.relativeLayout.setEnabled(false);
            } else {
                viewHolder.relativeLayout.setEnabled(true);
            }
        }
        return convertView;
    }

    class ViewHolder {
        private RelativeLayout relativeLayout;
        private ImageView imageView;
        private TextView textView;
    }

    public void setListener(onItemCheckedListener listener) {
        this.listener = listener;
    }

    private onItemCheckedListener listener;


    public interface onItemCheckedListener {
        void onItemChecked(int position, boolean isLast);
    }
}
