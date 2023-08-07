package com.yibo.yiboapp.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.PointExchangeReocrdWraper;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PointExchangeRecordAdapter extends LAdapter<PointExchangeReocrdWraper.ListBean> {
    @Override
    public void convert(int position, LViewHolder holder, ViewGroup parent, PointExchangeReocrdWraper.ListBean item) {

        TextView tv_type = holder.getView(R.id.tv_type);
        TextView tv_before_score =  holder.getView(R.id.tv_before_score);
        TextView tv_change_score = holder.getView(R.id.tv_change_score);
        TextView tv_after_score =  holder.getView(R.id.tv_after_score);
        TextView tv_time =   holder.getView(R.id.tv_time);
        TextView tv_backup =   holder.getView(R.id.tv_backup);


        SpanUtils.with(tv_type).append("变动类型：").setForegroundColor(Color.BLACK).append(getType(item.getType())).setForegroundColor(Color.parseColor("#6dbf6d")).create();
        SpanUtils.with(tv_before_score).append("变动前积分：").setForegroundColor(Color.BLACK).append(item.getBeforeScore()+"").setForegroundColor(Color.parseColor("#6fb7f2")).create();
        SpanUtils.with(tv_change_score).append("变动积分：").setForegroundColor(Color.BLACK).append(item.getScore()+"").setForegroundColor(Color.parseColor("#ff311d")).create();
        SpanUtils.with(tv_after_score).append("变动后积分：").setForegroundColor(Color.BLACK).append(item.getAfterScore()+"").setForegroundColor(Color.parseColor("#37d654")).create();
        tv_time.setText("变动时间："+new SimpleDateFormat().format(new Date(item.getCreateDatetime())));
        tv_backup.setText("备注："+item.getRemark());
    }


    public String getType(int code){
        String[] arrays = mContext.getResources().getStringArray(R.array.point_exchange_record);
        return arrays[code];
    }
}
