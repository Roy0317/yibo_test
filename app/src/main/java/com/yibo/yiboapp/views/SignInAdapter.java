package com.yibo.yiboapp.views;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.BaseRecyclerAdapter;
import com.yibo.yiboapp.ui.SignInBean;
import com.yibo.yiboapp.utils.Utils;


import java.util.List;


public class SignInAdapter extends BaseRecyclerAdapter<SignInBean> {


    public SignInAdapter(Context ctx, List<SignInBean> list) {
        super(ctx, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_sign_in_record, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        ViewHolder holder = (ViewHolder) viewHolder;
        SignInBean bean = getList().get(position);
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(ctx, R.color.white));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color_light_blue));
        }
        holder.signInDate.setText(Utils.getDateFormat(bean.getSignDate(), "yyyy.MM.dd HH:mm:ss"));
        holder.signInDay.setText(bean.getSignDays() + "天");
        holder.signInScore.setText(bean.getScore() + "分");
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView signInDate;
        public TextView signInDay;
        public TextView signInScore;

        public ViewHolder(View itemView) {
            super(itemView);
            signInDate = itemView.findViewById(R.id.signInDate);
            signInDay = itemView.findViewById(R.id.signInDay);
            signInScore = itemView.findViewById(R.id.signInScore);
            itemView.setTag(this);
        }
    }


}
