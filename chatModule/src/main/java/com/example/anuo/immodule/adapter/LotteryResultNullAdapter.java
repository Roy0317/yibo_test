package com.example.anuo.immodule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.anuo.immodule.R;

/*
* 彩票开奖结果为空的时候显示问好球
* */
public class LotteryResultNullAdapter extends BaseAdapter {

    private Context context;

    private int size;

    public LotteryResultNullAdapter(Context context, int size){
        this.context = context;
        this.size = size;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =  LayoutInflater.from(context).inflate(R.layout.item_lottery_result_null, null);
        }
        return convertView;
    }
}
