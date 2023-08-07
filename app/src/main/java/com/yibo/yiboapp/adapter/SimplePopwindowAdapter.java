package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.entify.PlayItem;
import com.yibo.yiboapp.entify.SimplePageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Ray
 * created on 2018/12/6
 * description :
 */
public class SimplePopwindowAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<LotteryData> list;

    public SimplePopwindowAdapter(Context context, ArrayList<LotteryData> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(ArrayList<LotteryData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context, R.layout.item_fragment_simple_pop_window, null);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_lottery_type);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.textView.setText(list.get(position).getName());
        holder.textView.setActivated(list.get(position).isSelect());
        return convertView;
    }


    class Holder {
        public TextView textView;
    }
}
