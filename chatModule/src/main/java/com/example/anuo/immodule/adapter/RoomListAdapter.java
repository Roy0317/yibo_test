package com.example.anuo.immodule.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.ChatRoomListBean;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.ChatSysConfig;
import com.example.anuo.immodule.utils.ScreenUtil;
import com.example.anuo.immodule.view.AlwaysMarqueeTextView;

import java.util.List;

public class RoomListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<ChatRoomListBean.SourceBean.DataBean> mData;

    private int morePeople = 0;
    private final ChatSysConfig sysConfig;

    public RoomListAdapter(Context context, List<ChatRoomListBean.SourceBean.DataBean> data) {
        this.context = context;
        this.mData = data;
        sysConfig = ChatSpUtils.instance(context).getChatSysConfig();
        morePeople = Integer.parseInt(sysConfig.getName_room_people_num());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ViewHolder holder = (ViewHolder) viewHolder;
        ChatRoomListBean.SourceBean.DataBean bean = mData.get(i);
        holder.nameTxt.setText(bean.getName());
        holder.idTxt.setText("id: " + bean.getStationId());
        holder.noticeTxt.setText(bean.getRemark());
        if (TextUtils.isEmpty(bean.getRoomKey())) {
            holder.lockImg.setVisibility(View.GONE);
        } else {
            holder.lockImg.setVisibility(View.VISIBLE);
        }
        if (sysConfig.getSwitch_room_people_admin_show().equals("1") && (!"2".equals(ChatSpUtils.instance(context).getUSER_TYPE())
                && !"4".equals(ChatSpUtils.instance(context).getUSER_TYPE()))) {
            holder.peopleTxt.setVisibility(View.GONE);
        } else {
            int num;
            if (bean.getFakePerNum() != 0) {
                num = bean.getFakePerNum() + bean.getRoomUserCount();
            } else {
                num = bean.getRoomUserCount() + morePeople;
            }
            if (num < 10) {
                num = 10;
            }
            holder.peopleTxt.setText(num + "人");
            holder.peopleTxt.setVisibility(View.VISIBLE);
        }
        if (i == 0) {
            holder.mView.setPadding(ScreenUtil.dip2px(context, 8), 0,
                    ScreenUtil.dip2px(context, 8), 0);
        } else {
            holder.mView.setPadding(ScreenUtil.dip2px(context, 10), 0,
                    ScreenUtil.dip2px(context, 10), 0);
        }

        if (i % 3 == 0) {
            holder.bgImg.setImageDrawable(context.getResources().getDrawable(R.drawable.chat_room_2));
        } else if (i % 3 == 1) {
            holder.bgImg.setImageDrawable(context.getResources().getDrawable(R.drawable.chat_room_1));
        } else {
            holder.bgImg.setImageDrawable(context.getResources().getDrawable(R.drawable.chat_room_3));
        }


        if (!TextUtils.isEmpty(bean.getRoomImg())) {
            Glide.with(context)
                    .load(bean.getRoomImg())
                    .into(holder.bgImg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomItemClick.onClick(i);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room_list, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bgImg;
        ImageView lockImg;
        TextView nameTxt;
        TextView idTxt;
        AlwaysMarqueeTextView noticeTxt;
        TextView peopleTxt;
        RelativeLayout mView;

        public ViewHolder(View view) {
            super(view);
            bgImg = view.findViewById(R.id.item_room_list_bg_img);
            lockImg = view.findViewById(R.id.item_room_list_lock_img);
            nameTxt = view.findViewById(R.id.item_room_list_name);
            idTxt = view.findViewById(R.id.item_room_list_id);
            noticeTxt = view.findViewById(R.id.item_room_list_notice);
            peopleTxt = view.findViewById(R.id.item_room_list_people_num);
            mView = view.findViewById(R.id.item_room_list_main);
        }

    }

    public interface RoomItemClick {
        void onClick(int position);
    }

    private RoomItemClick roomItemClick = null;

    public void setRoomItemClick(RoomItemClick roomItemClick) {
        this.roomItemClick = roomItemClick;
    }


}
