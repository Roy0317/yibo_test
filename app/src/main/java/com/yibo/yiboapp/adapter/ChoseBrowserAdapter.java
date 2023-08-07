package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.BrowserInfo;

import java.util.ArrayList;
import java.util.List;

public class ChoseBrowserAdapter extends BaseAdapter {
   List<BrowserInfo> list;
   Context context;
    public ChoseBrowserAdapter(Context context,ArrayList<BrowserInfo> list){
     this.context=context;
     this.list=list;
    }


    @Override
    public int getCount() {
        return list!=null&list.size()>=0?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv_name;
        TextView is_choose;
        ImageView src;
        convertView=LayoutInflater.from(context).inflate(R.layout.item_browser,null);
        tv_name= (TextView) convertView.findViewById(R.id.tv_browsername);
        is_choose= (TextView) convertView.findViewById(R.id.ischose);
        src= (ImageView) convertView.findViewById(R.id.img_browsericon);
        tv_name.setText(list.get(position).getBrowserName()+"");
        src.setBackground(list.get(position).getImageurl());
        if(list.get(position).getSelected()==1){
            is_choose.setVisibility(View.VISIBLE);
        }else{
            is_choose.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
